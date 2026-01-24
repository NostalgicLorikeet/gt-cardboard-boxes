package nostalgic.cardboardboxes.common.metatileentities.storage;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.unification.material.Material;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.items.MetaItems;
import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nostalgic.cardboardboxes.config.Config;
import org.apache.commons.lang3.tuple.Pair;
import nostalgic.cardboardboxes.client.renderer.texture.CardboardTextures;

import static gregtech.api.capability.GregtechDataCodes.IS_TAPED;

public class MetaTileEntityBox extends MetaTileEntityCrate {
    private final Material material;
    private final int inventorySize;
    private boolean isTaped;
    private final String TAPED_NBT = "Taped";
    private static final boolean boxKeepTapeOnPlace = (Config.boxKeepTapeOnPlace && !Config.boxNoTape);
    private static final boolean boxNoTape = (Config.boxNoTape);

    public MetaTileEntityBox(ResourceLocation metaTileEntityId, Material material, int inventorySize) {
        super(metaTileEntityId, material, inventorySize);
        this.material = material;
        this.inventorySize = inventorySize;
        if (boxNoTape) {
            isTaped = true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Pair<TextureAtlasSprite, Integer> getParticleTexture() {
        return Pair.of(CardboardTextures.CARDBOARD_CRATE.getParticleTexture(), getPaintingColorForRendering());
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityBox(metaTileEntityId, material, inventorySize);
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        CardboardTextures.CARDBOARD_CRATE.render(renderState, translation, GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()), pipeline);
        if (!boxNoTape) {
            boolean taped = isTaped;
            if (renderContextStack != null && renderContextStack.getTagCompound() != null) {
                NBTTagCompound tag = renderContextStack.getTagCompound();
                if (tag.hasKey(TAPED_NBT) && tag.getBoolean(TAPED_NBT)) {
                    taped = true;
                }
            }
            if (taped) {
                Textures.TAPED_OVERLAY.render(renderState, translation, pipeline);
            }
        }
    }

    @Override
    public void clearMachineInventory(NonNullList<ItemStack> itemBuffer) {
        if (!isTaped) {
            clearInventory(itemBuffer, inventory);
        }
    }

    @Override
    public boolean onRightClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing,
                                CuboidRayTraceResult hitResult) {
        ItemStack stack = playerIn.getHeldItem(hand);

        boolean playerHoldingTape = (stack.isItemEqual(MetaItems.DUCT_TAPE.getStackForm()) || stack.isItemEqual(MetaItems.BASIC_TAPE.getStackForm()));
        boolean playerSneaking = playerIn.isSneaking();

        if (!boxNoTape) {
            if (playerSneaking) {
                if (!isTaped) {
                    if (playerHoldingTape) {
                        if (!playerIn.isCreative()) {
                            stack.shrink(1);
                        }
                        isTaped = true;
                        if (!getWorld().isRemote) {
                            writeCustomData(IS_TAPED, buf -> buf.writeBoolean(isTaped));
                            markDirty();
                        }
                        return true;
                    }
                } else if (boxKeepTapeOnPlace && isTaped) {
                    isTaped = false;
                    if (!getWorld().isRemote) {
                        writeCustomData(IS_TAPED, buf -> buf.writeBoolean(isTaped));
                        markDirty();
                    }
                    return true;
                }
            } else if (boxKeepTapeOnPlace && isTaped) {
                return true;
            }
        }
        return super.onRightClick(playerIn, hand, facing, hitResult);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setTag("Inventory", inventory.serializeNBT());
        data.setBoolean(TAPED_NBT, isTaped);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.inventory.deserializeNBT(data.getCompoundTag("Inventory"));
        if (data.hasKey(TAPED_NBT)) {
            this.isTaped = (data.getBoolean(TAPED_NBT) || boxNoTape);
        }
    }

    @Override
    public void initFromItemStackData(NBTTagCompound data) {
        super.initFromItemStackData(data);
        if (data.hasKey(TAG_KEY_PAINTING_COLOR)) {
            this.setPaintingColor(data.getInteger(TAG_KEY_PAINTING_COLOR));
        }
        this.isTaped = boxNoTape || data.getBoolean(TAPED_NBT);
        if (isTaped) {
            this.inventory.deserializeNBT(data.getCompoundTag("Inventory"));
        }

        data.removeTag(TAPED_NBT);
        data.removeTag(TAG_KEY_PAINTING_COLOR);

        this.isTaped = boxNoTape;
    }

    @Override
    public void writeItemStackData(NBTTagCompound data) {
        super.writeItemStackData(data);

        // Account for painting color when breaking the crate
        if (this.isPainted()) {
            data.setInteger(TAG_KEY_PAINTING_COLOR, this.getPaintingColor());
        }
        // Don't write tape NBT if not taped, to stack with ones from JEI
        // i could prolly simply this but idk if the second data.setBoolean would get messed up by some other weird thing so its this
        if (!boxNoTape) {
            if (isTaped) {
                data.setBoolean(TAPED_NBT, isTaped);
                data.setTag("Inventory", inventory.serializeNBT());
            }
        } else {
            if (!isInventoryEmpty()) {
                data.setBoolean(TAPED_NBT, true);
                data.setTag("Inventory", inventory.serializeNBT());
            } else {
                data.setBoolean(TAPED_NBT, false);
            }
        }
    }

    @Override
    public void receiveCustomData(int dataId, PacketBuffer buf) {
        super.receiveCustomData(dataId, buf);

        if (dataId == IS_TAPED) {
            this.isTaped = buf.readBoolean();
            scheduleRenderUpdate();
            markDirty();
        }
    }

    public boolean isInventoryEmpty() {
        for (int i = 0; i<inventory.getSlots(); i+=1) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
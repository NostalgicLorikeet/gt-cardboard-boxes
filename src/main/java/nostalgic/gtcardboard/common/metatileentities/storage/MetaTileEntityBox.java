package nostalgic.gtcardboard.common.metatileentities.storage;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.SlotWidget;
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
import nostalgic.gtcardboard.IMixinCrate;
import nostalgic.gtcardboard.common.CardboardBoxConfigHolder;
import org.apache.commons.lang3.tuple.Pair;
import nostalgic.gtcardboard.client.renderer.texture.CardboardTextures;
import org.jetbrains.annotations.NotNull;

import static gregtech.api.capability.GregtechDataCodes.IS_TAPED;
import static nostalgic.gtcardboard.common.CardboardBoxConfigHolder.box;

public class MetaTileEntityBox extends MetaTileEntityCrate {
    private final Material material;
    private final int inventorySize;
    //private boolean isTaped;
    private final String TAPED_NBT = "Taped";
    //private static final boolean boxKeepTapeOnPlace = (Config.boxKeepTapeOnPlace && !Config.boxNoTape);
    //private static final boolean boxNoTape = (Config.boxNoTape);
    IMixinCrate mixinme = (IMixinCrate) this;

    public MetaTileEntityBox(ResourceLocation metaTileEntityId, Material material, int inventorySize) {
        super(metaTileEntityId, material, inventorySize);
        this.material = material;
        this.inventorySize = inventorySize;
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
        boolean taped = mixinme.isTaped();
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

    @Override
    public void clearMachineInventory(NonNullList<ItemStack> itemBuffer) {
        if (!mixinme.isTaped()) {
            clearInventory(itemBuffer, inventory);
        }
    }

    @Override
    public boolean onRightClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing,
                                CuboidRayTraceResult hitResult) {
        ItemStack stack = playerIn.getHeldItem(hand);
        if (playerIn.isSneaking() && !mixinme.isTaped()) {
            if (stack.isItemEqual(MetaItems.DUCT_TAPE.getStackForm()) ||
                    stack.isItemEqual(MetaItems.BASIC_TAPE.getStackForm())) {
                if (!playerIn.isCreative()) {
                    stack.shrink(1);
                }
                mixinme.setTaped(true);
                if (!getWorld().isRemote) {
                    writeCustomData(IS_TAPED, buf -> buf.writeBoolean(mixinme.isTaped()));
                    markDirty();
                }
                return true;
            }
        }
        return super.onRightClick(playerIn, hand, facing, hitResult);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setTag("Inventory", inventory.serializeNBT());
        data.setBoolean(TAPED_NBT, mixinme.isTaped());
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.inventory.deserializeNBT(data.getCompoundTag("Inventory"));
        if (data.hasKey(TAPED_NBT)) {
            mixinme.setTaped(data.getBoolean(TAPED_NBT));
        }
    }

    @Override
    public void initFromItemStackData(NBTTagCompound data) {
        super.initFromItemStackData(data);
        if (data.hasKey(TAG_KEY_PAINTING_COLOR)) {
            this.setPaintingColor(data.getInteger(TAG_KEY_PAINTING_COLOR));
        }
        mixinme.setTaped(data.getBoolean(TAPED_NBT));
        if (mixinme.isTaped()) {
            this.inventory.deserializeNBT(data.getCompoundTag("Inventory"));
        }

        data.removeTag(TAPED_NBT);
        data.removeTag(TAG_KEY_PAINTING_COLOR);

        mixinme.setTaped(false);
    }

    @Override
    public void writeItemStackData(NBTTagCompound data) {
        super.writeItemStackData(data);

        // Account for painting color when breaking the crate
        if (this.isPainted()) {
            data.setInteger(TAG_KEY_PAINTING_COLOR, this.getPaintingColor());
        }
        // Don't write tape NBT if not taped, to stack with ones from JEI
        if (mixinme.isTaped()) {
            data.setBoolean(TAPED_NBT, mixinme.isTaped());
            data.setTag("Inventory", inventory.serializeNBT());
        }
    }

    @Override
    public void receiveCustomData(int dataId, PacketBuffer buf) {
        super.receiveCustomData(dataId, buf);

        if (dataId == IS_TAPED) {
            mixinme.setTaped(buf.readBoolean());
            scheduleRenderUpdate();
            markDirty();
        }
    }

    @Override
    public float getBlockHardness() {
        return box.boxesBreakQuick ? 0.4F : super.getBlockHardness() ;
    }

    @Override
    public float getBlockResistance() {
        return box.boxesBreakQuick ? 0.4F : super.getBlockHardness() ;
    }

    //@Override
    //public void writeInitialSyncData(@NotNull PacketBuffer buf) {
    //    super.writeInitialSyncData(buf);
    //    buf.writeBoolean(mixinme.isTaped());
    //}

    //@Override
    //public void receiveInitialSyncData(@NotNull PacketBuffer buf) {
    //    super.receiveInitialSyncData(buf);
    //    mixinme.setTaped(buf.readBoolean());
    //}
}
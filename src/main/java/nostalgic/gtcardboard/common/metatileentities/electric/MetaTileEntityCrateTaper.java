package nostalgic.gtcardboard.common.metatileentities.electric;

import gregtech.api.GTValues;
import gregtech.api.block.machines.BlockMachine;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.items.itemhandlers.GTItemStackHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.TieredMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.RenderUtil;

import gregtech.common.items.MetaItems;
import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import gregtech.core.sound.GTSoundEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import nostalgic.gtcardboard.IMixinCrate;
import nostalgic.gtcardboard.client.renderer.texture.CardboardTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static gregtech.api.capability.GregtechDataCodes.IS_TAPED;
import static gregtech.api.capability.GregtechDataCodes.UPDATE_OUTPUT_FACING;
import static gregtech.api.util.GTUtility.getMetaTileEntity;

public class MetaTileEntityCrateTaper extends TieredMetaTileEntity {

    private EnumFacing outputFacing;
    private int cooldown;
    private final int cooldownTime;

    public MetaTileEntityCrateTaper(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, tier);
        initializeInventory();
        cooldownTime = 60/(tier+1);
        cooldown = 0;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityCrateTaper(metaTileEntityId, getTier());
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        CardboardTextures.TAPER_OVERLAY.renderOrientedState(renderState, translation, pipeline, getFrontFacing(), false,
                false);
        Textures.PIPE_OUT_OVERLAY.renderSided(getOutputFacing(), renderState,
                RenderUtil.adjustTrans(translation, getOutputFacing(), 2), pipeline);
    }

    @Override
    public void update() {
        super.update();
        World world = getWorld();
        if (!world.isRemote) {
            if (cooldown > 0) {
                cooldown--;
            }
            if (isBlockRedstonePowered() && cooldown == 0 && energyContainer.getEnergyStored() >= getEnergyPerCrateTape()) {
                BlockPos blockPos = getPos().offset(getFrontFacing());
                IBlockState blockState = world.getBlockState(blockPos);

                if (blockState.getBlock() instanceof BlockMachine) {
                    MetaTileEntity metaTileEntity = getMetaTileEntity(world, blockPos);

                    if (metaTileEntity instanceof MetaTileEntityCrate) {
                        IMixinCrate crate = (IMixinCrate) metaTileEntity;

                        if (!crate.isTaped()) {
                            int slot = -1;

                            //this is reversed or else it looks in the last slot first for some reason idk why dont ask me im sorry
                            for (int i = exportItems.getSlots()-1; i >= 0; i--) {
                                if (!exportItems.getStackInSlot(i).isEmpty()) {
                                    slot = i;
                                }
                            }

                            if (slot > -1) {
                                exportItems.extractItem(slot, 1, false);
                                crate.setTaped(true);
                                metaTileEntity.markDirty();
                                metaTileEntity.writeCustomData(IS_TAPED, buf -> buf.writeBoolean(true));
                                cooldown = cooldownTime;
                                energyContainer.removeEnergy(getEnergyPerCrateTape());
                                //prolly a better way to do this
                                world.playSound(null, blockPos, GTSoundEvents.SOFT_MALLET_TOOL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            }
                        }
                    }
                }
            }
        }
    }

    private int getEnergyPerCrateTape() {
        return (int) GTValues.V[getTier()] * 4;
    }

    @Override
    protected IItemHandlerModifiable createExportItemHandler() {
        return new GTItemStackHandler(this, getInventorySize()) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.isItemEqual(MetaItems.DUCT_TAPE.getStackForm()) || stack.isItemEqual(MetaItems.BASIC_TAPE.getStackForm());
            }
        };
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        int rowSize = (int) Math.sqrt(getInventorySize());
        ModularUI.Builder builder = ModularUI.builder(GuiTextures.BACKGROUND, 176, 18 + 18 * rowSize + 94)
                .label(10, 5, getMetaFullName());

        for (int y = 0; y < rowSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                int index = y * rowSize + x;
                builder.widget(new SlotWidget(exportItems, index, 89 - rowSize * 9 + x * 18, 18 + y * 18, true, true)
                        .setBackgroundTexture(GuiTextures.SLOT));
            }
        }
        builder.bindPlayerInventory(entityPlayer.inventory, GuiTextures.SLOT, 7, 18 + 18 * rowSize + 12);
        return builder.build(getHolder(), entityPlayer);
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("OutputFacing", getOutputFacing().getIndex());
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.outputFacing = EnumFacing.VALUES[data.getInteger("OutputFacing")];
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        this.outputFacing = EnumFacing.VALUES[buf.readByte()];
    }

    @Override
    public void receiveCustomData(int dataId, PacketBuffer buf) {
        super.receiveCustomData(dataId, buf);
        if (dataId == UPDATE_OUTPUT_FACING) {
            this.outputFacing = EnumFacing.VALUES[buf.readByte()];
            scheduleRenderUpdate();
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.machine.crate_taper.tooltip"));
        tooltip.add(I18n.format("gregtech.universal.tooltip.uses_per_op", getEnergyPerCrateTape()));
        tooltip.add(I18n.format("gregtech.universal.tooltip.voltage_in", energyContainer.getInputVoltage(),
                GTValues.VNF[getTier()]));
        tooltip.add(
                I18n.format("gregtech.universal.tooltip.energy_storage_capacity", energyContainer.getEnergyCapacity()));
        tooltip.add(I18n.format("gregtech.universal.tooltip.item_storage_capacity", getInventorySize()));
        tooltip.add(I18n.format("gregtech.universal.tooltip.requires_redstone"));
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public void addToolUsages(ItemStack stack, @Nullable World world, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.tool_action.screwdriver.access_covers"));
        tooltip.add(I18n.format("gregtech.tool_action.wrench.set_facing"));
        super.addToolUsages(stack, world, tooltip, advanced);
    }

    private int getInventorySize() {
        int sizeRoot = (1 + getTier());
        return sizeRoot * sizeRoot;
    }

    //fix all this stuff below here
    //a lot of this code was just taken from the blockbreaker
    //i didnt just extend it because tbh it doesnt make sense to
    //anything below this is stuff i havent changed, or didnt really touch at all even if unchanged regardless

    @Override
    public boolean onWrenchClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing,
                                 CuboidRayTraceResult hitResult) {
        if (!playerIn.isSneaking()) {
            EnumFacing currentOutputSide = getOutputFacing();
            if (currentOutputSide == facing || getFrontFacing() == facing) return false;
            setOutputFacing(facing);
            return true;
        }
        return super.onWrenchClick(playerIn, hand, facing, hitResult);
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeByte(getOutputFacing().getIndex());
    }

    @Override
    public boolean isValidFrontFacing(EnumFacing facing) {
        // use direct outputFacing field instead of getter method because otherwise
        // it will just return SOUTH for null output facing
        return super.isValidFrontFacing(facing) && facing != outputFacing;
    }

    public EnumFacing getOutputFacing() {
        return outputFacing == null ? EnumFacing.SOUTH : outputFacing;
    }

    public void setOutputFacing(EnumFacing outputFacing) {
        this.outputFacing = outputFacing;
        if (!getWorld().isRemote) {
            notifyBlockUpdate();
            writeCustomData(UPDATE_OUTPUT_FACING, buf -> buf.writeByte(outputFacing.getIndex()));
            markDirty();
        }
    }

    @Override
    public void setFrontFacing(EnumFacing frontFacing) {
        super.setFrontFacing(frontFacing);
        if (this.outputFacing == null) {
            // set initial output facing as opposite to front
            setOutputFacing(frontFacing.getOpposite());
        }
    }

    @Override
    public boolean getIsWeatherOrTerrainResistant() {
        return true;
    }


    @Override
    public boolean needsSneakToRotate() {
        return true;
    }
}

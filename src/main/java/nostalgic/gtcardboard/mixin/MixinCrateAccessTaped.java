package nostalgic.gtcardboard.mixin;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import nostalgic.gtcardboard.IMixinCrate;
import nostalgic.gtcardboard.common.CardboardBoxConfigHolder;
import nostalgic.gtcardboard.common.metatileentities.storage.MetaTileEntityBox;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = MetaTileEntityCrate.class, remap = false)
public abstract class MixinCrateAccessTaped extends MetaTileEntity implements IMixinCrate {
    //@Accessor("isTaped")
    //boolean isTaped();
    @Shadow(remap = false)
    protected boolean isTaped;

    @Shadow(remap = false)
    protected ItemStackHandler inventory;

    @Override
    public boolean isTaped() {
        return this.isTaped;
    }

    @Override
    public void setTaped(boolean taped) {
        this.isTaped = taped;
    }

    @Override
    public ItemStackHandler getInventoryHandler() {
        return this.inventory;
    }

    @Override
    public NonNullList<ItemStack> getInventoryAsNonNullList() {
        NonNullList<ItemStack> list = NonNullList.create();
        for (int i = 0; i<inventory.getSlots(); i+=1) {
            if (inventory.getStackInSlot(i) != ItemStack.EMPTY) {
                list.add(inventory.getStackInSlot(i));
            }
        }
        return list;
    }

    @Override
    public void setInventoryEmpty() {
        for (int i = 0; i<inventory.getSlots(); i+=1) {
            inventory.setStackInSlot(i,ItemStack.EMPTY);
        }
    }

    @Override
    public boolean isInventoryEmpty() {
        for (int i = 0; i<inventory.getSlots(); i+=1) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    //please fix the sync issue with crates this fix sucks
    //this seemed to work on MTEBox
    //@Override
    //public void writeInitialSyncData(@NotNull PacketBuffer buf) {
    //    super.writeInitialSyncData(buf);
    //    buf.writeBoolean(isTaped);
    //}

    //@Override
    //public void receiveInitialSyncData(@NotNull PacketBuffer buf) {
    //    super.receiveInitialSyncData(buf);
    //    isTaped = buf.readBoolean();
    //}

    public MixinCrateAccessTaped() {
        super(null);
    }

    @Override
    public void writeInitialSyncData(@NotNull PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        if (CardboardBoxConfigHolder.box.enableWorldLoadTapeFix) {
            buf.writeBoolean(this.isTaped);
        }
    }

    @Override
    public void receiveInitialSyncData(@NotNull PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        if (CardboardBoxConfigHolder.box.enableWorldLoadTapeFix) {
            this.isTaped = buf.readBoolean();

            if (this.getWorld() != null && this.getWorld().isRemote) {
                this.scheduleRenderUpdate();
            }
        }
    }

    @ModifyVariable(method = "renderMetaTileEntity", at = @At(value = "STORE"), ordinal = 0)
    private boolean dontRenderTapeIfAlwaysTaped(boolean taped) {
        return taped && !CardboardBoxConfigHolder.box.makeCratesAlwaysTaped;
    }
}

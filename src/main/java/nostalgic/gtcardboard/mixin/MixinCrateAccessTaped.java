package nostalgic.gtcardboard.mixin;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import nostalgic.gtcardboard.IMixinCrate;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
            list.add(inventory.getStackInSlot(i));
        }
        return list;
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
        buf.writeBoolean(this.isTaped);
    }

    @Override
    public void receiveInitialSyncData(@NotNull PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        this.isTaped = buf.readBoolean();

        if (this.getWorld() != null && this.getWorld().isRemote) {
            this.scheduleRenderUpdate();
        }
    }
}

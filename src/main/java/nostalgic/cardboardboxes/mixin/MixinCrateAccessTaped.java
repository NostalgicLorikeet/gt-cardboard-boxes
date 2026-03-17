package nostalgic.cardboardboxes.mixin;

import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import nostalgic.cardboardboxes.IMixinCrate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MetaTileEntityCrate.class, remap = false)
public abstract class MixinCrateAccessTaped implements IMixinCrate {
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
}

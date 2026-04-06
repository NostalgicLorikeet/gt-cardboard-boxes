package nostalgic.gtcardboard;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public interface IMixinCrate {
    boolean isTaped();
    void setTaped(boolean taped);
    NonNullList<ItemStack> getInventoryAsNonNullList();
    ItemStackHandler getInventoryHandler();
    boolean isInventoryEmpty();
    void setInventoryEmpty();
}

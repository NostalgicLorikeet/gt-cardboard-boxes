package nostalgic.cardboardboxes;

import gregtech.api.block.machines.BlockMachine;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nostalgic.cardboardboxes.common.metatileentities.storage.MetaTileEntityBox;

import static gregtech.api.util.GTUtility.getMetaTileEntity;

public class CrateCustomBreakEvent {
    //This is a workaround for the fact that you cant break MTEs by hand
    //And also the fact that taped crates just delete items when broken by hand
    //Only applies to boxes rn but will make apply to crates when not lazy
    //Also will prolly use for config stuff
    //This also really sucks
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() instanceof BlockMachine) {

            World world = event.getWorld();
            BlockPos pos = event.getPos();
            MetaTileEntity metaTileEntity = getMetaTileEntity(world, pos);

            if (!world.isRemote && metaTileEntity instanceof IMixinCrate) {

                EntityPlayer player = event.getPlayer();
                MetaTileEntityCrate crate = (MetaTileEntityCrate) metaTileEntity;
                NonNullList<ItemStack> inventoryContents = ((IMixinCrate) crate).getInventoryAsNonNullList();

                boolean isBox = crate instanceof MetaTileEntityBox;
                boolean isTaped = ((IMixinCrate) metaTileEntity).isTaped();
                boolean harvestable = player.getHeldItemMainhand().canHarvestBlock(event.getState());

                if ((!isBox && isTaped && !harvestable) || (isBox && !isTaped)) {
                    for (ItemStack itemStack : inventoryContents) {
                        Block.spawnAsEntity(world, pos, itemStack);
                    }
                    if (isBox) {
                        Block.spawnAsEntity(world, pos, crate.getStackForm());
                    }
                }

                if (isBox && isTaped) {
                    ItemStack boxDrop = crate.getStackForm();
                    boxDrop.setTagCompound(crate.writeToNBT(new NBTTagCompound()));
                    boxDrop.getTagCompound().setBoolean("Taped", true);
                    boxDrop.getTagCompound().setTag("Inventory", ((IMixinCrate) crate).getInventoryHandler().serializeNBT());
                    Block.spawnAsEntity(world, pos, boxDrop);
                }

                if (isBox || !harvestable) {
                    world.setBlockToAir(pos);
                    event.setCanceled(true);
                }
            }
        }
    }
}

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
        EntityPlayer player = event.getPlayer();

        if (event.getState().getBlock() instanceof BlockMachine) {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            MetaTileEntity metaTileEntity = getMetaTileEntity(world, pos);
            if (!world.isRemote && metaTileEntity instanceof MetaTileEntityCrate) {
                boolean isBox = metaTileEntity instanceof MetaTileEntityBox;
                boolean taped = ((IMixinCrate) metaTileEntity).isTaped();
                if (isBox) {
                    MetaTileEntityBox box = (MetaTileEntityBox) metaTileEntity;
                    ItemStack boxDrop = box.getStackForm();
                    if (!taped) {
                        NonNullList<ItemStack> inventoryContents = NonNullList.create();
                        box.clearMachineInventory(inventoryContents);
                        for (ItemStack itemStack : inventoryContents) {
                            Block.spawnAsEntity(world, pos, itemStack);
                        }
                    } else {
                        boxDrop.setTagCompound(box.writeToNBT(new NBTTagCompound()));
                        boxDrop.getTagCompound().setBoolean("Taped", true);
                    }
                    if (!player.isCreative() || taped) {
                        Block.spawnAsEntity(world, pos, boxDrop);
                    }
                    world.setBlockToAir(pos);
                    event.setCanceled(true);
                }
            }
        }
    }
}

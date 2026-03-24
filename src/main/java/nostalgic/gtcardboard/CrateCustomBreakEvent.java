package nostalgic.gtcardboard;

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
import nostalgic.gtcardboard.common.metatileentities.storage.MetaTileEntityBox;
import nostalgic.gtcardboard.IMixinCrate;

import static gregtech.api.util.GTUtility.getMetaTileEntity;

public class CrateCustomBreakEvent {
    //This is a workaround for the fact that you cant break MTEs by hand
    //And also the fact that taped crates just delete items when broken by hand
    //This kinda sucks and could prolly be implemented better later
    //it also DOESNT FUCKING WORK!!!
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() instanceof BlockMachine) {

            World world = event.getWorld();
            BlockPos pos = event.getPos();
            MetaTileEntity metaTileEntity = getMetaTileEntity(world, pos);

            if (!world.isRemote && metaTileEntity instanceof IMixinCrate) {

                EntityPlayer player = event.getPlayer();
                MetaTileEntityCrate crate = (MetaTileEntityCrate) metaTileEntity;
                IMixinCrate crateCasted = (IMixinCrate) crate;
                NonNullList<ItemStack> inventoryContents = crateCasted.getInventoryAsNonNullList();

                boolean isBox = crate instanceof MetaTileEntityBox;
                boolean isTaped = crateCasted.isTaped();
                boolean harvestable = player.getHeldItemMainhand().canHarvestBlock(event.getState());

                if (player.isCreative() || (!isBox && isTaped && !harvestable) || (isBox && !isTaped)) {
                    for (ItemStack itemStack : inventoryContents) {
                        Block.spawnAsEntity(world, pos, itemStack);
                    }
                    if (isBox && !player.isCreative()) {
                        Block.spawnAsEntity(world, pos, crate.getStackForm());
                    }
                }

                if (isBox && isTaped && !player.isCreative()) {
                    ItemStack boxDrop = crate.getStackForm();
                    boxDrop.setTagCompound(crate.writeToNBT(new NBTTagCompound()));
                    boxDrop.getTagCompound().setBoolean("Taped", true);
                    boxDrop.getTagCompound().setTag("Inventory", crateCasted.getInventoryHandler().serializeNBT());
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
package nostalgic.cardboardboxes.common;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nostalgic.cardboardboxes.loaders.recipes.CardboardRecipes;
import nostalgic.gtcardboard.Tags;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class CommonProxy {

    @SubscribeEvent()
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
         CardboardRecipes.registerRecipes();
    }
}

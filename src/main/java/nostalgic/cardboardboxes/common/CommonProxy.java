package nostalgic.cardboardboxes.common;

import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.event.MaterialEvent;
import gregtech.api.unification.material.event.MaterialRegistryEvent;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nostalgic.cardboardboxes.loaders.recipes.CardboardRecipes;
import nostalgic.cardboardboxes.materials.CardboardMaterials;
import nostalgic.gtcardboard.Tags;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class CommonProxy {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAH FUUUUCK1");
        CardboardRecipes.registerRecipes();
    }

    @SubscribeEvent
    public static void createMaterialRegistry(MaterialRegistryEvent event) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAH FUUUUCK2");
        GregTechAPI.materialManager.createRegistry(Tags.MOD_ID);
    }

    @SubscribeEvent()
    public static void registerMaterials(MaterialEvent event) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAH FUUUUCK3");
        CardboardMaterials.register();
    }
}

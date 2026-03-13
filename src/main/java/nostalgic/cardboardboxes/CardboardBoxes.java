package nostalgic.cardboardboxes;

import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.event.MaterialRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nostalgic.cardboardboxes.client.renderer.texture.CardboardTextures;
import nostalgic.cardboardboxes.machine.CardboardMetaTileEntities;
import nostalgic.cardboardboxes.materials.CardboardMaterials;
import nostalgic.gtcardboard.Tags;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class CardboardBoxes {

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //LOGGER.info("Hello From {}!", Tags.MOD_NAME);
        //Config.load(event);
        CardboardMetaTileEntities.preInit();
        CardboardTextures.preInit();
    }
}

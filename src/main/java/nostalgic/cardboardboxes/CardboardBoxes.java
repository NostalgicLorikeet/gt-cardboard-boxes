package nostalgic.cardboardboxes;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nostalgic.cardboardboxes.client.renderer.texture.CardboardTextures;
import nostalgic.cardboardboxes.config.Config;
import nostalgic.cardboardboxes.machine.CardboardMetaTileEntities;
import nostalgic.cardboardboxes.materials.CardboardMaterials;
import nostalgic.gtcardboard.Tags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class CardboardBoxes {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws NoSuchFieldException {
        LOGGER.info("Hello From {}!", Tags.MOD_NAME);
        Config.load(event);
        CardboardMetaTileEntities.preInit();
        CardboardTextures.preInit();
        CardboardMaterials.register();
    }
}

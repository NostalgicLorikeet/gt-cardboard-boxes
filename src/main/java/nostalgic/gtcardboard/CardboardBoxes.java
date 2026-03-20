package nostalgic.gtcardboard;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nostalgic.gtcardboard.client.renderer.texture.CardboardTextures;
import nostalgic.gtcardboard.machine.CardboardMetaTileEntities;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class CardboardBoxes {

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //LOGGER.info("Hello From {}!", Tags.MOD_NAME);
        //Config.load(event);
        CardboardMetaTileEntities.preInit();
        CardboardTextures.preInit();

        MinecraftForge.EVENT_BUS.register(new CrateCustomBreakEvent());
    }
}

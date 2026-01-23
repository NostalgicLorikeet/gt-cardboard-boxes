package nostalgic.cardboardboxes.config;

import nostalgic.gtcardboard.Tags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

//figure out how to use ConfigHolder to do this later

public final class Config
{
    public static Config instance = new Config();

    public static Logger log = LogManager.getLogger(Tags.MOD_ID + "-" + "Config");

    private static final String ENABLE_DISABLE = "ENABLE-DISABLE";

    private Config()
    {
    }

    public static void load(FMLPreInitializationEvent event)
    {
        configFile = new Configuration(event.getSuggestedConfigurationFile(), "0.2", false);
        configFile.load();

        syncConfig();
    }

    public static boolean syncConfig()
    {
        crateKeepTapeOnPlace = configFile.get(ENABLE_DISABLE, "Make crates remain taped when placed down, instead, tape is removed when opened", crateKeepTapeOnPlace).getBoolean(crateKeepTapeOnPlace);
        boxKeepTapeOnPlace = configFile.get(ENABLE_DISABLE, "Make boxes remain taped when placed down, instead, tape is removed when opened", boxKeepTapeOnPlace).getBoolean(boxKeepTapeOnPlace);
        crateNoTape = configFile.get(ENABLE_DISABLE, "Make crates not need to be taped to retain their contents when broken", crateNoTape).getBoolean(crateNoTape);
        boxNoTape = configFile.get(ENABLE_DISABLE, "Make boxes not need to be taped to retain their contents when broken", boxNoTape).getBoolean(boxNoTape);

        // save changes if any
        boolean changed = false;

        if (configFile.hasChanged())
        {
            configFile.save();
            changed = true;
        }

        return changed;
    }

    //@formatter:off
    public static boolean crateKeepTapeOnPlace = false;
    public static boolean boxKeepTapeOnPlace = false;
    public static boolean crateNoTape = false;
    public static boolean boxNoTape = false;

    static Configuration configFile;
    //@formatter:on
}


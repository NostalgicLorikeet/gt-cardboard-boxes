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
        //unimplemented cuz torture
        //crateKeepTapeOnPlace = configFile.get("Make crates remain taped when placed down, instead, tape is removed when opened", "crateKeepTapeOnPlace", crateKeepTapeOnPlace).getBoolean(crateKeepTapeOnPlace);
        //boxKeepTapeOnPlace = configFile.get("Make boxes remain taped when placed down, instead, tape is removed when opened", "boxKeepTapeOnPlace", boxKeepTapeOnPlace).getBoolean(boxKeepTapeOnPlace);
        crateNoTape = configFile.get("Make crates not need to be taped to retain their contents when broken", "crateNoTape", crateNoTape).getBoolean(crateNoTape);
        boxNoTape = configFile.get("Make boxes not need to be taped to retain their contents when broken", "boxNoTape", boxNoTape).getBoolean(boxNoTape);

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


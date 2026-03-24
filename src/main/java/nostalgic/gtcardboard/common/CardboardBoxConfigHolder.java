package nostalgic.gtcardboard.common;

import net.minecraftforge.common.config.Config;
import nostalgic.gtcardboard.Tags;

@Config(modid = Tags.MOD_ID, name = Tags.MOD_ID + '/' + Tags.MOD_ID)
public class CardboardBoxConfigHolder {

    @Config.Comment("Config options for GregTech Cardboard Boxes")
    @Config.Name("Cardboard Box Options")
    @Config.RequiresMcRestart
    public static BoxOptions box = new BoxOptions();

    public static class BoxOptions {
        //@Config.Comment({ "Allow cardboard boxes to be broken by hand.", "Default: true"  })
        //public boolean allowBoxBreakByHand = true;

        //@Config.Comment({ "Allow all crates to be broken by hand.", "Default: false"  })
        //public boolean allowAllCratesBreakByHand = false;

        @Config.Comment({ "Make cardboard boxes have a quicker break speed than other MTEs.", "Default: true"  })
        public boolean boxesBreakQuick = true;

        @Config.Comment({ "Enable cardboard box recipes, disable if you ONLY want the Tapers obtainable in survival or if you want to change the recipes more easily.", "Default: true"  })
        public boolean cardboardBoxCraftable = true;

        @Config.Comment({ "Enable taper recipes, disable if you don't want them obtainable in survival.", "Default: true"  })
        public boolean tapersCraftable = true;
    }
}

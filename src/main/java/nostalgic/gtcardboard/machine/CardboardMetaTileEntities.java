package nostalgic.gtcardboard.machine;

import static gregtech.common.metatileentities.MetaTileEntities.registerMetaTileEntity;

import gregtech.api.GTValues;
import net.minecraft.util.ResourceLocation;
import nostalgic.gtcardboard.common.metatileentities.electric.MetaTileEntityCrateTaper;
import nostalgic.gtcardboard.common.metatileentities.storage.MetaTileEntityBox;
import nostalgic.gtcardboard.materials.CardboardMaterials;
import nostalgic.gtcardboard.Tags;

public class CardboardMetaTileEntities {
    public static MetaTileEntityBox CARDBOARD_CRATE;
    public static final MetaTileEntityCrateTaper[] CRATE_TAPER = new MetaTileEntityCrateTaper[3];

    public static void preInit() {
        CARDBOARD_CRATE = registerMetaTileEntity(16039,
            new MetaTileEntityBox(new ResourceLocation(Tags.MOD_ID,"crate.cardboard"), CardboardMaterials.Cardboard, 9));

        for (int i = 0; i < CRATE_TAPER.length; i++) {
            String voltageName = GTValues.VN[i + 1].toLowerCase();
            CRATE_TAPER[i] = new MetaTileEntityCrateTaper(new ResourceLocation(Tags.MOD_ID,"crate_taper." + voltageName), i + 1);
            registerMetaTileEntity(16040 + i, CRATE_TAPER[i]);
        }
    }
}

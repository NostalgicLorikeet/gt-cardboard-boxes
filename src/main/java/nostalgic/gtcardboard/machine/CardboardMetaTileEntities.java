package nostalgic.gtcardboard.machine;

import static gregtech.common.metatileentities.MetaTileEntities.registerMetaTileEntity;

import net.minecraft.util.ResourceLocation;
import nostalgic.gtcardboard.common.metatileentities.storage.MetaTileEntityBox;
import nostalgic.gtcardboard.materials.CardboardMaterials;
import nostalgic.gtcardboard.Tags;

public class CardboardMetaTileEntities {
    public static MetaTileEntityBox CARDBOARD_CRATE;

    public static void preInit() {
        CARDBOARD_CRATE = registerMetaTileEntity(1639,
            new MetaTileEntityBox(new ResourceLocation(Tags.MOD_ID,"crate.cardboard"), CardboardMaterials.Cardboard, 9));
    }
}

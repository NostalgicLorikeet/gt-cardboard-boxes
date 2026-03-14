package nostalgic.cardboardboxes.machine;

import static gregtech.api.util.GTUtility.gregtechId;
import static gregtech.common.metatileentities.MetaTileEntities.registerMetaTileEntity;
import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import net.minecraft.util.ResourceLocation;
import nostalgic.cardboardboxes.common.metatileentities.storage.MetaTileEntityBox;
import nostalgic.cardboardboxes.materials.CardboardMaterials;
import nostalgic.gtcardboard.Tags;

public class CardboardMetaTileEntities {
    public static MetaTileEntityBox CARDBOARD_CRATE;

    public static void preInit() {
        CARDBOARD_CRATE = registerMetaTileEntity(1639,
            new MetaTileEntityBox(new ResourceLocation(Tags.MOD_ID,"crate.cardboard"), CardboardMaterials.Cardboard, 9));
    }
}

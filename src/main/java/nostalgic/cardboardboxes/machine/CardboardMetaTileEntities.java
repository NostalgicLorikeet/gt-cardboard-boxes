package nostalgic.cardboardboxes.machine;

import static gregtech.api.util.GTUtility.gregtechId;
import static gregtech.common.metatileentities.MetaTileEntities.registerMetaTileEntity;
import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import nostalgic.cardboardboxes.common.metatileentities.storage.MetaTileEntityBox;
import nostalgic.cardboardboxes.materials.CardboardMaterials;

public class CardboardMetaTileEntities {
    public static MetaTileEntityCrate CARDBOARD_CRATE;

    public static void preInit() {
        CARDBOARD_CRATE = registerMetaTileEntity(1639,
            new MetaTileEntityBox(gregtechId("crate.cardboard"), CardboardMaterials.Cardboard, 9));
    }
}

package nostalgic.cardboardboxes.materials;

import gregtech.api.unification.material.Material;

import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.util.GTUtility.gregtechId;

public class CardboardMaterials {
    public static Material Cardboard;

    public static void register() {
        Material Cardboard = new Material.Builder(17779, gregtechId("cardboard"))
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .color(0xA07752).build();
    }
}

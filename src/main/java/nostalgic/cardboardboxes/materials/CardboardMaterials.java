package nostalgic.cardboardboxes.materials;

import gregtech.api.unification.material.Material;
import net.minecraft.util.ResourceLocation;
import nostalgic.gtcardboard.Tags;

import static gregtech.api.unification.material.info.MaterialFlags.*;

public class CardboardMaterials {
    public static Material Cardboard;

    public static void register() {
        Cardboard = new Material.Builder(0, new ResourceLocation(Tags.MOD_ID,"cardboard"))
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .color(0xA07752)
                .dust()
                .build();
    }
}

package nostalgic.cardboardboxes.loaders.recipes;

import gregtech.common.items.MetaItems;

import static gregtech.api.recipes.RecipeMaps.ASSEMBLER_RECIPES;
import static gregtech.api.unification.material.Materials.Glue;
import static gregtech.api.unification.ore.OrePrefix.plate;
import static nostalgic.cardboardboxes.machine.CardboardMetaTileEntities.CARDBOARD_CRATE;
import static nostalgic.cardboardboxes.materials.CardboardMaterials.Cardboard;

public class CardboardRecipes {
    public static void registerRecipes() {
        ASSEMBLER_RECIPES.recipeBuilder().EUt(8).input(plate, Cardboard, 6).input(MetaItems.STICKY_RESIN, 1)
                .outputs(CARDBOARD_CRATE.getStackForm()).duration(200).circuitMeta(1).buildAndRegister();
        ASSEMBLER_RECIPES.recipeBuilder().EUt(8).input(plate, Cardboard, 6).fluidInputs(Glue.getFluid(20))
                .outputs(CARDBOARD_CRATE.getStackForm()).duration(200).circuitMeta(1).buildAndRegister();
    }
}

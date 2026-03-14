package nostalgic.cardboardboxes.loaders.recipes;

import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.common.items.MetaItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import nostalgic.cardboardboxes.machine.CardboardMetaTileEntities;
import nostalgic.cardboardboxes.materials.CardboardMaterials;

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

        ModHandler.addShapedRecipe("cardboard_crate", CardboardMetaTileEntities.CARDBOARD_CRATE.getStackForm(),
                "RRR", "RPR", " R ",
                'R', new UnificationEntry(OrePrefix.plate, CardboardMaterials.Cardboard),
                'P', MetaItems.STICKY_RESIN.getStackForm());

        ModHandler.addShapedRecipe(true, "cardboard_sheet", OreDictUnifier.get(OrePrefix.plate, CardboardMaterials.Cardboard, 1),
                "CRC", "CRC", "CBC",
                'R', MetaItems.STICKY_RESIN.getStackForm(),
                'B', new ItemStack(Items.WATER_BUCKET),
                'C', new UnificationEntry(OrePrefix.dust, Materials.Paper));
    }
}

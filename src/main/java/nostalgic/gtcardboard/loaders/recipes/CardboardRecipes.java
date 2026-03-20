package nostalgic.gtcardboard.loaders.recipes;

import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.common.items.MetaItems;
import gregtech.common.items.ToolItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import nostalgic.gtcardboard.machine.CardboardMetaTileEntities;
import nostalgic.gtcardboard.materials.CardboardMaterials;

import static gregtech.api.recipes.RecipeMaps.ASSEMBLER_RECIPES;
import static gregtech.api.recipes.RecipeMaps.MACERATOR_RECIPES;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.ore.OrePrefix.*;
import static nostalgic.gtcardboard.machine.CardboardMetaTileEntities.CARDBOARD_CRATE;
import static nostalgic.gtcardboard.materials.CardboardMaterials.Cardboard;

public class CardboardRecipes {
    public static void registerRecipes() {
        ASSEMBLER_RECIPES.recipeBuilder()
                .EUt(6)
                .input(plate, Cardboard, 6)
                .input(MetaItems.STICKY_RESIN, 1)
                .outputs(CARDBOARD_CRATE.getStackForm())
                .duration(200)
                .circuitMeta(1)
                .buildAndRegister();

        ASSEMBLER_RECIPES.recipeBuilder()
                .EUt(6)
                .input(plate, Cardboard, 6)
                .fluidInputs(Glue.getFluid(20))
                .outputs(CARDBOARD_CRATE.getStackForm())
                .duration(200)
                .circuitMeta(1)
                .buildAndRegister();

        ModHandler.addShapedRecipe("cardboard_crate", CardboardMetaTileEntities.CARDBOARD_CRATE.getStackForm(),
                "RRR", "RPR", " R ",
                'R', new UnificationEntry(OrePrefix.plate, CardboardMaterials.Cardboard),
                'P', MetaItems.STICKY_RESIN.getStackForm());

        ASSEMBLER_RECIPES.recipeBuilder()
                .EUt(4)
                .input(MetaItems.STICKY_RESIN, 1)
                .input(dust, Paper, 6)
                .outputs(OreDictUnifier.get(OrePrefix.plate, CardboardMaterials.Cardboard, 1))
                .duration(200)
                .circuitMeta(1)
                .buildAndRegister();

        ASSEMBLER_RECIPES.recipeBuilder()
                .EUt(4)
                .fluidInputs(Glue.getFluid(20))
                .input(dust, Paper, 6)
                .outputs(OreDictUnifier.get(OrePrefix.plate, CardboardMaterials.Cardboard, 1))
                .duration(200)
                .circuitMeta(1)
                .buildAndRegister();

        ModHandler.addShapedRecipe(true, "cardboard_sheet", OreDictUnifier.get(OrePrefix.plate, CardboardMaterials.Cardboard, 1),
                "CHC", "CRC", "CBC",
                'H', ToolItems.HARD_HAMMER,
                'R', MetaItems.STICKY_RESIN.getStackForm(),
                'B', new ItemStack(Items.WATER_BUCKET),
                'C', new UnificationEntry(dust, Paper));

        MACERATOR_RECIPES.recipeBuilder()
                .EUt(12)
                .duration(147)
                .input(CARDBOARD_CRATE)
                .output(dust, Paper, 36)
                .buildAndRegister();
    }
}

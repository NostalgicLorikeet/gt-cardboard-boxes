package nostalgic.gtcardboard.loaders.recipes;

import com.cleanroommc.groovyscript.compat.vanilla.OreDictEntry;
import gregtech.api.GTValues;
import gregtech.api.items.OreDictNames;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.MarkerMaterials;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.common.items.MetaItems;
import gregtech.common.items.ToolItems;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.common.metatileentities.electric.MetaTileEntityHull;
import gregtech.loaders.recipe.CraftingComponent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import nostalgic.gtcardboard.Tags;
import nostalgic.gtcardboard.common.metatileentities.electric.MetaTileEntityCrateTaper;
import nostalgic.gtcardboard.machine.CardboardMetaTileEntities;
import nostalgic.gtcardboard.materials.CardboardMaterials;

import static gregtech.api.recipes.RecipeMaps.ASSEMBLER_RECIPES;
import static gregtech.api.recipes.RecipeMaps.MACERATOR_RECIPES;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.ore.OrePrefix.*;
import static nostalgic.gtcardboard.machine.CardboardMetaTileEntities.CARDBOARD_CRATE;
import static nostalgic.gtcardboard.machine.CardboardMetaTileEntities.CRATE_TAPER;
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

        for (int i = 0; i < CRATE_TAPER.length; i++) {
            String voltageName = GTValues.VN[i + 1].toLowerCase();

            ItemStack robotArm = (i == 0 ? MetaItems.ROBOT_ARM_LV : (i == 1 ? MetaItems.ROBOT_ARM_MV : MetaItems.ROBOT_ARM_HV)).getStackForm();
            ItemStack electricMotor = (i == 0 ? MetaItems.ELECTRIC_MOTOR_LV : (i == 1 ? MetaItems.ELECTRIC_MOTOR_MV : MetaItems.ELECTRIC_MOTOR_HV)).getStackForm();
            Material tier = (i == 0 ? MarkerMaterials.Tier.LV : (i == 1 ? MarkerMaterials.Tier.MV : MarkerMaterials.Tier.HV));
            Material cableMaterial = (i == 0 ? Materials.Tin : (i == 1 ? Materials.Copper : Materials.Gold));

            ModHandler.addShapedRecipe(true, "crate_taper_" + voltageName,
                    CRATE_TAPER[i].getStackForm(),
                    "RWM", "CHC", "KWK",
                    'R', robotArm, //robot arm
                    'W', new UnificationEntry(OrePrefix.ring, Materials.Iron), //ring
                    'M', electricMotor, //electric motor
                    'C', new UnificationEntry(OrePrefix.circuit, tier), //circuit
                    'H', MetaTileEntities.HULL[i].getStackForm(), //hull
                    'K', new UnificationEntry(OrePrefix.cableGtSingle, cableMaterial)); //cable
        }
    }
}

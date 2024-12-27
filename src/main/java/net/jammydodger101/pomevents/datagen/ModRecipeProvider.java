package net.jammydodger101.pomevents.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.jammydodger101.pomevents.item.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {


    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generate(RecipeExporter exporter) {

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.LUCK_BLADE, 1)
                .input(Items.DIAMOND_SWORD)
                .input(Items.GLASS_BOTTLE)
                .input(Items.RABBIT_FOOT)
                .input(Items.BROWN_MUSHROOM)
                .input(Items.LILY_OF_THE_VALLEY)
                .input(Items.SLIME_BALL)
                .input(Items.LAPIS_LAZULI)
                .input(Items.SPORE_BLOSSOM)
                .input(Items.LEATHER)
                .criterion(hasItem(Items.DIAMOND_SWORD), conditionsFromItem(Items.DIAMOND_SWORD))
                .offerTo(exporter, Identifier.of(getRecipeName(ModItems.LUCK_BLADE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.BLAZE_SHIELD, 1)
                .pattern("MBM")
                .pattern("ASA")
                .pattern("MBM")
                .input('M', Blocks.MAGMA_BLOCK)
                .input('B', Items.BLAZE_POWDER)
                .input('S', Items.SHIELD)
                .input('A', Blocks.ANCIENT_DEBRIS)
                .criterion(hasItem(Items.SHIELD), conditionsFromItem(Items.SHIELD))
                .offerTo(exporter, Identifier.of(getRecipeName(ModItems.BLAZE_SHIELD)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.DRAGON_WING, 1)
                .pattern(" E ")
                .pattern(" D ")
                .pattern(" A ")
                .input('E', Items.ENDER_PEARL)
                .input('D', Items.DRAGON_EGG)
                .input('A', Items.DIAMOND_AXE)
                .criterion(hasItem(Items.DRAGON_EGG), conditionsFromItem(Items.DRAGON_EGG))
                .offerTo(exporter, Identifier.of(getRecipeName(ModItems.DRAGON_WING)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SUMMONER_STAFF, 1)
                .pattern("XHX")
                .pattern("XDX")
                .pattern("XSX")
                .input('X', Items.EXPERIENCE_BOTTLE)
                .input('D', Blocks.DIAMOND_BLOCK)
                .input('S', Items.STICK)
                .input('H', Items.HEART_OF_THE_SEA)
                .criterion(hasItem(Items.HEART_OF_THE_SEA), conditionsFromItem(Items.HEART_OF_THE_SEA))
                .offerTo(exporter, Identifier.of(getRecipeName(ModItems.SUMMONER_STAFF)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.GALE_BOW, 1)
                .pattern("BS ")
                .pattern("BWS")
                .pattern("BS ")
                .input('B', Items.BREEZE_ROD)
                .input('W', Items.WIND_CHARGE)
                .input('S', Items.STRING)
                .criterion(hasItem(Items.BREEZE_ROD), conditionsFromItem(Items.BREEZE_ROD))
                .offerTo(exporter, Identifier.of(getRecipeName(ModItems.GALE_BOW)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.PILLAGER_CROSSBOW, 1)
                .pattern("XSX")
                .pattern("TCT")
                .pattern("XUX")
                .input('X', Items.EXPERIENCE_BOTTLE)
                .input('S', Items.SPECTRAL_ARROW)
                .input('T', Items.TNT)
                .input('C', Items.CROSSBOW)
                .input('U', Items.TOTEM_OF_UNDYING)
                .criterion(hasItem(Items.TOTEM_OF_UNDYING), conditionsFromItem(Items.TOTEM_OF_UNDYING))
                .offerTo(exporter, Identifier.of(getRecipeName(ModItems.PILLAGER_CROSSBOW)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SCULK_TRIDENT, 1)
                .pattern("SGS")
                .pattern("STS")
                .pattern("SCS")
                .input('G', Items.GUNPOWDER)
                .input('T', Items.TRIDENT)
                .input('S', Blocks.SCULK)
                .input('C', Blocks.SCULK_CATALYST)
                .criterion(hasItem(Items.SCULK_CATALYST), conditionsFromItem(Items.SCULK_CATALYST))
                .offerTo(exporter, Identifier.of(getRecipeName(ModItems.SCULK_TRIDENT)));
    }
}

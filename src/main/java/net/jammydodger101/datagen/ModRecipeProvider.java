package net.jammydodger101.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.jammydodger101.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, BootstrapContext<Recipe<?>> bootstrapContext, BootstrapContext<Advancement> bootstrapContext1) {

        return new RecipeProvider(bootstrapContext, bootstrapContext1) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.MISC, ModItems.CROCKSMARTER_BLADE)
                        .pattern("NON")
                        .pattern("OEO")
                        .pattern("NON")
                        .define('O', Items.CRYING_OBSIDIAN)
                        .define('N', Items.NETHERITE_INGOT)
                        .define('E', Items.ENCHANTED_GOLDEN_APPLE)
                        .unlockedBy(getHasName(Items.ENCHANTED_GOLDEN_APPLE), has(Items.ENCHANTED_GOLDEN_APPLE))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}
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
                shaped(RecipeCategory.MISC, ModItems.SCULK_TRIDENT)
                        .pattern("STS")
                        .pattern("SHS")
                        .pattern("SRS")
                        .define('S', Items.SCULK)
                        .define('T', Items.TNT)
                        .define('R', Items.TRIDENT)
                        .define('H', ModItems.WARDEN_HEART)
                        .unlockedBy(getHasName(ModItems.WARDEN_HEART), has(ModItems.WARDEN_HEART))
                        .save(output);

            }
        };
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}
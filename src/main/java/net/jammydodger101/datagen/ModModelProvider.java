package net.jammydodger101.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.jammydodger101.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        //itemModelGenerators.generateFlatItem(ModItems.LIFESTEAL_HEART, ModelTemplates.FLAT_ITEM);

        //itemModelGenerators.declareCustomModelItem(ModItems.SCULK_TRIDENT);
        itemModelGenerators.declareCustomModelItem(ModItems.DRAGON_WING);
        itemModelGenerators.declareCustomModelItem(ModItems.PRISMARINE_ANCHOR);

        itemModelGenerators.generateFlatItem(ModItems.WARDEN_HEART, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.CROCKSMARTER_BLADE, ModelTemplates.FLAT_HANDHELD_ITEM);
    }
}

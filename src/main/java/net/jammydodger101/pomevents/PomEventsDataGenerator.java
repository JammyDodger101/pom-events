package net.jammydodger101.pomevents;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.jammydodger101.pomevents.datagen.ModItemTagProvider;
import net.jammydodger101.pomevents.datagen.ModModelProvider;
import net.jammydodger101.pomevents.datagen.ModRecipeProvider;

public class PomEventsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModItemTagProvider::new);
	}
}

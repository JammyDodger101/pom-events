package net.jammydodger101pom;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.jammydodger101pom.datagen.ModItemTagsProvider;
import net.jammydodger101pom.datagen.ModModelProvider;
import net.jammydodger101pom.datagen.ModRecipeProvider;

public class PomEvents2DataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModItemTagsProvider::new);
	}
}

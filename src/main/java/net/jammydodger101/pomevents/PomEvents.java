package net.jammydodger101.pomevents;

import net.fabricmc.api.ModInitializer;

import net.jammydodger101.pomevents.command.ModCommands;
import net.jammydodger101.pomevents.component.ModDataComponents;
import net.jammydodger101.pomevents.item.ModItemGroups;
import net.jammydodger101.pomevents.item.ModItems;
import net.jammydodger101.pomevents.util.ModModelPredicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PomEvents implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("pom-events");
	public static final String MOD_ID = "pom-events";

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//LOGGER.info("Hello Fabric world!");

		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModDataComponents.registerDataComponentTypes();
		ModModelPredicates.registerModelPredicates();
		ModCommands.init();



	}
}
package net.jammydodger101.pomevents;

import net.fabricmc.api.ModInitializer;

import net.jammydodger101.pomevents.command.ModCommands;
import net.jammydodger101.pomevents.component.ModDataComponents;
import net.jammydodger101.pomevents.item.ModItemGroups;
import net.jammydodger101.pomevents.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PomEvents implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("pom-events");
	public static final String MOD_ID = "pom-events";

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModDataComponents.registerDataComponentTypes();
		ModCommands.init();
	}
}
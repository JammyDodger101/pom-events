package net.jammydodger101.pomevents.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.jammydodger101.pomevents.PomEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/*
Adds all the items to its own tab in the creative inventory
 */
public class ModItemGroups {
    public static final ItemGroup FUN_GROUP = Registry.register(Registries.ITEM_GROUP, Identifier.of(PomEvents.MOD_ID, "pom-events"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.pom-events")).icon(() -> new ItemStack(ModItems.CROCKSMARTER_BLADE)).entries(((displayContext, entries) -> {
                entries.add(ModItems.CROCKSMARTER_BLADE);
                entries.add(ModItems.LUCK_BLADE);
                entries.add(ModItems.DRAGON_WING);
                entries.add(ModItems.BLAZE_SHIELD);
                entries.add(ModItems.SUMMONER_STAFF);
                entries.add(ModItems.LIFESTEAL_HEART);
                entries.add(ModItems.GALE_BOW);
                entries.add(ModItems.SCULK_TRIDENT);
                entries.add(ModItems.STORM_SPEAR);
                entries.add(ModItems.CHUNK_SHIFTER);
            })).build());

    public static void registerItemGroups() {
        PomEvents.LOGGER.info("Registering Item Groups for " + PomEvents.MOD_ID);
    }
}

package net.jammydodger101.pomevents.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.jammydodger101.pomevents.PomEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup FUN_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(PomEvents.MOD_ID, "jammysmodoffun"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.pom-events")).icon(() -> new ItemStack(ModItems.CROCKSMARTER_BLADE)).entries(((displayContext, entries) -> {
                entries.add(ModItems.CROCKSMARTER_BLADE);
                entries.add(ModItems.LUCK_BLADE);
                entries.add(ModItems.DRAGON_WING);
                entries.add(ModItems.BLAZE_SHIELD);
                entries.add(ModItems.SUMMONER_STAFF);
            })).build());

    public static void registerItemGroups() {
        PomEvents.LOGGER.info("Registering Item Groups for " + PomEvents.MOD_ID);
    }
}

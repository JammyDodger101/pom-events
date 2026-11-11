package net.jammydodger101pom.item;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.jammydodger101pom.PomEvents2;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTab {
    public static final CreativeModeTab EVENT_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(PomEvents2.MOD_ID, "event_tab"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModItems.CROCKSMARTER_BLADE))
                    .displayItems(((parameters, output) -> {
                        output.accept(ModItems.CROCKSMARTER_BLADE);
                        output.accept(ModItems.SCULK_TRIDENT);
                        output.accept(ModItems.DRAGON_WING);
                        output.accept(ModItems.PRISMARINE_ANCHOR);
                        output.accept(ModItems.SUMMONER_STAFF);
                    })).title(Component.literal("Event Tab")).build());

    public static void registerCreativeTab() {
        PomEvents2.LOGGER.info("Registering Creative Mode Tab");
    }
}

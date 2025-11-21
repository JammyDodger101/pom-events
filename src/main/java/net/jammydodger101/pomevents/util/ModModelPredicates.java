package net.jammydodger101.pomevents.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.item.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ModModelPredicates {
    public static void registerModelPredicates() {
        registerCustomBow(ModItems.GALE_BOW);
        registerCustomTrident(ModItems.SCULK_TRIDENT);
        registerCustomTrident(ModItems.STORM_SPEAR);
        registerCustomShield(ModItems.BLAZE_SHIELD);
    }

    private static void registerCustomBow(Item item) {
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            return (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft()) / 20.0f;
        });
        ModelPredicateProviderRegistry.register(item, Identifier.of(PomEvents.MOD_ID,"pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
    }

    private static void registerCustomTrident(Item item) {
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("throwing"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
        });
    }

    private static void registerCustomShield(Item item) {
        ModelPredicateProviderRegistry.register(item, Identifier.of(PomEvents.MOD_ID,"blocking"), ((stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
        }));
    }
}

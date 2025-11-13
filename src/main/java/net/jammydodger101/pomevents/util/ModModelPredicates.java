package net.jammydodger101.pomevents.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jammydodger101.pomevents.item.ModItems;
import net.jammydodger101.pomevents.item.custom.PillagerCrossbow;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ModModelPredicates {
    public static void registerModelPredicates() {
        registerCustomBow(ModItems.GALE_BOW);
        registerCustomTrident(ModItems.SCULK_TRIDENT);
        registerCustomTrident(ModItems.STORM_SPEAR);
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
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
    }

    private static void registerCustomTrident(Item item) {
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("throwing"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
        });
    }

    private static void registerCustomCrossbow(Item item) {
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return CrossbowItem.isCharged(stack) ? 0.0F : (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft()) / (float)CrossbowItem.getPullTime(stack, entity);
            }
        });
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("pulling"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("charged"), (stack, world, entity, seed) -> {
            return CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
        });
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("exploding"), ((stack, world, entity, seed) -> {
            return PillagerCrossbow.getExploding() || PillagerCrossbow.getExplode_cooldown() < 0 ? 1.0f : 0.0f;
        }));
    }
}

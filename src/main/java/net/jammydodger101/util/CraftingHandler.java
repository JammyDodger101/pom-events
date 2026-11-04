package net.jammydodger101.util;

import net.jammydodger101.particle.ParticleSpawner;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingHandler {
    public static List<Item> lockedItems = new ArrayList<>();

    private static final Map<Item, Integer> legendaryWeaponColours = new HashMap<>(){{
        /*
        put(ModItems.BLAZE_SHIELD, 16756493);
        put(ModItems.DRAGON_WING, 8667632);
        put(ModItems.GALE_BOW, 12581119);
        put(ModItems.LUCK_BLADE, 8513562);
        put(ModItems.SCULK_TRIDENT, 1466496);
        put(ModItems.STORM_SPEAR, 650680);
        put(ModItems.SUMMONER_STAFF, 12392203);
        */
        put(Items.DIAMOND_AXE, 1466496);
    }};



    public static void craftedItem(ItemStack stack, Level world, Player player) {
        if (world.isClientSide()) {
            ParticleSpawner.spawnFireworkParticles(world, player.getClientPosition(), 70);
            return;
        }

        lockedItems.add(stack.getItem());
        tellWorldItemWasCrafted(stack, world, player);
        world.players().forEach(playerEntity -> playerEntity.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE));
    }

    private static void tellWorldItemWasCrafted(ItemStack stack, Level world, Player player) {
        if (world.isClientSide()) {
            return;
        }
        if (legendaryWeaponColours.containsKey(stack.getItem())) {
            String message = (player.getScoreboardName() + "has just crafted the " + stack.getItemName().getString() + "!");
            world.players().forEach(playerEntity -> playerEntity.sendSystemMessage(Component.literal(message).withColor(legendaryWeaponColours.get(stack.getItem()))));
        }
    }
}

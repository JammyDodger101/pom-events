package net.jammydodger101.pomevents.util;

import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.item.ModItems;
import net.jammydodger101.pomevents.particle.ParticleSpawner;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.*;
import net.minecraft.world.World;

import java.util.*;

public class CraftingHandler {
    public static List<Item> craftedItems = new ArrayList<>();
    private static final Map<Item, Integer> legendaryWeaponColours = new HashMap<>(){{
        put(ModItems.BLAZE_SHIELD, 16756493);
        put(ModItems.DRAGON_WING, 8667632);
        put(ModItems.GALE_BOW, 12581119);
        put(ModItems.LUCK_BLADE, 8513562);
        put(ModItems.SCULK_TRIDENT, 1466496);
        put(ModItems.STORM_SPEAR, 650680);
        put(ModItems.SUMMONER_STAFF, 12392203);
    }};

    public static void craftedItem(ItemStack stack, World world, PlayerEntity player) {
        if (world.isClient()) {
            ParticleSpawner.spawnFireworkParticles(world, player.getPos(), 70);
            return;
        }
        craftedItems.add(stack.getItem());
        tellWorldItemWasCrafted(stack, world, player);
        world.getPlayers().forEach(playerEntity -> playerEntity.playSoundToPlayer(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 1, 1));
    }

    private static void tellWorldItemWasCrafted(ItemStack stack, World world, PlayerEntity player) {
        String message = (player.getNameForScoreboard() + " has just crafted the " + stack.getItem().getName().getString() + "!");
        MutableText text = MutableText.of(Text.of(message).getContent());
        text.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(legendaryWeaponColours.get(stack.getItem()))).withBold(true));
        world.getPlayers().forEach(playerEntity -> player.sendMessage(text, false));
    }
}

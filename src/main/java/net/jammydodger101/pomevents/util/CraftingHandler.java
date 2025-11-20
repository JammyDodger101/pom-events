package net.jammydodger101.pomevents.util;

import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.particle.ParticleSpawner;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CraftingHandler {
    public static List<Item> craftedItems = new ArrayList<>();

    public static void craftedItem(ItemStack stack, World world, PlayerEntity player) {
        if (world.isClient()) {
            ParticleSpawner.spawnFireworkParticles(world, player.getPos());
            return;
        }
        craftedItems.add(stack.getItem());
        tellWorldItemWasCrafted(stack, world, player);
    }

    private static void tellWorldItemWasCrafted(ItemStack stack, World world, PlayerEntity player) {
        String message = (player.getNameForScoreboard() + " has just crafted " + stack.getItem().getName(stack) + "!!!");
        world.getPlayers().forEach(playerEntity -> player.sendMessage(Text.of(message), false));
    }
}

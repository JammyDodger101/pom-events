package net.jammydodger101.pomevents.item.custom;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/*
An item that represents a heart, allowing them to use the item to gain an extra heart
 */
public class LifestealHeartItem extends Item {
    public LifestealHeartItem(Settings settings) {
        super(settings);
    }

    // Using the item adds a heart to the player's maximum health
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (isOnMaximumHealth(user)) {
            increasePlayerHealth(user);
            itemStack.decrement(1);
            return TypedActionResult.success(itemStack);
        }

        return TypedActionResult.pass(itemStack);
    }

    // Checks the player is on less than 20 hearts (40 health)
    private boolean isOnMaximumHealth(PlayerEntity user) {
        return user.getMaxHealth() < 40f;
    }

    // Adds a heart to the player's maximum health
    private void increasePlayerHealth(PlayerEntity user) {
        user.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(user.getMaxHealth()+2f);
    }
}

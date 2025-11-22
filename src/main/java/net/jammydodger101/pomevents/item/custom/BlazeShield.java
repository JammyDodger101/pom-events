package net.jammydodger101.pomevents.item.custom;

import net.jammydodger101.pomevents.util.CraftingHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/*
Powerful shield that explodes when hit, launching players back and setting them on fire
 */
public class BlazeShield extends ShieldItem implements Equipment {
    public BlazeShield(Settings settings) {
        super(settings);
    }

    // Gives the player fire resistance when holding in main or offhand
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity) {
            if (isWeaponInAHand(((PlayerEntity) entity), stack)) {
                ((PlayerEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    // Creates explosion when the player uses the item
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!user.getItemCooldownManager().isCoolingDown(this)) {

            if (world.isClient()) return TypedActionResult.fail(itemStack);
            user.setCurrentHand(hand);

            user.getItemCooldownManager().set(this, 300);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    // Checks if the blaze shield is in the main hand or off-hand
    public boolean isWeaponInAHand(PlayerEntity player, ItemStack stack) {
        return player.getStackInHand(player.getActiveHand()) == stack || player.getStackInHand(Hand.OFF_HAND) == stack;
    }

    // Ensures that multiple blaze shields cannot be crafted
    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        CraftingHandler.craftedItem(stack, world, player);
    }
}

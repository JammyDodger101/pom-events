package net.jammydodger101.pomevents.item.custom;

import net.jammydodger101.pomevents.entity.custom.SculkTridentEntity;
import net.jammydodger101.pomevents.util.CraftingHandler;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

/*
A legendary trident that causes a sculk explosion when thrown
 */
public class SculkTrident extends Item implements ProjectileItem {
    public SculkTrident(Item.Settings settings) {
        super(settings);
    }

    // Creates the sculk trident entity
    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        SculkTridentEntity tridentEntity = new SculkTridentEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
        tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
        return tridentEntity;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    // Adds damage to the item
    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }

    // Throw the trident if enough time has passed
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int timeUsed = this.getMaxUseTime(stack, user) - remainingUseTicks;
            if (timeUsed >= 10) {
                SculkTridentEntity tridentEntity = new SculkTridentEntity(world, playerEntity, stack);
                tridentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, 2.5f, 1.0f);

                world.spawnEntity(tridentEntity);
                world.playSoundFromEntity(null, tridentEntity, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (!playerEntity.isInCreativeMode()) {
                    playerEntity.getInventory().removeOne(stack);
                    ((PlayerEntity) user).getItemCooldownManager().set(this, 300);
                }
            }
        }
    }

    // Sets the player to start holding the trident
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!user.getItemCooldownManager().isCoolingDown(this)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        } else {
            return TypedActionResult.fail(itemStack);
        }
    }

    // Ensures that multiple cannot be crafted
    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        CraftingHandler.craftedItem(stack, world, player);
    }
}

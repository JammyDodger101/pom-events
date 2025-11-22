package net.jammydodger101.pomevents.item.custom;

import net.jammydodger101.pomevents.entity.custom.StormSpearEntity;
import net.jammydodger101.pomevents.util.CraftingHandler;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import java.util.Random;

/*
A legendary spear that summons lightning on hit players
When thrown, nearby entities will all be struck by lightning
 */
public class StormSpear extends Item implements ProjectileItem {
    public StormSpear(Item.Settings settings) {
        super(settings);
    }

    public static AttributeModifiersComponent createAttributeModifiers(ToolMaterial material, float baseAttackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, (baseAttackDamage + material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
    }

    // Creates the actual thrown entity
    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        StormSpearEntity tridentEntity = new StormSpearEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
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

    // Damages the spear
    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }

    // Throws the spear
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int timeUsed = this.getMaxUseTime(stack, user) - remainingUseTicks;
            if (timeUsed >= 10) {
                StormSpearEntity tridentEntity = new StormSpearEntity(world, playerEntity, stack);
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

    // Sets the player to start holding the spear and adds sound
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world.isClient()) return TypedActionResult.fail(itemStack);
        if (!user.getItemCooldownManager().isCoolingDown(this)) {
            user.setCurrentHand(hand);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_CONDUIT_AMBIENT, SoundCategory.PLAYERS, 1.0F, 1f);
            return TypedActionResult.consume(itemStack);
        } else {
            return TypedActionResult.fail(itemStack);
        }
    }

    // Spawns particles when the player is wielding the spear
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.usageTick(world, user, stack, remainingUseTicks);
        if (world.isClient()) return;
        ((ServerWorld) world).spawnParticles(ParticleTypes.OMINOUS_SPAWNING, user.getX(), user.getY()+1, user.getZ(), 3, 0,0,0,1f);

    }

    // Has a 1/4 chance (100% in thunderstorms) to summon lightning on a hit entity
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        var rand = new Random();
        if(rand.nextInt(4) == 0 || target.getWorld().getLevelProperties().isThundering()) {

            var world = attacker.getWorld();
            affectHitPlayer(world, target);
            world.playSound(attacker, attacker.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1f, 1f);
        }
        return true;
    }

    // Summons lightning at hit player, as well as setting them on fire and giving glowing
    private void affectHitPlayer(World world, LivingEntity target) {
        var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        lightning.setPosition(target.getPos());
        world.spawnEntity(lightning);

        target.setOnFireFor(6);

        target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60));

    }

    // Ensures that multiple cannot be crafted
    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        CraftingHandler.craftedItem(stack, world, player);
    }
}

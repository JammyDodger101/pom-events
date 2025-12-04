package net.jammydodger101.pomevents.mixin;

import net.jammydodger101.pomevents.explosion.custom.TestExplosionBehaviour;
import net.jammydodger101.pomevents.item.ModItems;
import net.jammydodger101.pomevents.particle.ParticleSpawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

/*
Activates the blaze shield whenever the player is damaged and it is active
 */
@Mixin(LivingEntity.class)
public class BlazeShieldMixin {
    @Unique
    private long lastUseTime = (long) -1;

    // Activates the shield if the player is damaged while it is being used
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"))
    public void activateShield(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        LivingEntity entity = (LivingEntity)(Object) this;
        World world = entity.getWorld();
        if (attacker == null) {
            return;
        }

        ItemStack stack = attacker.getWeaponStack();
        if (stack != null) {
            breakShieldIfHitWithAxe(world, stack, entity);
        }

        if (entity.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.BLAZE_SHIELD) || entity.getStackInHand(Hand.OFF_HAND).isOf(ModItems.BLAZE_SHIELD)) {
            if (lastUseTime != -1) {
                int cooldownTime = 10;
                if (entity.getWorld().getTime() - lastUseTime < (cooldownTime * 20L)) {
                    return;
                }
            }
            lastUseTime = entity.getWorld().getTime();

            createFireExplosion(entity.getBlockPos(), world, entity, 3);
            ((PlayerEntity) entity).getItemCooldownManager().set(ModItems.BLAZE_SHIELD, 300);

            if (attacker != null) {
                attacker.setOnFireFor(100);
            }
        }
    }

    // Forces the player to stop using the shield if they are hit with an axe
    @Unique
    private void breakShieldIfHitWithAxe(World world, ItemStack stack, LivingEntity entity) {
        if (stack.isIn(ItemTags.AXES)) {
            entity.stopUsingItem();
            world.playSound(null, entity.getBlockPos(), SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.PLAYERS, 1, 1);
        }
    }

    // Creates an explosion and launches nearby players
    @Unique
    public void createFireExplosion(BlockPos centre, World world, LivingEntity player, int size) {
        if (!world.isClient()) {
            world.createExplosion(player, null, new TestExplosionBehaviour(false, true, Optional.of(1.0f), Optional.empty(), 1f), centre.getX(), centre.getY(), centre.getZ(), 2f, false, World.ExplosionSourceType.TNT);
            ParticleSpawner.spawnFireExplosionParticles(world, centre.toBottomCenterPos(), 0.2, 100, 0.8);
        }
        launchEntitiesWithinArea(world, player, size);
    }

    // Launches players within a certain area away from the player
    @Unique
    private void launchEntitiesWithinArea(World world, LivingEntity player, int size) {
        Vec3d offset = new Vec3d(size, size, size);
        List<Entity> players = world.getOtherEntities(player, new Box(player.getPos().subtract(offset), player.getPos().add(offset)));
        for (Entity entity : players) {
            Vec3d vel = entity.getPos().subtract(player.getPos()).multiply(1);
            entity.velocityModified = true;
            entity.addVelocity(vel);
            entity.setOnFireFor(100);
        }
    }
}

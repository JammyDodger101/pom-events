package net.jammydodger101.pomevents.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

/*
Entity that is shot from the gale bow and summons a wind charge when landing
 */
public class GaleBowArrowEntity extends PersistentProjectileEntity {
    private static final ExplosionBehavior EXPLOSION_BEHAVIOR;
    private int duration = 200;

    public GaleBowArrowEntity(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public GaleBowArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityType.ARROW, owner, world, stack, shotFrom);
    }

    public GaleBowArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityType.ARROW, x, y, z, world, stack, shotFrom);
    }

    // Adds particles when the arrow is in the ground
    public void tick() {
        super.tick();
        if (this.getWorld().isClient && !this.inGround) {
            this.getWorld().addParticle(ParticleTypes.GUST_EMITTER_LARGE, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    // Explodes and removes the arrow when hitting an entity
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            explodeWithBurst(serverWorld, target.getPos());
            this.discard();
        }
    }

    // Explodes and removes the arrow when hitting a block
    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        super.onBlockHit(hitResult);
        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            Vec3d hitPos = calculateHitPos(hitResult);
            explodeWithBurst(serverWorld, hitPos);
            this.discard();
        }
    }

    // Did not write this code, no clue why it does this lol
    private Vec3d calculateHitPos(BlockHitResult hitResult) {
        Vec3i vec3i = hitResult.getSide().getVector();
        Vec3d vec3d = Vec3d.of(vec3i).multiply(0.25, 0.25, 0.25);
        return hitResult.getPos().add(vec3d);
    }

    // Creates an explosion
    private void explodeWithBurst(ServerWorld serverWorld, Vec3d pos) {
        serverWorld.createExplosion(this, null, EXPLOSION_BEHAVIOR, pos.getX(), pos.getY(), pos.getZ(), 1.2F, false, World.ExplosionSourceType.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Duration")) {
            this.duration = nbt.getInt("Duration");
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Duration", this.duration);
    }

    //Pretty sure this does not work
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }

    static {
        EXPLOSION_BEHAVIOR = new AdvancedExplosionBehavior(true, false, Optional.of(1.22F), Registries.BLOCK.getEntryList(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
    }
}

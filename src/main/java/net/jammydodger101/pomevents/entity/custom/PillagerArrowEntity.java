package net.jammydodger101.pomevents.entity.custom;

import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PillagerArrowEntity extends PersistentProjectileEntity {
    private int duration = 200;
    private boolean exploding = true;

    public PillagerArrowEntity(EntityType<? extends SpectralArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public PillagerArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom, Boolean exploding) {
        super(EntityType.SPECTRAL_ARROW, owner, world, stack, shotFrom);
        this.exploding = exploding;
    }

    public PillagerArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityType.SPECTRAL_ARROW, x, y, z, world, stack, shotFrom);
    }

    public void tick() {
        super.tick();
        if (this.getWorld().isClient && !this.inGround) {
            this.getWorld().addParticle(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }

    }

    protected void onHit(LivingEntity target) {
        super.onHit(target);

        this.discard();

        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.GLOWING, this.duration, 0);
        target.addStatusEffect(statusEffectInstance, this.getEffectCause());

        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            if (!(target instanceof VexEntity) && exploding) {
                explodeAndSummon(serverWorld, target.getBlockPos(), true);
            }

        }
    }



    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        super.onBlockHit(hitResult);

        this.discard();

        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            if (exploding) {
                explodeAndSummon(serverWorld, hitResult.getBlockPos(), false);
            }

        }
    }

    private void explodeAndSummon(ServerWorld serverWorld, BlockPos pos, boolean summon) {
        serverWorld.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 2.0f, World.ExplosionSourceType.MOB);
        // BlockPos blockPos = new BlockPos((int) hitResult.getPos().getX(), (int) hitResult.getPos().getY(), (int) hitResult.getPos().getZ());

        if (summon) {
            Entity vexEntity = EntityType.VEX.create(serverWorld, null, pos, SpawnReason.MOB_SUMMONED, false, false);
            Entity vexEntity1 = EntityType.VEX.create(serverWorld, null, pos, SpawnReason.MOB_SUMMONED, false, false);
            serverWorld.spawnEntity(vexEntity);
            serverWorld.spawnEntity(vexEntity1);
        }

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

    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }
}

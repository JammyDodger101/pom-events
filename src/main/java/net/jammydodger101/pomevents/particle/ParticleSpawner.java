package net.jammydodger101.pomevents.particle;

import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

/*
Spawns particles in different patterns
 */
public class ParticleSpawner {
    // Spawns particles evenly in a circle using sunflower seed placement algorithms
    public static <T extends ParticleEffect> void spawnSunflowerParticles(T particle, int numParticles, ServerWorld serverWorld, Vec3d center, double speed, float size, boolean higherSpeedsOnOutside) {
        float goldenAngle = 137.5f;
        for (int i = 0; i < numParticles; i++) {
            double theta = i * goldenAngle;
            double radius = Math.sqrt(i) * size;
            double thetaRad = Math.toRadians(theta);

            Vec3d particlePosition = new Vec3d(center.x + (radius*Math.cos(thetaRad)), center.y, center.z + (radius*Math.sin(thetaRad)));

            if (higherSpeedsOnOutside) {
                speed *= ((double) i /numParticles);
            }

            serverWorld.spawnParticles(particle, particlePosition.x, particlePosition.y, particlePosition.z, 1, 0, 0, 0, speed);
        }
    }

    // Spawns particles in a ring
    public static <T extends ParticleEffect> void spawnCircleCircumferenceParticles(T particle, int numParticles, ServerWorld serverWorld, Vec3d center, float radius, float speed, double randomOffsetSize) {
        for (int i = 0; i < numParticles; i++) {
            double angle = ((2*Math.PI)/numParticles) * i;
            Vec3d particlePosition = new Vec3d(center.x + (radius*Math.cos(angle)), center.y, center.z + (radius*Math.sin(angle)));
            Random random = new Random();
            Vec3d randomOffsets = new Vec3d(random.nextGaussian()*randomOffsetSize, random.nextGaussian()*randomOffsetSize, random.nextGaussian()*randomOffsetSize);

            serverWorld.spawnParticles(particle, particlePosition.x, particlePosition.y, particlePosition.z, 1, randomOffsets.x, randomOffsets.y, randomOffsets.z, speed);
        }
    }

    // Spawns a dust pillar (the particles emitted when macing someone)
    public static void createDustPillar(BlockState blockState, World world, Vec3d pos, double speed, int numPillars, int radius) {
        Random random = new Random();
        BlockStateParticleEffect stateParticleEffect = new BlockStateParticleEffect(ParticleTypes.DUST_PILLAR, blockState);
        if (!world.isClient()) {
            for (int i = 0; i < numPillars; i++) {
                Vec3d rPos = new Vec3d(pos.x + (random.nextGaussian()*radius), pos.y+1, pos.z+(random.nextGaussian()*radius));
                ((ServerWorld) world).spawnParticles(stateParticleEffect, rPos.x, rPos.y, rPos.z, 5, 0, 0, 0, speed);
            }
        }
    }

    // Spawns the same particles as when a firework explodes
    public static void spawnFireworkParticles(World world, Vec3d pos, int numParticles) {
        if (!(world instanceof ClientWorld)) return;
        Random random = new Random();
        double d = random.nextGaussian() * 0.05;
        double e = random.nextGaussian() * 0.05;

        for(int i = 0; i < numParticles; ++i) {
            double f = random.nextGaussian() * 0.15 + d;
            double g = random.nextGaussian() * 0.15 + e;
            double h = random.nextDouble() * 0.5;
            for (PlayerEntity player : world.getPlayers()) {
                player.getWorld().addParticle(ParticleTypes.FIREWORK, true, pos.x, pos.y, pos.z, f, h, g);
            }
        }
    }

    // Spawns the particles for blaze shield
    public static void spawnFireExplosionParticles(World world, Vec3d pos, double speed, int numParticles, double randomOffsetSize) {
        Random random = new Random();

        for(int i = 0; i < numParticles; ++i) {
            Vec3d randomOffsets = new Vec3d(random.nextGaussian()*randomOffsetSize, random.nextGaussian()*randomOffsetSize, random.nextGaussian()*randomOffsetSize);

            ((ServerWorld) world).spawnParticles(ParticleTypes.LAVA, pos.x, pos.y, pos.z, 1, randomOffsets.x, randomOffsets.y, randomOffsets.z, speed);
        }
    }
}

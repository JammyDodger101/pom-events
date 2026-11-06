package net.jammydodger101.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.effects.SpawnParticlesEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ParticleSpawner {

    // Spawns particles evenly in a circle using sunflower seed placement algorithms
    public static <T extends SpawnParticlesEffect> void spawnSunflowerParticles(ParticleOptions particle, int numParticles, ServerLevel serverWorld, Vec3 center, double speed, float size, boolean higherSpeedsOnOutside) {
        float goldenAngle = 137.5f;
        for (int i = 0; i < numParticles; i++) {
            double theta = i * goldenAngle;
            double radius = Math.sqrt(i) * size;
            double thetaRad = Math.toRadians(theta);

            Vec3 particlePosition = new Vec3(center.x + (radius*Math.cos(thetaRad)), center.y, center.z + (radius*Math.sin(thetaRad)));

            if (higherSpeedsOnOutside) {
                speed *= ((double) i /numParticles);
            }

            serverWorld.sendParticles(particle, particlePosition.x, particlePosition.y, particlePosition.z, 1, 0, 0, 0, speed);
            //serverWorld.addParticle(particle, particlePosition.x, particlePosition.y, particlePosition.z, 0, 0, 0);
        }
    }

    public static void createDustPillar(BlockState blockState, Level world, Vec3 pos, double speed, int numPillars, int radius) {
        Random random = new Random();
        BlockParticleOption blockParticleOption = new BlockParticleOption(ParticleTypes.DUST_PILLAR, blockState);
        if (!world.isClientSide()) {
            for (int i = 0; i < numPillars; i++) {
                Vec3 rPos = new Vec3(pos.x + (random.nextGaussian()*radius), pos.y+1, pos.z+(random.nextGaussian()*radius));
                ((ServerLevel) world).sendParticles(blockParticleOption, rPos.x, rPos.y, rPos.z, 5, 0 ,0 ,0 , speed);
            }
        }
    }

    // Spawns the same particles as when a firework explodes
    public static void spawnFireworkParticles(Level world, Vec3 pos, int numParticles) {
        if ((world.isClientSide())) return;
        Random random = new Random();
        double d = random.nextGaussian() * 0.05;
        double e = random.nextGaussian() * 0.05;
        ServerLevel serverLevel = ((ServerLevel) world);

        for(int i = 0; i < numParticles; ++i) {
            double f = random.nextGaussian() * 0.15 + d;
            double g = random.nextGaussian() * 0.15 + e;
            double h = random.nextDouble() * 0.5;
            serverLevel.sendParticles(ParticleTypes.FIREWORK, pos.x, pos.y, pos.z, 3, f, h, g, 0.5);

        }
    }
}

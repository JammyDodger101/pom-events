package net.jammydodger101.pomevents.particle;

import it.unimi.dsi.fastutil.ints.IntList;
import net.jammydodger101.pomevents.PomEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.*;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ParticleSpawner {
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

    public static <T extends ParticleEffect> void spawnCircleCircumferenceParticles(T particle, int numParticles, ServerWorld serverWorld, Vec3d center, float radius, float speed, double randomOffsetSize) {
        for (int i = 0; i < numParticles; i++) {
            double angle = ((2*Math.PI)/numParticles) * i;
            Vec3d particlePosition = new Vec3d(center.x + (radius*Math.cos(angle)), center.y, center.z + (radius*Math.sin(angle)));
            Random random = new Random();
            Vec3d randomOffsets = new Vec3d(random.nextGaussian()*randomOffsetSize, random.nextGaussian()*randomOffsetSize, random.nextGaussian()*randomOffsetSize);

            serverWorld.spawnParticles(particle, particlePosition.x, particlePosition.y, particlePosition.z, 1, randomOffsets.x, randomOffsets.y, randomOffsets.z, speed);
        }
    }

    public static void createDustPillar(BlockState blockState, World world, Vec3d pos, double speed, int numPillars, int radius) {
        Random random = new Random();
        BlockStateParticleEffect stateParticleEffect = new BlockStateParticleEffect(ParticleTypes.DUST_PILLAR, blockState);
        if (!world.isClient()) {
            for (int i = 0; i < numPillars; i++) {
                Vec3d vel = new Vec3d(random.nextGaussian() / 30.0, speed + random.nextGaussian() / 2.0, random.nextGaussian() / 30.0);
                Vec3d rPos = new Vec3d(pos.x + (random.nextGaussian()*radius), pos.y+1, pos.z+(random.nextGaussian()*radius));
                //Particle pillarParticle = dustPillarFactory.createParticle(stateParticleEffect, (ClientWorld) world, pos.x, pos.y+1, pos.z, vel.x, vel.y, vel.z);
                //world.addParticle(new BlockStateParticleEffect(ParticleTypes.DUST_PILLAR, Blocks.GRASS_BLOCK.getDefaultState()), true, pos.x + random.nextGaussian(), pos.y+random.nextGaussian(), pos.z+random.nextGaussian(), vel.x, vel.y, vel.z);
                ((ServerWorld) world).spawnParticles(stateParticleEffect, rPos.x, rPos.y, rPos.z, 5, 0, 0, 0, speed);
            }
        }
    }

    public static void spawnFireworkParticles(World world, Vec3d pos, int numParticles) {
        if (!(world instanceof ClientWorld clientWorld)) return;
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
            //clientWorld.addParticle(ParticleTypes.FIREWORK, true, pos.x, pos.y, pos.z, f, h, g);
            //((ServerWorld) world).spawnParticles(ParticleTypes.FIREWORK, pos.x, pos.y, pos.z, 1, 0, 0, 0, 1);
        }
    }

    public static void spawnFireExplosionParticles(World world, Vec3d pos, double speed, int numParticles, double randomOffsetSize) {
        Random random = new Random();

        for(int i = 0; i < numParticles; ++i) {
            Vec3d velocity = new Vec3d(random.nextGaussian() * speed, random.nextGaussian() * speed, random.nextGaussian() * speed);
            Vec3d randomOffsets = new Vec3d(random.nextGaussian()*randomOffsetSize, random.nextGaussian()*randomOffsetSize, random.nextGaussian()*randomOffsetSize);

            ((ServerWorld) world).spawnParticles(ParticleTypes.LAVA, pos.x, pos.y, pos.z, 1, randomOffsets.x, randomOffsets.y, randomOffsets.z, speed);
        }
    }
}

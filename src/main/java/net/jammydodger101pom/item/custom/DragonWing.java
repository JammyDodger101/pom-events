package net.jammydodger101pom.item.custom;

import net.jammydodger101pom.particle.ParticleSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.BlockUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class DragonWing extends Item {
    double largeHPower = -4f;
    double largeVPower = 2f;
    int maxTpRange = 500;

    public DragonWing(Properties properties) {
        super(properties);
    }

    @Override
    public void hurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
        Level level = attacker.level();

        if (level instanceof ServerLevel serverLevel) {
            if (!((Player) attacker).getCooldowns().isOnCooldown(itemStack)) {
                if (mob instanceof ServerPlayer serverPlayer) {
                    serverPlayer.setSpawnExtraParticlesOnFall(true);
                }

                addFlairs(serverLevel, mob);
                launchEntity(mob, attacker, largeHPower, largeVPower);

                ((Player) attacker).getCooldowns().addCooldown(itemStack, 300);
            }
        }

        super.hurtEnemy(itemStack, mob, attacker);
    }

    private void addFlairs(ServerLevel serverLevel, LivingEntity target) {
        serverLevel.playSound(null, target.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1.0f, 1.0f);
        serverLevel.playSound(null, target.blockPosition(), SoundEvents.MACE_SMASH_GROUND_HEAVY, SoundSource.PLAYERS, 1.0f, 1.0f);
        ParticleSpawner.spawnSunflowerParticles(ParticleTypes.CLOUD, 150, serverLevel, target.position(), 0.3f, 0.5f, true);
        ParticleSpawner.createDustPillar(Blocks.CRYING_OBSIDIAN.defaultBlockState(), serverLevel, target.position(), 1, 100 , 3);
    }

    private void launchEntity(LivingEntity target, LivingEntity attacker, double hPower, double vPower) {
        Vec3 vec3 = attacker.position().subtract(target.position());
        Vec3 vec4 = vec3.normalize().multiply(hPower, hPower, hPower);
        target.syncVelocity = true;
        target.addDeltaMovement(new Vec3(vec4.x, vPower, vec4.z));

    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.getCooldowns().isOnCooldown(player.getItemInHand(hand))) {
            return InteractionResult.FAIL;
        }

        if (level instanceof ServerLevel serverLevel) {
            if (!teleportToClosestPlayer(player, serverLevel)) {
                chorusTeleport(player, serverLevel);
            }
            player.getCooldowns().addCooldown(player.getItemInHand(hand), 30);
            serverLevel.sendParticles(ParticleTypes.PORTAL, player.getX(), player.getY(), player.getZ(), 10, 0, 0,0, 3f);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    private boolean teleportToClosestPlayer(Player player, ServerLevel serverLevel) {
        Player closestPlayer = serverLevel.getNearestPlayer(player, maxTpRange);
        if (closestPlayer == null) {
            return false;
        }

        if (closestPlayer == player) {
            return false;
        }

        BlockPos oldPos = player.blockPosition();
        player.teleportTo(closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ());

        serverLevel.gameEvent(GameEvent.TELEPORT, player.blockPosition(), GameEvent.Context.of(player));

        serverLevel.playSound(null, closestPlayer.blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS);

        BlockPos target = player.blockPosition();
        serverLevel.levelEvent(2017, oldPos, BlockUtil.clampedPackDifferenceInPosition(oldPos, target, 127, 127, 127));

        player.resetFallDistance();

        return true;
    }

    private void chorusTeleport(Player player, ServerLevel serverLevel) {
        for(int attempt = 0; attempt < 16; ++attempt) {
            double xx = player.getX() + (player.getRandom().nextDouble() - 0.5) * 16;
            double yy = Mth.clamp(player.getY() + (player.getRandom().nextDouble() - 0.5) * 16, (double) serverLevel.getMinY(), (double) (serverLevel.getMinY() + (serverLevel).getLogicalHeight() - 10));
            double zz = player.getZ() + (player.getRandom().nextDouble() - 0.5) * 16;
            if (player.isPassenger()) {
                player.stopRiding();
            }

            Vec3 oldPos = player.position();
            if (player.randomTeleport(xx, yy, zz, true, BlockTags.CONSUMABLE_DOES_NOT_TELEPORT_TO)) {
                serverLevel.gameEvent(GameEvent.TELEPORT, oldPos, GameEvent.Context.of(player));
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS);

                BlockPos origin = BlockPos.containing(oldPos);
                BlockPos target = player.blockPosition();
                serverLevel.levelEvent(2017, origin, BlockUtil.clampedPackDifferenceInPosition(origin, target, 127, 127, 127));

                player.resetFallDistance();
                player.resetCurrentImpulseContext();
            }
        }
    }
}

package net.jammydodger101.pomevents.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class DragonWing extends AxeItem {
    double largeHPower = -2f;
    double largeVPower = 1f;
    int maxTpRange = 500;

    public DragonWing(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        use(context.getWorld(),context.getPlayer(),context.getHand());
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            if (teleportToClosestPlayer(user, ((ServerWorld) world))) {
                return TypedActionResult.success(user.getStackInHand(hand));
            }
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = attacker.getWorld();
        if (world.isClient()) {
            return false;
        }
        ServerWorld serverWorld = ((ServerWorld) world);

        if (!((PlayerEntity) attacker).getItemCooldownManager().isCoolingDown(this)) {
            serverWorld.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS);

            if (target instanceof ServerPlayerEntity serverPlayerTarget) {
                serverPlayerTarget.setSpawnExtraParticlesOnFall(true);
            }

            spawnSunflowerParticles(100, serverWorld, target.getPos());

            launchEntity(target, attacker, largeHPower, largeVPower);
            ((PlayerEntity) attacker).getItemCooldownManager().set(this, 300);
        }

        return super.postHit(stack, target, attacker);
    }

    public void launchEntity(LivingEntity target, LivingEntity attacker, double hPower, double vPower) {
        Vec3d vec3d = attacker.getPos().subtract(target.getPos());
        Vec3d vec3d2 = vec3d.normalize().multiply(hPower);
        target.velocityModified = true;
        target.addVelocity(vec3d2.x, vPower, vec3d2.z);
    }

    private void spawnSunflowerParticles(int numParticles, ServerWorld serverWorld, Vec3d center) {
        float goldenAngle = 137.5f;
        float speed = 0f;
        for (int i = 0; i < numParticles; i++) {
            double theta = i * goldenAngle;
            double radius = Math.sqrt(i);
            double thetaRad = Math.toRadians(theta);

            Vec3d particleVelocity = new Vec3d(i*Math.cos(thetaRad)*speed,0,i*Math.sin(thetaRad)*speed);
            Vec3d particlePosition = new Vec3d(center.x + (radius*Math.cos(thetaRad)), center.y + 1, center.z + (radius*Math.sin(thetaRad)));

            serverWorld.getPlayers().forEach(serverPlayerEntity -> serverPlayerEntity.getWorld().addParticle(ParticleTypes.CLOUD, true, particlePosition.x, particlePosition.y, particlePosition.z, particleVelocity.x, particleVelocity.y, particleVelocity.z));
        }
    }

    private void chorusTeleport(PlayerEntity user, ServerWorld world) {
        for (int i = 0; i < 16; ++i) {
            SoundCategory soundCategory;
            SoundEvent soundEvent;
            double d = user.getX() + (user.getRandom().nextDouble() - 0.5) * 16.0;
            double e = MathHelper.clamp(user.getY() + (double)(user.getRandom().nextInt(16) - 8), (double)world.getBottomY(), (double)(world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1));
            double f = user.getZ() + (user.getRandom().nextDouble() - 0.5) * 16.0;
            if (user.hasVehicle()) {
                user.stopRiding();
            }
            Vec3d vec3d = user.getPos();
            if (!user.teleport(d, e, f, true)) continue;
            world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(user));
            soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
            soundCategory = SoundCategory.PLAYERS;

            world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, soundCategory, 1f, 1f);
            user.onLanding();
            break;
        }
    }

    private boolean teleportToClosestPlayer(PlayerEntity user, ServerWorld world) {
        PlayerEntity closestPlayer = world.getClosestPlayer(user.getX(), user.getY(), user.getZ(), maxTpRange, true);
        if (closestPlayer == null) {
            return false;
        }
        user.teleport(world, closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ()-1, null, user.getYaw(), user.getPitch());
        return true;
    }
}



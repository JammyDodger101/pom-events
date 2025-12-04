package net.jammydodger101.pomevents.explosion.custom;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.Optional;

public class TestExplosionBehaviour extends AdvancedExplosionBehavior {
    private final float power;

    public TestExplosionBehaviour(boolean destroyBlocks, boolean damageEntities, Optional<Float> knockbackModifier, Optional<RegistryEntryList<Block>> immuneBlocks, float power) {
        super(destroyBlocks, damageEntities, knockbackModifier, immuneBlocks);
        this.power = power;
    }

    @Override
    public float calculateDamage(Explosion explosion, Entity entity) {
        float power = this.power * 2.0F;
        Vec3d vec3d = explosion.getPosition();
        double distance = Math.sqrt(entity.squaredDistanceTo(vec3d));
        double impact = (1.0 - distance / (power)) * (double)Explosion.getExposure(vec3d, entity);
        return (float)((impact * impact + impact) / 2.0 * 7.0 * power + 1.0);
    }
}

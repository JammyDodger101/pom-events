package net.jammydodger101pom.entity.client.prismarine_anchor;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

public class PrismarineAnchorRenderState extends EntityRenderState {
    public float xRot;
    public float yRot;
    public Vec3 lineOriginOffset;

    public PrismarineAnchorRenderState() {
        this.lineOriginOffset = Vec3.ZERO;
    }
}

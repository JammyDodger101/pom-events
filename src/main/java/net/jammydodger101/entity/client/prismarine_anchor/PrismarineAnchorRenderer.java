package net.jammydodger101.entity.client.prismarine_anchor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.jammydodger101.PomEvents2;
import net.jammydodger101.entity.client.ModModelLayerLocations;
import net.jammydodger101.entity.custom.ThrownPrismarineAnchor;
import net.jammydodger101.entity.custom.ThrownSculkTrident;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PrismarineAnchorRenderer extends EntityRenderer<ThrownPrismarineAnchor, PrismarineAnchorRenderState> {
    //public static final Identifier TRIDENT_LOCATION = Identifier.withDefaultNamespace("textures/entity/trident/trident.png");
    private PrismarineAnchorModel model;

    public PrismarineAnchorRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PrismarineAnchorModel(context.bakeLayer(ModModelLayerLocations.PRISMARINE_ANCHOR));
    }

    @Override
    public PrismarineAnchorRenderState createRenderState() {
        return new PrismarineAnchorRenderState();
    }

    @Override
    public void submit(PrismarineAnchorRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.rotateDegrees(Axis.YP, state.yRot - 90.0F);
        poseStack.rotateDegrees(Axis.ZP, state.xRot + 90.0F);
        submitNodeCollector.submitModel(this.model, state, poseStack, RenderTypes.entitySolid(getTextureLocation()), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);

        float xa = (float)state.lineOriginOffset.x;
        float ya = (float)state.lineOriginOffset.y;
        float za = (float)state.lineOriginOffset.z;
        float width = Minecraft.getInstance().gameRenderer.gameRenderState().windowRenderState.appropriateLineWidth;
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, buffer) -> {
            for(int i = 0; i < 16; ++i) {
                float a0 = (float) i /16f;
                float a1 = ((float)i + 1)/ 16f;
                stringVertex(xa, ya, za, buffer, pose, a0, a1, width);
                stringVertex(xa, ya, za, buffer, pose, a1, a0, width);
            }

        });

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    private static void stringVertex(final float xa, final float ya, final float za, final VertexConsumer stringBuffer, final PoseStack.Pose stringPose, final float aa, final float nexta, final float width) {
        float x = xa * aa;
        float y = ya * (aa * aa + aa) * 0.5F + 0.25F;
        float z = za * aa;
        float nx = xa * nexta - x;
        float ny = ya * (nexta * nexta + nexta) * 0.5F + 0.25F - y;
        float nz = za * nexta - z;
        float length = Mth.sqrt(nx * nx + ny * ny + nz * nz);
        nx /= length;
        ny /= length;
        nz /= length;
        stringBuffer.addVertex(stringPose, x, y, z).setColor(-16777216).setNormal(stringPose, nx, ny, nz).setLineWidth(width);
    }

    protected AABB getBoundingBoxForCulling(final ThrownPrismarineAnchor entity, final float partialTicks) {
        return super.getBoundingBoxForCulling(entity, partialTicks).inflate(1.5);
    }

    @Override
    public void extractRenderState(ThrownPrismarineAnchor entity, PrismarineAnchorRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        Player owner = entity.getPlayerOwner();
        if (owner == null) {
            state.lineOriginOffset = Vec3.ZERO;
        } else {
            float swing = owner.getSwingAnimation(partialTicks);
            float swing2 = Mth.sin((double)(Mth.sqrt(swing) * 3.1415927F));
            Vec3 playerPos = owner.position();
            Vec3 hookPos = entity.getPosition(partialTicks).add(0.0, 0.25, 0.0);
            state.lineOriginOffset = playerPos.subtract(hookPos);
        }
    }

    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(PomEvents2.MOD_ID, "textures/entity/sculk_trident/base.png");
    }
}

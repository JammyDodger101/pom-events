package net.jammydodger101.entity.client.sculk_trident;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.jammydodger101.PomEvents2;
import net.jammydodger101.entity.client.ModModelLayerLocations;
import net.jammydodger101.entity.custom.ThrownSculkTrident;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;

public class SculkTridentRenderer extends EntityRenderer<ThrownSculkTrident, SculkTridentRenderState> {
    //public static final Identifier TRIDENT_LOCATION = Identifier.withDefaultNamespace("textures/entity/trident/trident.png");
    private SculkTridentModel model;

    public SculkTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SculkTridentModel(context.bakeLayer(ModModelLayerLocations.SCULK_TRIDENT));
    }

    @Override
    public SculkTridentRenderState createRenderState() {
        return new SculkTridentRenderState();
    }

    @Override
    public void submit(SculkTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.rotateDegrees(Axis.YP, state.yRot - 90.0F);
        poseStack.rotateDegrees(Axis.ZP, state.xRot + 90.0F);
        submitNodeCollector.submitModel(this.model, state, poseStack, RenderTypes.entityCutout(getTextureLocation()), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);


        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    protected AABB getBoundingBoxForCulling(final ThrownSculkTrident entity, final float partialTicks) {
        return super.getBoundingBoxForCulling(entity, partialTicks).inflate(1.5);
    }

    @Override
    public void extractRenderState(ThrownSculkTrident entity, SculkTridentRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
    }

    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(PomEvents2.MOD_ID, "textures/entity/sculk_trident/sculk_trident.png");
    }


}

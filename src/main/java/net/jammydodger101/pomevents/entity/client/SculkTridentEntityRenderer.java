package net.jammydodger101.pomevents.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jammydodger101.pomevents.PomEvents;
import net.jammydodger101.pomevents.entity.custom.SculkTridentEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class SculkTridentEntityRenderer extends EntityRenderer<SculkTridentEntity> {
    public static final Identifier TEXTURE = Identifier.of(PomEvents.MOD_ID,"textures/entity/sculk_trident.png");
    private final SculkTridentProjectileModel model;

    public SculkTridentEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new SculkTridentProjectileModel(context.getPart(EntityModelLayers.TRIDENT));
    }

    @Override
    public Identifier getTexture(SculkTridentEntity entity) {
        return TEXTURE;
    }

    public void render(SculkTridentEntity tridentEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, tridentEntity.prevYaw, tridentEntity.getYaw()) - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, tridentEntity.prevPitch, tridentEntity.getPitch()) + 90.0F));
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(this.getTexture(tridentEntity)), false, tridentEntity.isEnchanted());
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
        super.render(tridentEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}

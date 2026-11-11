package net.jammydodger101pom.entity.client.prismarine_anchor;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class PrismarineAnchorModel extends EntityModel<EntityRenderState> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    //public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLoca("modid", "custommodel"), "main");
    private final ModelPart prismarine_anchor;

    public PrismarineAnchorModel(ModelPart root) {
        super(root);
        this.prismarine_anchor = root.getChild("prismarine_anchor");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition prismarine_anchor = partdefinition.addOrReplaceChild("prismarine_anchor", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -10.0F, 7.25F, 2.0F, 18.1F, 1.5F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.1F, -18.0F, 7.0F, 2.5F, 5.6F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.6F, -18.2F, 8.0F, 3.5F, 7.8F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-9.25F, -21.0F, 7.75F, 2.5F, 8.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-9.75F, -21.0F, 8.0F, 3.5F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-14.4F, -18.0F, 7.0F, 2.5F, 5.6F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-14.9F, -18.2F, 8.0F, 3.5F, 7.8F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-8.75F, 9.0F, 7.5F, 1.5F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-9.25F, -10.0F, 7.0F, 2.5F, 18.35F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-10.0F, 9.0F, 8.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = prismarine_anchor.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-27.0F, -3.0F, 8.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r2 = prismarine_anchor.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, -2.9F, 8.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r3 = prismarine_anchor.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -4.25F, 1.0F, 2.0F, 4.25F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.25F, -2.25F, 0.5F, 1.5F, 2.25F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 8.0F, 7.0F, 0.0F, 0.0F, 1.9635F));

        PartDefinition cube_r4 = prismarine_anchor.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.75F, -2.25F, 0.5F, 1.5F, 2.25F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, -4.25F, 1.0F, 2.0F, 4.25F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 8.0F, 7.0F, 0.0F, 0.0F, -1.9635F));

        PartDefinition cube_r5 = prismarine_anchor.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-0.25F, -6.0F, 1.0F, 2.5F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.0F, -4.0F, 0.25F, 2.0F, 3.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -12.0F, 7.0F, 0.0F, 0.0F, 1.9635F));

        PartDefinition cube_r6 = prismarine_anchor.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-0.25F, -6.0F, 1.0F, 2.5F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.0F, -4.0F, 0.25F, 2.0F, 3.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -10.0F, 7.0F, 0.0F, 0.0F, 2.7489F));

        PartDefinition cube_r7 = prismarine_anchor.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-2.25F, -6.0F, 1.0F, 2.5F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, -4.0F, 0.25F, 2.0F, 3.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -10.0F, 7.0F, 0.0F, 0.0F, -2.7489F));

        PartDefinition cube_r8 = prismarine_anchor.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-2.25F, -6.0F, 1.0F, 2.5F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, -4.0F, 0.25F, 2.0F, 3.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -12.0F, 7.0F, 0.0F, 0.0F, -1.9635F));

        PartDefinition cube_r9 = prismarine_anchor.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.5F, -0.25F, 2.5F, 2.5F, 2.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 7.0F, 7.0F, 0.0F, 0.0F, -2.3562F));

        PartDefinition cube_r10 = prismarine_anchor.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 0).addBox(-2.25F, -2.25F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 8.0F, 7.0F, 0.0F, 0.0F, -2.3562F));

        PartDefinition cube_r11 = prismarine_anchor.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-7.25F, 0.0F, 0.2F, 4.25F, 1.75F, 1.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.25F, -5.0F, 7.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition cube_r12 = prismarine_anchor.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 0).addBox(3.0F, 0.0F, 0.2F, 4.25F, 1.75F, 1.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.75F, -5.0F, 7.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition cube_r13 = prismarine_anchor.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 0.0F, 0.2F, 3.0F, 1.75F, 1.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.25F, -11.0F, 7.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition cube_r14 = prismarine_anchor.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 0).addBox(3.0F, 0.0F, 0.2F, 3.0F, 1.75F, 1.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.75F, -11.0F, 7.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition cube_r15 = prismarine_anchor.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -0.25F, 5.0F, 5.0F, 2.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -13.0F, 7.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r16 = prismarine_anchor.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.25F, 1.15F, 3.5F, 3.5F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -3.0F, 0.4F, 2.5F, 3.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.9F, -18.0F, 6.85F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r17 = prismarine_anchor.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -13.5F, 1.0F, 3.5F, 13.85F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.5F, -10.0F, 0.5F, 2.5F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.3F, -18.2F, 7.0F, 0.0F, 0.0F, 0.3054F));

        PartDefinition cube_r18 = prismarine_anchor.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.5F, 1.0F, 3.5F, 3.5F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, -2.0F, 0.65F, 2.5F, 2.5F, 0.7F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -20.5F, 7.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r19 = prismarine_anchor.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -3.25F, 1.15F, 3.5F, 3.5F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.0F, -3.0F, 0.4F, 2.5F, 3.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1F, -18.0F, 6.85F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r20 = prismarine_anchor.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -13.5F, 1.0F, 3.5F, 13.85F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, -10.0F, 0.5F, 2.5F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -18.2F, 7.0F, 0.0F, 0.0F, -0.3054F));

        PartDefinition cube_r21 = prismarine_anchor.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -0.05F, 6.0F, 3.0F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -12.0F, 7.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r22 = prismarine_anchor.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -1.0F, -0.05F, 6.0F, 3.0F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -12.0F, 7.0F, 0.0F, 0.0F, 0.3927F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }
}

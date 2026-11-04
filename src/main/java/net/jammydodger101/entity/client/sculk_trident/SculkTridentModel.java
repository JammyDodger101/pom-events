package net.jammydodger101.entity.client.sculk_trident;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class SculkTridentModel extends EntityModel<EntityRenderState> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    //public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLoca("modid", "custommodel"), "main");
    private final ModelPart bb_main;

    public SculkTridentModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(32, 0).addBox(-9.0F, -9.9F, 7.25F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 40).addBox(-4.6F, -18.4F, 7.25F, 3.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 48).mirror().addBox(-4.85F, -18.4F, 8.25F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 0).addBox(-9.5F, -21.0F, 7.75F, 3.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 9).addBox(-10.0F, -21.0F, 8.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(40, 22).addBox(-14.4F, -18.4F, 7.25F, 3.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 48).addBox(-14.9F, -18.4F, 8.25F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(22, 40).addBox(-9.0F, 9.0F, 7.75F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-9.5F, -9.65F, 6.75F, 3.0F, 18.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(28, 48).addBox(-10.0F, 9.0F, 8.25F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-26.5F, -3.0F, 8.25F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, -3.0F, 8.25F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(20, 56).addBox(0.0F, -4.0F, 1.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 7.75F, 7.25F, 0.0F, 0.0F, 1.9635F));

        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(14, 56).addBox(-2.25F, -2.0F, 0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(44, 50).addBox(-2.0F, -4.0F, 1.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 8.0F, 7.25F, 0.0F, 0.0F, -1.9635F));

        PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(56, 10).addBox(-0.75F, -6.0F, 1.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(52, 39).addBox(0.0F, -4.0F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -12.0F, 7.25F, 0.0F, 0.0F, 1.9635F));

        PartDefinition cube_r6 = bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(8, 56).addBox(-0.75F, -6.0F, 1.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.35F, -9.75F, 7.25F, 0.0F, 0.0F, 2.7489F));

        PartDefinition cube_r7 = bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(56, 5).addBox(-2.75F, -6.0F, 1.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(52, 29).addBox(-2.0F, -4.0F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -10.0F, 7.25F, 0.0F, 0.0F, -2.7489F));

        PartDefinition cube_r8 = bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(56, 0).addBox(-2.75F, -6.0F, 1.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -12.5F, 7.25F, 0.0F, 0.0F, -1.9635F));

        PartDefinition cube_r9 = bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(36, 44).addBox(-3.0F, -3.0F, -0.25F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 7.0F, 7.0F, 0.0F, 0.0F, -2.3562F));

        PartDefinition cube_r10 = bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(22, 45).addBox(-0.25F, -2.0F, 0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 8.25F, 7.25F, 0.0F, 0.0F, 1.9635F));

        PartDefinition cube_r11 = bb_main.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(36, 55).addBox(-2.25F, -2.25F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 8.0F, 7.0F, 0.0F, 0.0F, -2.3562F));

        PartDefinition cube_r12 = bb_main.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(52, 34).addBox(0.0F, -4.0F, 0.25F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -10.0F, 7.0F, 0.0F, 0.0F, 2.7489F));

        PartDefinition cube_r13 = bb_main.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(36, 50).addBox(-2.0F, -4.0F, 0.25F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -12.0F, 7.0F, 0.0F, 0.0F, -1.9635F));

        PartDefinition cube_r14 = bb_main.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(48, 17).addBox(-7.0F, -0.25F, 0.2F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.25F, -5.0F, 7.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition cube_r15 = bb_main.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(40, 30).addBox(3.25F, -0.25F, 0.2F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.75F, -5.0F, 7.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition cube_r16 = bb_main.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(50, 25).addBox(-6.0F, -0.25F, 0.2F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.25F, -11.0F, 7.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition cube_r17 = bb_main.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(50, 21).addBox(3.0F, -0.25F, 0.2F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.75F, -11.0F, 7.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition cube_r18 = bb_main.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(12, 32).addBox(-2.0F, -2.0F, -0.25F, 5.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -13.0F, 7.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r19 = bb_main.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(52, 54).addBox(-3.5F, -3.75F, 1.15F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(48, 49).addBox(-3.0F, -3.0F, 0.15F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.4F, -18.0F, 7.1F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r20 = bb_main.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(28, 34).addBox(-1.5F, -13.65F, 1.0F, 4.0F, 14.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(40, 11).addBox(-1.0F, -10.0F, 0.5F, 3.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.8F, -18.2F, 7.25F, 0.0F, 0.0F, 0.3054F));

        PartDefinition cube_r21 = bb_main.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(44, 54).addBox(-3.0F, -3.0F, 1.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(28, 54).addBox(-2.5F, -2.5F, 0.65F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -20.25F, 7.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r22 = bb_main.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 53).addBox(-1.0F, -3.75F, 1.15F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(48, 44).addBox(-0.5F, -3.0F, 0.15F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1F, -18.0F, 7.1F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r23 = bb_main.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(32, 20).addBox(-3.0F, -13.65F, 1.0F, 4.0F, 14.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(40, 0).addBox(-2.5F, -10.0F, 0.5F, 3.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, -18.2F, 7.25F, 0.0F, 0.0F, -0.3054F));

        PartDefinition cube_r24 = bb_main.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(36, 39).addBox(0.0F, -1.0F, -0.05F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -12.0F, 7.3F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r25 = bb_main.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(36, 34).addBox(-6.0F, -1.0F, -0.05F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -12.0F, 7.3F, 0.0F, 0.0F, 0.3927F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}

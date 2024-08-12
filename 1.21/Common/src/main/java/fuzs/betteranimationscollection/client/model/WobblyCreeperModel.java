package fuzs.betteranimationscollection.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.betteranimationscollection.client.element.WobblyCreeperElement;
import fuzs.betteranimationscollection.mixin.client.accessor.LayerDefinitionAccessor;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class WobblyCreeperModel<T extends Entity> extends CreeperModel<T> {
    private final ModelPart body;
    private final ModelPart[] bodyParts;
    private final ModelPart head;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final boolean chargedModel;

    public WobblyCreeperModel(ModelPart modelPart, boolean chargedModel) {
        super(modelPart);
        this.body = modelPart.getChild("body");
        // fewer parts for charge layer as it looks silly with too much overlap inside the model
        this.bodyParts = new ModelPart[chargedModel ? 4 : 11];
        for (int i = 0; i < this.bodyParts.length; i++) {
            if (i == 0) {
                this.bodyParts[i] = this.body.getChild("body" + i);
            } else {
                this.bodyParts[i] = this.bodyParts[i - 1].getChild("body" + i);
            }
        }
        this.head = this.bodyParts[this.bodyParts.length - 1].getChild("head");
        this.leftHindLeg = modelPart.getChild("right_hind_leg");
        this.rightHindLeg = modelPart.getChild("left_hind_leg");
        this.leftFrontLeg = modelPart.getChild("right_front_leg");
        this.rightFrontLeg = modelPart.getChild("left_front_leg");
        this.chargedModel = chargedModel;
    }

    public static LayerDefinition createAnimatedBodyLayer(CubeDeformation cubeDeformation, boolean chargedModel) {
        // fewer parts for charge layer as it looks silly with too much overlap inside the model
        int partHeight = chargedModel ? 3 : 1;
        LayerDefinition layerDefinition = CreeperModel.createBodyLayer(cubeDeformation);
        MeshDefinition meshDefinition = ((LayerDefinitionAccessor) layerDefinition).getMesh();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition lastBodyPart = partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 27).addBox(-4.0F, 12.0F - partHeight, -2.0F, 8.0F, partHeight, 4.0F, cubeDeformation), PartPose.offset(0.0F, 6.0F, 0.0F));
        for (int i = 0; i < (chargedModel ? 4 : 11); i++) {
            lastBodyPart = lastBodyPart.addOrReplaceChild("body" + i, CubeListBuilder.create().texOffs(16, 27 - partHeight - i * partHeight).addBox(-4.0F, partHeight, -2.0F, 8.0F, partHeight, 4.0F, cubeDeformation), PartPose.offset(0.0F, i == 0 ? 9.0F : -partHeight, 0.0F));
        }
        lastBodyPart.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubeDeformation), PartPose.offset(0.0F, 1.0F, 0.0F));
        // basically sets rotation points to be at body, fixes a vanilla issue, has nothing to do with any animations
        partDefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, cubeDeformation), PartPose.offset(-2.0F, 18.0F, 2.0F));
        partDefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, cubeDeformation), PartPose.offset(2.0F, 18.0F, 2.0F));
        partDefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, cubeDeformation), PartPose.offset(-2.0F, 18.0F, -2.0F));
        partDefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, cubeDeformation), PartPose.offset(2.0F, 18.0F, -2.0F));
        return layerDefinition;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        ImmutableList.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg).forEach(
                (ModelPart modelPart) -> {
                    modelPart.render(poseStack, buffer, packedLight, packedOverlay, color);
                });
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        WobblyCreeperElement.WobbleDirection wobbleDirection = WobblyCreeperElement.wobbleDirection;
        if (wobbleDirection == WobblyCreeperElement.WobbleDirection.RANDOM) {
            wobbleDirection = WobblyCreeperElement.WobbleDirection.values()[(int) Math.abs(entityIn.getUUID().getLeastSignificantBits() % (WobblyCreeperElement.WobbleDirection.values().length - 1))];
        }

        final float magnitude = this.resolveMagnitude(limbSwingAmount * 3.5F);
        float cosSwing = magnitude * Mth.cos(limbSwing * 0.6662F) * (this.chargedModel ? 3 : 1);
        float sinSwing = magnitude * Mth.sin(limbSwing * 0.6662F) * (this.chargedModel ? 3 : 1);
        for (ModelPart bodyPart : this.bodyParts) {
            switch (wobbleDirection) {
                case SIDE -> {
                    bodyPart.xRot = 0.0F;
                    bodyPart.zRot = cosSwing;
                }
                case FRONT -> {
                    bodyPart.xRot = cosSwing;
                    bodyPart.zRot = 0.0F;
                }
                case CIRCLE -> {
                    bodyPart.xRot = sinSwing;
                    bodyPart.zRot = cosSwing;
                }
            }
        }
    }

    private float resolveMagnitude(float limbSwingAmount) {
        if (limbSwingAmount < 0.0F) {
            return  0.0F;
        } else if (limbSwingAmount < 0.6F) {
            return  0.125F * limbSwingAmount;
        } else {
            return  0.075F;
        }
    }
}

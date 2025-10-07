package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.WobblyCreeperElement;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.util.Mth;

public class WobblyCreeperModel extends CreeperModel {
    private final ModelPart[] bodyParts;
    private final ModelPart head;
    private final boolean chargedModel;

    public WobblyCreeperModel(ModelPart modelPart, boolean chargedModel) {
        super(modelPart);
        // fewer parts for charge layer as it looks silly with too much overlap inside the model
        this.bodyParts = new ModelPart[chargedModel ? 4 : 11];
        for (int i = 0; i < this.bodyParts.length; i++) {
            if (i == 0) {
                this.bodyParts[i] = modelPart.getChild("body").getChild("body" + i);
            } else {
                this.bodyParts[i] = this.bodyParts[i - 1].getChild("body" + i);
            }
        }
        this.head = this.bodyParts[this.bodyParts.length - 1].getChild("head");
        this.chargedModel = chargedModel;
    }

    public static LayerDefinition createAnimatedBodyLayer(CubeDeformation cubeDeformation) {
        return CreeperModel.createBodyLayer(cubeDeformation).apply((MeshDefinition meshDefinition) -> {
            modifyMesh(meshDefinition.getRoot(), cubeDeformation);
            return meshDefinition;
        });
    }

    private static void modifyMesh(PartDefinition partDefinition, CubeDeformation cubeDeformation) {
        // fewer parts for the charge layer as it looks silly with too much overlap inside the model
        int partHeight = cubeDeformation != CubeDeformation.NONE ? 3 : 1;
        partDefinition.clearChild("head");
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(16, 27)
                        .addBox(-4.0F, 12.0F - partHeight, -2.0F, 8.0F, partHeight, 4.0F, cubeDeformation),
                PartPose.offset(0.0F, 6.0F, 0.0F));
        for (int i = 0; i < (cubeDeformation != CubeDeformation.NONE ? 4 : 11); i++) {
            partDefinition1 = partDefinition1.addOrReplaceChild("body" + i,
                    CubeListBuilder.create()
                            .texOffs(16, 27 - partHeight - i * partHeight)
                            .addBox(-4.0F, partHeight, -2.0F, 8.0F, partHeight, 4.0F, cubeDeformation),
                    PartPose.offset(0.0F, i == 0 ? 9.0F : -partHeight, 0.0F));
        }
        partDefinition1.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubeDeformation),
                PartPose.offset(0.0F, 1.0F, 0.0F));
        // basically sets rotation points to be at body, fixes a vanilla issue, has nothing to do with any animations
        partDefinition.addOrReplaceChild("right_hind_leg",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, cubeDeformation),
                PartPose.offset(-2.0F, 18.0F, 2.0F));
        partDefinition.addOrReplaceChild("left_hind_leg",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, cubeDeformation),
                PartPose.offset(2.0F, 18.0F, 2.0F));
        partDefinition.addOrReplaceChild("right_front_leg",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, cubeDeformation),
                PartPose.offset(-2.0F, 18.0F, -2.0F));
        partDefinition.addOrReplaceChild("left_front_leg",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, cubeDeformation),
                PartPose.offset(2.0F, 18.0F, -2.0F));
    }

    @Override
    public void setupAnim(CreeperRenderState renderState) {
        super.setupAnim(renderState);

        // copied from super, our head is a different model part
        this.head.yRot = renderState.yRot * ((float) Math.PI / 180F);
        this.head.xRot = renderState.xRot * ((float) Math.PI / 180F);

        float magnitude = this.resolveMagnitude(renderState.walkAnimationSpeed * 3.5F);
        float cosSwing = magnitude * Mth.cos(renderState.walkAnimationPos * 0.6662F) * (this.chargedModel ? 3 : 1);
        float sinSwing = magnitude * Mth.sin(renderState.walkAnimationPos * 0.6662F) * (this.chargedModel ? 3 : 1);
        WobblyCreeperElement.WobbleDirection wobbleDirection = RenderStateExtraData.getOrDefault(renderState,
                WobblyCreeperElement.WOBBLE_DIRECTION_PROPERTY,
                WobblyCreeperElement.WobbleDirection.SIDE);
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
            return 0.0F;
        } else if (limbSwingAmount < 0.6F) {
            return 0.125F * limbSwingAmount;
        } else {
            return 0.075F;
        }
    }
}

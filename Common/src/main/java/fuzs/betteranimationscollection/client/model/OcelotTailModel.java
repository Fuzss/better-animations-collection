package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.OcelotTailElement;
import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.FelineRenderState;
import net.minecraft.util.Mth;

public class OcelotTailModel extends OcelotModel {
    public static final int OCELOT_TAIL_LENGTH = 15;

    private final ModelPart tail;
    private final ModelPart[] tailParts;

    public OcelotTailModel(ModelPart modelPart) {
        super(modelPart);
        this.tail = modelPart.getChild("tail1");
        this.tailParts = getTailParts(this.tail);
    }

    public static LayerDefinition createAnimatedBodyMesh(CubeDeformation cubeDeformation) {
        MeshDefinition meshDefinition = OcelotModel.createBodyMesh(cubeDeformation);
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 15).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, cubeDeformation),
                PartPose.offsetAndRotation(0.0F, 15.0F, 8.0F, 0.9F, 0.0F, 0.0F));
        partDefinition.clearChild("tail2");
        for (int i = 0; i < OCELOT_TAIL_LENGTH; ++i) {
            if (i < OCELOT_TAIL_LENGTH / 2) {
                partDefinition1 = partDefinition1.addOrReplaceChild("tail" + (i + 2),
                        CubeListBuilder.create()
                                .texOffs(0, 16 + i)
                                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, cubeDeformation),
                        PartPose.offset(0.0F, 1.0F, 0.0F));
            } else {
                partDefinition1 = partDefinition1.addOrReplaceChild("tail" + (i + 2),
                        CubeListBuilder.create()
                                .texOffs(4, 8 + i)
                                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, cubeDeformation),
                        PartPose.offset(0.0F, 1.0F, 0.0F));
            }
        }
        // makes front legs stick out of body a little like they used to
        CubeListBuilder cubeListBuilder2 = CubeListBuilder.create()
                .texOffs(40, 0)
                .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, cubeDeformation);
        partDefinition.addOrReplaceChild("left_front_leg", cubeListBuilder2, PartPose.offset(1.2F, 13.7F, -5.0F));
        partDefinition.addOrReplaceChild("right_front_leg", cubeListBuilder2, PartPose.offset(-1.2F, 13.7F, -5.0F));
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    public static ModelPart[] getTailParts(ModelPart tail) {
        final ModelPart[] tailParts = new ModelPart[OCELOT_TAIL_LENGTH];
        for (int i = 0; i < tailParts.length; i++) {
            if (i == 0) {
                tailParts[i] = tail.getChild("tail" + (i + 2));
            } else {
                tailParts[i] = tailParts[i - 1].getChild("tail" + (i + 2));
            }
        }
        return tailParts;
    }

    @Override
    public void setupAnim(FelineRenderState renderState) {
        super.setupAnim(renderState);
        // tail1 is still being updated by super class although unused, so we just copy the angles
        this.tail.y = this.tail1.y;
        this.tail.z = this.tail1.z;
        this.tail.xRot = this.tail1.xRot;
        setupTailAnim(this.tail,
                this.tailParts,
                renderState.walkAnimationPos,
                renderState.walkAnimationSpeed,
                renderState.ageInTicks,
                OcelotTailElement.animationSpeed,
                OcelotTailElement.tailLength);
    }

    public static void setupTailAnim(ModelPart tail, ModelPart[] tailParts, float limbSwing, float limbSwingAmount, float ageInTicks, int animationSpeed, int tailLength) {
        float magnitude = (0.5F + limbSwingAmount) * 0.125F;
        float amplitude = limbSwing * 0.6662F + (ageInTicks / 4.66F) * 0.6662F;
        tail.xRot += Mth.sin(amplitude) * magnitude;
        for (int i = 0; i < tailParts.length; i++) {
            tailParts[i].zRot = 0.0F;
            tailParts[i].xRot = 0.05F;
            tailParts[i].xRot += Mth.sin(amplitude - (float) (i + 1) * animationSpeed * 0.05F) * magnitude;
            tailParts[i].visible = i < tailLength;
        }
    }
}

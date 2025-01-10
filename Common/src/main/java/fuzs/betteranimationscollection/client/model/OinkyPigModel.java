package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.OinkyPigElement;
import fuzs.betteranimationscollection.client.element.SoundBasedElement;
import fuzs.puzzleslib.api.client.util.v1.RenderPropertyKey;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class OinkyPigModel extends PigModel {
    private final ModelPart snout;
    private final ModelPart rightEar;
    private final ModelPart leftEar;

    public OinkyPigModel(ModelPart modelPart) {
        super(modelPart);
        this.snout = modelPart.getChild("head").getChild("snout");
        this.rightEar = modelPart.getChild("head").getChild("right_ear");
        this.leftEar = modelPart.getChild("head").getChild("left_ear");
    }

    public static LayerDefinition createAnimatedBodyLayer(CubeDeformation cubeDeformation) {
        LayerDefinition layerDefinition = PigModel.createBodyLayer(cubeDeformation);
        MeshDefinition meshDefinition = layerDefinition.mesh;
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, cubeDeformation),
                PartPose.offset(0.0F, 12.0F, -6.0F));
        partDefinition1.addOrReplaceChild("snout",
                CubeListBuilder.create().texOffs(16, 16).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 3.0F, 1.0F, cubeDeformation),
                PartPose.offset(0.0F, 3.0F, -8.0F));
        partDefinition1.addOrReplaceChild("right_ear",
                CubeListBuilder.create().texOffs(16, 4).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, cubeDeformation),
                PartPose.offset(3.5F, -2.0F, -4.0F));
        partDefinition1.addOrReplaceChild("left_ear",
                CubeListBuilder.create().texOffs(16, 4).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, cubeDeformation),
                PartPose.offsetAndRotation(-3.5F, -2.0F, -4.0F, 0.0F, (float) Math.PI, 0.0F));
        return layerDefinition;
    }

    @Override
    public void setupAnim(LivingEntityRenderState renderState) {
        super.setupAnim(renderState);
        float soundTime = RenderPropertyKey.getRenderProperty(renderState,
                SoundBasedElement.AMBIENT_SOUND_TIME_PROPERTY);
        if (0.0F < soundTime && soundTime < 8.0F) {
            this.snout.y -= Mth.sin(soundTime * (Mth.PI / 8.0F));
        }
        this.rightEar.visible = OinkyPigElement.floatyEars;
        this.leftEar.visible = OinkyPigElement.floatyEars;
        if (OinkyPigElement.floatyEars) {
            float flapAnimationPos = renderState.ageInTicks * 0.1F + renderState.walkAnimationPos * 0.5F;
            float flapAnimationSpeed =
                    0.08F + renderState.walkAnimationSpeed * OinkyPigElement.earAnimationSpeed * 0.04F;
            this.rightEar.zRot = (-(float) Math.PI / 6.0F) - Mth.cos(flapAnimationPos * 1.2F) * flapAnimationSpeed;
            this.leftEar.zRot = ((float) Math.PI / 6.0F) + Mth.cos(flapAnimationPos) * flapAnimationSpeed;
        }
    }
}
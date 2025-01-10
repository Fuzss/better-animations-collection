package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class SpiderKneesModel extends SpiderModel {
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightMiddleHindLeg;
    private final ModelPart leftMiddleHindLeg;
    private final ModelPart rightMiddleFrontLeg;
    private final ModelPart leftMiddleFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;

    public SpiderKneesModel(ModelPart modelPart) {
        super(modelPart);
        this.rightHindLeg = modelPart.getChild("right_hind_leg");
        this.leftHindLeg = modelPart.getChild("left_hind_leg");
        this.rightMiddleHindLeg = modelPart.getChild("right_middle_hind_leg");
        this.leftMiddleHindLeg = modelPart.getChild("left_middle_hind_leg");
        this.rightMiddleFrontLeg = modelPart.getChild("right_middle_front_leg");
        this.leftMiddleFrontLeg = modelPart.getChild("left_middle_front_leg");
        this.rightFrontLeg = modelPart.getChild("right_front_leg");
        this.leftFrontLeg = modelPart.getChild("left_front_leg");
    }

    public static LayerDefinition createAnimatedSpiderBodyLayer() {
        LayerDefinition layerDefinition = SpiderModel.createSpiderBodyLayer();
        MeshDefinition meshDefinition = layerDefinition.mesh;
        PartDefinition partDefinition = meshDefinition.getRoot();
        CubeListBuilder rightLegBuilder = CubeListBuilder.create().texOffs(18, 0).addBox(-7.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F);
        CubeListBuilder leftLegBuilder = CubeListBuilder.create().texOffs(18, 0).mirror().addBox(-1.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F);
        CubeListBuilder lowerRightLegBuilder = CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-9.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F));
        CubeListBuilder lowerLeftLegBuilder = CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-1.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F));
        partDefinition.addOrReplaceChild("right_hind_leg", rightLegBuilder, PartPose.offset(-4.0F, 15.0F, 2.0F)).addOrReplaceChild("lower_right_hind_leg", lowerRightLegBuilder, PartPose.offsetAndRotation(-7.0F, 0.5F, 0.0F, 0.0F, 0.0F, -1.05F));
        partDefinition.addOrReplaceChild("left_hind_leg", leftLegBuilder, PartPose.offset(4.0F, 15.0F, 2.0F)).addOrReplaceChild("lower_left_hind_leg", lowerLeftLegBuilder, PartPose.offsetAndRotation(7.0F, 0.5F, 0.0F, 0.0F, 0.0F, 1.05F));
        partDefinition.addOrReplaceChild("right_middle_hind_leg", rightLegBuilder, PartPose.offset(-4.0F, 15.0F, 1.0F)).addOrReplaceChild("lower_right_middle_hind_leg", lowerRightLegBuilder, PartPose.offsetAndRotation(-7.0F, 0.5F, 0.0F, 0.0F, 0.0F, -1.05F));
        partDefinition.addOrReplaceChild("left_middle_hind_leg", leftLegBuilder, PartPose.offset(4.0F, 15.0F, 1.0F)).addOrReplaceChild("lower_left_middle_hind_leg", lowerLeftLegBuilder, PartPose.offsetAndRotation(7.0F, 0.5F, 0.0F, 0.0F, 0.0F, 1.05F));
        partDefinition.addOrReplaceChild("right_middle_front_leg", rightLegBuilder, PartPose.offset(-4.0F, 15.0F, 0.0F)).addOrReplaceChild("lower_right_middle_front_leg", lowerRightLegBuilder, PartPose.offsetAndRotation(-7.0F, 0.5F, 0.0F, 0.0F, 0.0F, -1.05F));
        partDefinition.addOrReplaceChild("left_middle_front_leg", leftLegBuilder, PartPose.offset(4.0F, 15.0F, 0.0F)).addOrReplaceChild("lower_left_middle_front_leg", lowerLeftLegBuilder, PartPose.offsetAndRotation(7.0F, 0.5F, 0.0F, 0.0F, 0.0F, 1.05F));
        partDefinition.addOrReplaceChild("right_front_leg", rightLegBuilder, PartPose.offset(-4.0F, 15.0F, -1.0F)).addOrReplaceChild("lower_right_front_leg", lowerRightLegBuilder, PartPose.offsetAndRotation(-7.0F, 0.5F, 0.0F, 0.0F, 0.0F, -1.05F));
        partDefinition.addOrReplaceChild("left_front_leg", leftLegBuilder, PartPose.offset(4.0F, 15.0F, -1.0F)).addOrReplaceChild("lower_left_front_leg", lowerLeftLegBuilder, PartPose.offsetAndRotation(7.0F, 0.5F, 0.0F, 0.0F, 0.0F, 1.05F));
        return layerDefinition;
    }

    @Override
    public void setupAnim(LivingEntityRenderState renderState) {
        super.setupAnim(renderState);
        this.rightHindLeg.zRot += 0.675F;
        this.leftHindLeg.zRot -= 0.675F;
        this.rightMiddleHindLeg.zRot += 0.675F;
        this.leftMiddleHindLeg.zRot -= 0.675F;
        this.rightMiddleFrontLeg.zRot += 0.675F;
        this.leftMiddleFrontLeg.zRot -= 0.675F;
        this.rightFrontLeg.zRot += 0.675F;
        this.leftFrontLeg.zRot -= 0.675F;
    }
}

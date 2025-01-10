package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.SheepRenderState;

public class KneelingSheepModel extends SheepModel {
    private final ModelPart rightFrontLowerLeg;
    private final ModelPart leftFrontLowerLeg;

    public KneelingSheepModel(ModelPart modelPart) {
        super(modelPart);
        ModelPart rightFrontLeg = modelPart.getChild("right_front_leg");
        this.rightFrontLowerLeg = rightFrontLeg.getChild("right_front_lower_leg");
        ModelPart leftFrontLeg = modelPart.getChild("left_front_leg");
        this.leftFrontLowerLeg = leftFrontLeg.getChild("left_front_lower_leg");
    }

    public static LayerDefinition createAnimatedBodyLayer() {
        LayerDefinition layerDefinition = SheepModel.createBodyLayer();
        MeshDefinition meshDefinition = layerDefinition.mesh;
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(28, 8).addBox(-4.0F, -15.0F, 0.0F, 8.0F, 16.0F, 6.0F),
                PartPose.offset(0.0F, 12.0F, 7.0F));
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("right_front_leg",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F),
                PartPose.offset(-3.0F, 12.0F, -5.0F));
        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("left_front_leg",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F),
                PartPose.offset(3.0F, 12.0F, -5.0F));
        CubeListBuilder cubeListBuilder = CubeListBuilder.create()
                .texOffs(0, 22)
                .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F)
                .texOffs(0, 16)
                .addBox(-2.0F, 5.02F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.01F));
        PartPose partPose = PartPose.offset(0.0F, 6.0F, 0.0F);
        partDefinition1.addOrReplaceChild("right_front_lower_leg", cubeListBuilder, partPose);
        partDefinition2.addOrReplaceChild("left_front_lower_leg", cubeListBuilder, partPose);
        return layerDefinition;
    }

    @Override
    public void setupAnim(SheepRenderState renderState) {
        super.setupAnim(renderState);
        float rotation = renderState.headEatPositionScale;
        this.body.xRot += rotation * 0.4F;
        this.rightFrontLeg.y = this.leftFrontLeg.y = 12.0F + rotation * 4.0F;
        this.rightFrontLeg.xRot -= rotation;
        this.leftFrontLeg.xRot -= rotation;
        this.rightFrontLowerLeg.xRot = this.leftFrontLowerLeg.xRot = rotation * 2.0F;
    }
}
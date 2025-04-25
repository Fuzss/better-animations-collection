package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.util.Mth;

import java.util.Set;

public class KneelingSheepModel extends SheepModel {
    public static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(false,
            8.0F,
            4.0F,
            2.0F,
            2.0F,
            16.0F,
            Set.of("head"));

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
        return SheepModel.createBodyLayer().apply((MeshDefinition meshDefinition) -> {
            modifyMesh(meshDefinition.getRoot());
            return meshDefinition;
        });
    }

    private static void modifyMesh(PartDefinition partDefinition) {
        partDefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(28, 8).addBox(-4.0F, -15.0F, 0.0F, 8.0F, 16.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, 7.0F, Mth.HALF_PI, 0.0F, 0.0F));
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
    }

    @Override
    public void setupAnim(SheepRenderState renderState) {
        super.setupAnim(renderState);
        setupAnim(renderState, this.body, this.rightFrontLeg, this.leftFrontLeg);
        this.rightFrontLowerLeg.xRot = this.leftFrontLowerLeg.xRot = renderState.headEatPositionScale * 2.0F;
    }

    static void setupAnim(SheepRenderState renderState, ModelPart body, ModelPart rightFrontLeg, ModelPart leftFrontLeg) {
        float rotation = renderState.headEatPositionScale;
        body.xRot += rotation * 0.4F;
        rightFrontLeg.y += rotation * 4.0F * renderState.ageScale;
        leftFrontLeg.y += rotation * 4.0F * renderState.ageScale;
        rightFrontLeg.xRot -= rotation;
        leftFrontLeg.xRot -= rotation;
    }
}
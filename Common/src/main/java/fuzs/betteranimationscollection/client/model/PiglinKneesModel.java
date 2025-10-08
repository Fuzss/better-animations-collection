package fuzs.betteranimationscollection.client.model;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.PiglinRenderState;

public class PiglinKneesModel extends PiglinModel implements KneesModel {
    private final ModelPart rightShin;
    private final ModelPart leftShin;

    public PiglinKneesModel(ModelPart modelPart) {
        super(modelPart);
        this.rightShin = modelPart.getChild("right_leg").getChild("right_shin");
        this.leftShin = modelPart.getChild("left_leg").getChild("left_shin");
    }

    public static LayerDefinition createAnimatedBodyLayer() {
        MeshDefinition meshDefinition = PiglinModel.createMesh(CubeDeformation.NONE);
        modifyMesh(meshDefinition.getRoot());
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    private static void modifyMesh(PartDefinition partDefinition) {
        Pair<CubeListBuilder, PartPose> rightLeg = HumanoidKneesModel.createShin(0,
                16,
                -1.9F,
                0.0F,
                0.0F,
                false,
                CubeDeformation.NONE);
        Pair<CubeListBuilder, PartPose> leftLeg = HumanoidKneesModel.createShin(16,
                48,
                1.9F,
                0.0F,
                0.0F,
                true,
                CubeDeformation.NONE);
        Pair<CubeListBuilder, PartPose> rightShin = HumanoidKneesModel.createShin(0,
                22,
                0.0F,
                -6.0F,
                -2.0F,
                false,
                CubeDeformation.NONE);
        Pair<CubeListBuilder, PartPose> leftShin = HumanoidKneesModel.createShin(16,
                54,
                0.0F,
                -6.0F,
                -2.0F,
                true,
                CubeDeformation.NONE);
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("right_leg",
                rightLeg.left(),
                rightLeg.right());
        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("left_leg", leftLeg.left(), leftLeg.right());
        PartDefinition partDefinition3 = partDefinition1.addOrReplaceChild("right_shin",
                rightShin.left(),
                rightShin.right());
        PartDefinition partDefinition4 = partDefinition2.addOrReplaceChild("left_shin",
                leftShin.left(),
                leftShin.right());
        CubeDeformation cubeDeformation = new CubeDeformation(0.25F);
        Pair<CubeListBuilder, PartPose> rightPants = HumanoidKneesModel.createShin(0,
                32,
                -1.9F,
                0.0F,
                0.0F,
                false,
                cubeDeformation);
        Pair<CubeListBuilder, PartPose> leftPants = HumanoidKneesModel.createShin(0,
                48,
                1.9F,
                0.0F,
                0.0F,
                true,
                cubeDeformation);
        Pair<CubeListBuilder, PartPose> rightLowerPants = HumanoidKneesModel.createShin(0,
                38,
                0.0F,
                -6.0F,
                -2.0F,
                false,
                cubeDeformation);
        Pair<CubeListBuilder, PartPose> leftLowerPants = HumanoidKneesModel.createShin(0,
                54,
                0.0F,
                -6.0F,
                -2.0F,
                true,
                cubeDeformation);
        partDefinition1.addOrReplaceChild("right_pants", rightPants.left(), PartPose.ZERO);
        partDefinition2.addOrReplaceChild("left_pants", leftPants.left(), PartPose.ZERO);
        partDefinition3.addOrReplaceChild("right_lower_pants", rightLowerPants.left(), PartPose.ZERO);
        partDefinition4.addOrReplaceChild("left_lower_pants", leftLowerPants.left(), PartPose.ZERO);
    }

    @Override
    public void setupAnim(PiglinRenderState renderState) {
        super.setupAnim(renderState);
        KneesModel.setupAnim(this, renderState);
    }

    @Override
    public ModelPart rightShin() {
        return this.rightShin;
    }

    @Override
    public ModelPart leftShin() {
        return this.leftShin;
    }
}

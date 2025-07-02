package fuzs.betteranimationscollection.client.model;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class HumanoidKneesModel extends HumanoidModel<HumanoidRenderState> implements KneesModel {
    private final ModelPart rightShin;
    private final ModelPart leftShin;

    public HumanoidKneesModel(ModelPart modelPart) {
        super(modelPart);
        this.rightShin = modelPart.getChild("right_leg").getChild("right_shin");
        this.leftShin = modelPart.getChild("left_leg").getChild("left_shin");
    }

    public static MeshDefinition createAnimatedMesh(CubeDeformation cubeDeformation, float offsetY) {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(cubeDeformation, offsetY);
        modifyMesh(meshDefinition.getRoot(), cubeDeformation);
        return meshDefinition;
    }

    private static void modifyMesh(PartDefinition partDefinition, CubeDeformation cubeDeformation) {
        Pair<CubeListBuilder, PartPose> rightLeg = createShin(0, 16, -1.9F, 0.0F, 0.0F, false, cubeDeformation);
        Pair<CubeListBuilder, PartPose> leftLeg = createShin(0, 16, 1.9F, 0.0F, 0.0F, true, cubeDeformation);
        Pair<CubeListBuilder, PartPose> rightShin = createShin(0, 22, 0.0F, -6.0F, -2.0F, false, cubeDeformation);
        Pair<CubeListBuilder, PartPose> leftShin = createShin(0, 22, 0.0F, -6.0F, -2.0F, true, cubeDeformation);
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("right_leg",
                rightLeg.left(),
                rightLeg.right());
        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("left_leg", leftLeg.left(), leftLeg.right());
        partDefinition1.addOrReplaceChild("right_shin", rightShin.left(), rightShin.right());
        partDefinition2.addOrReplaceChild("left_shin", leftShin.left(), leftShin.right());
    }

    public static Pair<CubeListBuilder, PartPose> createShin(int textureX, int textureY, float offsetX, float offsetY, float offsetZ, boolean mirror, CubeDeformation cubeDeformation) {
        CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(textureX, textureY);
        if (mirror) cubeListBuilder.mirror();
        cubeListBuilder.addBox(-2.0F, 0.0F, -2.0F - offsetZ, 4.0F, 6.0F, 4.0F, cubeDeformation);
        return Pair.of(cubeListBuilder, PartPose.offset(offsetX, 12.0F + offsetY, offsetZ));
    }

    @Override
    public void setupAnim(HumanoidRenderState renderState) {
        super.setupAnim(renderState);
        KneesModel.setupAnim(this, renderState);
    }

    @Override
    public void copyPropertiesTo(HumanoidModel<HumanoidRenderState> model) {
        super.copyPropertiesTo(model);
        KneesModel.copyPropertiesTo(this, model);
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

package fuzs.betteranimationscollection.client.model;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.monster.Zombie;

public class DrownedKneesModel<T extends Zombie> extends DrownedModel<T> implements KneesModel {
    private final ModelPart rightShin;
    private final ModelPart leftShin;

    public DrownedKneesModel(ModelPart modelPart) {
        super(modelPart);
        ModelPart rightLeg = modelPart.getChild("right_leg");
        this.rightShin = rightLeg.getChild("right_shin");
        ModelPart leftLeg = modelPart.getChild("left_leg");
        this.leftShin = leftLeg.getChild("left_shin");
    }

    public static MeshDefinition createAnimatedMesh(CubeDeformation cubeDeformation, float offsetY) {
        MeshDefinition meshDefinition = HumanoidKneesModel.createAnimatedMesh(cubeDeformation, offsetY);
        PartDefinition partDefinition = meshDefinition.getRoot();
        Pair<CubeListBuilder, PartPose> leftLeg = HumanoidKneesModel.createShin(16, 48, 1.9F, 0.0F, 0.0F, true, cubeDeformation);
        Pair<CubeListBuilder, PartPose> leftShin = HumanoidKneesModel.createShin(16, 54, 0.0F, -6.0F, -2.0F, true, cubeDeformation);
        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("left_leg", leftLeg.left(), leftLeg.right());
        partDefinition2.addOrReplaceChild("left_shin", leftShin.left(), leftShin.right());
        return meshDefinition;
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        KneesModel.setupAnim(this, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        // should probably also do something about swimming animation as legs are used there, but it's broken for drowneds anyway
    }

    @Override
    public void copyPropertiesTo(HumanoidModel<T> model) {
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

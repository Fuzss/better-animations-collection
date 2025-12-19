package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.animal.equine.DonkeyModel;
import net.minecraft.client.model.animal.equine.EquineSaddleModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.renderer.entity.state.DonkeyRenderState;

public class FamiliarDonkeyModel extends DonkeyModel {
    private final ModelPart upperMouth;
    private final ModelPart lowerMouth;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftHindShin;
    private final ModelPart rightHindShin;
    private final ModelPart leftFrontShin;
    private final ModelPart rightFrontShin;

    public FamiliarDonkeyModel(ModelPart modelPart) {
        super(modelPart);
        ModelPart headParts = modelPart.getChild("head_parts");
        this.upperMouth = headParts.getChild("upper_mouth");
        this.lowerMouth = headParts.getChild("lower_mouth");
        this.rightHindLeg = modelPart.getChild("right_hind_leg");
        this.leftHindLeg = modelPart.getChild("left_hind_leg");
        this.rightFrontLeg = modelPart.getChild("right_front_leg");
        this.leftFrontLeg = modelPart.getChild("left_front_leg");
        this.rightHindShin = modelPart.getChild("right_hind_shin");
        this.leftHindShin = modelPart.getChild("left_hind_shin");
        this.rightFrontShin = modelPart.getChild("right_front_shin");
        this.leftFrontShin = modelPart.getChild("left_front_shin");
    }

    public static LayerDefinition createAnimatedBodyLayer(float scale, boolean isBaby) {
        MeshDefinition meshDefinition = FamiliarHorseModel.createAnimatedBodyMesh(CubeDeformation.NONE);
        modifyMesh(meshDefinition.getRoot());
        return LayerDefinition.create(meshDefinition, 64, 64)
                .apply(isBaby ? FamiliarHorseModel.BABY_TRANSFORMER : MeshTransformer.IDENTITY)
                .apply(MeshTransformer.scaling(scale));
    }

    public static LayerDefinition createAnimatedSaddleLayer(float scale, boolean isBaby) {
        return EquineSaddleModel.createFullScaleSaddleLayer(isBaby)
                .apply((MeshDefinition meshDefinition) -> {
                    FamiliarHorseModel.modifyHeadMesh(meshDefinition.getRoot(), CubeDeformation.NONE);
                    FamiliarEquineSaddleModel.modifyMesh(meshDefinition.getRoot());
                    modifyMesh(meshDefinition.getRoot());
                    return meshDefinition;
                })
                .apply(isBaby ? FamiliarHorseModel.BABY_TRANSFORMER : MeshTransformer.IDENTITY)
                .apply(MeshTransformer.scaling(scale));
    }

    @Override
    public void setupAnim(DonkeyRenderState renderState) {
        super.setupAnim(renderState);
        FamiliarHorseModel.setupAnim(renderState,
                this.upperMouth,
                this.lowerMouth,
                this.rightHindShin,
                this.rightHindLeg,
                this.leftHindShin,
                this.leftHindLeg,
                this.rightFrontShin,
                this.rightFrontLeg,
                this.leftFrontShin,
                this.leftFrontLeg);
    }
}

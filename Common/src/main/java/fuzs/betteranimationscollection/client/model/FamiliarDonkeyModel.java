package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.DonkeyModel;
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
    private final ModelPart[] saddleParts;

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
        ModelPart leftSaddleMouth = this.upperMouth.getChild("left_saddle_mouth");
        ModelPart rightSaddleMouth = this.upperMouth.getChild("right_saddle_mouth");
        ModelPart mouthSaddleWrap = this.upperMouth.getChild("mouth_saddle_wrap");
        ModelPart lowerMouthSaddleWrap = this.lowerMouth.getChild("lower_mouth_saddle_wrap");
        this.saddleParts = new ModelPart[]{
                leftSaddleMouth, rightSaddleMouth, mouthSaddleWrap, lowerMouthSaddleWrap
        };
    }

    public static LayerDefinition createAnimatedBodyLayer(boolean isBaby) {
        MeshDefinition meshDefinition = FamiliarHorseModel.createAnimatedBodyMesh(CubeDeformation.NONE, isBaby);
        modifyMesh(meshDefinition.getRoot());
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    public static MeshTransformer getBabyTransformer() {
        return BABY_TRANSFORMER;
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
                this.leftFrontLeg,
                this.saddleParts);
    }
}

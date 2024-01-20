package fuzs.betteranimationscollection.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ChestedHorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * everything copied from {@link FamiliarHorseModel}
 */
public class FamiliarChestedHorseModel<T extends AbstractChestedHorse> extends ChestedHorseModel<T> {
    private final ModelPart headParts;
    private final ModelPart upperMouth;
    private final ModelPart lowerMouth;
    private final ModelPart mouthSaddleWrap;
    private final ModelPart lowerMouthSaddleWrap;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftHindShin;
    private final ModelPart rightHindShin;
    private final ModelPart leftFrontShin;
    private final ModelPart rightFrontShin;
    private final ModelPart leftHindBabyLeg;
    private final ModelPart rightHindBabyLeg;
    private final ModelPart leftFrontBabyLeg;
    private final ModelPart rightFrontBabyLeg;
    private final ModelPart leftHindBabyShin;
    private final ModelPart rightHindBabyShin;
    private final ModelPart leftFrontBabyShin;
    private final ModelPart rightFrontBabyShin;
    private final ModelPart rightSaddleMouth;
    private final ModelPart leftSaddleMouth;
//    private final ModelPart[] saddleParts;
//    private final ModelPart[] ridingParts;

    public FamiliarChestedHorseModel(ModelPart modelPart) {
        super(modelPart);
        this.headParts = modelPart.getChild("head_parts");
        this.upperMouth = this.headParts.getChild("upper_mouth");
        this.lowerMouth = this.headParts.getChild("lower_mouth");
        this.rightHindLeg = modelPart.getChild("right_hind_leg");
        this.leftHindLeg = modelPart.getChild("left_hind_leg");
        this.rightFrontLeg = modelPart.getChild("right_front_leg");
        this.leftFrontLeg = modelPart.getChild("left_front_leg");
        this.rightHindShin = modelPart.getChild("right_hind_shin");
        this.leftHindShin = modelPart.getChild("left_hind_shin");
        this.rightFrontShin = modelPart.getChild("right_front_shin");
        this.leftFrontShin = modelPart.getChild("left_front_shin");
        this.rightHindBabyLeg = modelPart.getChild("right_hind_baby_leg");
        this.leftHindBabyLeg = modelPart.getChild("left_hind_baby_leg");
        this.rightFrontBabyLeg = modelPart.getChild("right_front_baby_leg");
        this.leftFrontBabyLeg = modelPart.getChild("left_front_baby_leg");
        this.rightHindBabyShin = modelPart.getChild("right_hind_baby_shin");
        this.leftHindBabyShin = modelPart.getChild("left_hind_baby_shin");
        this.rightFrontBabyShin = modelPart.getChild("right_front_baby_shin");
        this.leftFrontBabyShin = modelPart.getChild("left_front_baby_shin");
//        ModelPart saddle = this.body.getChild("saddle");
        this.leftSaddleMouth = this.headParts.getChild("left_saddle_mouth");
        this.rightSaddleMouth = this.headParts.getChild("right_saddle_mouth");
//        ModelPart leftSaddleLine = this.headParts.getChild("left_saddle_line");
//        ModelPart rightSaddleLine = this.headParts.getChild("right_saddle_line");
//        ModelPart headSaddle = this.headParts.getChild("head_saddle");
        this.mouthSaddleWrap = this.headParts.getChild("mouth_saddle_wrap");
        this.lowerMouthSaddleWrap = this.headParts.getChild("lower_mouth_saddle_wrap");
//        this.saddleParts = new ModelPart[]{saddle, this.leftSaddleMouth, this.rightSaddleMouth, headSaddle, this.mouthSaddleWrap, this.lowerMouthSaddleWrap};
//        this.ridingParts = new ModelPart[]{leftSaddleLine, rightSaddleLine};
    }

    public static LayerDefinition createAnimatedBodyLayer() {
        MeshDefinition meshDefinition = FamiliarHorseModel.createAnimatedBodyMesh(CubeDeformation.NONE);
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition partDefinition2 = partDefinition.getChild("body");
        CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(26, 21).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 3.0F);
        partDefinition2.addOrReplaceChild("left_chest", cubeListBuilder, PartPose.offsetAndRotation(6.0F, -8.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));
        partDefinition2.addOrReplaceChild("right_chest", cubeListBuilder, PartPose.offsetAndRotation(-6.0F, -8.0F, 0.0F, 0.0F, 1.5707964F, 0.0F));
        PartDefinition partDefinition3 = partDefinition.getChild("head_parts").getChild("head");
        CubeListBuilder cubeListBuilder2 = CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 1.0F);
        partDefinition3.addOrReplaceChild("left_ear", cubeListBuilder2, PartPose.offsetAndRotation(1.25F, -10.0F, 4.0F, 0.2617994F, 0.0F, 0.2617994F));
        partDefinition3.addOrReplaceChild("right_ear", cubeListBuilder2, PartPose.offsetAndRotation(-1.25F, -10.0F, 4.0F, 0.2617994F, 0.0F, -0.2617994F));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return Stream.concat(StreamSupport.stream(super.bodyParts().spliterator(), false), Stream.of(this.leftHindShin, this.rightHindShin, this.leftFrontShin, this.rightFrontShin, this.leftHindBabyShin, this.rightHindBabyShin, this.leftFrontBabyShin, this.rightFrontBabyShin)).collect(ImmutableList.toImmutableList());
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.lowerMouthSaddleWrap.visible = entityIn.isSaddled();
    }

    @Override
    public void prepareMobModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        super.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);

        float f = Mth.rotLerp(entitylivingbaseIn.yBodyRotO, entitylivingbaseIn.yBodyRot, partialTickTime);
        float f1 = Mth.rotLerp(entitylivingbaseIn.yHeadRotO, entitylivingbaseIn.yHeadRot, partialTickTime);
        float f2 = Mth.lerp(partialTickTime, entitylivingbaseIn.xRotO, entitylivingbaseIn.getXRot());
        float f3 = f1 - f;
        float f4 = f2 * ((float) Math.PI / 180F);
        f3 = Mth.clamp(f3, -20.0F, 20.0F);
        if (limbSwingAmount > 0.2F) {

            f4 += Mth.cos(limbSwing * 0.4F) * 0.15F * limbSwingAmount;
        }

        float f5 = entitylivingbaseIn.getEatAnim(partialTickTime);
        float f6 = entitylivingbaseIn.getStandAnim(partialTickTime);
        float f7 = 1.0F - f6;
        float f8 = entitylivingbaseIn.getMouthAnim(partialTickTime);
        float f9 = (float) entitylivingbaseIn.tickCount + partialTickTime;
        this.headParts.y = 4.0F;
        this.headParts.z = -12.0F;
        this.headParts.xRot = ((float) Math.PI / 6F) + f4;
        this.headParts.yRot = f3 * ((float) Math.PI / 180F);
        float f10 = entitylivingbaseIn.isInWater() ? 0.2F : 1.0F;
        float f11 = Mth.cos(f10 * limbSwing * 0.6662F + (float) Math.PI);
        float f12 = f11 * 1.8F * limbSwingAmount;
        float f13 = f11 * 1.4F * limbSwingAmount;
        this.headParts.xRot = f6 * (0.2617994F + f4) + f5 * 2.1816616F + (1.0F - Math.max(f6, f5)) * this.headParts.xRot;
        this.headParts.yRot = f6 * f3 * 0.017453292F + (1.0F - Math.max(f6, f5)) * this.headParts.yRot;
        this.headParts.y = f6 * -4.0F + f5 * 11.0F + (1.0F - Math.max(f6, f5)) * this.headParts.y;
        this.headParts.z = f6 * -4.0F + f5 * -12.0F + (1.0F - Math.max(f6, f5)) * this.headParts.z;

        this.upperMouth.y = this.mouthSaddleWrap.y = this.leftSaddleMouth.y = this.rightSaddleMouth.y = 0.02F;
        this.lowerMouth.y = this.lowerMouthSaddleWrap.y = 0.0F;
        this.upperMouth.z = this.mouthSaddleWrap.z = this.leftSaddleMouth.z = this.rightSaddleMouth.z = 0.02F - f8;
        this.lowerMouth.z = this.lowerMouthSaddleWrap.z = f8;
        this.upperMouth.xRot = this.mouthSaddleWrap.xRot = this.leftSaddleMouth.xRot = this.rightSaddleMouth.xRot = -0.09424778F * f8;
        this.lowerMouth.xRot = this.lowerMouthSaddleWrap.xRot = 0.15707964F * f8;
        this.upperMouth.yRot = this.mouthSaddleWrap.yRot = this.leftSaddleMouth.yRot = this.rightSaddleMouth.yRot = 0.0F;
        this.lowerMouth.yRot = this.lowerMouthSaddleWrap.yRot = 0.0F;

        float f14 = 0.2617994F * f6;
        float f15 = Mth.cos(f9 * 0.6F + (float) Math.PI);
        this.rightFrontLeg.y = 2.0F * f6 + 14.0F * f7;
        this.rightFrontLeg.z = -6.0F * f6 - 10.0F * f7;
        this.leftFrontLeg.y = this.rightFrontLeg.y;
        this.leftFrontLeg.z = this.rightFrontLeg.z;
        this.rightHindShin.y = this.rightHindLeg.y + Mth.sin(((float) Math.PI / 2F) + f14 + f7 * - f12) * 3.0F;
        this.rightHindShin.z = this.rightHindLeg.z + Mth.cos(-((float) Math.PI / 2F) + f14 + f7 * - f12) * 3.0F;
        this.leftHindShin.y = this.leftHindLeg.y + Mth.sin(((float) Math.PI / 2F) + f14 + f7 * f12) * 3.0F;
        this.leftHindShin.z = this.leftHindLeg.z + Mth.cos(-((float) Math.PI / 2F) + f14 + f7 * f12) * 3.0F;
        float f16 = ((-(float) Math.PI / 3F) + f15) * f6 + f12 * f7;
        float f17 = ((-(float) Math.PI / 3F) - f15) * f6 - f12 * f7;
        this.rightFrontShin.y = this.rightFrontLeg.y + Mth.sin(((float) Math.PI / 2F) + f16) * 3.0F;
        this.rightFrontShin.z = this.rightFrontLeg.z + Mth.cos(-((float) Math.PI / 2F) + f16) * 3.0F;
        this.leftFrontShin.y = this.leftFrontLeg.y + Mth.sin(((float) Math.PI / 2F) + f17) * 3.0F;
        this.leftFrontShin.z = this.leftFrontLeg.z + Mth.cos(-((float) Math.PI / 2F) + f17) * 3.0F;
        this.rightHindLeg.xRot = f14 - f12 * f7;
        this.rightHindShin.xRot = -0.08726646F * f6 + (-f13 - Math.max(0.0F, f13)) * f7;
        this.leftHindLeg.xRot = f14 + f12 * f7;
        this.leftHindShin.xRot = -0.08726646F * f6 + (f13 - Math.max(0.0F, -f13)) * f7;
        this.rightFrontLeg.xRot = f16;
        this.rightFrontShin.xRot = (this.rightFrontLeg.xRot + (float) Math.PI * Math.max(0.0F, 0.2F + f15 * 0.2F)) * f6 + (f13 + Math.max(0.0F, f13)) * f7;
        this.leftFrontLeg.xRot = f17;
        this.leftFrontShin.xRot = (this.leftFrontLeg.xRot + (float) Math.PI * Math.max(0.0F, 0.2F - f15 * 0.2F)) * f6 + (-f13 + Math.max(0.0F, -f13)) * f7;
        this.copyBabyModelAngles();
        this.setBabyModelVisibility();
    }

    private void copyBabyModelAngles() {
        this.rightHindBabyLeg.y = this.rightHindLeg.y;
        this.rightHindBabyLeg.z = this.rightHindLeg.z;
        this.rightHindBabyLeg.xRot = this.rightHindLeg.xRot;
        this.leftHindBabyLeg.y = this.leftHindLeg.y;
        this.leftHindBabyLeg.z = this.leftHindLeg.z;
        this.leftHindBabyLeg.xRot = this.leftHindLeg.xRot;
        this.rightFrontBabyLeg.y = this.rightFrontLeg.y;
        this.rightFrontBabyLeg.z = this.rightFrontLeg.z;
        this.rightFrontBabyLeg.xRot = this.rightFrontLeg.xRot;
        this.leftFrontBabyLeg.y = this.leftFrontLeg.y;
        this.leftFrontBabyLeg.z = this.leftFrontLeg.z;
        this.leftFrontBabyLeg.xRot = this.leftFrontLeg.xRot;
        this.rightHindBabyShin.y = this.rightHindShin.y;
        this.rightHindBabyShin.z = this.rightHindShin.z;
        this.rightHindBabyShin.xRot = this.rightHindShin.xRot;
        this.leftHindBabyShin.y = this.leftHindShin.y;
        this.leftHindBabyShin.z = this.leftHindShin.z;
        this.leftHindBabyShin.xRot = this.leftHindShin.xRot;
        this.rightFrontBabyShin.y = this.rightFrontShin.y;
        this.rightFrontBabyShin.z = this.rightFrontShin.z;
        this.rightFrontBabyShin.xRot = this.rightFrontShin.xRot;
        this.leftFrontBabyShin.y = this.leftFrontShin.y;
        this.leftFrontBabyShin.z = this.leftFrontShin.z;
        this.leftFrontBabyShin.xRot = this.leftFrontShin.xRot;
    }

    private void setBabyModelVisibility() {
        this.rightHindLeg.visible = !this.young;
        this.leftHindLeg.visible = !this.young;
        this.rightFrontLeg.visible = !this.young;
        this.leftFrontLeg.visible = !this.young;
        this.rightHindShin.visible = !this.young;
        this.leftHindShin.visible = !this.young;
        this.rightFrontShin.visible = !this.young;
        this.leftFrontShin.visible = !this.young;
        this.rightHindBabyLeg.visible = this.young;
        this.leftHindBabyLeg.visible = this.young;
        this.rightFrontBabyLeg.visible = this.young;
        this.leftFrontBabyLeg.visible = this.young;
        this.rightHindBabyShin.visible = this.young;
        this.leftHindBabyShin.visible = this.young;
        this.rightFrontBabyShin.visible = this.young;
        this.leftFrontBabyShin.visible = this.young;
    }
}

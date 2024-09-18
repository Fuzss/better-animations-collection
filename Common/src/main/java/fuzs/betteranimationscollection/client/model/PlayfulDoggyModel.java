package fuzs.betteranimationscollection.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fuzs.betteranimationscollection.client.element.PlayfulDoggyElement;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Wolf;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PlayfulDoggyModel<T extends Wolf> extends WolfModel<T> {
    public static final int WOLF_TAIL_LENGTH = 7;

    private final ModelPart head;
    private final ModelPart realHead;
    private final ModelPart body;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart tail;
    private final ModelPart realTail;
    private final ModelPart[] realTailParts;
    private final ModelPart fluffyTail;
    private final ModelPart realFluffyTail;
    private final ModelPart[] realFluffyTailParts;
    private final ModelPart upperBody;

    private boolean isInSittingPose;
    private float rollOverAmount;
    private float tickDelta;

    public PlayfulDoggyModel(ModelPart modelPart) {
        super(modelPart);
        this.head = modelPart.getChild("head");
        this.realHead = this.head.getChild("real_head");
        this.body = modelPart.getChild("body");
        this.upperBody = modelPart.getChild("upper_body");
        this.rightHindLeg = modelPart.getChild("right_hind_leg");
        this.leftHindLeg = modelPart.getChild("left_hind_leg");
        this.rightFrontLeg = modelPart.getChild("right_front_leg");
        this.leftFrontLeg = modelPart.getChild("left_front_leg");
        this.tail = modelPart.getChild("tail");
        this.fluffyTail = modelPart.getChild("fluffy_tail");
        ModelPart modelPart1 = this.realTail = this.tail.getChild("real_tail");
        this.realTailParts = new ModelPart[WOLF_TAIL_LENGTH];
        for (int i = 0; i < this.realTailParts.length; i++) {
            this.realTailParts[i] = modelPart1 = modelPart1.getChild("real_tail" + i);
        }
        ModelPart modelPart2 = this.realFluffyTail = this.fluffyTail.getChild("real_fluffy_tail");
        this.realFluffyTailParts = new ModelPart[WOLF_TAIL_LENGTH];
        for (int i = 0; i < this.realFluffyTailParts.length; i++) {
            this.realFluffyTailParts[i] = modelPart2 = modelPart2.getChild("real_fluffy_tail" + i);
        }
    }

    public static MeshDefinition createAnimatedBodyLayer(CubeDeformation cubeDeformation) {
        MeshDefinition meshDefinition = WolfModel.createMeshDefinition(cubeDeformation);
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition partDefinition1 = partDefinition.getChild("tail");
        PartDefinition partDefinition3 = partDefinition.addOrReplaceChild("fluffy_tail", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 12.0F, 8.0F, 0.62831855F, 0.0F, 0.0F));
        CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(9, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, cubeDeformation);
        PartDefinition partDefinition2 = partDefinition1.addOrReplaceChild("real_tail", cubeListBuilder, PartPose.ZERO);
        PartDefinition partDefinition4 = partDefinition3.addOrReplaceChild("real_fluffy_tail", cubeListBuilder, PartPose.ZERO);
        for (int i = 0; i < WOLF_TAIL_LENGTH; i++) {
            partDefinition2 = partDefinition2.addOrReplaceChild("real_tail" + i, CubeListBuilder.create().texOffs(9, Math.min(19 + i, 25)).addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, cubeDeformation), PartPose.offset(0.0F, 1.0F, 0.0F));
            CubeDeformation cubeDeformation1 = cubeDeformation.extend(getTailFluffiness(i));
            partDefinition4 = partDefinition4.addOrReplaceChild("real_fluffy_tail" + i, CubeListBuilder.create().texOffs(9, Math.min(19 + i, 25)).addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F,
                    cubeDeformation1
            ), PartPose.offset(0.0F, 1.0F + getTailFluffiness(i), 0.0F));
        }
        return meshDefinition;
    }

    private static float getTailFluffiness(int index) {
        if (index < 5) {
            return 0.1F + 0.1F * index;
        } else if (index == 5) {
            return 0.4F;
        } else {
            return  0.15F;
        }
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return Stream.concat(StreamSupport.stream(super.bodyParts().spliterator(), false), Stream.of(this.fluffyTail)).collect(ImmutableList.toImmutableList());
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (!this.isInSittingPose || !PlayfulDoggyElement.sittingBehaviour.rollOver() || PlayfulDoggyElement.sittingBehaviour.begForMeat() && this.rollOverAmount < 1E-4) {
            super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
            return;
        } else {
            // max amount from head and body roll angles
            float rollOverAmount = PlayfulDoggyElement.sittingBehaviour.begForMeat() ? this.rollOverAmount : 0.47123888F;
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.25F, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F * rollOverAmount));
            poseStack.translate(0.0F, -1.25F, 0.0F);
            this.rightHindLeg.xRot += rollOverAmount * 1.5F;
            this.leftHindLeg.xRot += rollOverAmount * 1.5F;
            this.rightFrontLeg.xRot += rollOverAmount * 1.5F;
            this.leftFrontLeg.xRot += rollOverAmount * 1.5F;
            this.rightHindLeg.y -= rollOverAmount * 1.75F;
            this.leftHindLeg.y -= rollOverAmount * 1.75F;
            this.rightFrontLeg.y -= rollOverAmount * 1.75F;
            this.leftFrontLeg.y -= rollOverAmount * 1.75F;
            this.realHead.zRot = -rollOverAmount * 1.5F;
            super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
            poseStack.popPose();
        }
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.tickDelta = partialTick;
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        if (entity.isInSittingPose()) {
            if (PlayfulDoggyElement.sittingBehaviour.lieDown()) {
                this.upperBody.setPos(-1.0F, 17.5F, -3.0F);
                this.upperBody.xRot = ((float) Math.PI / 2F);
                this.upperBody.yRot = 0.0F;
                this.body.setPos(0.0F, 17.5F, 0.0F);
                this.body.xRot = ((float) Math.PI / 7F * 3F);
                this.tail.setPos(-1.0F, 19.0F, 6.0F);
                this.rightHindLeg.setPos(-2.5F, 22.0F, 5.25F);
                this.rightHindLeg.xRot = ((float) Math.PI * 3F / 2F);
                this.rightHindLeg.yRot = 0.4F;
                this.leftHindLeg.setPos(0.5F, 22.0F, 5.25F);
                this.leftHindLeg.xRot = ((float) Math.PI * 3F / 2F);
                this.leftHindLeg.yRot = -0.4F;
                this.rightFrontLeg.xRot = ((float) Math.PI * 3F / 2F);
                this.rightFrontLeg.setPos(-2.49F, 21.5F, -2.0F);
                this.rightFrontLeg.yRot = 0.15F;
                this.leftFrontLeg.xRot = ((float) Math.PI * 3F / 2F);
                this.leftFrontLeg.setPos(0.51F, 21.5F, -2.0F);
                this.leftFrontLeg.yRot = -0.15F;
                this.head.y = this.young ? 15.5F : 17.0F;
            } else {
                this.upperBody.setPos(-1.0F, 16.0F, -3.0F);
                this.upperBody.xRot = ((float) Math.PI * 2F / 5F);
                this.upperBody.yRot = 0.0F;
                this.tail.setPos(-1.0F, 21.0F, 6.0F);
            }
        } else {
            this.upperBody.setPos(-1.0F, 14.0F, -3.0F);
            this.upperBody.xRot = this.body.xRot;
            this.tail.setPos(-1.0F, 12.0F, 8.0F);
            this.head.y = 13.5F;
            this.rightHindLeg.yRot = this.leftHindLeg.yRot = this.rightFrontLeg.yRot = this.leftFrontLeg.yRot = 0.0F;
        }
        this.upperBody.zRot = entity.getBodyRollAngle(partialTick, -0.08F);
        this.realTail.zRot = entity.getBodyRollAngle(partialTick, -0.2F);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.isInSittingPose = entityIn.isInSittingPose();
        this.rollOverAmount = entityIn.getHeadRollAngle(1.0F) + entityIn.getBodyRollAngle(1.0F, 0.0F);
        // needs to run after tail xRot has been set in super.setupAnim
        this.setupAnimTail(entityIn, limbSwing, limbSwingAmount, this.tickDelta);
    }

    private void setupAnimTail(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        // what ageInTicks would normally be which we don't have here
        float progress = ((float) entitylivingbaseIn.tickCount + partialTickTime) / 3.978873F;
        float magnitude = (0.5F + Math.max(limbSwingAmount, entitylivingbaseIn.getHeadRollAngle(partialTickTime) * 1.5F)) * 0.25F;
        float amplitude = limbSwing * 0.6662F + progress * 0.6662F;
        if (!entitylivingbaseIn.isTame()) {
            this.tail.xRot += Mth.sin(amplitude) * magnitude;
            this.tail.yRot = 0.0F;
            for (int i = 0; i < this.realTailParts.length; i++) {
                this.realTailParts[i].zRot = 0.0F;
                this.realTailParts[i].xRot = Mth.sin(amplitude - (float)(i + 1) * PlayfulDoggyElement.animationSpeed * 0.15F) * magnitude;
            }
        } else {
            this.tail.yRot = Mth.sin(amplitude) * magnitude;
            for (int i = 0; i < this.realTailParts.length; i++) {
                this.realTailParts[i].xRot = 0.0F;
                this.realTailParts[i].zRot = Mth.sin(amplitude - (float)(i + 1) * PlayfulDoggyElement.animationSpeed * 0.15F) * magnitude;
            }
        }

        this.copyAllTailParts();
        this.setModelPartVisibilities();
    }

    private void copyAllTailParts() {
        this.fluffyTail.copyFrom(this.tail);
        this.realFluffyTail.copyFrom(this.realTail);
        for (int i = 0; i < this.realTailParts.length; i++) {
            this.realFluffyTailParts[i].copyFrom(this.realTailParts[i]);
        }
    }

    private void setModelPartVisibilities() {
        // this also makes all children invisible, so setting just the main tail is enough
        this.tail.visible = !PlayfulDoggyElement.fluffyTail;
        this.fluffyTail.visible = PlayfulDoggyElement.fluffyTail;
    }
}

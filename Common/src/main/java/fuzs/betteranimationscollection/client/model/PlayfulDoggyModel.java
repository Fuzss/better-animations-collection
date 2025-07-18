package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.PlayfulDoggyElement;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.util.Mth;

public class PlayfulDoggyModel extends WolfModel {
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
        ModelPart tail = this.realTail = this.tail.getChild("real_tail");
        this.realTailParts = new ModelPart[WOLF_TAIL_LENGTH];
        for (int i = 0; i < this.realTailParts.length; i++) {
            this.realTailParts[i] = tail = tail.getChild("real_tail" + i);
        }
        ModelPart fluffyTail = this.realFluffyTail = this.fluffyTail.getChild("real_fluffy_tail");
        this.realFluffyTailParts = new ModelPart[WOLF_TAIL_LENGTH];
        for (int i = 0; i < this.realFluffyTailParts.length; i++) {
            this.realFluffyTailParts[i] = fluffyTail = fluffyTail.getChild("real_fluffy_tail" + i);
        }
    }

    public static MeshDefinition createAnimatedBodyLayer(CubeDeformation cubeDeformation) {
        MeshDefinition meshDefinition = WolfModel.createMeshDefinition(cubeDeformation);
        modifyMesh(meshDefinition.getRoot(), cubeDeformation);
        return meshDefinition;
    }

    private static void modifyMesh(PartDefinition partDefinition, CubeDeformation cubeDeformation) {
        PartDefinition partDefinition1 = partDefinition.getChild("tail");
        PartDefinition partDefinition3 = partDefinition.addOrReplaceChild("fluffy_tail",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-1.0F, 12.0F, 8.0F, 0.62831855F, 0.0F, 0.0F));
        CubeListBuilder cubeListBuilder = CubeListBuilder.create()
                .texOffs(9, 18)
                .addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, cubeDeformation);
        PartDefinition partDefinition2 = partDefinition1.addOrReplaceChild("real_tail", cubeListBuilder, PartPose.ZERO);
        PartDefinition partDefinition4 = partDefinition3.addOrReplaceChild("real_fluffy_tail",
                cubeListBuilder,
                PartPose.ZERO);
        for (int i = 0; i < WOLF_TAIL_LENGTH; i++) {
            partDefinition2 = partDefinition2.addOrReplaceChild("real_tail" + i,
                    CubeListBuilder.create()
                            .texOffs(9, Math.min(19 + i, 25))
                            .addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, cubeDeformation),
                    PartPose.offset(0.0F, 1.0F, 0.0F));
            CubeDeformation cubeDeformation1 = cubeDeformation.extend(getTailFluffiness(i));
            partDefinition4 = partDefinition4.addOrReplaceChild("real_fluffy_tail" + i,
                    CubeListBuilder.create()
                            .texOffs(9, Math.min(19 + i, 25))
                            .addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, cubeDeformation1),
                    PartPose.offset(0.0F, 1.0F + getTailFluffiness(i), 0.0F));
        }
    }

    private static float getTailFluffiness(int index) {
        if (index < 5) {
            return 0.1F + 0.1F * index;
        } else if (index == 5) {
            return 0.4F;
        } else {
            return 0.15F;
        }
    }

    @Override
    public void setupAnim(WolfRenderState renderState) {
        super.setupAnim(renderState);
        if (renderState.isSitting && PlayfulDoggyElement.sittingAnim.lieDown()) {
            // designed to go on top the vanilla super call (where to model is positioned for sitting)
            // full x, y, z values for every model part from adult model remain present as comment above
            float ageScale = renderState.ageScale;
            // -1.0F, 17.5F, -3.0F
            this.upperBody.y += 1.5F * ageScale;
            this.upperBody.xRot = Mth.HALF_PI;
            this.upperBody.yRot = 0.0F;
            // 0.0F, 17.5F, 0.0F
            this.body.y -= 0.5F * ageScale;
            this.body.xRot = Mth.PI / 7F * 3F;
            // -1.0F, 19.0F, 6.0F
            this.tail.y -= 2.0F * ageScale;
            // -2.5F, 22.0F, 5.25F
            this.rightHindLeg.y -= 0.7F * ageScale;
            this.rightHindLeg.z += 3.25F * ageScale;
            this.rightHindLeg.xRot = (Mth.PI * 3F / 2F);
            this.rightHindLeg.yRot = 0.4F;
            // 0.5F, 22.0F, 5.25F
            this.leftHindLeg.y -= 0.7F * ageScale;
            this.leftHindLeg.z += 3.25F * ageScale;
            this.leftHindLeg.xRot = (Mth.PI * 3F / 2F);
            this.leftHindLeg.yRot = -0.4F;
            // -2.49F, 21.5F, -2.0F
            this.rightFrontLeg.y += 4.5F * ageScale;
            this.rightFrontLeg.z += 2.0F * ageScale;
            this.rightFrontLeg.xRot = (Mth.PI * 3F / 2F);
            this.rightFrontLeg.yRot = 0.15F;
            // 0.51F, 21.5F, -2.0F
            this.leftFrontLeg.y += 4.5F * ageScale;
            this.leftFrontLeg.z += 2.0F * ageScale;
            this.leftFrontLeg.xRot = (Mth.PI * 3F / 2F);
            this.leftFrontLeg.yRot = -0.15F;
            this.head.y += 3.5F * ageScale;

            float rollAnim = PlayfulDoggyElement.getRollAnimScale(renderState) * PlayfulDoggyElement.MAX_ROLL_ANIM;
            if (rollAnim != 0.0F) {
                this.rightHindLeg.xRot += rollAnim * 1.5F;
                this.leftHindLeg.xRot += rollAnim * 1.5F;
                this.rightFrontLeg.xRot += rollAnim * 1.5F;
                this.leftFrontLeg.xRot += rollAnim * 1.5F;
                this.rightHindLeg.y -= rollAnim * 1.75F;
                this.leftHindLeg.y -= rollAnim * 1.75F;
                this.rightFrontLeg.y -= rollAnim * 1.75F;
                this.leftFrontLeg.y -= rollAnim * 1.75F;
                this.realHead.zRot = -rollAnim * 1.5F;
            }
        }

        this.setupTailAnim(renderState);
    }

    private void setupTailAnim(WolfRenderState renderState) {
        float progress = renderState.ageInTicks / 3.978873F;
        float magnitude = (0.5F + Math.max(renderState.walkAnimationSpeed, renderState.headRollAngle * 1.5F)) * 0.25F;
        float amplitude = renderState.walkAnimationPos * 0.6662F + progress * 0.6662F;
        if (renderState.collarColor == null) {
            this.tail.xRot += Mth.sin(amplitude) * magnitude;
            this.tail.yRot = 0.0F;
            for (int i = 0; i < this.realTailParts.length; i++) {
                this.realTailParts[i].zRot = 0.0F;
                this.realTailParts[i].xRot =
                        Mth.sin(amplitude - (float) (i + 1) * PlayfulDoggyElement.animationSpeed * 0.15F) * magnitude;
            }
        } else {
            this.tail.yRot = Mth.sin(amplitude) * magnitude;
            for (int i = 0; i < this.realTailParts.length; i++) {
                this.realTailParts[i].xRot = 0.0F;
                this.realTailParts[i].zRot =
                        Mth.sin(amplitude - (float) (i + 1) * PlayfulDoggyElement.animationSpeed * 0.15F) * magnitude;
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

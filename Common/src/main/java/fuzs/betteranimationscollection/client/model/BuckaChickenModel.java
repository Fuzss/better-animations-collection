package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.BuckaChickenElement;
import fuzs.betteranimationscollection.client.element.SoundBasedElement;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.ColdChickenModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.util.Mth;

import java.util.Set;

public class BuckaChickenModel extends ChickenModel {
    public static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(Set.of("head"));

    private final ModelPart head;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart redThing;
    private final ModelPart billTop;
    private final ModelPart billBottom;

    public BuckaChickenModel(ModelPart modelPart) {
        super(modelPart);
        this.head = modelPart.getChild("head");
        this.rightWing = modelPart.getChild("right_wing");
        this.leftWing = modelPart.getChild("left_wing");
        this.billTop = this.head.getChild("bill_top");
        this.billBottom = this.billTop.getChild("bill_bottom");
        this.redThing = this.billBottom.getChild("red_thing");
    }

    public static LayerDefinition createAnimatedBodyLayer() {
        LayerDefinition layerDefinition = ChickenModel.createBodyLayer();
        MeshDefinition meshDefinition = layerDefinition.mesh;
        modifyMesh(meshDefinition.getRoot(), BuckaChickenElement.slimBill);
        return layerDefinition;
    }

    public static LayerDefinition createAnimatedColdBodyLayer() {
        LayerDefinition layerDefinition = ColdChickenModel.createBodyLayer();
        MeshDefinition meshDefinition = layerDefinition.mesh;
        modifyMesh(meshDefinition.getRoot(), BuckaChickenElement.slimBill);
        return layerDefinition;
    }

    private static void modifyMesh(PartDefinition partDefinition, boolean slimBeak) {
        PartDefinition partDefinition1 = partDefinition.getChild("head");
        partDefinition1.clearChild("beak");
        partDefinition1.clearChild("red_thing");
        PartDefinition partDefinition2;
        if (slimBeak) {
            partDefinition2 = partDefinition1.addOrReplaceChild("bill_top",
                    CubeListBuilder.create().texOffs(15, 0).addBox(-1.0F, -4.0F, -4.0F, 2.0F, 1.0F, 2.0F),
                    PartPose.ZERO);
        } else {
            partDefinition2 = partDefinition1.addOrReplaceChild("bill_top",
                    CubeListBuilder.create().texOffs(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 1.0F, 2.0F),
                    PartPose.ZERO);
        }

        PartDefinition partDefinition3;
        if (slimBeak) {
            partDefinition3 = partDefinition2.addOrReplaceChild("bill_bottom",
                    CubeListBuilder.create().texOffs(15, 1).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F),
                    PartPose.offset(0.0F, -3.0F, -2.0F));
        } else {
            partDefinition3 = partDefinition2.addOrReplaceChild("bill_bottom",
                    CubeListBuilder.create().texOffs(14, 1).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 1.0F, 2.0F),
                    PartPose.offset(0.0F, -3.0F, -2.0F));
        }

        CubeListBuilder cubeListBuilder = CubeListBuilder.create()
                .texOffs(14, 4)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F);
        PartPose partPose = PartPose.offset(0.0F, 1.0F, 0.0F);
        partDefinition3.addOrReplaceChild("red_thing", cubeListBuilder, partPose);
        // fix rotation point to be at body and not in air
        partDefinition.addOrReplaceChild("right_wing",
                CubeListBuilder.create().texOffs(24, 13).addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F),
                PartPose.offset(3.0F, 13.0F, 0.0F));
        partDefinition.addOrReplaceChild("left_wing",
                CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F),
                PartPose.offset(-3.0F, 13.0F, 0.0F));
    }

    @Override
    public void setupAnim(ChickenRenderState renderState) {
        super.setupAnim(renderState);
        float soundTime = RenderStateExtraData.getOrDefault(renderState,
                SoundBasedElement.AMBIENT_SOUND_TIME_PROPERTY,
                0.0F);
        if (0.0F < soundTime && soundTime < 8.0F) {
            float rotation = Math.abs(Mth.sin(soundTime * Mth.PI / 5.0F));
            this.billBottom.xRot = rotation * 0.75F;
        } else {
            this.billBottom.xRot = 0.0F;
        }

        if (renderState.flapSpeed == 0 && BuckaChickenElement.moveWings) {
            float wingSwingAmount = renderState.walkAnimationSpeed * BuckaChickenElement.wingAnimationSpeed * 0.1F;
            float wingFlapRot = Mth.sin(renderState.walkAnimationPos) * wingSwingAmount + wingSwingAmount;
            this.rightWing.zRot = -wingFlapRot;
            this.leftWing.zRot = wingFlapRot;
        } else {
            float flapSpeed = (Mth.sin(renderState.flap) + 1.0F) * renderState.flapSpeed;
            this.rightWing.zRot = -flapSpeed;
            this.leftWing.zRot = flapSpeed;
        }

        if (BuckaChickenElement.moveHead) {
            this.head.z += Mth.cos(renderState.walkAnimationPos) * BuckaChickenElement.headAnimationSpeed * 0.5F
                    * renderState.walkAnimationSpeed * renderState.ageScale;
        }

        if (BuckaChickenElement.moveWattles) {
            this.redThing.zRot =
                    Mth.sin(renderState.walkAnimationPos) * (float) BuckaChickenElement.wattlesAnimationSpeed * 0.1F
                            * renderState.walkAnimationSpeed;
        }
    }
}
package fuzs.betteranimationscollection.common.client.model;

import fuzs.betteranimationscollection.common.client.element.BuckaChickenElement;
import fuzs.betteranimationscollection.common.client.element.SoundBasedElement;
import fuzs.puzzleslib.common.api.client.renderer.v1.RenderStateExtraData;
import net.minecraft.client.model.animal.chicken.AdultChickenModel;
import net.minecraft.client.model.animal.chicken.ColdChickenModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.util.Mth;

public class BuckaChickenModel extends AdultChickenModel {
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
        LayerDefinition layerDefinition = AdultChickenModel.createBodyLayer();
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
    public void setupAnim(ChickenRenderState state) {
        super.setupAnim(state);
        float soundTime = RenderStateExtraData.getOrDefault(state, SoundBasedElement.AMBIENT_SOUND_TIME_PROPERTY, 0.0F);
        if (0.0F < soundTime && soundTime < 8.0F) {
            float rotation = Math.abs(Mth.sin(soundTime * Mth.PI / 5.0F));
            this.billBottom.xRot = rotation * 0.75F;
        } else {
            this.billBottom.xRot = 0.0F;
        }

        if (state.flapSpeed == 0 && BuckaChickenElement.moveWings) {
            float wingSwingAmount = state.walkAnimationSpeed * BuckaChickenElement.wingAnimationSpeed * 0.1F;
            float wingFlapRot = Mth.sin(state.walkAnimationPos) * wingSwingAmount + wingSwingAmount;
            this.rightWing.zRot = -wingFlapRot;
            this.leftWing.zRot = wingFlapRot;
        } else {
            float flapSpeed = (Mth.sin(state.flap) + 1.0F) * state.flapSpeed;
            this.rightWing.zRot = -flapSpeed;
            this.leftWing.zRot = flapSpeed;
        }

        if (BuckaChickenElement.moveHead) {
            this.head.z += Mth.cos(state.walkAnimationPos) * BuckaChickenElement.headAnimationSpeed * 0.5F
                    * state.walkAnimationSpeed * state.ageScale;
        }

        if (BuckaChickenElement.moveWattles) {
            this.redThing.zRot =
                    Mth.sin(state.walkAnimationPos) * (float) BuckaChickenElement.wattlesAnimationSpeed * 0.1F
                            * state.walkAnimationSpeed;
        }
    }
}

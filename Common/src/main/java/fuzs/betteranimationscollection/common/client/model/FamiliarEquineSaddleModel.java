package fuzs.betteranimationscollection.common.client.model;

import net.minecraft.client.model.animal.equine.EquineSaddleModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.EquineRenderState;

public class FamiliarEquineSaddleModel extends EquineSaddleModel {
    private final ModelPart upperMouth;
    private final ModelPart lowerMouth;

    public FamiliarEquineSaddleModel(ModelPart modelPart) {
        super(modelPart);
        ModelPart headParts = modelPart.getChild("head_parts");
        this.upperMouth = headParts.getChild("upper_mouth");
        this.lowerMouth = headParts.getChild("lower_mouth");
    }

    public static void modifyMesh(PartDefinition partDefinition) {
        PartDefinition headParts = partDefinition.getChild("head_parts");
        PartDefinition upperMouth = headParts.getChild("upper_mouth");
        PartDefinition lowerMouth = headParts.getChild("lower_mouth");
        headParts.clearChild("left_saddle_mouth");
        headParts.clearChild("right_saddle_mouth");
        upperMouth.addOrReplaceChild("left_saddle_mouth",
                CubeListBuilder.create().texOffs(29, 5).addBox(2.0F, -1.0F, -4.0F, 1.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        upperMouth.addOrReplaceChild("right_saddle_mouth",
                CubeListBuilder.create().texOffs(29, 5).addBox(-3.0F, -1.0F, -4.0F, 1.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        headParts.clearChild("mouth_saddle_wrap");
        upperMouth.addOrReplaceChild("mouth_saddle_wrap",
                CubeListBuilder.create()
                        .texOffs(19, 0)
                        .addBox(-2.0F, -3.125F, -2.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.2F)),
                PartPose.ZERO);
        lowerMouth.addOrReplaceChild("lower_mouth_saddle_wrap",
                CubeListBuilder.create()
                        .texOffs(19, 0)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.18F)),
                PartPose.ZERO);
    }

    @Override
    public void setupAnim(EquineRenderState state) {
        super.setupAnim(state);
        FamiliarHorseModel.setupMouthAnim(state, this.upperMouth, this.lowerMouth);
    }
}

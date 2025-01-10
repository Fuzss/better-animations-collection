package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.SheepRenderState;

public class KneelingSheepFurModel extends SheepFurModel {

    public KneelingSheepFurModel(ModelPart modelPart) {
        super(modelPart);
    }

    public static LayerDefinition createAnimatedFurLayer() {
        LayerDefinition layerDefinition = SheepFurModel.createFurLayer();
        MeshDefinition meshDefinition = layerDefinition.mesh;
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(28, 8)
                        .addBox(-4.0F, -15.0F, 0.0F, 8.0F, 16.0F, 6.0F, new CubeDeformation(1.75F)),
                PartPose.offset(0.0F, 12.0F, 7.0F));
        return layerDefinition;
    }

    @Override
    public void setupAnim(SheepRenderState renderState) {
        super.setupAnim(renderState);
        float rotation = renderState.headEatPositionScale;
        this.body.xRot += rotation * 0.4F;
        this.rightFrontLeg.y = this.leftFrontLeg.y = 12.0F + rotation * 4.0F;
        this.rightFrontLeg.xRot -= rotation;
        this.leftFrontLeg.xRot -= rotation;
    }
}
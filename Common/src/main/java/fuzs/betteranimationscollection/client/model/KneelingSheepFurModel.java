package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.util.Mth;

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
                PartPose.offsetAndRotation(0.0F, 12.0F, 7.0F, Mth.HALF_PI, 0.0F, 0.0F));
        return layerDefinition;
    }

    @Override
    public void setupAnim(SheepRenderState renderState) {
        super.setupAnim(renderState);
        KneelingSheepModel.setupAnim(renderState, this.body, this.rightFrontLeg, this.leftFrontLeg);
    }
}

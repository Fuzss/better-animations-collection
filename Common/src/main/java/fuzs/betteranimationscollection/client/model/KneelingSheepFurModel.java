package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.mixin.client.accessor.LayerDefinitionAccessor;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.animal.Sheep;

public class KneelingSheepFurModel<T extends Sheep> extends SheepFurModel<T> {

    public KneelingSheepFurModel(ModelPart modelPart) {
        super(modelPart);
    }
    
    public static LayerDefinition createAnimatedFurLayer() {
        LayerDefinition layerDefinition = SheepFurModel.createFurLayer();
        MeshDefinition meshDefinition = ((LayerDefinitionAccessor) layerDefinition).getMesh();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 8).addBox(-4.0F, -15.0F, 0.0F, 8.0F, 16.0F, 6.0F, new CubeDeformation(1.75F)), PartPose.offset(0.0F, 12.0F, 7.0F));
        return layerDefinition;
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        // this was removed from super in 1.17, but we need it since we modify the rotation for some sheep only
        this.body.xRot = ((float) Math.PI / 2.0F);
        // small trick to get partial ticks in here again
        float partialTicks = ageInTicks - entityIn.tickCount;
        float rotation = entityIn.getHeadEatPositionScale(partialTicks);
        this.body.xRot += rotation * 0.4F;
        this.rightFrontLeg.y = this.leftFrontLeg.y = 12.0F + rotation * 4.0F;
        this.rightFrontLeg.xRot -= rotation;
        this.leftFrontLeg.xRot -= rotation;
    }
}
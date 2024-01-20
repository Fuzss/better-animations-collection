package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.CowUdderElement;
import fuzs.betteranimationscollection.mixin.client.accessor.AgeableListModelAccessor;
import fuzs.betteranimationscollection.mixin.client.accessor.LayerDefinitionAccessor;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class CowUdderModel<T extends Entity> extends CowModel<T> {
    private final ModelPart utter;
    private final ModelPart[] nipples = new ModelPart[4];

    public CowUdderModel(ModelPart modelPart) {
        super(modelPart);
        ModelPart body = modelPart.getChild("body");
        this.utter = body.getChild("utter");
        for (int i = 0; i < this.nipples.length; i++) {
            this.nipples[i] = this.utter.getChild("nipple" + i);
        }
        // fix calf head being positioned wrongly since vanilla 1.15, don't need a config option for that
        ((AgeableListModelAccessor) this).setBabyYHeadOffset(8.0F);
        ((AgeableListModelAccessor) this).setBabyZHeadOffset(6.0F);
    }

    public static LayerDefinition createAnimatedBodyLayer() {
        LayerDefinition layerDefinition = CowModel.createBodyLayer();
        MeshDefinition meshDefinition = ((LayerDefinitionAccessor) layerDefinition).getMesh();
        PartDefinition partDefinition = meshDefinition.getRoot();
        // body has to be replaced as it already includes the utter normally
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
        PartDefinition partDefinition2 = partDefinition1.addOrReplaceChild("utter", CubeListBuilder.create().texOffs(52, 0).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 6.0F, 1.0F), PartPose.offset(0.0F, 4.5F, -6.0F));
        for (int i = 0; i < 4; i++) {
            partDefinition2.addOrReplaceChild("nipple" + i, CubeListBuilder.create().texOffs(52, 0).addBox(i % 2 == 0 ? -2.0F : 1.0F, i / 2 == 0 ? -1.5F : 1.5F, -3.0F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);
        }
        return layerDefinition;
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.utter.visible = !this.young || CowUdderElement.calfUtter;
        for (ModelPart renderer : this.nipples) {
            renderer.visible = CowUdderElement.showNipples;
        }
    }

    @Override
    public void prepareMobModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.utter.xRot = Mth.sin(limbSwing * 0.5F) * limbSwingAmount * CowUdderElement.animationSpeed * 0.05F;
        this.utter.yRot = Mth.cos(limbSwing) * limbSwingAmount * CowUdderElement.animationSpeed * 0.125F;
    }
}

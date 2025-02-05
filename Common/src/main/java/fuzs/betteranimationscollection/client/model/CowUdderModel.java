package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.CowUdderElement;
import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

import java.util.Set;

public class CowUdderModel extends CowModel {
    /**
     * Fix calf head being positioned wrongly since vanilla 1.15, don't need a config option for that.
     */
    public static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(false, 8.0F, 6.0F, Set.of("head"));

    private final ModelPart utter;
    private final ModelPart[] nipples = new ModelPart[4];

    public CowUdderModel(ModelPart modelPart) {
        super(modelPart);
        this.utter = modelPart.getChild("body").getChild("utter");
        for (int i = 0; i < this.nipples.length; i++) {
            this.nipples[i] = this.utter.getChild("nipple" + i);
        }
    }

    public static LayerDefinition createAnimatedBodyLayer() {
        LayerDefinition layerDefinition = CowModel.createBodyLayer();
        MeshDefinition meshDefinition = layerDefinition.mesh;
        PartDefinition partDefinition = meshDefinition.getRoot();
        // body has to be replaced as it already includes the utter normally
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
        PartDefinition partDefinition2 = partDefinition1.addOrReplaceChild("utter",
                CubeListBuilder.create().texOffs(52, 0).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 6.0F, 1.0F),
                PartPose.offset(0.0F, 4.5F, -6.0F));
        for (int i = 0; i < 4; i++) {
            partDefinition2.addOrReplaceChild("nipple" + i,
                    CubeListBuilder.create()
                            .texOffs(52, 0)
                            .addBox(i % 2 == 0 ? -2.0F : 1.0F, i / 2 == 0 ? -1.5F : 1.5F, -3.0F, 1.0F, 1.0F, 1.0F),
                    PartPose.ZERO);
        }
        return layerDefinition;
    }

    @Override
    public void setupAnim(LivingEntityRenderState renderState) {
        super.setupAnim(renderState);
        this.utter.xRot = Mth.sin(renderState.walkAnimationPos * 0.5F) * renderState.walkAnimationSpeed *
                CowUdderElement.animationSpeed * 0.05F;
        this.utter.yRot = Mth.cos(renderState.walkAnimationPos) * renderState.walkAnimationSpeed *
                CowUdderElement.animationSpeed * 0.125F;
        this.utter.visible = !renderState.isBaby || CowUdderElement.calfUtter;
        for (ModelPart renderer : this.nipples) {
            renderer.visible = CowUdderElement.showNipples;
        }
    }
}

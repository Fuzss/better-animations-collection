package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.CowUdderElement;
import net.minecraft.client.model.animal.cow.ColdCowModel;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.animal.cow.WarmCowModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class CowUdderModel extends CowModel {
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
        return CowModel.createBodyLayer().apply((MeshDefinition meshDefinition) -> {
            modifyMesh(meshDefinition.getRoot());
            return meshDefinition;
        });
    }

    public static LayerDefinition createAnimatedColdBodyLayer() {
        return ColdCowModel.createBodyLayer().apply((MeshDefinition meshDefinition) -> {
            modifyMesh(meshDefinition.getRoot());
            // copied from the cold cow model, as we replace the body
            meshDefinition.getRoot()
                    .getChild("body")
                    .addOrReplaceChild("wool",
                            CubeListBuilder.create()
                                    .texOffs(20, 32)
                                    .addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F, new CubeDeformation(0.5F)),
                            PartPose.ZERO);
            return meshDefinition;
        });
    }

    public static LayerDefinition createAnimatedWarmBodyLayer() {
        return WarmCowModel.createBodyLayer().apply((MeshDefinition meshDefinition) -> {
            modifyMesh(meshDefinition.getRoot());
            return meshDefinition;
        });
    }

    private static void modifyMesh(PartDefinition partDefinition) {
        // the body has to be replaced as it already includes the utter normally
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, Mth.HALF_PI, 0.0F, 0.0F));
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
    }

    @Override
    public void setupAnim(LivingEntityRenderState renderState) {
        super.setupAnim(renderState);
        this.utter.xRot = Mth.sin(renderState.walkAnimationPos * 0.5F) * renderState.walkAnimationSpeed
                * CowUdderElement.animationSpeed * 0.05F;
        this.utter.yRot =
                Mth.cos(renderState.walkAnimationPos) * renderState.walkAnimationSpeed * CowUdderElement.animationSpeed
                        * 0.125F;
        this.utter.visible = !renderState.isBaby || CowUdderElement.calfUtter;
        for (ModelPart renderer : this.nipples) {
            renderer.visible = CowUdderElement.showNipples;
        }
    }
}

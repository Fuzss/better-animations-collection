package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.SoundBasedElement;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.util.Mth;

public class IronGolemNoseModel extends IronGolemModel {
    private final ModelPart nose;

    public IronGolemNoseModel(ModelPart modelPart) {
        super(modelPart);
        this.nose = modelPart.getChild("head").getChild("nose");
    }

    public static LayerDefinition createAnimatedBodyLayer() {
        return IronGolemModel.createBodyLayer().apply((MeshDefinition meshDefinition) -> {
            modifyMesh(meshDefinition.getRoot());
            return meshDefinition;
        });
    }

    private static void modifyMesh(PartDefinition partDefinition) {
        // separate nose from head model
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F),
                PartPose.offset(0.0F, -7.0F, -2.0F));
        partDefinition1.addOrReplaceChild("nose",
                CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -7.5F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(0.0F, -4.0F, 0.0F));
    }

    @Override
    public void setupAnim(IronGolemRenderState renderState) {
        super.setupAnim(renderState);
        float soundTime = RenderStateExtraData.getOrDefault(renderState,
                SoundBasedElement.AMBIENT_SOUND_TIME_PROPERTY,
                0.0F);
        float maxSoundTime = 20.0F;
        if (0.0F < soundTime && soundTime < maxSoundTime) {
            float rotation = Mth.sin(soundTime * ((3.0F * Mth.PI) / 20.0F));
            this.nose.zRot = rotation * 0.75F * ((maxSoundTime - soundTime) / 20.0F);
        } else {
            this.nose.zRot = 0.0F;
        }
    }
}

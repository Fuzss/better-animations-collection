package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.SoundBasedElement;
import fuzs.puzzleslib.api.client.util.v1.RenderPropertyKey;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LlamaRenderState;
import net.minecraft.util.Mth;

public class SpitfulLlamaModel extends LlamaModel {
    private final ModelPart mouth;

    public SpitfulLlamaModel(ModelPart modelPart) {
        super(modelPart);
        this.mouth = modelPart.getChild("head").getChild("mouth");
    }

    public static LayerDefinition createAnimatedBodyLayer(CubeDeformation cubeDeformation) {
        LayerDefinition layerDefinition = LlamaModel.createBodyLayer(cubeDeformation);
        MeshDefinition meshDefinition = layerDefinition.mesh;
        PartDefinition partDefinition = meshDefinition.getRoot();
        // overwrite normal head as it already includes the mouth
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.0F, -14.0F, -10.0F, 4.0F, 2.0F, 9.0F, cubeDeformation)
                        .texOffs(0, 14)
                        .addBox("neck", -4.0F, -16.0F, -6.0F, 8.0F, 18.0F, 6.0F, cubeDeformation)
                        .texOffs(17, 0)
                        .addBox("ear", -4.0F, -19.0F, -4.0F, 3.0F, 3.0F, 2.0F, cubeDeformation)
                        .texOffs(17, 0)
                        .addBox("ear", 1.0F, -19.0F, -4.0F, 3.0F, 3.0F, 2.0F, cubeDeformation),
                PartPose.offset(0.0F, 7.0F, -6.0F));
        // make mouth a separate part
        partDefinition1.addOrReplaceChild("mouth",
                CubeListBuilder.create().texOffs(0, 2).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 2.0F, 9.0F, cubeDeformation),
                PartPose.offset(0.0F, -12.0F, -6.0F));
        return layerDefinition;
    }

    @Override
    public void setupAnim(LlamaRenderState renderState) {
        super.setupAnim(renderState);
        float soundTime = RenderPropertyKey.getRenderProperty(renderState,
                SoundBasedElement.AMBIENT_SOUND_TIME_PROPERTY);
        if (0.0F < soundTime && soundTime < 5.0F) {
            float rotation = Math.abs(Mth.sin(soundTime * Mth.PI / 5.0F));
            this.mouth.xRot = rotation * 0.75F;
        } else {
            this.mouth.xRot = 0.0F;
        }
    }
}

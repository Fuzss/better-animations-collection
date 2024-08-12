package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.mixin.client.accessor.LayerDefinitionAccessor;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;

public class SpitfulLlamaModel<T extends AbstractChestedHorse> extends LlamaModel<T> {
    private final ModelPart mouth;
    
    public SpitfulLlamaModel(ModelPart modelPart) {
        super(modelPart);
        ModelPart head = modelPart.getChild("head");
        this.mouth = head.getChild("mouth");
    }

    public static LayerDefinition createAnimatedBodyLayer(CubeDeformation cubeDeformation) {
        LayerDefinition layerDefinition = LlamaModel.createBodyLayer(cubeDeformation);
        MeshDefinition meshDefinition = ((LayerDefinitionAccessor) layerDefinition).getMesh();
        PartDefinition partDefinition = meshDefinition.getRoot();
        // overwrite normal head as it already includes the mouth
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.0F, -14.0F, -10.0F, 4.0F, 2.0F, 9.0F, cubeDeformation)
                .texOffs(0, 14).addBox("neck", -4.0F, -16.0F, -6.0F, 8.0F, 18.0F, 6.0F, cubeDeformation)
                .texOffs(17, 0).addBox("ear", -4.0F, -19.0F, -4.0F, 3.0F, 3.0F, 2.0F, cubeDeformation)
                .texOffs(17, 0).addBox("ear", 1.0F, -19.0F, -4.0F, 3.0F, 3.0F, 2.0F, cubeDeformation),
                PartPose.offset(0.0F, 7.0F, -6.0F));
        // make mouth a separate part
        partDefinition1.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(0, 2).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 2.0F, 9.0F, cubeDeformation), PartPose.offset(0.0F, -12.0F, -6.0F));
        return layerDefinition;
    }

    @Override
    public void prepareMobModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        // this only works because MobEntity#ambientSoundTime is manually being synced to the client in {@link fuzs.betteranimationscollection.client.element.SyncSoundElement}
        int soundTime = entitylivingbaseIn.ambientSoundTime + entitylivingbaseIn.getAmbientSoundInterval();
        if (0 < soundTime && soundTime < 5) {
            float rotation = Math.abs(Mth.sin((float) soundTime * (float) Math.PI / 5.0F));
            this.mouth.xRot = rotation * 0.75F;
        } else {
            this.mouth.xRot = 0.0F;
        }
    }
}

package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.mixin.client.accessor.LayerDefinitionAccessor;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.IronGolem;

public class IronGolemNoseModel<T extends IronGolem> extends IronGolemModel<T> {
    private final ModelPart nose;

    public IronGolemNoseModel(ModelPart modelPart) {
        super(modelPart);
        ModelPart head = modelPart.getChild("head");
        this.nose = head.getChild("nose");
    }

    public static LayerDefinition createAnimatedBodyLayer() {
        LayerDefinition layerDefinition = IronGolemModel.createBodyLayer();
        MeshDefinition meshDefinition = ((LayerDefinitionAccessor) layerDefinition).getMesh();
        PartDefinition partDefinition = meshDefinition.getRoot();
        // separate nose from head model
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F), PartPose.offset(0.0F, -7.0F, -2.0F));
        partDefinition1.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -7.5F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, -4.0F, 0.0F));
        return layerDefinition;
    }

    @Override
    public void prepareMobModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        super.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
        // this only works because MobEntity#ambientSoundTime is manually being synced to the client in {@link fuzs.betteranimationscollection.client.element.SyncSoundElement}
        int soundTime = entitylivingbaseIn.ambientSoundTime + entitylivingbaseIn.getAmbientSoundInterval();
        final int maxSoundTime = 20;
        if (0 < soundTime && soundTime < maxSoundTime) {
            float rotation = Mth.sin((float) soundTime * (float) ((3.0F * Math.PI) / 20.0F));
            this.nose.zRot = rotation * 0.75F * ((float) (maxSoundTime - soundTime) / 20.0F);
        } else {
            this.nose.zRot = 0.0F;
        }
    }
}

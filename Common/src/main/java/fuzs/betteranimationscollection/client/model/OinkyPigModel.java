package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.OinkyPigElement;
import fuzs.betteranimationscollection.mixin.client.accessor.LayerDefinitionAccessor;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class OinkyPigModel<T extends Entity> extends PigModel<T> {
    private final ModelPart snout;
    private final ModelPart rightEar;
    private final ModelPart leftEar;

    public OinkyPigModel(ModelPart modelPart) {
        super(modelPart);
        ModelPart head = modelPart.getChild("head");
        this.snout = head.getChild("snout");
        this.rightEar = head.getChild("right_ear");
        this.leftEar = head.getChild("left_ear");
    }

    public static LayerDefinition createAnimatedBodyLayer(CubeDeformation cubeDeformation) {
        LayerDefinition layerDefinition = PigModel.createBodyLayer(cubeDeformation);
        MeshDefinition meshDefinition = ((LayerDefinitionAccessor) layerDefinition).getMesh();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, cubeDeformation), PartPose.offset(0.0F, 12.0F, -6.0F));
        partDefinition1.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(16, 16).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 3.0F, 1.0F, cubeDeformation), PartPose.offset(0.0F, 3.0F, -8.0F));
        partDefinition1.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(16, 4).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, cubeDeformation), PartPose.offset(3.5F, -2.0F, -4.0F));
        partDefinition1.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(16, 4).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, cubeDeformation), PartPose.offsetAndRotation(-3.5F, -2.0F, -4.0F, 0.0F, (float) Math.PI, 0.0F));
        return layerDefinition;
    }

    @Override
    public void prepareMobModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        if (entitylivingbaseIn instanceof Mob mob) {
            // this only works because MobEntity#ambientSoundTime is manually being synced to the client in {@link fuzs.betteranimationscollection.client.element.SyncSoundElement}
            int soundTime = mob.ambientSoundTime + mob.getAmbientSoundInterval();
            if (0 < soundTime && soundTime < 8) {
                float rotation = Mth.sin((float) soundTime * (float) (Math.PI / 8.0F));
                this.snout.y = 3.0F - rotation;
            } else {
                this.snout.y = 3.0F;
            }
        }
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.rightEar.visible = OinkyPigElement.floatyEars;
        this.leftEar.visible = OinkyPigElement.floatyEars;
        if (OinkyPigElement.floatyEars) {
            float f1 = ageInTicks * 0.1F + limbSwing * 0.5F;
            float f2 = 0.08F + limbSwingAmount * OinkyPigElement.earAnimationSpeed * 0.04F;
            this.rightEar.zRot = (-(float) Math.PI / 6.0F) - Mth.cos(f1 * 1.2F) * f2;
            this.leftEar.zRot = ((float) Math.PI / 6.0F) + Mth.cos(f1) * f2;
        }
    }
}
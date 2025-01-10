package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.SquidTentaclesElement;
import fuzs.betteranimationscollection.mixin.client.accessor.LayerDefinitionAccessor;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class SquidTentaclesModel<T extends Entity> extends SquidModel<T> {
    public static final int SQUID_TENTACLES_LENGTH = 8;

    private final ModelPart[] tentacles = new ModelPart[8];
    private final ModelPart[][] tentacleParts = new ModelPart[8][];

    public SquidTentaclesModel(ModelPart modelPart) {
        super(modelPart);
        for (int j = 0; j < this.tentacles.length; j++) {
            this.tentacles[j] = modelPart.getChild("tentacle" + j);
            this.tentacleParts[j] = new ModelPart[SQUID_TENTACLES_LENGTH];
            for (int i = 0; i < this.tentacleParts[j].length; i++) {
                String partName = "tentacle" + j + i;
                if (i == 0) {
                    this.tentacleParts[j][i] = this.tentacles[j].getChild(partName);
                } else {
                    this.tentacleParts[j][i] = this.tentacleParts[j][i - 1].getChild(partName);
                }
            }
        }
    }

    public static LayerDefinition createAnimatedBodyLayer() {
        LayerDefinition layerDefinition = SquidModel.createBodyLayer();
        MeshDefinition meshDefinition = ((LayerDefinitionAccessor) layerDefinition).getMesh();
        PartDefinition partDefinition = meshDefinition.getRoot();
        CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F);
        for (int i = 0; i < SQUID_TENTACLES_LENGTH; ++i) {
            double swingAmount = i * Math.PI * 2.0 / 8.0;
            float x = (float) Math.cos(swingAmount) * 5.0F;
            float z = (float) Math.sin(swingAmount) * 5.0F;
            swingAmount = i * Math.PI * -2.0 / 8.0 + Math.PI / 2.0;
            PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("tentacle" + i, cubeListBuilder, PartPose.offsetAndRotation(x, 15.0F, z, 0.0F, (float) swingAmount, 0.0F));
            for (int k = 0; k < 8; k++) {
                CubeListBuilder cubeListBuilder1 = CubeListBuilder.create().texOffs(48, 2 + 2 * k).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F);
                partDefinition1 = partDefinition1.addOrReplaceChild("tentacle" + i + k, cubeListBuilder1, PartPose.offset(0.0F, 2.0F, 0.0F));
            }
        }
        return layerDefinition;
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // always holds for squids, but other mods might use this model weirdly
        if (!(entityIn instanceof Mob mob)) return;
        float progress = ageInTicks / 1.75F;
        float magnitude = (float) (Math.sqrt(Math.abs(mob.getDeltaMovement().x) + Math.abs(mob.getDeltaMovement().y) + Math.abs(mob.getDeltaMovement().z)) - 0.075F);
        magnitude *= 0.375F;
        if (magnitude < 0.0F || !mob.isInWater()) magnitude = 0.0F;
        for (ModelPart modelPart : this.tentacles) {
            modelPart.xRot = ageInTicks * 2.0F;
        }
        for (int i = 0; i < this.tentacles.length; i++) {
            this.tentacles[i].xRot += (float) Math.sin(progress) * magnitude;
            for (int j = 0; j < this.tentacleParts[i].length; j++) {
                this.tentacleParts[i][j].xRot = -ageInTicks * 0.375F + (float) Math.sin(progress + (float) (j + 1)) * magnitude;
                this.tentacleParts[i][j].visible = i < SquidTentaclesElement.tentaclesLength;
            }
        }
    }
}

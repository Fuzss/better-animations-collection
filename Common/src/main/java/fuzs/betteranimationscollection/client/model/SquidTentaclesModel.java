package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.SquidTentaclesElement;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import net.minecraft.client.model.animal.squid.SquidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.SquidRenderState;
import net.minecraft.world.phys.Vec3;

public class SquidTentaclesModel extends SquidModel {
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
        return SquidModel.createBodyLayer().apply((MeshDefinition meshDefinition) -> {
            modifyMesh(meshDefinition.getRoot());
            return meshDefinition;
        });
    }

    private static void modifyMesh(PartDefinition partDefinition) {
        CubeListBuilder cubeListBuilder = CubeListBuilder.create()
                .texOffs(48, 0)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F);
        for (int i = 0; i < SQUID_TENTACLES_LENGTH; ++i) {
            double swingAmount = i * Math.PI * 2.0 / 8.0;
            float x = (float) Math.cos(swingAmount) * 5.0F;
            float z = (float) Math.sin(swingAmount) * 5.0F;
            swingAmount = i * Math.PI * -2.0 / 8.0 + Math.PI / 2.0;
            PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("tentacle" + i,
                    cubeListBuilder,
                    PartPose.offsetAndRotation(x, 15.0F, z, 0.0F, (float) swingAmount, 0.0F));
            for (int k = 0; k < 8; k++) {
                CubeListBuilder cubeListBuilder1 = CubeListBuilder.create()
                        .texOffs(48, 2 + 2 * k)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F);
                partDefinition1 = partDefinition1.addOrReplaceChild("tentacle" + i + k,
                        cubeListBuilder1,
                        PartPose.offset(0.0F, 2.0F, 0.0F));
            }
        }
    }

    @Override
    public void setupAnim(SquidRenderState renderState) {
        super.setupAnim(renderState);
        float progress = renderState.tentacleAngle / 1.75F;
        Vec3 deltaMovement = RenderStateExtraData.getOrDefault(renderState,
                SquidTentaclesElement.DELTA_MOVEMENT_PROPERTY,
                Vec3.ZERO);
        float magnitude = (float) (
                Math.sqrt(Math.abs(deltaMovement.x) + Math.abs(deltaMovement.y) + Math.abs(deltaMovement.z)) - 0.075F);
        magnitude *= 0.375F;
        if (magnitude < 0.0F || !renderState.isInWater) magnitude = 0.0F;
        for (ModelPart modelPart : this.tentacles) {
            modelPart.xRot = renderState.tentacleAngle * 2.0F;
        }
        for (int i = 0; i < this.tentacles.length; i++) {
            this.tentacles[i].xRot += (float) Math.sin(progress) * magnitude;
            for (int j = 0; j < this.tentacleParts[i].length; j++) {
                this.tentacleParts[i][j].xRot =
                        -renderState.tentacleAngle * 0.375F + (float) Math.sin(progress + (float) (j + 1)) * magnitude;
                this.tentacleParts[i][j].visible = j < SquidTentaclesElement.tentaclesLength;
            }
        }
    }
}

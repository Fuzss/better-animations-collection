package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.GhastTentaclesElement;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.monster.ghast.GhastModel;
import net.minecraft.client.renderer.entity.state.GhastRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class GhastTentaclesModel extends GhastModel {
    public static final int GHAST_MAX_TENTACLES_LENGTH = 14;

    private final ModelPart[] tentacles = new ModelPart[9];
    private final ModelPart[][] tentacleParts = new ModelPart[9][];

    public GhastTentaclesModel(ModelPart modelPart) {
        super(modelPart);
        for (int i = 0; i < this.tentacles.length; i++) {
            ModelPart tentacle = this.tentacles[i] = modelPart.getChild("tentacle" + i);
            this.tentacleParts[i] = new ModelPart[GHAST_MAX_TENTACLES_LENGTH];
            for (int j = 0; j < this.tentacleParts[i].length; j++) {
                tentacle = this.tentacleParts[i][j] = tentacle.getChild("tentacle_part" + j);
            }
        }
    }

    public static LayerDefinition createAnimatedBodyLayer() {
        return GhastModel.createBodyLayer().apply((MeshDefinition meshDefinition) -> {
            modifyMesh(meshDefinition.getRoot());
            return meshDefinition;
        });
    }

    private static void modifyMesh(PartDefinition partDefinition) {
        for (int i = 0; i < 9; ++i) {
            float offsetX = (((float) (i % 3) - (float) (i / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
            float offsetY = 24.6F;
            float offsetZ = ((float) (i / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
            PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("tentacle" + i,
                    CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                    PartPose.offset(offsetX, offsetY, offsetZ));
            for (int j = 0; j < GHAST_MAX_TENTACLES_LENGTH; j++) {
                partDefinition1 = partDefinition1.addOrReplaceChild("tentacle_part" + j,
                        CubeListBuilder.create().texOffs(0, 1 + j).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                        PartPose.offset(0.0F, 1.0F, 0.0F));
            }
        }
    }

    @Override
    public void setupAnim(GhastRenderState renderState) {
        super.setupAnim(renderState);
        float speed = 1.0F / (float) GhastTentaclesElement.animationSpeed;
        RandomSource randomSource = RandomSource.create(1660L);
        for (int i = 0; i < this.tentacles.length; i++) {
            this.tentacles[i].xRot = speed * Mth.sin(renderState.ageInTicks * speed + (float) i) + 0.4F;
            int randomLength = randomSource.nextInt(GhastTentaclesElement.maxTentaclesLength / 2)
                    + GhastTentaclesElement.maxTentaclesLength / 2 + 1;
            for (int j = 0; j < this.tentacleParts[i].length; j++) {
                this.tentacleParts[i][j].xRot =
                        speed * Mth.sin(renderState.ageInTicks * speed + (float) i - (float) j / 2.0F);
                this.tentacleParts[i][j].visible = j < randomLength;
            }
        }
    }
}

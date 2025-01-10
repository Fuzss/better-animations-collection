package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.FlailingEndermanElement;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.util.Mth;

public class FlailingEndermanModel extends EndermanModel<EndermanRenderState> {
    public static final int ENDERMAN_ARM_LENGTH = 12;

    private final ModelPart[] rightArmParts = new ModelPart[ENDERMAN_ARM_LENGTH];
    private final ModelPart[] leftArmParts = new ModelPart[ENDERMAN_ARM_LENGTH];

    public FlailingEndermanModel(ModelPart modelPart) {
        super(modelPart);
        ModelPart rightArm = modelPart.getChild("right_arm");
        for (int i = 0; i < this.rightArmParts.length; i++) {
            rightArm = this.rightArmParts[i] = rightArm.getChild("right_arm_part" + i);
        }
        ModelPart leftArm = modelPart.getChild("left_arm");
        for (int i = 0; i < this.leftArmParts.length; i++) {
            leftArm = this.leftArmParts[i] = leftArm.getChild("left_arm_part" + i);
        }
    }

    public static LayerDefinition createAnimatedBodyLayer() {
        LayerDefinition layerDefinition = EndermanModel.createBodyLayer();
        MeshDefinition meshDefinition = layerDefinition.mesh;
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("right_arm",
                CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-3.0F, -13.0F, 0.0F));
        for (int i = 0; i < ENDERMAN_ARM_LENGTH; i++) {
            partDefinition1 = partDefinition1.addOrReplaceChild("right_arm_part" + i,
                    CubeListBuilder.create().texOffs(56, 2 + i * 2).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                    PartPose.offset(0.0F, i == 0 ? 1.0F : 2.0F, 0.0F));
        }
        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("left_arm",
                CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, true),
                PartPose.offset(5.0F, -13.0F, 0.0F));
        for (int i = 0; i < ENDERMAN_ARM_LENGTH; i++) {
            partDefinition2 = partDefinition2.addOrReplaceChild("left_arm_part" + i,
                    CubeListBuilder.create().texOffs(56, 2 + i * 2).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, true),
                    PartPose.offset(0.0F, i == 0 ? 1.0F : 2.0F, 0.0F));
        }
        return layerDefinition;
    }

    @Override
    public void setupAnim(EndermanRenderState renderState) {
        super.setupAnim(renderState);
        if (renderState.isCreepy && (renderState.carriedBlock == null || FlailingEndermanElement.whileCarrying)) {
            final float animationSpeed = FlailingEndermanElement.animationSpeed * 0.025F;
            if (renderState.carriedBlock == null) {
                this.rightArm.zRot = 2.6F;
                this.leftArm.zRot = -2.6F;
                this.rightArm.xRot = 0.0F;
                this.leftArm.xRot = 0.0F;
                for (int i = 0; i < ENDERMAN_ARM_LENGTH; i++) {
                    float armPartZRot = Mth.sin(renderState.ageInTicks * animationSpeed * 7 + (float) i * 0.45F) *
                            ((float) (i + 8) / 8.0F) * animationSpeed;
                    this.rightArmParts[i].zRot = armPartZRot;
                    this.leftArmParts[i].zRot = -armPartZRot;
                }
            } else {
                for (int i = 0; i < ENDERMAN_ARM_LENGTH; i++) {
                    int j = i > ENDERMAN_ARM_LENGTH / 2 ? ENDERMAN_ARM_LENGTH - i : i;
                    float armPartZRot = Mth.sin(renderState.ageInTicks * animationSpeed * 5 + (float) j * 0.45F) *
                            ((float) (j + 8) / 8.0F) * animationSpeed;
                    this.rightArmParts[i].zRot = this.leftArmParts[i].zRot = i != j ? -armPartZRot : armPartZRot;
                }
            }
        } else {
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            for (int i = 0; i < ENDERMAN_ARM_LENGTH; i++) {
                this.rightArmParts[i].zRot = this.leftArmParts[i].zRot = 0.0F;
            }
        }
    }
}

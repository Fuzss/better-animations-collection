package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.monster.spider.SpiderModel;

public class SpiderKneesModel extends SpiderModel {

    public SpiderKneesModel(ModelPart modelPart) {
        super(modelPart);
    }

    public static LayerDefinition createAnimatedSpiderBodyLayer() {
        return SpiderModel.createSpiderBodyLayer().apply((MeshDefinition meshDefinition) -> {
            modifyMesh(meshDefinition.getRoot());
            return meshDefinition;
        });
    }

    private static void modifyMesh(PartDefinition partDefinition) {
        CubeListBuilder rightLegBuilder = CubeListBuilder.create()
                .texOffs(18, 0)
                .addBox(-7.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F);
        CubeListBuilder leftLegBuilder = CubeListBuilder.create()
                .texOffs(18, 0)
                .mirror()
                .addBox(-1.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F);
        CubeListBuilder lowerRightLegBuilder = CubeListBuilder.create()
                .texOffs(24, 0)
                .mirror()
                .addBox(-9.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F));
        CubeListBuilder lowerLeftLegBuilder = CubeListBuilder.create()
                .texOffs(24, 0)
                .mirror()
                .addBox(-1.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F));
        partDefinition.addOrReplaceChild("right_hind_leg",
                        rightLegBuilder,
                        PartPose.offsetAndRotation(-4.0F,
                                15.0F,
                                2.0F,
                                0.0F,
                                (float) (Math.PI / 4),
                                (float) (-Math.PI / 4) + 0.675F))
                .addOrReplaceChild("lower_right_hind_leg",
                        lowerRightLegBuilder,
                        PartPose.offsetAndRotation(-7.0F, 0.5F, 0.0F, 0.0F, 0.0F, -1.05F));
        partDefinition.addOrReplaceChild("left_hind_leg",
                        leftLegBuilder,
                        PartPose.offsetAndRotation(4.0F,
                                15.0F,
                                2.0F,
                                0.0F,
                                (float) (-Math.PI / 4),
                                (float) (Math.PI / 4) - 0.675F))
                .addOrReplaceChild("lower_left_hind_leg",
                        lowerLeftLegBuilder,
                        PartPose.offsetAndRotation(7.0F, 0.5F, 0.0F, 0.0F, 0.0F, 1.05F));
        partDefinition.addOrReplaceChild("right_middle_hind_leg",
                        rightLegBuilder,
                        PartPose.offsetAndRotation(-4.0F, 15.0F, 1.0F, 0.0F, (float) (Math.PI / 8), -0.58119464F + 0.675F))
                .addOrReplaceChild("lower_right_middle_hind_leg",
                        lowerRightLegBuilder,
                        PartPose.offsetAndRotation(-7.0F, 0.5F, 0.0F, 0.0F, 0.0F, -1.05F));
        partDefinition.addOrReplaceChild("left_middle_hind_leg",
                        leftLegBuilder,
                        PartPose.offsetAndRotation(4.0F, 15.0F, 1.0F, 0.0F, (float) (-Math.PI / 8), 0.58119464F - 0.675F))
                .addOrReplaceChild("lower_left_middle_hind_leg",
                        lowerLeftLegBuilder,
                        PartPose.offsetAndRotation(7.0F, 0.5F, 0.0F, 0.0F, 0.0F, 1.05F));
        partDefinition.addOrReplaceChild("right_middle_front_leg",
                        rightLegBuilder,
                        PartPose.offsetAndRotation(-4.0F, 15.0F, 0.0F, 0.0F, (float) (-Math.PI / 8), -0.58119464F + 0.675F))
                .addOrReplaceChild("lower_right_middle_front_leg",
                        lowerRightLegBuilder,
                        PartPose.offsetAndRotation(-7.0F, 0.5F, 0.0F, 0.0F, 0.0F, -1.05F));
        partDefinition.addOrReplaceChild("left_middle_front_leg",
                        leftLegBuilder,
                        PartPose.offsetAndRotation(4.0F, 15.0F, 0.0F, 0.0F, (float) (Math.PI / 8), 0.58119464F - 0.675F))
                .addOrReplaceChild("lower_left_middle_front_leg",
                        lowerLeftLegBuilder,
                        PartPose.offsetAndRotation(7.0F, 0.5F, 0.0F, 0.0F, 0.0F, 1.05F));
        partDefinition.addOrReplaceChild("right_front_leg",
                        rightLegBuilder,
                        PartPose.offsetAndRotation(-4.0F,
                                15.0F,
                                -1.0F,
                                0.0F,
                                (float) (-Math.PI / 4),
                                (float) (-Math.PI / 4) + 0.675F))
                .addOrReplaceChild("lower_right_front_leg",
                        lowerRightLegBuilder,
                        PartPose.offsetAndRotation(-7.0F, 0.5F, 0.0F, 0.0F, 0.0F, -1.05F));
        partDefinition.addOrReplaceChild("left_front_leg",
                        leftLegBuilder,
                        PartPose.offsetAndRotation(4.0F,
                                15.0F,
                                -1.0F,
                                0.0F,
                                (float) (Math.PI / 4),
                                (float) (Math.PI / 4) - 0.675F))
                .addOrReplaceChild("lower_left_front_leg",
                        lowerLeftLegBuilder,
                        PartPose.offsetAndRotation(7.0F, 0.5F, 0.0F, 0.0F, 0.0F, 1.05F));
    }
}

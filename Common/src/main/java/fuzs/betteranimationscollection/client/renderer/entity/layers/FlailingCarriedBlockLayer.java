package fuzs.betteranimationscollection.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fuzs.betteranimationscollection.client.element.FlailingEndermanElement;
import fuzs.betteranimationscollection.client.model.FlailingEndermanModel;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CarriedBlockLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.state.BlockState;

public class FlailingCarriedBlockLayer extends CarriedBlockLayer {
    private final BlockRenderDispatcher blockRenderer;

    public FlailingCarriedBlockLayer(RenderLayerParent<EnderMan, EndermanModel<EnderMan>> renderLayerParent, BlockRenderDispatcher blockRenderDispatcher) {
        super(renderLayerParent, blockRenderDispatcher);
        this.blockRenderer = blockRenderDispatcher;
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, EnderMan livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        BlockState blockstate = livingEntity.getCarriedBlock();
        if (blockstate != null) {
            matrixStack.pushPose();
            double flailAmount = 0.0;
            if (FlailingEndermanElement.whileCarrying && livingEntity.isCreepy()) {
                final float animationSpeed = FlailingEndermanElement.animationSpeed * 0.025F;
                final double armPartLength = 2.0;
                double totalAngle = 0.0;
                for (int i = 0; i < FlailingEndermanModel.ENDERMAN_ARM_LENGTH; i++) {
                    int j = i > FlailingEndermanModel.ENDERMAN_ARM_LENGTH / 2 ? FlailingEndermanModel.ENDERMAN_ARM_LENGTH - i : i;
                    float armPartZRot = Mth.sin(ageInTicks * animationSpeed * 5 + (float) j * 0.45F) * ((float) (j + 8) / 8.0F) * animationSpeed;
                    totalAngle += i != j ? -armPartZRot : armPartZRot;
                    flailAmount += Math.tan(totalAngle) * armPartLength;
                }
            }
            // below just copied from super
            matrixStack.translate(-flailAmount * 0.0575, 0.6875D, -0.75D);
            matrixStack.mulPose(Axis.XP.rotationDegrees(20.0F));
            matrixStack.mulPose(Axis.YP.rotationDegrees(45.0F));
            matrixStack.translate(0.25D, 0.1875D, 0.25D);
            matrixStack.scale(-0.5F, -0.5F, 0.5F);
            matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            this.blockRenderer.renderSingleBlock(blockstate, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
            matrixStack.popPose();
        }
    }
}

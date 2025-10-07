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
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class FlailingCarriedBlockLayer extends CarriedBlockLayer {
    private final BlockRenderDispatcher blockRenderer;

    public FlailingCarriedBlockLayer(RenderLayerParent<EndermanRenderState, EndermanModel<EndermanRenderState>> renderLayerParent, BlockRenderDispatcher blockRenderDispatcher) {
        super(renderLayerParent, blockRenderDispatcher);
        this.blockRenderer = blockRenderDispatcher;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EndermanRenderState renderState, float yRot, float xRot) {
        BlockState blockstate = renderState.carriedBlock;
        if (blockstate != null) {
            poseStack.pushPose();
            double flailAmount = 0.0;
            if (FlailingEndermanElement.whileCarrying && renderState.isCreepy) {
                final float animationSpeed = FlailingEndermanElement.animationSpeed * 0.025F;
                final double armPartLength = 2.0;
                double totalAngle = 0.0;
                for (int i = 0; i < FlailingEndermanModel.ENDERMAN_ARM_LENGTH; i++) {
                    int j = i > FlailingEndermanModel.ENDERMAN_ARM_LENGTH / 2 ?
                            FlailingEndermanModel.ENDERMAN_ARM_LENGTH - i : i;
                    float armPartZRot = Mth.sin(renderState.ageInTicks * animationSpeed * 5 + (float) j * 0.45F) *
                            ((float) (j + 8) / 8.0F) * animationSpeed;
                    totalAngle += i != j ? -armPartZRot : armPartZRot;
                    flailAmount += Math.tan(totalAngle) * armPartLength;
                }
            }
            // below just copied from super
            poseStack.translate(-flailAmount * 0.0575F, 0.6875F, -0.75F);
            poseStack.mulPose(Axis.XP.rotationDegrees(20.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
            poseStack.translate(0.25F, 0.1875F, 0.25F);
            poseStack.scale(-0.5F, -0.5F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            this.blockRenderer.renderSingleBlock(blockstate,
                    poseStack,
                    bufferSource,
                    packedLight,
                    OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }
}

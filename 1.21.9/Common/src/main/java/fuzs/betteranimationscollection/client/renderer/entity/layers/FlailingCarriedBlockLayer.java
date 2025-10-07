package fuzs.betteranimationscollection.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.betteranimationscollection.client.element.FlailingEndermanElement;
import fuzs.betteranimationscollection.client.model.FlailingEndermanModel;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CarriedBlockLayer;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class FlailingCarriedBlockLayer extends CarriedBlockLayer {

    public FlailingCarriedBlockLayer(RenderLayerParent<EndermanRenderState, EndermanModel<EndermanRenderState>> renderLayerParent, BlockRenderDispatcher blockRenderDispatcher) {
        super(renderLayerParent, blockRenderDispatcher);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, EndermanRenderState renderState, float yRot, float xRot) {
        BlockState blockState = renderState.carriedBlock;
        if (blockState != null) {
            poseStack.pushPose();
            double flailAmount = this.getFlailAmount(renderState);
            poseStack.translate(-flailAmount * 0.0575F, 0.0F, 0.0F);
            super.submit(poseStack, nodeCollector, packedLight, renderState, yRot, xRot);
            poseStack.popPose();
        }
    }

    private float getFlailAmount(EndermanRenderState renderState) {
        float flailAmount = 0.0F;
        if (FlailingEndermanElement.whileCarrying && renderState.isCreepy) {
            float animationSpeed = FlailingEndermanElement.animationSpeed * 0.025F;
            float totalAngle = 0.0F;
            for (int i = 0; i < FlailingEndermanModel.ENDERMAN_ARM_LENGTH; i++) {
                int j = i > FlailingEndermanModel.ENDERMAN_ARM_LENGTH / 2 ?
                        FlailingEndermanModel.ENDERMAN_ARM_LENGTH - i : i;
                float armPartZRot =
                        Mth.sin(renderState.ageInTicks * animationSpeed * 5 + (float) j * 0.45F) * ((float) (j + 8)
                                / 8.0F) * animationSpeed;
                totalAngle += i != j ? -armPartZRot : armPartZRot;
                flailAmount += (float) (Math.tan(totalAngle) * 2.0F);
            }
        }

        return flailAmount;
    }
}

package fuzs.betteranimationscollection.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fuzs.betteranimationscollection.client.element.PlayfulDoggyElement;
import fuzs.betteranimationscollection.client.model.PlayfulDoggyModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements RenderLayerParent<S, M> {

    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "setupRotations", at = @At("TAIL"))
    protected void setupRotations(S renderState, PoseStack poseStack, float bodyRot, float scale, CallbackInfo callback) {
        // patch this in here, as modded renderers using the wolf model must not necessarily extend WolfRenderer
        if (this.getModel() instanceof PlayfulDoggyModel && renderState instanceof WolfRenderState wolfRenderState) {
            float rollAnimScale = PlayfulDoggyElement.getRollAnimScale(wolfRenderState);
            if (rollAnimScale != 0.0F) {
                poseStack.translate(0.25F * rollAnimScale, 0.23F * rollAnimScale, 0.0F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F * rollAnimScale * PlayfulDoggyElement.MAX_ROLL_ANIM));
            }
        }
    }
}

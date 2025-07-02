package fuzs.betteranimationscollection.client.element;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public abstract class SingletonModelElement<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends ModelElement {
    protected final Class<T> entityClazz;
    private final Class<S> renderStateClazz;
    private final Class<M> modelClazz;

    protected SingletonModelElement(Class<T> entityClazz, Class<S> renderStateClazz, Class<M> modelClazz) {
        this.entityClazz = entityClazz;
        this.renderStateClazz = renderStateClazz;
        this.modelClazz = modelClazz;
    }

    @SuppressWarnings("unchecked")
    public final void applyModelAnimations(LivingEntityRenderer<?, ?, ?> entityRenderer, EntityRendererProvider.Context context) {
        if (entityRenderer.getModel().getClass() == this.modelClazz) {
            this.setAnimatedModel((LivingEntityRenderer<?, S, M>) entityRenderer, context);
            if (entityRenderer instanceof AgeableMobRenderer<?, ?, ?> ageableMobRenderer) {
                if (ageableMobRenderer.model.getClass() == this.modelClazz ||
                        ageableMobRenderer.adultModel.getClass() == this.modelClazz ||
                        ageableMobRenderer.babyModel.getClass() == this.modelClazz) {
                    throw new IllegalStateException("AgeableMobRenderer has invalid models");
                }
            }
            applyLayerAnimation((LivingEntityRenderer<?, S, M>) entityRenderer,
                    context,
                    (RenderLayer<S, M> renderLayer) -> {
                        return this.getAnimatedLayer(renderLayer,
                                (LivingEntityRenderer<?, S, M>) entityRenderer,
                                context);
                    });
        }
    }

    protected abstract void setAnimatedModel(LivingEntityRenderer<?, S, M> entityRenderer, EntityRendererProvider.Context context);

    @Nullable
    protected RenderLayer<S, M> getAnimatedLayer(RenderLayer<S, M> renderLayer, LivingEntityRenderer<?, S, M> entityRenderer, EntityRendererProvider.Context context) {
        return null;
    }

    public final void onExtractRenderState(Entity entity, EntityRenderState renderState, float partialTick) {
        if (this.entityClazz.isInstance(entity) && this.renderStateClazz.isInstance(renderState)) {
            this.extractRenderState((T) entity, (S) renderState, partialTick);
        }
    }

    protected void extractRenderState(T entity, S renderState, float partialTick) {
        // NO-OP
    }
}

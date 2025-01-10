package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.puzzleslib.api.client.util.v1.RenderPropertyKey;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class ModelElement<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> implements ModelLayerFactory {
    protected final Class<T> entityClazz;
    private final Class<S> renderStateClazz;
    private final Class<M> modelClazz;
    private boolean isEnabled = true;
    private boolean markedChanged = true;

    protected ModelElement(Class<T> entityClazz, Class<S> renderStateClazz, Class<M> modelClazz) {
        this.entityClazz = entityClazz;
        this.renderStateClazz = renderStateClazz;
        this.modelClazz = modelClazz;
    }

    protected static <T> RenderPropertyKey<T> key(String path) {
        return new RenderPropertyKey<>(BetterAnimationsCollection.id(path));
    }

    public void setEnabled(boolean isEnabled) {
        if (isEnabled != this.isEnabled) {
            this.isEnabled = isEnabled;
            this.markedChanged = true;
        }
    }

    public boolean markedChanged() {
        return this.markedChanged;
    }

    public abstract String[] getDescriptionComponent();

    @SuppressWarnings("unchecked")
    public final void onApplyModelAnimations(LivingEntityRenderer<?, ?, ?> entityRenderer, EntityRendererProvider.Context context) {
        this.markedChanged = false;
        if (this.isEnabled && entityRenderer.getModel().getClass() == this.modelClazz) {
            this.setAnimatedModel((LivingEntityRenderer<?, S, M>) entityRenderer, context);
            if (entityRenderer instanceof AgeableMobRenderer<?, ?, ?> ageableMobRenderer) {
                if (ageableMobRenderer.model.getClass() == this.modelClazz ||
                        ageableMobRenderer.adultModel.getClass() == this.modelClazz ||
                        ageableMobRenderer.babyModel.getClass() == this.modelClazz) {
                    throw new IllegalStateException("AgeableMobRenderer has invalid models");
                }
            }
            for (int i = 0; i < entityRenderer.layers.size(); i++) {
                RenderLayer<S, M> animatedRenderLayer = this.getAnimatedLayer(((LivingEntityRenderer<?, S, M>) entityRenderer).layers.get(
                        i), (LivingEntityRenderer<?, S, M>) entityRenderer, context);
                if (animatedRenderLayer != null) {
                    ((LivingEntityRenderer<?, S, M>) entityRenderer).layers.set(i, animatedRenderLayer);
                    break;
                }
            }
        }
    }

    protected abstract void setAnimatedModel(LivingEntityRenderer<?, S, M> entityRenderer, EntityRendererProvider.Context context);

    protected static <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void setAnimatedAgeableModel(LivingEntityRenderer<?, S, M> entityRenderer, M adultModel, M childModel) {
        AgeableMobRenderer<?, S, M> ageableRenderer = (AgeableMobRenderer<?, S, M>) entityRenderer;
        ageableRenderer.model = ageableRenderer.adultModel = adultModel;
        ageableRenderer.babyModel = childModel;
    }

    @Nullable
    protected RenderLayer<S, M> getAnimatedLayer(RenderLayer<S, M> renderLayer, LivingEntityRenderer<?, S, M> entityRenderer, EntityRendererProvider.Context context) {
        return null;
    }

    public abstract void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context);

    public final void onExtractRenderState(Entity entity, EntityRenderState renderState, float partialTick) {
        if (this.entityClazz.isInstance(entity) && this.renderStateClazz.isInstance(renderState)) {
            this.extractRenderState((T) entity, (S) renderState, partialTick);
        }
    }

    protected void extractRenderState(T entity, S renderState, float partialTick) {
        // NO-OP
    }

    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        // NO-OP
    }

    @Override
    public String modId() {
        return BetterAnimationsCollection.MOD_ID;
    }
}

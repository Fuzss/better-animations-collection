package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import fuzs.puzzleslib.api.client.renderer.v1.RenderPropertyKey;
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
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ModelElement implements ModelLayerFactory {
    private boolean isEnabled = true;
    private boolean markedChanged = true;

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

    public final void onApplyModelAnimations(LivingEntityRenderer<?, ?, ?> entityRenderer, EntityRendererProvider.Context context) {
        this.markedChanged = false;
        if (this.isEnabled) {
            this.applyModelAnimations(entityRenderer, context);
        }
    }

    protected abstract void applyModelAnimations(LivingEntityRenderer<?, ?, ?> entityRenderer, EntityRendererProvider.Context context);

    protected static <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void applyLayerAnimation(LivingEntityRenderer<?, S, M> entityRenderer, EntityRendererProvider.Context context, LayerMutator<S, M> mutator) {
        for (int i = 0; i < entityRenderer.layers.size(); i++) {
            RenderLayer<S, M> animatedRenderLayer = mutator.apply(entityRenderer.layers.get(i));
            if (animatedRenderLayer != null) {
                entityRenderer.layers.set(i, animatedRenderLayer);
                break;
            }
        }
    }

    protected static <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void setAnimatedAgeableModel(LivingEntityRenderer<?, S, M> entityRenderer, M adultModel, M childModel) {
        AgeableMobRenderer<?, S, M> ageableRenderer = (AgeableMobRenderer<?, S, M>) entityRenderer;
        ageableRenderer.model = ageableRenderer.adultModel = adultModel;
        ageableRenderer.babyModel = childModel;
    }

    public abstract void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context);

    public void onExtractRenderState(Entity entity, EntityRenderState renderState, float partialTick) {
        // NO-OP
    }

    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        // NO-OP
    }

    @Override
    public String modId() {
        return BetterAnimationsCollection.MOD_ID;
    }

    @FunctionalInterface
    protected interface LayerMutator<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends Function<RenderLayer<S, M>, @Nullable RenderLayer<S, M>> {

    }
}

package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.puzzleslib.common.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.common.api.client.init.v1.ModelLayerFactory;
import fuzs.puzzleslib.common.api.config.v3.ValueCallback;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public abstract class ModelElement implements ModelLayerFactory {
    private boolean isEnabled = true;
    private boolean markedChanged;

    protected static <T> ContextKey<T> key(String path) {
        return new ContextKey<>(BetterAnimationsCollection.id(path));
    }

    public void setEnabled(boolean isEnabled) {
        if (isEnabled != this.isEnabled) {
            this.isEnabled = isEnabled;
            this.markChanged();
        }
    }

    protected void markChanged() {
        this.markedChanged = true;
    }

    public boolean markedChanged() {
        if (this.markedChanged) {
            this.markedChanged = false;
            return true;
        } else {
            return false;
        }
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

    protected static <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void setAnimatedAgeableModel(AgeableMobRenderer<?, ?, ?> ageableRenderer, M adultModel) {
        ((AgeableMobRenderer<?, S, M>) ageableRenderer).adultModel = adultModel;
    }

    public abstract void onRegisterLayerDefinitions(LayerDefinitionsContext context);

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

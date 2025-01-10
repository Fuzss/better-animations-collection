package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.puzzleslib.api.client.util.v1.RenderPropertyKey;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class AbstractModelElement implements ModelLayerFactory {
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

    protected void applyModelAnimations(LivingEntityRenderer<?, ?, ?> entityRenderer, EntityRendererProvider.Context context) {
        // NO-OP
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
}

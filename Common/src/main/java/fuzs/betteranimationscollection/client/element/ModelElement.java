package fuzs.betteranimationscollection.client.element;

import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class ModelElement {
    private boolean enabled = true;
    private boolean dirty = true;

    public void setEnabled(boolean enabled) {
        if (enabled != this.enabled) {
            this.enabled = enabled;
            this.dirty = true;
        }
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public abstract String[] modelDescription();

    public final void registerAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        this.dirty = false;
        if (this.enabled) {
            this.onRegisterAnimatedModels(context, bakery);
        }
    }

    abstract void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery);

    public abstract void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context);

    public void setupModelConfig(ForgeConfigSpec.Builder builder, ValueCallback callback) {

    }

    @FunctionalInterface
    public interface AnimatedModelsContext {

        default <T extends LivingEntity, M extends EntityModel<T>> void registerAnimatedModel(Class<? super M> vanillaModelClazz, Supplier<? extends M> animatedModel) {
            this.registerAnimatedModel(vanillaModelClazz, animatedModel, (RenderLayerParent<T, M> renderLayerParent, RenderLayer<T, M> renderLayer) -> Optional.empty());
        }

        <T extends LivingEntity, M extends EntityModel<T>> void registerAnimatedModel(Class<? super M> vanillaModelClazz, Supplier<? extends M> animatedModel, LayerTransformer<T, M> layerTransformer);
    }

    @FunctionalInterface
    public interface LayerTransformer<T extends LivingEntity, M extends EntityModel<T>> {

        Optional<RenderLayer<T, M>> apply(RenderLayerParent<T, M> renderLayerParent, RenderLayer<T, M> renderLayer);
    }

    public record AnimatedModelData<T extends LivingEntity, M extends EntityModel<T>>(Class<? super M> vanillaModelClazz, Supplier<? extends M> animatedModel, LayerTransformer<T, M> layerTransformer) {

    }

    public record EntityModelBakery(Supplier<EntityModelSet> entityModelSet) {

        public EntityModelSet get() {
            return this.entityModelSet.get();
        }

        public ModelPart bakeLayer(ModelLayerLocation modelLayerLocation) {
            return this.entityModelSet.get().bakeLayer(modelLayerLocation);
        }

        public static EntityModelBakery of(Supplier<EntityModelSet> entityModelSet) {
            return new EntityModelBakery(entityModelSet);
        }
    }
}

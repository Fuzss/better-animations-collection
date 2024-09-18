package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.JigglySlimeModel;
import fuzs.betteranimationscollection.mixin.client.accessor.SlimeOuterLayerAccessor;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class JigglySlimeElement extends ModelElement {
    public static int animationSpeed;

    private final ModelLayerLocation animatedSlime;
    private final ModelLayerLocation animatedSlimeOuter;

    public JigglySlimeElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedSlime = factory.apply("animated_slime", "main");
        this.animatedSlimeOuter = factory.apply("animated_slime", "outer");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"A pleasing visual change; this makes the insides of slimes flow around like liquid.",
                "They splish-splosh about even more when they jump. The eyes, the mouth, and the core itself all move independently."};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<LivingEntity, SlimeModel<LivingEntity>>registerAnimatedModel(SlimeModel.class, () -> new JigglySlimeModel<>(bakery.bakeLayer(this.animatedSlime)), (RenderLayerParent<LivingEntity, SlimeModel<LivingEntity>> renderLayerParent, RenderLayer<LivingEntity, SlimeModel<LivingEntity>> renderLayer) -> {
            if (renderLayer instanceof SlimeOuterLayer) {
                ((SlimeOuterLayerAccessor<?>) renderLayer).setModel(new JigglySlimeModel<>(bakery.bakeLayer(this.animatedSlimeOuter)));
            }

            return Optional.empty();
        });
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedSlime, SlimeModel::createInnerBodyLayer);
        context.accept(this.animatedSlimeOuter, SlimeModel::createOuterBodyLayer);
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Animation swing speed of inner slime parts.").defineInRange("animation_speed", 5, 1, 20), v -> animationSpeed = v);
    }
}

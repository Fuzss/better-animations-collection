package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.OinkyPigModel;
import fuzs.betteranimationscollection.mixin.client.accessor.SaddleLayerAccessor;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class OinkyPigElement extends SoundDetectionElement {
    public static boolean floatyEars;
    public static int earAnimationSpeed;

    private final ModelLayerLocation animatedPig;
    private final ModelLayerLocation animatedPigSaddle;

    public OinkyPigElement(BiFunction<String, String, ModelLayerLocation> factory) {
        super(Pig.class, SoundEvents.PIG_AMBIENT);
        this.animatedPig = factory.apply("animated_pig", "main");
        this.animatedPigSaddle = factory.apply("animated_pig", "saddle");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"This makes the udders on cows wobble around when they walk.",
                "Also makes their udders have nipples."};
    }

    @SuppressWarnings("unchecked")
    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<LivingEntity, PigModel<LivingEntity>>registerAnimatedModel(PigModel.class, () -> new OinkyPigModel<>(bakery.bakeLayer(this.animatedPig)), (RenderLayerParent<LivingEntity, PigModel<LivingEntity>> renderLayerParent, RenderLayer<LivingEntity, PigModel<LivingEntity>> renderLayer) -> {
            if (renderLayer instanceof SaddleLayer) {
                // we could also replace the whole layer, but we don't know which texture location has been used, so better this way
                ((SaddleLayerAccessor<Pig, PigModel<Pig>>) renderLayer).setModel(new OinkyPigModel<>(bakery.bakeLayer(this.animatedPigSaddle)));
            }
            return Optional.empty();
        });
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedPig, () -> OinkyPigModel.createAnimatedBodyLayer(CubeDeformation.NONE));
        context.accept(this.animatedPigSaddle, () -> OinkyPigModel.createAnimatedBodyLayer(new CubeDeformation(0.5F)));
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        super.setupModelConfig(builder, callback);
        callback.accept(builder.comment("Fancy ears for pigs, just like piglins have them.").define("floaty_ears", true), v -> floatyEars = v);
        callback.accept(builder.comment("Animation swing speed for ear floatiness.").defineInRange("ear_animation_speed", 10, 1, 20), v -> earAnimationSpeed = v);
    }
}

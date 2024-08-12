package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.GhastTentaclesModel;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.GhastModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class GhastTentaclesElement extends ModelElement {
    public static int maxTentaclesLength;
    public static int animationSpeed;

    private final ModelLayerLocation animatedGhast;

    public GhastTentaclesElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedGhast = factory.apply("animated_ghast", "main");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"Divides ghast tentacles into parts and makes them wiggle realistically, like those tentacle monsters you always see at the movies.",
                "Makes them a little more scary, but ultimately nicer to look at."};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<LivingEntity, GhastTentaclesModel<LivingEntity>>registerAnimatedModel(GhastModel.class, () -> new GhastTentaclesModel<>(bakery.bakeLayer(this.animatedGhast)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedGhast, GhastTentaclesModel::createAnimatedBodyLayer);
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Define the max length of tentacles.").defineInRange("max_tentacles_length", GhastTentaclesModel.GHAST_MAX_TENTACLES_LENGTH, 2, GhastTentaclesModel.GHAST_MAX_TENTACLES_LENGTH), v -> maxTentaclesLength = v);
        callback.accept(builder.comment("Animation swing speed of tentacles.").defineInRange("animation_speed", 5, 1, 20), v -> animationSpeed = v);
    }
}

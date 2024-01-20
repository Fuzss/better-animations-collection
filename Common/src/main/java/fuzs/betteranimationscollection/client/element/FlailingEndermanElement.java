package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.FlailingEndermanModel;
import fuzs.betteranimationscollection.client.renderer.entity.layers.FlailingCarriedBlockLayer;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CarriedBlockLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.monster.EnderMan;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class FlailingEndermanElement extends ModelElement {
    public static int animationSpeed;
    public static boolean whileCarrying;

    private final ModelLayerLocation animatedEnderman;

    public FlailingEndermanElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedEnderman = factory.apply("animated_enderman", "main");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"If an enderman is angry it will wave its arms around wildly while chasing its target.",
                "Suits their twisted nature very well."};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<EnderMan, EndermanModel<EnderMan>>registerAnimatedModel(EndermanModel.class, () -> new FlailingEndermanModel<>(bakery.bakeLayer(this.animatedEnderman)), (RenderLayerParent<EnderMan, EndermanModel<EnderMan>> renderLayerParent, RenderLayer<EnderMan, EndermanModel<EnderMan>> renderLayer) -> {
            if (renderLayer instanceof CarriedBlockLayer) {
                return Optional.of(new FlailingCarriedBlockLayer(renderLayerParent, Minecraft.getInstance().getBlockRenderer()));
            }
            return Optional.empty();
        });
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedEnderman, FlailingEndermanModel::createAnimatedBodyLayer);
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Animation swing speed for arms.").defineInRange("animation_speed", 5, 1, 20), v -> animationSpeed = v);
        callback.accept(builder.comment("Flail arms while carrying a block.").define("fail_while_carrying", true), v -> whileCarrying = v);
    }
}

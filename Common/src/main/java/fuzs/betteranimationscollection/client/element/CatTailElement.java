package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.CatTailModel;
import fuzs.betteranimationscollection.client.model.OcelotTailModel;
import fuzs.betteranimationscollection.mixin.client.accessor.CatCollarLayerAccessor;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.animal.Cat;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class CatTailElement extends ModelElement {
    public static int tailLength;
    public static int animationSpeed;

    private final ModelLayerLocation animatedCat;
    private final ModelLayerLocation animatedCatCollar;

    public CatTailElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedCat = factory.apply("animated_cat", "main");
        this.animatedCatCollar = factory.apply("animated_cat", "collar");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"Takes away the stick tails of the current cats and gives them something nicer instead.",
                "Fully animated flowing tails that move while they stand or run, and even curl around their bodies when they sit."};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<Cat, CatModel<Cat>>registerAnimatedModel(CatModel.class, () -> new CatTailModel<>(bakery.bakeLayer(this.animatedCat)), (RenderLayerParent<Cat, CatModel<Cat>> renderLayerParent, RenderLayer<Cat, CatModel<Cat>> renderLayer) -> {
            if (renderLayer instanceof CatCollarLayer) {
                ((CatCollarLayerAccessor) renderLayer).setCatModel(new CatTailModel<>(bakery.bakeLayer(this.animatedCatCollar)));
            }
            return Optional.empty();
        });
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedCat, () -> OcelotTailModel.createAnimatedBodyMesh(CubeDeformation.NONE));
        context.accept(this.animatedCatCollar, () -> OcelotTailModel.createAnimatedBodyMesh(new CubeDeformation(0.01F)));
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Define tail length.").defineInRange("tail_length", OcelotTailModel.OCELOT_TAIL_LENGTH, 1, OcelotTailModel.OCELOT_TAIL_LENGTH), v -> tailLength = v);
        callback.accept(builder.comment("Animation swing speed for tail.").defineInRange("animation_speed", 7, 1, 20), v -> animationSpeed = v);
    }
}

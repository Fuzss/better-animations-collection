package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.MagmaCubeBurgerModel;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.monster.Slime;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class MagmaCubeBurgerElement extends ModelElement {
    private final ModelLayerLocation animatedMagmaCube;

    public MagmaCubeBurgerElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedMagmaCube = factory.apply("animated_magma_cube", "main");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"Adds a custom death animation to magma cubes, which causes their bodies to form into a pile of steamy, delicious hamburger patties when they die.",
                "Unfortunately, you can't eat them because they're way too hot."};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<Slime, LavaSlimeModel<Slime>>registerAnimatedModel(LavaSlimeModel.class, () -> new MagmaCubeBurgerModel<>(bakery.bakeLayer(this.animatedMagmaCube)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedMagmaCube, LavaSlimeModel::createBodyLayer);
    }
}

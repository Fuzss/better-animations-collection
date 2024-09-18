package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.SpiderKneesModel;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class SpiderKneesElement extends ModelElement {
    private final ModelLayerLocation animatedSpider;

    public SpiderKneesElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedSpider = factory.apply("animated_spider", "main");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"A truly stunning visual addition. Spiders now finally have the knees they've always dreamed of."};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<LivingEntity, SpiderModel<LivingEntity>>registerAnimatedModel(SpiderModel.class, () -> new SpiderKneesModel<>(bakery.bakeLayer(this.animatedSpider)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedSpider, SpiderKneesModel::createAnimatedSpiderBodyLayer);
    }
}

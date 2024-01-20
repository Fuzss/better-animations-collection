package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.SnowGolemStickModel;
import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.SnowGolem;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class SnowGolemStickElement extends SoundDetectionElement {
    private final ModelLayerLocation animatedSnowGolem;

    public SnowGolemStickElement(BiFunction<String, String, ModelLayerLocation> factory) {
        super(SnowGolem.class, SoundEvents.SNOW_GOLEM_SHOOT);
        this.animatedSnowGolem = factory.apply("animated_snow_golem", "main");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"This makes a snowman's arm swing when it throws a snowball."};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<LivingEntity, SnowGolemModel<LivingEntity>>registerAnimatedModel(SnowGolemModel.class, () -> new SnowGolemStickModel<>(bakery.bakeLayer(this.animatedSnowGolem)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedSnowGolem, SnowGolemModel::createBodyLayer);
    }
}

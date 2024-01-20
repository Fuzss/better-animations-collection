package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.SquidTentaclesModel;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class SquidTentaclesElement extends ModelElement {
    public static int tentaclesLength;

    private final ModelLayerLocation animatedSquid;

    public SquidTentaclesElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedSquid = factory.apply("animated_squid", "main");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"Gives a jellyfish-like effect to the swimming animation of squids; generally just makes their tentacles flow more while moving."};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<LivingEntity, SquidModel<LivingEntity>>registerAnimatedModel(SquidModel.class, () -> new SquidTentaclesModel<>(bakery.bakeLayer(this.animatedSquid)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedSquid, SquidTentaclesModel::createAnimatedBodyLayer);
    }

    @Override
    public void setupModelConfig(ForgeConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Define length for squid tentacles.").defineInRange("tentacles_length", SquidTentaclesModel.SQUID_TENTACLES_LENGTH, 1, SquidTentaclesModel.SQUID_TENTACLES_LENGTH), v -> tentaclesLength = v);
    }
}

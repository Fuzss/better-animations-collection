package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.OcelotTailModel;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class OcelotTailElement extends ModelElement {
    public static int tailLength;
    public static int animationSpeed;

    private final ModelLayerLocation animatedOcelot;

    public OcelotTailElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedOcelot = factory.apply("animated_ocelot", "main");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"Takes away the stick tails of the current ocelots and gives them something nicer instead.",
                "Fully animated flowing tails that move while they stand or run."};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<LivingEntity, OcelotTailModel<LivingEntity>>registerAnimatedModel(OcelotModel.class, () -> new OcelotTailModel<>(bakery.bakeLayer(this.animatedOcelot)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedOcelot, () -> OcelotTailModel.createAnimatedBodyMesh(CubeDeformation.NONE));
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Define tail length.").defineInRange("tail_length", OcelotTailModel.OCELOT_TAIL_LENGTH, 1, OcelotTailModel.OCELOT_TAIL_LENGTH), v -> tailLength = v);
        callback.accept(builder.comment("Animation swing speed for tail.").defineInRange("animation_speed", 7, 1, 20), v -> animationSpeed = v);
    }
}

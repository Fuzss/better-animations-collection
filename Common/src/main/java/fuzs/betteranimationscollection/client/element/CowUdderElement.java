package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.CowUdderModel;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class CowUdderElement extends ModelElement {
    public static int animationSpeed;
    public static boolean showNipples;
    public static boolean calfUtter;

    private final ModelLayerLocation animatedCow;

    public CowUdderElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedCow = factory.apply("animated_cow", "main");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"This makes the udders on cows wobble around when they walk.",
                "Also makes their udders have nipples."};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<LivingEntity, CowUdderModel<LivingEntity>>registerAnimatedModel(CowModel.class, () -> new CowUdderModel<>(bakery.bakeLayer(this.animatedCow)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedCow, CowUdderModel::createAnimatedBodyLayer);
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Animation swing speed of utter when the cow is walking.").defineInRange("animation_speed", 5, 1, 20), v -> animationSpeed = v);
        callback.accept(builder.comment("Render tiny nipples on a cow's utter.").define("show_nipples", true), v -> showNipples = v);
        callback.accept(builder.comment("Should calves show an utter.").define("calf_utter", false), v -> calfUtter = v);
    }
}

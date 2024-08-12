package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.WobblyCreeperModel;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CreeperPowerLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.monster.Creeper;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class WobblyCreeperElement extends ModelElement {
    public static WobbleDirection wobbleDirection;

    private final ModelLayerLocation animatedCreeper;
    private final ModelLayerLocation animatedCreeperArmor;

    public WobblyCreeperElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedCreeper = factory.apply("animated_creeper", "main");
        this.animatedCreeperArmor = factory.apply("animated_creeper", "armor");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"Remember that one very popular \"TNT\" music video by CaptainSparklez with the really cute wobbly Creeper?",
                "Well, it's in the game now. The full thing, the real deal, exactly like the video."};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<Creeper, CreeperModel<Creeper>>registerAnimatedModel(CreeperModel.class, () -> new WobblyCreeperModel<>(bakery.bakeLayer(this.animatedCreeper), false), (RenderLayerParent<Creeper, CreeperModel<Creeper>> renderLayerParent, RenderLayer<Creeper, CreeperModel<Creeper>> renderLayer) -> {
            if (renderLayer instanceof CreeperPowerLayer) {
                return Optional.of(new CreeperPowerLayer(renderLayerParent, bakery.get()) {
                    private final WobblyCreeperModel<Creeper> model = new WobblyCreeperModel<>(bakery.bakeLayer(WobblyCreeperElement.this.animatedCreeperArmor), true);

                    @Override
                    protected EntityModel<Creeper> model() {
                        return this.model;
                    }
                });
            }
            return Optional.empty();
        });
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedCreeper, () -> WobblyCreeperModel.createAnimatedBodyLayer(CubeDeformation.NONE, false));
        context.accept(this.animatedCreeperArmor, () -> WobblyCreeperModel.createAnimatedBodyLayer(new CubeDeformation(2.0F), true));
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Different directional behaviour modes for the walking animation.").defineEnum("wobble_direction", WobbleDirection.SIDE), v -> wobbleDirection = v);
    }

    public enum WobbleDirection {
        SIDE, FRONT, CIRCLE, RANDOM
    }
}

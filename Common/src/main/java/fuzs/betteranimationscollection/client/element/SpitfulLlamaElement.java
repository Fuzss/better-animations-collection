package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.SpitfulLlamaModel;
import fuzs.betteranimationscollection.mixin.client.accessor.LlamaDecorLayerAccessor;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.horse.Llama;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class SpitfulLlamaElement extends SoundDetectionElement {
    private final ModelLayerLocation animatedLlama;
    private final ModelLayerLocation animatedLlamaDecor;

    public SpitfulLlamaElement(BiFunction<String, String, ModelLayerLocation> factory) {
        super(Llama.class, SoundEvents.LLAMA_SPIT);
        this.animatedLlama = factory.apply("animated_llama", "main");
        this.animatedLlamaDecor = factory.apply("animated_llama", "decor");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"This one makes llamas open their mouth when spitting. How have they been doing that before?!"};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<Llama, LlamaModel<Llama>>registerAnimatedModel(LlamaModel.class, () -> new SpitfulLlamaModel<>(bakery.bakeLayer(this.animatedLlama)), (RenderLayerParent<Llama, LlamaModel<Llama>> renderLayerParent, RenderLayer<Llama, LlamaModel<Llama>> renderLayer) -> {
            if (renderLayer instanceof LlamaDecorLayer) {
                ((LlamaDecorLayerAccessor) renderLayer).setModel(new SpitfulLlamaModel<>(bakery.bakeLayer(this.animatedLlamaDecor)));
            }
            return Optional.empty();
        });
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedLlama, () -> SpitfulLlamaModel.createAnimatedBodyLayer(CubeDeformation.NONE));
        context.accept(this.animatedLlamaDecor, () -> SpitfulLlamaModel.createAnimatedBodyLayer(new CubeDeformation(0.5F)));
    }
}

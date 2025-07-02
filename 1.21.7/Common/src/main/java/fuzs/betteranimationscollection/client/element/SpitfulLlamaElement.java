package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.SpitfulLlamaModel;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LlamaRenderState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.horse.Llama;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SpitfulLlamaElement extends SoundBasedElement<Llama, LlamaRenderState, LlamaModel> {
    private final ModelLayerLocation animatedLlama;
    private final ModelLayerLocation animatedLlamaDecor;
    private final ModelLayerLocation animatedLlamaBaby;
    private final ModelLayerLocation animatedLlamaBabyDecor;

    public SpitfulLlamaElement() {
        super(Llama.class, LlamaRenderState.class, LlamaModel.class, SoundEvents.LLAMA_SPIT);
        this.animatedLlama = this.registerModelLayer("animated_llama");
        this.animatedLlamaDecor = this.registerModelLayer("animated_llama", "decor");
        this.animatedLlamaBaby = this.registerModelLayer("animated_llama_baby");
        this.animatedLlamaBabyDecor = this.registerModelLayer("animated_llama_baby", "decor");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "This one makes llamas open their mouth when spitting. How have they been doing that before?!"
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, LlamaRenderState, LlamaModel> entityRenderer, EntityRendererProvider.Context context) {
        setAnimatedAgeableModel(entityRenderer,
                new SpitfulLlamaModel(context.bakeLayer(this.animatedLlama)),
                new SpitfulLlamaModel(context.bakeLayer(this.animatedLlamaBaby)));
    }

    @Override
    protected @Nullable RenderLayer<LlamaRenderState, LlamaModel> getAnimatedLayer(RenderLayer<LlamaRenderState, LlamaModel> renderLayer, LivingEntityRenderer<?, LlamaRenderState, LlamaModel> entityRenderer, EntityRendererProvider.Context context) {
        if (renderLayer instanceof LlamaDecorLayer llamaDecorLayer) {
            llamaDecorLayer.adultModel = new SpitfulLlamaModel(context.bakeLayer(this.animatedLlamaDecor));
            llamaDecorLayer.babyModel = new SpitfulLlamaModel(context.bakeLayer(this.animatedLlamaBabyDecor));
            return renderLayer;
        } else {
            return super.getAnimatedLayer(renderLayer, entityRenderer, context);
        }
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedLlama, () -> SpitfulLlamaModel.createAnimatedBodyLayer(CubeDeformation.NONE));
        context.accept(this.animatedLlamaDecor,
                () -> SpitfulLlamaModel.createAnimatedBodyLayer(new CubeDeformation(0.5F)));
        context.accept(this.animatedLlamaBaby,
                () -> SpitfulLlamaModel.createAnimatedBodyLayer(CubeDeformation.NONE)
                        .apply(LlamaModel.BABY_TRANSFORMER));
        context.accept(this.animatedLlamaBabyDecor,
                () -> SpitfulLlamaModel.createAnimatedBodyLayer(new CubeDeformation(0.5F))
                        .apply(LlamaModel.BABY_TRANSFORMER));
    }
}

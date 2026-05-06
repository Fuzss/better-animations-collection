package fuzs.betteranimationscollection.common.client.element;

import fuzs.betteranimationscollection.common.client.model.SpitfulLlamaModel;
import fuzs.puzzleslib.common.api.client.core.v1.context.LayerDefinitionsContext;
import net.minecraft.client.model.animal.llama.LlamaModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LlamaRenderState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.equine.Llama;
import org.jspecify.annotations.Nullable;

public class SpitfulLlamaElement extends SoundBasedElement<Llama, LlamaRenderState, LlamaModel> {
    private final ModelLayerLocation animatedLlama;
    private final ModelLayerLocation animatedLlamaDecor;

    public SpitfulLlamaElement() {
        super(Llama.class, LlamaRenderState.class, LlamaModel.class, SoundEvents.LLAMA_SPIT);
        this.animatedLlama = this.registerModelLayer("animated_llama");
        this.animatedLlamaDecor = this.registerModelLayer("animated_llama", "decor");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "This one makes llamas open their mouth when spitting. How have they been doing that before?!"
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, LlamaRenderState, LlamaModel> entityRenderer, EntityRendererProvider.Context context) {
        if (entityRenderer instanceof AgeableMobRenderer<?, ?, ?> ageableMobRenderer) {
            this.setAnimatedAgeableModel(ageableMobRenderer,
                    new SpitfulLlamaModel(context.bakeLayer(this.animatedLlama)));
        }
    }

    @Override
    protected @Nullable RenderLayer<LlamaRenderState, LlamaModel> getAnimatedLayer(RenderLayer<LlamaRenderState, LlamaModel> renderLayer, LivingEntityRenderer<?, LlamaRenderState, LlamaModel> entityRenderer, EntityRendererProvider.Context context) {
        if (renderLayer instanceof LlamaDecorLayer llamaDecorLayer) {
            llamaDecorLayer.adultModel = new SpitfulLlamaModel(context.bakeLayer(this.animatedLlamaDecor));
            return renderLayer;
        } else {
            return super.getAnimatedLayer(renderLayer, entityRenderer, context);
        }
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedLlama,
                () -> SpitfulLlamaModel.createAnimatedBodyLayer(CubeDeformation.NONE));
        context.registerLayerDefinition(this.animatedLlamaDecor,
                () -> SpitfulLlamaModel.createAnimatedBodyLayer(new CubeDeformation(0.5F)));
    }
}

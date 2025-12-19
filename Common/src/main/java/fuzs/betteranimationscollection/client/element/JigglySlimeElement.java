package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.JigglySlimeModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.monster.Slime;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.Nullable;

public class JigglySlimeElement extends SingletonModelElement<Slime, SlimeRenderState, SlimeModel> {
    public static final ContextKey<Float> WALK_ANIMATION_POS_PROPERTY = key("walk_animation_pos");
    public static final ContextKey<Float> TARGET_SQUISH_PROPERTY = key("target_squish");

    public static int animationSpeed;

    private final ModelLayerLocation animatedSlime;
    private final ModelLayerLocation animatedSlimeOuter;

    public JigglySlimeElement() {
        super(Slime.class, SlimeRenderState.class, SlimeModel.class);
        this.animatedSlime = this.registerModelLayer("animated_slime");
        this.animatedSlimeOuter = this.registerModelLayer("animated_slime", "outer");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "A pleasing visual change; this makes the insides of slimes flow around like liquid.",
                "They splish-splosh about even more when they jump. The eyes, the mouth, and the core itself all move independently."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, SlimeRenderState, SlimeModel> entityRenderer, EntityRendererProvider.Context context) {
        entityRenderer.model = new JigglySlimeModel(context.bakeLayer(this.animatedSlime));
    }

    @Override
    protected @Nullable RenderLayer<SlimeRenderState, SlimeModel> getAnimatedLayer(RenderLayer<SlimeRenderState, SlimeModel> renderLayer, LivingEntityRenderer<?, SlimeRenderState, SlimeModel> entityRenderer, EntityRendererProvider.Context context) {
        if (renderLayer instanceof SlimeOuterLayer slimeOuterLayer) {
            slimeOuterLayer.model = new JigglySlimeModel(context.bakeLayer(this.animatedSlimeOuter));
            return slimeOuterLayer;
        } else {
            return super.getAnimatedLayer(renderLayer, entityRenderer, context);
        }
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedSlime, SlimeModel::createInnerBodyLayer);
        context.registerLayerDefinition(this.animatedSlimeOuter, SlimeModel::createOuterBodyLayer);
    }

    @Override
    protected void extractRenderState(Slime entity, SlimeRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        if (!entity.isPassenger() && entity.isAlive()) {
            RenderStateExtraData.set(renderState,
                    WALK_ANIMATION_POS_PROPERTY,
                    entity.walkAnimation.position(partialTick));
        } else {
            RenderStateExtraData.set(renderState, WALK_ANIMATION_POS_PROPERTY, 0.0F);
        }

        RenderStateExtraData.set(renderState, TARGET_SQUISH_PROPERTY, entity.targetSquish);
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Animation swing speed of inner slime parts.")
                .defineInRange("animation_speed", 5, 1, 20), v -> animationSpeed = v);
    }
}

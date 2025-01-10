package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.JigglySlimeModel;
import fuzs.puzzleslib.api.client.util.v1.RenderPropertyKey;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.world.entity.monster.Slime;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class JigglySlimeElement extends ModelElement<Slime, SlimeRenderState, SlimeModel> {
    public static final RenderPropertyKey<Float> WALK_ANIMATION_POS_PROPERTY = key("walk_animation_pos");
    public static final RenderPropertyKey<Float> TARGET_SQUISH_PROPERTY = key("target_squish");

    public static int animationSpeed;

    private final ModelLayerLocation animatedSlime;
    private final ModelLayerLocation animatedSlimeOuter;

    public JigglySlimeElement(BiFunction<String, String, ModelLayerLocation> factory) {
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
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedSlime, SlimeModel::createInnerBodyLayer);
        context.accept(this.animatedSlimeOuter, SlimeModel::createOuterBodyLayer);
    }

    @Override
    protected void extractRenderState(Slime entity, SlimeRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        if (!entity.isPassenger() && entity.isAlive()) {
            RenderPropertyKey.setRenderProperty(renderState,
                    WALK_ANIMATION_POS_PROPERTY,
                    entity.walkAnimation.position(partialTick));
        } else {
            RenderPropertyKey.setRenderProperty(renderState, WALK_ANIMATION_POS_PROPERTY, 0.0F);
        }
        RenderPropertyKey.setRenderProperty(renderState, TARGET_SQUISH_PROPERTY, entity.targetSquish);
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Animation swing speed of inner slime parts.")
                .defineInRange("animation_speed", 5, 1, 20), v -> animationSpeed = v);
    }
}

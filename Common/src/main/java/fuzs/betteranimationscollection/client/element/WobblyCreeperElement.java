package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.WobblyCreeperModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CreeperPowerLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.monster.Creeper;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

public class WobblyCreeperElement extends SingletonModelElement<Creeper, CreeperRenderState, CreeperModel> {
    public static final ContextKey<WobbleDirection> WOBBLE_DIRECTION_PROPERTY = key("wobble_direction");

    public static WobbleDirection wobbleDirection;

    private final ModelLayerLocation animatedCreeper;
    private final ModelLayerLocation animatedCreeperArmor;

    public WobblyCreeperElement() {
        super(Creeper.class, CreeperRenderState.class, CreeperModel.class);
        this.animatedCreeper = this.registerModelLayer("animated_creeper");
        this.animatedCreeperArmor = this.registerModelLayer("animated_creeper", "armor");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "Remember that one very popular \"TNT\" music video by CaptainSparklez with the really cute wobbly Creeper?",
                "Well, it's in the game now. The full thing, the real deal, exactly like the video."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, CreeperRenderState, CreeperModel> entityRenderer, EntityRendererProvider.Context context) {
        entityRenderer.model = new WobblyCreeperModel(context.bakeLayer(this.animatedCreeper), false);
        for (int i = 0; i < entityRenderer.layers.size(); i++) {
            RenderLayer<CreeperRenderState, CreeperModel> renderLayer = entityRenderer.layers.get(i);
            RenderLayer<CreeperRenderState, CreeperModel> animatedRenderLayer = this.getAnimatedLayer(renderLayer,
                    entityRenderer,
                    context);
            if (animatedRenderLayer != null) {
                entityRenderer.layers.set(i, animatedRenderLayer);
                break;
            }
        }
    }

    @Nullable
    @Override
    protected RenderLayer<CreeperRenderState, CreeperModel> getAnimatedLayer(RenderLayer<CreeperRenderState, CreeperModel> renderLayer, LivingEntityRenderer<?, CreeperRenderState, CreeperModel> entityRenderer, EntityRendererProvider.Context context) {
        if (renderLayer instanceof CreeperPowerLayer) {
            return new CreeperPowerLayer(entityRenderer, context.getModelSet()) {
                private final WobblyCreeperModel model = new WobblyCreeperModel(context.bakeLayer(WobblyCreeperElement.this.animatedCreeperArmor),
                        true);

                @Override
                protected CreeperModel model() {
                    return this.model;
                }
            };
        } else {
            return super.getAnimatedLayer(renderLayer, entityRenderer, context);
        }
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedCreeper,
                () -> WobblyCreeperModel.createAnimatedBodyLayer(CubeDeformation.NONE));
        context.registerLayerDefinition(this.animatedCreeperArmor,
                () -> WobblyCreeperModel.createAnimatedBodyLayer(new CubeDeformation(2.0F)));
    }

    @Override
    protected void extractRenderState(Creeper entity, CreeperRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        WobblyCreeperElement.WobbleDirection wobbleDirection = WobblyCreeperElement.wobbleDirection;
        if (wobbleDirection == WobblyCreeperElement.WobbleDirection.RANDOM) {
            wobbleDirection = WobblyCreeperElement.WobbleDirection.values()[(int) Math.abs(
                    entity.getUUID().getLeastSignificantBits() % (WobblyCreeperElement.WobbleDirection.values().length
                            - 1))];
        }

        RenderStateExtraData.set(renderState, WOBBLE_DIRECTION_PROPERTY, wobbleDirection);
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Different directional behaviour modes for the walking animation.")
                .defineEnum("wobble_direction", WobbleDirection.SIDE), v -> wobbleDirection = v);
    }

    public enum WobbleDirection {
        SIDE,
        FRONT,
        CIRCLE,
        RANDOM
    }
}

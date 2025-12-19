package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.FlailingEndermanModel;
import fuzs.betteranimationscollection.client.renderer.entity.layers.FlailingCarriedBlockLayer;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.monster.enderman.EndermanModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CarriedBlockLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.world.entity.monster.EnderMan;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.Nullable;

public class FlailingEndermanElement extends SingletonModelElement<EnderMan, EndermanRenderState, EndermanModel<EndermanRenderState>> {
    public static int animationSpeed;
    public static boolean whileCarrying;

    private final ModelLayerLocation animatedEnderman;

    public FlailingEndermanElement() {
        super(EnderMan.class,
                EndermanRenderState.class,
                (Class<EndermanModel<EndermanRenderState>>) (Class<?>) EndermanModel.class);
        this.animatedEnderman = this.registerModelLayer("animated_enderman");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "If an enderman is angry it will wave its arms around wildly while chasing its target.",
                "Suits their twisted nature very well."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, EndermanRenderState, EndermanModel<EndermanRenderState>> entityRenderer, EntityRendererProvider.Context context) {
        entityRenderer.model = new FlailingEndermanModel(context.bakeLayer(this.animatedEnderman));
    }

    @Override
    protected @Nullable RenderLayer<EndermanRenderState, EndermanModel<EndermanRenderState>> getAnimatedLayer(RenderLayer<EndermanRenderState, EndermanModel<EndermanRenderState>> renderLayer, LivingEntityRenderer<?, EndermanRenderState, EndermanModel<EndermanRenderState>> entityRenderer, EntityRendererProvider.Context context) {
        if (renderLayer instanceof CarriedBlockLayer) {
            return new FlailingCarriedBlockLayer(entityRenderer);
        } else {
            return super.getAnimatedLayer(renderLayer, entityRenderer, context);
        }
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedEnderman, FlailingEndermanModel::createAnimatedBodyLayer);
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Animation swing speed for arms.").defineInRange("animation_speed", 5, 1, 20),
                v -> animationSpeed = v);
        callback.accept(builder.comment("Flail arms while carrying a block.").define("fail_while_carrying", true),
                v -> whileCarrying = v);
    }
}

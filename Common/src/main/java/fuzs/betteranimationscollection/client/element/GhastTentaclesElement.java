package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.GhastTentaclesModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.monster.ghast.GhastModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.GhastRenderState;
import net.minecraft.world.entity.monster.Ghast;
import net.neoforged.neoforge.common.ModConfigSpec;

public class GhastTentaclesElement extends SingletonModelElement<Ghast, GhastRenderState, GhastModel> {
    public static int maxTentaclesLength;
    public static int animationSpeed;

    private final ModelLayerLocation animatedGhast;

    public GhastTentaclesElement() {
        super(Ghast.class, GhastRenderState.class, GhastModel.class);
        this.animatedGhast = this.registerModelLayer("animated_ghast");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "Divides ghast tentacles into parts and makes them wiggle realistically, like those tentacle monsters you always see at the movies.",
                "Makes them a little more scary, but ultimately nicer to look at."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, GhastRenderState, GhastModel> entityRenderer, EntityRendererProvider.Context context) {
        entityRenderer.model = new GhastTentaclesModel(context.bakeLayer(this.animatedGhast));
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedGhast, GhastTentaclesModel::createAnimatedBodyLayer);
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Define the max length of tentacles.")
                .defineInRange("max_tentacles_length",
                        GhastTentaclesModel.GHAST_MAX_TENTACLES_LENGTH,
                        2,
                        GhastTentaclesModel.GHAST_MAX_TENTACLES_LENGTH), v -> maxTentaclesLength = v);
        callback.accept(builder.comment("Animation swing speed of tentacles.")
                .defineInRange("animation_speed", 5, 1, 20), v -> animationSpeed = v);
    }
}

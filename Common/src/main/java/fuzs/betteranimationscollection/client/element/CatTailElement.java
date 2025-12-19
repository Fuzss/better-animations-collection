package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.CatTailModel;
import fuzs.betteranimationscollection.client.model.OcelotTailModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.animal.feline.CatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.world.entity.animal.feline.Cat;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.Nullable;

public class CatTailElement extends SingletonModelElement<Cat, CatRenderState, CatModel> {
    public static int tailLength;
    public static int animationSpeed;

    private final ModelLayerLocation animatedCat;
    private final ModelLayerLocation animatedCatCollar;
    private final ModelLayerLocation animatedCatBaby;
    private final ModelLayerLocation animatedCatBabyCollar;

    public CatTailElement() {
        super(Cat.class, CatRenderState.class, CatModel.class);
        this.animatedCat = this.registerModelLayer("animated_cat");
        this.animatedCatCollar = this.registerModelLayer("animated_cat", "collar");
        this.animatedCatBaby = this.registerModelLayer("animated_cat_baby");
        this.animatedCatBabyCollar = this.registerModelLayer("animated_cat_baby", "collar");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "Takes away the stick tails of the current cats and gives them something nicer instead.",
                "Fully animated flowing tails that move while they stand or run, and even curl around their bodies when they sit."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, CatRenderState, CatModel> entityRenderer, EntityRendererProvider.Context context) {
        setAnimatedAgeableModel(entityRenderer,
                new CatTailModel(context.bakeLayer(this.animatedCat)),
                new CatTailModel(context.bakeLayer(this.animatedCatBaby)));
    }

    @Override
    protected @Nullable RenderLayer<CatRenderState, CatModel> getAnimatedLayer(RenderLayer<CatRenderState, CatModel> renderLayer, LivingEntityRenderer<?, CatRenderState, CatModel> entityRenderer, EntityRendererProvider.Context context) {
        if (renderLayer instanceof CatCollarLayer catCollarLayer) {
            catCollarLayer.adultModel = new CatTailModel(context.bakeLayer(this.animatedCatCollar));
            catCollarLayer.babyModel = new CatTailModel(context.bakeLayer(this.animatedCatBabyCollar));
            return catCollarLayer;
        } else {
            return super.getAnimatedLayer(renderLayer, entityRenderer, context);
        }
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedCat,
                () -> OcelotTailModel.createAnimatedBodyMesh(CubeDeformation.NONE));
        context.registerLayerDefinition(this.animatedCatCollar,
                () -> OcelotTailModel.createAnimatedBodyMesh(new CubeDeformation(0.01F)));
        context.registerLayerDefinition(this.animatedCatBaby,
                () -> OcelotTailModel.createAnimatedBodyMesh(CubeDeformation.NONE).apply(CatModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(this.animatedCatBabyCollar,
                () -> OcelotTailModel.createAnimatedBodyMesh(new CubeDeformation(0.01F))
                        .apply(CatModel.BABY_TRANSFORMER));
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Define tail length.")
                .defineInRange("tail_length",
                        OcelotTailModel.OCELOT_TAIL_LENGTH,
                        1,
                        OcelotTailModel.OCELOT_TAIL_LENGTH), v -> tailLength = v);
        callback.accept(builder.comment("Animation swing speed for tail.").defineInRange("animation_speed", 7, 1, 20),
                v -> animationSpeed = v);
    }
}

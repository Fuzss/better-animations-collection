package fuzs.betteranimationscollection.common.client.element;

import fuzs.betteranimationscollection.common.client.model.CatTailModel;
import fuzs.betteranimationscollection.common.client.model.OcelotTailModel;
import fuzs.puzzleslib.common.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.common.api.config.v3.ValueCallback;
import net.minecraft.client.model.animal.feline.AbstractFelineModel;
import net.minecraft.client.model.animal.feline.AdultCatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.world.entity.animal.feline.Cat;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.Nullable;

public class CatTailElement extends SingletonModelElement<Cat, CatRenderState, AbstractFelineModel<CatRenderState>> {
    public static int tailLength;
    public static int animationSpeed;

    private final ModelLayerLocation animatedCat;
    private final ModelLayerLocation animatedCatCollar;

    public CatTailElement() {
        super(Cat.class, CatRenderState.class, AdultCatModel.class);
        this.animatedCat = this.registerModelLayer("animated_cat");
        this.animatedCatCollar = this.registerModelLayer("animated_cat", "collar");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "Takes away the stick tails of the current cats and gives them something nicer instead.",
                "Fully animated flowing tails that move while they stand or run, and even curl around their bodies when they sit."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, CatRenderState, AbstractFelineModel<CatRenderState>> entityRenderer, EntityRendererProvider.Context context) {
        if (entityRenderer instanceof AgeableMobRenderer<?, ?, ?> ageableMobRenderer) {
            this.setAnimatedAgeableModel(ageableMobRenderer, new CatTailModel(context.bakeLayer(this.animatedCat)));
        }
    }

    @Override
    protected @Nullable RenderLayer<CatRenderState, AbstractFelineModel<CatRenderState>> getAnimatedLayer(RenderLayer<CatRenderState, AbstractFelineModel<CatRenderState>> renderLayer, LivingEntityRenderer<?, CatRenderState, AbstractFelineModel<CatRenderState>> entityRenderer, EntityRendererProvider.Context context) {
        if (renderLayer instanceof CatCollarLayer catCollarLayer) {
            catCollarLayer.adultModel = new CatTailModel(context.bakeLayer(this.animatedCatCollar));
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

package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.OinkyPigModel;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.Pig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class OinkyPigElement extends SoundBasedElement<Pig, PigRenderState, PigModel> {
    public static boolean floatyEars;
    public static int earAnimationSpeed;

    private final ModelLayerLocation animatedPig;
    private final ModelLayerLocation animatedPigSaddle;
    private final ModelLayerLocation animatedPigBaby;
    private final ModelLayerLocation animatedPigBabySaddle;

    public OinkyPigElement() {
        super(Pig.class, PigRenderState.class, PigModel.class, SoundEvents.PIG_AMBIENT);
        this.animatedPig = this.registerModelLayer("animated_pig");
        this.animatedPigSaddle = this.registerModelLayer("animated_pig", "saddle");
        this.animatedPigBaby = this.registerModelLayer("animated_pig_baby");
        this.animatedPigBabySaddle = this.registerModelLayer("animated_pig_baby", "saddle");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "This makes the udders on cows wobble around when they walk.", "Also makes their udders have nipples."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, PigRenderState, PigModel> entityRenderer, EntityRendererProvider.Context context) {
        setAnimatedAgeableModel(entityRenderer,
                new OinkyPigModel(context.bakeLayer(this.animatedPig)),
                new OinkyPigModel(context.bakeLayer(this.animatedPigBaby)));
    }

    @Override
    protected @Nullable RenderLayer<PigRenderState, PigModel> getAnimatedLayer(RenderLayer<PigRenderState, PigModel> renderLayer, LivingEntityRenderer<?, PigRenderState, PigModel> entityRenderer, EntityRendererProvider.Context context) {
        if (renderLayer instanceof SaddleLayer<PigRenderState, PigModel> saddleLayer) {
            return new SaddleLayer<>(entityRenderer,
                    new OinkyPigModel(context.bakeLayer(this.animatedPigSaddle)),
                    new OinkyPigModel(context.bakeLayer(this.animatedPigBabySaddle)),
                    saddleLayer.textureLocation);
        } else {
            return super.getAnimatedLayer(renderLayer, entityRenderer, context);
        }
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedPig, () -> OinkyPigModel.createAnimatedBodyLayer(CubeDeformation.NONE));
        context.accept(this.animatedPigSaddle, () -> OinkyPigModel.createAnimatedBodyLayer(new CubeDeformation(0.5F)));
        context.accept(this.animatedPigBaby,
                () -> OinkyPigModel.createAnimatedBodyLayer(CubeDeformation.NONE).apply(PigModel.BABY_TRANSFORMER));
        context.accept(this.animatedPigBabySaddle,
                () -> OinkyPigModel.createAnimatedBodyLayer(new CubeDeformation(0.5F))
                        .apply(PigModel.BABY_TRANSFORMER));
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        super.setupModelConfig(builder, callback);
        callback.accept(builder.comment("Fancy ears for pigs, just like piglins have them.")
                .define("floaty_ears", true), v -> floatyEars = v);
        callback.accept(builder.comment("Animation swing speed for ear floatiness.")
                .defineInRange("ear_animation_speed", 10, 1, 20), v -> earAnimationSpeed = v);
    }
}

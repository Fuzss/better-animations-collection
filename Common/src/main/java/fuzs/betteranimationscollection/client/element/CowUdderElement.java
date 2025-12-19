package fuzs.betteranimationscollection.client.element;

import com.google.common.collect.Maps;
import fuzs.betteranimationscollection.client.model.CowUdderModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Map;

public class CowUdderElement extends SingletonModelElement<Cow, CowRenderState, CowModel> {
    public static int animationSpeed;
    public static boolean showNipples;
    public static boolean calfUtter;

    private final ModelLayerLocation animatedCow;
    private final ModelLayerLocation animatedColdCow;
    private final ModelLayerLocation animatedWarmCow;
    private final ModelLayerLocation animatedCowBaby;
    private final ModelLayerLocation animatedColdCowBaby;
    private final ModelLayerLocation animatedWarmCowBaby;

    public CowUdderElement() {
        super(Cow.class, CowRenderState.class, CowModel.class);
        this.animatedCow = this.registerModelLayer("animated_cow");
        this.animatedColdCow = this.registerModelLayer("animated_cold_cow");
        this.animatedWarmCow = this.registerModelLayer("animated_warm_cow");
        this.animatedCowBaby = this.registerModelLayer("animated_cow_baby");
        this.animatedColdCowBaby = this.registerModelLayer("animated_cold_cow_baby");
        this.animatedWarmCowBaby = this.registerModelLayer("animated_warm_cow_baby");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "This makes the udders on cows wobble around when they walk.", "Also makes their udders have nipples."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, CowRenderState, CowModel> entityRenderer, EntityRendererProvider.Context context) {
        if (entityRenderer instanceof CowRenderer cowRenderer) {
            cowRenderer.models = this.bakeModels(context);
        } else if (entityRenderer instanceof AgeableMobRenderer<?, ?, ?>) {
            setAnimatedAgeableModel(entityRenderer,
                    new CowUdderModel(context.bakeLayer(this.animatedCow)),
                    new CowUdderModel(context.bakeLayer(this.animatedCowBaby)));
        }
    }

    private Map<CowVariant.ModelType, AdultAndBabyModelPair<CowModel>> bakeModels(EntityRendererProvider.Context context) {
        return Maps.newEnumMap(Map.of(CowVariant.ModelType.NORMAL,
                new AdultAndBabyModelPair<>(new CowUdderModel(context.bakeLayer(this.animatedCow)),
                        new CowUdderModel(context.bakeLayer(this.animatedCowBaby))),
                CowVariant.ModelType.WARM,
                new AdultAndBabyModelPair<>(new CowUdderModel(context.bakeLayer(this.animatedWarmCow)),
                        new CowUdderModel(context.bakeLayer(this.animatedWarmCowBaby))),
                CowVariant.ModelType.COLD,
                new AdultAndBabyModelPair<>(new CowUdderModel(context.bakeLayer(this.animatedColdCow)),
                        new CowUdderModel(context.bakeLayer(this.animatedColdCowBaby)))));
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedCow, CowUdderModel::createAnimatedBodyLayer);
        context.registerLayerDefinition(this.animatedColdCow, CowUdderModel::createAnimatedColdBodyLayer);
        context.registerLayerDefinition(this.animatedWarmCow, CowUdderModel::createAnimatedWarmBodyLayer);
        context.registerLayerDefinition(this.animatedCowBaby,
                () -> CowUdderModel.createAnimatedBodyLayer().apply(CowUdderModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(this.animatedColdCowBaby,
                () -> CowUdderModel.createAnimatedColdBodyLayer().apply(CowUdderModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(this.animatedWarmCowBaby,
                () -> CowUdderModel.createAnimatedWarmBodyLayer().apply(CowUdderModel.BABY_TRANSFORMER));
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Animation swing speed of utter when the cow is walking.")
                .defineInRange("animation_speed", 5, 1, 20), v -> animationSpeed = v);
        callback.accept(builder.comment("Render tiny nipples on a cow's utter.").define("show_nipples", true),
                v -> showNipples = v);
        callback.accept(builder.comment("Should calves show an utter.").define("calf_utter", false),
                v -> calfUtter = v);
    }
}

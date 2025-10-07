package fuzs.betteranimationscollection.client.element;

import com.google.common.collect.Maps;
import fuzs.betteranimationscollection.client.model.BuckaChickenModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Map;

public class BuckaChickenElement extends SoundBasedElement<Chicken, ChickenRenderState, ChickenModel> {
    public static boolean slimBill;
    public static boolean moveHead;
    public static boolean moveWattles;
    public static boolean moveWings;
    public static int headAnimationSpeed;
    public static int wattlesAnimationSpeed;
    public static int wingAnimationSpeed;

    private final ModelLayerLocation animatedChicken;
    private final ModelLayerLocation animatedColdChicken;
    private final ModelLayerLocation animatedChickenBaby;
    private final ModelLayerLocation animatedColdChickenBaby;

    public BuckaChickenElement() {
        super(Chicken.class, ChickenRenderState.class, ChickenModel.class, SoundEvents.CHICKEN_AMBIENT);
        this.animatedChicken = this.registerModelLayer("animated_chicken");
        this.animatedColdChicken = this.registerModelLayer("animated_cold_chicken");
        this.animatedChickenBaby = this.registerModelLayer("animated_chicken_baby");
        this.animatedColdChickenBaby = this.registerModelLayer("animated_cold_chicken_baby");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "This one makes chicken beaks open and close when they cluck.",
                "When they strut their heads move back and forth, the red thing under their beak swings around and their wings flap a little. Just like the real deal!"
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, ChickenRenderState, ChickenModel> entityRenderer, EntityRendererProvider.Context context) {
        if (entityRenderer instanceof ChickenRenderer chickenRenderer) {
            chickenRenderer.models = this.bakeModels(context);
        } else if (entityRenderer instanceof AgeableMobRenderer<?, ?, ?>) {
            setAnimatedAgeableModel(entityRenderer,
                    new BuckaChickenModel(context.bakeLayer(this.animatedChicken)),
                    new BuckaChickenModel(context.bakeLayer(this.animatedChickenBaby)));
        }
    }

    private Map<ChickenVariant.ModelType, AdultAndBabyModelPair<ChickenModel>> bakeModels(EntityRendererProvider.Context context) {
        return Maps.newEnumMap(Map.of(ChickenVariant.ModelType.NORMAL,
                new AdultAndBabyModelPair<>(new BuckaChickenModel(context.bakeLayer(this.animatedChicken)),
                        new BuckaChickenModel(context.bakeLayer(this.animatedChickenBaby))),
                ChickenVariant.ModelType.COLD,
                new AdultAndBabyModelPair<>(new BuckaChickenModel(context.bakeLayer(this.animatedColdChicken)),
                        new BuckaChickenModel(context.bakeLayer(this.animatedColdChickenBaby)))));
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedChicken, BuckaChickenModel::createAnimatedBodyLayer);
        context.registerLayerDefinition(this.animatedColdChicken, BuckaChickenModel::createAnimatedColdBodyLayer);
        context.registerLayerDefinition(this.animatedChickenBaby,
                () -> BuckaChickenModel.createAnimatedBodyLayer().apply(ChickenModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(this.animatedColdChickenBaby,
                () -> BuckaChickenModel.createAnimatedColdBodyLayer().apply(ChickenModel.BABY_TRANSFORMER));
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        super.setupModelConfig(builder, callback);
        callback.accept(builder.comment("Make bill a lot slimmer so chickens look less like ducks.")
                .define("slim_bill", true), v -> {
            if (slimBill != v) {
                this.markChanged();
            }

            slimBill = v;
        });
        callback.accept(builder.comment("Move head back and forth when chicken is walking.").define("move_head", true),
                v -> moveHead = v);
        callback.accept(builder.comment("Wiggle chin when chicken is walking.").define("wiggle_wattles", true),
                v -> moveWattles = v);
        callback.accept(builder.comment("Flap wings when chicken is walking.").define("flap_wings", true),
                v -> moveWings = v);
        callback.accept(builder.comment("Move head back and forth when chicken is walking.")
                .defineInRange("head_animation_speed", 4, 1, 20), v -> headAnimationSpeed = v);
        callback.accept(builder.comment("Animation swing speed for wattles movement.")
                .defineInRange("wattles_animation_speed", 5, 1, 20), v -> wattlesAnimationSpeed = v);
        callback.accept(builder.comment("Animation swing speed of wing flapping.")
                .defineInRange("wing_animation_speed", 3, 1, 20), v -> wingAnimationSpeed = v);
    }
}

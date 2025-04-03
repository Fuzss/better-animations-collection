package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.BuckaChickenModel;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.Chicken;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BuckaChickenElement extends SoundBasedElement<Chicken, ChickenRenderState, ChickenModel> {
    public static boolean slimBill;
    public static boolean moveHead;
    public static boolean moveWattles;
    public static boolean moveWings;
    public static int headAnimationSpeed;
    public static int wattlesAnimationSpeed;
    public static int wingAnimationSpeed;

    private final ModelLayerLocation animatedChicken;
    private final ModelLayerLocation animatedChickenBaby;

    public BuckaChickenElement() {
        super(Chicken.class, ChickenRenderState.class, ChickenModel.class, SoundEvents.CHICKEN_AMBIENT);
        this.animatedChicken = this.registerModelLayer("animated_chicken");
        this.animatedChickenBaby = this.registerModelLayer("animated_chicken_baby");
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
        setAnimatedAgeableModel(entityRenderer,
                new BuckaChickenModel(context.bakeLayer(this.animatedChicken)),
                new BuckaChickenModel(context.bakeLayer(this.animatedChickenBaby)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedChicken, BuckaChickenModel::createAnimatedBodyLayer);
        context.accept(this.animatedChickenBaby,
                () -> BuckaChickenModel.createAnimatedBodyLayer().apply(ChickenModel.BABY_TRANSFORMER));
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        super.setupModelConfig(builder, callback);
        callback.accept(builder.comment("Make bill a lot slimmer so chickens look less like ducks.")
                .define("slim_bill", true), v -> slimBill = v);
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

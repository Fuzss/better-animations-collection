package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.PlayfulDoggyModel;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WolfArmorLayer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Wolf;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class PlayfulDoggyElement extends SingletonModelElement<Wolf, WolfRenderState, WolfModel> {
    public static final float MAX_ROLL_ANIM = 0.15F * Mth.PI;

    public static int tailLength;
    public static boolean fluffyTail;
    public static int animationSpeed;
    public static SittingAnim sittingAnim;

    private final ModelLayerLocation animatedWolf;
    private final ModelLayerLocation animatedWolfArmor;
    private final ModelLayerLocation animatedWolfBaby;
    private final ModelLayerLocation animatedWolfBabyArmor;

    public PlayfulDoggyElement() {
        super(Wolf.class, WolfRenderState.class, WolfModel.class);
        this.animatedWolf = this.registerModelLayer("animated_wolf");
        this.animatedWolfArmor = this.registerModelLayer("animated_wolf", "armor");
        this.animatedWolfBaby = this.registerModelLayer("animated_wolf_baby");
        this.animatedWolfBabyArmor = this.registerModelLayer("animated_wolf_baby", "armor");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "Changes wolf tails to be fluffier and flowier, wagging realistically while they stand and run.",
                "Also makes tamed wolves lie down instead of sitting. Hold up some meat and they'll roll over, too."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, WolfRenderState, WolfModel> entityRenderer, EntityRendererProvider.Context context) {
        setAnimatedAgeableModel(entityRenderer,
                new PlayfulDoggyModel(context.bakeLayer(this.animatedWolf)),
                new PlayfulDoggyModel(context.bakeLayer(this.animatedWolfBaby)));
    }

    @Override
    protected @Nullable RenderLayer<WolfRenderState, WolfModel> getAnimatedLayer(RenderLayer<WolfRenderState, WolfModel> renderLayer, LivingEntityRenderer<?, WolfRenderState, WolfModel> entityRenderer, EntityRendererProvider.Context context) {
        if (renderLayer instanceof WolfArmorLayer wolfArmorLayer) {
            wolfArmorLayer.adultModel = new PlayfulDoggyModel(context.bakeLayer(this.animatedWolfArmor));
            wolfArmorLayer.babyModel = new PlayfulDoggyModel(context.bakeLayer(this.animatedWolfBabyArmor));
            return wolfArmorLayer;
        } else {
            return super.getAnimatedLayer(renderLayer, entityRenderer, context);
        }
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedWolf,
                () -> LayerDefinition.create(PlayfulDoggyModel.createAnimatedBodyLayer(CubeDeformation.NONE), 64, 32));
        context.accept(this.animatedWolfArmor,
                () -> LayerDefinition.create(PlayfulDoggyModel.createAnimatedBodyLayer(new CubeDeformation(0.2F)),
                        64,
                        32));
        context.accept(this.animatedWolfBaby,
                () -> LayerDefinition.create(PlayfulDoggyModel.createAnimatedBodyLayer(CubeDeformation.NONE), 64, 32)
                        .apply(WolfModel.BABY_TRANSFORMER));
        context.accept(this.animatedWolfBabyArmor,
                () -> LayerDefinition.create(PlayfulDoggyModel.createAnimatedBodyLayer(new CubeDeformation(0.2F)),
                        64,
                        32).apply(WolfModel.BABY_TRANSFORMER));
    }

    public static float getRollAnimScale(WolfRenderState renderState) {
        if (renderState.isSitting && PlayfulDoggyElement.sittingAnim.rollOver()) {
            if (PlayfulDoggyElement.sittingAnim.begForMeat()) {
                return renderState.headRollAngle >= 1.0E-4F ? renderState.headRollAngle / MAX_ROLL_ANIM : 0.0F;
            } else {
                return 1.0F;
            }
        } else {
            return 0.0F;
        }
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Define tail length.")
                .defineInRange("tail_length",
                        PlayfulDoggyModel.WOLF_TAIL_LENGTH,
                        1,
                        PlayfulDoggyModel.WOLF_TAIL_LENGTH), v -> tailLength = v);
        callback.accept(builder.comment("Make wolf tail fluffy.").define("fluffy_tail", true), v -> fluffyTail = v);
        callback.accept(builder.comment("Animation swing speed for tail.").defineInRange("animation_speed", 5, 1, 20),
                v -> animationSpeed = v);
        callback.accept(builder.comment("Pose and behaviour when sitting.",
                        "By default makes wolves lie down instead, and roll over when a nearby player is holding a piece meat.")
                .defineEnum("sitting_behaviour", SittingAnim.LIE_DOWN_AND_BEG_FOR_MEAT), v -> sittingAnim = v);
    }

    public enum SittingAnim {
        VANILLA,
        LIE_DOWN,
        ROLL_OVER,
        LIE_DOWN_AND_BEG_FOR_MEAT;

        public boolean lieDown() {
            return this != VANILLA;
        }

        public boolean rollOver() {
            return this == ROLL_OVER || this == LIE_DOWN_AND_BEG_FOR_MEAT;
        }

        public boolean begForMeat() {
            return this == LIE_DOWN_AND_BEG_FOR_MEAT;
        }
    }
}

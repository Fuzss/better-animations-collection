package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.CowUdderModel;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.animal.Cow;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CowUdderElement extends SingletonModelElement<Cow, LivingEntityRenderState, CowModel> {
    public static int animationSpeed;
    public static boolean showNipples;
    public static boolean calfUtter;

    private final ModelLayerLocation animatedCow;
    private final ModelLayerLocation animatedCowBaby;

    public CowUdderElement() {
        super(Cow.class, LivingEntityRenderState.class, CowModel.class);
        this.animatedCow = this.registerModelLayer("animated_cow");
        this.animatedCowBaby = this.registerModelLayer("animated_cow_baby");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "This makes the udders on cows wobble around when they walk.", "Also makes their udders have nipples."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, LivingEntityRenderState, CowModel> entityRenderer, EntityRendererProvider.Context context) {
        setAnimatedAgeableModel(entityRenderer,
                new CowUdderModel(context.bakeLayer(this.animatedCow)),
                new CowUdderModel(context.bakeLayer(this.animatedCowBaby)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedCow, CowUdderModel::createAnimatedBodyLayer);
        context.accept(this.animatedCowBaby,
                () -> CowUdderModel.createAnimatedBodyLayer().apply(CowUdderModel.BABY_TRANSFORMER));
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

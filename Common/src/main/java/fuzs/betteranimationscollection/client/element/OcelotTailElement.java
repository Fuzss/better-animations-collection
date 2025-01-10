package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.OcelotTailModel;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.FelineRenderState;
import net.minecraft.world.entity.animal.Ocelot;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class OcelotTailElement extends ModelElement<Ocelot, FelineRenderState, OcelotModel> {
    public static int tailLength;
    public static int animationSpeed;

    private final ModelLayerLocation animatedOcelot;
    private final ModelLayerLocation animatedOcelotBaby;

    public OcelotTailElement(BiFunction<String, String, ModelLayerLocation> factory) {
        super(Ocelot.class, FelineRenderState.class, OcelotModel.class);
        this.animatedOcelot = this.registerModelLayer("animated_ocelot");
        this.animatedOcelotBaby = this.registerModelLayer("animated_ocelot_baby");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "Takes away the stick tails of the current ocelots and gives them something nicer instead.",
                "Fully animated flowing tails that move while they stand or run."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, FelineRenderState, OcelotModel> entityRenderer, EntityRendererProvider.Context context) {
        setAnimatedAgeableModel(entityRenderer,
                new OcelotTailModel(context.bakeLayer(this.animatedOcelot)),
                new OcelotTailModel(context.bakeLayer(this.animatedOcelotBaby)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedOcelot, () -> OcelotTailModel.createAnimatedBodyMesh(CubeDeformation.NONE));
        context.accept(this.animatedOcelotBaby,
                () -> OcelotTailModel.createAnimatedBodyMesh(CubeDeformation.NONE).apply(OcelotModel.BABY_TRANSFORMER));
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

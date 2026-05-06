package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.OcelotTailModel;
import fuzs.puzzleslib.common.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.common.api.config.v3.ValueCallback;
import net.minecraft.client.model.animal.feline.AdultOcelotModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.FelineRenderState;
import net.minecraft.world.entity.animal.feline.Ocelot;
import net.neoforged.neoforge.common.ModConfigSpec;

public class OcelotTailElement extends SingletonModelElement<Ocelot, FelineRenderState, AdultOcelotModel> {
    public static int tailLength;
    public static int animationSpeed;

    private final ModelLayerLocation animatedOcelot;

    public OcelotTailElement() {
        super(Ocelot.class, FelineRenderState.class, AdultOcelotModel.class);
        this.animatedOcelot = this.registerModelLayer("animated_ocelot");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "Takes away the stick tails of the current ocelots and gives them something nicer instead.",
                "Fully animated flowing tails that move while they stand or run."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, FelineRenderState, AdultOcelotModel> entityRenderer, EntityRendererProvider.Context context) {
        if (entityRenderer instanceof AgeableMobRenderer<?, ?, ?> ageableMobRenderer) {
            setAnimatedAgeableModel(ageableMobRenderer, new OcelotTailModel(context.bakeLayer(this.animatedOcelot)));
        }
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedOcelot,
                () -> OcelotTailModel.createAnimatedBodyMesh(CubeDeformation.NONE));
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

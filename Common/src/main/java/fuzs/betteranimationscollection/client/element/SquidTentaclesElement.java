package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.SquidTentaclesModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.SquidRenderState;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;

public class SquidTentaclesElement extends SingletonModelElement<Squid, SquidRenderState, SquidModel> {
    public static final ContextKey<Vec3> DELTA_MOVEMENT_PROPERTY = key("delta_movement");

    public static int tentaclesLength;

    private final ModelLayerLocation animatedSquid;
    private final ModelLayerLocation animatedSquidBaby;

    public SquidTentaclesElement() {
        super(Squid.class, SquidRenderState.class, SquidModel.class);
        this.animatedSquid = this.registerModelLayer("animated_squid");
        this.animatedSquidBaby = this.registerModelLayer("animated_squid_baby");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "Gives a jellyfish-like effect to the swimming animation of squids; generally just makes their tentacles flow more while moving."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, SquidRenderState, SquidModel> entityRenderer, EntityRendererProvider.Context context) {
        setAnimatedAgeableModel(entityRenderer,
                new SquidTentaclesModel(context.bakeLayer(this.animatedSquid)),
                new SquidTentaclesModel(context.bakeLayer(this.animatedSquidBaby)));
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedSquid, SquidTentaclesModel::createAnimatedBodyLayer);
        context.registerLayerDefinition(this.animatedSquidBaby,
                () -> SquidTentaclesModel.createAnimatedBodyLayer().apply(SquidModel.BABY_TRANSFORMER));
    }

    @Override
    protected void extractRenderState(Squid entity, SquidRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        RenderStateExtraData.set(renderState, DELTA_MOVEMENT_PROPERTY, entity.getDeltaMovement());
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Define length for squid tentacles.")
                .defineInRange("tentacles_length",
                        SquidTentaclesModel.SQUID_TENTACLES_LENGTH,
                        1,
                        SquidTentaclesModel.SQUID_TENTACLES_LENGTH), v -> tentaclesLength = v);
    }
}

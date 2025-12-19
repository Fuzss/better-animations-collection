package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.SnowGolemStickModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import net.minecraft.client.model.animal.golem.SnowGolemModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.animal.golem.SnowGolem;

public class SnowGolemStickElement extends SoundBasedElement<SnowGolem, LivingEntityRenderState, SnowGolemModel> {
    public static final ContextKey<Boolean> IS_LEFT_HANDED_PROPERTY = key("is_left_handed");

    private final ModelLayerLocation animatedSnowGolem;

    public SnowGolemStickElement() {
        super(SnowGolem.class, LivingEntityRenderState.class, SnowGolemModel.class, SoundEvents.SNOW_GOLEM_SHOOT);
        this.animatedSnowGolem = this.registerModelLayer("animated_snow_golem");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{"This makes a snowman's arm swing when it throws a snowball."};
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, LivingEntityRenderState, SnowGolemModel> entityRenderer, EntityRendererProvider.Context context) {
        entityRenderer.model = new SnowGolemStickModel(context.bakeLayer(this.animatedSnowGolem));
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedSnowGolem, SnowGolemModel::createBodyLayer);
    }

    @Override
    protected void extractRenderState(SnowGolem entity, LivingEntityRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        // makes 5 % of snowman render left-handed, like for most mobs with arms in vanilla
        RenderStateExtraData.set(renderState,
                IS_LEFT_HANDED_PROPERTY,
                Math.abs(entity.getUUID().getLeastSignificantBits() % 20) == 0);
    }
}

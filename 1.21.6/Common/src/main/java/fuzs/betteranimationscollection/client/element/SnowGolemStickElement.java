package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.SnowGolemStickModel;
import fuzs.puzzleslib.api.client.renderer.v1.RenderPropertyKey;
import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.SnowGolem;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SnowGolemStickElement extends SoundBasedElement<SnowGolem, LivingEntityRenderState, SnowGolemModel> {
    public static final RenderPropertyKey<Boolean> IS_LEFT_HANDED_PROPERTY = key("is_left_handed");

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
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedSnowGolem, SnowGolemModel::createBodyLayer);
    }

    @Override
    protected void extractRenderState(SnowGolem entity, LivingEntityRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        // makes 5 % of snowman render left-handed, like for most mobs with arms in vanilla
        RenderPropertyKey.set(renderState,
                IS_LEFT_HANDED_PROPERTY,
                Math.abs(entity.getUUID().getLeastSignificantBits() % 20) == 0);
    }
}

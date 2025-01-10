package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.handler.RemoteSoundHandler;
import fuzs.betteranimationscollection.client.model.IronGolemNoseModel;
import fuzs.puzzleslib.api.client.util.v1.RenderPropertyKey;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.world.entity.animal.IronGolem;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class IronGolemNoseElement extends ModelElement<IronGolem, IronGolemRenderState, IronGolemModel> {
    private final ModelLayerLocation animatedIronGolem;

    public IronGolemNoseElement(BiFunction<String, String, ModelLayerLocation> factory) {
        super(IronGolem.class, IronGolemRenderState.class, IronGolemModel.class);
        this.animatedIronGolem = this.registerModelLayer("animated_iron_golem");
        RemoteSoundHandler.INSTANCE.addAttackableEntity(this.entityClazz);
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "A subtle change; this makes iron golems wiggle their big noses whenever they're hurt.",
                "Exactly the same animation as for villagers, except for iron golems!"
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, IronGolemRenderState, IronGolemModel> entityRenderer, EntityRendererProvider.Context context) {
        entityRenderer.model = new IronGolemNoseModel(context.bakeLayer(this.animatedIronGolem));
    }

    @Override
    protected void extractRenderState(IronGolem entity, IronGolemRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        // this only works because MobEntity#ambientSoundTime is manually being synced to the client
        RenderPropertyKey.setRenderProperty(renderState,
                SoundBasedElement.AMBIENT_SOUND_TIME_PROPERTY,
                entity.ambientSoundTime + entity.getAmbientSoundInterval());
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedIronGolem, IronGolemNoseModel::createAnimatedBodyLayer);
    }
}

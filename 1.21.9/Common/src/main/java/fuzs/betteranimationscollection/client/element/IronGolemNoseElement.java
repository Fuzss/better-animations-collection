package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.handler.RemoteSoundHandler;
import fuzs.betteranimationscollection.client.model.IronGolemNoseModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.world.entity.animal.IronGolem;

public class IronGolemNoseElement extends SingletonModelElement<IronGolem, IronGolemRenderState, IronGolemModel> {
    private final ModelLayerLocation animatedIronGolem;

    public IronGolemNoseElement() {
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
        RenderStateExtraData.set(renderState,
                SoundBasedElement.AMBIENT_SOUND_TIME_PROPERTY,
                entity.ambientSoundTime + entity.getAmbientSoundInterval() + partialTick);
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedIronGolem, IronGolemNoseModel::createAnimatedBodyLayer);
    }
}

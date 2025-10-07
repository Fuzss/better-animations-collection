package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.handler.RemoteSoundHandler;
import fuzs.betteranimationscollection.client.model.VillagerNoseModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.AbstractVillager;

public class VillagerNoseElement extends SoundBasedElement<AbstractVillager, VillagerRenderState, VillagerModel> {
    private final ModelLayerLocation animatedGenericVillager;
    private final ModelLayerLocation animatedVillager;
    private final ModelLayerLocation animatedVillagerBaby;

    public VillagerNoseElement() {
        super(AbstractVillager.class,
                VillagerRenderState.class,
                VillagerModel.class,
                SoundEvents.VILLAGER_AMBIENT,
                SoundEvents.VILLAGER_TRADE,
                SoundEvents.WANDERING_TRADER_AMBIENT,
                SoundEvents.WANDERING_TRADER_TRADE);
        this.animatedGenericVillager = this.registerModelLayer("animated_generic_villager");
        this.animatedVillager = this.registerModelLayer("animated_villager");
        this.animatedVillagerBaby = this.registerModelLayer("animated_villager_baby");
        RemoteSoundHandler.INSTANCE.addAttackableEntity(this.entityClazz);
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "A subtle change; this makes villagers wiggle their big noses whenever they make their iconic sound.",
                "It's a small change, but who doesn't get a kick out of it?"
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, VillagerRenderState, VillagerModel> entityRenderer, EntityRendererProvider.Context context) {
        if (entityRenderer instanceof AgeableMobRenderer<?, ?, ?>) {
            setAnimatedAgeableModel(entityRenderer,
                    new VillagerNoseModel(context.bakeLayer(this.animatedVillager)),
                    new VillagerNoseModel(context.bakeLayer(this.animatedVillagerBaby)));
        } else {
            entityRenderer.model = new VillagerNoseModel(context.bakeLayer(this.animatedGenericVillager));
        }
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedGenericVillager,
                () -> LayerDefinition.create(VillagerModel.createBodyModel(), 64, 64));
        context.registerLayerDefinition(this.animatedVillager,
                () -> LayerDefinition.create(VillagerModel.createBodyModel(), 64, 64));
        // large baby villager head like Bedrock Edition
        context.registerLayerDefinition(this.animatedVillagerBaby,
                () -> LayerDefinition.create(VillagerModel.createBodyModel(), 64, 64)
                        .apply(HumanoidModel.BABY_TRANSFORMER));
    }
}

package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.handler.RemoteSoundHandler;
import fuzs.betteranimationscollection.client.model.VillagerNoseModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.AbstractVillager;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class VillagerNoseElement extends SoundBasedElement<AbstractVillager, VillagerRenderState, VillagerModel> {
    private final ModelLayerLocation animatedVillager;

    public VillagerNoseElement() {
        super(AbstractVillager.class,
                VillagerRenderState.class,
                VillagerModel.class,
                SoundEvents.VILLAGER_AMBIENT,
                SoundEvents.VILLAGER_TRADE,
                SoundEvents.WANDERING_TRADER_AMBIENT,
                SoundEvents.WANDERING_TRADER_TRADE);
        this.animatedVillager = this.registerModelLayer("animated_villager");
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
        entityRenderer.model = new VillagerNoseModel(context.bakeLayer(this.animatedVillager));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedVillager, () -> LayerDefinition.create(VillagerModel.createBodyModel(), 64, 64));
    }
}

package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.handler.RemoteSoundHandler;
import fuzs.betteranimationscollection.client.model.VillagerNoseModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class VillagerNoseElement extends SoundDetectionElement {
    private final ModelLayerLocation animatedVillager;

    public VillagerNoseElement(BiFunction<String, String, ModelLayerLocation> factory) {
        super(AbstractVillager.class, SoundEvents.VILLAGER_AMBIENT, SoundEvents.VILLAGER_TRADE, SoundEvents.WANDERING_TRADER_AMBIENT, SoundEvents.WANDERING_TRADER_TRADE);
        this.animatedVillager = factory.apply("animated_villager", "main");
        RemoteSoundHandler.INSTANCE.addAttackableEntity(AbstractVillager.class);
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"A subtle change; this makes villagers wiggle their big noses whenever they make their iconic sound.",
                "It's a small change, but who doesn't get a kick out of it?"};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<LivingEntity, VillagerModel<LivingEntity>>registerAnimatedModel(VillagerModel.class, () -> new VillagerNoseModel<>(bakery.bakeLayer(this.animatedVillager)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedVillager, () -> LayerDefinition.create(VillagerModel.createBodyModel(), 64, 64));
    }
}

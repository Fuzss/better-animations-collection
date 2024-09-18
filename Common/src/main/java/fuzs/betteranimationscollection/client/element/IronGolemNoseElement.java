package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.handler.RemoteSoundHandler;
import fuzs.betteranimationscollection.client.model.IronGolemNoseModel;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.animal.IronGolem;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class IronGolemNoseElement extends ModelElement {
    private final ModelLayerLocation animatedIronGolem;

    public IronGolemNoseElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedIronGolem = factory.apply("animated_iron_golem", "main");
        RemoteSoundHandler.INSTANCE.addAttackableEntity(IronGolem.class);
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"A subtle change; this makes iron golems wiggle their big noses whenever they're hurt.",
                "Exactly the same animation as for villagers, except for iron golems!"};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<IronGolem, IronGolemModel<IronGolem>>registerAnimatedModel(IronGolemModel.class, () -> new IronGolemNoseModel<>(bakery.bakeLayer(this.animatedIronGolem)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedIronGolem, IronGolemNoseModel::createAnimatedBodyLayer);
    }
}

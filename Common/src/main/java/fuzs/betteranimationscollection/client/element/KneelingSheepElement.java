package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.KneelingSheepFurModel;
import fuzs.betteranimationscollection.client.model.KneelingSheepModel;
import fuzs.betteranimationscollection.mixin.client.accessor.SheepFurLayerAccessor;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.world.entity.animal.Sheep;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class KneelingSheepElement extends ModelElement {
    private final ModelLayerLocation animatedSheep;
    private final ModelLayerLocation animatedSheepFur;

    public KneelingSheepElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedSheep = factory.apply("animated_sheep", "main");
        this.animatedSheepFur = factory.apply("animated_sheep", "fur");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"This one is pretty kneat. It makes sheep actually bend down to eat grass.",
                "It's no longer just their head lowering, their whole body lowers down to get a sweet sample of that succulent cellulose.",
                "Did you notice their KNEES bend too when they kneel?"};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<Sheep, SheepModel<Sheep>>registerAnimatedModel(SheepModel.class, () -> new KneelingSheepModel<>(bakery.bakeLayer(this.animatedSheep)), (RenderLayerParent<Sheep, SheepModel<Sheep>> renderLayerParent, RenderLayer<Sheep, SheepModel<Sheep>> renderLayer) -> {
            if (renderLayer instanceof SheepFurLayer) {
                ((SheepFurLayerAccessor) renderLayer).setModel(new KneelingSheepFurModel<>(bakery.bakeLayer(this.animatedSheepFur)));
            }
            return Optional.empty();
        });
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedSheep, KneelingSheepModel::createAnimatedBodyLayer);
        context.accept(this.animatedSheepFur, KneelingSheepFurModel::createAnimatedFurLayer);
    }
}

package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.FamiliarChestedHorseModel;
import fuzs.betteranimationscollection.client.model.FamiliarHorseModel;
import fuzs.betteranimationscollection.mixin.client.accessor.HorseArmorLayerAccessor;
import net.minecraft.client.model.ChestedHorseModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.Horse;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class FamiliarHorseElement extends ModelElement {
    private final ModelLayerLocation animatedHorse;
    private final ModelLayerLocation animatedHorseArmor;
    private final ModelLayerLocation animatedChestedHorse;

    public FamiliarHorseElement(BiFunction<String, String, ModelLayerLocation> factory) {
        super(, );
        this.animatedHorse = factory.apply("animated_horse", "main");
        this.animatedHorseArmor = factory.apply("animated_horse_armor", "main");
        this.animatedChestedHorse = factory.apply("animated_chested_horse", "main");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{"Makes horses more lively again, just like they used to be in older versions.",
                "Does this by adding back their mouth and knees while staying true to the vanilla model style."};
    }

    @Override
    protected void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<Horse, HorseModel<Horse>>registerAnimatedModel(HorseModel.class, () -> new FamiliarHorseModel<>(bakery.bakeLayer(this.animatedHorse)), (RenderLayerParent<Horse, HorseModel<Horse>> renderLayerParent, RenderLayer<Horse, HorseModel<Horse>> renderLayer) -> {
            if (renderLayer instanceof HorseArmorLayer) {
                ((HorseArmorLayerAccessor) renderLayer).setModel(new FamiliarHorseModel<>(bakery.bakeLayer(this.animatedHorseArmor)));
            }
            return Optional.empty();
        });
        context.<AbstractChestedHorse, FamiliarChestedHorseModel<AbstractChestedHorse>>registerAnimatedModel(ChestedHorseModel.class, () -> new FamiliarChestedHorseModel<>(bakery.bakeLayer(this.animatedChestedHorse)));
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedHorse, () -> LayerDefinition.create(FamiliarHorseModel.createAnimatedBodyMesh(CubeDeformation.NONE), 64, 64));
        context.accept(this.animatedHorseArmor, () -> LayerDefinition.create(FamiliarHorseModel.createAnimatedBodyMesh(new CubeDeformation(0.1F)), 64, 64));
        context.accept(this.animatedChestedHorse, FamiliarChestedHorseModel::createAnimatedBodyLayer);
    }
}

package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.FamiliarDonkeyModel;
import fuzs.betteranimationscollection.client.model.FamiliarHorseModel;
import net.minecraft.client.model.DonkeyModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
import net.minecraft.client.renderer.entity.state.HorseRenderState;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FamiliarHorseElement extends ModelElement {
    private final ModelLayerLocation animatedHorse;
    private final ModelLayerLocation animatedHorseArmor;
    private final ModelLayerLocation animatedDonkey;
    private final ModelLayerLocation animatedHorseBaby;
    private final ModelLayerLocation animatedHorseBabyArmor;
    private final ModelLayerLocation animatedDonkeyBaby;

    public FamiliarHorseElement() {
        this.animatedHorse = this.registerModelLayer("animated_horse");
        this.animatedHorseArmor = this.registerModelLayer("animated_horse", "armor");
        this.animatedDonkey = this.registerModelLayer("animated_donkey");
        this.animatedHorseBaby = this.registerModelLayer("animated_horse_baby");
        this.animatedHorseBabyArmor = this.registerModelLayer("animated_horse_baby", "armor");
        this.animatedDonkeyBaby = this.registerModelLayer("animated_donkey_baby");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "Makes horses more lively again, just like they used to be in older versions.",
                "Does this by adding back their mouth and knees while staying true to the vanilla model style."
        };
    }

    @Override
    protected void applyModelAnimations(LivingEntityRenderer<?, ?, ?> entityRenderer, EntityRendererProvider.Context context) {
        if (entityRenderer.getModel().getClass() == HorseModel.class) {
            setAnimatedAgeableModel((LivingEntityRenderer<?, HorseRenderState, HorseModel>) entityRenderer,
                    new FamiliarHorseModel(context.bakeLayer(this.animatedHorse)),
                    new FamiliarHorseModel(context.bakeLayer(this.animatedHorseBaby)));
            applyLayerAnimation((LivingEntityRenderer<?, HorseRenderState, HorseModel>) entityRenderer,
                    context,
                    (RenderLayer<HorseRenderState, HorseModel> renderLayer) -> {
                        if (renderLayer instanceof HorseArmorLayer horseArmorLayer) {
                            horseArmorLayer.adultModel = new FamiliarHorseModel(context.bakeLayer(this.animatedHorseArmor));
                            horseArmorLayer.babyModel = new FamiliarHorseModel(context.bakeLayer(this.animatedHorseBabyArmor));
                            return horseArmorLayer;
                        } else {
                            return null;
                        }
                    });
        }
        if (entityRenderer.getModel().getClass() == DonkeyModel.class) {
            setAnimatedAgeableModel((LivingEntityRenderer<?, DonkeyRenderState, DonkeyModel>) entityRenderer,
                    new FamiliarDonkeyModel(context.bakeLayer(this.animatedDonkey)),
                    new FamiliarDonkeyModel(context.bakeLayer(this.animatedDonkeyBaby)));
        }
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedHorse,
                () -> LayerDefinition.create(FamiliarHorseModel.createAnimatedBodyMesh(CubeDeformation.NONE), 64, 64));
        context.accept(this.animatedHorseArmor,
                () -> LayerDefinition.create(FamiliarHorseModel.createAnimatedBodyMesh(new CubeDeformation(0.1F)),
                        64,
                        64));
        context.accept(this.animatedDonkey, FamiliarDonkeyModel::createAnimatedBodyLayer);
        context.accept(this.animatedHorseBaby,
                () -> LayerDefinition.create(FamiliarHorseModel.createAnimatedBodyMesh(CubeDeformation.NONE), 64, 64));
        context.accept(this.animatedHorseBabyArmor,
                () -> LayerDefinition.create(FamiliarHorseModel.createAnimatedBodyMesh(new CubeDeformation(0.1F)),
                        64,
                        64));
        context.accept(this.animatedDonkeyBaby, FamiliarDonkeyModel::createAnimatedBodyLayer);
    }
}

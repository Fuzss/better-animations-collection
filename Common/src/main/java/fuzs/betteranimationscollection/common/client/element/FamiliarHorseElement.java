package fuzs.betteranimationscollection.common.client.element;

import fuzs.betteranimationscollection.common.client.model.FamiliarDonkeyModel;
import fuzs.betteranimationscollection.common.client.model.FamiliarEquineSaddleModel;
import fuzs.betteranimationscollection.common.client.model.FamiliarHorseModel;
import fuzs.puzzleslib.common.api.client.core.v1.context.LayerDefinitionsContext;
import net.minecraft.client.model.animal.equine.DonkeyModel;
import net.minecraft.client.model.animal.equine.EquineSaddleModel;
import net.minecraft.client.model.animal.equine.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.DonkeyRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
import net.minecraft.client.renderer.entity.state.HorseRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;

public class FamiliarHorseElement extends ModelElement {
    private final ModelLayerLocation animatedHorse;
    private final ModelLayerLocation animatedHorseSaddle;
    private final ModelLayerLocation animatedHorseArmor;
    private final ModelLayerLocation animatedDonkey;
    private final ModelLayerLocation animatedDonkeySaddle;
    private final ModelLayerLocation animatedMule;
    private final ModelLayerLocation animatedMuleSaddle;

    public FamiliarHorseElement() {
        this.animatedHorse = this.registerModelLayer("animated_horse");
        this.animatedHorseSaddle = this.registerModelLayer("animated_horse", "saddle");
        this.animatedHorseArmor = this.registerModelLayer("animated_horse", "armor");
        this.animatedDonkey = this.registerModelLayer("animated_donkey");
        this.animatedDonkeySaddle = this.registerModelLayer("animated_donkey", "saddle");
        this.animatedMule = this.registerModelLayer("animated_mule");
        this.animatedMuleSaddle = this.registerModelLayer("animated_mule", "saddle");
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
        // Only support horses, donkeys, and mules; otherwise this becomes needlessly complex.
        if (entityRenderer.getModel().getClass() == HorseModel.class) {
            if (entityRenderer instanceof HorseRenderer horseRenderer) {
                this.applyHorseAnimations(context, horseRenderer);
            }
        } else if (entityRenderer.getModel().getClass() == DonkeyModel.class) {
            if (entityRenderer instanceof DonkeyRenderer<?> donkeyRenderer) {
                if (donkeyRenderer.adultTexture == DonkeyRenderer.Type.MULE.texture
                        && donkeyRenderer.babyTexture == DonkeyRenderer.Type.MULE_BABY.texture) {
                    this.applyMuleAnimations(context, donkeyRenderer);
                } else {
                    this.applyDonkeyAnimations(context, donkeyRenderer);
                }
            }
        }
    }

    private void applyHorseAnimations(EntityRendererProvider.Context context, HorseRenderer horseRenderer) {
        this.setAnimatedAgeableModel(horseRenderer, new FamiliarHorseModel(context.bakeLayer(this.animatedHorse)));
        this.applyLayerAnimation(horseRenderer, context, (RenderLayer<HorseRenderState, HorseModel> renderLayer) -> {
            if (renderLayer instanceof SimpleEquipmentLayer<HorseRenderState, HorseModel, ?> equipmentLayer
                    && equipmentLayer.layer == EquipmentClientInfo.LayerType.HORSE_BODY) {
                ((SimpleEquipmentLayer<HorseRenderState, HorseModel, HorseModel>) renderLayer).adultModel = new FamiliarHorseModel(
                        context.bakeLayer(this.animatedHorseArmor));
                return equipmentLayer;
            } else if (renderLayer instanceof SimpleEquipmentLayer<HorseRenderState, HorseModel, ?> equipmentLayer
                    && equipmentLayer.layer == EquipmentClientInfo.LayerType.HORSE_SADDLE) {
                ((SimpleEquipmentLayer<HorseRenderState, HorseModel, EquineSaddleModel>) renderLayer).adultModel = new FamiliarEquineSaddleModel(
                        context.bakeLayer(this.animatedHorseSaddle));
                return equipmentLayer;
            } else {
                return null;
            }
        });
    }

    private void applyDonkeyAnimations(EntityRendererProvider.Context context, DonkeyRenderer<?> donkeyRenderer) {
        this.applyEquineAnimations(context,
                donkeyRenderer,
                this.animatedDonkey,
                this.animatedDonkeySaddle,
                EquipmentClientInfo.LayerType.DONKEY_SADDLE);
    }

    private void applyMuleAnimations(EntityRendererProvider.Context context, DonkeyRenderer<?> donkeyRenderer) {
        this.applyEquineAnimations(context,
                donkeyRenderer,
                this.animatedMule,
                this.animatedMuleSaddle,
                EquipmentClientInfo.LayerType.MULE_SADDLE);
    }

    private void applyEquineAnimations(EntityRendererProvider.Context context, DonkeyRenderer<?> donkeyRenderer, ModelLayerLocation modelLayer, ModelLayerLocation saddleLayer, EquipmentClientInfo.LayerType layerType) {
        this.setAnimatedAgeableModel(donkeyRenderer, new FamiliarDonkeyModel(context.bakeLayer(modelLayer)));
        this.applyLayerAnimation(donkeyRenderer, context, (RenderLayer<DonkeyRenderState, DonkeyModel> renderLayer) -> {
            if (renderLayer instanceof SimpleEquipmentLayer<DonkeyRenderState, DonkeyModel, ?> equipmentLayer
                    && equipmentLayer.layer == layerType) {
                ((SimpleEquipmentLayer<DonkeyRenderState, DonkeyModel, EquineSaddleModel>) renderLayer).adultModel = new FamiliarEquineSaddleModel(
                        context.bakeLayer(saddleLayer));
                return equipmentLayer;
            } else {
                return null;
            }
        });
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedHorse,
                () -> FamiliarHorseModel.createAnimatedBodyLayer(CubeDeformation.NONE, 1.1F));
        context.registerLayerDefinition(this.animatedHorseSaddle,
                () -> FamiliarHorseModel.createAnimatedSaddleLayer(1.1F));
        context.registerLayerDefinition(this.animatedHorseArmor,
                () -> FamiliarHorseModel.createAnimatedBodyLayer(new CubeDeformation(0.1F), 1.1F));
        context.registerLayerDefinition(this.animatedDonkey, () -> FamiliarDonkeyModel.createAnimatedBodyLayer(0.87F));
        context.registerLayerDefinition(this.animatedDonkeySaddle,
                () -> FamiliarDonkeyModel.createAnimatedSaddleLayer(0.87F));
        context.registerLayerDefinition(this.animatedMule, () -> FamiliarDonkeyModel.createAnimatedBodyLayer(0.92F));
        context.registerLayerDefinition(this.animatedMuleSaddle,
                () -> FamiliarDonkeyModel.createAnimatedSaddleLayer(0.92F));
    }
}

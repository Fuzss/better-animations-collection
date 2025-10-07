package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.FamiliarDonkeyModel;
import fuzs.betteranimationscollection.client.model.FamiliarEquineSaddleModel;
import fuzs.betteranimationscollection.client.model.FamiliarHorseModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import net.minecraft.client.model.DonkeyModel;
import net.minecraft.client.model.EquineSaddleModel;
import net.minecraft.client.model.HorseModel;
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
    private final ModelLayerLocation animatedHorseBaby;
    private final ModelLayerLocation animatedHorseBabySaddle;
    private final ModelLayerLocation animatedHorseBabyArmor;
    private final ModelLayerLocation animatedDonkeyBaby;
    private final ModelLayerLocation animatedDonkeyBabySaddle;
    private final ModelLayerLocation animatedMuleBaby;
    private final ModelLayerLocation animatedMuleBabySaddle;

    public FamiliarHorseElement() {
        this.animatedHorse = this.registerModelLayer("animated_horse");
        this.animatedHorseSaddle = this.registerModelLayer("animated_horse", "saddle");
        this.animatedHorseArmor = this.registerModelLayer("animated_horse", "armor");
        this.animatedDonkey = this.registerModelLayer("animated_donkey");
        this.animatedDonkeySaddle = this.registerModelLayer("animated_donkey", "saddle");
        this.animatedMule = this.registerModelLayer("animated_mule");
        this.animatedMuleSaddle = this.registerModelLayer("animated_mule", "saddle");
        this.animatedHorseBaby = this.registerModelLayer("animated_horse_baby");
        this.animatedHorseBabySaddle = this.registerModelLayer("animated_horse_baby", "saddle");
        this.animatedHorseBabyArmor = this.registerModelLayer("animated_horse_baby", "armor");
        this.animatedDonkeyBaby = this.registerModelLayer("animated_donkey_baby");
        this.animatedDonkeyBabySaddle = this.registerModelLayer("animated_donkey_baby", "saddle");
        this.animatedMuleBaby = this.registerModelLayer("animated_mule_baby");
        this.animatedMuleBabySaddle = this.registerModelLayer("animated_mule_baby", "saddle");
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
        // only support horses, donkeys, and mules; otherwise this becomes needlessly complex
        if (entityRenderer.getModel().getClass() == HorseModel.class && entityRenderer instanceof HorseRenderer) {
            setAnimatedAgeableModel((LivingEntityRenderer<?, HorseRenderState, HorseModel>) entityRenderer,
                    new FamiliarHorseModel(context.bakeLayer(this.animatedHorse)),
                    new FamiliarHorseModel(context.bakeLayer(this.animatedHorseBaby)));
            applyLayerAnimation((LivingEntityRenderer<?, HorseRenderState, HorseModel>) entityRenderer,
                    context,
                    (RenderLayer<HorseRenderState, HorseModel> renderLayer) -> {
                        if (renderLayer instanceof SimpleEquipmentLayer<HorseRenderState, HorseModel, ?> equipmentLayer
                                && equipmentLayer.layer == EquipmentClientInfo.LayerType.HORSE_BODY) {
                            ((SimpleEquipmentLayer<HorseRenderState, HorseModel, HorseModel>) renderLayer).adultModel = new FamiliarHorseModel(
                                    context.bakeLayer(this.animatedHorseArmor));
                            ((SimpleEquipmentLayer<HorseRenderState, HorseModel, HorseModel>) renderLayer).babyModel = new FamiliarHorseModel(
                                    context.bakeLayer(this.animatedHorseBabyArmor));
                            return equipmentLayer;
                        } else if (
                                renderLayer instanceof SimpleEquipmentLayer<HorseRenderState, HorseModel, ?> equipmentLayer
                                        && equipmentLayer.layer == EquipmentClientInfo.LayerType.HORSE_SADDLE) {
                            ((SimpleEquipmentLayer<HorseRenderState, HorseModel, EquineSaddleModel>) renderLayer).adultModel = new FamiliarEquineSaddleModel(
                                    context.bakeLayer(this.animatedHorseSaddle));
                            ((SimpleEquipmentLayer<HorseRenderState, HorseModel, EquineSaddleModel>) renderLayer).babyModel = new FamiliarEquineSaddleModel(
                                    context.bakeLayer(this.animatedHorseBabySaddle));
                            return equipmentLayer;
                        } else {
                            return null;
                        }
                    });
        } else if (entityRenderer.getModel().getClass() == DonkeyModel.class
                && entityRenderer instanceof DonkeyRenderer<?> donkeyRenderer) {
            if (donkeyRenderer.texture == DonkeyRenderer.Type.MULE.texture) {
                setAnimatedAgeableModel((LivingEntityRenderer<?, DonkeyRenderState, DonkeyModel>) entityRenderer,
                        new FamiliarDonkeyModel(context.bakeLayer(this.animatedMule)),
                        new FamiliarDonkeyModel(context.bakeLayer(this.animatedMuleBaby)));
                applyLayerAnimation((LivingEntityRenderer<?, DonkeyRenderState, DonkeyModel>) entityRenderer,
                        context,
                        (RenderLayer<DonkeyRenderState, DonkeyModel> renderLayer) -> {
                            if (renderLayer instanceof SimpleEquipmentLayer<DonkeyRenderState, DonkeyModel, ?> equipmentLayer
                                    && equipmentLayer.layer == EquipmentClientInfo.LayerType.MULE_SADDLE) {
                                ((SimpleEquipmentLayer<DonkeyRenderState, DonkeyModel, EquineSaddleModel>) renderLayer).adultModel = new FamiliarEquineSaddleModel(
                                        context.bakeLayer(this.animatedMuleSaddle));
                                ((SimpleEquipmentLayer<DonkeyRenderState, DonkeyModel, EquineSaddleModel>) renderLayer).babyModel = new FamiliarEquineSaddleModel(
                                        context.bakeLayer(this.animatedMuleBabySaddle));
                                return equipmentLayer;
                            } else {
                                return null;
                            }
                        });
            } else {
                setAnimatedAgeableModel((LivingEntityRenderer<?, DonkeyRenderState, DonkeyModel>) entityRenderer,
                        new FamiliarDonkeyModel(context.bakeLayer(this.animatedDonkey)),
                        new FamiliarDonkeyModel(context.bakeLayer(this.animatedDonkeyBaby)));
                applyLayerAnimation((LivingEntityRenderer<?, DonkeyRenderState, DonkeyModel>) entityRenderer,
                        context,
                        (RenderLayer<DonkeyRenderState, DonkeyModel> renderLayer) -> {
                            if (renderLayer instanceof SimpleEquipmentLayer<DonkeyRenderState, DonkeyModel, ?> equipmentLayer
                                    && equipmentLayer.layer == EquipmentClientInfo.LayerType.DONKEY_SADDLE) {
                                ((SimpleEquipmentLayer<DonkeyRenderState, DonkeyModel, EquineSaddleModel>) renderLayer).adultModel = new FamiliarEquineSaddleModel(
                                        context.bakeLayer(this.animatedDonkeySaddle));
                                ((SimpleEquipmentLayer<DonkeyRenderState, DonkeyModel, EquineSaddleModel>) renderLayer).babyModel = new FamiliarEquineSaddleModel(
                                        context.bakeLayer(this.animatedDonkeyBabySaddle));
                                return equipmentLayer;
                            } else {
                                return null;
                            }
                        });
            }
        }
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        CubeDeformation armorDeformation = new CubeDeformation(0.1F);
        float horseScale = 1.1F;
        float donkeyScale = 0.87F;
        float muleScale = 0.92F;
        context.registerLayerDefinition(this.animatedHorse,
                () -> FamiliarHorseModel.createAnimatedBodyLayer(CubeDeformation.NONE, horseScale, false));
        context.registerLayerDefinition(this.animatedHorseSaddle,
                () -> FamiliarHorseModel.createAnimatedSaddleLayer(horseScale, false));
        context.registerLayerDefinition(this.animatedHorseArmor,
                () -> FamiliarHorseModel.createAnimatedBodyLayer(armorDeformation, horseScale, false));
        context.registerLayerDefinition(this.animatedDonkey,
                () -> FamiliarDonkeyModel.createAnimatedBodyLayer(donkeyScale, false));
        context.registerLayerDefinition(this.animatedDonkeySaddle,
                () -> FamiliarDonkeyModel.createAnimatedSaddleLayer(donkeyScale, false));
        context.registerLayerDefinition(this.animatedMule,
                () -> FamiliarDonkeyModel.createAnimatedBodyLayer(muleScale, false));
        context.registerLayerDefinition(this.animatedMuleSaddle,
                () -> FamiliarDonkeyModel.createAnimatedSaddleLayer(muleScale, false));
        context.registerLayerDefinition(this.animatedHorseBaby,
                () -> FamiliarHorseModel.createAnimatedBodyLayer(CubeDeformation.NONE, horseScale, true));
        context.registerLayerDefinition(this.animatedHorseBabySaddle,
                () -> FamiliarHorseModel.createAnimatedSaddleLayer(horseScale, true));
        context.registerLayerDefinition(this.animatedHorseBabyArmor,
                () -> FamiliarHorseModel.createAnimatedBodyLayer(armorDeformation, horseScale, true));
        context.registerLayerDefinition(this.animatedDonkeyBaby,
                () -> FamiliarDonkeyModel.createAnimatedBodyLayer(donkeyScale, true));
        context.registerLayerDefinition(this.animatedDonkeyBabySaddle,
                () -> FamiliarDonkeyModel.createAnimatedSaddleLayer(donkeyScale, true));
        context.registerLayerDefinition(this.animatedMuleBaby,
                () -> FamiliarDonkeyModel.createAnimatedBodyLayer(muleScale, true));
        context.registerLayerDefinition(this.animatedMuleBabySaddle,
                () -> FamiliarDonkeyModel.createAnimatedSaddleLayer(muleScale, true));
    }
}

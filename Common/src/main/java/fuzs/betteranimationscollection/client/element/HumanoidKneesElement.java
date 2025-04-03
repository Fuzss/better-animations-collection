package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.DrownedKneesModel;
import fuzs.betteranimationscollection.client.model.HumanoidKneesModel;
import fuzs.betteranimationscollection.client.model.PiglinKneesModel;
import fuzs.betteranimationscollection.client.model.ZombieKneesModel;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.PiglinRenderState;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class HumanoidKneesElement extends ModelElement {
    private final ModelLayerLocation animatedZombie;
    private final ModelLayerLocation animatedZombieInnerArmor;
    private final ModelLayerLocation animatedZombieOuterArmor;
    private final ModelLayerLocation animatedZombieBaby;
    private final ModelLayerLocation animatedZombieBabyInnerArmor;
    private final ModelLayerLocation animatedZombieBabyOuterArmor;
    private final ModelLayerLocation animatedDrowned;
    private final ModelLayerLocation animatedDrownedOuterLayer;
    private final ModelLayerLocation animatedDrownedInnerArmor;
    private final ModelLayerLocation animatedDrownedOuterArmor;
    private final ModelLayerLocation animatedDrownedBaby;
    private final ModelLayerLocation animatedDrownedBabyOuterLayer;
    private final ModelLayerLocation animatedDrownedBabyInnerArmor;
    private final ModelLayerLocation animatedDrownedBabyOuterArmor;
    private final ModelLayerLocation animatedPiglin;
    private final ModelLayerLocation animatedPiglinInnerArmor;
    private final ModelLayerLocation animatedPiglinOuterArmor;
    private final ModelLayerLocation animatedPiglinBaby;
    private final ModelLayerLocation animatedPiglinBabyInnerArmor;
    private final ModelLayerLocation animatedPiglinBabyOuterArmor;

    public HumanoidKneesElement() {
        this.animatedZombie = this.registerModelLayer("animated_zombie");
        this.animatedZombieInnerArmor = this.registerInnerArmorModelLayer("animated_zombie");
        this.animatedZombieOuterArmor = this.registerOuterArmorModelLayer("animated_zombie");
        this.animatedZombieBaby = this.registerModelLayer("animated_zombie_baby");
        this.animatedZombieBabyInnerArmor = this.registerInnerArmorModelLayer("animated_zombie_baby");
        this.animatedZombieBabyOuterArmor = this.registerOuterArmorModelLayer("animated_zombie_baby");
        this.animatedDrowned = this.registerModelLayer("animated_drowned");
        this.animatedDrownedOuterLayer = this.registerModelLayer("animated_drowned", "outer");
        this.animatedDrownedInnerArmor = this.registerInnerArmorModelLayer("animated_drowned");
        this.animatedDrownedOuterArmor = this.registerOuterArmorModelLayer("animated_drowned");
        this.animatedDrownedBaby = this.registerModelLayer("animated_drowned_baby");
        this.animatedDrownedBabyOuterLayer = this.registerModelLayer("animated_drowned_baby", "outer");
        this.animatedDrownedBabyInnerArmor = this.registerInnerArmorModelLayer("animated_drowned_baby");
        this.animatedDrownedBabyOuterArmor = this.registerOuterArmorModelLayer("animated_drowned_baby");
        this.animatedPiglin = this.registerModelLayer("animated_piglin");
        this.animatedPiglinInnerArmor = this.registerInnerArmorModelLayer("animated_piglin");
        this.animatedPiglinOuterArmor = this.registerOuterArmorModelLayer("animated_piglin");
        this.animatedPiglinBaby = this.registerModelLayer("animated_piglin_baby");
        this.animatedPiglinBabyInnerArmor = this.registerInnerArmorModelLayer("animated_piglin_baby");
        this.animatedPiglinBabyOuterArmor = this.registerOuterArmorModelLayer("animated_piglin_baby");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "This one makes the knees of zombie-like and piglin-like mobs bend when they walk around.",
                "Looks pretty fluid and nice. You'll like it, trust me."
        };
    }

    @Override
    protected void applyModelAnimations(LivingEntityRenderer<?, ?, ?> entityRenderer, EntityRendererProvider.Context context) {
        if (entityRenderer.getModel().getClass() == ZombieModel.class) {
            setAnimatedAgeableModel((LivingEntityRenderer<?, ZombieRenderState, ZombieModel<ZombieRenderState>>) entityRenderer,
                    new ZombieKneesModel(context.bakeLayer(this.animatedZombie)),
                    new ZombieKneesModel(context.bakeLayer(this.animatedZombieBaby)));
            applyLayerAnimation((LivingEntityRenderer<?, ZombieRenderState, ZombieModel<ZombieRenderState>>) entityRenderer,
                    context,
                    (RenderLayer<ZombieRenderState, ZombieModel<ZombieRenderState>> renderLayer) -> {
                        if (renderLayer instanceof HumanoidArmorLayer<?, ?, ?>) {
                            return new HumanoidArmorLayer<>((LivingEntityRenderer<?, ZombieRenderState, ZombieModel<ZombieRenderState>>) entityRenderer,
                                    new ZombieKneesModel(context.bakeLayer(this.animatedZombieInnerArmor)),
                                    new ZombieKneesModel(context.bakeLayer(this.animatedZombieOuterArmor)),
                                    new ZombieKneesModel(context.bakeLayer(this.animatedZombieBabyInnerArmor)),
                                    new ZombieKneesModel(context.bakeLayer(this.animatedZombieBabyOuterArmor)),
                                    context.getEquipmentRenderer());
                        } else {
                            return null;
                        }
                    });
        }
        if (entityRenderer.getModel().getClass() == DrownedModel.class) {
            setAnimatedAgeableModel((LivingEntityRenderer<?, ZombieRenderState, DrownedModel>) entityRenderer,
                    new DrownedKneesModel(context.bakeLayer(this.animatedDrowned)),
                    new DrownedKneesModel(context.bakeLayer(this.animatedDrownedBaby)));
            applyLayerAnimation((LivingEntityRenderer<?, ZombieRenderState, DrownedModel>) entityRenderer,
                    context,
                    (RenderLayer<ZombieRenderState, DrownedModel> renderLayer) -> {
                        if (renderLayer instanceof HumanoidArmorLayer<?, ?, ?>) {
                            return new HumanoidArmorLayer<>((LivingEntityRenderer<?, ZombieRenderState, DrownedModel>) entityRenderer,
                                    new DrownedKneesModel(context.bakeLayer(this.animatedDrownedInnerArmor)),
                                    new DrownedKneesModel(context.bakeLayer(this.animatedDrownedOuterArmor)),
                                    new DrownedKneesModel(context.bakeLayer(this.animatedDrownedBabyInnerArmor)),
                                    new DrownedKneesModel(context.bakeLayer(this.animatedDrownedBabyOuterArmor)),
                                    context.getEquipmentRenderer());
                        } else {
                            return null;
                        }
                    });
            applyLayerAnimation((LivingEntityRenderer<?, ZombieRenderState, DrownedModel>) entityRenderer,
                    context,
                    (RenderLayer<ZombieRenderState, DrownedModel> renderLayer) -> {
                        if (renderLayer instanceof DrownedOuterLayer drownedOuterLayer) {
                            drownedOuterLayer.model = new DrownedKneesModel(context.bakeLayer(this.animatedDrownedOuterLayer));
                            drownedOuterLayer.babyModel = new DrownedKneesModel(context.bakeLayer(this.animatedDrownedBabyOuterLayer));
                            return renderLayer;
                        } else {
                            return null;
                        }
                    });
        }
        if (entityRenderer.getModel().getClass() == PiglinModel.class) {
            setAnimatedAgeableModel((LivingEntityRenderer<?, PiglinRenderState, PiglinModel>) entityRenderer,
                    new PiglinKneesModel(context.bakeLayer(this.animatedPiglin)),
                    new PiglinKneesModel(context.bakeLayer(this.animatedPiglinBaby)));
            applyLayerAnimation((LivingEntityRenderer<?, HumanoidRenderState, HumanoidModel<HumanoidRenderState>>) entityRenderer,
                    context,
                    (RenderLayer<HumanoidRenderState, HumanoidModel<HumanoidRenderState>> renderLayer) -> {
                        if (renderLayer instanceof HumanoidArmorLayer<?, ?, ?>) {
                            return new HumanoidArmorLayer<>((LivingEntityRenderer<?, HumanoidRenderState, HumanoidModel<HumanoidRenderState>>) entityRenderer,
                                    new HumanoidKneesModel(context.bakeLayer(this.animatedPiglinInnerArmor)),
                                    new HumanoidKneesModel(context.bakeLayer(this.animatedPiglinOuterArmor)),
                                    new HumanoidKneesModel(context.bakeLayer(this.animatedPiglinBabyInnerArmor)),
                                    new HumanoidKneesModel(context.bakeLayer(this.animatedPiglinBabyOuterArmor)),
                                    context.getEquipmentRenderer());
                        } else {
                            return null;
                        }
                    });
        }
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedZombie,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(CubeDeformation.NONE, 0.0F),
                        64,
                        64));
        context.accept(this.animatedZombieInnerArmor,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F),
                        64,
                        32));
        context.accept(this.animatedZombieOuterArmor,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F),
                        64,
                        32));
        context.accept(this.animatedZombieBaby,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(CubeDeformation.NONE, 0.0F), 64, 64)
                        .apply(HumanoidModel.BABY_TRANSFORMER));
        context.accept(this.animatedZombieBabyInnerArmor,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F),
                        64,
                        32).apply(HumanoidModel.BABY_TRANSFORMER));
        context.accept(this.animatedZombieBabyOuterArmor,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F),
                        64,
                        32).apply(HumanoidModel.BABY_TRANSFORMER));
        context.accept(this.animatedDrowned,
                () -> LayerDefinition.create(DrownedKneesModel.createAnimatedMesh(CubeDeformation.NONE, 0.0F), 64, 64));
        context.accept(this.animatedDrownedOuterLayer,
                () -> LayerDefinition.create(DrownedKneesModel.createAnimatedMesh(new CubeDeformation(0.25F), 0.0F),
                        64,
                        64));
        context.accept(this.animatedDrownedInnerArmor,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F),
                        64,
                        32));
        context.accept(this.animatedDrownedOuterArmor,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F),
                        64,
                        32));
        context.accept(this.animatedDrownedBaby,
                () -> LayerDefinition.create(DrownedKneesModel.createAnimatedMesh(CubeDeformation.NONE, 0.0F), 64, 64)
                        .apply(HumanoidModel.BABY_TRANSFORMER));
        context.accept(this.animatedDrownedBabyOuterLayer,
                () -> LayerDefinition.create(DrownedKneesModel.createAnimatedMesh(new CubeDeformation(0.25F), 0.0F),
                        64,
                        64).apply(HumanoidModel.BABY_TRANSFORMER));
        context.accept(this.animatedDrownedBabyInnerArmor,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F),
                        64,
                        32).apply(HumanoidModel.BABY_TRANSFORMER));
        context.accept(this.animatedDrownedBabyOuterArmor,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F),
                        64,
                        32).apply(HumanoidModel.BABY_TRANSFORMER));
        context.accept(this.animatedPiglin, PiglinKneesModel::createAnimatedBodyLayer);
        context.accept(this.animatedPiglinInnerArmor,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F),
                        64,
                        32));
        context.accept(this.animatedPiglinOuterArmor,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(new CubeDeformation(1.02F), 0.0F),
                        64,
                        32));
        context.accept(this.animatedPiglinBaby,
                () -> PiglinKneesModel.createAnimatedBodyLayer().apply(HumanoidModel.BABY_TRANSFORMER));
        context.accept(this.animatedPiglinBabyInnerArmor,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F),
                        64,
                        32).apply(HumanoidModel.BABY_TRANSFORMER));
        context.accept(this.animatedPiglinBabyOuterArmor,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(new CubeDeformation(1.02F), 0.0F),
                        64,
                        32).apply(HumanoidModel.BABY_TRANSFORMER));
    }
}

package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.DrownedKneesModel;
import fuzs.betteranimationscollection.client.model.HumanoidKneesModel;
import fuzs.betteranimationscollection.client.model.PiglinKneesModel;
import fuzs.betteranimationscollection.client.model.ZombieKneesModel;
import fuzs.puzzleslib.common.api.client.core.v1.context.LayerDefinitionsContext;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.monster.piglin.PiglinModel;
import net.minecraft.client.model.monster.zombie.DrownedModel;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.PiglinRenderState;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;

public class HumanoidKneesElement extends ModelElement {
    private final ModelLayerLocation animatedZombie;
    private final ArmorModelSet<ModelLayerLocation> animatedZombieArmor;
    private final ModelLayerLocation animatedDrowned;
    private final ModelLayerLocation animatedDrownedOuterLayer;
    private final ArmorModelSet<ModelLayerLocation> animatedDrownedArmor;
    private final ModelLayerLocation animatedPiglin;
    private final ArmorModelSet<ModelLayerLocation> animatedPiglinArmor;

    public HumanoidKneesElement() {
        this.animatedZombie = this.registerModelLayer("animated_zombie");
        this.animatedZombieArmor = this.registerArmorSet("animated_zombie");
        this.animatedDrowned = this.registerModelLayer("animated_drowned");
        this.animatedDrownedOuterLayer = this.registerModelLayer("animated_drowned", "outer");
        this.animatedDrownedArmor = this.registerArmorSet("animated_drowned");
        this.animatedPiglin = this.registerModelLayer("animated_piglin");
        this.animatedPiglinArmor = this.registerArmorSet("animated_piglin");
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
            setAnimatedAgeableModel((AgeableMobRenderer<?, ZombieRenderState, ZombieModel<ZombieRenderState>>) entityRenderer,
                    new ZombieKneesModel(context.bakeLayer(this.animatedZombie)));
            applyLayerAnimation((LivingEntityRenderer<?, ZombieRenderState, ZombieModel<ZombieRenderState>>) entityRenderer,
                    context,
                    (RenderLayer<ZombieRenderState, ZombieModel<ZombieRenderState>> renderLayer) -> {
                        // TODO temporarily disabled using the animated mesh, as the boots don't work
                        if (false && renderLayer instanceof HumanoidArmorLayer<?, ?, ?>) {
                            return new HumanoidArmorLayer<>((LivingEntityRenderer<?, ZombieRenderState, ZombieModel<ZombieRenderState>>) entityRenderer,
                                    ArmorModelSet.bake(this.animatedZombieArmor,
                                            context.getModelSet(),
                                            ZombieKneesModel::new),
                                    context.getEquipmentRenderer());
                        } else {
                            return null;
                        }
                    });
        }

        if (entityRenderer.getModel().getClass() == DrownedModel.class) {
            setAnimatedAgeableModel((AgeableMobRenderer<?, ZombieRenderState, DrownedModel>) entityRenderer,
                    new DrownedKneesModel(context.bakeLayer(this.animatedDrowned)));
            applyLayerAnimation((LivingEntityRenderer<?, ZombieRenderState, DrownedModel>) entityRenderer,
                    context,
                    (RenderLayer<ZombieRenderState, DrownedModel> renderLayer) -> {
                        // TODO temporarily disabled using the animated mesh, as the boots don't work
                        if (false && renderLayer instanceof HumanoidArmorLayer<?, ?, ?>) {
                            return new HumanoidArmorLayer<>((LivingEntityRenderer<?, ZombieRenderState, DrownedModel>) entityRenderer,
                                    ArmorModelSet.bake(this.animatedDrownedArmor,
                                            context.getModelSet(),
                                            DrownedKneesModel::new),
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
                            return renderLayer;
                        } else {
                            return null;
                        }
                    });
        }

        if (entityRenderer.getModel().getClass() == PiglinModel.class) {
            setAnimatedAgeableModel((AgeableMobRenderer<?, PiglinRenderState, PiglinModel>) entityRenderer,
                    new PiglinKneesModel(context.bakeLayer(this.animatedPiglin)));
            applyLayerAnimation((LivingEntityRenderer<?, HumanoidRenderState, HumanoidModel<HumanoidRenderState>>) entityRenderer,
                    context,
                    (RenderLayer<HumanoidRenderState, HumanoidModel<HumanoidRenderState>> renderLayer) -> {
                        // TODO temporarily disabled using the animated mesh, as the boots don't work
                        if (false && renderLayer instanceof HumanoidArmorLayer<?, ?, ?>) {
                            return new HumanoidArmorLayer<>((LivingEntityRenderer<?, HumanoidRenderState, HumanoidModel<HumanoidRenderState>>) entityRenderer,
                                    ArmorModelSet.bake(this.animatedPiglinArmor,
                                            context.getModelSet(),
                                            HumanoidKneesModel::new),
                                    context.getEquipmentRenderer());
                        } else {
                            return null;
                        }
                    });
        }
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedZombie,
                () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(CubeDeformation.NONE), 64, 64));
        context.registerArmorDefinition(this.animatedZombieArmor,
                () -> HumanoidKneesModel.createArmorLayerSet(LayerDefinitions.INNER_ARMOR_DEFORMATION,
                        LayerDefinitions.OUTER_ARMOR_DEFORMATION));
        context.registerLayerDefinition(this.animatedDrowned,
                () -> LayerDefinition.create(DrownedKneesModel.createAnimatedMesh(CubeDeformation.NONE), 64, 64));
        context.registerLayerDefinition(this.animatedDrownedOuterLayer,
                () -> LayerDefinition.create(DrownedKneesModel.createAnimatedMesh(new CubeDeformation(0.25F)), 64, 64));
        context.registerArmorDefinition(this.animatedDrownedArmor,
                () -> HumanoidKneesModel.createArmorLayerSet(LayerDefinitions.INNER_ARMOR_DEFORMATION,
                        LayerDefinitions.OUTER_ARMOR_DEFORMATION));
        context.registerLayerDefinition(this.animatedPiglin, PiglinKneesModel::createAnimatedBodyLayer);
        context.registerArmorDefinition(this.animatedPiglinArmor,
                () -> HumanoidKneesModel.createArmorLayerSet(LayerDefinitions.INNER_ARMOR_DEFORMATION,
                        new CubeDeformation(1.02F)));
    }
}

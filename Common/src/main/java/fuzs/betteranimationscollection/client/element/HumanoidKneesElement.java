package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.DrownedKneesModel;
import fuzs.betteranimationscollection.client.model.HumanoidKneesModel;
import fuzs.betteranimationscollection.client.model.PiglinKneesModel;
import fuzs.betteranimationscollection.client.model.ZombieKneesModel;
import fuzs.betteranimationscollection.mixin.client.accessor.DrownedOuterLayerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class HumanoidKneesElement extends ModelElement {
    private final ModelLayerLocation animatedZombie;
    private final ModelLayerLocation animatedZombieInnerArmor;
    private final ModelLayerLocation animatedZombieOuterArmor;
    private final ModelLayerLocation animatedDrowned;
    private final ModelLayerLocation animatedDrownedOuterLayer;
    private final ModelLayerLocation animatedDrownedInnerArmor;
    private final ModelLayerLocation animatedDrownedOuterArmor;
    private final ModelLayerLocation animatedPiglin;
    private final ModelLayerLocation animatedPiglinInnerArmor;
    private final ModelLayerLocation animatedPiglinOuterArmor;

    public HumanoidKneesElement(BiFunction<String, String, ModelLayerLocation> factory) {
        this.animatedZombie = factory.apply("animated_zombie", "main");
        this.animatedZombieInnerArmor = factory.apply("animated_zombie", "inner_armor");
        this.animatedZombieOuterArmor = factory.apply("animated_zombie", "outer_armor");
        this.animatedDrowned = factory.apply("animated_drowned", "main");
        this.animatedDrownedOuterLayer = factory.apply("animated_drowned", "outer");
        this.animatedDrownedInnerArmor = factory.apply("animated_drowned", "inner_armor");
        this.animatedDrownedOuterArmor = factory.apply("animated_drowned", "outer_armor");
        this.animatedPiglin = factory.apply("animated_piglin", "main");
        this.animatedPiglinInnerArmor = factory.apply("animated_piglin", "inner_armor");
        this.animatedPiglinOuterArmor = factory.apply("animated_piglin", "outer_armor");
    }

    @Override
    public String[] modelDescription() {
        return new String[]{"This one makes the knees of zombie-like and piglin-like mobs bend when they walk around.",
                "Looks pretty fluid and nice. You'll like it, trust me."};
    }

    @Override
    void onRegisterAnimatedModels(AnimatedModelsContext context, EntityModelBakery bakery) {
        context.<Zombie, ZombieModel<Zombie>>registerAnimatedModel(ZombieModel.class, () -> new ZombieKneesModel<>(bakery.bakeLayer(this.animatedZombie)), (RenderLayerParent<Zombie, ZombieModel<Zombie>> renderLayerParent, RenderLayer<Zombie, ZombieModel<Zombie>> renderLayer) -> {
            if (renderLayer instanceof HumanoidArmorLayer) {
                return Optional.of(new HumanoidArmorLayer<>(renderLayerParent, new ZombieKneesModel<>(bakery.bakeLayer(this.animatedZombieInnerArmor)), new ZombieKneesModel<>(bakery.bakeLayer(this.animatedZombieOuterArmor)), Minecraft.getInstance().getModelManager()));
            }
            return Optional.empty();
        });
        context.<Zombie, DrownedModel<Zombie>>registerAnimatedModel(DrownedModel.class, () -> new DrownedKneesModel<>(bakery.bakeLayer(this.animatedDrowned)), (RenderLayerParent<Zombie, DrownedModel<Zombie>> renderLayerParent, RenderLayer<Zombie, DrownedModel<Zombie>> renderLayer) -> {
            if (renderLayer instanceof HumanoidArmorLayer) {
                return Optional.of(new HumanoidArmorLayer<>(renderLayerParent, new DrownedKneesModel<>(bakery.bakeLayer(this.animatedDrownedInnerArmor)), new DrownedKneesModel<>(bakery.bakeLayer(this.animatedDrownedOuterArmor)), Minecraft.getInstance().getModelManager()));
            }
            if (renderLayer instanceof DrownedOuterLayer) {
                ((DrownedOuterLayerAccessor<?>) renderLayer).setModel(new DrownedKneesModel<>(bakery.bakeLayer(this.animatedDrownedOuterLayer)));
            }
            return Optional.empty();
        });
        context.<Mob, PiglinModel<Mob>>registerAnimatedModel(PiglinModel.class, () -> new PiglinKneesModel<>(bakery.bakeLayer(this.animatedPiglin)), (RenderLayerParent<Mob, PiglinModel<Mob>> renderLayerParent, RenderLayer<Mob, PiglinModel<Mob>> renderLayer) -> {
            if (renderLayer instanceof HumanoidArmorLayer) {
                return Optional.of(new HumanoidArmorLayer<>(renderLayerParent, new HumanoidKneesModel<>(bakery.bakeLayer(this.animatedPiglinInnerArmor)), new HumanoidKneesModel<>(bakery.bakeLayer(this.animatedPiglinOuterArmor)), Minecraft.getInstance().getModelManager()));
            }
            return Optional.empty();
        });
    }

    @Override
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedZombie, () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(CubeDeformation.NONE, 0.0F), 64, 64));
        context.accept(this.animatedZombieInnerArmor, () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(new CubeDeformation(0.5F), 0.0F), 64, 32));
        context.accept(this.animatedZombieOuterArmor, () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(new CubeDeformation(1.0F), 0.0F), 64, 32));
        context.accept(this.animatedDrowned, () -> LayerDefinition.create(DrownedKneesModel.createAnimatedMesh(CubeDeformation.NONE, 0.0F), 64, 64));
        context.accept(this.animatedDrownedOuterLayer, () -> LayerDefinition.create(DrownedKneesModel.createAnimatedMesh(new CubeDeformation(0.25F), 0.0F), 64, 64));
        context.accept(this.animatedDrownedInnerArmor, () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(new CubeDeformation(0.5F), 0.0F), 64, 32));
        context.accept(this.animatedDrownedOuterArmor, () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(new CubeDeformation(1.0F), 0.0F), 64, 32));
        context.accept(this.animatedPiglin, PiglinKneesModel::createAnimatedBodyLayer);
        context.accept(this.animatedPiglinInnerArmor, () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(new CubeDeformation(0.5F), 0.0F), 64, 32));
        context.accept(this.animatedPiglinOuterArmor, () -> LayerDefinition.create(HumanoidKneesModel.createAnimatedMesh(new CubeDeformation(1.02F), 0.0F), 64, 32));
    }
}

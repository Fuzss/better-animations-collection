package fuzs.betteranimationscollection.client.element;

import com.google.common.collect.Maps;
import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.betteranimationscollection.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ModelElements {
    public static final Map<ResourceLocation, ModelElement> MODEL_ELEMENTS = Maps.newHashMap();

    static {
        registerModelElement("oinky_pig", OinkyPigElement::new);
        registerModelElement("bucka_bucka_chicken", BuckaChickenElement::new);
        registerModelElement("wiggly_ghast_tentacles", GhastTentaclesElement::new);
        registerModelElement("squiggly_squid_tentacles", SquidTentaclesElement::new);
        registerModelElement("kneeling_sheep", KneelingSheepElement::new);
        registerModelElement("spider_knees", SpiderKneesElement::new);
        registerModelElement("animated_snow_man_stick", SnowGolemStickElement::new);
        registerModelElement("wobbly_cow_udder", CowUdderElement::new);
        registerModelElement("wiggly_iron_golem_nose", IronGolemNoseElement::new);
        registerModelElement("flowy_ocelot_tail", OcelotTailElement::new);
        registerModelElement("curly_cat_tail", CatTailElement::new);
        registerModelElement("wiggly_villager_nose", VillagerNoseElement::new);
        registerModelElement("magma_cube_burger", MagmaCubeBurgerElement::new);
        registerModelElement("jiggly_liquidy_slime", JigglySlimeElement::new);
        registerModelElement("arm_flailing_enderman", FlailingEndermanElement::new);
        registerModelElement("wobbly_creeper", WobblyCreeperElement::new);
        registerModelElement("playful_doggy", PlayfulDoggyElement::new);
        registerModelElement("bending_humanoid_knees", HumanoidKneesElement::new);
        registerModelElement("familiar_horse", FamiliarHorseElement::new);
        registerModelElement("spitful_llama", SpitfulLlamaElement::new);
    }

    private ModelElements() {
        // NO-OP
    }

    private static void registerModelElement(String path, Supplier<ModelElement> factory) {
        MODEL_ELEMENTS.put(BetterAnimationsCollection.id(path), factory.get());
    }

    public static void forEach(Consumer<ModelElement> consumer) {
        MODEL_ELEMENTS.values().forEach(consumer);
    }

    public static void buildAnimatedModels(boolean mustBeChanged) {
        if (!mustBeChanged || MODEL_ELEMENTS.values().stream().anyMatch(ModelElement::markedChanged)) {
            Minecraft.getInstance().reloadResourcePacks();
        }
    }

    @SuppressWarnings("ConstantValue")
    public static void applyAnimatedModels(ResourceManager resourceManager) {
        Minecraft minecraft = Minecraft.getInstance();
        for (Map.Entry<EntityType<?>, EntityRenderer<?, ?>> entityRendererEntry : minecraft.getEntityRenderDispatcher().renderers.entrySet()) {
            if (!BetterAnimationsCollection.CONFIG.get(ClientConfig.class).mobBlacklist.contains(entityRendererEntry.getKey()) &&
                    entityRendererEntry.getValue() instanceof LivingEntityRenderer<?, ?, ?> entityRenderer &&
                    entityRenderer.getModel() != null) {
                EntityRendererProvider.Context context = new EntityRendererProvider.Context(minecraft.getEntityRenderDispatcher(),
                        minecraft.getItemModelResolver(),
                        minecraft.getMapRenderer(),
                        minecraft.getBlockRenderer(),
                        resourceManager,
                        minecraft.getEntityModels(),
                        minecraft.getEntityRenderDispatcher().equipmentAssets,
                        minecraft.font);
                for (Map.Entry<ResourceLocation, ModelElement> modelElementEntry : MODEL_ELEMENTS.entrySet()) {
                    try {
                        modelElementEntry.getValue().onApplyModelAnimations(entityRenderer, context);
                    } catch (Exception exception) {
                        BetterAnimationsCollection.LOGGER.warn("Failed to apply model animations for '{}'",
                                modelElementEntry.getKey(),
                                exception);
                    }
                }
            }
        }
    }
}

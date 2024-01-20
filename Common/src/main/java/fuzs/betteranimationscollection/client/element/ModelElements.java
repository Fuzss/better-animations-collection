package fuzs.betteranimationscollection.client.element;

import com.google.common.collect.Maps;
import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.betteranimationscollection.config.ClientConfig;
import fuzs.betteranimationscollection.mixin.client.accessor.EntityRenderDispatcherAccessor;
import fuzs.betteranimationscollection.mixin.client.accessor.LivingEntityRendererAccessor;
import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ModelElements {
    private static final ModelLayerFactory MODEL_LAYER_REGISTRY = ModelLayerFactory.from(BetterAnimationsCollection.MOD_ID);
    public static final Map<ResourceLocation, ModelElement> MODEL_ELEMENTS = Maps.newHashMap();
    private static final Map<Class<? extends EntityModel<?>>, ModelElement.AnimatedModelData<?, ?>> ANIMATED_MODEL_DATA = Maps.newIdentityHashMap();

    private static boolean allowResourceReloading;

    static {
        register("oinky_pig", OinkyPigElement::new);
        register("bucka_bucka_chicken", BuckaChickenElement::new);
        register("wiggly_ghast_tentacles", GhastTentaclesElement::new);
        register("squiggly_squid_tentacles", SquidTentaclesElement::new);
        register("kneeling_sheep", KneelingSheepElement::new);
        register("spider_knees", SpiderKneesElement::new);
        register("animated_snow_man_stick", SnowGolemStickElement::new);
        register("wobbly_cow_udder", CowUdderElement::new);
        register("wiggly_iron_golem_nose", IronGolemNoseElement::new);
        register("flowy_ocelot_tail", OcelotTailElement::new);
        register("curly_cat_tail", CatTailElement::new);
        register("wiggly_villager_nose", VillagerNoseElement::new);
        register("magma_cube_burger", MagmaCubeBurgerElement::new);
        register("jiggly_liquidy_slime", JigglySlimeElement::new);
        register("arm_flailing_enderman", FlailingEndermanElement::new);
        register("wobbly_creeper", WobblyCreeperElement::new);
        register("playful_doggy", PlayfulDoggyElement::new);
        register("bending_humanoid_knees", HumanoidKneesElement::new);
        register("familiar_horse", FamiliarHorseElement::new);
        register("spitful_llama", SpitfulLlamaElement::new);
    }

    private static void register(String identifier, Function<BiFunction<String, String, ModelLayerLocation>, ModelElement> factory) {
        MODEL_ELEMENTS.put(new ResourceLocation(BetterAnimationsCollection.MOD_ID, identifier), factory.apply(MODEL_LAYER_REGISTRY::register));
    }

    public static Collection<ModelElement> elements() {
        return MODEL_ELEMENTS.values();
    }

    public static void buildAnimatedModels(boolean checkDirty, boolean reloadResourcePacks) {
        if (!checkDirty || MODEL_ELEMENTS.values().stream().anyMatch(ModelElement::isDirty)) {
            ANIMATED_MODEL_DATA.clear();
            ModelElement.AnimatedModelsContext context = new ModelElement.AnimatedModelsContext() {

                @SuppressWarnings("unchecked")
                @Override
                public <T extends LivingEntity, M extends EntityModel<T>> void registerAnimatedModel(Class<? super M> vanillaModelClazz, Supplier<? extends M> animatedModel, ModelElement.LayerTransformer<T, M> layerTransformer) {
                    ANIMATED_MODEL_DATA.put((Class<? extends EntityModel<?>>) vanillaModelClazz, new ModelElement.AnimatedModelData<>(vanillaModelClazz, animatedModel, layerTransformer));
                }
            };
            MODEL_ELEMENTS.values().forEach(element -> {
                element.registerAnimatedModels(context, ModelElement.EntityModelBakery.of(Minecraft.getInstance()::getEntityModels));
            });
            if (reloadResourcePacks && allowResourceReloading) {
                Minecraft.getInstance().reloadResourcePacks();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends LivingEntity, M extends EntityModel<T>> void applyAnimatedModels() {
        for (Map.Entry<EntityType<?>, EntityRenderer<?>> entry : ((EntityRenderDispatcherAccessor) Minecraft.getInstance().getEntityRenderDispatcher()).getRenderers().entrySet()) {
            if (!BetterAnimationsCollection.CONFIG.get(ClientConfig.class).mobBlacklist.contains(entry.getKey()) && entry.getValue() instanceof LivingEntityRenderer<?,?>) {
                RenderLayerParent<T, M> livingRenderer = (LivingEntityRenderer<T, M>) entry.getValue();
                // this shouldn't be null, but some mods do it anyway...
                if (livingRenderer.getModel() == null) continue;
                ModelElement.AnimatedModelData<T, M> animatedModelData = (ModelElement.AnimatedModelData<T, M>) ANIMATED_MODEL_DATA.get(livingRenderer.getModel().getClass());
                if (animatedModelData != null) {
                    ((LivingEntityRendererAccessor<T, M>) livingRenderer).setModel(animatedModelData.animatedModel().get());
                    ListIterator<RenderLayer<T, M>> iterator = ((LivingEntityRendererAccessor<T, M>) livingRenderer).getLayers().listIterator();
                    while (iterator.hasNext()) {
                        animatedModelData.layerTransformer().apply(livingRenderer, iterator.next()).ifPresent(iterator::set);
                    }
                }
            }
        }
        // let vanilla run this once during start-up, afterwards it's ok for us to trigger resource pack reloads as well
        allowResourceReloading = true;
    }
}

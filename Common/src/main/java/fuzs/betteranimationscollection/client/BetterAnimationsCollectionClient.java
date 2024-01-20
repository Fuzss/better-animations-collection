package fuzs.betteranimationscollection.client;

import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.betteranimationscollection.client.element.ModelElements;
import fuzs.betteranimationscollection.client.handler.RemoteSoundHandler;
import fuzs.betteranimationscollection.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.core.v1.context.AddReloadListenersContext;
import fuzs.puzzleslib.api.core.v1.context.ModLifecycleContext;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingEvents;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.Executor;

public class BetterAnimationsCollectionClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        LivingEvents.TICK.register(RemoteSoundHandler.INSTANCE::onLivingTick);
    }

    @Override
    public void onClientSetup(ModLifecycleContext context) {
        ModelElements.buildAnimatedModels(false, false);
        // add this listener later, so it doesn't interfere with initial config loading
        BetterAnimationsCollection.CONFIG.getHolder(ClientConfig.class).accept(() -> ModelElements.buildAnimatedModels(true, true));
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        ModelElements.elements().forEach(element -> element.onRegisterLayerDefinitions(context::registerLayerDefinition));
    }

    @Override
    public void onRegisterResourcePackReloadListeners(AddReloadListenersContext context) {
        context.registerReloadListener("animated_models", (PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) -> {
            return preparationBarrier.wait(Unit.INSTANCE).thenRunAsync(ModelElements::applyAnimatedModels, executor2);
        });
    }
}

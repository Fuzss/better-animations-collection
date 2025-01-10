package fuzs.betteranimationscollection.client;

import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.betteranimationscollection.client.element.ModelElements;
import fuzs.betteranimationscollection.client.handler.RemoteSoundHandler;
import fuzs.betteranimationscollection.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.event.v1.AddResourcePackReloadListenersCallback;
import fuzs.puzzleslib.api.event.v1.entity.EntityTickEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.util.function.BiConsumer;

public class BetterAnimationsCollectionClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        EntityTickEvents.END.register(RemoteSoundHandler.INSTANCE::onEndEntityTick);
        AddResourcePackReloadListenersCallback.EVENT.register(
                (BiConsumer<ResourceLocation, PreparableReloadListener> consumer) -> {
                    consumer.accept(BetterAnimationsCollection.id("animated_models"),
                            (ResourceManagerReloadListener) (ResourceManager resourceManager) -> {
                                ModelElements.applyAnimatedModels();
                            }
                    );
                });
    }

    @Override
    public void onClientSetup() {
        ModelElements.buildAnimatedModels(false, false);
        // add this listener later, so it doesn't interfere with initial config loading
        BetterAnimationsCollection.CONFIG.getHolder(ClientConfig.class)
                .addCallback(() -> ModelElements.buildAnimatedModels(true, true));
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        ModelElements.elements()
                .forEach(element -> element.onRegisterLayerDefinitions(context::registerLayerDefinition));
    }
}

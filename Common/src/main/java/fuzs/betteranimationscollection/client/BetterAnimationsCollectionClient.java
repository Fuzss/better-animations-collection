package fuzs.betteranimationscollection.client;

import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.betteranimationscollection.client.element.ModelElement;
import fuzs.betteranimationscollection.client.element.ModelElements;
import fuzs.betteranimationscollection.client.handler.RemoteSoundHandler;
import fuzs.betteranimationscollection.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.core.v1.context.ResourcePackReloadListenersContext;
import fuzs.puzzleslib.api.client.event.v1.renderer.ExtractRenderStateCallback;
import fuzs.puzzleslib.api.event.v1.entity.EntityTickEvents;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;

public class BetterAnimationsCollectionClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        EntityTickEvents.END.register(RemoteSoundHandler.INSTANCE::onEndEntityTick);
        ExtractRenderStateCallback.EVENT.register((Entity entity, EntityRenderState renderState, float partialTick) -> {
            ModelElements.forEach((ModelElement modelElement) -> {
                modelElement.onExtractRenderState(entity, renderState, partialTick);
            });
        });
    }

    @Override
    public void onClientSetup() {
        // add this listener later, so it doesn't interfere with the initial config loading
        BetterAnimationsCollection.CONFIG.getHolder(ClientConfig.class)
                .addCallback(() -> ModelElements.buildAnimatedModels(true));
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        ModelElements.forEach((ModelElement modelElement) -> {
            modelElement.onRegisterLayerDefinitions(context);
        });
    }

    @Override
    public void onAddResourcePackReloadListeners(ResourcePackReloadListenersContext context) {
        context.registerReloadListener(BetterAnimationsCollection.id("animated_models"),
                ModelElements::applyAnimatedModels);
    }
}

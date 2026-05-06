package fuzs.betteranimationscollection.fabric.client;

import fuzs.betteranimationscollection.common.BetterAnimationsCollection;
import fuzs.betteranimationscollection.common.client.BetterAnimationsCollectionClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class BetterAnimationsCollectionFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(BetterAnimationsCollection.MOD_ID, BetterAnimationsCollectionClient::new);
    }
}

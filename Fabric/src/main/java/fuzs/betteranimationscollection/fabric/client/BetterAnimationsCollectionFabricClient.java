package fuzs.betteranimationscollection.fabric.client;

import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.betteranimationscollection.client.BetterAnimationsCollectionClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class BetterAnimationsCollectionFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(BetterAnimationsCollection.MOD_ID, BetterAnimationsCollectionClient::new);
    }
}

package fuzs.betteranimationscollection.neoforge.client;

import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.betteranimationscollection.client.BetterAnimationsCollectionClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = BetterAnimationsCollection.MOD_ID, dist = Dist.CLIENT)
public class BetterAnimationsCollectionNeoForgeClient {

    public BetterAnimationsCollectionNeoForgeClient() {
        ClientModConstructor.construct(BetterAnimationsCollection.MOD_ID, BetterAnimationsCollectionClient::new);
    }
}

package fuzs.betteranimationscollection.neoforge.client;

import fuzs.betteranimationscollection.common.BetterAnimationsCollection;
import fuzs.betteranimationscollection.common.client.BetterAnimationsCollectionClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = BetterAnimationsCollection.MOD_ID, dist = Dist.CLIENT)
public class BetterAnimationsCollectionNeoForgeClient {

    public BetterAnimationsCollectionNeoForgeClient() {
        ClientModConstructor.construct(BetterAnimationsCollection.MOD_ID, BetterAnimationsCollectionClient::new);
    }
}

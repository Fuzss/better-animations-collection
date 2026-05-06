package fuzs.betteranimationscollection.fabric;

import fuzs.betteranimationscollection.common.BetterAnimationsCollection;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class BetterAnimationsCollectionFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(BetterAnimationsCollection.MOD_ID, BetterAnimationsCollection::new);
    }
}

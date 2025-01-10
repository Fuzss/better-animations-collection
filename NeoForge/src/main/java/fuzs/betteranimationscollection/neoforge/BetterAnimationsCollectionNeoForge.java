package fuzs.betteranimationscollection.neoforge;

import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(BetterAnimationsCollection.MOD_ID)
public class BetterAnimationsCollectionNeoForge {

    public BetterAnimationsCollectionNeoForge() {
        ModConstructor.construct(BetterAnimationsCollection.MOD_ID, BetterAnimationsCollection::new);
    }
}

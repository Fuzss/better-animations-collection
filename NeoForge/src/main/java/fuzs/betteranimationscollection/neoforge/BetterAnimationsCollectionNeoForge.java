package fuzs.betteranimationscollection.neoforge;

import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(BetterAnimationsCollection.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterAnimationsCollectionNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(BetterAnimationsCollection.MOD_ID, BetterAnimationsCollection::new);
    }
}

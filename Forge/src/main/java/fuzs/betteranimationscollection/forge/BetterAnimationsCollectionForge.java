package fuzs.betteranimationscollection.forge;

import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(BetterAnimationsCollection.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterAnimationsCollectionForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(BetterAnimationsCollection.MOD_ID, BetterAnimationsCollection::new);
    }
}

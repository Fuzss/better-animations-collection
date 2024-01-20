package fuzs.betteranimationscollection.client.element;

import com.google.common.collect.Lists;
import fuzs.betteranimationscollection.client.handler.RemoteSoundHandler;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SoundDetectionElement extends ModelElement {
    private final Class<? extends Mob> mobClazz;
    private final SoundEvent[] sounds;

    public SoundDetectionElement(Class<? extends Mob> mobClazz, SoundEvent... sounds) {
        this.mobClazz = mobClazz;
        this.sounds = sounds;
        // add this here already once in case config reload is not called during start-up (seems to happen on Windows)
        RemoteSoundHandler.INSTANCE.addAmbientSounds(mobClazz, Lists.newArrayList(sounds));
    }

    @Override
    public void setupModelConfig(ForgeConfigSpec.Builder builder, ValueCallback callback) {
        callback.accept(builder.comment("Mob sounds to play a unique animation for.", "Useful for adding support for modded mob variants which have different sounds from their vanilla counterparts.", ConfigDataSet.CONFIG_DESCRIPTION).define("mob_sounds", Stream.of(this.sounds).map(BuiltInRegistries.SOUND_EVENT::getKey)
                .filter(Objects::nonNull)
                .map(ResourceLocation::toString)
                .collect(Collectors.toList())), v -> {
            RemoteSoundHandler.INSTANCE.removeAmbientSounds(this.mobClazz);
            ConfigDataSet<SoundEvent> soundEvents = ConfigDataSet.from(Registries.SOUND_EVENT, v);
            RemoteSoundHandler.INSTANCE.addAmbientSounds(this.mobClazz, soundEvents);
        });
    }
}

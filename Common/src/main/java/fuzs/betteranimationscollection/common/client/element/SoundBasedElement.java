package fuzs.betteranimationscollection.common.client.element;

import com.google.common.base.Predicates;
import fuzs.betteranimationscollection.common.client.handler.RemoteSoundHandler;
import fuzs.puzzleslib.common.api.client.renderer.v1.RenderStateExtraData;
import fuzs.puzzleslib.common.api.config.v3.ValueCallback;
import fuzs.puzzleslib.common.api.config.v3.serialization.ConfigDataSet;
import fuzs.puzzleslib.common.api.config.v3.serialization.KeyedValueProvider;
import fuzs.puzzleslib.common.api.data.v2.tags.AbstractTagAppender;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class SoundBasedElement<T extends Mob, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends SingletonModelElement<T, S, M> {
    public static final ContextKey<Float> AMBIENT_SOUND_TIME_PROPERTY = key("ambient_sound_time");

    private final List<ResourceKey<SoundEvent>> sounds;

    public SoundBasedElement(Class<T> entityClazz, Class<S> renderStateClazz, Class<? extends M> modelClazz, SoundEvent... sounds) {
        super(entityClazz, renderStateClazz, modelClazz);
        this.sounds = Stream.of(sounds)
                .map(BuiltInRegistries.SOUND_EVENT::getResourceKey)
                .<ResourceKey<SoundEvent>>mapMulti(Optional::ifPresent)
                .toList();
        // add this here already once in case config reload is not called during start-up (seems to happen on Windows)
        RemoteSoundHandler.INSTANCE.addAmbientSounds(entityClazz, Arrays.asList(sounds));
    }

    @Override
    protected void extractRenderState(T entity, S renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        // this only works because MobEntity#ambientSoundTime is manually being synced to the client
        RenderStateExtraData.set(renderState,
                AMBIENT_SOUND_TIME_PROPERTY,
                entity.ambientSoundTime + entity.getAmbientSoundInterval() + partialTick);
    }

    @Override
    public void setupModelConfig(ModConfigSpec.Builder builder, ValueCallback callback) {
        AbstractTagAppender<SoundEvent> tag = KeyedValueProvider.<SoundEvent>tags();
        this.sounds.forEach(tag::add);
        callback.accept(builder.comment("Mob sounds to play a unique animation for.",
                        "Useful for adding support for modded mob variants which have different sounds from their vanilla counterparts.",
                        ConfigDataSet.CONFIG_DESCRIPTION)
                .defineList("mob_sounds", tag.asStringList(), () -> "", Predicates.alwaysTrue()), v -> {
            RemoteSoundHandler.INSTANCE.removeAmbientSounds(this.entityClazz);
            ConfigDataSet<SoundEvent> soundEvents = ConfigDataSet.from(Registries.SOUND_EVENT, (List<String>) v);
            RemoteSoundHandler.INSTANCE.addAmbientSounds(this.entityClazz, soundEvents);
        });
    }
}

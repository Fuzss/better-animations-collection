package fuzs.betteranimationscollection.common.client.handler;

import fuzs.betteranimationscollection.common.BetterAnimationsCollection;
import fuzs.betteranimationscollection.common.config.ClientConfig;
import fuzs.puzzleslib.common.api.event.v1.core.EventResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class RemoteSoundHandler {
    public static final RemoteSoundHandler INSTANCE = new RemoteSoundHandler();
    /**
     * max time an animation takes, so we don't confuse our own stuff when resetting {@link Mob#ambientSoundTime}
     */
    private static final int MAX_SOUND_ANIMATION_TIME = 20;

    /**
     * map of entities whose model should do something when they make a certain sound
     */
    private final Map<Identifier, Class<? extends Mob>> ambientSounds = new ConcurrentHashMap<>();
    /**
     * set of entities whose model should do something when they make a noise this is separate from
     * {@link #ambientSounds} to make sure even when no sound is registered (the user probably wants to disable the
     * animation) no animation is played from the client updating the sound time value
     */
    private final Set<Class<? extends Mob>> noisyEntities = ConcurrentHashMap.newKeySet();
    /**
     * set of entities whose model should do something when they are hurt
     */
    private final Set<Class<? extends Mob>> attackableEntities = new HashSet<>();
    private final SoundDetectionListener soundListener = new SoundDetectionListener();

    public EventResult onStartEntityTick(Entity entity) {
        this.soundListener.addListener();
        if (!entity.level().isClientSide() || !(entity instanceof Mob mob)) {
            return EventResult.PASS;
        }

        Stream.concat(this.noisyEntities.stream(), this.attackableEntities.stream())
                .forEach((Class<? extends Mob> clazz) -> {
                    if (clazz.isAssignableFrom(entity.getClass())) {
                        if (mob.ambientSoundTime >= 0) {
                            // prevent ambientSoundTime from reaching values greater than zero, as the client tries to play a sound then, messing up our system
                            // MAX_SOUND_ANIMATION_TIME has to be added so our stuff doesn't trigger
                            // value will be set properly without MAX_SOUND_ANIMATION_TIME in #onPlaySound
                            mob.ambientSoundTime = -mob.getAmbientSoundInterval() + MAX_SOUND_ANIMATION_TIME;
                        }
                    }
                });
        // just do this, so we can handle everything via ambientSoundTime and don't have to bother with hurtDuration as well
        for (Class<? extends Mob> clazz : this.attackableEntities) {
            if (clazz.isAssignableFrom(entity.getClass())) {
                // set this when a mob has just been hurt
                if (mob.hurtDuration > 0 && mob.hurtTime == mob.hurtDuration) {
                    // this is used to play an animation when hurt
                    mob.ambientSoundTime = -mob.getAmbientSoundInterval();
                }
            }
        }

        return EventResult.PASS;
    }

    public void addAmbientSounds(Class<? extends Mob> entityClazz, Collection<SoundEvent> soundEvents) {
        this.noisyEntities.add(entityClazz);
        for (SoundEvent soundEvent : soundEvents) {
            this.ambientSounds.put(soundEvent.location(), entityClazz);
        }
    }

    public void removeAmbientSounds(Class<? extends Mob> entityClazz) {
        this.ambientSounds.values().removeIf(clazz -> clazz.equals(entityClazz));
    }

    public void addAttackableEntity(Class<? extends Mob> entityClazz) {
        this.attackableEntities.add(entityClazz);
    }

    private class SoundDetectionListener implements SoundEventListener {
        private boolean isListening;

        public void addListener() {
            if (!this.isListening) {
                Minecraft.getInstance().getSoundManager().addListener(this);
                this.isListening = true;
            }
        }

        @Override
        public void onPlaySound(SoundInstance sound, WeighedSoundEvents soundEvent, float range) {
            // Check is necessary here, as sounds might be played in some menu when no world has been loaded yet.
            ClientLevel clientLevel = Minecraft.getInstance().level;
            if (clientLevel != null) {
                Class<? extends Mob> entityClazz = RemoteSoundHandler.this.ambientSounds.get(sound.getIdentifier());
                if (entityClazz != null) {
                    // accuracy is 1/8, so we center this and then apply #soundRange
                    Vec3 position = new Vec3(sound.getX() + 0.0625, sound.getY() + 0.0625, sound.getZ() + 0.0625);
                    double detectionRange = BetterAnimationsCollection.CONFIG.get(ClientConfig.class).soundDetectionRange;
                    AABB axisAlignedBB = new AABB(position, position).inflate(detectionRange + 0.0625);
                    List<? extends Mob> entities = clientLevel.getEntitiesOfClass(entityClazz, axisAlignedBB);
                    entities.stream()
                            .min((o1, o2) -> (int) Math.signum(
                                    o1.position().distanceTo(position) - o2.position().distanceTo(position)))
                            .ifPresent(entity -> entity.ambientSoundTime = -entity.getAmbientSoundInterval());
                }
            }
        }
    }
}

package fuzs.betteranimationscollection.client.handler;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fuzs.betteranimationscollection.BetterAnimationsCollection;
import fuzs.betteranimationscollection.config.ClientConfig;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final Map<ResourceLocation, Class<? extends Mob>> ambientSounds = Maps.newConcurrentMap();
    /**
     * set of entities whose model should do something when they make a noise
     * this is separate from {@link #ambientSounds} to make sure even when no sound is registered
     * (the user probably wants to disable the animation) no animation is played from the client updating the sound time value
     */
    private final Set<Class<? extends Mob>> noisyEntities = Sets.newConcurrentHashSet();
    /**
     * set of entities whose model should do something when they are hurt
     */
    private final Set<Class<? extends Mob>> attackableEntities = Sets.newHashSet();
    private final SoundDetectionListener soundListener = new SoundDetectionListener();

    public EventResult onLivingTick(LivingEntity entity) {
        this.soundListener.ensureInitialized();
        if (!entity.level().isClientSide || !(entity instanceof Mob mob)) return EventResult.PASS;
        Stream.concat(this.noisyEntities.stream(), this.attackableEntities.stream()).forEach(clazz -> {
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
            this.ambientSounds.put(soundEvent.getLocation(), entityClazz);
        }
    }

    public void removeAmbientSounds(Class<? extends Mob> entityClazz) {
        this.ambientSounds.values().removeIf(clazz -> clazz.equals(entityClazz));
    }

    public void addAttackableEntity(Class<? extends Mob> entityClazz) {
        this.attackableEntities.add(entityClazz);
    }

    private class SoundDetectionListener implements SoundEventListener {
        private boolean initialized;

        public void ensureInitialized() {
            if (!this.initialized) {
                Minecraft.getInstance().getSoundManager().addListener(this);
                this.initialized = true;
            }
        }

        @Override
        public void onPlaySound(SoundInstance soundIn, WeighedSoundEvents accessor) {
            Level level = Minecraft.getInstance().level;
            // check is actually necessary here, as sounds might be played in some menu when no world has been loaded yet
            if (level == null) return;
            Class<? extends Mob> entityClazz = RemoteSoundHandler.this.ambientSounds.get(soundIn.getLocation());
            if (entityClazz != null) {
                // accuracy is 1/8, so we center this and then apply #soundRange
                Vec3 center = new Vec3(soundIn.getX() + 0.0625, soundIn.getY() + 0.0625, soundIn.getZ() + 0.0625);
                final double soundDetectionRange = BetterAnimationsCollection.CONFIG.get(ClientConfig.class).soundDetectionRange;
                AABB axisAlignedBB = new AABB(center, center).inflate(soundDetectionRange + 0.0625);
                List<? extends Mob> entities = level.getEntitiesOfClass(entityClazz, axisAlignedBB);
                entities.stream().min((o1, o2) -> (int) Math.signum(o1.position().distanceTo(center) - o2.position().distanceTo(center))).ifPresent(entity -> entity.ambientSoundTime = -entity.getAmbientSoundInterval());
            }
        }
    }
}

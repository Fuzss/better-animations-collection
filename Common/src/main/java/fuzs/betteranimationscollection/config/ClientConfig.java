package fuzs.betteranimationscollection.config;

import fuzs.betteranimationscollection.client.element.ModelElement;
import fuzs.betteranimationscollection.client.element.ModelElements;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import fuzs.puzzleslib.api.config.v3.serialization.KeyedValueProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Map;

public class ClientConfig implements ConfigCore {
    @Config(category = "general", name = "mob_blacklist", description = {"Mob variants that shouldn't have any model changes applied to them.", ConfigDataSet.CONFIG_DESCRIPTION})
    List<String> mobBlacklistRaw = KeyedValueProvider.toString(Registries.ENTITY_TYPE);
    @Config(category = "general", description = {"Block range for sound detection system to look for a mob that made a certain sound on the server, so the client may play an animation.", "The client is not sent an exact position, so the mob's location must be estimated."})
    @Config.DoubleRange(min = 0.5, max = 8.0)
    public double soundDetectionRange = 1.5;

    public ConfigDataSet<EntityType<?>> mobBlacklist;

    @Override
    public void addToBuilder(ModConfigSpec.Builder builder, ValueCallback callback) {
        builder.push("models");
        for (Map.Entry<ResourceLocation, ModelElement> entry : ModelElements.MODEL_ELEMENTS.entrySet()) {
            callback.accept(builder.comment(entry.getValue().modelDescription()).define(entry.getKey().getPath(), true), entry.getValue()::setEnabled);
        }
        builder.pop();
        for (Map.Entry<ResourceLocation, ModelElement> entry : ModelElements.MODEL_ELEMENTS.entrySet()) {
            builder.push(entry.getKey().getPath());
            entry.getValue().setupModelConfig(builder, callback);
            builder.pop();
        }
    }

    @Override
    public void afterConfigReload() {
        ConfigDataSet<EntityType<?>> mobBlacklist = ConfigDataSet.from(Registries.ENTITY_TYPE, this.mobBlacklistRaw);
        if (!mobBlacklist.equals(this.mobBlacklist)) {
            boolean initialReload = this.mobBlacklist == null;
            this.mobBlacklist = mobBlacklist;
            if (!initialReload) {
                ModelElements.buildAnimatedModels(false, true);
            }
        }
    }
}

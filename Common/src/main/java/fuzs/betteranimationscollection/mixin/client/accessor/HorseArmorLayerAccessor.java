package fuzs.betteranimationscollection.mixin.client.accessor;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HorseArmorLayer.class)
public interface HorseArmorLayerAccessor {

    @Accessor
    @Mutable
    void setModel(HorseModel<Horse> model);
}

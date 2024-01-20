package fuzs.betteranimationscollection.mixin.client.accessor;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SlimeOuterLayer.class)
public interface SlimeOuterLayerAccessor<T extends LivingEntity> {

    @Accessor
    @Mutable
    void setModel(EntityModel<T> model);
}

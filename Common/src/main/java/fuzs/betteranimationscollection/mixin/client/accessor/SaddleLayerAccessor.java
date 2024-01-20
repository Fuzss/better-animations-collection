package fuzs.betteranimationscollection.mixin.client.accessor;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Saddleable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SaddleLayer.class)
public interface SaddleLayerAccessor<T extends Entity & Saddleable, M extends EntityModel<T>> {

    @Accessor
    @Mutable
    void setModel(M model);
}

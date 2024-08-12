package fuzs.betteranimationscollection.mixin.client.accessor;

import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.world.entity.monster.Drowned;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DrownedOuterLayer.class)
public interface DrownedOuterLayerAccessor<T extends Drowned> {

    @Accessor
    @Mutable
    void setModel(DrownedModel<T> model);
}

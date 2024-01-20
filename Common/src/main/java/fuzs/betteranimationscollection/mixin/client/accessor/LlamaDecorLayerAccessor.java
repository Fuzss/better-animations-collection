package fuzs.betteranimationscollection.mixin.client.accessor;

import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.world.entity.animal.horse.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LlamaDecorLayer.class)
public interface LlamaDecorLayerAccessor {

    @Accessor
    @Mutable
    void setModel(LlamaModel<Llama> model);
}

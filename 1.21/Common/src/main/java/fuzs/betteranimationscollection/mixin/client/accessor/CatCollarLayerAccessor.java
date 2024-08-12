package fuzs.betteranimationscollection.mixin.client.accessor;

import net.minecraft.client.model.CatModel;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CatCollarLayer.class)
public interface CatCollarLayerAccessor {

    @Accessor
    @Mutable
    void setCatModel(CatModel<Cat> catModel);
}

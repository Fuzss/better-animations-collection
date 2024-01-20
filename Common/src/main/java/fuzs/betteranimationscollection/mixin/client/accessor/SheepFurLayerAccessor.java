package fuzs.betteranimationscollection.mixin.client.accessor;

import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SheepFurLayer.class)
public interface SheepFurLayerAccessor {

    @Accessor
    @Mutable
    void setModel(SheepFurModel<Sheep> model);
}

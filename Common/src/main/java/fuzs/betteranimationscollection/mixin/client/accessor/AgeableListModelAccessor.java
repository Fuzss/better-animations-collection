package fuzs.betteranimationscollection.mixin.client.accessor;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AgeableListModel.class)
public interface AgeableListModelAccessor {

    @Accessor
    @Mutable
    void setBabyYHeadOffset(float babyYHeadOffset);

    @Accessor
    @Mutable
    void setBabyZHeadOffset(float babyZHeadOffset);
}

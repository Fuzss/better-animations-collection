package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class VillagerNoseModel<T extends Entity> extends VillagerModel<T> {

    public VillagerNoseModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public void prepareMobModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        if (entitylivingbaseIn instanceof Mob mob) {
            // this only works because MobEntity#ambientSoundTime is manually being synced to the client in {@link fuzs.betteranimationscollection.client.element.SyncSoundElement}
            int soundTime = mob.ambientSoundTime + mob.getAmbientSoundInterval();
            final int maxSoundTime = 20;
            if (0 < soundTime && soundTime < maxSoundTime) {
                float rotation = Mth.sin((float) soundTime * (float) ((3.0F * Math.PI) / 20.0F));
                this.nose.zRot = rotation * 0.75F * ((float) (maxSoundTime - soundTime) / 20.0F);
            } else {
                this.nose.zRot = 0.0F;
            }
        }
    }
}

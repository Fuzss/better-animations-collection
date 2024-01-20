package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class SnowGolemStickModel<T extends Entity> extends SnowGolemModel<T> {
    private final ModelPart leftArm;
    private final ModelPart rightArm;

    public SnowGolemStickModel(ModelPart modelPart) {
        super(modelPart);
        this.leftArm = modelPart.getChild("left_arm");
        this.rightArm = modelPart.getChild("right_arm");
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (entityIn instanceof Mob mob) {
            // this only works because MobEntity#ambientSoundTime is manually being synced to the client in {@link fuzs.betteranimationscollection.client.element.SyncSoundElement}
            int soundTime = mob.ambientSoundTime + mob.getAmbientSoundInterval();
            // makes 5 % of snowman render left-handed, like for most mobs with arms in vanilla
            boolean leftHanded = Math.abs(mob.getUUID().getLeastSignificantBits() % 20) == 0;
            if (0 < soundTime && soundTime < 8) {
                if (leftHanded) {
                    this.leftArm.xRot = 1.5F - (float) soundTime * 1.5F / 8.0F;
                    this.leftArm.yRot += (1.0F - (float) soundTime / 8.0F) * 2.0F;
                } else {
                    this.rightArm.xRot = 1.5F - (float) soundTime * 1.5F / 8.0F;
                    this.rightArm.yRot -= (1.0F - (float) soundTime / 8.0F) * 2.0F;
                }
            } else {
                this.leftArm.xRot = 0.0F;
                this.rightArm.xRot = 0.0F;
            }
        }
    }
}

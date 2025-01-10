package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.SnowGolemStickElement;
import fuzs.betteranimationscollection.client.element.SoundBasedElement;
import fuzs.puzzleslib.api.client.util.v1.RenderPropertyKey;
import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class SnowGolemStickModel extends SnowGolemModel {
    private final ModelPart leftArm;
    private final ModelPart rightArm;

    public SnowGolemStickModel(ModelPart modelPart) {
        super(modelPart);
        this.leftArm = modelPart.getChild("left_arm");
        this.rightArm = modelPart.getChild("right_arm");
    }

    @Override
    public void setupAnim(LivingEntityRenderState renderState) {
        super.setupAnim(renderState);
        float soundTime = RenderPropertyKey.getRenderProperty(renderState,
                SoundBasedElement.AMBIENT_SOUND_TIME_PROPERTY);
        boolean isLeftHanded = RenderPropertyKey.getRenderProperty(renderState,
                SnowGolemStickElement.IS_LEFT_HANDED_PROPERTY);
        if (0.0F < soundTime && soundTime < 8.0F) {
            if (isLeftHanded) {
                this.leftArm.xRot = 1.5F - soundTime * 1.5F / 8.0F;
                this.leftArm.yRot += (1.0F - soundTime / 8.0F) * 2.0F;
            } else {
                this.rightArm.xRot = 1.5F - soundTime * 1.5F / 8.0F;
                this.rightArm.yRot -= (1.0F - soundTime / 8.0F) * 2.0F;
            }
        } else {
            this.leftArm.xRot = 0.0F;
            this.rightArm.xRot = 0.0F;
        }
    }
}

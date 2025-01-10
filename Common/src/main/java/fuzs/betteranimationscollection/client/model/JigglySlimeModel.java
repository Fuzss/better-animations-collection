package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.JigglySlimeElement;
import fuzs.puzzleslib.api.client.util.v1.RenderPropertyKey;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;

public class JigglySlimeModel extends SlimeModel {
    private final ModelPart cube;
    private final ModelPart rightEye;
    private final ModelPart leftEye;
    private final ModelPart mouth;
    private final boolean isSlimeGelLayer;

    public JigglySlimeModel(ModelPart modelPart) {
        super(modelPart);
        this.cube = modelPart.getChild("cube");
        this.isSlimeGelLayer = !modelPart.hasChild("mouth");
        if (!this.isSlimeGelLayer) {
            this.rightEye = modelPart.getChild("right_eye");
            this.leftEye = modelPart.getChild("left_eye");
            this.mouth = modelPart.getChild("mouth");
        } else {
            this.rightEye = null;
            this.leftEye = null;
            this.mouth = null;
        }
    }

    @Override
    public void setupAnim(EntityRenderState renderState) {
        super.setupAnim(renderState);
        float walkAnimationPos = RenderPropertyKey.getRenderProperty(renderState,
                JigglySlimeElement.WALK_ANIMATION_POS_PROPERTY);
        float progress = walkAnimationPos + renderState.ageInTicks * JigglySlimeElement.animationSpeed / 15.0F;
        float magnitude = JigglySlimeElement.animationSpeed / 20.0F;
        float targetSquish = RenderPropertyKey.getRenderProperty(renderState,
                JigglySlimeElement.TARGET_SQUISH_PROPERTY);
        if (targetSquish < 0.0F) {
            magnitude += -targetSquish * 0.5F;
        }
        if (this.isSlimeGelLayer) {
            this.cube.x = Mth.sin(progress * 0.3F) * magnitude * 0.5F;
            this.cube.y = Mth.sin(progress * 0.33F) * magnitude * 0.5F;
            this.cube.z = Mth.sin(progress * 0.375F) * magnitude * 0.25F;
        } else {
            this.rightEye.x = Mth.sin(progress * 0.5F + 0.5F) * magnitude - 0.125F;
            this.rightEye.y = Mth.sin(progress * 0.45F + 1.5F) * magnitude;
            this.rightEye.z = Mth.sin(progress * 0.475F + 2.5F) * magnitude * 0.25F;
            this.leftEye.x = Mth.sin(progress * 0.525F + 1.0F) * magnitude + 0.125F;
            this.leftEye.y = Mth.sin(progress * 0.475F + 3.0F) * magnitude;
            this.leftEye.z = Mth.sin(progress * 0.425F + 2.0F) * magnitude * 0.25F;
            this.mouth.x = Mth.sin(progress * 0.55F + 3.75F) * magnitude;
            this.mouth.y = Mth.sin(progress * 0.625F + 1.75F) * magnitude;
            this.mouth.z = Mth.sin(progress * 0.6F + 2.75F) * magnitude * 0.25F;
            this.cube.x = Mth.sin(progress * 0.4F) * magnitude;
            this.cube.y = Mth.sin(progress * 0.44F) * magnitude;
            this.cube.z = Mth.sin(progress * 0.475F) * magnitude * 0.5F;
        }
    }
}

package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;

public interface KneesModel {

    ModelPart rightShin();

    ModelPart leftShin();

    static <S extends HumanoidRenderState, M extends HumanoidModel<S> & KneesModel> void setupAnim(M model, S renderState) {
        if (renderState.isPassenger) {
            model.rightShin().xRot = 0.6F;
            model.leftShin().xRot = 0.6F;
        } else {
            model.rightShin().xRot = Math.max(0.0F, Mth.sin(renderState.walkAnimationPos * 0.6662F)) * 1.5F *
                    renderState.walkAnimationSpeed / renderState.speedValue;
            model.leftShin().xRot =
                    Math.max(0.0F, Mth.sin(renderState.walkAnimationPos * 0.6662F + (float) Math.PI)) * 1.5F *
                            renderState.walkAnimationSpeed / renderState.speedValue;
        }
    }

    static void copyPropertiesTo(KneesModel model, EntityModel<?> otherModel) {
        if (otherModel instanceof KneesModel otherKneesModel) {
            otherKneesModel.rightShin().copyFrom(model.rightShin());
            otherKneesModel.leftShin().copyFrom(model.leftShin());
        }
    }
}

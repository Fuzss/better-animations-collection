package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public interface KneesModel {

    ModelPart rightShin();

    ModelPart leftShin();

    static <T extends LivingEntity, M extends HumanoidModel<T> & KneesModel> void setupAnim(M model, T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float flightAmplifier = 1.0F;
        if (entityIn.getFallFlyingTicks() > 4) {
            flightAmplifier = (float) entityIn.getDeltaMovement().lengthSqr();
            flightAmplifier = flightAmplifier / 0.2F;
            flightAmplifier = Math.max(1.0F, flightAmplifier * flightAmplifier * flightAmplifier);
        }
        if (model.riding) {
            model.rightShin().xRot = 0.6F;
            model.leftShin().xRot = 0.6F;
        } else {
            model.rightShin().xRot = Math.max(0.0F, Mth.sin(limbSwing * 0.6662F)) * 1.5F * limbSwingAmount / flightAmplifier;
            model.leftShin().xRot = Math.max(0.0F, Mth.sin(limbSwing * 0.6662F + (float) Math.PI)) * 1.5F * limbSwingAmount / flightAmplifier;
        }
    }

    static void copyPropertiesTo(KneesModel model, EntityModel<?> otherModel) {
        if (otherModel instanceof KneesModel otherKneesModel) {
            otherKneesModel.rightShin().copyFrom(model.rightShin());
            otherKneesModel.leftShin().copyFrom(model.leftShin());
        }
    }
}

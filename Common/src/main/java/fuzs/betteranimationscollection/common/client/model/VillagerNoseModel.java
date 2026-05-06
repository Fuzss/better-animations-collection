package fuzs.betteranimationscollection.common.client.model;

import fuzs.betteranimationscollection.common.client.element.SoundBasedElement;
import fuzs.puzzleslib.common.api.client.renderer.v1.RenderStateExtraData;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.npc.VillagerModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.util.Mth;

public class VillagerNoseModel extends VillagerModel {
    private final ModelPart nose;

    public VillagerNoseModel(ModelPart modelPart) {
        super(modelPart);
        this.nose = modelPart.getChild("head").getChild("nose");
    }

    @Override
    public void setupAnim(VillagerRenderState state) {
        super.setupAnim(state);
        animateNose(state, this.nose);
    }

    public static void animateNose(EntityRenderState state, ModelPart nose) {
        float soundTime = RenderStateExtraData.getOrDefault(state, SoundBasedElement.AMBIENT_SOUND_TIME_PROPERTY, 0.0F);
        float maxSoundTime = 20.0F;
        if (0.0F < soundTime && soundTime < maxSoundTime) {
            float rotation = Mth.sin(soundTime * ((3.0F * Mth.PI) / 20.0F));
            nose.zRot = rotation * 0.75F * ((maxSoundTime - soundTime) / 20.0F);
        } else {
            nose.zRot = 0.0F;
        }
    }
}

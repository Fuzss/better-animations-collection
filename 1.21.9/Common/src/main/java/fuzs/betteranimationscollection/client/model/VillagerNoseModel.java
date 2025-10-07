package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.SoundBasedElement;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.util.Mth;

public class VillagerNoseModel extends VillagerModel {
    private final ModelPart nose;

    public VillagerNoseModel(ModelPart modelPart) {
        super(modelPart);
        this.nose = modelPart.getChild("head").getChild("nose");
    }

    @Override
    public void setupAnim(VillagerRenderState renderState) {
        super.setupAnim(renderState);
        float soundTime = RenderStateExtraData.getOrDefault(renderState,
                SoundBasedElement.AMBIENT_SOUND_TIME_PROPERTY,
                0.0F);
        float maxSoundTime = 20.0F;
        if (0.0F < soundTime && soundTime < maxSoundTime) {
            float rotation = Mth.sin(soundTime * ((3.0F * Mth.PI) / 20.0F));
            this.nose.zRot = rotation * 0.75F * ((maxSoundTime - soundTime) / 20.0F);
        } else {
            this.nose.zRot = 0.0F;
        }
    }
}

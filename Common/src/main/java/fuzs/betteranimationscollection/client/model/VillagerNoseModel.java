package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.SoundBasedElement;
import fuzs.puzzleslib.api.client.util.v1.RenderPropertyKey;
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
        int soundTime = RenderPropertyKey.getRenderProperty(renderState, SoundBasedElement.AMBIENT_SOUND_TIME_PROPERTY);
        int maxSoundTime = 20;
        if (0 < soundTime && soundTime < maxSoundTime) {
            float rotation = Mth.sin((float) soundTime * (float) ((3.0F * Math.PI) / 20.0F));
            this.nose.zRot = rotation * 0.75F * ((maxSoundTime - soundTime) / 20.0F);
        } else {
            this.nose.zRot = 0.0F;
        }
    }
}

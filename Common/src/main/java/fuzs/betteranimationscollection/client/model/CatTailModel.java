package fuzs.betteranimationscollection.client.model;

import fuzs.betteranimationscollection.client.element.CatTailElement;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.Cat;

public class CatTailModel<T extends Cat> extends CatModel<T> {
    private final ModelPart tail;
    private final ModelPart[] tailParts;

    public CatTailModel(ModelPart modelPart) {
        super(modelPart);
        this.tail = modelPart.getChild("tail1");
        this.tailParts = OcelotTailModel.getTailParts(this.tail);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // tail1 is still being updated by super class although unused, so we just copy the angles
        this.tail.y = this.tail1.y;
        this.tail.z = this.tail1.z;
        this.tail.xRot = this.tail1.xRot;
        this.tail.yRot = 0.0F;
        if (entityIn.isInSittingPose()) {
            this.tail.yRot = -1.0F;
            for (int i = 0; i < this.tailParts.length; i++) {
                this.tailParts[i].xRot = 0.0F;
                this.tailParts[i].zRot = (15.0F - (float) i) / 50.0F;
                this.tailParts[i].visible = i < CatTailElement.tailLength;
            }
        } else {
            OcelotTailModel.setupTailAnim(this.tail, this.tailParts, limbSwing, limbSwingAmount, ageInTicks, CatTailElement.animationSpeed, CatTailElement.tailLength);
        }
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}

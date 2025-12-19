package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;

public class ZombieKneesModel extends ZombieModel<ZombieRenderState> implements KneesModel {
    private final ModelPart rightShin;
    private final ModelPart leftShin;

    public ZombieKneesModel(ModelPart modelPart) {
        super(modelPart);
        this.rightShin = modelPart.getChild("right_leg").getChild("right_shin");
        this.leftShin = modelPart.getChild("left_leg").getChild("left_shin");
    }

    @Override
    public void setupAnim(ZombieRenderState renderState) {
        super.setupAnim(renderState);
        KneesModel.setupAnim(this, renderState);
    }

    @Override
    public ModelPart rightShin() {
        return this.rightShin;
    }

    @Override
    public ModelPart leftShin() {
        return this.leftShin;
    }
}

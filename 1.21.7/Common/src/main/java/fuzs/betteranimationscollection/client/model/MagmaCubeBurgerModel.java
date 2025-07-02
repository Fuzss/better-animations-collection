package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;

import java.util.Arrays;

public class MagmaCubeBurgerModel extends LavaSlimeModel {
    private final ModelPart insideCube;
    private final ModelPart[] bodyCubes = new ModelPart[8];

    public MagmaCubeBurgerModel(ModelPart modelPart) {
        super(modelPart);
        this.insideCube = modelPart.getChild("inside_cube");
        Arrays.setAll(this.bodyCubes, (int i) -> {
            return modelPart.getChild("cube" + i);
        });
    }

    @Override
    public void setupAnim(SlimeRenderState renderState) {
        super.setupAnim(renderState);
        float deathTime = Math.min(10.0F, renderState.deathTime);
        this.insideCube.visible = deathTime <= 0.0F;
        for (int i = 0; i < this.bodyCubes.length; i++) {
            this.bodyCubes[i].y += (float) (10 - i) * deathTime * 0.325F;
            this.bodyCubes[i].x += deathTime * 2.0F - (deathTime <= 0.0F ? 0.0F : (float) (7 - i) * 0.675F);
            this.bodyCubes[i].zRot = deathTime * 0.195F;
            this.bodyCubes[i].yRot = (float) (5 * i % 7 - 3) * deathTime * 0.05F;
        }
    }
}

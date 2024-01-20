package fuzs.betteranimationscollection.client.model;

import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;

import java.util.Arrays;

public class MagmaCubeBurgerModel<T extends Slime> extends LavaSlimeModel<T> {
    private final ModelPart insideCube;
    private final ModelPart[] bodyCubes = new ModelPart[8];
    
    public MagmaCubeBurgerModel(ModelPart modelPart) {
        super(modelPart);
        this.insideCube = modelPart.getChild("inside_cube");
        Arrays.setAll(this.bodyCubes, (i) -> {
            return modelPart.getChild("cube" + i);
        });
    }

    @Override
    public void prepareMobModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        float deathTime = Math.min(10.0F, entitylivingbaseIn.deathTime);
        this.insideCube.visible = deathTime <= 0.0F;
        float squishFactor = Math.max(0.0F, Mth.lerp(partialTickTime, entitylivingbaseIn.oSquish, entitylivingbaseIn.squish));
        for (int i = 0; i < this.bodyCubes.length; i++) {
            this.bodyCubes[i].y = (float) (-(4 - i)) * squishFactor * 1.7F;
            this.bodyCubes[i].y += (float) (10 - i) * deathTime * 0.325F;
            this.bodyCubes[i].x = deathTime * 2.0F - (deathTime <= 0.0F ? 0.0F : (float) (7 - i) * 0.675F);
            this.bodyCubes[i].zRot = deathTime * 0.195F;
            this.bodyCubes[i].yRot = (float) (5 * i % 7 - 3) * deathTime * 0.05F;
        }
    }
}

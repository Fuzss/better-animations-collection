package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.MagmaCubeBurgerModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.monster.slime.MagmaCubeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.world.entity.monster.MagmaCube;

public class MagmaCubeBurgerElement extends SingletonModelElement<MagmaCube, SlimeRenderState, MagmaCubeModel> {
    private final ModelLayerLocation animatedMagmaCube;

    public MagmaCubeBurgerElement() {
        super(MagmaCube.class, SlimeRenderState.class, MagmaCubeModel.class);
        this.animatedMagmaCube = this.registerModelLayer("animated_magma_cube");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "Adds a custom death animation to magma cubes, which causes their bodies to form into a pile of steamy, delicious hamburger patties when they die.",
                "Unfortunately, you can't eat them because they're way too hot."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, SlimeRenderState, MagmaCubeModel> entityRenderer, EntityRendererProvider.Context context) {
        entityRenderer.model = new MagmaCubeBurgerModel(context.bakeLayer(this.animatedMagmaCube));
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedMagmaCube, MagmaCubeModel::createBodyLayer);
    }
}

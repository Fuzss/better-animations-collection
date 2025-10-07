package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.SpiderKneesModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.monster.Spider;

public class SpiderKneesElement extends SingletonModelElement<Spider, LivingEntityRenderState, SpiderModel> {
    private final ModelLayerLocation animatedSpider;

    public SpiderKneesElement() {
        super(Spider.class, LivingEntityRenderState.class, SpiderModel.class);
        this.animatedSpider = this.registerModelLayer("animated_spider");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "A truly stunning visual addition. Spiders now finally have the knees they've always dreamed of."
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, LivingEntityRenderState, SpiderModel> entityRenderer, EntityRendererProvider.Context context) {
        entityRenderer.model = new SpiderKneesModel(context.bakeLayer(this.animatedSpider));
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedSpider, SpiderKneesModel::createAnimatedSpiderBodyLayer);
    }
}

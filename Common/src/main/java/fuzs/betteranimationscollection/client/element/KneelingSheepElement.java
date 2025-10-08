package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.KneelingSheepFurModel;
import fuzs.betteranimationscollection.client.model.KneelingSheepModel;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.world.entity.animal.sheep.Sheep;
import org.jetbrains.annotations.Nullable;

public class KneelingSheepElement extends SingletonModelElement<Sheep, SheepRenderState, SheepModel> {
    private final ModelLayerLocation animatedSheep;
    private final ModelLayerLocation animatedSheepFur;
    private final ModelLayerLocation animatedSheepBaby;
    private final ModelLayerLocation animatedSheepBabyFur;

    public KneelingSheepElement() {
        super(Sheep.class, SheepRenderState.class, SheepModel.class);
        this.animatedSheep = this.registerModelLayer("animated_sheep");
        this.animatedSheepFur = this.registerModelLayer("animated_sheep", "fur");
        this.animatedSheepBaby = this.registerModelLayer("animated_sheep_baby");
        this.animatedSheepBabyFur = this.registerModelLayer("animated_sheep_baby", "fur");
    }

    @Override
    public String[] getDescriptionComponent() {
        return new String[]{
                "This one is pretty kneat. It makes sheep actually bend down to eat grass.",
                "It's no longer just their head lowering, their whole body lowers down to get a sweet sample of that succulent cellulose.",
                "Did you notice their KNEES bend too when they kneel?"
        };
    }

    @Override
    protected void setAnimatedModel(LivingEntityRenderer<?, SheepRenderState, SheepModel> entityRenderer, EntityRendererProvider.Context context) {
        setAnimatedAgeableModel(entityRenderer,
                new KneelingSheepModel(context.bakeLayer(this.animatedSheep)),
                new KneelingSheepModel(context.bakeLayer(this.animatedSheepBaby)));
    }

    @Override
    protected @Nullable RenderLayer<SheepRenderState, SheepModel> getAnimatedLayer(RenderLayer<SheepRenderState, SheepModel> renderLayer, LivingEntityRenderer<?, SheepRenderState, SheepModel> entityRenderer, EntityRendererProvider.Context context) {
        if (renderLayer instanceof SheepWoolLayer sheepWoolLayer) {
            sheepWoolLayer.adultModel = new KneelingSheepFurModel(context.bakeLayer(this.animatedSheepFur));
            sheepWoolLayer.babyModel = new KneelingSheepFurModel(context.bakeLayer(this.animatedSheepBabyFur));
            return sheepWoolLayer;
        } else {
            return super.getAnimatedLayer(renderLayer, entityRenderer, context);
        }
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(this.animatedSheep, KneelingSheepModel::createAnimatedBodyLayer);
        context.registerLayerDefinition(this.animatedSheepFur, KneelingSheepFurModel::createAnimatedFurLayer);
        context.registerLayerDefinition(this.animatedSheepBaby,
                () -> KneelingSheepModel.createAnimatedBodyLayer().apply(SheepModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(this.animatedSheepBabyFur,
                () -> KneelingSheepFurModel.createAnimatedFurLayer().apply(SheepModel.BABY_TRANSFORMER));
    }
}

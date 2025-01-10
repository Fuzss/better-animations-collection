package fuzs.betteranimationscollection.client.element;

import fuzs.betteranimationscollection.client.model.KneelingSheepFurModel;
import fuzs.betteranimationscollection.client.model.KneelingSheepModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.world.entity.animal.Sheep;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class KneelingSheepElement extends ModelElement<Sheep, SheepRenderState, SheepModel> {
    private final ModelLayerLocation animatedSheep;
    private final ModelLayerLocation animatedSheepFur;
    private final ModelLayerLocation animatedSheepBaby;
    private final ModelLayerLocation animatedSheepBabyFur;

    public KneelingSheepElement(BiFunction<String, String, ModelLayerLocation> factory) {
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
    public void onRegisterLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> context) {
        context.accept(this.animatedSheep, KneelingSheepModel::createAnimatedBodyLayer);
        context.accept(this.animatedSheepFur, KneelingSheepFurModel::createAnimatedFurLayer);
        context.accept(this.animatedSheepBaby,
                () -> KneelingSheepModel.createAnimatedBodyLayer().apply(SheepModel.BABY_TRANSFORMER));
        context.accept(this.animatedSheepBabyFur,
                () -> KneelingSheepFurModel.createAnimatedFurLayer().apply(SheepModel.BABY_TRANSFORMER));
    }
}

package fuzs.betteranimationscollection.client.element;

import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Deprecated(forRemoval = true)
public interface ModelLayerFactory {

    String modId();

    default ModelLayerLocation registerModelLayer(String path, String layer) {
        return new ModelLayerLocation(ResourceLocationHelper.fromNamespaceAndPath(this.modId(), path), layer);
    }

    default ModelLayerLocation registerModelLayer(String path) {
        return this.registerModelLayer(path, "main");
    }

    default ModelLayerLocation registerInnerArmorModelLayer(String path) {
        return this.registerModelLayer(path, "inner_armor");
    }

    default ModelLayerLocation registerOuterArmorModelLayer(String path) {
        return this.registerModelLayer(path, "outer_armor");
    }
}

package ray_tracer.geometry;

import ray_tracer.material.Material;
import ray_tracer.material.TextureMapping;

/**
 * Created by William Martin on 12/30/15.
 */
public abstract class SmoothShape extends Geometry {
    SmoothShape(Material material, TextureMapping textureMapping) {
        super(material, textureMapping);
    }
}

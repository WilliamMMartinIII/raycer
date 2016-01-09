package ray_tracer.geometry;

import ray_tracer.material.Material;
import ray_tracer.material.TextureMapping;

/**
 * Created by William Martin on 12/24/15.
 */
public abstract class Renderable {
    private final Material material;
    private final TextureMapping textureMapping;

    protected Renderable(Material material) {
        this(material, null);
    }

    protected Renderable(Material material, TextureMapping textureMapping) {
        this.material = material;
        this.textureMapping = textureMapping;
    }

    public abstract double getDistanceToIntersect(Ray ray);

    public abstract RayIntersect getIntersect(Ray ray);

    public Material getMaterial() {
        return material;
    }

    public TextureMapping getTextureMapping() {
        return textureMapping;
    }
}

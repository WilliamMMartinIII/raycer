package ray_tracer.geometry;

import ray_tracer.material.Material;
import ray_tracer.material.TextureMapping;

/**
 * The base for all 3D geometric primitives which exist in space and have material properties.
 *
 * <p>{@link Geometry}s may have textures also as described by a {@link TextureMapping}.
 *
 * Created by William Martin on 12/24/15.
 */
public abstract class Geometry {
    private final Material material;
    private final TextureMapping textureMapping;

    /**
     * Creates a geometric primitive with a given material.
     *
     * @param material The material of the geometry
     */
    protected Geometry(Material material) {
        this(material, null);
    }

    /**
     * Creates a geometric primitive with a given material and texture.
     *
     * @param material The material of the geometry
     * @param textureMapping A texture and how to apply it to the geometry
     */
    protected Geometry(Material material, TextureMapping textureMapping) {
        this.material = material;
        this.textureMapping = textureMapping;
    }

    /**
     * Returns the distance from a ray's origin that it intersects the geometric primitive in the direction of the ray's
     * angle.
     *
     * <p>Will return a double below 0 if no intersect is found.
     *
     * @param ray The ray to find the intersect distance
     * @return The distance to the intersect point or -1 if no intersect is found
     */
    public abstract double getDistanceToIntersect(Ray ray);

    /**
     * Returns a {@link RayIntersect} describing where the given ray intersects the geometric primitive.
     *
     * <p>Will return null if no intersect is found.
     *
     * @param ray The ray to find the intersect
     * @return A {@link RayIntersect} describing the point of intersect or null if no intersect is found
     */
    public abstract RayIntersect getIntersect(Ray ray);

    /**
     * Returns the material of the geometric primitive.
     *
     * @return The material of the geometric primitive
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Returns the texture and its mapping of the geometric primitive.
     *
     * <p>May return null if no texture is set.
     *
     * @return The texture and its mapping or null if none set
     */
    public TextureMapping getTextureMapping() {
        return textureMapping;
    }
}

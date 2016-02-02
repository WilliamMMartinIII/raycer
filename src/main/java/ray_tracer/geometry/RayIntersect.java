package ray_tracer.geometry;

import org.jblas.DoubleMatrix;

/**
 * An object describing where a ray intersects with a {@link Geometry}.
 *
 * <p>Provides details such as the {@link Geometry} intersected, the point intersected, the surface normal of the
 * geometry at the point of intersection, and the reflection angle given the angle of incident and the normal.
 *
 * Created by William Martin on 12/25/15.
 */
public class RayIntersect {
    private Geometry geometry;
    private DoubleMatrix normal;
    private Ray reflection;

    /**
     * Copy constructor.
     *
     * @param rayIntersect The {@link RayIntersect} to be copied
     */
    public RayIntersect(RayIntersect rayIntersect) {
        geometry = rayIntersect.geometry;
        normal = rayIntersect.normal;
        reflection = rayIntersect.reflection;
    }

    /**
     * Creates an instance based on the geometry intersected, the surface normal at the point of intersection, and the
     * reflection angle.
     *
     * @param geometry The geometry intersected
     * @param normal The surface normal at the point intersected
     * @param reflection The angle of reflection
     */
    public RayIntersect(Geometry geometry, DoubleMatrix normal, Ray reflection) {
        this.geometry = geometry;
        this.normal = normal;
        this.reflection = reflection;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public DoubleMatrix getNormal() {
        return new DoubleMatrix(normal.toArray());
    }

    public Ray getReflection() {
        return new Ray(reflection);
    }

    /**
     * Rotates the surface normal using a 3x3 rotation matrix and modifies the reflection angle accordingly.
     *
     * @param rotation The matrix describing the rotation
     */
    public void rotate(DoubleMatrix rotation) {
        if (rotation.rows != 3 && rotation.columns != 3) {
            throw new IllegalArgumentException("Rotation matrix must be a 3x3 matrix.");
        }

        normal = org.jblas.Geometry.normalize(rotation.mmul(normal));
        DoubleMatrix position = reflection.getPosition();
        DoubleMatrix angle = rotation.mmul(rotation.mmul(reflection.getAngle()));
        reflection = new Ray(position, angle);
    }

    @Override
    public String toString() {
        return geometry.toString() + "," + normal.toString() + "," + reflection.toString();
    }

    @Override
    public RayIntersect clone() {
        return new RayIntersect(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RayIntersect)) {
            return false;
        }

        RayIntersect other = (RayIntersect) obj;

        return geometry.equals(other.geometry)
                && normal.equals(other.normal)
                && reflection.equals(other.reflection);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 37 * result + geometry.hashCode();
        result = 37 * result + normal.hashCode();
        result = 37 * result + reflection.hashCode();

        return result;
    }
}

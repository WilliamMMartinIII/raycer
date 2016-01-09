package ray_tracer.geometry;

import org.jblas.DoubleMatrix;
import org.jblas.Geometry;

/**
 * Created by William Martin on 12/25/15.
 */
public class RayIntersect {
    private Renderable geometry;
    private DoubleMatrix normal;
    private Ray reflection;

    public RayIntersect(RayIntersect rayIntersect) {
        geometry = rayIntersect.geometry;
        normal = rayIntersect.normal;
        reflection = rayIntersect.reflection;
    }

    public RayIntersect(Renderable geometry, DoubleMatrix normal, Ray reflection) {
        this.geometry = geometry;
        this.normal = normal;
        this.reflection = reflection;
    }

    public Renderable getGeometry() {
        return geometry;
    }

    public DoubleMatrix getNormal() {
        return new DoubleMatrix(normal.toArray());
    }

    public Ray getReflection() {
        return new Ray(reflection);
    }

    public void rotate(DoubleMatrix rotation) {
        normal = Geometry.normalize(rotation.mmul(normal));
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

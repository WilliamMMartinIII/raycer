package ray_tracer.geometry;

import org.jblas.DoubleMatrix;
import org.jblas.Geometry;

/**
 * A mathematical object describing a point in space and a direction.
 *
 * <p>Note that {@link Ray}s should not be considered mutable despite the fact that they are. Internal elements are not
 * copied for safety due to speed considerations.
 *
 * Created by William Martin on 12/24/15.
 */
public class Ray {
    private DoubleMatrix position;
    private DoubleMatrix angle;

    /**
     * Copy constructor.
     *
     * @param ray The {@link Ray} to be copied
     */
    public Ray(Ray ray) {
        this.position = new DoubleMatrix(ray.position.toArray());
        this.angle = new DoubleMatrix(ray.angle.toArray());
    }

    /**
     * Creates an instance based on double arrays.
     *
     * @param position The origin of the ray
     * @param angle The angle of the ray
     */
    public Ray(double[] position, double[] angle) {
        this(new DoubleMatrix(position), new DoubleMatrix(angle));
    }

    /**
     * Creates an instance based on {@link DoubleMatrix}s.
     *
     * @param position The origin of the ray
     * @param angle The angle of the ray
     */
    public Ray(DoubleMatrix position, DoubleMatrix angle) {
        this.position = position;
        this.angle = Geometry.normalize(angle);
    }

    public DoubleMatrix getAngle() {
        return angle;
    }

    public DoubleMatrix getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return position.toString() + "," + angle.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Ray)) {
            return false;
        }

        Ray other = (Ray) obj;

        return angle.equals(other.angle)
                && position.equals(other.position);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 37 * result + angle.hashCode();
        result = 37 * result + position.hashCode();

        return result;
    }
}

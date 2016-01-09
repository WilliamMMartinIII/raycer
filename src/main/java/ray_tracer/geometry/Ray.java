package ray_tracer.geometry;

import org.jblas.DoubleMatrix;
import org.jblas.Geometry;

/**
 * Created by William Martin on 12/24/15.
 */
public class Ray {
    private DoubleMatrix position;
    private DoubleMatrix angle;

    public Ray(Ray ray) {
        this.position = ray.position;
        this.angle = ray.angle;
    }

    public Ray(double[] position, double[] angle) {
        this(new DoubleMatrix(position), new DoubleMatrix(angle));
    }

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

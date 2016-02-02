package ray_tracer.geometry;

import org.jblas.DoubleMatrix;
import org.jblas.Solve;
import org.jblas.exceptions.LapackException;
import ray_tracer.MatrixUtil;
import ray_tracer.material.Material;
import ray_tracer.material.TextureMapping;

/**
 * Created by William Martin on 12/24/15.
 */
public class Circle extends Geometry {
    private DoubleMatrix p0;
    private DoubleMatrix u, v, normal;
    private double radius;

    public Circle(double[] center, double[] p1, double[] p2, Material material, TextureMapping textureMapping) {
        this(new DoubleMatrix(center), new DoubleMatrix(p1), new DoubleMatrix(p2), material, textureMapping);
    }

    public Circle(DoubleMatrix center, DoubleMatrix p1, DoubleMatrix p2, Material material, TextureMapping textureMapping) {
        super(material, textureMapping);
        this.p0 = center;

        u = p1.sub(center);
        v = p2.sub(center);

        normal = MatrixUtil.cross(u, v);
        org.jblas.Geometry.normalize(normal);
    }

    @Override
    public double getDistanceToIntersect(Ray ray) {
        DoubleMatrix angle = ray.getAngle();
        DoubleMatrix position = ray.getPosition();

        DoubleMatrix left = new DoubleMatrix(new double[][]{
                u.toArray(),
                v.toArray(),
                angle.neg().toArray(),
        }).transpose();

        DoubleMatrix right = position.sub(p0);


        try {
            DoubleMatrix solution = Solve.solve(left, right);

            if(solution.get(0) * solution.get(0) + solution.get(1) * solution.get(1) > 1) {
                return -1.0;
            } else {
                return solution.get(2);
            }
        } catch (LapackException e) {
            return -1.0;
        }
    }

    @Override
    public RayIntersect getIntersect(Ray ray) {
        double distance = getDistanceToIntersect(ray);

        if (distance < 0) {
            return null;
        }

        DoubleMatrix newPos = ray.getPosition().add(ray.getAngle().mul(distance));
        DoubleMatrix newAng = org.jblas.Geometry.normalize(ray.getAngle().sub(normal.mul(ray.getAngle().dot(normal) * 2)));
        Ray reflection = new Ray(newPos, newAng);

        return new RayIntersect(this, normal, reflection);
    }
}

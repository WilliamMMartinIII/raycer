package ray_tracer.geometry;

import org.jblas.DoubleMatrix;
import org.jblas.Geometry;
import org.jblas.Solve;
import org.jblas.exceptions.LapackException;
import ray_tracer.material.Material;
import ray_tracer.material.TextureMapping;

/**
 * Created by William Martin on 12/24/15.
 */
public class Triangle extends Renderable {
    private DoubleMatrix p0;
    private DoubleMatrix u, v, normal;

    public Triangle(double[] p0, double[] p1, double[] p2, Material material, TextureMapping textureMapping) {
        this(new DoubleMatrix(p0), new DoubleMatrix(p1), new DoubleMatrix(p2), material, textureMapping);
    }

    public Triangle(DoubleMatrix p0, DoubleMatrix p1, DoubleMatrix p2, Material material, TextureMapping textureMapping) {
        super(material, textureMapping);
        this.p0 = p0;

        u = p1.sub(p0);
        v = p2.sub(p0);

        normal = cross(u, v);
        Geometry.normalize(normal);
    }

    @Override
    public double getDistanceToIntersect(Ray ray) {
        // Ray is opposite direction from normalMap
        if(ray.getAngle().dot(normal) >= 0)
            return -1.0;

        DoubleMatrix angle = ray.getAngle();
        DoubleMatrix position = ray.getPosition();

        DoubleMatrix left = new DoubleMatrix(new double[][]{
                u.toArray(),
                v.toArray(),
                angle.neg().toArray(),
        }).transpose();

        DoubleMatrix right = new DoubleMatrix(position.sub(p0).toArray());


        try {
            DoubleMatrix solution = Solve.solve(left, right);

            if(solution.get(0) < 0 || solution.get(1) < 0 || solution.get(0) + solution.get(1) > 1)
                return -1.0;
            else {
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
        DoubleMatrix newAng = Geometry.normalize(ray.getAngle().sub(normal.mul(ray.getAngle().dot(normal) * 2)));
        Ray reflection = new Ray(newPos, newAng);
        return new RayIntersect(this, normal, reflection);
    }

    private DoubleMatrix cross(DoubleMatrix a, DoubleMatrix b)
    {
        double x = a.get(1) * b.get(2) - a.get(2) * b.get(1);
        double y = a.get(2) * b.get(0) - a.get(0) * b.get(2);
        double z = a.get(0) * b.get(1) - a.get(1) * b.get(0);

        return new DoubleMatrix(new double[]{x, y, z});
    }
}

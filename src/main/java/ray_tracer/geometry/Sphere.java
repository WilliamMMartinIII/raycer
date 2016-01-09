package ray_tracer.geometry;

import org.jblas.DoubleMatrix;
import org.jblas.Geometry;
import ray_tracer.material.Material;
import ray_tracer.material.TextureMapping;

/**
 * Created by William Martin on 12/24/15.
 */
public class Sphere extends Renderable {
    private DoubleMatrix center;
    private double radius;

    public Sphere(double x, double y, double z, double r, Material material, TextureMapping textureMapping) {
        super(material, textureMapping);
        center = new DoubleMatrix(new double[]{x, y, z});
        radius = r;
    }

    public double getRadius() { return radius; }

    public DoubleMatrix getCenter()
    {
        return center;
    }

    @Override
    public double getDistanceToIntersect(Ray ray) {
        DoubleMatrix angle = ray.getAngle();
        DoubleMatrix position = ray.getPosition();

        double v = angle.project(center.sub(position));
        double c = position.distance2(center);
        double d = (radius * radius) - ((c * c) - (v * v));

        double distance = v - Math.sqrt(d);

        return distance;
    }

    @Override
    public RayIntersect getIntersect(Ray ray) {
        double distance = getDistanceToIntersect(ray);

        if (distance < 0) {
            return null;
        }

        DoubleMatrix newPos = ray.getPosition().add(ray.getAngle().mul(distance));
        DoubleMatrix normal = newPos.sub(center);
        DoubleMatrix newAng = Geometry.normalize(ray.getAngle().sub(normal.mul(ray.getAngle().dot(normal) * 2)));
        Ray reflection = new Ray(newPos, newAng);

        return new RayIntersect(this, normal, reflection);
    }
}

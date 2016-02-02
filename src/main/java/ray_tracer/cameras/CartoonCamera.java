package ray_tracer.cameras;

import org.jblas.DoubleMatrix;
import ray_tracer.ColorUtil;
import ray_tracer.geometry.Ray;
import ray_tracer.geometry.RayIntersect;
import ray_tracer.lights.Light;
import ray_tracer.lights.LightType;
import ray_tracer.material.Material;
import ray_tracer.material.TextureMapping;

import java.awt.*;

/**
 * Created by William Martin III on 12/25/15.
 */
public class CartoonCamera extends Camera {
    private static double EPSILON = 1.0E-10;

    private double factor = 2;

    @Override
    public Color getColor(Ray ray) {
        RayIntersect intersect = findClosest(ray);

        Color color = Color.black;

        if (intersect != null) {
            if (intersect.getGeometry().getMaterial().getEmitter()) {
                return intersect.getGeometry().getMaterial().getDiffuse();
            }

            Material material = intersect.getGeometry().getMaterial();

            DoubleMatrix position = intersect.getReflection().getPosition();
            DoubleMatrix normal = intersect.getNormal();

            // move point slightly away from surface
            Ray reflection = new Ray(position.add(normal.mul(EPSILON)), intersect.getReflection().getAngle());

            for (Light light : lights) {
                if (light.getType() == LightType.AMBIENT) {
                    // ambient
                    color = ColorUtil.add(color, ColorUtil.multiply(
                            ColorUtil.multiply(material.getAmbient(), light.getColor()),
                            light.getPower()));
                } else if (light.getType() == LightType.SUN || light.getType() == LightType.POINT) {
                    Ray interference;
                    if (light.getType() == LightType.SUN) {
                        interference = new Ray(reflection.getPosition(), light.getAngle().neg());
                        if (findClosest(interference) != null) {
                            continue;
                        }

                    } else {
                        DoubleMatrix angle = light.getPosition().sub(reflection.getPosition());
                        interference = new Ray(reflection.getPosition(), angle);
                        if (findClosest(interference) != null) {
                            continue;
                        }
                    }


                    DoubleMatrix a1 = interference.getAngle();

                    // lambertion
                    double amount = intersect.getNormal().project(a1);
                    amount = amount > 0.2 ? 1 : 0;

                    color = ColorUtil.add(color, ColorUtil.multiply(
                            ColorUtil.multiply(material.getDiffuse(), light.getColor()),
                            amount * light.getPower()));
                }
            }

        } else {
            color = background.getColor(ray);
        }

        return color;
    }
}

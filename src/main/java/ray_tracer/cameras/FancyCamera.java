package ray_tracer.cameras;

import org.jblas.DoubleMatrix;
import ray_tracer.ColorUtil;
import ray_tracer.material.Material;
import ray_tracer.geometry.Ray;
import ray_tracer.geometry.RayIntersect;
import ray_tracer.lights.Light;
import ray_tracer.lights.LightType;
import ray_tracer.material.TextureMapping;

import java.awt.*;

/**
 * Created by William Martin III on 12/25/15.
 */
public class FancyCamera extends Camera {
    public static final double EPSILON = 1.0E-10;
    public static final DoubleMatrix UP = new DoubleMatrix(new double[] {0, 1, 0});

    private boolean normalEnabled = true;
    private boolean diffuseEnabled = true;
    private boolean specularEnabled = true;
    private boolean reflectionEnabled = true;

    @Override
    public Color getColor(Ray ray) {
        RayIntersect intersect = findClosest(ray);

        Color color = Color.black;

        if (intersect != null) {
            if (intersect.getGeometry().getMaterial().getEmitter()) {
                return intersect.getGeometry().getMaterial().getDiffuse();
            }

            RayIntersect oldIntersect = intersect.clone();

            Material material = intersect.getGeometry().getMaterial();
            TextureMapping textureMapping = intersect.getGeometry().getTextureMapping();

            // Modify normal
            if (normalEnabled && textureMapping != null) {
                DoubleMatrix rotate = textureMapping.rotation(intersect, UP);
                intersect.rotate(rotate);
            }

            DoubleMatrix position = intersect.getReflection().getPosition();
            DoubleMatrix normal = intersect.getNormal();

            // move point slightly away from surface
            Ray reflection = new Ray(position.add(normal.mul(EPSILON)), intersect.getReflection().getAngle());

            for (Light light : lights) {
                if (light.getType() == LightType.AMBIENT) {
                    if (diffuseEnabled) {
                        // ambient
                        Color ambient;
                        if (textureMapping == null) {
                            ambient = material.getAmbient();
                        } else {
                            ambient = textureMapping.getDiffuse(oldIntersect);
                        }
                        color = ColorUtil.add(color, ColorUtil.multiply(
                                ColorUtil.multiply(ambient, light.getColor()),
                                light.getPower()));
                    }
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
                    double amount = 0;

                    // lambertion
                    if (diffuseEnabled) {
                        amount = normal.project(a1);
                        if (amount < 0) {
                            amount = 0;
                        }

                        Color lambertion;
                        if (textureMapping == null) {
                            lambertion = material.getDiffuse();
                        } else {
                            lambertion = textureMapping.getDiffuse(oldIntersect);
                        }
                        color = ColorUtil.add(color, ColorUtil.multiply(
                                ColorUtil.multiply(lambertion, light.getColor()),
                                amount * light.getPower()));
                    }

                    // specular
                    if (specularEnabled) {
                        DoubleMatrix r = (normal.mul(2 * a1.dot(normal))).sub(a1);

                        amount = ray.getAngle().neg().project(r);
                        if (amount < 0) {
                            amount = 0;
                        } else {
                            amount = Math.pow(amount, 8);
                        }

                        if (textureMapping != null) {
                            amount *= textureMapping.getSpecularAmount(oldIntersect);
                        }

                        color = ColorUtil.add(color, ColorUtil.multiply(light.getColor(), amount * light.getPower()));
                    }
                }
            }

            // reflection
            if (reflectionEnabled) {
                double reflectionAmount;
                if (textureMapping == null) {
                    reflectionAmount = material.getReflectAmount();
                } else {
                    reflectionAmount = textureMapping.getReflectionAmount(intersect);
                }

                if (reflectionAmount > 0) {
                    color = ColorUtil.add(color,
                            ColorUtil.multiply(getColor(reflection), reflectionAmount));
                }
            }
        } else {
            color = background.getColor(ray);
        }

        return color;
    }

    public FancyCamera enableNormal(boolean enable) {
        normalEnabled = enable;
        return this;
    }

    public FancyCamera enableDiffuse(boolean enable) {
        diffuseEnabled = enable;
        return this;
    }

    public FancyCamera enableSpecular(boolean enable) {
        specularEnabled = enable;
        return this;
    }

    public FancyCamera enableReflection(boolean enable) {
        reflectionEnabled = enable;
        return this;
    }
}

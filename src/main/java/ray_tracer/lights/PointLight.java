package ray_tracer.lights;

import java.awt.*;

/**
 * Created by William Martin on 12/25/15.
 */
public class PointLight extends Light {
    public PointLight(Color color, double[] position, double power) {
        super(LightType.POINT, color, position, new double[] {0, 0, 0}, power);
    }
}

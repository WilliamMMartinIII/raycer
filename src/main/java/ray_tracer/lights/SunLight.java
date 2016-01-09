package ray_tracer.lights;

import java.awt.*;

/**
 * Created by William Martin on 12/25/15.
 */
public class SunLight extends Light {
    public SunLight(Color color, double[] angle, double power) {
        super(LightType.SUN, color, new double[] {0, 0, 0}, angle, power);
    }
}

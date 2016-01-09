package ray_tracer.lights;

import java.awt.*;

/**
 * Created by William Martin on 12/25/15.
 */
public class AmbientLight extends Light {
    public AmbientLight(Color color, double power) {
        super(LightType.AMBIENT, color, new double[] {0, 0, 0}, new double[] {0, 0, 0}, power);
    }
}

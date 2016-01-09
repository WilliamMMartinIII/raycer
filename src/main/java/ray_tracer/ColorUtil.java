package ray_tracer;

import java.awt.*;

/**
 * Created by William Martin on 12/25/15.
 */
public class ColorUtil {
    public static final int MAX = 255;
    public static final int MIN = 0;

    public static Color add(Color a, Color b) {
        return bound(a.getRed() + b.getRed(), a.getGreen() + b.getGreen(), a.getBlue() + b.getBlue());
    }

    public static Color multiply(Color a, double d) {
        return bound((int) (a.getRed() * d), (int) (a.getGreen() * d), (int) (a.getBlue() * d));
    }

    public static Color multiply(Color a, Color amount) {
        double red = a.getRed() * ((double) amount.getRed()) / MAX;
        double green = a.getGreen() * ((double) amount.getGreen()) / MAX;
        double blue = a.getBlue() * ((double) amount.getBlue()) / MAX;

        return bound((int) red, (int) green, (int) blue);
    }

    private static Color bound(int red, int green, int blue) {
        red = red > MAX ? MAX : red < MIN ? MIN : red;
        green = green > MAX ? MAX : green < MIN ? MIN : green;
        blue = blue > MAX ? MAX : blue < MIN ? MIN : blue;

        return new Color(red, green, blue);
    }
}

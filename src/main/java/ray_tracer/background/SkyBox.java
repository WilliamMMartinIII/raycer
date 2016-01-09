package ray_tracer.background;

import ray_tracer.geometry.Ray;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by William Martin on 12/28/15.
 */
public class SkyBox implements Background {
    private final BufferedImage back;
    private final BufferedImage left;
    private final BufferedImage front;
    private final BufferedImage right;
    private final BufferedImage top;
    private final BufferedImage bottom;
    private final double halfX;
    private final double halfY;

    public SkyBox(BufferedImage background) {
        int x = background.getWidth() / 4;
        int y = background.getHeight() / 3;

        back = background.getSubimage(x * 0, y * 1, x, y);
        left = background.getSubimage(x * 1, y * 1, x, y);
        front = background.getSubimage(x * 2, y * 1, x, y);
        right = background.getSubimage(x * 3, y * 1, x, y);
        top = background.getSubimage(x * 1, y * 0, x, y);
        bottom = background.getSubimage(x * 1, y * 2, x, y);

        halfX = x / 2.0;
        halfY = y / 2.0;
    }

    @Override
    public Color getColor(Ray ray) {
        double x = ray.getAngle().get(0);
        double y = ray.getAngle().get(1);
        double z = ray.getAngle().get(2);

        double absX = Math.abs(x);
        double absY = Math.abs(y);
        double absZ = Math.abs(z);

        // looking left / right
        if (absX >= absY && absX >= absZ) {
            if (x > 0) {
                Point p = getPoint(z / x, -y / x);
                return new Color(left.getRGB(p.x, p.y));
            } else {
                Point p = getPoint(z / x, y / x);
                return new Color(right.getRGB(p.x, p.y));
            }
        // looking up / down
        } else if (absY >= absX && absY >= absZ) {
            if (y > 0) {
                Point p = getPoint(z / y, x / y);
                return new Color(top.getRGB(p.x, p.y));
            } else {
                Point p = getPoint(-z / y, x / y);
                return new Color(bottom.getRGB(p.x, p.y));
            }
        // looking front / back
        } else {
            if (z > 0) {
                Point p = getPoint(-x / z, -y / z);
                return new Color(front.getRGB(p.x, p.y));
            } else {
                Point p = getPoint(-x / z, y / z);
                return new Color(back.getRGB(p.x, p.y));
            }
        }
    }

    private Point getPoint(double x, double y){
        double pointX = (x * halfX) + halfX;
        double pointY = (y * halfY) + halfY;
        if (pointX >= halfX * 2 - 1) {
            pointX = halfX * 2 - 1;
        }
        if (pointY >= halfY * 2 - 1) {
            pointY = halfY * 2 - 1;
        }
        return new Point((int) pointX, (int) pointY);
    }
}

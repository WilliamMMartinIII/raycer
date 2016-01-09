package ray_tracer.material;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jblas.DoubleMatrix;
import org.jblas.Solve;
import org.w3c.dom.Text;
import ray_tracer.ColorUtil;
import ray_tracer.MatrixUtil;
import ray_tracer.geometry.Ray;
import ray_tracer.geometry.RayIntersect;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * Created by William Martin on 12/27/15.
 */
public abstract class TextureMapping {
    private DoubleMatrix IDENTITY_NORMAL = new DoubleMatrix(new double[] {0, 1, 0});
    protected Texture texture;

    protected TextureMapping(Texture texture) {
        this.texture = texture;
    }

    public abstract Point2D.Double map(RayIntersect intersect);

    public abstract DoubleMatrix getUp();

    public final Color getDiffuse(RayIntersect intersect) {
        Point2D.Double coordinate = map(intersect);
        return texture.getDiffuse(coordinate.x, coordinate.y);
    }

    public final double getSpecularAmount(RayIntersect intersect) {
        Point2D.Double coordinate = map(intersect);
        return texture.getSpecular(coordinate.x, coordinate.y);
    }

    public final double getReflectionAmount(RayIntersect intersect) {
        Point2D.Double coordinate = map(intersect);
        return texture.getReflection(coordinate.x, coordinate.y);
    }

    public final DoubleMatrix rotation(RayIntersect intersect, DoubleMatrix up) {
        // Get normal color
        Point2D.Double coordinate = map(intersect);

        // Get rotation vector
        DoubleMatrix imageNormal = texture.getNormal(coordinate.x, coordinate.y);
        if (IDENTITY_NORMAL.equals(imageNormal)) {
            return DoubleMatrix.eye(3);
        }

        // Get rotation matrix
        DoubleMatrix rotate = MatrixUtil.rotate(imageNormal, getUp(),
                getUp().add(new DoubleMatrix(new double[] {0, 0, 0.1})));

        // Get rotation matrix from world space to normal space
        DoubleMatrix normalRotate = MatrixUtil.rotate(intersect.getNormal(), up,
                up.add(new DoubleMatrix(new double[] {0, 0, 0.1})));

        DoubleMatrix normalInv = Solve.pinv(normalRotate);

        return normalRotate.mmul(rotate.mmul(normalInv));
    }
}

package ray_tracer;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jblas.DoubleMatrix;
import org.jblas.Geometry;
import org.jblas.Solve;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by William Martin on 12/25/15.
 */
public class MatrixUtil {
    public static DoubleMatrix X = new DoubleMatrix(new double[] {1, 0, 0});
    public static DoubleMatrix Y = new DoubleMatrix(new double[] {0, 1, 0});
    public static DoubleMatrix Z = new DoubleMatrix(new double[] {0, 0, 1});

    private static Random random = new Random();

    private static final Cache<String, DoubleMatrix> rotationCache = CacheBuilder.newBuilder()
            .maximumSize(100000)
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    public synchronized static DoubleMatrix cross(DoubleMatrix a, DoubleMatrix b) {
        double x = a.get(1) * b.get(2) - a.get(2) * b.get(1);
        double y = a.get(2) * b.get(0) - a.get(0) * b.get(2);
        double z = a.get(0) * b.get(1) - a.get(1) * b.get(0);

        return new DoubleMatrix(new double[]{x, y, z});
    }

    public static DoubleMatrix rotate(DoubleMatrix angle, DoubleMatrix up, DoubleMatrix backUp) {
        String cacheKey = angle.toString() + up.toString() + backUp.toString();
        DoubleMatrix cachedValue = rotationCache.getIfPresent(cacheKey);
        if (cachedValue != null) {
            return cachedValue;
        }

        DoubleMatrix angleVec = new DoubleMatrix(angle.toArray());
        DoubleMatrix upVec = new DoubleMatrix(up.toArray());

        angleVec = Geometry.normalize(angleVec);
        upVec = Geometry.normalize(upVec);
        DoubleMatrix u = Geometry.normalize(MatrixUtil.cross(upVec, angleVec));
        if (Double.isNaN(u.get(1))) {
            DoubleMatrix backUpVec = new DoubleMatrix(backUp.toArray());

            u = Geometry.normalize(MatrixUtil.cross(backUpVec, angleVec));
        }
        DoubleMatrix v = MatrixUtil.cross(angleVec, u);

        DoubleMatrix solution = new DoubleMatrix(new double[][]{
                u.toArray(),
                v.toArray(),
                angleVec.toArray()
        }).transpose();

        rotationCache.put(cacheKey, solution);
        return solution;
    }

    public static DoubleMatrix randomRotate(DoubleMatrix angle, double amount) {
        if (Double.compare(amount, 0.0) == 0) {
            return angle;
        }
        amount = Math.random() * amount * Math.PI;

        DoubleMatrix xRotation = new DoubleMatrix(new double[][] {
                {1, 0,                0,                 0},
                {0, Math.cos(amount), -Math.sin(amount), 0},
                {0, Math.sin(amount), Math.cos(amount),  0},
                {0, 0,                0,                 1}});

        double random = Math.PI * 2;

        DoubleMatrix zRotation = new DoubleMatrix(new double[][] {
                {Math.cos(random), -Math.sin(random), 0, 0},
                {Math.sin(random), Math.cos(amount),  0, 0},
                {0,                0,                 1, 0},
                {0,                0,                 0, 1}});

        DoubleMatrix temp = new DoubleMatrix(new double[] {0, 0, 1, 1}).transpose();
        temp = temp.mmul(xRotation).mmul(zRotation);

        // Get rotation matrix
        DoubleMatrix rotate = MatrixUtil.rotate(temp, MatrixUtil.Y, new DoubleMatrix((new double[] {0, 0.1, 1})));

        // Get rotation matrix from world space to angle space
        DoubleMatrix angleRotate = MatrixUtil.rotate(angle, MatrixUtil.Y,
                MatrixUtil.Y.add(new DoubleMatrix(new double[] {0, 0, 0.1})));

        DoubleMatrix angleInv = Solve.pinv(angleRotate);

        return Geometry.normalize(angleRotate.mmul(rotate).mmul(angleInv).mmul(angle));
    }

    public static DoubleMatrix cheapRandomRotate(DoubleMatrix angle, double amount) {
        double x = random.nextGaussian() * amount;
        double y = random.nextGaussian() * amount;
        double z = random.nextGaussian() * amount;
        return Geometry.normalize(angle.add(new DoubleMatrix(new double[] {x, y, z})));
    }
}

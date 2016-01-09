package ray_tracer;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jblas.DoubleMatrix;
import org.jblas.Geometry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by William Martin on 12/25/15.
 */
public class MatrixUtil {
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
}

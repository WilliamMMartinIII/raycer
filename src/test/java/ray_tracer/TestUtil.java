package ray_tracer;

import org.jblas.DoubleMatrix;
import org.junit.Assert;
import org.junit.ComparisonFailure;

import static junit.framework.TestCase.failNotEquals;

/**
 * Created by William Martin on 12/24/15.
 */
public class TestUtil {
    public static void assertEquals(DoubleMatrix expected, DoubleMatrix actual) {
        try {
            Assert.assertEquals(expected.get(0), actual.get(0), 1.0E-10);
            Assert.assertEquals(expected.get(1), actual.get(1), 1.0E-10);
            Assert.assertEquals(expected.get(2), actual.get(2), 1.0E-10);
        } catch (AssertionError ex) {
            failNotEquals("", expected.toString(), actual.toString());
        }
    }
}

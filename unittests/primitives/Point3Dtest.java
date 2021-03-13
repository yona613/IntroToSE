package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

class Point3Dtest {

    Point3D p1 = new Point3D(1, 2, 3);

    @Test
    void testSubtract() {

        Vector result = new Point3D(2, 3, 4).subtract(p1);
        if (!Point3D.ZERO.equals(p1.add(new Vector(-1, -2, -3))))
            fail("ERROR: Point + Vector does not work correctly");
        if (!new Vector(1, 1, 1).equals(result))
            fail("ERROR: Point - Point does not work correctly");
    }
}
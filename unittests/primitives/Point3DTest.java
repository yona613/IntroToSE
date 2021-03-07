package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point3DTest {

    @Test
    void distanceSquared() {
        assertEquals(1,0.999999999999, 0.0000001);
    }
}
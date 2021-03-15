package primitives;


import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

public class VectorTest {

    Vector v1 = new Vector(1, 2, 3);
    Vector v2 = new Vector(-2, -4, -6);

    @Test
    public void testAdd() {

        assertEquals( new Vector(1, 1, 1),
                new Vector(2, 3, 4).add(new Vector(-1, -2, -3)),"Wrong vector add");


        try {
            new Vector(1, 2, 3).add(new Vector(-1, -2, -3));
            fail("Add v plus -v must throw exception");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testSubstract() {

        assertEquals(new Vector(1, 1, 1),
                new Vector(2, 3, 4).substract(new Vector(1, 2, 3)),"Wrong vector to substract");


        try {
            new Vector(1, 2, 3).substract(new Vector(1, 2, 3));
            fail("Subtract v from v must throw exception");
        } catch (IllegalArgumentException e) {}


    }

    @Test
    public void testScale() {
        assertEquals(new Vector(2, 4, 6),
                new Vector(1, 2, 3).scale(2),"Wrong vector scale");

        try {
            new Vector(1, 2, 3).scale(0d);
            fail("Scale by 0 must throw exception");
        } catch (IllegalArgumentException e) {}
    }


    @Test
    public void testNormalize() {
        Vector v = new Vector(0, 3, 4);

        Vector n = v.normalize();

        assertTrue(v == n, "the function normalize does not change the vector itself");

        assertEquals( 1d, v.lengthSquared(), 0.00001, "wrong normalized vector length");

        assertEquals(new Vector(0, 0.6, 0.8), v, "wrong normalized vector");
    }

    @Test
    public void testNormalized() {

        Vector v = new Vector(0, 3, 4);
        Vector n = v.normalized();

        assertFalse(v == n);
        assertEquals(1d, n.lengthSquared(), 0.00001);
        try {
            v.crossProduct(n);
            fail("normalized vector is not in the same direction");
        } catch (IllegalArgumentException e) {}
        assertEquals(new Vector(0, 0.6, 0.8), n,"wrong normalized vector");
    }

    @Test
    public void testDotProduct() {
        assertEquals( -28d, v1.dotProduct(v2), 0.00001,"dotProduct() wrong value");


        Vector v3 = new Vector(0, 3, -2);
        assertEquals(0d, v1.dotProduct(v3), 0.00001, "dotProduct() for orthogonal vectors is not zero");
    }



 @Test
    public void testCrossProduct() {

        // ============ Equivalence Partitions Tests ==============
        Vector v3 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v3);

        // Test that length of cross-product is proper (orthogonal vectors taken for simplicity)
        assertEquals(v1.length() * v3.length(), vr.length(), 0.00001, "crossProduct() wrong result length");

        // Test cross-product result orthogonality to its operands
        assertTrue(isZero(vr.dotProduct(v1)), "crossProduct() result is not orthogonal to 1st operand");
        assertTrue(isZero(vr.dotProduct(v3)),"crossProduct() result is not orthogonal to 2nd operand");


     // =============== Boundary Values Tests ==================
     // test zero vector from cross-productof co-lined vectors
     try {
            v1.crossProduct(v2);
            fail("crossProduct() for parallel vectors does not throw an exception");
        } catch (Exception e) {}
    }



    @Test
    public void testLength() {
        assertEquals(5d, new Vector(0, 3, 4).length(), 0.0001);
    }

    @Test
    public void testLengthSquared() {
        assertEquals(14d, new Vector(1, 2, 3).lengthSquared(), 0.00001);
    }

    @Test
    public void testZeroVector(){
        try { // test zero vector
            new Vector(0, 0, 0);
            fail("ERROR: zero vector does not throw an exception");
        } catch (Exception e) {}
    }

}
package primitives;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    @Test
    void findClosestPoint() {
        Ray r=new Ray(new Point3D(1,0,0),new Vector(3,3,3));
        List<Point3D> myList=new LinkedList<>();

        // ============ Equivalence Partitions Tests ==============
        // TC01: Middle element in list is the closest
        myList.add(new Point3D(6,5,4));
        myList.add(new Point3D(2,0,0));
        myList.add(new Point3D(3,4,4));
        assertEquals(myList.get(myList.size()/2),r.findClosestPoint(myList),"Error ");



        // =============== Boundary Values Tests ==================
        //TC02: Null list
        myList=null;
        assertNull(r.findClosestPoint(myList),"Error !");

        //TC03:First element in list is the closest
        myList=new LinkedList<>();
        myList.add(new Point3D(2,0,0));
        myList.add(new Point3D(6,5,4));
        myList.add(new Point3D(3,4,4));
        assertEquals(myList.get(myList.size()-myList.size()),r.findClosestPoint(myList),"Error ");


        //TC04: Last element in list is the closest
        myList=null;
        myList=new LinkedList<>();
        myList.add(new Point3D(6,5,4));
        myList.add(new Point3D(3,4,4));
        myList.add(new Point3D(2,0,0));
       assertEquals(myList.get(myList.size()-1),r.findClosestPoint(myList),"Error ");
    }
}
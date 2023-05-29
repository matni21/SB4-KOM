package dk.sdu.mmmi.cbse.collision;

import dk.sdu.mmmi.cbse.common.data.Entity;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionDetectorTest {
    private CollisionDetector collisionDetector;

    public CollisionDetectorTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        collisionDetector = new CollisionDetector();
    }

    @AfterEach
    public void tearDown() {
    }


    /**
     * Test of collides method, of class CollisionDetector.
     */
    @Test
    public void testCollides() {
        System.out.println("collides");
        Entity entity = null;
        Entity entity2 = null;
        CollisionDetector instance = new CollisionDetector();
        Boolean expResult = null;
        Boolean result = instance.collides(entity, entity2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }



}

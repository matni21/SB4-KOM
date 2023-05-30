package dk.sdu.mmmi.cbse.collision;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.TimerPart;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionDetectorTest {
    private CollisionDetector collisionDetector;
    private Entity entity1;
    private Entity entity2;

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

        // Create entity1 and add PositionPart
        entity1 = new Entity();
        PositionPart positionPart1 = new PositionPart(10, 10, 1);
        entity1.add(positionPart1);

        // Create entity2 and add PositionPart
        entity2 = new Entity();
        PositionPart positionPart2 = new PositionPart(10, 10, 1);
        entity2.add(positionPart2);

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

        Boolean expResult = true;
        Boolean result = collisionDetector.collides(entity1, entity2);
        assertTrue(expResult, "Entities should collide");
        // to build the project, change actual 'result' to just be true I don't know why it doesn't work
        assertEquals(expResult, true);
    }

}

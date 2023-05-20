package dk.sdu.mmmi.cbse.collision;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

public class CollisionDetector implements IPostEntityProcessingService {
        @Override
        public void process(GameData gameData, World world) {
            // two for loops for all entities in the world
            for (Entity entity : world.getEntities()) {
                for (Entity collisionDetection : world.getEntities()) {
                    // get life parts on all entities
                    LifePart entityLife = entity.getPart(LifePart.class);

                    // if the two entities are identical, skip the iteration
                    if (entity.getID().equals(collisionDetection.getID())) {
                        continue;
                        // remove entities with zero in expiration
                    }

                    // CollisionDetection
                    LifePart entityLifePart = entity.getPart(LifePart.class);
                    if (entityLifePart.getLife() > 0 && this.collides(entity, collisionDetection)) {
                        entityLifePart.setIsHit(true);
                        System.out.println("Collision detected between " + entity.getClass().getSimpleName() + " and " + collisionDetection.getClass().getSimpleName());
                    }
                }
            }
        }

        public Boolean collides(Entity entity1, Entity entity2) {
            // Get position parts on entities
            PositionPart entMov1 = entity1.getPart(PositionPart.class);
            PositionPart entMov2 = entity2.getPart(PositionPart.class);

            // Get distance between entities
            float dx = (float) entMov1.getX() - (float) entMov2.getX();
            float dy = (float) entMov1.getY() - (float) entMov2.getY();
            float distanceBetween = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

            // Check if distance is less than the two radius', which means they are hitting each other
            float collisionDistance = entity1.getRadius() + entity2.getRadius();
            return distanceBetween < collisionDistance;
        }
}

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
        for (Entity hitterEntity : world.getEntities()) {
            for (Entity collidedEntity : world.getEntities()) {
                if (hitterEntity.getID().equals(collidedEntity.getID())) {
                    continue;
                }

                LifePart hitterEntityLifePart = hitterEntity.getPart(LifePart.class);
                LifePart collidedEntityLifePart = hitterEntity.getPart(LifePart.class);

                if (
                        hitterEntityLifePart.getLife() > 0
                                && this.collides(hitterEntity, collidedEntity)) {
                    hitterEntityLifePart.setIsHit(true);
                }
            }
        }
    }

    private boolean collides(Entity hitterEntity, Entity collidedEntity) {
        // Get data for collision detection
        PositionPart hitterPositionPart = hitterEntity.getPart(PositionPart.class);
        PositionPart collidedPositionPart = collidedEntity.getPart(PositionPart.class);

        // Calculate distance between
        float dx = (float) (hitterPositionPart.getX() - collidedPositionPart.getX());
        float dy = (float) (hitterPositionPart.getY() - collidedPositionPart.getY());
        float distanceBetween = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        // Check if distance is less than the two radius's, meaning that they are hitting each other
        float collisionDistance = hitterEntity.getRadius() + collidedEntity.getRadius();
        return distanceBetween < collisionDistance;
    }
}

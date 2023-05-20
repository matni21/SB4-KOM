package dk.sdu.mmmi.cbse.asteroid;


import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

/**
 * Asteroid control system, which controls all asteroids' movement, parameters and splitting.
 */
public class AsteroidControlSystem implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            PositionPart positionPart = asteroid.getPart(PositionPart.class);
            MovingPart movingPart = asteroid.getPart(MovingPart.class);
            LifePart lifePart = asteroid.getPart(LifePart.class);

            this.handleAsteroidSplitting(gameData, world, asteroid);

            movingPart.process(gameData, asteroid);
            positionPart.process(gameData, asteroid);
            lifePart.process(gameData, asteroid);

            if (lifePart.isDead()) {
                world.removeEntity(asteroid);
            }

            this.updateShape(asteroid);
        }
    }

    /**
     * Handle asteroid splitting, by checking if it needs to split.
     * <br>
     * Pre-condition: Asteroid is present in the game, and has life part. <br />
     * post-condition: Splitted asteroids are created if it isnt in splitting stage.
     *
     * @param gameData Data for the game
     * @param world World of the game
     * @param asteroid The asteroid that needs to be checked and handled
     */
    private void handleAsteroidSplitting(GameData gameData, World world, Entity asteroid) {
        // Get asteroid parts
        LifePart lifePart = asteroid.getPart(LifePart.class);

        // Discontinue if not hit or already dead
        if (!lifePart.isIsHit() || lifePart.isDead()) {
            return;
        }

        // Create new split asteroids
        AsteroidPlugin asteroidPlugin = new AsteroidPlugin();
        asteroidPlugin.createSplittetAsteroid(gameData, world, asteroid);


        return;
    }

    /**
     * Update the shape of Entity
     * <br />
     * Pre-condition: An entity that can be drawn, and the game has parsed once since last call for that entity <br />
     * Post-condition: An updated shape location for the entity
     *
     * @param entity Entity to update shape for
     */
    private void updateShape(Entity entity) {
        float[] shapeX = entity.getShapeX();
        float[] shapeY = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);

        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = positionPart.getRadians();

        LifePart lifePart = entity.getPart(LifePart.class);

        float[] distances = new float[8];
        switch (lifePart.getLife()) {
            default:
            case 1:
                distances = new float[] {10, 1, 1, 5, 8, 8, 9, 10};
                break;
            case 2:
                distances = new float[] {20, 10, 10, 15, 15, 18, 18, 20};
                break;
            case 3:
                distances = new float[] {27, 25, 23, 21, 21, 23, 25, 27};
                break;
        }

        for (int i = 0; i < 8; i++) {
            shapeX[i] = (float) (x + Math.cos(radians + Math.PI * (i / 4f)) * distances[i]);
            shapeY[i] = (float) (y + Math.sin(radians + Math.PI * (i / 4f)) * distances[i]);
        }

        entity.setShapeX(shapeX);
        entity.setShapeY(shapeY);
    }
}

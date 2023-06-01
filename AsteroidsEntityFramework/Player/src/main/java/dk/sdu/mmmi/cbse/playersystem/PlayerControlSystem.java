package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.ShootingPart;
import dk.sdu.mmmi.cbse.common.services.IBulletCreator;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.util.SPILocator;

import java.util.Collection;

/**
 *
 * @author jcs
 */
public class PlayerControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity player : world.getEntities(Player.class)) {
            PositionPart positionPart = player.getPart(PositionPart.class);
            MovingPart movingPart = player.getPart(MovingPart.class);
            ShootingPart shootingPart = player.getPart(ShootingPart.class);
            LifePart lifePart = player.getPart(LifePart.class);

            //Set moving to true if the corresponding key is pressed
            movingPart.setLeft(gameData.getKeys().isDown(GameKeys.LEFT));
            movingPart.setRight(gameData.getKeys().isDown(GameKeys.RIGHT));
            movingPart.setUp(gameData.getKeys().isDown(GameKeys.UP));

            //Process the parts
            movingPart.process(gameData, player);
            positionPart.process(gameData, player);
            shootingPart.process(gameData, player);
            lifePart.process(gameData, player);

            //Set shooting to true if space is pressed
            shootingPart.setShooting(gameData.getKeys().isDown(GameKeys.SPACE));
            if (shootingPart.getShooting()) {
                Collection<IBulletCreator> bulletPlugins = SPILocator.locateAll(IBulletCreator.class);
                //Create a bullet for each plugin
                for (IBulletCreator bulletPlugin : bulletPlugins) {
                    world.addEntity(bulletPlugin.create(player, gameData));
                }
            }
            //Remove the player if it is dead
            if (lifePart.isDead()) {
                world.removeEntity(player);
            }

            updateShape(player);
        }
    }

    /**
     * Update the shape of Entity
     * <br />
     * Pre-condition: An entity that can be drawn, and the game has parsed once since last call for that entity <br />
     * Post-condition: An updated shape location for that entity
     *
     * @param entity Entity to update the shape of
     */
    private void updateShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = positionPart.getRadians();

        shapex[0] = (float) (x + Math.cos(radians) * 8);
        shapey[0] = (float) (y + Math.sin(radians) * 8);

        shapex[1] = (float) (x + Math.cos(radians - 4 * 3.1415f / 5) * 8);
        shapey[1] = (float) (y + Math.sin(radians - 4 * 3.1145f / 5) * 8);

        shapex[2] = (float) (x + Math.cos(radians + 3.1415f) * 5);
        shapey[2] = (float) (y + Math.sin(radians + 3.1415f) * 5);

        shapex[3] = (float) (x + Math.cos(radians + 4 * 3.1415f / 5) * 8);
        shapey[3] = (float) (y + Math.sin(radians + 4 * 3.1415f / 5) * 8);

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }
}
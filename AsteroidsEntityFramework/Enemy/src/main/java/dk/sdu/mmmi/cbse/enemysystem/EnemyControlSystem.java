package dk.sdu.mmmi.cbse.enemysystem;

import java.lang.Math;
import java.util.Collection;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.ShootingPart;
import dk.sdu.mmmi.cbse.common.services.IBulletCreator;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.util.SPILocator;

public class EnemyControlSystem implements IEntityProcessingService {

    private float totalTime = 0f;

    private float getRandomNumber(float min, float max) {
        return (float) ((Math.random() * (max - min)) + min);
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(Enemy.class)) {
            PositionPart positionPart = enemy.getPart(PositionPart.class);
            MovingPart movingPart = enemy.getPart(MovingPart.class);
            ShootingPart shootingPart = enemy.getPart(ShootingPart.class);
            LifePart lifePart = enemy.getPart(LifePart.class);

            this.totalTime = (this.totalTime + gameData.getDelta()) % 100;

            float controlRotateAmplifier = (float) (Math.random() * 2f) + 0.1f;
            float controlGeneralAmplifier = (float) (Math.random() * 2f) + 0.1f;

            movingPart.setLeft(
                    (Math.sin(totalTime * controlRotateAmplifier + (Math.random() * 2f)) * controlGeneralAmplifier) < this.getRandomNumber(-0.3f, -controlGeneralAmplifier)
            );
            movingPart.setRight(
                    (Math.sin(totalTime * controlRotateAmplifier + (Math.random() * 2f)) * controlGeneralAmplifier) > this.getRandomNumber(0.8f, controlGeneralAmplifier)
            );
            movingPart.setUp(
                    this.getRandomNumber(0.02f, 1f) > this.getRandomNumber(0.4f, 1f)
            );

            movingPart.process(gameData, enemy);
            positionPart.process(gameData, enemy);
            shootingPart.process(gameData, enemy);
            lifePart.process(gameData, enemy);

            //Set random shooting
            shootingPart.setShooting(this.getRandomNumber(0f,1f) > 0.99f);
            if (shootingPart.getShooting()) {
                Collection<IBulletCreator> bulletPlugins = SPILocator.locateAll(IBulletCreator.class);
                //Add bullet to world
                for (IBulletCreator bulletPlugin : bulletPlugins) {
                    world.addEntity(bulletPlugin.create(enemy, gameData));
                }
            }

            //Remove enemy if dead
            if (lifePart.isDead()) {
                world.removeEntity(enemy);
            }

            updateShape(enemy);
        }
    }

    /**
     * Update the shape of Entity
     * <br />
     * Pre-condition: An entity that can be drawn, and the game has parsed once since last call for that entity <br />
     * Post-condition: An updated shape location for that entity
     *
     * @param entity Entity to update shape of
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


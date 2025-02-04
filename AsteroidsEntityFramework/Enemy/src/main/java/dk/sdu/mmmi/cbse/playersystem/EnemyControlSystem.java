package dk.sdu.mmmi.cbse.playersystem;

import com.badlogic.gdx.math.MathUtils;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.ShootingPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class EnemyControlSystem implements IEntityProcessingService {

    private float totalTime = 0f;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(Enemy.class)) {
            PositionPart positionPart = enemy.getPart(PositionPart.class);
            MovingPart movingPart = enemy.getPart(MovingPart.class);
            ShootingPart shootingPart = enemy.getPart(ShootingPart.class);
            LifePart lifePart = enemy.getPart(LifePart.class);

            this.totalTime = (this.totalTime + gameData.getDelta()) % 100;

            float controlRotateAmplifier = MathUtils.random(0.1f,2f);
            float controlGeneralAmplifier = MathUtils.random(0.1f,2f);

            movingPart.setLeft(
                    (MathUtils.sin(totalTime * controlRotateAmplifier + MathUtils.random(0f, 2f)) * controlGeneralAmplifier) < MathUtils.random(-0.3f, -controlGeneralAmplifier)
            );
            movingPart.setRight(
                    (MathUtils.sin(totalTime * controlRotateAmplifier + MathUtils.random(0f, 2f)) * controlGeneralAmplifier) > MathUtils.random(0.8f, controlGeneralAmplifier)
            );
            movingPart.setUp(
                    MathUtils.random(0.01f, 1f) > MathUtils.random(0.5f, 1f)
            );

            movingPart.process(gameData, enemy);
            positionPart.process(gameData, enemy);
            shootingPart.process(gameData, enemy);
            lifePart.process(gameData, enemy);

            shootingPart.setShooting(MathUtils.random(0f,1f) > 0.99f);

            if (lifePart.isDead()) {
                world.removeEntity(enemy);
            }

            updateShape(enemy);
        }
    }

    /**
     * Update the shape of entity
     * <br />
     * Pre-condition: An entity that can be drawn, and a game tick has passed since last call for entity <br />
     * Post-condition: Updated shape location for the entity
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


package dk.sdu.mmmi.cbse.playersystem;

import com.badlogic.gdx.math.MathUtils;
import dk.sdu.mmmi.cbse.common.data.Color;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.ShootingPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.awt.*;

public class EnemyPlugin implements IGamePluginService {

    private Entity enemy;

    public EnemyPlugin() {

    }

    @Override
    public void start(GameData gameData, World world) {

        // Add entities to the world
        enemy = createEnemyShip(gameData);
        world.addEntity(enemy);
    }

    /**
     * Create enemy ship entity with default data and parts
     * <br />
     * Pre-condition: New enemy entity has to be created for the game <br />
     * Post-condition: Enemy entity, that has default parameters and parts
     *
     * @param gameData Data for the game
     * @return Enemy entity with default parameters and parts
     */
    private Entity createEnemyShip(GameData gameData) {

        float deacceleration = 10;
        float acceleration = 150;
        float maxSpeed = 200;
        float rotationSpeed = 5;
        float x = MathUtils.random(0, gameData.getDisplayWidth());
        float y = MathUtils.random(0, gameData.getDisplayHeight());
        float radians = MathUtils.random(0f, (float) (2 * Math.PI));

        Entity enemyShip = new Enemy();

        enemyShip.setRadius(10);

        enemyShip.setColor(new Color(1,0,0,1));
        enemyShip.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed, 20));
        enemyShip.add(new PositionPart(x, y, radians));
        enemyShip.add(new LifePart(1, 0));
        enemyShip.add(new ShootingPart(0.3f));

        return enemyShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.removeEntity(enemy);
    }
}

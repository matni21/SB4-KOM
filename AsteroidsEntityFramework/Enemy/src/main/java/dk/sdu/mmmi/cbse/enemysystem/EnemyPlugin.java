package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Color;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.ShootingPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;


public class EnemyPlugin implements IGamePluginService {

    private Entity enemy;

    public EnemyPlugin() {

    }

    private float getRandomNumber(float min, float max) {
        return (float) ((Math.random() * (max - min)) + min);
    }

    @Override
    public void start(GameData gameData, World world) {

        // Add random amount entities to the world
        for (int i = 0; i < this.getRandomNumber(3, 5); i++) {
            world.addEntity(this.createEnemyShip(gameData));
        }
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
        float startSpeed = 20;
        float x = this.getRandomNumber(0, gameData.getDisplayWidth());
        float y = this.getRandomNumber(0, gameData.getDisplayHeight());
        float radians = this.getRandomNumber(0f, (float) (2 * Math.PI));

        Entity enemyShip = new Enemy();

        enemyShip.setRadius(10);
        //Set enemy color to red
        enemyShip.setColor(new Color(1,0,0,1));
        enemyShip.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed, startSpeed));
        enemyShip.add(new PositionPart(x, y, radians));
        //Set enemy to have 1 life
        enemyShip.add(new LifePart(1, 0));
        enemyShip.add(new ShootingPart(0.1f));

        return enemyShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entity
        for (Entity enemy : world.getEntities(Enemy.class)) {
            world.removeEntity(enemy);
        }
    }
}

package dk.sdu.mmmi.cbse.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import dk.sdu.mmmi.cbse.asteroid.AsteroidControlSystem;
import dk.sdu.mmmi.cbse.asteroid.AsteroidPlugin;
import dk.sdu.mmmi.cbse.bullet.BulletControlSystem;
import dk.sdu.mmmi.cbse.bullet.BulletPlugin;
import dk.sdu.mmmi.cbse.collision.CollisionDetector;
import dk.sdu.mmmi.cbse.common.data.Color;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.ShootingPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.managers.GameInputProcessor;
import dk.sdu.mmmi.cbse.playersystem.EnemyControlSystem;
import dk.sdu.mmmi.cbse.playersystem.EnemyPlugin;
import dk.sdu.mmmi.cbse.playersystem.PlayerControlSystem;
import dk.sdu.mmmi.cbse.playersystem.PlayerPlugin;
import java.util.ArrayList;
import java.util.List;

public class Game
        implements ApplicationListener {

    private static OrthographicCamera cam;
    private ShapeRenderer sr;

    private final GameData gameData = new GameData();
    private List<IEntityProcessingService> entityProcessors = new ArrayList<>();
    private List<IGamePluginService> entityPlugins = new ArrayList<>();
    private List<IPostEntityProcessingService> entityPostProcessors = new ArrayList<>();
    private World world = new World();

    @Override
    public void create() {

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        sr = new ShapeRenderer();

        Gdx.input.setInputProcessor(
                new GameInputProcessor(gameData)
        );

        // Adding Player
        IGamePluginService playerPlugin = new PlayerPlugin();
        IEntityProcessingService playerProcess = new PlayerControlSystem();
        entityPlugins.add(playerPlugin);
        entityProcessors.add(playerProcess);

        // Adding Enemy
        for (int i = 0; i < MathUtils.random(1, 5); i++) {
            IGamePluginService enemyPlugin = new EnemyPlugin();
            entityPlugins.add(enemyPlugin);
        }
        IEntityProcessingService enemyProcess = new EnemyControlSystem();
        entityProcessors.add(enemyProcess);

        // Bullet controller
        IEntityProcessingService bulletProcess = new BulletControlSystem();
        entityProcessors.add(bulletProcess);

        // Big Asteroid
        for (int i = 0; i < MathUtils.random(5, 20); i++) {
            IGamePluginService bigAsteroidPlugin = new AsteroidPlugin(MathUtils.random(2,3));
            entityPlugins.add(bigAsteroidPlugin);
        }

        // Big Asteroid controller
        IEntityProcessingService bigAsteroidProcess = new AsteroidControlSystem();
        entityProcessors.add(bigAsteroidProcess);

        // Adding Collision
        IPostEntityProcessingService collisionProcess = new CollisionDetector();
        entityPostProcessors.add(collisionProcess);

        // Lookup all Game Plugins using ServiceLoader
        for (IGamePluginService iGamePlugin : entityPlugins) {
            iGamePlugin.start(gameData, world);
        }
    }

    @Override
    public void render() {

        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());

        update();

        draw();

        gameData.getKeys().update();
    }

    private void update() {
        // Bullet
        for (Entity entity : world.getEntities()) {
            try {
                ShootingPart shootingPart = entity.getPart(ShootingPart.class);

                if (shootingPart.getShooting()) {
                    IGamePluginService bulletPlugin = new BulletPlugin(
                            entity
                    );
                    entityPlugins.add(bulletPlugin);
                    bulletPlugin.start(gameData, world);
                }
            } catch (NullPointerException error) {
                // Part does not shoot
                System.out.println("Part does not shoot");
            }
        }


        // Update
        for (IEntityProcessingService entityProcessorService : entityProcessors) {
            entityProcessorService.process(gameData, world);
        }


        // Collision detection
        for (IPostEntityProcessingService entityPostProcessorService : entityPostProcessors) {
            entityPostProcessorService.process(gameData, world);
        }
    }

    private void draw() {
        for (Entity entity : world.getEntities()) {

            Color color = entity.getColor();
            sr.setColor(color.getR(), color.getG(), color.getB(), color.getA());

            sr.begin(ShapeRenderer.ShapeType.Line);

            float[] shapex = entity.getShapeX();
            float[] shapey = entity.getShapeY();

            for (int i = 0, j = shapex.length - 1;
                    i < shapex.length;
                    j = i++) {

                sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
            }

            sr.end();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}

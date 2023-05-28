package dk.sdu.mmmi.cbse.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.common.data.Color;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.managers.GameInputProcessor;
import dk.sdu.mmmi.cbse.common.util.SPILocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collection;
import java.util.List;

public class Game implements ApplicationListener {

    private final List<IGamePluginService> gamePluginServices;
    private final List<IEntityProcessingService> entityProcessors;
    private final List<IPostEntityProcessingService> postEntityProcessors;

    public Game(List<IGamePluginService> gamePluginServices, List<IEntityProcessingService> entityProcessors, List<IPostEntityProcessingService> postEntityProcessors) {
        this.gamePluginServices = gamePluginServices;
        this.entityProcessors = entityProcessors;
        this.postEntityProcessors = postEntityProcessors;
    }

    private static OrthographicCamera cam;
    private ShapeRenderer sr;

    private final GameData gameData = new GameData();
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

        // Create initial objects
        for (IGamePluginService gamePlugin : gamePluginServices) {
            gamePlugin.start(gameData, world);
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
        for (IEntityProcessingService entityProcessorService : entityProcessors) {
            entityProcessorService.process(gameData, world);
        }
        for (IPostEntityProcessingService postEntityProcessors : postEntityProcessors) {
            postEntityProcessors.process(gameData, world);
        }
//        ((IProcessor) components.getBean("processorInjector")).process(gameData, world);
//        ((IProcessor) components.getBean("postProcessorInjector")).process(gameData, world);

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

//    private Collection<? extends IGamePluginService> getPluginServices() {
//        return SPILocator.locateAll(IGamePluginService.class);
//    }
//
//    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
//        return SPILocator.locateAll(IEntityProcessingService.class);
//    }
//
//    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
//        return SPILocator.locateAll(IPostEntityProcessingService.class);
//    }
}

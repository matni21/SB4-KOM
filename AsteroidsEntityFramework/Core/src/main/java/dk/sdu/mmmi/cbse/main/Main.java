package dk.sdu.mmmi.cbse.main;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	
	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ModuleConfig.class);

		for (String beanName : ctx.getBeanDefinitionNames()) {
			System.out.println(beanName);
		}
		
		Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
		cfg.setTitle("Asteroids");
		int width = 1200;
		int height = 800;
		cfg.setWindowSizeLimits(width,height,width,height);
		cfg.setWindowedMode(width, height);
		cfg.setResizable(false);

		new Lwjgl3Application(ctx.getBean(Game.class), cfg);

	}
	
}

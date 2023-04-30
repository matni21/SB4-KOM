package dk.sdu.mmmi.cbse.gamestates;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import dk.sdu.mmmi.cbse.entities.Bullet;
import dk.sdu.mmmi.cbse.entities.Enemy;
import dk.sdu.mmmi.cbse.entities.Player;
import dk.sdu.mmmi.cbse.managers.GameKeys;
import dk.sdu.mmmi.cbse.managers.GameStateManager;

import java.util.ArrayList;

public class PlayState extends GameState {

	private ShapeRenderer sr;

	private Player player;
	private ArrayList<Bullet> bullets;
	private ArrayList<Bullet> enemyBullets;
	private ArrayList<Enemy> enemies;

	private Enemy enemy;
	private float enTimer;
	private float enTime;

	public PlayState(GameStateManager gsm) {
		super(gsm);
	}

	public void init() {

		sr = new ShapeRenderer();

		bullets = new ArrayList<Bullet>();

		player = new Player(bullets);

		enTimer = 0;
		enTime = 15;
		enemyBullets = new ArrayList<Bullet>();

	}

	public void update(float dt) {
		//Get user input
		handleInput();
		//Update player
		player.update(dt);
		//update player bullets
		for (int i = 0; i < bullets.size(); i++){
			bullets.get(i).update(dt);
			if (bullets.get(i).shouldRemove()){
				bullets.remove(i);
				i--;
			}
		}

		//update enemy
		if (enemy == null){
			enTimer += dt;
			if (enTimer > enTime){
				enTimer = 0;
				//kinda a if statement:
				//int type = MathUtils.random() < 0.5 ? Enemy.SMALL : Enemy.LARGE;
				int type = Enemy.LARGE;

				int direction = MathUtils.random() < 0.5 ? Enemy.RIGHT : Enemy.LEFT;
				enemy = new Enemy(type, direction, player, enemyBullets);
			}
		}
		// if there already is an enemy, we need to move it
		else{
			enemy.update(dt);
			if (enemy.shouldRemove()){
				enemy = null;
			}
		}

		//update enemy bullets
		for (int i = 0; i < enemyBullets.size(); i++){
			enemyBullets.get(i).update(dt);
			if (enemyBullets.get(i).shouldRemove()){
				enemyBullets.remove(i);
				i--;
			}
		}


	}

	public void draw() {
		player.draw(sr);

		for (int i = 0; i < bullets.size(); i++){
			bullets.get(i).draw(sr);
		}

		//Draw enemy
		if (enemy != null){
			enemy.draw(sr);
		}

		//draw enemy bullets
		for (int i = 0; i < enemyBullets.size(); i++){
			enemyBullets.get(i).draw(sr);
		}

	}

	public void handleInput() {
		player.setLeft(GameKeys.isDown(GameKeys.LEFT));
		player.setRight(GameKeys.isDown(GameKeys.RIGHT));
		player.setUp(GameKeys.isDown(GameKeys.UP));
		if(GameKeys.isPressed(GameKeys.SPACE)){
			player.shoot();
		}
	}

	public void dispose() {}
}

package dk.sdu.mmmi.cbse.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import dk.sdu.mmmi.cbse.main.Game;

import java.util.ArrayList;

public class Enemy extends SpaceObject{

    private ArrayList<Bullet> bullets;
    private int type;
    public static final int LARGE = 0;
    public static final int SMALL = 1;

    private static int score;

    private float fireTimer;
    private float fireTime;

    private Player player;

    private float pathTimer;
    private float pathTime1;
    private float pathTime2;

    private int direction;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    private boolean remove;

    public Enemy(int type, int direction, Player player, ArrayList<Bullet> bullets){
        this.type = type;
        this.direction = direction;
        this.player = player;
        this.bullets = bullets;

        speed = 70;
        if (direction == LEFT){
            dx = -speed;
            x = Game.WIDTH;
        }
        else if (direction == RIGHT){
            dx = speed;
            x = 0;
        }
        y = MathUtils.random(Game.HEIGHT);
        shapex = new float[4];
        shapey = new float[4];
        setShape();

        fireTimer = 0;
        fireTime = 1;

        pathTimer = 0;
        pathTime1 = 2;
        pathTime2 = pathTime1 + 2;

    }

    private void setShape(){
        if (type == LARGE){
            shapex[0] = x + MathUtils.cos(radians) * 8;
            shapey[0] = y + MathUtils.sin(radians) * 8;

            shapex[1] = x + MathUtils.cos(radians - 4 * 3.1415f / 5) * 8;
            shapey[1] = y + MathUtils.sin(radians - 4 * 3.1145f / 5) * 8;

            shapex[2] = x + MathUtils.cos(radians + 3.1415f) * 5;
            shapey[2] = y + MathUtils.sin(radians + 3.1415f) * 5;

            shapex[3] = x + MathUtils.cos(radians + 4 * 3.1415f / 5) * 8;
            shapey[3] = y + MathUtils.sin(radians + 4 * 3.1415f / 5) * 8;


        }
        else if(type == SMALL){

            shapex[0] = x - 6;
            shapey[0] = y;

            shapex[1] = x - 2;
            shapey[1] = y - 3;

            shapex[2] = x + 2;
            shapey[2] = y - 3;

            shapex[3] = x + 6;
            shapey[3] = y;

            shapex[4] = x + 2;
            shapey[4] = y - 3;

            shapex[5] = x - 2;
            shapey[5] = y + 3;

        }

    }

    public int getScore(){
        return score;
    }

    public boolean shouldRemove(){
        return remove;
    }

    public void update(float dt){

        fireTimer += dt;
        if (fireTimer > fireTime){
            fireTimer = 0;
            if (type == LARGE){
                radians = MathUtils.random(2*3.1415f);
            }
            else if(type == SMALL){
                radians = MathUtils.atan2(
                        player.gety() - y,
                        player.getx() - x);
            }
            bullets.add(new Bullet(x,y,radians));
        }

        //Move along path
        pathTimer += dt;
        //Move forward
        if (pathTimer < pathTime1){
            dy = 0;
        }

        if(pathTimer > pathTime1 && pathTimer<pathTime2){
            dy = -speed;
        }

        if(pathTimer > pathTime1 + pathTime2){
            dy=0;
        }

        x += dx * dt;
        y += dy * dt;

        //screen wrap
        if (y<0){y = Game.HEIGHT;}
        if (x<0){x = Game.WIDTH;}

        //set shape
        setShape();

        //Check if remove
        if ((direction == RIGHT && x>Game.WIDTH) || (direction == LEFT && x < 0 )){
            remove = true;
        }

    }

    public void draw(ShapeRenderer sr){

        sr.setColor(Color.RED);
        sr.begin(ShapeRenderer.ShapeType.Line);

        for (int i = 0, j = shapex.length - 1;
             i < shapex.length;
             j = i++) {

            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);

        }

        sr.end();

    }

}

package biz.brainpowered.plane;

import biz.brainpowered.util.Util;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.Input.*;
import java.util.ArrayList;
import java.util.Vector;


public class Pilot extends ApplicationAdapter implements ApplicationListener
{
    // New Refactored Classe HEre
    Debug debug;
    Plane plane;
    ExplosionFactory explosionFactory;
    Ground ground;
    EnemyFactory enemyFactory1;
    EnemyFactory enemyFactory2;

    // TODO: UI Class
    SpriteBatch batch;
    Texture titleImg; // top be implemented

    // global config
    float planeScale = 0.5f;

    // TODO: Game Model
    Integer appWidth;
    Integer appHeight;
    String playerState;
    Integer score;
    float lastTimeScore;

    ArrayList<Enemy> enemyCollection = new ArrayList<Enemy>();// = new Sprite();
    Integer enemyCollectionSize;

    ArrayList<Explosion> expCollection = new ArrayList<Explosion>();// = new Sprite();
    Integer expCollectionSize;

    // Functions/Factories
    BulletFactory bulletFactory;

    ArrayList<Bullet> bulletCollection = new ArrayList<Bullet>();
    Integer bulletCollectionSize = 0;

    // SOUNDS:
    Sound pigeon;
    Enemy tmp;
    Camera camera;

    @Override
    public void create ()
    {
        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();

        // viewport
        camera = new OrthographicCamera(appWidth, appHeight);
        camera.update();

        // TODO: move Score Models into GameManager
        lastTimeScore = 0.0f;
        score = 0;

        enemyCollectionSize = 0;
        expCollectionSize = 0;

        batch = new SpriteBatch();

        // Explosion Sound
        pigeon = Gdx.audio.newSound(Gdx.files.internal("pigeon.mp3"));

        // Refactored Classes Init Here
        debug = new Debug();

        // asset manager to load asset config and generate types of Config objects
        PlaneConfig pc = new PlaneConfig();
        pc.normalTP = "airplane/PLANE_8_N.png";
        pc.leftTP = "airplane/PLANE_8_L.png";
        pc.rightTP = "airplane/PLANE_8_R.png";
        pc.scale = planeScale;

        plane = new Plane(pc, bulletCollection);
        plane.init();

        ground = new Ground("airplane/airPlanesBackground.png", 80.0f);

        explosionFactory = new ExplosionFactory("explosion19.png", 5, 5, 1/15f);
        explosionFactory.init();

        enemyFactory1 = new EnemyFactory("airplane/PLANE_1_N.png", planeScale);
        enemyFactory1.init();
        enemyFactory2 = new EnemyFactory("airplane/PLANE_2_N.png", planeScale);
        enemyFactory2.init();


        bulletFactory = new BulletFactory("airplane/B_2.png", 0.5f);
        bulletFactory.init();
        plane.setBulletFactory(bulletFactory);


        playerState = PlayerState.NORMAL;
//
//        // control management
//        directionKeyReleased = true;
//

        scheduleEnemies();
    }

    private void scheduleEnemies(){
        Timer.schedule(new Task() {
            @Override
            public void run() {
                EnemiesAndClouds();
            }
        }, 0, 1.0f);
    }

    private void EnemiesAndClouds()
    {
        // TODO: Determine condition for creating new Enemies
        Integer GoOrNot = Math.round((float)Math.random());
        System.out.println("GoOrNot: "+GoOrNot);
        if(GoOrNot == 1){
            Enemy enemy;

            //int randomEnemy = Util.getRandomNumberBetween(0, 1);
            double rand = Math.random();
            if(rand < 0.5f)
                enemy = enemyFactory1.create(appWidth/2, appHeight/2);
            else
                enemy = enemyFactory2.create(appWidth/2, appHeight/2);

            enemy.initPath(appWidth, appHeight);
            enemyCollection.add(enemy);
            enemyCollectionSize++;
        }
    }

    // todo: more disposal
    public void dispose() {
        // post refactor
        debug.dispose();
        plane.dispose();
        ground.dispose();
        explosionFactory.dispose();

        // pre-refactor
        batch.dispose();
        titleImg.dispose();
    }

    @Override
    public void render ()
    {
        Gdx.gl.glClearColor(0.5f, 1, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        ground.render(batch);
        //timeScore(); // todo scoring in game model

        // Post-Refactor
        // Debug
        debug.draw(batch, "Score: "+score);
        debug.draw(batch, "Planes: "+enemyCollection.size());
        debug.draw(batch, "Explosions: "+expCollection.size());
        debug.reset();

        plane.render(batch);




        for (int x = 0; x<bulletCollection.size(); x++)
        {
            bulletCollection.get(x).render(batch);
        }

        // Pre-Refactor - Collision Detection
        for (int x = 0; x<enemyCollection.size(); x++)
        {
            enemyCollection.get(x).render(batch);

            for (int y = 0; y<bulletCollection.size(); y++)
            {


                if(enemyCollection.get(x).checkOverlap(bulletCollection.get(y).getRectangle()))
                {
                    // sound
                    pigeon.play();
                    enemyCollection.get(x).setDispose();
                    bulletCollection.get(y).setDispose();
                    tmp = enemyCollection.get(x);

                    expCollection.add(explosionFactory.create((tmp._x + tmp.sprite.getWidth()/2), tmp._y + tmp.sprite.getHeight()/2));
                    //expCollectionSize++;
                    score += 250000;
                }
            }

            if(enemyCollection.get(x).checkOverlap(plane.getBoundingBox()))
            {
                // sound
                pigeon.play();
                // plane dies!
            }
        }

        for (int x = 0; x<expCollection.size(); x++)
        {
            expCollection.get(x).render(batch);
        }

        batch.end();

        // GC after rendering
        for (int x = 0; x<enemyCollection.size(); x++)
        {
            if(enemyCollection.get(x)._dispose)
            {
                enemyCollection.get(x).dispose();
                enemyCollection.remove(x);
            }
        }

        for (int x = 0; x<expCollection.size(); x++)
        {
            if (expCollection.get(x).elapsedTime > 1.6f){
                expCollection.get(x).dispose();
                expCollection.remove(x);
            }
        }

        for (int x = 0; x<bulletCollection.size(); x++)
        {
            if (bulletCollection.get(x)._dispose){
                bulletCollection.get(x).dispose();
                bulletCollection.remove(x);
            }
        }

        // A little Hack with arrays (kind of forcing the GC here)
        Util.cleanNulls(enemyCollection);
        Util.cleanNulls(expCollection);
        Util.cleanNulls(bulletCollection);
    }

    public void timeScore(float timeElapsed)
    {
        if ((lastTimeScore + 1.0f) < (timeElapsed))
        {
            score += 100;
            lastTimeScore = timeElapsed;
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
        //initPlane();
    }
}

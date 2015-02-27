package biz.brainpowered.plane;
import biz.brainpowered.plane.model.Game;
import biz.brainpowered.plane.factory.BulletFactory;
import biz.brainpowered.plane.factory.EnemyFactory;
import biz.brainpowered.plane.factory.ExplosionFactory;
import biz.brainpowered.plane.model.*;
import biz.brainpowered.plane.render.BumpRenderer;
import biz.brainpowered.plane.render.LightMapRenderer;
import biz.brainpowered.plane.render.ShaderUtil;
import biz.brainpowered.plane.render.SimpleLightsRenderer;
import biz.brainpowered.util.Util;
import biz.brainpowered.plane.entity.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import java.util.ArrayList;

public class Pilot implements ApplicationListener
{
    private Bullet tmpBullet;
    private Enemy tmpEnemy;

    // The good Parts
    Debug debug;
    Plane plane;
    Ground ground;
    ExplosionFactory explosionFactory;

    // TODO: replace ArrayList with shizzles
    ArrayList<Explosion> expCollection = new ArrayList<Explosion>();
    EnemyFactory enemyFactory1;
    EnemyFactory enemyFactory2;
    ArrayList<Enemy> enemyCollection = new ArrayList<Enemy>();
    BulletFactory bulletFactory;
    ArrayList<Bullet> bulletCollection = new ArrayList<Bullet>();

    BumpRenderer bumpRenderer; // TODO: WIP
    SimpleLightsRenderer simpleLightsRenderer;
    LightMapRenderer lightMapRenderer;

    // TODO: UI Class
    Texture titleImg; // top be implemented
    Texture planeTex; // top be implemented

    // TODO: global config
    float planeScale = 0.5f;
    // TODO: all hardcoded values to be moved to global config

    // TODO: Game Model
    Integer appWidth;
    Integer appHeight;
    Integer score;
    float lastTimeScore;

    // SOUNDS:
    Sound explode;
    Music cruise;

    Enemy tmp;
   // Camera camera;
    OrthographicCamera cam;

    SpriteBatch batch;
    BitmapFont font;

    Array<Light> lights = new Array<Light>();


    // WAVES
    private long waveStartTimer;
    private long waveStartTimerDiff;
    private int waveNumber;
    private boolean waveStart;
    private long waveDelay;

    // Player Health
    String playerHealthString;
    int playerHealth;
    ShapeRenderer shapeRenderer;

    @Override
    public void create ()
    {
        // Load Global Config

        // Load Asset Manifest

        // Asset Load 'all' Assets // todo: investigate load progress bar

        // viewport
        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(appWidth, appHeight);
        cam.update();
        batch = new SpriteBatch( 1000, null);

        // Explosion Sound
        explode = Gdx.audio.newSound(Gdx.files.internal("sound/95951__tmokonen__lazer.wav"));
        cruise = Gdx.audio.newMusic(Gdx.files.internal("sound/41143__digifishmusic__cruising-20013-feet.wav"));

        // Refactored Classes Init Here
        debug = new Debug();
        ShaderUtil.init();

        lightMapRenderer = new LightMapRenderer("shaders/pixelShader.glsl", "shaders/shadow/shadowMap.glsl", "shaders/shadow/shadowRender.glsl", lights);
        lightMapRenderer.init();

        // asset manager to load asset config and generate types of Config objects
        PlaneConfig pc = new PlaneConfig();
        pc.normalTP = "airplane/PLANE_8_N.png";
        pc.leftTP = "airplane/PLANE_8_L.png";
        pc.rightTP = "airplane/PLANE_8_R.png";
        pc.scale = planeScale;

        plane = new Plane(pc, bulletCollection);
        plane.init();

        ground = new Ground("airplane/airPlanesBackground.png", 80.0f);

        explosionFactory = new ExplosionFactory("explosion19.png", 5, 5, 1/25f, lights);
        explosionFactory.init();

        enemyFactory1 = new EnemyFactory("airplane/PLANE_1_N.png", planeScale);
        enemyFactory1.init();
        enemyFactory2 = new EnemyFactory("airplane/PLANE_2_N.png", planeScale);
        enemyFactory2.init();

        bulletFactory = new BulletFactory("airplane/B_2.png", 0.5f, lights);
        bulletFactory.init();
        plane.setBulletFactory(bulletFactory);

        // TODO: Complete Game Class
        Game.setPlayerState(Game.PlayerState.NORMAL); // TODO: Can use real enums too

        // TODO: move Score Models into GameManager
        lastTimeScore = 0.0f;
        score = 0;

        //cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //cam.setToOrtho(false);

        bumpRenderer = new BumpRenderer("shaders/bumpFragment.glsl", cam);
        bumpRenderer.init();

//        simpleLightsRenderer = new SimpleLightsRenderer("light.png", lights);
//        simpleLightsRenderer.init();

        font = new BitmapFont();

//        scheduleEnemies();
        EnemiesAndClouds();


        waveStartTimer = 0;
        waveStartTimerDiff = 0;
        waveStart = true;
        waveNumber = 0;
        waveDelay = 2000;

        playerHealth = 100;
        playerHealthString = "Health: "+playerHealth;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

//        cruise.setLooping(true);
//        cruise.play();
    }

//    private void scheduleEnemies(){
//        Timer.schedule(new Task() {
//            @Override
//            public void run() {
//                EnemiesAndClouds();
//            }
//        }, 0, 0.5f);
//    }

    private void EnemiesAndClouds()
    {
//        // TODO: Determine condition for creating new Enemies
//        Integer GoOrNot = Math.round((float)Math.random());
//        //System.out.println("GoOrNot: "+GoOrNot);
//        if(GoOrNot == 1){
//            Enemy enemy;
//            double rand = Math.random();
//            if(rand < 0.5f)
//                enemy = enemyFactory1.create(appWidth/2, appHeight/2);
//            else
//                enemy = enemyFactory2.create(appWidth/2, appHeight/2);
//
//            enemy.initPath(appWidth, appHeight);
//            enemyCollection.add(enemy);
//        }

//        Enemy enemy;
//        for(int i =0; i<5; i++) {
//            enemy = enemyFactory2.create(appWidth/2, appHeight/2);
//
//            enemy.initPath(appWidth, appHeight);
//            enemyCollection.add(enemy);
//        }
    }

    // todo: more disposal
    public void dispose() {
        // post refactor
        debug.dispose();
        plane.dispose();
        ground.dispose();
        explosionFactory.dispose();
        enemyFactory1.dispose();
        enemyFactory2.dispose();
        bulletFactory.dispose();

        // TODO: dispose renderers
//        simpleLightsRenderer.dipose();
//        lightMapRenderer.dipose();
//        bumpRenderer.dispose();

        // todo: re-init/load shaders on 'reinit'

        // pre-refactor
        batch.dispose();
    }

    private void gameUpdate() {
        if(waveStartTimer == 0 && enemyCollection.isEmpty()) {
            waveNumber++;
            waveStart = false;
            waveStartTimer = System.nanoTime();

        }else {
            waveStartTimerDiff = (System.nanoTime() - waveStartTimer) / 1000000;
            if(waveStartTimerDiff > waveDelay){
                waveStart = true;
                waveStartTimer = 0;
                waveStartTimerDiff = 0;
            }
        }

        // create enemies
        if (waveStart && enemyCollection.isEmpty()){
            createNewEnemies();
        }
    }

    public void createNewEnemies(){
        enemyCollection.clear();
        Enemy enemy;
        if (waveNumber == 1){
            for (int i=1; i<5; i++) {
                enemy = enemyFactory2.create(appWidth/2, appHeight/2);
                enemy.initPath(appWidth, appHeight);
                enemyCollection.add(enemy);
            }
        }
        if (waveNumber == 2){
            for (int i=1; i<7; i++) {
                enemy = enemyFactory2.create(appWidth/2, appHeight/2);
                enemy.initPath(appWidth, appHeight);
                enemyCollection.add(enemy);
            }
        }
        if (waveNumber == 3){
            for (int i=1; i<10; i++) {
                enemy = enemyFactory2.create(appWidth/2, appHeight/2);
                enemy.initPath(appWidth, appHeight);
                enemyCollection.add(enemy);
            }
        }
    }
    @Override
    public void render ()
    {
        gameUpdate();
        // delta time is useful for rendering -> pass it into all render/draw functions
        final float dt = Gdx.graphics.getRawDeltaTime();

        //simpleLightsRenderer.render(batch);// TODO: get reference for FBO for simple lights and integrate into Final Renderer

        // render lights
        lightMapRenderer.renderLights(batch, lights, enemyCollection); // todo: implement complete Occlusion entity collection object

        // BEGIN: render final
        lightMapRenderer.render(batch);

        ground.update(dt, 1.0f);
        ground.render(batch);
        //timeScore(); // todo scoring in game model

        // Post-Refactor
        // Debug
        debug.draw(batch, "Score: "+score);
        debug.draw(batch, "Planes: "+enemyCollection.size());
        debug.draw(batch, "Explosions: "+expCollection.size());
        debug.draw(batch, "Bullets: "+bulletCollection.size());
        debug.draw(batch, "Lights: "+lights.size);
        debug.draw(batch, "Health: "+playerHealth);
        debug.reset();
        // END Debug

        // draw wave number
        if (waveStartTimer != 0) {
            String waveString = "- W A V E "+waveNumber+" -";
            float stringLength = font.getBounds(waveString).width;
            float alpha = (float) (1f * Math.sin(3.14 * waveStartTimerDiff / waveDelay));
            if (alpha>255)alpha=255;
            font.setColor(new Color(1f,1f,1f,alpha));
            font.draw(batch, waveString, (appWidth / 2) - stringLength/2, (appHeight / 2));
        }

        //bumpRenderer.render(batch, lights, plane);
        //plane.render(batch);

        // TODO: SpriteEntity Component System will check all Entities for type Renderable (and then do it in the loop)
        // Render Bullets
        for (int y = 0; y<bulletCollection.size(); y++) {
            tmpBullet = bulletCollection.get(y);
            tmpBullet.render(batch);
        }

        // Enemy Render and Collision Detection
        for (int x = 0; x<enemyCollection.size(); x++)
        {
            tmpEnemy = enemyCollection.get(x);
            tmpEnemy.updatePos();
            tmpEnemy.render(batch);

            // Player collision detection
            if(tmpEnemy.checkOverlap(plane.getSprite().getBoundingRectangle())){
                expCollection.add(explosionFactory.create((tmpEnemy.getX() + (tmpEnemy.getSprite().getWidth()/2) - 50), tmpEnemy.getY() + (tmpEnemy.getSprite().getHeight()/2) - 50));
                explode.play();
                // reduce health
                playerHealth -= 20;
            }

            for (int y = 0; y<bulletCollection.size(); y++)
            {
                tmpBullet = bulletCollection.get(y);
                if (tmpBullet.getY() > appHeight) {
                    tmpBullet.setDispose(true);
                }

                if(tmpEnemy.checkOverlap(tmpBullet.getRectangle()))
                {
                    tmpEnemy.setDispose(true);
                    tmpBullet.setDispose(true);
                    expCollection.add(explosionFactory.create((tmpEnemy.getX() + (tmpEnemy.getSprite().getWidth()/2) - 50), tmpEnemy.getY() + (tmpEnemy.getSprite().getHeight()/2) - 50));
                    explode.play();
                    // TODO: Score Manager
                    score += 250000;
                }
            }

            // Collision with Enemy
//            if(tmpEnemy.checkOverlap(plane.getBoundingBox()))
//            {
//                // sound
//                pigeon.play();
//                // plane dies!
//            }
        }

        for (int x = 0; x<expCollection.size(); x++)
        {
            expCollection.get(x).render(batch);
        }

        if(playerHealth <= 0) {
            playerHealth = 0;
            String gameOverString = "- GAME OVER -";
            float stringLength = font.getBounds(gameOverString).width;
            font.setColor(new Color(1f,1f,1f,1f));
            font.draw(batch ,gameOverString , (appWidth / 2) - stringLength/2, (appHeight / 2));
        }

        shapeRenderer.setProjectionMatrix(cam.combined);
        Rectangle playerRect = plane.getSprite().getBoundingRectangle();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);
        shapeRenderer.rect(playerRect.x, playerRect.y, playerRect.width, playerRect.height);
        shapeRenderer.end();

        // Batch Rendering Done
        batch.end();
        lightMapRenderer.end();
        // END: Render Final

        // Bump
        //
        bumpRenderer.render(batch, lights, plane);

        // GC after rendering
        // TODO: Note - ArrayLists are lame (use Array<> instead courtesy of LibGDX)
        for (int x = 0; x<enemyCollection.size(); x++)
        {
            if(enemyCollection.get(x).getDispose())
            {
                enemyCollection.get(x).dispose();
                enemyCollection.remove(x);
            }
        }

        for (int x = 0; x<expCollection.size(); x++)
        {
            if (expCollection.get(x).getElapsedTime() > 1.0f){
                expCollection.get(x).dispose();
                expCollection.remove(x);
            }
        }

        for (int x = 0; x<bulletCollection.size(); x++)
        {
            if (bulletCollection.get(x).getDispose()){
                bulletCollection.get(x).dispose();
                bulletCollection.remove(x);
            }
        }

        // A little Hack with arrays (kind of forcing the GC here)
        // TODO: potentially avoid this...
        Util.cleanNulls(enemyCollection);
        Util.cleanNulls(expCollection);
        Util.cleanNulls(bulletCollection);
    }

    // todo: figure out a Score Model in the Game Manager
    public void timeScore(float timeElapsed)
    {
        if ((lastTimeScore + 1.0f) < (timeElapsed))
        {
            score += 100;
            lastTimeScore = timeElapsed;
        }
    }

    @Override
    public void resize(final int width, final int height)
    {
        //simpleLightsRenderer.resize(width, height);
        lightMapRenderer.resize(width, height, batch);
        bumpRenderer.resize(width, height);
    }

    @Override
    public void pause() {
        // todo: stop timers, unload shaders
    }

    @Override
    public void resume() {
        //initPlane();
        // todo: reinit shaders
    }
}

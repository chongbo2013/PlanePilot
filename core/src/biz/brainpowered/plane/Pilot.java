package biz.brainpowered.plane;

import biz.brainpowered.util.Util;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.ArrayList;


public class Pilot extends ApplicationAdapter implements ApplicationListener
{
    Debug debug;
    Plane plane;
    Ground ground;
    ExplosionFactory explosionFactory;
    ArrayList<Explosion> expCollection = new ArrayList<Explosion>();
    EnemyFactory enemyFactory1;
    EnemyFactory enemyFactory2;
    ArrayList<Enemy> enemyCollection = new ArrayList<Enemy>();
    BulletFactory bulletFactory;
    ArrayList<Bullet> bulletCollection = new ArrayList<Bullet>();

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

    // SOUNDS:
    Sound pigeon;
    Enemy tmp;
    Camera camera;

    //used to make the light flicker
    public float zAngle;
    public static final float zSpeed = 15.0f;
    public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;

    private boolean	lightMove = false;
    private boolean lightOscillate = false;
    private Texture light;
    private FrameBuffer fbo;

    //read our shader files
    private String vertexShader;// = Gdx.files.internal("shaders/vertexShader.glsl").readString();
    private String defaultPixelShader;// = Gdx.files.internal("shaders/defaultPixelShader.glsl").readString();
    private String finalPixelShader;// =  Gdx.files.internal("shaders/pixelShader.glsl").readString();
    private ShaderProgram defaultShader;
    private ShaderProgram finalShader;

    //values passed to the shader
    public static final float ambientIntensity = .99f;
    public static final Vector3 ambientColor = new Vector3(0.99f, 0.99f, 0.99f);

    @Override
    public void create ()
    {
        // viewport
        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(appWidth, appHeight);
        camera.update();
        batch = new SpriteBatch();

        // Explosion Sound
        pigeon = Gdx.audio.newSound(Gdx.files.internal("pigeon.mp3"));

        // Refactored Classes Init Here
        debug = new Debug();


        //read our shader files
        vertexShader = Gdx.files.internal("shaders/vertexShader.glsl").readString();
        defaultPixelShader = Gdx.files.internal("shaders/defaultPixelShader.glsl").readString();
        finalPixelShader =  Gdx.files.internal("shaders/pixelShader.glsl").readString();


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

        ShaderProgram.pedantic = false;
        defaultShader = new ShaderProgram(vertexShader, defaultPixelShader);
        finalShader = new ShaderProgram(vertexShader, finalPixelShader);

        finalShader.begin();
        finalShader.setUniformi("u_lightmap", 1);
        finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
                ambientColor.z, ambientIntensity);
        finalShader.end();

        light = new Texture("light.png");

        playerState = PlayerState.NORMAL; // TODO: Can use real enums too

        // TODO: move Score Models into GameManager
        lastTimeScore = 0.0f;
        score = 0;
//
//        // control management
//        directionKeyReleased = true;

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
        //System.out.println("GoOrNot: "+GoOrNot);
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
        final float dt = Gdx.graphics.getRawDeltaTime();

        // Occilation angle
        zAngle += dt * zSpeed;
        while(zAngle > PI2)
            zAngle -= PI2;

        //draw the light to the FBO
        fbo.begin();
        batch.setShader(defaultShader);
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        // TODO:  Explosion Class to implement light source
        boolean lightOscillate = true;
        float lightSize = lightOscillate? (100.00f + (25.0f * (float)Math.sin(zAngle)) + 25.0f* MathUtils.random()): 150.0f;
        //lightSize = 150f;
        for (int x = 0; x<expCollection.size(); x++)
        {
            batch.draw(light,
                    expCollection.get(x)._x + 32 - (lightSize*0.5f),
                    expCollection.get(x)._y + 32 - (lightSize*0.5f),
                    lightSize,
                    lightSize);
            // todo: light size to change over time
        }
        for (int x = 0; x<bulletCollection.size(); x++)
        {
            batch.draw(light,
                    bulletCollection.get(x)._x + 6 - (lightSize*0.5f),
                    bulletCollection.get(x)._y + 16 - (lightSize*0.5f),
                    lightSize,
                    lightSize);
        }
        batch.end();
        fbo.end();

        //draw the actual scene
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setShader(finalShader);
        batch.setColor(0.5f, 0.5f, 1f, 1f);
        batch.begin();
        // Bind Textures here - access them in the Shader Program (little trick!)
        fbo.getColorBufferTexture().bind(1); //this is important! bind the FBO to the 2nd texture unit
        light.bind(0); //we force the binding of a texture on first texture unit to avoid artefacts
        //this is because our default and ambiant shader dont use multi texturing...
        //you can basically bind anything, it doesnt matter

        ground.render(batch);
        //timeScore(); // todo scoring in game model

        // Post-Refactor
        // Debug
        debug.draw(batch, "Score: "+score);
        debug.draw(batch, "Planes: "+enemyCollection.size());
        debug.draw(batch, "Explosions: "+expCollection.size());
        debug.reset();

        plane.render(batch);

        // Bullets
        for (int x = 0; x<bulletCollection.size(); x++)
        {
            bulletCollection.get(x).render(batch);
        }

        // Enemy Render and Collision Detection
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

                    expCollection.add(explosionFactory.create((tmp._x + (tmp.sprite.getWidth()/2) - 50), tmp._y + (tmp.sprite.getHeight()/2) - 50));
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

        // Batch Rendering Done
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
    public void resize(final int width, final int height) {

        // todo: update camera
//        cam = new OrthographicCamera(20.0f, 20.0f * height / width);
//        cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
//        cam.update();

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);

        finalShader.begin();
        finalShader.setUniformf("resolution", width, height);
        finalShader.end();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        //initPlane();
    }
}

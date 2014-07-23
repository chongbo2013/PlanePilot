package biz.brainpowered.plane;

import biz.brainpowered.util.Util;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import java.util.ArrayList;

import static biz.brainpowered.plane.ShaderUtil.createShader;

public class Pilot implements ApplicationListener
{
    // todo: find a place for this
    public enum PlayerState {
        NORMAL
    };

    private Light tmpLight;
    private Bullet tmpBullet;
    private Enemy tmpEnemy;

    // The good Parts
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

    BumpShader bumpShader; // todo: class name is ambiguous (WIP)

    // TODO: UI Class
    Texture titleImg; // top be implemented
    Texture planeNormals; // top be implemented
    Texture planeTex; // top be implemented

    // global config
    float planeScale = 0.5f;

    // TODO: Game Model
    Integer appWidth;
    Integer appHeight;
    PlayerState playerState;
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
    private FrameBuffer fbo;

    //read our shader files
    private String finalPixelShader;
    private ShaderProgram finalShader;

    //values passed to the shader
    public static final float ambientIntensity = .99f;
    public static final Vector3 ambientColor = new Vector3(0.99f, 0.99f, 0.99f);

    // Shadow Casting
    private int lightSize = 256;
    private float upScale = 1.0f; //for example; try lightSize=128, upScale=1.5f

    SpriteBatch batch;
    OrthographicCamera cam;
    BitmapFont font;

    TextureRegion shadowMap1D; //1 dimensional shadow map
    TextureRegion occluders;   //occluder map
    TextureRegion finalLightMap;   //occluder map

    FrameBuffer shadowMapFBO;
    FrameBuffer occludersFBO;

    FrameBuffer finalLightMapFBO;
    Texture finalLightMapTex;

    Texture casterSprites;
    Texture light;

    ShaderProgram shadowMapShader, shadowRenderShader;

    Array<Light> lights = new Array<Light>();

    boolean additive = true;
    boolean softShadows = true;

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
        ShaderUtil.init();

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

        explosionFactory = new ExplosionFactory("explosion19.png", 5, 5, 1/25f, lights);
        explosionFactory.init();

        enemyFactory1 = new EnemyFactory("airplane/PLANE_1_N.png", planeScale);
        enemyFactory1.init();
        enemyFactory2 = new EnemyFactory("airplane/PLANE_2_N.png", planeScale);
        enemyFactory2.init();

        bulletFactory = new BulletFactory("airplane/B_2.png", 0.5f, lights);
        bulletFactory.init();
        plane.setBulletFactory(bulletFactory);

        finalShader = new ShaderProgram(ShaderUtil.defaultVertexShader, finalPixelShader);
        finalShader.begin();
        finalShader.setUniformi("u_lightmap", 1); //  1 value refers to the FBO-to-ShaderProgramTextureUnit  binding
        finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y, ambientColor.z, ambientIntensity);
        finalShader.end();

        light = new Texture("light.png");
        planeNormals = new Texture(Gdx.files.internal("airplane/PLANE_8_N_NRM.png"));

        playerState = PlayerState.NORMAL; // TODO: Can use real enums too

        // TODO: move Score Models into GameManager
        lastTimeScore = 0.0f;
        score = 0;

        // Shadow Map Setup
        planeTex = new Texture(Gdx.files.internal("airplane/PLANE_8_N.png"));

        // Bump
        bumpShader = new BumpShader();
        bumpShader.init();
        // End Bump

        // renders occluders to 1D shadow map
        shadowMapShader = createShader(ShaderUtil.defaultVertexShader, Gdx.files.internal("shaders/shadow/shadowMap.glsl").readString());
        // samples 1D shadow map to create the blurred soft shadow
        shadowRenderShader = createShader(ShaderUtil.defaultVertexShader, Gdx.files.internal("shaders/shadow/shadowRender.glsl").readString());

        //the occluders
        casterSprites = new Texture("explosion19.png");
        //the light sprite
        light = new Texture("light.png");

        //build frame buffers
        occludersFBO = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, lightSize, false);
        occluders = new TextureRegion(occludersFBO.getColorBufferTexture());
        occluders.flip(false, true);

        //our 1D shadow map, lightSize x 1 pixels, no depth
        shadowMapFBO = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, 1, false);
        Texture shadowMapTex = shadowMapFBO.getColorBufferTexture();

        //use linear filtering and repeat wrap mode when sampling
        shadowMapTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        shadowMapTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        //for debugging only; in order to render the 1D shadow map FBO to screen
        shadowMap1D = new TextureRegion(shadowMapTex);
        shadowMap1D.flip(false, true);

        finalLightMapFBO = new FrameBuffer(Pixmap.Format.RGBA8888, appWidth, appHeight, false);
        finalLightMapTex = finalLightMapFBO.getColorBufferTexture();

        finalLightMap = new TextureRegion(finalLightMapTex);
        finalLightMap.flip(false, true);

        font = new BitmapFont();

        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.setToOrtho(false);

        scheduleEnemies();
    }

    private void scheduleEnemies(){
        Timer.schedule(new Task() {
            @Override
            public void run() {
                EnemiesAndClouds();
            }
        }, 0, 0.5f);
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
        // Lighting Rendering HEre
        final float dt = Gdx.graphics.getRawDeltaTime();

        // Occilation angle
        zAngle += dt * zSpeed;
        while(zAngle > PI2)
            zAngle -= PI2;

        // these are simpler lights - good for ones without Bump/Shadow flags
        //draw the light to the FBO
//        fbo.begin();
//        batch.setShader(defaultShader); // passthrough
//        Gdx.gl.glClearColor(0f, 0f, 0f, 0f); // clear with white opaque
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        batch.begin();
//
//        // TODO:  Explosion Class to implement light source
//        boolean lightOscillate = true;
//        float lightSize = lightOscillate? (100.00f + (25.0f * (float)Math.sin(zAngle)) + 25.0f* MathUtils.random()): 150.0f;
//        //lightSize = 150f;
//        for (int x = 0; x<expCollection.size(); x++)
//        {
//            batch.draw(light,
//                    expCollection.get(x)._x + 32 - (lightSize*0.5f),
//                    expCollection.get(x)._y + 32 - (lightSize*0.5f),
//                    lightSize,
//                    lightSize);
//            // todo: light size to change over time
//        }
//        for (int x = 0; x<bulletCollection.size(); x++)
//        {
//            batch.draw(light,
//                    bulletCollection.get(x)._x + 6 - (lightSize*0.5f),
//                    bulletCollection.get(x)._y + 16 - (lightSize*0.5f),
//                    lightSize,
//                    lightSize);
//        }
//        batch.end();
//        fbo.end();


        // Shadow Rendering
        // all stored in the "finalLightMapFBO"
        //clear frame
        Gdx.gl.glClearColor(0.25f,0.25f,0.25f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (additive)
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i=0; i<lights.size; i++)
        {
            tmpLight = lights.get(i);
            // only render lights that cast shadows
            if(tmpLight.castShadows)
                renderLight(tmpLight);
        }
        if (additive)
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        // End Shadow Rendering


        // Render 'Diffuse' Everything Else (and Put Shadow Maps ontop)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setShader(finalShader);
        batch.begin();
        finalLightMapFBO.getColorBufferTexture().bind(1); //this is important! bind the FBO to the 2nd texture unit
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
        debug.draw(batch, "Bullets: "+bulletCollection.size());
        debug.draw(batch, "Lights: "+lights.size);
        debug.reset();
        // END Debug

        plane.render(batch);

        // Bullets
        for (int x = 0; x<bulletCollection.size(); x++)
        {
            tmpBullet = bulletCollection.get(x);
            tmpBullet.render(batch);
            //System.out.println("tmpBullet._x:"+tmpBullet._x);
            if (tmpBullet._y > appHeight) {
                bulletCollection.get(x).setDispose();
            }
        }

        // Enemy Render and Collision Detection
        for (int x = 0; x<enemyCollection.size(); x++)
        {
            tmpEnemy = enemyCollection.get(x);
            tmpEnemy.updatePos();
            tmpEnemy.render(batch);

            for (int y = 0; y<bulletCollection.size(); y++)
            {
                if(tmpEnemy.checkOverlap(bulletCollection.get(y).getRectangle()))
                {
                    // sound
                    pigeon.play();
                    tmpEnemy.setDispose();
                    bulletCollection.get(y).setDispose();
                    //tmp = enemyCollection.get(x);

                    expCollection.add(explosionFactory.create((tmpEnemy._x + (tmpEnemy.sprite.getWidth()/2) - 50), tmpEnemy._y + (tmpEnemy.sprite.getHeight()/2) - 50));
                    //expCollectionSize++;
                    score += 250000;
                }
            }

            if(tmpEnemy.checkOverlap(plane.getBoundingBox()))
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

        // Bump
        bumpShader.render(batch);


        // Empty out the Light Map once per frame (thanks!)
        finalLightMapFBO.begin();
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        finalLightMapFBO.end();

        // GC after rendering
        // TODO: Note - ArrayLists are lame (use Array<> instead courtesy of LibGDX)
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
            if (expCollection.get(x).elapsedTime > 1.0f){
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

    // Renders Lights that cast shadows - todo: Specify a Separate Shadow Cast Rendering Class
    void renderLight(Light o)
    {
        float mx = o.x;
        float my = o.y;
        float lightSize = this.lightSize;//o.scale *

        //STEP 1. render light region to occluder FBO

        //bind the occluder FBO
        occludersFBO.begin();

        //clear the FBO to WHITE/ TRANSPARENT
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set the orthographic camera to the size of our FBO
        cam.setToOrtho(false, occludersFBO.getWidth(), occludersFBO.getHeight());

        //translate camera so that light is in the center
        cam.translate(mx - lightSize/2f, my - lightSize/2f);

        //update camera matrices
        cam.update();

        //set up our batch for the occluder pass
        batch.setProjectionMatrix(cam.combined);
        batch.setShader(null); //use default shader
        batch.begin();

        // ... draw any sprites that will cast shadows here ... //
        for (int x = 0; x<enemyCollection.size(); x++) {
            enemyCollection.get(x).render(batch);
        }

        //end the batch before unbinding the FBO
        batch.end();

        //unbind the FBO
        occludersFBO.end();

        //STEP 2. build a 1D shadow map from occlude FBO

        //bind shadow map
        shadowMapFBO.begin();

        //clear it
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set our shadow map shader
        batch.setShader(shadowMapShader);
        batch.begin();
        shadowMapShader.setUniformf("resolution", lightSize, lightSize);
        shadowMapShader.setUniformf("upScale", upScale);

        //reset our projection matrix to the FBO size
        cam.setToOrtho(false, shadowMapFBO.getWidth(), shadowMapFBO.getHeight());
        batch.setProjectionMatrix(cam.combined);

        //draw the occluders texture to our 1D shadow map FBO
        batch.draw(occluders.getTexture(), 0, 0, lightSize, shadowMapFBO.getHeight());

        //flush batch
        batch.end();

        //unbind shadow map FBO
        shadowMapFBO.end();

        //STEP 3. render the blurred shadows

        finalLightMapFBO.begin();
        // DONT CLEAR HERE - only after each frame
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //reset projection matrix to screen !!!
        cam.setToOrtho(false);
        batch.setProjectionMatrix(cam.combined);

        //set the shader which actually draws the light/shadow
        batch.setShader(shadowRenderShader);
        batch.begin();

        shadowRenderShader.setUniformf("resolution", lightSize, lightSize);
        shadowRenderShader.setUniformf("softShadows", softShadows ? 1f : 0f);

        //set color to light
        batch.setColor(o.color);
        float finalSize = lightSize * o.scale;

        //draw centered on light position
        batch.draw(shadowMap1D.getTexture(), mx-finalSize/2f, my-finalSize/2f, finalSize, finalSize);

        //flush the batch before swapping shaders into the FrameBuffer
        batch.end();
        finalLightMapFBO.end();

        //reset color
        batch.setColor(Color.WHITE);
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
        //Shading (2d)
        cam.setToOrtho(false, width, height);
        batch.setProjectionMatrix(cam.combined);

        // bubble up!
        bumpShader.resize(width, height);

        // todo: check if FBOs need to be re-inited

        // Lighting
        //fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);

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

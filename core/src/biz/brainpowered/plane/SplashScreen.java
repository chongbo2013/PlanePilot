package biz.brainpowered.plane;

import aurelienribon.tweenengine.*;
import biz.brainpowered.plane.comp.ComponentGroupManager;
import biz.brainpowered.plane.comp.GraphicsComponentGroup;
import biz.brainpowered.plane.comp.InputComponentGroup;
import biz.brainpowered.plane.comp.LightComponentGroup;
import biz.brainpowered.plane.comp.entities.ProgressBarEntity;
import biz.brainpowered.plane.manager.GameManager;
import biz.brainpowered.plane.tweenable.SpriteAccessor;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Splash Screen
 * Start Loading Configs, Scenes, Assets
 * when Complete, Switch to Main Menu
 */
public class SplashScreen implements Screen {
    private boolean init;
    private boolean finalInit;

    int appWidth;
    int appHeight;

    private SpriteBatch batch;
    private Game myGame;
    private Texture texture;
    private Sprite sprite;
    private OrthographicCamera camera;
    private long showTime;
    private long initTime;
    private int rendCount;

    private BitmapFont bmFont;
    private String fontText = "Loading";

    private static TweenManager tweenManager;

    // inline for now - todo: go global
    GameManager gm;
    Screen iAm;
    TweenCallback cb;

    public SplashScreen (Game game) {
        iAm = this;
        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();
        Gdx.app.log("Splash Screen", "constructor called");
        myGame = game; // ** get Game parameter **//
        camera = new OrthographicCamera();
        camera.setToOrtho(false, appWidth, appHeight);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        gm = new GameManager();
        gm.init();

        // Load AssetLoader with Asset List
        GameManager.assetLoader.loadAsset("font/Reload.fnt", "BitmapFont");
        GameManager.assetLoader.loadAsset("splash.png", "Texture");
        GameManager.assetLoader.loadAsset("progressbar.png", "Texture");

        // TODO: also can do multi step

        //Step 1:
        // Get Splash Assets and Draw for xamount

        // then Step 2:
        // get all other resources _> and switch to Main menu when loaded complete

        init = false;
        finalInit = false;

        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        tweenManager = new TweenManager();


        ComponentGroupManager.getInstance().registerComponentGroup(ComponentGroupManager.GRAPHICS, GraphicsComponentGroup.getInstance());
        ComponentGroupManager.getInstance().registerComponentGroup(ComponentGroupManager.INPUT, InputComponentGroup.getInstance());
        ComponentGroupManager.getInstance().registerComponentGroup(ComponentGroupManager.LIGHT, LightComponentGroup.getInstance());


        cb = new TweenCallback()
        {
            @Override
            public void onEvent(int type, BaseTween<?> source)
            {
                if(finalInit) {
                    myGame.setScreen(new MainMenuScreen(myGame));
                }else {
                    finalCheck();
                }
                Gdx.app.log("Splash Sprite", "tween callback called");
            }
        };
    }

    private void init () {
        bmFont = GameManager.assetLoader.get("font/Reload.fnt", "BitmapFont");
        texture = GameManager.assetLoader.get("splash.png", "Texture"); //** texture is now the splash image **//

        GameManager.entityManager.createProgressBarEntity("SplashScreenProgressBar", "progressbar.png", appWidth/2, appHeight*0.2);

        sprite = new Sprite(texture);
        sprite.setColor(sprite.getColor().r, sprite.getColor().g, sprite.getColor().b, 0f);
        sprite.setOriginCenter();
        sprite.setX(appWidth / 2 - texture.getWidth() / 2);
        sprite.setY(appHeight / 2 - texture.getHeight() / 2);

        Timeline.createSequence()
                .push(Tween.set(sprite, SpriteAccessor.OPACITY).target(0f))
                .push(Tween.set(sprite, SpriteAccessor.SCALE).target(0f, 0f))
                .beginParallel()
                    .push(Tween.to(sprite, SpriteAccessor.OPACITY, 0.3f).target(1f).ease(TweenEquations.easeInOutQuad))
                    .push(Tween.to(sprite, SpriteAccessor.SCALE, 0.3f).target(1.3f, 1.3f).ease(TweenEquations.easeInOutQuad))
                .end()
                .push(Tween.to(sprite, SpriteAccessor.SCALE, 0.1f).target(1f, 1f).ease(TweenEquations.easeInOutQuad))
                .start(tweenManager);
        init = true;
        initTime = TimeUtils.millis();

        finalInit();
    }

    private void finalInit(){

        // todo: Config based loading
        GameManager.assetLoader.loadAsset("airplane/B_2.png", "Texture");
        GameManager.assetLoader.loadAsset("airplane/airPlanesBackground.png", "Texture");
        GameManager.assetLoader.loadAsset("airplane/PLANE_8_N.png", "Texture");
        GameManager.assetLoader.loadAsset("airplane/PLANE_8_L.png", "Texture");
        GameManager.assetLoader.loadAsset("airplane/PLANE_8_R.png", "Texture");
        GameManager.assetLoader.loadAsset("airplane/PLANE_8_SHADOW.png", "Texture");
        GameManager.assetLoader.loadAsset("airplane/PLANE_PROPELLER_1.png", "Texture");
        GameManager.assetLoader.loadAsset("airplane/PLANE_PROPELLER_2.png", "Texture");
        GameManager.assetLoader.loadAsset("airplane/PLANE_8_N_NRM.png", "Texture");
        GameManager.assetLoader.loadAsset("light.png", "Texture");
    }

    private void finalCheck() {
        Timeline.createSequence()
                 .pushPause(1.0f)
                .setCallback(cb)
                .setCallbackTriggers(TweenCallback.COMPLETE)
                .start(tweenManager);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(Gdx.graphics.getDeltaTime());

        if (!init){
            if (GameManager.assetLoader.finishedLoading) {
                // Init SplashScreen Scene with Loaded Assets
                GameManager.assetLoader.finishedLoading = false;
                init();
            }else{
                // Prompt Loader to Load
                GameManager.assetLoader.render();
            }
        }else if(!finalInit) {
            if (GameManager.assetLoader.finishedLoading) {
                finalInit = true;
                finalCheck();
                GameManager.assetLoader.finishedLoading = false;
            }else{
                GameManager.assetLoader.render();
            }
        }

        if(init){

            ((ProgressBarEntity)GameManager.entityManager.getEntity("SplashScreenProgressBar")).currentProgress = GameManager.assetLoader.getProgress();

            batch.begin();
            sprite.draw(batch);
            GraphicsComponentGroup.getInstance().updateGroup(batch); // Benefit of ECS is here
            bmFont.draw(batch, fontText, appWidth / 2f - bmFont.getBounds(fontText).width / 2f, appHeight * 0.2f - bmFont.getBounds(fontText).height / 2f);
            // draw progress bar
            batch.end();
            rendCount++;

        }
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("Splash Screen", "Resize called");
    }

    @Override
    public void show() {
        Gdx.app.log("Splash Screen", "show called");
        showTime = TimeUtils.millis();
    }

    @Override
    public void hide() {
        Gdx.app.log("Splash Screen", "hide called");
        Gdx.app.log("Splash Screen", "rendered " + rendCount + " times.");
    }

    @Override
    public void pause() {
        Gdx.app.log("Splash Screen", "pause called");
    }

    @Override
    public void resume() {
        Gdx.app.log("Splash Screen", "resume called");
    }

    /**
     * todo: Screens get managed by GameManager, disposal also needs to be managed, thanks
     */
    @Override
    public void dispose() {
        Gdx.app.log("Splash Screen", "dispose called");
        // Manual disposal for Now
        // Config Based AssetLoading will manage this automatically
        texture.dispose();
        batch.dispose();
        bmFont.dispose();
        ((ProgressBarEntity)GameManager.entityManager.getEntity("SplashScreenProgressBar")).setDispose(true);
    }
}

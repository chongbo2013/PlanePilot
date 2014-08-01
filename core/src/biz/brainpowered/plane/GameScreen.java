package biz.brainpowered.plane;

import biz.brainpowered.plane.comp.*;
import biz.brainpowered.plane.manager.GameManager;
import biz.brainpowered.plane.render.SimpleLightsRenderer;
import biz.brainpowered.plane.scene.Scene;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

/**
 * Todo: realise a Menu system in LibGDX
 */
public class GameScreen implements Screen {
    // GameManager/Model Singleton Properties here
    int appWidth;
    int appHeight;
    Camera camera;
    SpriteBatch batch;
    EntityManager entityManager;
    SimpleLightsRenderer simpleLightsRenderer;

    final Game game; // Game will never be re-assigned, duh :)
    final Scene scene;

    TextureRegion occluders;   //occluder map

    FrameBuffer occludersFBO;

    /**
     * @todo: load Game Screen with a Scene reference/id
     * @param game
     */
    public GameScreen(final Game game, final Scene scene) {
        Gdx.app.log(" GameScreen", "Constructor called");
        this.game = game; // this means nothing for now
        this.scene = scene; // is essentially a Model with all the info that can Create Entities (which make up a scene)


        // TODO: init this into GameManager thanks
        // init game manager
        // Init Globals/GameManager Props
        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(appWidth, appHeight);
        camera.update();
        batch = new SpriteBatch( 1000, null);
        batch.setShader(null);

        // Show Loader Progress Item While Scene Assets are loaded :D
        entityManager = GameManager.entityManager;


        // todo: will use SceneManagement and Scripting to Control Entities
        entityManager.createGroundEntity( "Ground1", "airplane/airPlanesBackground.png", 80.0f);
        entityManager.createPlaneEntity( "Player1", "airplane/PLANE_8_N.png"); // returns a Reference-value

        // TODO:...
        // TODO: CouldSpawner entity (seed based algo)
        // TODO: GroundDecorationsSpawner (seed based algo)
        // now lets create some Enemies, with PRESET Movement Patterns @see MovementStrategy, Sprites, Sounds, Difficulty/HP/DMG

        simpleLightsRenderer = new SimpleLightsRenderer("light.png", LightComponentGroup.getInstance().getComponents());
        simpleLightsRenderer.init();
    }

    @Override
    public void render(float delta) {



        // get NEW X,Y positions
        LightComponentGroup.getInstance().updateGroup(batch); // Benefit of ECS is here

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f); // clear with white opaque
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // todo: refactor and get working again
        //occludersFBO = simpleLightsRenderer.render(batch);

        batch.begin();
        GraphicsComponentGroup.getInstance().updateGroup(batch); // Benefit of ECS is here
        batch.draw(occluders, 0 ,0);
        batch.end();

        InputComponentGroup.getInstance().updateGroup( null ); // Benefit of ECS is here

        /**
         * One major benefit illustrated here is that much of the boilerplate and micromanagement is abstracted away
         */


        // GC BULLets
        // todo: tuck this away
        //bullets = entityManager.getEntityByClass(BulletEntity.class);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(" GameScreen", "Resize called");

        simpleLightsRenderer.resize(width, height);
        occludersFBO = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        occluders = new TextureRegion(occludersFBO.getColorBufferTexture());
        occluders.flip(false, true);
    }

    @Override
    public void show() {
        Gdx.app.log(" GameScreen", "Show called");
    }

    @Override
    public void hide() {
        Gdx.app.log(" GameScreen", "Hide called");
    }

    @Override
    public void pause() {
        Gdx.app.log(" GameScreen", "Pause called");
    }

    @Override
    public void resume() {
        Gdx.app.log(" GameScreen", "Resume called");
    }

    @Override
    public void dispose() {
        Gdx.app.log(" GameScreen", "Dispose called");
    }
}

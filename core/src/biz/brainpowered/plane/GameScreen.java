package biz.brainpowered.plane;

import biz.brainpowered.plane.comp.ComponentGroupManager;
import biz.brainpowered.plane.comp.EntityManager;
import biz.brainpowered.plane.comp.GraphicsComponentGroup;
import biz.brainpowered.plane.comp.InputComponentGroup;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

    final Game game; // Game will never be re-assigned, duh :)

    public GameScreen(final Game game) {
        this.game = game;
        // TODO: init this into GameManager thanks
        // init game manager
        // Init Globals/GameManager Props
        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(appWidth, appHeight);
        camera.update();
        batch = new SpriteBatch( 1000, null);
        batch.setShader(null);
        entityManager = new EntityManager();

        ComponentGroupManager.getInstance().registerComponentGroup(ComponentGroupManager.GRAPHICS, GraphicsComponentGroup.getInstance());
        ComponentGroupManager.getInstance().registerComponentGroup(ComponentGroupManager.INPUT, InputComponentGroup.getInstance());

        // todo: will use SceneManagement and Scripting to Control Entities
        entityManager.createGroundEntity( "Ground1", "airplane/airPlanesBackground.png", 80.0f);
        entityManager.createPlaneEntity( "Player1", "airplane/PLANE_8_N.png"); // returns a Reference-value

        // Manager prevents duplicates, each MUST have a Unique EntityId
        entityManager.createGroundEntity( "Ground1", "airplane/airPlanesBackground.png", 80.0f);
        entityManager.createGroundEntity( "Ground1", "airplane/airPlanesBackground.png", 80.0f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f); // clear with white opaque
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        GraphicsComponentGroup.getInstance().updateGroup(batch); // Benefit of ECS is here
        batch.end();

        InputComponentGroup.getInstance().updateGroup( null ); // Benefit of ECS is here

        /**
         * One major benefit illustrated here is that much of the boilerplate and micromanagement is abstracted away
         */
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

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
}

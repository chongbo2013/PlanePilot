package biz.brainpowered.plane;

import biz.brainpowered.plane.GameScreen;
import biz.brainpowered.plane.manager.GameManager;
import biz.brainpowered.plane.scene.Scene;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen implements Screen {
    Skin skin;
    Stage stage;
    SpriteBatch batch;

    Game g;

    int appWidth;
    int appHeight;
    Camera camera;

    private TextButton playButton, hiscoreButton, exitButton;

    private BitmapFont bmFont;
    //private String fontText = "Splash Screen";

    public MainMenuScreen(Game g){
        create();
        this.g=g;
        //todo: dispose previous screen
        //previousScreen.dispose();
    }

    public MainMenuScreen(){
        create();
    }

    public void create(){
        Gdx.app.log("Main Menu Screen", "Constructor called");
        batch = new SpriteBatch();
        stage = new Stage();

        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();

        stage.getViewport().update(appWidth, appHeight);
        Gdx.input.setInputProcessor(stage);

        // SKIN STUFF
        ///

        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin();
        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(250, 72, Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();

        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        bmFont = GameManager.assetLoader.get("font/Reload.fnt", "BitmapFont");
        //bmFont.scale(1);
        skin.add("default",bmFont);

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);

        textButtonStyle.font = skin.getFont("default");

        skin.add("default", textButtonStyle);
        // END SKIN STUFF
        //

        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        playButton = new TextButton("PLAY",textButtonStyle);
        playButton.setPosition(appWidth/2 - playButton.getWidth()/2, appHeight * 0.8f - playButton.getHeight()/2);
        stage.addActor(playButton);

        hiscoreButton = new TextButton("High Scores",textButtonStyle);
        hiscoreButton.setPosition(appWidth/2 - hiscoreButton.getWidth()/2, appHeight * 0.6f - hiscoreButton.getHeight()/2);
        stage.addActor(hiscoreButton);

        exitButton = new TextButton("Exit",textButtonStyle);
        exitButton.setPosition(appWidth/2 - exitButton.getWidth()/2, appHeight * 0.4f - exitButton.getHeight()/2);
        stage.addActor(exitButton);

        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        playButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor((InputProcessor) null);
                g.setScreen( new GameScreen(g, new Scene("default", null, null)));
            }
        });
        hiscoreButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //Gdx.input.setInputProcessor((InputProcessor) null);
                //g.setScreen( new HighscoreScreen(g));
            }
        });
        exitButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        //Table.drawDebug(stage);
    }

    @Override
    public void resize (int width, int height) {
        Gdx.app.log("Main Menu Screen", "resize called");
        System.out.println("size: "+width+" "+height);
        stage.getViewport().update(width, height);
        //stage.setViewport(width, height, false);
    }

    @Override
    public void dispose () {
        Gdx.app.log("Main Menu Screen", "dispose called");
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        Gdx.app.log("Main Menu Screen", "Show called");

    }

    @Override
    public void hide() {
        // TODO: disable Inputs, etc here
        playButton.setDisabled(true);
        Gdx.app.log("Main Menu Screen", "Hide called");

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        Gdx.app.log("Main Menu Screen", "pause called");

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        // todo: re-init textures???
        Gdx.app.log("Main Menu Screen", "resume called");
    }
}
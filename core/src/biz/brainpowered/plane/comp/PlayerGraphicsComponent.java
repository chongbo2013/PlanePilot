package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import biz.brainpowered.plane.comp.interfaces.SpriteEntityInterface;
import biz.brainpowered.plane.manager.GameManager;
import biz.brainpowered.plane.model.PlaneConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import java.util.Vector;

/**
 * Created by sebastian on 2014/07/28.
 */
public class PlayerGraphicsComponent extends GenericGraphicsComponent {

    // TODO: fill out the class variables from the Plane class
    Texture leftTexture;
    Sprite leftSprite;

    Texture rightTexture;
    Sprite rightSprite;

    // Old Pasty
    //private PlaneConfig pc;
    private float appWidth;
    private float appHeight;
    private float scale;

    private Texture normalT;
    private Texture leftT;
    private Texture rightT;

    private Sprite normalS;
    private Sprite leftS;
    private Sprite rightS;

    private Sprite _sprite; // todo: substitute this for _sprite ...? or not (go custom)

    private Texture propeller1T;
    private Texture propeller2T;
    private Texture bulletT;
    private Texture smokeT;

    PlaneConfig pc;

    // extra items
    Texture planeShadowTexture;
    Sprite planeShadow;
    Vector<Texture> propTexArray;
    Texture propellerTexture1;
    Texture propellerTexture2;
    Sprite propeller;
    Integer propellerNumber;

    public static Texture RIGHT;
    public static Texture LEFT;
    public static Texture NORMAL;

    Texture _normalMap;

    // pass a complete parameter list in here, thanks
    public PlayerGraphicsComponent(Texture texture, SpriteEntityInterface entity) {
        super(texture, entity);
        _normalMap = GameManager.assetLoader.get("airplane/PLANE_8_N_NRM.png", "Texture");//new Texture(Gdx.files.internal("airplane/PLANE_8_N_NRM.png"));

        PlaneConfig pc = new PlaneConfig();
        pc.normalTP = "airplane/PLANE_8_N.png";
        pc.leftTP = "airplane/PLANE_8_L.png";
        pc.rightTP = "airplane/PLANE_8_R.png";
        pc.scale = 0.5f;

        // assignment
        this.pc = pc;
        scale = pc.scale;

        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();

        init();
    }

    // todo: init Components 'in-entity' at AsyncCall
    public boolean init()
    {
        try
        {
            // instanciation
            normalT = GameManager.assetLoader.get(pc.normalTP, "Texture");//new Texture(Gdx.files.internal(pc.normalTP)); // create Texture from TexturePath
            leftT = GameManager.assetLoader.get(pc.leftTP, "Texture");//new Texture(Gdx.files.internal(pc.leftTP));
            rightT = GameManager.assetLoader.get(pc.rightTP, "Texture");//new Texture(Gdx.files.internal(pc.rightTP));
            RIGHT = rightT;
            LEFT = leftT;
            NORMAL = normalT;

            // todo: instead of scaling the Sprite each time (to global.planeScale), just downscale the texture once here

            // also set origin of sprite
            _sprite = new Sprite();
            leftS = new Sprite(leftT);
            leftS.setSize(scale * leftT.getWidth(), scale * leftT.getHeight());
            leftS.setOrigin(leftS.getWidth() / 2, 0);
            rightS = new Sprite(rightT);
            rightS.setSize(scale * rightT.getWidth(), scale * rightT.getHeight());
            rightS.setOrigin(rightS.getWidth() / 2, 0);
            normalS = new Sprite(normalT);
            normalS.setSize(scale * normalT.getWidth(), scale * normalT.getHeight());

            _sprite.set(normalS);
            _sprite.setOrigin(normalS.getWidth() / 2, 0);

            /**
             * Specials Notes:
             * Need to investigate if casting is required (but will need to push down the SpriteEntityInterface methods) to Entity Interface
             */
            _entity.setWidth(_sprite.getWidth());
            _entity.setHeight(_sprite.getHeight());

            // some old pasty here - move paths to config
            planeShadowTexture = GameManager.assetLoader.get("airplane/PLANE_8_SHADOW.png", "Texture");//new Texture(Gdx.files.internal("airplane/PLANE_8_SHADOW.png"));
            planeShadow = new Sprite(planeShadowTexture);
            planeShadow.setScale(0.3f, 0.3f);

            propellerNumber = 1;
            propellerTexture1 = GameManager.assetLoader.get("airplane/PLANE_PROPELLER_1.png", "Texture");//new Texture(Gdx.files.internal("airplane/PLANE_PROPELLER_1.png")); // TODO: get from asset manager
            propellerTexture2 = GameManager.assetLoader.get("airplane/PLANE_PROPELLER_2.png", "Texture");//new Texture(Gdx.files.internal("airplane/PLANE_PROPELLER_2.png"));
            propTexArray = new Vector<Texture>();
            propTexArray.add(0, propellerTexture1);
            propTexArray.add(1, propellerTexture2);
            propeller = new Sprite(propellerTexture1);
            propeller.setScale(scale);
//
            schedulePropeller();

            // todo: implement SoundComponent
            // control management
//            directionKeyReleased = true;
//            fire = Gdx.audio.newSound(Gdx.files.internal("sound/219622__ani-music__little-zap-zaps-1a.wav"));
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    private void schedulePropeller(){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (propellerNumber == 2)
                    propellerNumber = 1;
                else
                    propellerNumber++;

                propeller.setTexture(propTexArray.get(propellerNumber - 1));
            }
        }, 0, 1 / 30.0f);
    }

    public void setNewPosition(float maxX, float minX, float maxY, float minY, float newX, float newY) {
        // TODO: center shadow origin
        float newXshadow = newX+_sprite.getX();
        float newYshadow = newY+_sprite.getY();

        newXshadow = Math.min(Math.max(newXshadow, minY + 15), maxY + 15) / 1.3f;
        newYshadow = Math.min(Math.max(newYshadow, minX - 15), maxX - 15) / 1.3f;

        float newXpropeller = newX+propeller.getX();
        float newYpropeller = newY+propeller.getY();

        //newXpropeller = Math.min(Math.max((subjectSprite.getWidth()/2),minY),maxY);
        //newYpropeller = Math.min(Math.max(newYpropeller,minX+(subjectSprite.getHeight())-15),maxX+(subjectSprite.getHeight()/2)-5);

        newX = Math.min(Math.max(newX + _sprite.getX(), minY), maxY);
        newY = Math.min(Math.max(newY + _sprite.getY(), minX), maxX);

        newXpropeller = newX + (_sprite.getWidth()/2) - (propeller.getWidth()/2);
        newYpropeller = newY + (_sprite.getHeight())-(15*scale);

        //_sprite.setPosition(Math.round(newX), Math.round(newY));
        //_sprite.translateX(Math.round(newX), Math.round(newY));
        planeShadow.setPosition(newXshadow, newYshadow);
        propeller.setPosition(newXpropeller, newYpropeller);

        _sprite.setX(Math.round(newX));
        //System.out.println("newX: "+newX);
        _sprite.setY(Math.round(newY));

        _entity.setY(newY);
        _entity.setX(newX);
    }

    @Override
    public void update(SpriteBatch batch) {
        planeShadow.draw(batch);
        _normalMap.bind(1);
        //bind diffuse color to texture unit 0
        //important that we specify 0 otherwise we'll still be bound to glActiveTexture(GL_TEXTURE1)
        _texture.bind(0);
        _sprite.draw(batch);
        propeller.draw(batch);
    }

    // todo: should be interfaced
    public float getWidth() {
        return _sprite.getWidth();
    }

    public float getHeight() {
        return _sprite.getHeight();
    }

    public void setSpriteTexture ( Texture texture ) {
        _sprite.setTexture(texture);
    }
}

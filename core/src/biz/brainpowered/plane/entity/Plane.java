package biz.brainpowered.plane.entity;


import biz.brainpowered.plane.model.PlaneConfig;
import biz.brainpowered.plane.factory.BulletFactory;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: sebastian
 * Date: 2014/05/29
 * Time: 8:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class Plane implements InputProcessor
{
    private PlaneConfig pc;
    private float appWidth;
    private float appHeight;
    private float scale;

    private Texture normalT;
    private Texture leftT;
    private Texture rightT;

    private Sprite normalS;
    private Sprite leftS;
    private Sprite rightS;

    private Sprite currentSprite;

    private Texture propeller1T;
    private Texture propeller2T;
    private Texture bulletT;
    private Texture smokeT;
    private Sound shootS;
    private Sound diveS;

    // extra items
    Texture planeShadowTexture;
    Sprite planeShadow;
    Vector<Texture> propTexArray;
    Texture propellerTexture1;
    Texture propellerTexture2;
    Sprite propeller;
    Integer propellerNumber;

    // TODO: Movement Class
    double currentMaxAccelX;
    double currentMaxAccelY;

    boolean directionKeyReleased;
    float elapsedTime;
    float lastInputCheck;

    float baseAcell;
    float highestAccelX;
    float highestAccelY;

    // Passed in
    BulletFactory bulletFactory;
    ArrayList<Bullet> bulletCollection;


    public Plane(PlaneConfig planeConfig, ArrayList<Bullet> bc)
    {
        // assignment
        pc = planeConfig;
        scale = pc.scale;

        bulletCollection = bc;

        // control management
        directionKeyReleased = true;

        // player management
        baseAcell = 0.333f;
        highestAccelX = 6.66f;
        highestAccelY = 6.66f;
        currentMaxAccelX = 0;
        currentMaxAccelY = 0;
        elapsedTime = 0;
        lastInputCheck = 0;

        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();

        Gdx.input.setInputProcessor(this);
    }

    public boolean init()
    {
        try
        {
            // instanciation
            normalT = new Texture(Gdx.files.internal(pc.normalTP)); // create Texture from TexturePath
            leftT = new Texture(Gdx.files.internal(pc.leftTP));
            rightT = new Texture(Gdx.files.internal(pc.rightTP));

            // instead of scaling the Sprite each time (to global.planeScale), just downscale the texture once here

            // also set origin of sprite
            currentSprite = new Sprite();
            leftS = new Sprite(leftT);
            leftS.setSize(scale * leftT.getWidth(), scale * leftT.getHeight());
            leftS.setOrigin(leftS.getWidth() / 2, 0);
            rightS = new Sprite(rightT);
            rightS.setSize(scale * rightT.getWidth(), scale * rightT.getHeight());
            rightS.setOrigin(rightS.getWidth() / 2, 0);
            normalS = new Sprite(normalT);
            normalS.setSize(scale * normalT.getWidth(), scale * normalT.getHeight());

            currentSprite.set(normalS);
            currentSprite.setOrigin(normalS.getWidth() / 2, 0);

            // some old pasty here - move paths to config
            planeShadowTexture = new Texture(Gdx.files.internal("airplane/PLANE_8_SHADOW.png"));
            planeShadow = new Sprite(planeShadowTexture);
            planeShadow.setScale(0.3f, 0.3f);

            propellerNumber = 1;
            propellerTexture1 = new Texture(Gdx.files.internal("airplane/PLANE_PROPELLER_1.png"));
            propellerTexture2 = new Texture(Gdx.files.internal("airplane/PLANE_PROPELLER_2.png"));
            propTexArray = new Vector<Texture>();
            propTexArray.add(0, propellerTexture1);
            propTexArray.add(1, propellerTexture2);
            propeller = new Sprite(propellerTexture1);
            propeller.setScale(scale);
//
            schedulePropeller();

            // control management
            directionKeyReleased = true;
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

    public void checkInput()
    {
//        System.out.println("elapsedTime: "+elapsedTime);
//        System.out.println("lastInputCheck: "+lastInputCheck);
//        System.out.println("currentMaxAccelX: "+currentMaxAccelX);
//        System.out.println("currentMaxAccelY: "+currentMaxAccelY);
        if ((lastInputCheck + 0.1f) < (elapsedTime))
        {
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){

                if(currentMaxAccelX > 0)
                    currentMaxAccelX = 0;
                currentMaxAccelX-=baseAcell;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){

                if(currentMaxAccelX < 0)
                    currentMaxAccelX = 0;
                currentMaxAccelX+=baseAcell;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){

                if(currentMaxAccelY > 0)
                    currentMaxAccelY = 0;
                currentMaxAccelY-=baseAcell;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.UP)){

                if(currentMaxAccelY < 0)
                    currentMaxAccelY = 0;
                currentMaxAccelY+=baseAcell;
            }
            lastInputCheck = elapsedTime;
        }
        elapsedTime += Gdx.graphics.getDeltaTime();
    }

    public void positionPlane()
    {
        // ACELLEROMETER CONTROLS
        // TODO: Controls System
        if(Gdx.app.getType() == Application.ApplicationType.iOS || Gdx.app.getType() == Application.ApplicationType.Android)
        {
            int deviceAngle = Gdx.input.getRotation();
            Input.Orientation orientation = Gdx.input.getNativeOrientation();
            float accelX = Gdx.input.getAccelerometerX()*-1;
            if(accelX > highestAccelX)
                currentMaxAccelX = highestAccelX;
            else
                currentMaxAccelX = accelX;

            float accelY = Gdx.input.getAccelerometerY()*-1;
            if(accelY > highestAccelY)
                currentMaxAccelY = highestAccelY;
            else
                currentMaxAccelY = accelY;
        }

        float maxY = appWidth - currentSprite.getWidth();
        float minY = 0;

        float maxX = appHeight - currentSprite.getHeight();
        float minX = 0;

        float newY = 0;
        float newX = 0;

        // Choose Currents Plane Sprite based on Movement Direction
        // TODO: optimise, Very Inefficent
        if(currentMaxAccelX > baseAcell){
            newX = (float)currentMaxAccelX * 10;
            currentSprite.setTexture(rightT);
            //currentSprite.set(rightS);
        }
        else if(currentMaxAccelX < -baseAcell){
            newX = (float)currentMaxAccelX * 10;
            currentSprite.setTexture(leftT);
            //currentSprite.set(leftS);
        }
        else{
            newX = (float)currentMaxAccelX * 10;
            currentSprite.setTexture(normalT);
            //currentSprite.set(normalS);
        }

        newY = 0.0f + (float)currentMaxAccelY *10;

        // TODO: center shadow origin
        float newXshadow = newX+currentSprite.getX();
        float newYshadow = newY+currentSprite.getY();

        newXshadow = Math.min(Math.max(newXshadow, minY + 15), maxY + 15) / 1.3f;
        newYshadow = Math.min(Math.max(newYshadow, minX - 15), maxX - 15) / 1.3f;

        float newXpropeller = newX+propeller.getX();
        float newYpropeller = newY+propeller.getY();

        //newXpropeller = Math.min(Math.max((subjectSprite.getWidth()/2),minY),maxY);
        //newYpropeller = Math.min(Math.max(newYpropeller,minX+(subjectSprite.getHeight())-15),maxX+(subjectSprite.getHeight()/2)-5);

        newX = Math.min(Math.max(newX + currentSprite.getX(), minY), maxY);
        newY = Math.min(Math.max(newY + currentSprite.getY(), minX), maxX);

        newXpropeller = newX + (currentSprite.getWidth()/2) - (propeller.getWidth()/2);
        newYpropeller = newY + (currentSprite.getHeight())-(15*scale);

        //currentSprite.setPosition(Math.round(newX), Math.round(newY));
        //currentSprite.translateX(Math.round(newX), Math.round(newY));
        planeShadow.setPosition(newXshadow, newYshadow);
        propeller.setPosition(newXpropeller, newYpropeller);

        currentSprite.setX(Math.round(newX));

        //System.out.println("newX: "+newX);
        currentSprite.setY(Math.round(newY));
    }

    public void dispose(){

    }

    public void render (SpriteBatch batch)
    {
        checkInput();
        positionPlane();

        planeShadow.draw(batch);
        currentSprite.draw(batch);
        propeller.draw(batch);
    }

    // TODO: refactor ones below -->
    // Utility functions (to be refactored)
    public Rectangle getBoundingBox()
    {
        return currentSprite.getBoundingRectangle();
    }

    public void setBulletFactory(BulletFactory bf)
    {
        bulletFactory = bf;
    }

    public void fireBullet()
    {
        float bulletX = currentSprite.getX() + (currentSprite.getWidth() / 2);
        float bulletY = currentSprite.getY() + (currentSprite.getHeight());
        bulletCollection.add(bulletFactory.create(bulletX, bulletY, 90f, 1000f));
        //bulletCollectionSize++;
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keydown keycode: "+keycode);
        float moveAmount = 0.3f;
        if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
            moveAmount = 10.0f;

        if(keycode == Keys.LEFT)
            currentMaxAccelX-=moveAmount;
        if(keycode == Input.Keys.RIGHT)
            currentMaxAccelX+=moveAmount;
        if(keycode == Input.Keys.SPACE)
            fireBullet();
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("keyUp keycode: "+keycode);

        if(keycode == Keys.LEFT)
            directionKeyReleased = true ;
        if(keycode == Keys.RIGHT)
            directionKeyReleased = true;
        return true;
        //return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean keyTyped(char character) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        fireBullet();
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean scrolled(int amount) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

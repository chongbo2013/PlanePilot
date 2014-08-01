package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.entities.PlaneEntity;
import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import biz.brainpowered.plane.comp.interfaces.InputComponentInterface;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by sebastian on 2014/07/28.
 */
public class PlayerInputComponent extends BaseComponent implements InputComponentInterface {

    // TODO: Movement Class
    double currentMaxAccelX;
    double currentMaxAccelY;

    boolean directionKeyReleased;
    float elapsedTime;
    float lastInputCheck;

    float baseAcell;
    float highestAccelX;
    float highestAccelY;

    private float appWidth;
    private float appHeight;

    private float minimumBulletInterval = 0.1f;

    public PlayerInputComponent(EntityInterface entity) {
        super(ComponentGroupManager.INPUT, entity);

        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();
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

//        // todo: inject custom callbacks(Processors) into InputComponents
//        Gdx.input.setInputProcessor(new InputAdapter() {
//
//            public boolean touchDown(int x, int y, int pointer, int button) {
//                float mx = x;
//                float my = Gdx.graphics.getHeight() - y;
//
//                /**
//                 * Larger Consideration:
//                 * Could implement "Spacial" Component that wraps x,y,z,velocity, angle, scale, etc
//                 * then simply, example: ((SpacialComponent)_entity.getComponent("Spacial")).getY()/.setX(x)
//                 * Considerations in this are, Performance trade-off vs Flexibility (consistency)
//                 * and then finally alternative Fast mapping techniques acheiving the same outcome
//                 */
//
//                _entity.setX(mx);
//                _entity.setY(my);
//                return true;
//            }
//
//        });
    }

    public void update( Object... params ) {

        // INPUT VALUES
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

        // TODO: Level progression will modify this value
        if ((lastInputCheck + minimumBulletInterval) < (elapsedTime)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                ((PlaneEntity) _entity).fireBullet();
            }
        }

        // POSITION PLANE
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

        // Choose Currents Plane Sprite based on Movement Direction
        // TODO: optimise, Very Inefficent
        if(currentMaxAccelX > baseAcell){
            ((PlayerGraphicsComponent)_entity.getComponent("Graphics")).setSpriteTexture(PlayerGraphicsComponent.RIGHT);
        }
        else if(currentMaxAccelX < -baseAcell){
            ((PlayerGraphicsComponent)_entity.getComponent("Graphics")).setSpriteTexture(PlayerGraphicsComponent.LEFT);
        }
        else{
            ((PlayerGraphicsComponent)_entity.getComponent("Graphics")).setSpriteTexture(PlayerGraphicsComponent.NORMAL);
        }

        // DO GRAPHICS ADJUSTMENTS ACCORDING TO MIN AND MAX XY VALUES
        float maxY = appWidth - ((PlayerGraphicsComponent)_entity.getComponent("Graphics")).getWidth();
        float minY = 0;

        float maxX = appHeight - ((PlayerGraphicsComponent)_entity.getComponent("Graphics")).getHeight();
        float minX = 0;

        float newX = (float)currentMaxAccelX * 10;
        float newY = 0.0f + (float)currentMaxAccelY *10;


        // todo: do more of this
        //((SpriteEntityInterface)_entity).setX(newX);r

        // TODO: streamline/break down this big indirection
        ((PlayerGraphicsComponent)_entity.getComponent("Graphics")).setNewPosition(maxX, 0, maxY, 0, newX, newY);
    }
}

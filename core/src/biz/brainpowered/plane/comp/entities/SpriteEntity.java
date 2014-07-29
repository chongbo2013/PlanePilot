package biz.brainpowered.plane.comp.entities;

import biz.brainpowered.plane.comp.interfaces.ComponentInterface;
import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import biz.brainpowered.plane.comp.interfaces.SpriteEntityInterface;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by sebastian on 2014/07/28.
 * Generic Tester Object
 * Also represents a Component Bag currently (this one is very Generic)
 */
public class SpriteEntity extends BaseEntity implements SpriteEntityInterface {
    // Components will Keep Reference to entity

    // Generics
    float _x;   // x-axis position
    float _y;   // y-axis position
    float _v;   // velocity
    float _r;   // rotation/angle

    // Specifics
    float MAX_Y_VELOCITY = 13f;
    float MIN_Y_VELOCITY = 0.3f;
    float MAX_X_VELOCITY = 13f;
    float MIN_X_VELOCITY = 0.3f;

    boolean _dispose = false;

    public SpriteEntity(Object params) {
        super(params);
        _x = 100;
        _y = 100;
        // not much
    }

    @Override
    public void setDispose(boolean dispose) {
        _dispose = dispose;
    }

    @Override
    public boolean getDispose() {
        return _dispose;
    }

//    @Override
//    public void setSprite(Sprite sprite) {
//
//    }
//
//    @Override
//    public Sprite getSprite() {
//        return _s;
//    }

    @Override
    public void setX(float x) {
        _x = x;
    }

    @Override
    public float getX() {
        return _x;
    }

    @Override
    public void setY(float y) {
        _y = y;
    }

    @Override
    public float getY() {
        return _y;
    }
}

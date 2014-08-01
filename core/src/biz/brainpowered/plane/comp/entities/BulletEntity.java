package biz.brainpowered.plane.comp.entities;

import com.badlogic.gdx.Gdx;

/**
 * Created by sebastian on 2014/07/28.
 * Generic Tester Object
 * Also represents a Component Bag currently (this one is very Generic)
 */
public class BulletEntity extends SpriteEntity {

    public float lifeTime = 0.5f;
    public float yLimit;

    public BulletEntity(Object params) {
        super(params);
        yLimit = Gdx.graphics.getHeight(); // TODO -> optimise
    }

    @Override
    public void setX(float x) {
        super.setX(x);
    }

    @Override
    public float getX() {
        return super.getX();
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        //System.out.println("setting y to: "+y);
        if (_y > yLimit) {
            //System.out.println("setting dispose: true");
            setDispose(true);
        }
    }

    @Override
    public float getY() {
        return super.getY();
    }
}

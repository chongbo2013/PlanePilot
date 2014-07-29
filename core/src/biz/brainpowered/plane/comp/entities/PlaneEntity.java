package biz.brainpowered.plane.comp.entities;

/**
 * Created by sebastian on 2014/07/28.
 * Generic Tester Object
 * Also represents a Component Bag currently (this one is very Generic)
 */
public class PlaneEntity extends BaseEntity {


    public PlaneEntity(Object params) {
        super(params);
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        //((PlayerGraphicsComponent)getComponent("Graphics")).
    }

    @Override
    public float getX() {
        return super.getX();
    }

    @Override
    public void setY(float y) {
        super.setY(y);
    }

    @Override
    public float getY() {
        return super.getY();
    }
}

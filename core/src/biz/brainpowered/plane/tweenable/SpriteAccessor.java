package biz.brainpowered.plane.tweenable;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by sebastian on 2014/07/31.
 */
public class SpriteAccessor implements TweenAccessor<Sprite> {

    // The following lines define the different possible tween types.
    // It's up to you to define what you need :-)

    public static final int POSITION_X = 1;
    public static final int POSITION_Y = 2;
    public static final int POSITION_XY = 3;
    public static final int HEIGHT = 4;
    public static final int WIDTH = 5;
    public static final int OPACITY = 6;
    public static final int SCALE = 7;

    // TweenAccessor implementation

    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POSITION_X: returnValues[0] = target.getX(); return 1;
            case POSITION_Y: returnValues[0] = target.getY(); return 1;
            case HEIGHT: returnValues[0] = target.getHeight(); return 1;
            case WIDTH: returnValues[0] = target.getWidth(); return 1;
            case OPACITY: returnValues[0] = target.getColor().a; return 1;
            case SCALE:

                //Gdx.app.log("Splash Screen", "getScaleX");
                returnValues[0] = target.getScaleX();
                returnValues[1] = target.getScaleY();
                return 2;
            case POSITION_XY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POSITION_X: target.setX( newValues[0]); break;
            case POSITION_Y: target.setY(newValues[0]); break;
            case HEIGHT: target.setSize(target.getWidth(), newValues[0]); break;
            case WIDTH: target.setSize(newValues[0], target.getHeight()); break;
            case OPACITY: target.setAlpha(newValues[0]); break;
            case SCALE:
                target.setScale( newValues[0], newValues[1] );

                //Gdx.app.log("Splash Screen", "setScale");
                break;
            case POSITION_XY:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                break;
            default: assert false; break;
        }
    }
}

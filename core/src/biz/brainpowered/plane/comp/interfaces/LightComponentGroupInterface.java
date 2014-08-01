package biz.brainpowered.plane.comp.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/28.
 */
public interface LightComponentGroupInterface extends ComponentGroupInterface {
    // for graphics
    // todo: rather split up the ComponentGroupTypes, only specifying thier preferred u[date method
    public void updateGroup(SpriteBatch batch);

}

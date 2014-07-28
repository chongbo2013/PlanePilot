package biz.brainpowered.plane.comp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/28.
 */
public interface ComponentGroupInterface {

    // initialise this type of Component Group
    public void init ();

    // add component instance (which has reference to the 'owning' Entity)
    public void addComponent ( ComponentInterface componentInterface );

    // for graphics
    public void updateGroup ( SpriteBatch batch );

}

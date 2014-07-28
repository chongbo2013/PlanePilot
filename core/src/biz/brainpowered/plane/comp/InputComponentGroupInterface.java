package biz.brainpowered.plane.comp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/28.
 */
public interface InputComponentGroupInterface {

    // initialise this type of Component Group
    public void init();

    // add component instance (which has reference to the 'owning' Entity)
    public void addComponent(ComponentInterface componentInterface);

    // for graphics
    // todo: rather split up the ComponentGroupTypes, only specifying thier preferred u[date method
    public void updateGroup( Object... params );

}

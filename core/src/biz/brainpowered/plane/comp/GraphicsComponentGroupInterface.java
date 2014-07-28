package biz.brainpowered.plane.comp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/28.
 */
public interface GraphicsComponentGroupInterface {

    // initialise this type of Component Group
    public void init();

    // add component instance (which has reference to the 'owning' Entity)
    public void addComponent(GraphicsComponentInterface componentInterface);

    // for graphics
    // todo: rather split up the ComponentGroupTypes, only specifying thier preferred u[date method
    public void updateGroup(SpriteBatch batch);

}

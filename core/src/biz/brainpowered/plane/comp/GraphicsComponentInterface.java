package biz.brainpowered.plane.comp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/28.
 * Note: this is a Generic Interface for Components, generally we could split up Component<T>Interfaces</T>
 */
public interface GraphicsComponentInterface extends ComponentInterface{

    public void update ( SpriteBatch batch );
}

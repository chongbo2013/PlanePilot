package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import biz.brainpowered.plane.comp.interfaces.GraphicsComponentInterface;
import biz.brainpowered.plane.comp.interfaces.SpriteEntityInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/28.
 */
public class GenericGraphicsComponent extends BaseComponent implements GraphicsComponentInterface {
    Texture _texture;
    Sprite _sprite;

    // pass in *this and the Texture from the Asset Manager
    public GenericGraphicsComponent ( Texture texture, EntityInterface entity ) {
        super(ComponentGroupManager.GRAPHICS, entity);
        _texture = texture;
        _sprite = new Sprite(_texture);
    }

    // this case requires that entity position be applied to sprite
    public void update ( SpriteBatch batch ) {
        // note - drawing data may be needed to be pre-calculated (j.i.c.)
//        if ( !draw(_entity.getDrawConfig(), batch) ) {
            // todo: instead of setting values, just draw in place
            _sprite.setX(_entity.getX());
            _sprite.setY(_entity.getY());
            _sprite.draw(batch); // the common fallback
//        }
    }

//    private boolean draw ( DrawConfig dc, SpriteBatch batch ) {
//        // May be required to do Lights/Shadows/BumpMaps Halleluja - not now
//        return false;
//    }
}

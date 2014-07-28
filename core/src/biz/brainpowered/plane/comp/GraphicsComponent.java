package biz.brainpowered.plane.comp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/28.
 */
public class GraphicsComponent implements ComponentInterface {
    private Texture _texture;
    private Sprite _sprite;
    private Entity _entity;

    // pass in *this and the Texture from the Asset Manager
    public GraphicsComponent ( Texture texture, Entity entity ) {
        _texture = texture;
        _sprite = new Sprite(_texture);
        _entity = entity;
    }

    // this case requires that entity position be applied to sprite
    public void update ( SpriteBatch batch ) {
        // note - drawing data may be needed to be pre-calculated (j.i.c.)
//        if ( !draw(_entity.getDrawConfig(), batch) ) {
            // todo: instead of setting values, just draw in place
            _sprite.setX(_entity._x);
            _sprite.setY(_entity._y);
            _sprite.draw(batch); // the common fallback
//        }
    }

//    private boolean draw ( DrawConfig dc, SpriteBatch batch ) {
//        // May be required to do Lights/Shadows/BumpMaps Halleluja - not now
//        return false;
//    }

    @Override
    public void registerComponent() {
        GraphicsComponentGroup.getInstance().addComponent(this);
    }

//    @Override
//    public ComponentGroupInterface getGroup() {
//        return GraphicsComponentGroup.getInstance();
//    }
}

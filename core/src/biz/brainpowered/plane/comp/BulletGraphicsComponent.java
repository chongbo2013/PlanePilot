package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import biz.brainpowered.plane.comp.interfaces.GraphicsComponentInterface;
import biz.brainpowered.plane.comp.interfaces.SpriteEntityInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/28.
 */
public class BulletGraphicsComponent extends GenericGraphicsComponent {
    public float _x;
    public float _y;
    public float _a;
    public float _v;

    // pass in *this and the Texture from the Asset Manager
    public BulletGraphicsComponent(Texture texture, EntityInterface entity, float x, float y, float a, float v) {
        super(texture, entity);

        _sprite.scale(0.5f); // todo: global
        _sprite.setOriginCenter();

        _x = x - _sprite.getWidth()/2;
        _y = y;
        _a = a;
        _v = v;

        _sprite.setRotation(_a - 90f); // todo: global
        _sprite.setPosition(_x, _y);
    }

    public void update ( SpriteBatch batch ) {
        _y += (_v * Gdx.graphics.getDeltaTime());
        _entity.setX(_x);
        _entity.setY(_y);
        _sprite.setPosition(_x, _y);
        _sprite.draw(batch);
    }

//    private boolean draw ( DrawConfig dc, SpriteBatch batch ) {
//        // May be required to do Lights/Shadows/BumpMaps Halleluja - not now
//        return false;
//    }

}

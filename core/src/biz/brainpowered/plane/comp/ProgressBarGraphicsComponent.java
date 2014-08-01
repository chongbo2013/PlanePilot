package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.entities.ProgressBarEntity;
import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/28.
 */
public class ProgressBarGraphicsComponent extends GenericGraphicsComponent {
    public float _x;
    public float _y;
    public float _a;
    public float _v;

    // pass in *this and the Texture from the Asset Manager
    public ProgressBarGraphicsComponent(Texture texture, EntityInterface entity, float x, float y, float a, float v) {
        super(texture, entity);

        _sprite.scale(((ProgressBarEntity)_entity).currentProgress); // todo: global
        _sprite.setOriginCenter();

        _x = x - _texture.getWidth()/2;
        _y = y - _texture.getHeight();
        _a = a;
        _v = v;

        _sprite.setPosition(_x, _y);
    }

    public void update ( SpriteBatch batch ) {
        _sprite.setScale(((ProgressBarEntity)_entity).currentProgress, 1.0f);
        _sprite.draw(batch);
    }

//    private boolean draw ( DrawConfig dc, SpriteBatch batch ) {
//        // May be required to do Lights/Shadows/BumpMaps Halleluja - not now
//        return false;
//    }

}

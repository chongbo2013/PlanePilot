package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.entities.GroundEntity;
import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/29.
 */
public class GroundGraphicsComponent extends GenericGraphicsComponent {
    Sprite _sprite2;

    float _width;
    float _height;

    //float _xVelocity;   // required for horizontal scolling
    //float _yVelocity;

    public GroundGraphicsComponent(Texture texture, EntityInterface entity) {
        super(texture, entity);

        _width = Gdx.graphics.getWidth();
        _height = Gdx.graphics.getHeight();

        //_yVelocity = ((GroundEntity)_entity).groundSpeed;

        // todo: investigate if this is needed
        //texture = new Texture(groundTexturePath);

        _sprite = new Sprite(texture);
        _sprite.setSize(_width, _height);

        _sprite2 = new Sprite(texture);
        _sprite2.setSize(_width, _height);
        _sprite2.setPosition(0.0f, _sprite.getHeight()+_sprite.getY());
    }

    /**
     * Entity Position isn't concerned as the internal calculations work differently
     * @param batch
     */
    public void update ( SpriteBatch batch ) {
        float deltaT = Gdx.graphics.getDeltaTime();
        float velocityModifier = 1.0f;

        float deltaYV = -(deltaT * (((GroundEntity)_entity).groundSpeed * velocityModifier));
        if (_sprite.getY() <= -_height){
            // off screen - move back up
            _sprite.setPosition(0, _sprite2.getY() + _sprite2.getHeight());
        }
        if (_sprite2.getY() <= -_height){
            // off screen - move back up
            _sprite2.setPosition(0, _sprite.getY() + _sprite.getHeight());
        }

        // WARNING: POSITION IS STORED IN SPRITE....
        _sprite.translateY(Math.round(deltaYV)); // no sub-pixels (that otherwise could lead to artifacts)
        _sprite2.translateY(Math.round(deltaYV));

        _sprite.draw(batch);
        _sprite2.draw(batch);
    }

//    private boolean draw ( DrawConfig dc, SpriteBatch batch ) {
//        // May be required to do Lights/Shadows/BumpMaps Halleluja - not now
//        return false;
//    }

}

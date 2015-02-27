package biz.brainpowered.plane.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/25.
 * @deprecated old
 * @todo refactor
 */
public class BumpLitSpriteEntity extends SpriteEntity {
    protected boolean _bumpLit;
    protected Texture _normalMap;

    public BumpLitSpriteEntity(Texture texture, Texture normal, boolean bumpLit) {
        super(texture);
        _normalMap = normal;
        _bumpLit = bumpLit;
    }
    public void render (SpriteBatch batch ) {
        //bind normal map to texture unit 1
        _normalMap.bind(1);
        //bind diffuse color to texture unit 0
        //important that we specify 0 otherwise we'll still be bound to glActiveTexture(GL_TEXTURE1)
        _texture.bind(0);
        _sprite.draw(batch);
    }
}

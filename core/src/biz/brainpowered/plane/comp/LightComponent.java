package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import biz.brainpowered.plane.comp.interfaces.LightComponentInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/31.
 */
public class LightComponent extends BaseComponent implements LightComponentInterface {
    public static Color YELLOW = new Color(0.5f, 0.5f, 0.1f, 0.5f);
    public float x, y, scale;
    public Color color;

    // TODO: scale should be replaced by width/height

    // TODO: use lights were specified below
    public boolean lightBumps = false;
    public boolean castShadows = false;

    public LightComponent(EntityInterface entity, float x, float y, Color color, float scale)
    {
        super(ComponentGroupManager.LIGHT, entity);
        new LightComponent(entity, x, y, color, scale, false, false);
    }

    public LightComponent(EntityInterface entity, float x, float y, Color color, float scale, boolean lightBumps, boolean castShadows) {
        super(ComponentGroupManager.LIGHT, entity);
//        this.x = x;
//        this.y = y;
        this.scale = scale;
        this.color = color;
        this.lightBumps = lightBumps;
        this.castShadows = castShadows;
    }

    public static Color randomColor() {
        float intensity = (float)Math.random() * 0.5f + 0.5f;
        return new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), intensity);
    }


    /**
     * Note: this component doesn't draw directly, it just maintains its own values, for use by the Various Renderers
     * @param batch
     */
    @Override
    public void update(SpriteBatch batch) {
        // just keep updating from the entity
        x = _entity.getX();
        y = _entity.getY();
          //not implemented
        //System.out.println("Light getting Bullet y:"+y);
    }

}

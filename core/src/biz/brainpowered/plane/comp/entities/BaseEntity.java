package biz.brainpowered.plane.comp.entities;

import biz.brainpowered.plane.comp.ComponentGroupManager;
import biz.brainpowered.plane.comp.interfaces.ComponentGroupInterface;
import biz.brainpowered.plane.comp.interfaces.ComponentInterface;
import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import biz.brainpowered.plane.comp.interfaces.SpriteEntityInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sebastian on 2014/07/28.
 * Generic Tester Object
 * Also represents a Component Bag currently (this one is very Generic)
 */
public class BaseEntity implements EntityInterface {
    // Components will Keep Reference to entity
    Map<String, ComponentInterface> components;

//    // Generics
    float _x;   // x-axis position
    float _y;   // y-axis position
    float _v;   // velocity
    float _r;   // rotation/angle
//
//    // Specifics
    float MAX_Y_VELOCITY = 13f;
    float MIN_Y_VELOCITY = 0.3f;
    float MAX_X_VELOCITY = 13f;
    float MIN_X_VELOCITY = 0.3f;

    public BaseEntity(Object params) {
//        _x = 100;
//        _y = 100;
        // not much
        
        // for now
        init();
    }

    public boolean init () {
        try
        {
            components = new HashMap<String, ComponentInterface>();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public void addComponent ( ComponentInterface comp ) {
        comp.registerComponent();
        components.put(comp.getName(), comp);
    }

    // addComponents ( args )
    public void addComponents ( ComponentInterface... comps ) {
        for ( int i =0; i<comps.length; i++ ) {
            comps[i].registerComponent();
            components.put(comps[i].getName(), comps[i]);
        }
    }

    @Override
    public ComponentInterface getComponent(String componentName) {
        return components.get(componentName);
    }

    @Override
    public void setX(float x) {
        _x = x;
    }

    @Override
    public float getX() {
        return _x;
    }

    @Override
    public void setY(float y) {
        _y = y;
    }

    @Override
    public float getY() {
        return _y;
    }
}

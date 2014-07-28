package biz.brainpowered.plane.comp;

/**
 * Created by sebastian on 2014/07/28.
 * Generic Tester Object
 * Also represents a Component Bag currently (this one is very Generic)
 */
public class Entity {
    // Could compose various Components
    //protected GraphicsComponent _graphics;
    // Another take could be that the Components Reference an Entity

    // Generics
    float _x;   // x-axis position
    float _y;   // y-axis position
    float _v;   // velocity
    float _r;   // rotation/angle

    // Specifics
    float MAX_Y_VELOCITY = 13f;
    float MIN_Y_VELOCITY = 0.3f;
    float MAX_X_VELOCITY = 13f;
    float MIN_X_VELOCITY = 0.3f;

//    protected DrawConfig getDrawConfig () {
//        return new DrawConfig();
//    }

    public Entity ( Object params ) {
        _x = 100;
        _y = 100;
        // not much
    }

    protected void addComponent ( ComponentInterface comp ) {
        comp.registerComponent();
    }

    // addComponents ( args )
    protected void addComponents ( ComponentInterface... comps ) {
        for ( int i =0; i<comps.length; i++ ) {
            comps[i].registerComponent();
        }
    }
}

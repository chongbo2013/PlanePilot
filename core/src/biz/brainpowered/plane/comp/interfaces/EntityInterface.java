package biz.brainpowered.plane.comp.interfaces;

/**
 * Created by sebastian on 2014/07/29.
 */
public interface EntityInterface {
    /**
     * Add Component to this Entity Instance
     * internally it only adds an Entity Reference to the Component Instance and adds that Component to the ComponentGroup
     * @param comp the Component instance to add to this entity
     */
    public void addComponent ( ComponentInterface comp );

    /**
     * A Parameter List of Component Instances can be Supplied, has the same effect as adding them individually
     * @param comps parameter list of Components
     */
    public void addComponents ( ComponentInterface... comps );

    public ComponentInterface getComponent ( String componentName );

    public void setX ( float x );

    public float getX ();

    public void setY ( float y );

    public float getY ();
}

package biz.brainpowered.plane.comp.interfaces;

/**
 * Component Interface
 * In the Entity Component System, all Components should derive from a common Interface
 */
public interface ComponentInterface {

    /**
     * add component to the Group
     */
    public void registerComponent();

    public String getName();

    public void dispose();

    public boolean checkDispose();
}

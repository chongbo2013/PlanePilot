package biz.brainpowered.plane.comp;

/**
 * Created by sebastian on 2014/07/28.
 */
public interface ComponentInterface {

    // add component to the Group
    public void registerComponent ();

    // update -- each component should feature an update function with different params list
    // ie: spritebatch for graphics, delta-time for position. etc..
}

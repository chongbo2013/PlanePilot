package biz.brainpowered.plane.comp.interfaces;

/**
 * Created by sebastian on 2014/07/28.
 */
public interface InputComponentGroupInterface extends ComponentGroupInterface{

    // add component instance (which has reference to the 'owning' Entity)
    public void addComponent(ComponentInterface componentInterface);

    // for graphics
    // todo: rather split up the ComponentGroupTypes, only specifying thier preferred u[date method
    public void updateGroup( Object... params );

}

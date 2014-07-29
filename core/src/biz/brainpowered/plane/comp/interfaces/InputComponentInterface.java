package biz.brainpowered.plane.comp.interfaces;

/**
 * Created by sebastian on 2014/07/28.
 * Note: this is a Generic Interface for Components, generally we could split up Component<T>Interfaces</T>
 */
public interface InputComponentInterface extends ComponentInterface {

    public void update (Object... params);
}

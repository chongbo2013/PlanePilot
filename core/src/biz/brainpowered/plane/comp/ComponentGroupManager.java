package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.interfaces.ComponentGroupInterface;
import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sebastian on 2014/07/29.
 */
public class ComponentGroupManager {

    public static String GRAPHICS = "Graphics";
    public static String INPUT = "Input";

    Map<String, ComponentGroupInterface> map;
    private static ComponentGroupManager instance = new ComponentGroupManager();
    public static ComponentGroupManager getInstance()
    {
        return instance;
    }
    private ComponentGroupManager () {
        init();
    }

    private boolean init () {
        try
        {
            map = new HashMap<String, ComponentGroupInterface>();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void registerComponentGroup (String id, ComponentGroupInterface componentGroup) {
        map.put(id, componentGroup);
    }

    public ComponentGroupInterface getComponentGroup (String id) {
        try
        {
            return map.get(id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

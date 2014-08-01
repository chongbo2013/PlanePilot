package biz.brainpowered.plane.comp.processors;

import com.badlogic.gdx.*;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Processors Indexed by String rather than int
 * todo: should implement InputComponent (for future reference)
 */
public class InputSystem extends InputMultiplexer {

    private Map<String, InputProcessor> processors = new HashMap<String, InputProcessor>(4);

    public InputSystem () {
        super();
    }

    public void addProcessor(String processorId, InputProcessor processor){
        processors.put(processorId, processor);
    }

    public void removeProcessor(String processorId) {
        processors.remove(processorId);
    }

    /**
     * @todo: safer getting and removals
     * @param processorId
     * @return
     */
    public InputProcessor getProcessor(String processorId) {
        return processors.get(processorId);
    }
}

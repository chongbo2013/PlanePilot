package biz.brainpowered.plane.comp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by sebastian on 2014/07/28.
 * GraphicsComponentGroup Singleton
 */
public class GraphicsComponentGroup implements ComponentGroupInterface {

    private static ComponentGroupInterface instance = new GraphicsComponentGroup();
    private Array<GraphicsComponent> graphicsCollection;
    private GraphicsComponentGroup()
    {
        graphicsCollection = new Array<GraphicsComponent>();
    }
    public static ComponentGroupInterface getInstance()
    {
        return instance;
    }

    @Override
    public void init() {

    }

    @Override
    public void addComponent(ComponentInterface componentInterface) {
        // add component to fast collection
        graphicsCollection.add((GraphicsComponent)componentInterface);
    }

    public void updateGroup ( SpriteBatch batch ) {
        for ( int i =0; i<graphicsCollection.size; i++ ) {
            graphicsCollection.get(i).update(batch);
        }
    }
}

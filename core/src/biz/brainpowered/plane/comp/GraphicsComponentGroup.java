package biz.brainpowered.plane.comp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by sebastian on 2014/07/28.
 * GraphicsComponentGroup Singleton
 */
public class GraphicsComponentGroup implements GraphicsComponentGroupInterface {

    private static GraphicsComponentGroupInterface instance = new GraphicsComponentGroup();
    private Array<GraphicsComponentInterface> graphicsCollection;
    private GraphicsComponentGroup()
    {
        graphicsCollection = new Array<GraphicsComponentInterface>();
    }
    public static GraphicsComponentGroupInterface getInstance()
    {
        return instance;
    }

    @Override
    public void init() {

    }

    @Override
    public void addComponent(GraphicsComponentInterface componentInterface) {
        // add component to fast collection
        graphicsCollection.add(componentInterface);
    }

    // Custom
    public void updateGroup ( SpriteBatch batch ) {
        for ( int i =0; i<graphicsCollection.size; i++ ) {
            graphicsCollection.get(i).update(batch);
        }
    }
}

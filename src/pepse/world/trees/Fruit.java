package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.util.function.Consumer;

public class Fruit extends GameObject {
    public static final String TAG = "fruit";
    private static final float FRUIT_ENERGY = 10;

    private Consumer<Float> onCollisionCallback;
    private Consumer<GameObject> removeObjectCallback;
    private Consumer<GameObject> addObjectCallback;

    private GameObject parent;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Fruit(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.setTag(TAG);
    }

    public void setCollisionCallback(Consumer<Float> onCollisionCallback) {
        this.onCollisionCallback = onCollisionCallback;
    }

    public void setRemoveObjectCallback(Consumer<GameObject> removeObjectCallback) {
        this.removeObjectCallback = removeObjectCallback;
    }

    public void setAddObjectCallback(Consumer<GameObject> addObjectCallback) {
        this.addObjectCallback = addObjectCallback;
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (onCollisionCallback != null) {
            onCollisionCallback.accept(FRUIT_ENERGY);
        }
        if(addObjectCallback != null && removeObjectCallback != null) {
            removeObjectCallback.accept(this);
            new ScheduledTask(parent, PepseGameManager.DAY_LENGTH, false,
                    () -> addObjectCallback.accept(this));
        }
    }
}

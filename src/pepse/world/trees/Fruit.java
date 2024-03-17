package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.util.function.Consumer;

/**
 * class representing fruit
 */
public class Fruit extends GameObject {
    /**
     * fruit tag
     */
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

    /**
     * setting callback
     * @param onCollisionCallback the callback to set
     */
    public void setCollisionCallback(Consumer<Float> onCollisionCallback) {
        this.onCollisionCallback = onCollisionCallback;
    }

    /**
     * removing object callback
     * @param removeObjectCallback the callback to remove
     */
    public void setRemoveObjectCallback(Consumer<GameObject> removeObjectCallback) {
        this.removeObjectCallback = removeObjectCallback;
    }

    /**
     * setting call back to an object
     * @param addObjectCallback the callback to add
     */
    public void setAddObjectCallback(Consumer<GameObject> addObjectCallback) {
        this.addObjectCallback = addObjectCallback;
    }

    /**
     * setting the parent
     * @param parent the parent to set.
     */
    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    /**
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
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

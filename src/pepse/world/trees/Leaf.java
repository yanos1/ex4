package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

public class Leaf extends GameObject {
    public static final String TAG = "leaf";
    private static final float BREEZE_MIN_ROTATION = -10f;
    private static final float BREEZE_MAX_ROTATION = 10f;
    private static final float BREEZE_CYCLE_DURATION = 2f;
    private static final float BREEZE_MAX_DELAY = 1.5f;

    private final Random rand = new Random();

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.setTag(TAG);
        new ScheduledTask(this, rand.nextFloat() * BREEZE_MAX_DELAY, false, this::startBreeze);
    }

    private void startBreeze() {
        new Transition<>(
            this, // the game object being changed
            this.renderer()::setRenderableAngle, // the method to call
            BREEZE_MIN_ROTATION, // initial transition value
            BREEZE_MAX_ROTATION, // final transition value
            Transition.CUBIC_INTERPOLATOR_FLOAT,// use a cubic interpolator
            BREEZE_CYCLE_DURATION, // transition fully over half a day
            Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
            null);
    }
}

package pepse.world.daynight;

import danogl.GameManager;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * This class represents Night in the game.
 */
public class Night {

    private static final String NIGHT_TAG = "night";
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * Creating night and day
     * @param windowDimensions the game dimentions
     * @param cycleLength the length of 1 day and 1 night
     * @return te night game object.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject night = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(Color.BLACK));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        new Transition<>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,// use a cubic interpolator
                cycleLength, // transition fully over half a day
        Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
        null);

        return night;

    }
}

package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;
import java.awt.geom.Area;

/**
 *  The class representing the sun
 */
public class Sun {
    /**
     * constant representing the sun size
     */
    public static final int SUN_SIZE = 100;
    private static final String SUN_TAG = "sun";
    private static final float HALF = 0.5f;
    public static final float FULL_SPIN = 360f;

    /**
     * creating the sun
     * @param windowDimensions game dimensions
     * @param cycleLength 1 day length
     * @return the sun object
     */
    public static GameObject create(Vector2 windowDimensions,
                                    float cycleLength) {
        GameObject sun = new GameObject(
                new Vector2(windowDimensions.x()*HALF, Terrain.groundHeightAtX0*HALF),
                new Vector2(SUN_SIZE, SUN_SIZE), new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);

        Vector2 initialSunCenter = sun.getCenter();
        Vector2 cycleCenter = new Vector2(windowDimensions.x()* HALF,
                Terrain.groundHeightAtX0);
        new Transition<>(
                sun, // the game object being changed
                (Float angle) -> sun.setCenter
                        (initialSunCenter.subtract(cycleCenter)
                                .rotated(angle)
                                .add(cycleCenter)), // the method to call
                0f, // initial transition value
                FULL_SPIN, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,// use a cubic interpolator
                cycleLength, // transition fully over half a day
                Transition.TransitionType.TRANSITION_LOOP, // Choose appropriate ENUM value
                null);

        return sun;
    }

}

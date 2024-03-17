package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * class representing sky
 */
public class Sky {

    private static final String SKY_COLOR = "#80C6E5";
    private static final Color BASIC_SKY_COLOR = Color.decode(SKY_COLOR);
    private static final String SKY_TAG = "sky";

    /**
     * creating sky
     * @param windowDimensions the win dimensions
     * @return the sky object
     */
    public static GameObject create(Vector2 windowDimensions){
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY_TAG);
        return sky;
    }

}

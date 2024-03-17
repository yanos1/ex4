package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

/**
 * representing the sun halo
 */
public class SunHalo {

    private static final float SIZE_FACTOR = 2f;

    /**
     * creating the halo
     * @param sun the sun to follow
     * @return halo object
     */
    public static GameObject create(GameObject sun) {
        GameObject sunHalo = new GameObject(
                new Vector2(sun.getTopLeftCorner()), new Vector2(
                        Sun.SUN_SIZE, Sun.SUN_SIZE).mult(SIZE_FACTOR),
                new OvalRenderable(new Color(255, 255, 0, 20)));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }


}

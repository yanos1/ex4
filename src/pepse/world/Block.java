package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * class representing a block
 */
public class Block extends GameObject {
    /**
     * block size
     */
    public static final int BLOCK_SIZE = 30;

    /**
     * constructor of a block
     * @param topLeftCorner the position to create
     * @param renderable the image of the block
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(BLOCK_SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}

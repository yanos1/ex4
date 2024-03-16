package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameTree {
    private static final int TRUNK_MIN_HEIGHT = 5;
    private static final int TRUNK_MAX_HEIGHT = 10;
    private static final Color TRUNK_BASE_COLOR = new Color(100, 50, 20);

    private GameObject trunk;
    private Random rand = new Random();

    public GameTree(Vector2 bottomRightCorner) {
        createTrunk(bottomRightCorner);
    }

    private void createTrunk(Vector2 position) {
        Vector2 trunkDimensions = new Vector2(
                Block.BLOCK_SIZE, Block.BLOCK_SIZE * rand.nextInt(TRUNK_MIN_HEIGHT, TRUNK_MAX_HEIGHT));
        trunk = new GameObject(position.subtract(trunkDimensions), trunkDimensions,
                new RectangleRenderable(TRUNK_BASE_COLOR));
        trunk.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        trunk.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    public GameObject getTrunk() {
        return trunk;
    }
}

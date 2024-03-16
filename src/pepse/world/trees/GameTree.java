package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameTree {
    private static final int TRUNK_MIN_HEIGHT = 5;
    private static final int TRUNK_MAX_HEIGHT = 10;
    private static final Color TRUNK_BASE_COLOR = new Color(100, 50, 20);
    private static final int LEAF_RADIUS = 3;

    private GameObject trunk;
    private final List<GameObject> leaves = new ArrayList<>();
    private final Random rand = new Random();
    private Vector2 treeTop;

    public GameTree(Vector2 bottomRightCorner) {
        createTrunk(bottomRightCorner);
        createLeaves();
    }

    private void createTrunk(Vector2 position) {
        int treeHeight = Block.BLOCK_SIZE *
                (rand.nextInt(TRUNK_MAX_HEIGHT - TRUNK_MIN_HEIGHT) + TRUNK_MIN_HEIGHT);
        Vector2 trunkDimensions = new Vector2(Block.BLOCK_SIZE, treeHeight);
        treeTop = position.subtract(trunkDimensions);
        trunk = new GameObject(treeTop, trunkDimensions,
                new RectangleRenderable(TRUNK_BASE_COLOR));
        trunk.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        trunk.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    private void createLeaves() {
        for (int x = -LEAF_RADIUS; x < LEAF_RADIUS; x++) {
            for (int y = -LEAF_RADIUS; y < LEAF_RADIUS; y++) {
//                leaves.add(new GameObject(treeTop.subtract(Vector2.ONES.mult(Block.BLOCK_SIZE))))
            }
        }
    }

    public GameObject getTrunk() {
        return trunk;
    }
}

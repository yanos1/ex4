package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameTree {
    private static final int TRUNK_MIN_HEIGHT = 5;
    private static final int TRUNK_MAX_HEIGHT = 10;
    private static final Color TRUNK_BASE_COLOR = new Color(100, 50, 20);
    private static final int TRUNK_HEIGHT_OFFSET = 10;
    private static final int FOLIAGE_RADIUS = 3;
    private static final float FOLIAGE_DENSITY = 1.2f;
    private static final float FOLIAGE_DENSITY_DISTANCE_MODIFIER = 0.2f;
    private static final Color LEAF_BASE_COLOR = new Color(50, 200, 30);
    private static final float LEAF_SIZE_IN_BLOCKS = 0.8f;
    private static final int LEAF_CENTER_HEIGHT_OFFSET = 2;

    private GameObject trunk;
    private final List<GameObject> leaves = new ArrayList<>();
    private final Random rand = new Random();
    private Vector2 treeTop;

    public GameTree(Vector2 bottomRightCorner) {
        createTrunk(bottomRightCorner);
        createLeaves();
    }

    public GameObject getTrunk() {
        return trunk;
    }

    public List<GameObject> getLeaves() {
        return leaves;
    }

    private void createTrunk(Vector2 position) {
        int treeHeight = Block.BLOCK_SIZE *
                (rand.nextInt(TRUNK_MAX_HEIGHT - TRUNK_MIN_HEIGHT) + TRUNK_MIN_HEIGHT);
        Vector2 trunkDimensions = new Vector2(Block.BLOCK_SIZE, treeHeight);
        treeTop = position.subtract(trunkDimensions).add(Vector2.DOWN.mult(TRUNK_HEIGHT_OFFSET));
        trunk = new GameObject(treeTop, trunkDimensions,
                new RectangleRenderable(TRUNK_BASE_COLOR));
        trunk.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        trunk.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    private void createLeaves() {
        for (int x = -FOLIAGE_RADIUS; x <= FOLIAGE_RADIUS; x++) {
            for (int y = -FOLIAGE_RADIUS; y <= FOLIAGE_RADIUS; y++) {
                if (shouldCreateLeaf(x,y)) {
                    createLeaf(x, y);
                }
            }
        }
    }

    private boolean shouldCreateLeaf(int x, int y) {
        return rand.nextFloat() < FOLIAGE_DENSITY -
                (Math.sqrt(Math.pow(x,2) + Math.pow(y,2)) * FOLIAGE_DENSITY_DISTANCE_MODIFIER);
    }

    private void createLeaf(int x, int y) {
        Vector2 leafSize = Vector2.ONES.mult(Block.BLOCK_SIZE * LEAF_SIZE_IN_BLOCKS);
        Vector2 leafPosition = treeTop.add(Vector2.UP.mult(Block.BLOCK_SIZE * LEAF_CENTER_HEIGHT_OFFSET)).add(
                new Vector2(Block.BLOCK_SIZE * x, Block.BLOCK_SIZE * y));
        Leaf leaf = new Leaf(leafPosition, leafSize, new RectangleRenderable(LEAF_BASE_COLOR));
        leaves.add(leaf);
    }
}

package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameTree extends GameObject {
    public static final String TRUNK_TAG = "trunk";
    private static final int TRUNK_MIN_HEIGHT = 5;
    private static final int TRUNK_MAX_HEIGHT = 10;
    private static final Color TRUNK_BASE_COLOR = new Color(100, 50, 20);
    private static final int TRUNK_HEIGHT_OFFSET = 10;
    private static final int FOLIAGE_RADIUS = 3;
    private static final float FOLIAGE_DENSITY = 1.2f;
    private static final float FOLIAGE_DENSITY_DISTANCE_MODIFIER = 0.2f;
    private static final Color LEAF_BASE_COLOR = new Color(50, 200, 30);
    private static final float LEAF_SIZE_IN_BLOCKS = 0.8f;
    private static final int FOLIAGE_CENTER_HEIGHT_OFFSET = 2;
    private static final float FRUIT_DENSITY = 0.3f;
    private static final Color FRUIT_BASE_COLOR = new Color(200, 55, 55);
    private static final float FRUIT_SIZE_IN_BLOCKS = 0.7f;
    private static final int COLOR_VARIATION = 61;
    private static final int COLOR_VARIATION_OFFSET = 30;
    private static final int MAX_COLOR_VALUE = 255;
    private static final int MIN_COLOR_VALUE = 0;

    private GameObject trunk;
    private final List<Leaf> leaves = new ArrayList<>();
    private final List<Fruit> fruits = new ArrayList<>();
    private final Random rand = new Random();
    private Vector2 treeTop;

    public GameTree(Vector2 bottomRightCorner) {
        super(bottomRightCorner, Vector2.ZERO, null);
        createTrunk(bottomRightCorner);
        createFoliage();
    }

    public GameObject getTrunk() {
        return trunk;
    }

    public List<Leaf> getLeaves() {
        return leaves;
    }

    public List<Fruit> getFruits() { return fruits; }

    private void createTrunk(Vector2 position) {
        int treeHeight = Block.BLOCK_SIZE *
                (rand.nextInt(TRUNK_MAX_HEIGHT - TRUNK_MIN_HEIGHT) + TRUNK_MIN_HEIGHT);
        Vector2 trunkDimensions = new Vector2(Block.BLOCK_SIZE, treeHeight);
        treeTop = position.subtract(trunkDimensions).add(Vector2.DOWN.mult(TRUNK_HEIGHT_OFFSET));
        trunk = new GameObject(treeTop, trunkDimensions,
                new RectangleRenderable(TRUNK_BASE_COLOR));
        trunk.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        trunk.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        trunk.setTag(TRUNK_TAG);
    }

    private void createFoliage() {
        for (int x = -FOLIAGE_RADIUS; x <= FOLIAGE_RADIUS; x++) {
            for (int y = -FOLIAGE_RADIUS; y <= FOLIAGE_RADIUS; y++) {
                if (shouldCreateLeaf(x,y)) {
                    createLeaf(x,y);
                }
                if (shouldCreateFruit(x,y)) {
                    createFruit(x,y);
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
        Vector2 leafPosition = treeTop.add(Vector2.UP.mult(Block.BLOCK_SIZE * FOLIAGE_CENTER_HEIGHT_OFFSET))
                .add(new Vector2(Block.BLOCK_SIZE * x, Block.BLOCK_SIZE * y));
        Leaf leaf = new Leaf(leafPosition, leafSize, new RectangleRenderable(LEAF_BASE_COLOR));
        leaves.add(leaf);
    }

    private boolean shouldCreateFruit(int x, int y) {
        return rand.nextFloat() < FRUIT_DENSITY;
    }

    private void createFruit(int x, int y) {
        Vector2 fruitSize = Vector2.ONES.mult(Block.BLOCK_SIZE * FRUIT_SIZE_IN_BLOCKS);
        Vector2 fruitPosition = treeTop.add(Vector2.UP.mult(Block.BLOCK_SIZE * FOLIAGE_CENTER_HEIGHT_OFFSET))
                .add(new Vector2(Block.BLOCK_SIZE * x, Block.BLOCK_SIZE * y));
        Fruit fruit = new Fruit(fruitPosition, fruitSize, new OvalRenderable(FRUIT_BASE_COLOR));
        fruit.setParent(this);
        fruits.add(fruit);
    }

    public void rotateLeaves() {
        for (Leaf leaf : leaves) {
            leaf.rotate90Degrees();
        }
    }

    public void changeFruitColor() {
        for (Fruit fruit : fruits) {
            fruit.renderer().setRenderable(new OvalRenderable(generateColorVariation(FRUIT_BASE_COLOR)));
        }
    }

    public void changeTrunkColor() {
        trunk.renderer().setRenderable(new RectangleRenderable(generateColorVariation(TRUNK_BASE_COLOR)));
    }

    private Color generateColorVariation(Color baseColor) {
        return new Color(
                generateColorAxisVariation(baseColor.getRed()),
                generateColorAxisVariation(baseColor.getGreen()),
                generateColorAxisVariation(baseColor.getBlue()));
    }

    private int generateColorAxisVariation(int colorAxis) {
        return Math.min(MAX_COLOR_VALUE, Math.max(MIN_COLOR_VALUE,
                colorAxis + rand.nextInt(COLOR_VARIATION) - COLOR_VARIATION_OFFSET));
    }
}

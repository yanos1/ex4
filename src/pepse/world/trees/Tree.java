package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

public class Tree {
    private static final int TRUNK_MIN_HEIGHT = 5;
    private static final int TRUNK_MAX_HEIGHT = 10;
    private static final Color TRUNK_BASE_COLOR = new Color(100, 50, 20);

    private GameObject trunk;
    private Random rand = new Random();

    private Vector2 bottomRightCorner;

    public Tree (Vector2 bottomRightCorner) {
        this.bottomRightCorner = bottomRightCorner;
    }

    public void createObjects(GameObjectCollection gameObjects) {
        Vector2 trunkDimensions = new Vector2(
                Block.BLOCK_SIZE, Block.BLOCK_SIZE * rand.nextInt(TRUNK_MIN_HEIGHT, TRUNK_MAX_HEIGHT));
        trunk = new GameObject(bottomRightCorner.subtract(trunkDimensions), trunkDimensions,
                new RectangleRenderable(TRUNK_BASE_COLOR));
        gameObjects.addGameObject(trunk);
    }
}

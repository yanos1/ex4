package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static pepse.world.Block.BLOCK_SIZE;


public class Terrain {

    public static int groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR =  new Color(212,123,74);
    private final NoiseGenerator noiseGenerator;
    public Terrain(Vector2 windowDimensions, int seed) {
        groundHeightAtX0 = (int)((windowDimensions.y()*2)/3);
        noiseGenerator = new NoiseGenerator(1234,groundHeightAtX0);
    }

    public float groundHeightAt(float x) {
                float noise = (float) noiseGenerator.noise(x, BLOCK_SIZE *7);
               return groundHeightAtX0 + noise;
    }

    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();

        float optimalHeight = groundHeightAtX0;
        int normalizedHeight = (int)Math.floor(optimalHeight / BLOCK_SIZE) * BLOCK_SIZE;
        int bricksToCreateInX = (int)(optimalHeight / BLOCK_SIZE);
        createBlockForCurrentX(bricksToCreateInX, 0, normalizedHeight, blocks);

        float currentX = minX;
        while (currentX < maxX) {
            optimalHeight = groundHeightAt(currentX);
            normalizedHeight = (int)Math.floor(optimalHeight / BLOCK_SIZE) * BLOCK_SIZE;
            bricksToCreateInX = (int)(optimalHeight / BLOCK_SIZE);
            createBlockForCurrentX(bricksToCreateInX, currentX, normalizedHeight, blocks);
            currentX += BLOCK_SIZE;
        }
        return blocks;

    }

    private static void createBlockForCurrentX(int bricksToCreateInX, float currentX, int normalizedHeight, List<Block> blocks) {
        for(int i = 0; i < bricksToCreateInX; ++i){
            RectangleRenderable rectangleRenderable = new RectangleRenderable(ColorSupplier.approximateColor(
                    BASE_GROUND_COLOR));
            Block block = new Block(new Vector2(currentX, normalizedHeight + i*BLOCK_SIZE),
                    rectangleRenderable);
            blocks.add(block);
        }
    }
}

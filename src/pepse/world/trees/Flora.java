package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class Flora {
    private static final float TREE_ABUNDANCE = 0.12f;

    private final Function<Float, Float> groundHeightCallback;

    public Flora(Function<Float, Float> groundHeightCallback) {
        this.groundHeightCallback = groundHeightCallback;
    }

    public Set<GameTree> createInRange(int minX, int maxX) {
        Set<GameTree> trees = new HashSet<>();
        for(float x = minX + Block.BLOCK_SIZE; x < maxX; x += Block.BLOCK_SIZE) {
            if (Math.random() < TREE_ABUNDANCE) {
                trees.add(new GameTree(new Vector2(x, groundHeightCallback.apply(x))));
                x += Block.BLOCK_SIZE * 2;
            }
        }
        return trees;
    }
}

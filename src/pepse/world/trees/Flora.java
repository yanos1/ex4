package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Block;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class Flora {
    private static final float TREE_ABUNDANCE = 0.1f;

    private final Function<Float, Float> groundHeightCallback;

    public Flora(Function<Float, Float> groundHeightCallback) {
        this.groundHeightCallback = groundHeightCallback;
    }

    public Set<Tree> createInRange(int minX, int maxX) {
        Set<Tree> trees = new HashSet<>();
        for(int x = minX + Block.BLOCK_SIZE; x < maxX; x += Block.BLOCK_SIZE) {
            if (Math.random() < TREE_ABUNDANCE) {
                trees.add(new Tree(new Vector2(x, groundHeightCallback.apply((float)x))));
            }
        }
        return trees;
    }
}

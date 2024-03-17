package pepse.world.trees;

import danogl.util.Vector2;
import pepse.world.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * representing the flora of the game
 */
public class Flora {
    private static final float TREE_DENSITY = 0.15f;

    private final Function<Float, Float> groundHeightCallback;

    private Set<GameTree> trees;

    /**
     *  the constructor for creating a flora
     * @param groundHeightCallback a callback for activating flora
     */
    public Flora(Function<Float, Float> groundHeightCallback) {
        this.groundHeightCallback = groundHeightCallback;
    }

    /**
     * creating flora at given range
     * @param minX min range
     * @param maxX max range
     * @return the position
     */
    public Set<GameTree> createInRange(int minX, int maxX) {
        trees = new HashSet<>();
        for(float x = minX + Block.BLOCK_SIZE; x < maxX; x += Block.BLOCK_SIZE) {
            if (Math.random() < TREE_DENSITY) {
                trees.add(new GameTree(new Vector2(x, groundHeightCallback.apply(x))));
                x += Block.BLOCK_SIZE * 2;
            }
        }
        return trees;
    }

    /**
     * applying the callbacks after the character jumped
     */
    public void reactToCharacter() {
        for (GameTree tree : trees) {
            tree.rotateLeaves();
            tree.changeFruitColor();
            tree.changeTrunkColor();
        }
    }
}

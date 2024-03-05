package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.util.List;

public class PepseGameManager extends GameManager {
    public static final int SEED = 10;
    public static final float DAY_LENGTH = 30f;

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader,soundReader,inputListener,windowController);

        gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()), Layer.BACKGROUND);

        initializeTerrain(windowController);

        gameObjects().addGameObject(Night.create(windowController.getWindowDimensions(), DAY_LENGTH/2),
                Layer.DEFAULT);

        initializeSun(windowController);
    }

    private void initializeSun(WindowController windowController) {
        GameObject sun = Sun.create(windowController.getWindowDimensions(),DAY_LENGTH);
        gameObjects().addGameObject(sun,Layer.BACKGROUND);
        gameObjects().addGameObject(SunHalo.create(sun), Layer.BACKGROUND);
    }


    private void initializeTerrain(WindowController windowController) {
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), SEED);
        List<Block> blocks = terrain.createInRange(Block.BLOCK_SIZE,(int)windowController.getWindowDimensions().x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block,Layer.STATIC_OBJECTS);
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

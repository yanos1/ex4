package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.ui.EnergyCounter;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.GameTree;

import java.util.List;
import java.util.Set;

public class PepseGameManager extends GameManager {
    public static final int SEED = 10;
    public static final float DAY_LENGTH = 30f;
    public static final Vector2 ENERGY_COUNTER_PADDING = new Vector2(20, -20);

    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;

    private Terrain terrain;
    private Avatar avatar;

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader,soundReader,inputListener,windowController);

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;

        gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()), Layer.BACKGROUND);

        this.terrain = initializeTerrain();

        gameObjects().addGameObject(Night.create(windowController.getWindowDimensions(), DAY_LENGTH/2),
                Layer.DEFAULT);

        initializeSun();

        initializeFlora();

        this.avatar = createAvatar();

        initializeUI();
    }

    private Terrain initializeTerrain() {
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), SEED);
        List<Block> blocks = terrain.createInRange(Block.BLOCK_SIZE,(int)windowController.getWindowDimensions().x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block,Layer.STATIC_OBJECTS);
        }
        return terrain;
    }

    private void initializeSun() {
        GameObject sun = Sun.create(windowController.getWindowDimensions(),DAY_LENGTH);
        gameObjects().addGameObject(sun,Layer.BACKGROUND);
        gameObjects().addGameObject(SunHalo.create(sun), Layer.BACKGROUND);
    }

    private Avatar createAvatar() {
        float avatarXPosition = windowController.getWindowDimensions().x() / 2;
        float avatarYPosition = terrain.groundHeightAt(avatarXPosition);
        Avatar avatar = new Avatar(
                new Vector2(avatarXPosition, avatarYPosition), inputListener, imageReader);
        gameObjects().addGameObject(avatar);
        return avatar;
    }

    private void initializeUI() {
        EnergyCounter energyCounter = new EnergyCounter();
        Vector2 energyCounterPosition =
                new Vector2(0, windowController.getWindowDimensions().y() - EnergyCounter.COUNTER_SIZE.y())
                        .add(ENERGY_COUNTER_PADDING);
        gameObjects().addGameObject(energyCounter.createCounter(
                energyCounterPosition, (int) avatar.GetEnergy()));

        avatar.SetEnergyChangeCallback(energyCounter::updateCounter);
    }

    private void initializeFlora() {
        Flora flora = new Flora(terrain::groundHeightAt);
        Set<GameTree> trees = flora.createInRange(0, (int)windowController.getWindowDimensions().x());
        for (GameTree tree : trees) {
            gameObjects().addGameObject(tree.getTrunk(), Layer.STATIC_OBJECTS);
            for (GameObject leaf : tree.getLeaves()) {
                gameObjects().addGameObject(leaf, Layer.FOREGROUND);
            }
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

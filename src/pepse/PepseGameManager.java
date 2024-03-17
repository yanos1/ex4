package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
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
import pepse.world.trees.Fruit;
import pepse.world.trees.GameTree;
import pepse.world.trees.Leaf;

import java.util.List;
import java.util.Set;

/**
 * the game manager of the game.
 */
public class PepseGameManager extends GameManager {
    /**
     * seed value
     */
    public static final int SEED = 10;
    /**
     * day length
     */
    public static final float DAY_LENGTH = 30f;
    private static final int FRAMERATE = 61;

    private static final Vector2 ENERGY_COUNTER_PADDING = new Vector2(20, -20);

    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;

    private Terrain terrain;
    private Avatar avatar;

    /**
     * initlizing game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader,soundReader,inputListener,windowController);
        windowController.setTargetFramerate(FRAMERATE);

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;

        gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()), Layer.BACKGROUND);

        this.terrain = initializeTerrain();

        gameObjects().addGameObject(Night.create(windowController.getWindowDimensions(),
                        DAY_LENGTH/2),
                Layer.DEFAULT);

        initializeSun();

        this.avatar = createAvatar();

        initializeUI();

        initializeFlora();
    }
    /*
    initilize terrain
     */
    private Terrain initializeTerrain() {
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), SEED);
        List<Block> blocks = terrain.createInRange(Block.BLOCK_SIZE,
                (int)windowController.getWindowDimensions().x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block,Layer.STATIC_OBJECTS);
        }
        return terrain;
    }
/*
initialize sun
 */
    private void initializeSun() {
        GameObject sun = Sun.create(windowController.getWindowDimensions(),DAY_LENGTH);
        gameObjects().addGameObject(sun,Layer.BACKGROUND);
        gameObjects().addGameObject(SunHalo.create(sun), Layer.BACKGROUND);
    }
/*
initialize avatar
 */
    private Avatar createAvatar() {
        float avatarXPosition = windowController.getWindowDimensions().x() / 2;
        float avatarYPosition = terrain.groundHeightAt(avatarXPosition);
        Avatar avatar = new Avatar(
                new Vector2(avatarXPosition, avatarYPosition), inputListener, imageReader);
        gameObjects().addGameObject(avatar);
        return avatar;
    }
/*
initialize UI
 */
    private void initializeUI() {
        EnergyCounter energyCounter = new EnergyCounter();
        Vector2 energyCounterPosition =
                new Vector2(0, windowController.getWindowDimensions().y() -
                        EnergyCounter.COUNTER_SIZE.y())
                        .add(ENERGY_COUNTER_PADDING);
        gameObjects().addGameObject(energyCounter.createCounter(
                energyCounterPosition, (int) avatar.GetEnergy()));

        avatar.SetEnergyChangeCallback(energyCounter::updateCounter);
    }
/*
initialize flora
 */
    private void initializeFlora() {
        Flora flora = new Flora(terrain::groundHeightAt);
        avatar.addJumpCallback(flora::reactToCharacter);
        Set<GameTree> trees = flora.createInRange(0, (int)windowController.getWindowDimensions().x());
        for (GameTree tree : trees) {
            gameObjects().addGameObject(tree);
            gameObjects().addGameObject(tree.getTrunk(), getObjectLayerByTag(tree.getTrunk()));
            for (Leaf leaf : tree.getLeaves()) {
                gameObjects().addGameObject(leaf, getObjectLayerByTag(leaf));
            }
            for (Fruit fruit : tree.getFruits()) {
                fruit.setCollisionCallback(avatar::addEnergy);
                fruit.setAddObjectCallback(gameObject ->
                        gameObjects().addGameObject(gameObject, getObjectLayerByTag(gameObject)));
                fruit.setRemoveObjectCallback(gameObject ->
                        gameObjects().removeGameObject(gameObject, getObjectLayerByTag(gameObject)));
                gameObjects().addGameObject(fruit, getObjectLayerByTag(fruit));
            }
        }
    }

    private int getObjectLayerByTag(GameObject gameObject) {
        switch(gameObject.getTag()) {
            case GameTree.TRUNK_TAG:
                return Layer.STATIC_OBJECTS;
            case Leaf.TAG:
                return Layer.FOREGROUND;
            case Fruit.TAG:
                return Layer.STATIC_OBJECTS;
            default:
                return Layer.DEFAULT;
        }
    }

    /**
     * running the game
     * @param args not used.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

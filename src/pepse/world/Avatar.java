package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class of the simulations main playable character.
 */
public class Avatar extends GameObject {
    public static final String[] IDLE_SPRITE_FILES =
            {"assets/idle_0.png", "assets/idle_1.png", "assets/idle_2.png", "assets/idle_3.png"};
    public static final String[] RUN_SPRITE_FILES =
            {"assets/run_0.png", "assets/run_1.png", "assets/run_2.png", "assets/run_3.png",
                    "assets/run_4.png", "assets/run_5.png"};
    public static final String[] JUMP_SPRITE_FILES =
            {"assets/jump_0.png", "assets/jump_1.png", "assets/jump_2.png", "assets/jump_3.png"};
    public static final int SPRITE_WIDTH = 50;
    public static final int SPRITE_HEIGHT = 50;
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 800;
    private static final float IDLE_ENERGY_GAIN = 1;
    private static final float RUN_ENERGY_LOSS = 0.5f;
    private static final float JUMP_ENERGY_LOSS = 10;
    private static final float MAX_ENERGY = 100;
    private static final float MIN_ENERGY = 0;
    private static final float TIME_BETWEEN_ANIMATIONS = 0.1f;

    private enum MovementState { IDLE, RUN, JUMP };

    private final UserInputListener inputListener;
    private float energy;
    private MovementState state;

    private final AnimationRenderable idleAnimation;
    private final AnimationRenderable runAnimation;
    private final AnimationRenderable jumpAnimation;

    private Consumer<Integer> energyChangeCallback;
    private final List<Runnable> jumpCallbacks = new LinkedList<>();

    /**
     * Constructor.
     * @param bottomRightCorner Bottom right corner of character.
     * @param inputListener Game managers input listener to get user input.
     * @param imageReader Game managers image reader to get avatars sprites.
     */
    public Avatar (Vector2 bottomRightCorner, UserInputListener inputListener, ImageReader imageReader) {
        super(bottomRightCorner.subtract(new Vector2(SPRITE_WIDTH, SPRITE_HEIGHT)),
                new Vector2(SPRITE_WIDTH, SPRITE_HEIGHT),
                imageReader.readImage(IDLE_SPRITE_FILES[0], false));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);

        this.inputListener = inputListener;
        energy = MAX_ENERGY;
        state = MovementState.IDLE;

        idleAnimation = new AnimationRenderable(
                IDLE_SPRITE_FILES, imageReader, false, TIME_BETWEEN_ANIMATIONS);
        runAnimation = new AnimationRenderable(
                RUN_SPRITE_FILES, imageReader, false, TIME_BETWEEN_ANIMATIONS);
        jumpAnimation = new AnimationRenderable(
                JUMP_SPRITE_FILES, imageReader, false, TIME_BETWEEN_ANIMATIONS);
    }

    public void SetEnergyChangeCallback(Consumer<Integer> energyChangeCallback) {
        this.energyChangeCallback = energyChangeCallback;
    }

    public float GetEnergy() {
        return energy;
    }

    public void addEnergy(float amount) {
        energy += amount;
        if (energy < MIN_ENERGY) {
            energy = MIN_ENERGY;
        }
        else if (energy > MAX_ENERGY) {
            energy = MAX_ENERGY;
        }

        if (energyChangeCallback != null) {
            energyChangeCallback.accept((int)energy);
        }
    }

    public void addJumpCallback(Runnable callback) {
        jumpCallbacks.add(callback);
    }

    /**
     * Simple avatar movement logic.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        state = MovementState.IDLE;
        updateRunning();
        updateJumping();
        updateEnergy();
        updateAnimation();
    }

    private void updateRunning() {
        if (energy < RUN_ENERGY_LOSS) {
            transform().setVelocityX(0);
            return;
        }

        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT))
        {
            xVel -= VELOCITY_X;
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
        {
            xVel += VELOCITY_X;
        }
        if (xVel != 0) {
            state = MovementState.RUN;
        }
        transform().setVelocityX(xVel);
    }

    private void updateJumping() {
        if (energy < JUMP_ENERGY_LOSS) {
            return;
        }

        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0)
        {
            state = MovementState.JUMP;
            transform().setVelocityY(VELOCITY_Y);
            for (Runnable callback : jumpCallbacks) {
                callback.run();
            }
        }
    }

    private void updateEnergy() {
        switch(state) {
            case MovementState.IDLE:
                addEnergy(IDLE_ENERGY_GAIN);
                break;
            case MovementState.RUN:
                addEnergy(-RUN_ENERGY_LOSS);
                break;
            case MovementState.JUMP:
                addEnergy(-JUMP_ENERGY_LOSS);
                break;
        }
    }

    private void updateAnimation() {
        if (transform().getVelocity().x() != 0) {
            renderer().setRenderable(runAnimation);
            renderer().setIsFlippedHorizontally(transform().getVelocity().x() < 0);
        }
        else if (transform().getVelocity().y() != 0) {
            renderer().setRenderable(jumpAnimation);
        }
        else {
            renderer().setRenderable(idleAnimation);
        }
    }
}

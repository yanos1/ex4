package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Class of the simulations main playable character.
 */
public class Avatar extends GameObject {
    public static final String IDLE_SPRITE_FILE = "assets/idle_0.png";
    public static final int SPRITE_WIDTH = 50;
    public static final int SPRITE_HEIGHT = 50;
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final float IDLE_ENERGY_GAIN = 1;
    private static final float RUN_ENERGY_LOSS = 0.5f;
    private static final float JUMP_ENERGY_LOSS = 10;
    private static final float MAX_ENERGY = 100;
    private static final float MIN_ENERGY = 0;

    private enum MovementState { IDLE, RUN, JUMP };

    private final UserInputListener inputListener;
    private float energy;
    private MovementState state;

    /**
     * Constructor.
     * @param pos Bottom right corner of character.
     * @param inputListener Game managers input listener to get user input.
     * @param imageReader Game managers image reader to get avatars sprites.
     */
    public Avatar (Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos.subtract(new Vector2(SPRITE_WIDTH, SPRITE_HEIGHT)),
                new Vector2(SPRITE_WIDTH, SPRITE_HEIGHT),
                imageReader.readImage(IDLE_SPRITE_FILE, false));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        energy = MAX_ENERGY;
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
        }
    }

    private void updateEnergy() {
        switch(state) {
            case MovementState.IDLE:
                energy = Math.min(MAX_ENERGY, energy + IDLE_ENERGY_GAIN);
                break;
            case MovementState.RUN:
                energy = Math.max(MIN_ENERGY, RUN_ENERGY_LOSS);
                break;
            case MovementState.JUMP:
                energy -= Math.max(MIN_ENERGY, JUMP_ENERGY_LOSS);
                break;
        }
    }
}

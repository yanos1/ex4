package pepse.ui;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

/**
 * this class represents the energy of the player which is consumed by walking / jumping.
 */
public class EnergyCounter {
    /**
     * The size of the energy counter
     */
    public static final Vector2 COUNTER_SIZE = new Vector2(100, 40);

    private final TextRenderable counterText;

    /**
     * creating the text
     */
    public EnergyCounter() {
        this.counterText = new TextRenderable("");
    }

    /**
     * creating the energy game objcet.
     * @param position the position of the energy label.
     * @param startingEnergy the starting energy value
     * @return the game object
     */
    public GameObject createCounter(Vector2 position, int startingEnergy) {
        updateCounter(startingEnergy);
        return new GameObject(position, COUNTER_SIZE, counterText);
    }

    /**
     * updating the energy amount
     * @param energy the new energy.
     */
    public void updateCounter(int energy) {
        counterText.setString(String.valueOf(energy));
    }
}

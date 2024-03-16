package pepse.ui;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

public class EnergyCounter {
    public static final Vector2 COUNTER_SIZE = new Vector2(100, 40);

    private final TextRenderable counterText;

    public EnergyCounter() {
        this.counterText = new TextRenderable("");
    }

    public GameObject createCounter(Vector2 position, int startingEnergy) {
        updateCounter(startingEnergy);
        return new GameObject(position, COUNTER_SIZE, counterText);
    }

    public void updateCounter(int energy) {
        counterText.setString(String.valueOf(energy));
    }
}

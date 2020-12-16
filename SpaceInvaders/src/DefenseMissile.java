import objectdraw.*;
import java.awt.*;

/**
 * A DefenseMissile is an ActiveObject that can be fired at Aliens by the
 * defender ship. It stops and removes itself from the canvas if it hits an
 * Alien or reaches the top of the play area.
 */
public class DefenseMissile extends ActiveObject {
	private static final int PAUSE_TIME = 5;

	// constants for missile size
	private static final int MISSILE_LENGTH = 40;
	private static final int MISSILE_WIDTH = 3;

	// filled rect representing a missile
	private FilledRect missile;

	// playing field that holds space ship and aliens
	private FilledRect playField;

	// invaders object that holds array full of aliens
	private Invaders invaders;

	/**
	 * constructor for defense missile missile shot by space ship, travels up the
	 * screen and can kill aliens
	 * 
	 * @param start
	 *            location missile is created at
	 * @param playField
	 *            field that holds aliens and ship. missile stops once it hits the
	 *            top
	 * @param invaders
	 *            invaders object
	 * @param canvas
	 *            drawing canvas
	 */
	public DefenseMissile(Location start, FilledRect playField, Invaders invaders, DrawingCanvas canvas) {
		this.playField = playField;
		this.invaders = invaders;

		// create the missile and set its color
		missile = new FilledRect(start, MISSILE_WIDTH, MISSILE_LENGTH, canvas);
		missile.setColor(Color.WHITE);
		start();
	}

	/**
	 * run thread for defense missile active object. moves missile up the screen after it is shot
	 */
	public void run() {

		// while the missile is in the playing field, progressively move it up
		while (missile.getY() > playField.getY()) {
			missile.move(0, -1);
			pause(PAUSE_TIME);

			// remove missile if it hits an alien
			if (invaders.overlap(missile)) {
				missile.removeFromCanvas();
			}
		}

		// if missile hasn't hit an alien, remove it when it hits the top of the playing
		// field
		if (missile.getCanvas() != null) {
			missile.removeFromCanvas();
		}
	}
}

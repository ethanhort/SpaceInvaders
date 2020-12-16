import objectdraw.*;
import java.awt.*;

/**
 * A Missle is fired, by Aliens, at the defender ship. It is an active object
 * that stops and removes itself from the canvas when it reaches hits its target
 * or reaches the bottom of the play area.
 */
public class Missile extends ActiveObject {
	private static final int PAUSE_TIME = 5;

	// constants that determine size of the missile
	private static final int MISSILE_LENGTH = 40;
	private static final int MISSILE_WIDTH = 3;

	// filled rect representing the missile
	private FilledRect missile;

	// playing field that contains ship, aliens, etc
	private FilledRect playField;

	// space ship object
	private SpaceShip ship;

	/**
	 * constructor for missile missile is shot by aliens and moves down the screen
	 * toward the space ship
	 * 
	 * @param playField
	 *            rectangle representing playing field
	 * @param startX
	 *            x coordinate at which missile is created
	 * @param startY
	 *            y coordinate at which missile is created
	 * @param ship
	 *            space ship object that missiles are shot at
	 * @param canvas
	 *            drawing canvas
	 */
	public Missile(FilledRect playField, double startX, double startY, SpaceShip ship, DrawingCanvas canvas) {
		this.playField = playField;
		this.ship = ship;

		// create the missile and set its color
		missile = new FilledRect(startX, startY, MISSILE_WIDTH, MISSILE_LENGTH, canvas);
		missile.setColor(Color.WHITE);
		start();
	}

	/**
	 * run thread that moves the missile down the screen
	 */
	public void run() {

		// while missile is still in the playing field, progressively move it down
		while (missile.getY() + missile.getHeight() < playField.getY() + playField.getHeight()) {
			missile.move(0, 1);

			// if the missile hits the ship, kill the ship and remove the missile
			if (ship.overlaps(missile)) {
				ship.kill();
				missile.removeFromCanvas();
			}
			pause(PAUSE_TIME);
		}

		// if the missile still exists when it hits the bottom of the screen, remove it
		if (missile.getCanvas() != null) {
			missile.removeFromCanvas();
		}
	}

}

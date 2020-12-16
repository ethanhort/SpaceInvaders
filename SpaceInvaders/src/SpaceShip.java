import objectdraw.*;
import java.awt.*;

/**
 * The SpaceShip moves and fires missiles in response to keyboard input from the
 * player.
 *
 * Note that it only needs to extend ActiveObject if you want it to move
 * continuously. If it simply moves from one square to the next in one sudden
 * motion, this should not be necessary.
 */
public class SpaceShip extends ActiveObject {
	private static final int PAUSE_TIME = 8;

	// directions based on which arrow key is pressed
	private static final int LEFT = 1;
	private static final int RIGHT = 2;

	// number of pixels to move ship by
	private static final int DISTANCE = 1;

	// objects for holding ship picture, alien array, score keeper
	private VisibleImage ship;
	private Invaders invaders;
	private ScoreKeeper scoreDisplay;

	// boolean tracks whether ship has been hit
	private boolean alive = true;

	// int corresponds to direction ship needs to move
	private int direction = 0;

	// instance variables for the square playing field and drawing canvas
	private FilledRect playField;
	private DrawingCanvas canvas;

	/**
	 * constructor for the space ship. draws space ship
	 * 
	 * @param ship
	 *            rocket ship that shoots aliens
	 * @param start
	 *            location that ship is created at
	 * @param playField
	 *            field that contains aliens and ship
	 * @param size
	 *            size of ship
	 * @param scoreDisplay
	 *            scoreKeeper object that displays score
	 * @param canvas
	 *            Drawing Canvas
	 */
	public SpaceShip(Image ship, Location start, FilledRect playField, int size, ScoreKeeper scoreDisplay,
			DrawingCanvas canvas) {

		// create the ship image
		this.ship = new VisibleImage(ship, start, canvas);
		this.ship.setSize(size, size);

		this.playField = playField;
		this.canvas = canvas;
		this.scoreDisplay = scoreDisplay;
		start();
	}

	/**
	 * sets the direction that the ship moves
	 * 
	 * @param dir
	 *            direction to move
	 */
	public void setDirection(int dir) {
		direction = dir;
	}

	/**
	 * check if the missile passed as a parameter hit the ship
	 * 
	 * @param missile
	 *            missile shot by alien
	 * @return true if objects overlap
	 */
	public boolean overlaps(FilledRect missile) {
		return missile.overlaps(ship);
	}

	/**
	 * kill the ship and display game over message
	 */
	public void kill() {
		ship.removeFromCanvas();
		scoreDisplay.setText("Game Over");
		alive = false;
	}

	/**
	 * check if ship has been hit by a missile
	 * 
	 * @return true if ship is dead
	 */
	public boolean isDead() {
		return !alive;
	}

	/**
	 * create a new defense missile when ship shoots and adjust score
	 */
	public void shoot() {
		if (alive) {
			new DefenseMissile(new Location(ship.getX() + ship.getWidth() / 2, ship.getY()), playField, invaders,
					canvas);
			ship.sendToFront();
			scoreDisplay.subtract();
		}
	}

	/**
	 * retrieve the invaders object so it can be passed to defense missile
	 * 
	 * @param invaders
	 *            invaders object that holds array of aliens
	 */
	public void setTarget(Invaders invaders) {
		this.invaders = invaders;
	}

	/**
	 * run method for active object space ship
	 */
	public void run() {

		while (alive) {

			// move the ship based on which arrow key was pressed if the ship has room to
			// move
			if (direction == LEFT && ship.getX() > playField.getX()) {
				ship.move(-DISTANCE, 0);
			} else if (direction == RIGHT
					&& (ship.getX() + ship.getWidth()) < (playField.getX() + playField.getWidth())) {
				ship.move(DISTANCE, 0);
			}
			pause(PAUSE_TIME);
		}
	}

}

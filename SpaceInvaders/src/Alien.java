import objectdraw.*;
import java.awt.*;

/**
 * An Alien is one individual invader. Each Alien moves independently, launch
 * missiles, and removes it self from the canvas when shot.
 */
public class Alien {

	// height and width of each alien
	private static final int ALIEN_SIZE = 32;

	// visible image that displays alien
	private VisibleImage alien;

	// boolean tracks whether alien is alive or not
	private boolean isDead = false;

	// instance variables to hold parameters
	private DrawingCanvas canvas;
	private FilledRect playField;
	private SpaceShip ship;

	/**
	 * constructor for the aliens aliens populate invaders array, can shoot missiles
	 * and move back and forth in the playing field
	 * 
	 * @param alien
	 *            image that displays pic of alien
	 * @param alienX
	 *            x coordinate of alien
	 * @param alienY
	 *            y coordinate of alien
	 * @param playField
	 *            playing field containing ship, aliens, etc
	 * @param ship
	 *            ship object that can be shot by aliens
	 * @param canvas
	 *            drawing canvas
	 */
	public Alien(Image alien, double alienX, double alienY, FilledRect playField, SpaceShip ship,
			DrawingCanvas canvas) {
		this.canvas = canvas;
		this.playField = playField;
		this.ship = ship;

		// create image of alien and set its size
		this.alien = new VisibleImage(alien, alienX, alienY, canvas);
		this.alien.setSize(ALIEN_SIZE, ALIEN_SIZE);
	}

	/**
	 * move the alien
	 * 
	 * @param dx
	 *            amount to move in x direction
	 * @param dy
	 *            amount to move in y direction
	 */
	public void move(double dx, double dy) {
		alien.move(dx, dy);
	}

	/**
	 * create a new missile
	 */
	public void shoot() {
		new Missile(playField, alien.getX(), alien.getY(), ship, canvas);
	}

	/**
	 * kill the alien
	 */
	public void kill() {

		// just move it to back instead of removing from canvas to avoid issues with
		// trying to check its position, make it shoot, etc
		alien.sendToBack();
		isDead = true;
	}

	/**
	 * get location of top left corner of alien
	 * 
	 * @return top left corner of alien
	 */
	public Location getLocation() {
		return alien.getLocation();
	}

	/**
	 * check if missile hit alien
	 * 
	 * @param missile
	 *            missile to be checked
	 * @return true if missile hit alien
	 */
	public boolean overlaps(FilledRect missile) {
		return alien.overlaps(missile);
	}

	/**
	 * check if alien is alive
	 * 
	 * @return true if alien has not yet been hit by a missile
	 */
	public boolean isAlive() {
		return !isDead;
	}

}

import objectdraw.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is the main controller class for a SpaceInvaders game, where Aliens
 * attack by marching down the screen and a defender fires missiles at them to
 * prevent them from reaching the bottom of the screen.
 *
 * Recommended Window Size 700x700
 *
 * @author Ethan Horton, CS51J
 */
public class SpaceInvaders extends WindowController implements KeyListener {

	private static final Location FIELD_LOC = new Location(50, 50);// location for the black playing field
	private static final int FIELD_SIZE = 600;// length and width of playing field
	private static final int SHIP_SIZE = 32; // size of space ship image
	private static final long MISSILE_COOLDOWN = 1000; // time delay between shots from the space ship

	private static final int LEFT = 1;// integers correspond to direction that ship needs to move
	private static final int RIGHT = 2;
	private static final int STOP = 0;

	private Image shipImg; // image of the space ship
	private Image invaderImg; // image of the alien
	private FilledRect playField; // playing playField
	private SpaceShip ship; // space ship
	private Invaders aliens;// invaders object responsible for moving and holding aliens
	private ScoreKeeper scoreDisplay;// score keeper object
	private JLabel scoreLabel; // displays score at bottom of screen
	private FilledRect background;// rectangle that hides aliens once they die
	private long shootTime; // time when last missile was fired

	/**
	 * create the arena, invaders, and ship
	 */
	public void begin() {

		// get images for space ship and alien
		shipImg = getImage("rocket.gif");
		invaderImg = getImage("invader1.gif");

		// background hides the aliens once they are dead
		background = new FilledRect(0, 0, canvas.getWidth(), canvas.getHeight(), canvas);
		background.setColor(Color.WHITE);

		// create the playField
		playField = new FilledRect(FIELD_LOC, FIELD_SIZE, FIELD_SIZE, canvas);
		playField.setColor(Color.BLACK);

		// set up score counter
		scoreLabel = new JLabel("Score: 0", JLabel.CENTER);
		add(scoreLabel, BorderLayout.SOUTH);
		scoreDisplay = new ScoreKeeper(scoreLabel);

		// create ship at bottom center of playing playField
		ship = new SpaceShip(shipImg,
				new Location((playField.getWidth() / 2) - (SHIP_SIZE / 2),
						playField.getY() + playField.getHeight() - SHIP_SIZE),
				playField, SHIP_SIZE, scoreDisplay, canvas);

		// create the invader aliens
		aliens = new Invaders(invaderImg, playField, ship, scoreDisplay, canvas);

		// pass the array of aliens to the ship
		ship.setTarget(aliens);

		// get ready to listen to user's input
		requestFocus();
		addKeyListener(this);
		setFocusable(true);
		canvas.addKeyListener(this);
	}

	/**
	 * (mandatory) KeyListener event handler for a key having been pressed and
	 * released.
	 * 
	 * @param e
	 *            event (key that was typed)
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * (mandatory) KeyListener event handler for a key having been released.
	 * 
	 * @param e
	 *            event (key that was released)
	 */
	public void keyReleased(KeyEvent e) {
		ship.setDirection(STOP);
	}

	/**
	 * (mandatory) KeyListener event handler for a key having been pressed.
	 * <P>
	 * Handle arrow keys by telling the snake to move in the indicated direction.
	 * 
	 * @param e
	 *            event (key that was pressed)
	 */
	public void keyPressed(KeyEvent e) {

		// user must wait a second before they can shoot again
		if (e.getKeyCode() == KeyEvent.VK_SPACE && (System.currentTimeMillis() - shootTime) > MISSILE_COOLDOWN
				&& !aliens.gameOver()) {
			ship.shoot();
			shootTime = System.currentTimeMillis();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			ship.setDirection(LEFT);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			ship.setDirection(RIGHT);
		}
	}
}

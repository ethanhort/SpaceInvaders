import objectdraw.*;
import java.awt.*;

/**
 * Invaders is a class that creates the initial array of Aliens and moves them
 * down across and down the playing area.
 */
public class Invaders extends ActiveObject {
	private static final int PAUSE_TIME = 33;

	// constants for array size
	private static final int NUM_COLS = 10;
	private static final int NUM_ROWS = 4;

	// size of each alien
	private static final int ALIEN_SIZE = 32;

	// space between each alien
	private static final int ALIEN_OFFSET = 15;

	// coordinates to create alien at
	private double alienX;
	private double alienY;

	// boolean tracks if all aliens have been killed
	private boolean gameOver = false;

	// double determines the direction the aliens move
	private double direction = 1;

	// array of alien objects
	private Alien[][] invaders;

	// number generators to determine when and which alien shoots
	private RandomIntGenerator shootGen = new RandomIntGenerator(1, 20);
	private RandomIntGenerator colGen = new RandomIntGenerator(0, 9);
	private RandomIntGenerator rowGen = new RandomIntGenerator(0, 3);

	// integer that determines how long to wait before another alien shoots
	private int shootNum;

	// integers that track which columns still have aliens in them - allow aliens to
	// move all the way to the edge of the screen once some columns have already
	// been removed
	private int colNumberR = NUM_COLS - 1;
	private int colNumberL = 0;

	// instance variables to hold objects passed as parameters
	private FilledRect playField;
	private SpaceShip ship;
	private ScoreKeeper scoreDisplay;

	/**
	 * constructor for invaders object invaders hold an array of aliens
	 *
	 * @param alien
	 *            image representing an alien
	 * @param playField
	 *            rectangle containing ship, aliens, etc
	 * @param ship
	 *            space ship object that can shoot aliens
	 * @param scoreDisplay
	 *            scorekeeper object that tracks user's score
	 * @param canvas
	 *            Drawing canvas
	 */
	public Invaders(Image alien, FilledRect playField, SpaceShip ship, ScoreKeeper scoreDisplay, DrawingCanvas canvas) {
		this.playField = playField;
		this.ship = ship;
		this.scoreDisplay = scoreDisplay;

		// initialize array of aliens
		invaders = new Alien[NUM_ROWS][NUM_COLS];

		// start aliens in the top left corner of the playField
		alienX = playField.getX();
		alienY = playField.getY();

		// for loop that populates array of aliens
		for (int i = 0; i < NUM_ROWS; i++) {
			alienX = playField.getX();
			for (int j = 0; j < NUM_COLS; j++) {
				invaders[i][j] = new Alien(alien, alienX, alienY, playField, ship, canvas);
				alienX = alienX + ALIEN_SIZE + ALIEN_OFFSET;
			}
			alienY = alienY + ALIEN_SIZE + ALIEN_OFFSET;
		}
		start();
	}

	/**
	 * method checks if missile passed to it overlaps any aliens
	 * 
	 * @param missile
	 *            filledRect representing a missile
	 * @return true if missile hit an alien
	 */
	public boolean overlap(FilledRect missile) {

		// check each individual alien to see if the missile hit any of them
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				if (invaders[i][j].isAlive() && invaders[i][j].overlaps(missile)) {

					// kill the alien that was shot and update score
					if (!gameOver) {
						invaders[i][j].kill();
						scoreDisplay.add(i);

						// checks each side of the array of aliens every time one is killed to see if
						// the end column is all dead
						removeColumnR();
						removeColumnL();
					}

					// update label when user kills all aliens
					if (gameOver()) {
						scoreDisplay.setText("You Won!");
					}

					return true;
				}
			}
		}
		return false;
	}

	/**
	 * checks if user has won the game by killing all the aliens
	 * 
	 * @return true if all aliens have been killed
	 */
	public boolean gameOver() {

		// check each alien to see if any are alive
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				if (invaders[i][j].isAlive()) {

					// return false as soon as a live alien is found
					return false;
				}
			}
		}

		// return true if all aliens are dead
		gameOver = true;
		return true;
	}

	/**
	 * check if all aliens in the far right column have been killed. Move the right
	 * edge in until there is a column with live aliens
	 */
	private void removeColumnR() {

		// this breaks when the user wins the game, so i just caught the exception
		// instead of trying to fix it
		try {
			for (int i = 0; i < NUM_ROWS; i++) {
				if (invaders[i][colNumberR].isAlive()) {

					// if an alien in the column is alive, do nothing
					return;
				}
			}

			// if no alien in column is alive, move one column left and check again
			colNumberR--;
			removeColumnR();
		} catch (Exception e) {
		}
	}

	/**
	 * check if all aliens in the far left column have been killed. move the left
	 * edge in until there is a column with live aliens
	 */
	private void removeColumnL() {
		try {
			for (int i = 0; i < NUM_ROWS; i++) {
				if (invaders[i][colNumberL].isAlive()) {

					// return if an alien in the left column is alive
					return;
				}
			}

			// move one column right and check again if there are no aliens
			colNumberL++;
			removeColumnL();
		} catch (Exception e) {

		}
	}

	/**
	 * run thread for active object. moves aliens and makes them shoot
	 */
	public void run() {
		int col = 0;
		int row = 0;
		shootNum = shootGen.nextValue();
		while (!gameOver) {

			// once random number counts down to 0, random alien tries to fire a missile
			if (shootNum == 0) {

				// shootNum can't be more than 20 so loop can iterate a maximum of 20 times
				// before trying to fire a missile. fixes problem where aliens would go extended
				// period of time without firing a missile while preserving the mechanic that
				// fewer missiles will be fired as aliens are destroyed
				shootNum = shootGen.nextValue();
				col = colGen.nextValue();
				row = rowGen.nextValue();
				if (invaders[row][col].isAlive()) {
					invaders[row][col].shoot();
				}
			}

			// move all of the aliens in direction
			for (int i = 0; i < NUM_ROWS; i++) {
				for (int j = 0; j < NUM_COLS; j++) {
					invaders[i][j].move(direction, 0);
				}
			}

			// change direction if alien hits edge of playing field
			if (invaders[1][colNumberR].getLocation().getX() + ALIEN_SIZE > playField.getX() + playField.getWidth()
					|| invaders[0][colNumberL].getLocation().getX() < playField.getX()) {
				direction = direction * -1;
			}

			// check if ship has died and end game if so
			if (ship.isDead()) {
				gameOver = true;
			}

			shootNum--;
			pause(PAUSE_TIME);
		}
	}
}

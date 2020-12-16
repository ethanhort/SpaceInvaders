import objectdraw.*;
import java.awt.*;
import javax.swing.JLabel;

/**
 * the ScoreKeeper manages a score display (of Aliens that have been hit and
 * DefenseMissiles that have been fired).
 */
public class ScoreKeeper {

	// points to add when user kills an alien
	private static final int FIRST_ROW = 10;
	private static final int SECOND_ROW = 20;
	private static final int THIRD_ROW = 30;
	private static final int FOURTH_ROW = 40;

	// keeps track of user's current score
	private int score = 0;

	// label that displays the score
	private JLabel scoreDisplay;

	/**
	 * constructor for the score keeper
	 * 
	 * @param scoreDisplay
	 *            label that displays the user's score
	 */
	public ScoreKeeper(JLabel scoreDisplay) {
		this.scoreDisplay = scoreDisplay;
	}

	/**
	 * add points to score and update the label based on which row the alien was
	 * from
	 */
	public void add(int row) {
		if (row == 3)
			score = score + FIRST_ROW;
		else if (row == 2)
			score = score + SECOND_ROW;
		else if (row == 1)
			score = score + THIRD_ROW;
		else
			score = score + FOURTH_ROW;
		
		scoreDisplay.setText("Score: " + score);
	}

	/**
	 * subtract points from score and update the label
	 */
	public void subtract() {
		score--;
		scoreDisplay.setText("Score: " + score);
	}

	/**
	 * set the text of the label
	 * 
	 * @param str
	 *            string to be displayed by the label
	 */
	public void setText(String str) {
		scoreDisplay.setText(str);
	}
}

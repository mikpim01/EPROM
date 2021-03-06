import java.util.Random;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.LayerManager;

public class BallSprite implements Runnable {

	public BallSprite(MyGameCanvas parent) {

		this.parent = parent;
		this.layerManager = parent.getManager();

	}

	public void start() {

		// first load the image
		try {
		  ballImg = Image.createImage("/ball.png");
		} catch(Exception e) {
		    System.err.println(e); return;
		}

		// next start the thread that will display balls
		// ar random locations
		forever = new Thread(this);
		forever.start();
	}

	public void run() {

		try {
		  while(true) {

				// create a random ball
				createRandomBall();

				// wait before creating another one
			  Thread.currentThread().sleep(500);
			}
		} catch(Exception e) { System.err.println(e); }
	}

	// creates and displays a ball at a random location
	private void createRandomBall() {

		// if maximum balls are shown don't do anything
		if(currentBalls == MAX_BALLS) return;

		// create a new ball sprite
		ballSprite = new Sprite(ballImg, 10, 10);

		// generate the random places 
		int randomBallX = parent.getRandom().nextInt(parent.PLAYFIELD_WIDTH);
		int randomBallY =  (parent.BASE -   parent.getRandom().nextInt(parent.MAX_HEIGHT + 1) -
		    ballSprite.getHeight());

		// make sure that these places are within bounds
		if(randomBallX < parent.PLAYFIELD_ORIGIN_X) 
		    randomBallX = parent.CENTER_X;
		if(randomBallY < (parent.BASE - parent.MAX_HEIGHT))
		  randomBallY = parent.CENTER_Y;

		// set this newly created ball sprite in its random position
		ballSprite.setPosition(randomBallX, randomBallY);

		// add it to the manager at index 0
		layerManager.insert(ballSprite, 0);

		// increment the total number of balls appearingx
		currentBalls++;

	}

	public void checkForCollision() {

		// if there are no balls shown 
		if(layerManager.getSize() == 2) return;

		// iterate through the layers, remember don't worry about
		// the last two because they are the couple and background
		for(int i = 0; i < (layerManager.getSize() - 2); i++) {

			// if collision occurs
			if(parent.getPokemonSprite().collidesWith(
				(Sprite)layerManager.getLayerAt(i), true)) {

			  // remove the  ball
			  layerManager.remove(layerManager.getLayerAt(i));

			  // reduce the number of balls on camvas
			  currentBalls--;

			  // increment the number of balls hit
			  ballsHit++;

			}
		}
	}

	// the no of balls hit

	public int getBallsHit() {
		return ballsHit;
	}

	// ball sprite
	private Sprite ballSprite;

	// ball image
	private Image ballImg;

	// the no of current balls in display

	private int currentBalls;

	// the parent canvas

	private MyGameCanvas parent;

	// the parent canvas's layer manager
	private LayerManager layerManager;


	private Thread forever;


	private int ballsHit;

	// the maximum no of balls to create
	private static final int MAX_BALLS = 15;

}


import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import java.util.*;
import java.applet.Applet;
import java.applet.AudioClip;

public class GameImpl extends Pane implements Game {
	/**
	 * Defines different states of the game.
	 */
	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}

	// Constants
	/**
	 * The width of the game board.
	 */
	public static final int WIDTH = 400;
	/**
	 * The height of the game board.
	 */
	public static final int HEIGHT = 600;
	
	/**
	 * Number of animals or bricks in the game
	 */
	public static final int NUMANIMALS = 16;
	
	/**
	 * Distance between each animal
	 */
	public static final int MARGIN = 42;

	// Instance variables
	private Ball ball;
	private Paddle paddle;
	private int strikes; //keeps track of how many times the ball has touched the bottom wall
	private int lastHit; //Makes sure the ball does not ricochet inside of an object or wall
	private AudioClip shatter, boing, chaching; //Audio Files
	private List<Animal> animals;

	/**
	 * Constructs a new GameImpl.
	 */
	public GameImpl () {
		setStyle("-fx-background-color: white;");
		
		//Loading Audio
		shatter =  Applet.newAudioClip(getClass().getClassLoader().getResource("shatter.wav"));
		chaching =  Applet.newAudioClip(getClass().getClassLoader().getResource("chaching.wav"));
		boing =  Applet.newAudioClip(getClass().getClassLoader().getResource("boing.wav"));

		restartGame(GameState.NEW);
	}

	public String getName () {
		return "Zutopia";
	}

	public Pane getPane () {
		return this;
	}

	private void restartGame (GameState state) {
		getChildren().clear();  // remove all components from the game

		// Create and add ball
		ball = new Ball();
		getChildren().add(ball.getCircle());  // Add the ball to the game board

		// Create and add animals
		animals = new ArrayList<Animal>();
		double crntx, crnty;
		crntx = 0.0;
		crnty = 0.0;
		for(int i = 0; i < NUMANIMALS; i++) {
			Animal temp = new Animal((int)(Math.random() * 3)); //create a new animal
			crntx += MARGIN + temp.getWidth()/2; //set distace from last animal
			if(crnty == 0.0) {crnty +=  MARGIN;} //sets margin from edge
			if(crntx >= WIDTH - MARGIN) {crnty += MARGIN * 1.5; crntx =  MARGIN + temp.getWidth()/2;} //checks to see if there needs to be line
			temp.setPos(crntx, crnty); //set position of new animal
			crntx += temp.getWidth()/2; //move away from the animal
			getChildren().add(temp.getLabel()); //add animal to scene
			animals.add(temp); //add animal to the list
		}
			

		// Create and add paddle
		paddle = new Paddle();
		getChildren().add(paddle.getRectangle());  // Add the paddle to the game board

		// Add start message
		final String message;
		if (state == GameState.LOST) {
			message = "Game Over\n";
		} else if (state == GameState.WON) {
			message = "You won!\n";
		}else if (state == GameState.NEW) {
			message = "Zootopia: Now With Volume!\n";
		} else {
			message = "";
		}
		final Label startLabel = new Label(message + "Click mouse to start");
		startLabel.setLayoutX(WIDTH / 2 - 75);
		startLabel.setLayoutY(HEIGHT / 2 + 100);
		getChildren().add(startLabel);
		
		strikes = 0;
		lastHit = -1;

		// Add event handler to start the game
		setOnMouseClicked(new EventHandler<MouseEvent> () {
			@Override
			public void handle (MouseEvent e) {
				GameImpl.this.setOnMouseClicked(null);

				// As soon as the mouse is clicked, remove the startLabel from the game board
				getChildren().remove(startLabel);
				run();
			}
		});

		// Event handler to steer paddle
		setOnMouseMoved(new EventHandler<MouseEvent> () {
			@Override
			public void handle (MouseEvent e) {
				paddle.moveTo(e.getX(), e.getY());
			}
	    });
	}

	/**
	 * Begins the game-play by creating and starting an AnimationTimer.
	 */
	public void run () {
		// Instantiate and start an AnimationTimer to update the component of the game.
		new AnimationTimer () {
			private long lastNanoTime = -1;
			public void handle (long currentNanoTime) {
				if (lastNanoTime >= 0) {  // Necessary for first clock-tick.
					GameState state;
					if ((state = runOneTimestep(currentNanoTime - lastNanoTime)) != GameState.ACTIVE) {
						// Once the game is no longer ACTIVE, stop the AnimationTimer.
						stop();
						// Restart the game, with a message that depends on whether
						// the user won or lost the game.
						restartGame(state);
					}
				}
				// Keep track of how much time actually transpired since the last clock-tick.
				lastNanoTime = currentNanoTime;
			}
		}.start();
	}

	/**
	 * Updates the state of the game at each timestep. In particular, this method
	 * moves the ball, checks if the ball collided with any of the animals, walls, or the paddle
	 * @param deltaNanoTime how much time (in nanoseconds) has transpired since the last update
	 * @return the current game state
	 */
	public GameState runOneTimestep (long deltaNanoTime) {
		checkBallPosition();
		checkCollision();
		ball.updatePosition(deltaNanoTime);
		return checkEndState();
	}
	
	/**
	 * Updates the postion of the paddle to the postion of the event.
	 * @param event a MouseEvent (setOnMouseMoved)
	 */
	public void updatePaddlePosition(MouseEvent event) {
		paddle.moveTo(event.getX(), event.getY());
	}
	
	/**
	 * Checks to see is the ball is leaving the screen, if so change the direction.
	 */
	private void checkBallPosition() {
		final double x = ball.getX();
		final double y = ball.getY();
		//Right Wall
		if(x + ball.getRad() >= WIDTH && lastHit != 0) {
			ball.bounceX();
			lastHit = 0;
		//Left Wall
		}else if(x - ball.getRad() <= 0 && lastHit != 1) {
			ball.bounceX();
			lastHit = 1;
		//Top Wall
		}else if(y - ball.getRad() <= 0 && lastHit != 2){
			ball.bounceY();
			lastHit = 2;
		//Bottom Wall
		}else if(y + ball.getRad() >= HEIGHT && lastHit != 3) {
			ball.bounceY();
			strikes ++;
			lastHit = 3;
		}
	}
	
	/**
	 * Checks to see if the ball is colliding with the paddle or any of the animals.
	 */
	private void checkCollision() {
		//Borders of the Ball
		final double ballRight = ball.getX() + ball.getRad();
		final double ballTop = ball.getY() - ball.getRad();
		final double ballLeft = ball.getX() - ball.getRad();
		final double ballBot = ball.getY() + ball.getRad();
		
			//Borders of the Paddle
			final double paddleRight = paddle.getX() + paddle.getWidth()/2;
			final double paddleLeft = paddle.getX() - paddle.getWidth()/2;
			final double paddleTop = paddle.getY() - paddle.getHeight()/2;
			final double paddleBot = paddle.getY() + paddle.getHeight()/2;
			//Check to see if the ball is on/in the paddle
			if(ballLeft <= paddleRight && ballRight >= paddleLeft) {
				if(ballBot >= paddleTop && ballTop <= paddleBot) {	
					//decide how to bounce off the paddle
					if(ballTop <= paddleTop) {
						ball.bounceDown();
						lastHit = 4;
					}else if(ballBot >= paddleBot) {
						ball.bounceUp();
						lastHit = 5;
					}
					boing.play();
					ball.bounceY();
					
				}
			}
		for(Animal brick : animals) {
			//Borders of the brick
			final double brickRight = brick.getX() + brick.getWidth();
			final double brickLeft = brick.getX() - brick.getWidth();
			final double brickTop = brick.getY() - brick.getHeight();
			final double brickBot = brick.getY() + brick.getHeight();
			//Check to see if the ball is on/in the brick
			if(ballLeft <= brickRight && ballRight >= brickLeft) {
				if(ballBot >= brickTop && ballTop <= brickBot) {
					//decide how to bounce off the brick
					if(ballRight >= brickRight || ballLeft < brickLeft) {
							ball.bounceX();
					}else {
						ball.bounceY();
					}
					ball.increaseSpeed();
					brick.playSound();
					//run the removal when the method returns to keep everthing in order
					Platform.runLater(() -> getChildren().remove(1 + animals.indexOf(brick)));
					Platform.runLater(() -> animals.remove(animals.indexOf(brick)));
					lastHit = -1;
				}
			}
		}
	}
	
	/**
	 * Updates the game state based on how many animals are left as well as the number of times
	 * the ball has bounced off the bottom of the screen.
	 * @return the updated game state
	 */
	private GameState checkEndState() {
		if(strikes >= 5) {
		    shatter.play();
			return GameState.LOST;
		}else if (animals.isEmpty()) {
		    chaching.play();
			return GameState.WON;
			
		}
		return GameState.ACTIVE;
	}
}

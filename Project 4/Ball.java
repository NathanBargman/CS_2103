import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Class that implements a ball with a position and velocity.
 */
public class Ball {
	// Constants
	/**
	 * The radius of the ball.
	 */
	public static final int BALL_RADIUS = 8;
	/**
	 * The initial velocity of the ball in the x direction.
	 */
	public static final double INITIAL_VX = 1e-7;
	/**
	 * The initial velocity of the ball in the y direction.
	 */
	public static final double INITIAL_VY = 1e-7;
	/**
	 * The rate the speed of the ball will increase when increaseSpeed() is called.
	 */
	public static final double SPEEDCONST = 0.15;


	// Instance variables
	// (x,y) is the position of the center of the ball.
	private double x, y;
	private double vx, vy;
	private double speed;
	private Circle circle;

	/**
	 * @return the Circle object that represents the ball on the game board.
	 */
	public Circle getCircle () {
		return circle;
	}

	/**
	 * Constructs a new Ball object at the centroid of the game board
	 * with a default velocity that points down and right.
	 */
	public Ball () {
		x = GameImpl.WIDTH/2;
		y = GameImpl.HEIGHT/2;
		vx = INITIAL_VX;
		vy = INITIAL_VY;
		speed = 1;

		circle = new Circle(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
		circle.setLayoutX(x - BALL_RADIUS);
		circle.setLayoutY(y - BALL_RADIUS);
		circle.setFill(Color.BLACK);
	}

	/**
	 * Updates the position of the ball, given its current position and velocity,
	 * based on the specified elapsed time since the last update.
	 * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
	 */
	public void updatePosition (long deltaNanoTime) {
		double dx = vx * deltaNanoTime * speed;
		double dy = vy * deltaNanoTime * speed;
		x += dx;
		y += dy;

		circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
		circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));
	}
	
	/**
	 * @return the X position of the circle.
	 */
	public double getX () {
		return x;
	}
	
	/**
	 * @return the Y position of the circle.
	 */
	public double getY () {
		return y;
	}
	
	/**
	 * Reverses the X velocity of the circle
	 */
	public void bounceX() {
		vx = -vx;
	}
	
	/**
	 * Reverses the Y velocity of the circle
	 */
	public void bounceY() {
		vy = -vy;
	}
	/**
	 * Transforms the Y velocity of the circle to a negative.
	 */
	public void bounceUp() {
		vy = -Math.abs(vy);
	}
	/**
	 * Transforms the Y velocity of the circle to a positive.
	 */
	public void bounceDown() {
		vy = Math.abs(vy);
	}
	/**
	 * @return the radius of the circle.
	 */
	public double getRad() {
		return BALL_RADIUS;
	}
	
	/**
	 * Increases the speed of the ball by the SPEEDCONST.
	 */
	public void increaseSpeed() {
		speed += SPEEDCONST;
	}
}

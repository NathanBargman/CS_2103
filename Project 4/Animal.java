import javafx.scene.control.Label;
import javafx.scene.image.*;
import java.applet.Applet;
import java.applet.AudioClip;

public class Animal {
	
	//Instance Variables
	// (x,y) is the position of the center of the Animal.
	private Label animalImage; 
	private Image image;
	private AudioClip audio;
	double Width, Height;
	double x, y;
	
	/**
	 * Constructs a new animal with an image.
	 * @param type an int equal to (0 for Duck || 1 for Goat || 2 for Horse)
	 */
	public Animal(int type){
		image = new Image(getFileName(type));
		audio = Applet.newAudioClip(getClass().getClassLoader().getResource(getAudioName(type)));
		Width = image.getWidth();
		Height = image.getHeight();
	}
	
	/**
	 * Sets the position of the image.
	 * @param xPos the center X position of the image.
	 * @param yPos the center Y position of the image.
	 */
	public void setPos(double xPos, double yPos) {
		x = xPos - Width/2;
		y = yPos - Height/2;
		animalImage = new Label("", new ImageView(image));
		animalImage.setLayoutX(x);
		animalImage.setLayoutY(y);
	}
	
	/**
	 * Method for finding image files.
	 * @param type an int equal to (0 for Duck || 1 for Goat || 2 for Horse)
	 * @returns a filename
	 */
	private String getFileName(int type) {
		if(type == 0) {
			return "/duck.jpg";
		}else if(type == 1) {
			return "/goat.jpg";
		}else {
			return "/horse.jpg";
		}
	}
	/**
	 * Method for finding audio files.
	 * @param type an int equal to (0 for Duck || 1 for Goat || 2 for Horse)
	 * @returns a filename
	 */
	private String getAudioName(int type) {
		if(type == 0) {
			return "quack.wav";
		}else if(type == 1) {
			return "bleat.wav";
		}else {
			return "whinny.wav";
		}
	}
	
	/**
	 * @returns a Label
	 */
	public Label getLabel() {
		return animalImage;
	}
	
	/**
	 * @returns the width of the animals image.
	 */
	public double getWidth() {
		return Width;
	}
	
	/**
	 * @returns the height of the animals image.
	 */
	public double getHeight() {
		return Height;
	}
	
	/**
	 * @returns the X position of the Label.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * @returns the Y position of the Label.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 *Plays the sound corresponding to the type of image the animal is.
	 */
	public void playSound() {
	    audio.play();
	}
}

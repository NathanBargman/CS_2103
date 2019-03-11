import java.util.ArrayList;

public class Moment extends Object {
	ArrayList<LivingObject> participants;
	ArrayList<Float> smileValues;
	public Moment(String name, Image image, ArrayList<LivingObject> participants, ArrayList<Float> smileValues) {
		super(name, image);
		this.participants = participants;
		this.smileValues = smileValues;
	}
}

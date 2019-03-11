import java.util.ArrayList;

public class Person extends LivingObject {
	ArrayList<Possession> _possessions;
	ArrayList<Pet> _pets;
	public Person(String name, Image image) {
		super(name, image);
	}
	void setPossessions(ArrayList<Possession> possessions) {
		this._possessions = possessions;
	}
	void setPets(ArrayList<Pet> pets) {
		this._pets = pets;
	}
}

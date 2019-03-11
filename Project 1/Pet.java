
public class Pet extends LivingObject {
	Person _owner;
	public Pet(String name, Image image) {
		super(name, image);
	}
	void setOwner(Person owner) {
		this._owner = owner;
	}
}

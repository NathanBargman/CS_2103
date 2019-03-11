
public class Possession extends Object{
	float price;
	Person owner;
	public Possession(String name, Image image, float price) {
		super(name, image);
		this.price = price;
	}
	void setOwner(Person owner) {
		this.owner = owner;
	}
	void setPrice(float price) {
		this.price = price;
	}
	
}

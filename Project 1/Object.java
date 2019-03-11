import java.util.ArrayList;

public class Object {
	String _name;
	Image _image;
	
	public Object (String name, Image image) {
		this._name = name;
		this._image = image;
	}
	
	public static boolean isClique(ArrayList<LivingObject> set) {	
	if(set == null) {
		return true;
	}
	for(int i = 0; i < set.size() - 1; i++) {
		for(int x = i+1; x < set.size(); x++) {
			if(!set.get(i).isFriend(set.get(x))) {
				return false;
			}
		}
	}
	return true;
	}
	
	public boolean equals(Object o) {
		if(_name.equals(((LivingObject) o)._name)){
			return true;
		}
		return false;
	}
}

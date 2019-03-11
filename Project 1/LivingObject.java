import java.util.ArrayList;

public class LivingObject extends Object {
	ArrayList<LivingObject> _friends;
	ArrayList<Moment> _moments;
	public LivingObject(String name, Image image) {
		super(name, image);
	}
	
	void setFriends (ArrayList<LivingObject> friends) {
		this._friends = friends;
	}
	void setMoments (ArrayList<Moment> moments) {
		this._moments = moments;
	}
	ArrayList<LivingObject> getFriends(){
		return this._friends;
	}
	void addFriend (LivingObject newFriend) {
		this._friends.add(newFriend);
	}
	boolean isFriend (LivingObject possibleFriend) {
		for(int i = 0; i < this._friends.size(); i++) {
			if(this._friends.get(i).equals(possibleFriend)) {
				return true;
			}
		}
		return false;
	}
	LivingObject getFriendWithWhomIAmHappiest() {
		float max = 0.0f;
		int index = 0;
		for(int i = 0; i < this._friends.size(); i++) {
			float temp = 0.0f;
			float numOf = 0.0f;
			for(int x = 0; x < this._moments.size(); x++) {
				ArrayList<LivingObject> beings = this._moments.get(x).participants;
				for(int z = 0; z < beings.size(); z++) {
					if(beings.get(z).equals(this._friends.get(i))){
						temp += this._moments.get(x).smileValues.get(z);
						numOf += 1.0f;
					}
				}
			}
			temp = temp/numOf;
			if(temp > max) {
				max = temp;
				index = i;			
			}
		}
		return this._friends.get(index);
	}
	
	Moment getOverallHappiestMoment() {
		float max = 0.0f;
		int index = 0;
		for(int i = 0; i < this._moments.size(); i++) {
			float temp = 0.0f;
			float numOfSmiles = this._moments.get(i).smileValues.size();
			for(int x = 0; x < this._moments.get(i).smileValues.size(); x++) {
				temp += this._moments.get(i).smileValues.get(x);
			}
			temp = temp/numOfSmiles;
			if(temp >= max) {
				max = temp;
				index = i;
			}
		}
		return this._moments.get(index);
	}
	//Returns the maximum clique a LivingObject can have
	ArrayList<LivingObject> findMaximumCliqueOfFriends(){
		int maxNum = 0; //Initialize Counter
		ArrayList<LivingObject> maxList = new ArrayList<LivingObject>(); //Initialize Object to be returned
		for (int i = 0; i < this._friends.size(); i++) { //Loop Through this's friend list to make sure all friends are considered
			int tempNum = 1; //Initialize Accumulator
			ArrayList<LivingObject> tempList = new ArrayList<LivingObject>(); //Initialize Setter
			tempList.add(this._friends.get(i)); //Put first Friend in array to test
			for(int x = 0; x < this._friends.size(); x++) { //Loop through friends again trying to insert all other friends into the clique
				boolean testAgainstList = false; //Initialize 
				for(int z = 0; z < tempList.size(); z++) { //Loop through the current clique to make sure the friend being tested is friends with everyone
					if(this._friends.get(x).isFriend(tempList.get(z))) {
						testAgainstList = true;
					}else {
						testAgainstList = false;
						z = tempList.size(); //If the the friend being tested fails 1 check exit the test
					}
				}
				if(testAgainstList) { //If every one is friends add the most recent friend into the clique
					tempList.add(this._friends.get(x));
					tempNum += 1;
				}
			}
			if(tempNum > maxNum) { //If the new clique is greater than our max make that our new max
				maxList = tempList;
				maxNum = tempNum;
			}
		}
		return maxList; //Return the biggest list
	}
	
}

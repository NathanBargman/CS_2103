
public class FriendRequest {
	LivingObject entity1;
	LivingObject entity2;
	boolean enitity1Approve;
	boolean enitity2Approve;
	public FriendRequest (LivingObject entity1, LivingObject entity2) {
		this.entity1 = entity1;
		this.entity2 = entity2;
	}
	void approve(LivingObject entity) {
		if(entity.equals(entity1)) {
			enitity1Approve = true;
		}else if(entity.equals(entity2)) {
			enitity2Approve = true;
		}else {
			throw new IllegalArgumentException("That Entity is not Part of the Friend Request");
		}
		if(enitity1Approve && enitity2Approve) {
			entity1.addFriend(entity2);
			entity2.addFriend(entity1);
		}
	}
	
}
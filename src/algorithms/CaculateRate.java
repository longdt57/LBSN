package algorithms;

import java.util.ArrayList;

import object.MyPlace;
import object.MyUser;

public class CaculateRate {
	
	public static CaculateRate CRATE = new CaculateRate();

	public void caculateRate(ArrayList<MyUser> users){
		for(int i=0; i<users.size(); i++){
			MyUser user = users.get(i);
			int average_checkin = 0;
			for(int j=0; j<user.getPlaceList().size(); j++){
				MyPlace place = user.getPlaceList().get(j);
				average_checkin += place.getNumCheck();
			}
			if(user.getPlaceList().size()!=0)
				average_checkin /= user.getPlaceList().size();
			
			for(int j=0; j<user.getPlaceList().size(); j++){
				MyPlace place = user.getPlaceList().get(j);
				double rate = place.getNumCheck()*5/(float)average_checkin<=5 ? place.getNumCheck()*5/(float)average_checkin:5;
				place.setRate(rate);
			}
		}
	}
	
	public float getSimilary(MyUser userA, MyUser userB){
		double tu=0, mauA=0, mauB=0;
		for(int i=0;i<userA.getPlaceList().size(); i++){
			MyPlace placeA = userA.getPlaceList().get(i);
			for(int j=0; j<userB.getPlaceList().size(); j++){
				MyPlace placeB = userB.getPlaceList().get(j);
				if(placeA.getId().compareTo(placeB.getId())==0){
					tu 		+= placeA.getRate()*placeB.getRate();
					mauA 	+= 
					break;
				}
			}
		}
		
		return 0;
	}
}

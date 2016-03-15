package algorithms;

import java.util.ArrayList;

import object.MyPlace;
import object.MyUser;

public class Algorithms {
	
	public static Algorithms ALGORITHM 	= new Algorithms();
	public static final int MIN_CHECKIN = 500;

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
	
	public double getSimilary(MyUser userA, MyUser userB){
		double tu=0, mauA=0, mauB=0;
		for(int i=0;i<userA.getPlaceList().size(); i++){
			MyPlace placeA = userA.getPlaceList().get(i);
			for(int j=0; j<userB.getPlaceList().size(); j++){
				MyPlace placeB = userB.getPlaceList().get(j);
				if(placeA.getId().compareTo(placeB.getId())==0){
					tu 		+= placeA.getRate()*placeB.getRate();
					mauA 	+= placeA.getRate()*placeA.getRate();
					mauB	+= placeB.getRate()*placeB.getRate();
					break;
				}
			}
		}
		if(mauA!=0 && mauB!=0)
			return tu/Math.sqrt(mauA*mauB);
		
		return 0;
	}
	
	//giam kich thuoc dữ liệu, tim so place
	public void preexcuteData(ArrayList<MyUser> users, ArrayList<MyPlace> places){
		for(int i=0; i<users.size(); i++){
			if(users.get(i).getnumcheckin()<MIN_CHECKIN) users.remove(i--);
		}
		
		for(int i=0;i <users.size();i++){
			MyUser user = users.get(i);
			for(int j=0;j<user.getPlaceList().size(); j++){
				MyPlace place = user.getPlaceList().get(j);
				boolean isinplaces = false;
				for(int k=0; k<places.size(); k++){
					if(places.get(k).getId().compareTo(place.getId())==0){
						isinplaces = true;
						places.get(k).setNumcheck(places.get(k).getNumCheck()+place.getNumCheck());
						break;
					}
				}
				if(!isinplaces) places.add(place.clone());
			}
		}
	}
}

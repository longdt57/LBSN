package algorithms;

import java.util.ArrayList;

import object.MyPlace;
import object.MyUser;

public class Algorithms {
	
	public static Algorithms ALGORITHM 	= new Algorithms();
	
	//xu ly du lieu
	public static int MIN_CHECKIN 	= 0;
	public static int MIN_PLACE		= 0;
	public static int MIN_FRIEND 	= 0;

	public void setAverageData(ArrayList<MyUser> users){
		int scheckin=0, sfriend=0, splace =0;
		for(int i=0;i<users.size(); i++){
			MyUser user = users.get(i);
			scheckin 	+= user.getnumcheckin();
			sfriend  	+= user.getFriendlist().size();
			splace 		+= user.getPlaceList().size();
		}
		MIN_CHECKIN = scheckin/users.size();
		MIN_PLACE 	= splace/users.size();
		MIN_FRIEND	= sfriend/users.size();
	}
	
	//xem gia tri trug binh la rate 2.5
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
				double rate = place.getNumCheck()*2.5/(float)average_checkin<=5 ? place.getNumCheck()*5/(float)average_checkin:5;
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
			MyUser user = users.get(i);
			if(user.getnumcheckin()<MIN_CHECKIN || user.getPlaceList().size()<MIN_PLACE) //users.remove(i--);
				user=null;
		}
		int dem=0;
//		for(int i=0;i <users.size();i++){
//			MyUser user = users.get(i);
//			dem+=user.getPlaceList().size();
//			for(int j=0;j<user.getPlaceList().size(); j++){
//				MyPlace place = user.getPlaceList().get(j);
//				boolean isinplaces = false;
//				for(int k=0; k<places.size(); k++){
//					if(places.get(k).getId().compareTo(place.getId())==0){
//						isinplaces = true;
//						places.get(k).setNumcheck(places.get(k).getNumCheck()+place.getNumCheck());
//						break;
//					}
//				}
//				if(!isinplaces) places.add(place.clone());
//				//System.out.println(place.toString());
//			}
//		}
//		System.out.println("dem: "+dem);
	}
}

package algorithms;

import java.util.ArrayList;

import object.MyDatabase;
import object.MyLog;
import object.MyPlace;
import object.MyUser;
import utilities.MyData;
import utilities.MyDatabaseHelper;


public class Algorithms {
	
	public static Algorithms ALGORITHM 	= new Algorithms();
	
	//xu ly du lieu
	public static int CHECKINS_PER_PEOPLE 			= 0;
	public static int PLACES_PER_PEOPLE				= 0;
	public static int FRIEND_PER_PEOPLE 			= 0;
	public static int PEOPLES_PER_PLACE				= 0;
	public static int CHECKINS_PER_PLACE			= 0;

	public void setAverageData(MyData mdata, MyDatabase database){
		MyLog.log("Entered average");
		ArrayList<MyUser> users 	= mdata.getUsers();
		ArrayList<MyPlace> places 	= mdata.getPlaces();
		int scheckin=0, sfriend=0, splace =0;
		int pcheckin=0, ppeople =0;
		int totalpeople = 0;
		int totalplace = 0;
		for(int i=0;i<users.size(); i++){
			MyUser user = users.get(i);
			if(MyData.isvalue(user)){
				scheckin 	+= user.getnumcheckin();
				sfriend  	+= user.getFriendlist().size();
				splace 		+= user.getPlaceList().size();
				totalpeople++;
			}
			
		}
		for(int i=0; i<places.size(); i++){
			MyPlace place = places.get(i);
			if(MyData.isvalue(place)){
				ppeople 	+= place.getNumpeoplecheck();
				pcheckin	+= place.getNumCheck();
				totalplace++;
			}
		}
		CHECKINS_PER_PEOPLE = scheckin/totalpeople;
		PLACES_PER_PEOPLE 	= splace/totalpeople;
		FRIEND_PER_PEOPLE	= sfriend/totalpeople;
		
		PEOPLES_PER_PLACE	= ppeople/totalplace;
		CHECKINS_PER_PLACE	= pcheckin/totalplace;
		
		System.out.println("Checkins: "+scheckin);
		System.out.println("Total people: "+totalpeople);
		System.out.println("Checkins per people: "+CHECKINS_PER_PEOPLE);
		System.out.println("places per people: "+PLACES_PER_PEOPLE);
		System.out.println("Friends per people: "+FRIEND_PER_PEOPLE);
		System.out.println("total peoplecheck: "+ppeople);
		System.out.println("Peoples per place: "+PEOPLES_PER_PLACE);
		System.out.println("Checkin per place: "+CHECKINS_PER_PLACE);
		
		MyLog.log("Get out average");

	}
	
	//xem gia tri trug binh la rate 2.5
	//tinh rate theo nguoi dung
	public void caculateRatebyPerson(ArrayList<MyUser> users){
		System.out.println("Start caculate rate");
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
				double rate = place.getNumCheck()*2.5/(float)average_checkin<=5 ? place.getNumCheck()*2.5/(float)average_checkin:5;
				place.setRate(rate);
			}
		}
		System.out.println("End caculaterate");
	}
	
	public double getSimilarybyCosin(MyUser userA, MyUser userB){
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
	
	//giam kich thuoc dia diem
	public void preexcutePlace(ArrayList<MyPlace> places){
		MyLog.log("Entered preexcutedPlace");
		for(int i=0; i<places.size(); i++)
			if(places.get(i).getNumCheck()<25 || places.get(i).getNumpeoplecheck()<3) places.set(i, null);
		MyLog.log("Get out preexcutedPlace");
	}
	
	
	public void refreshPlace(ArrayList<MyUser> users, ArrayList<MyPlace> places, MyDatabase database){
		MyLog.log("Entered refreshPlace");
		long timestart = System.currentTimeMillis();
		for(int i=0; i<users.size(); i++){
			MyUser user = users.get(i);
			if(MyData.isvalue(user))
			for(int j=0; j<user.getPlaceList().size(); j++){
				if(!MyDatabaseHelper.isInplaces(user.getPlaceList().get(j).getId(), database)){
					user.getPlaceList().remove(j);
					j--;
				}
			}
		}
		long timeend = System.currentTimeMillis();
		MyLog.log("Get out refresh place, calculate in: "+(timeend-timestart));
	}
	//giam kich thuoc dữ liệu, tim so place
	public void preexcuteUser(ArrayList<MyUser> users, ArrayList<MyPlace> places, MyDatabase database){
		System.out.println("Start preexcutedata");
		//loai bot du lieu it gia tri
		for(int i=0; i<users.size(); i++){
			MyUser user = users.get(i);
			if(user.getnumcheckin()<88*4 || user.getPlaceList().size()<21*4
					|| user.getFriendlist().size()<8*4) //users.remove(i--);
				//users.set(i, null);
				users.get(i).setIsvalue(false);
		}
		
		System.out.println("End preexcute data");
		
	}
	public void refreshFriend(ArrayList<MyUser> users){
		MyLog.log("Entered refreshfriend");
		int id2 = 0;
		for(int i=0; i<users.size(); i++)
			if(MyData.isvalue(users.get(i))){
				users.get(i).setId2(id2++);
				for(int j=0; j<users.get(i).getFriendlist().size(); j++){
					if(!MyData.isvalue(users.get(i).getFriendlist().get(j))){
						users.get(i).getFriendlist().remove(j);
						j--;
					}
				}
			}
		MyLog.log("Get out refreshfriend");
	}
}

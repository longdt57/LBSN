package algorithms;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import object.MyPlace;
import object.MyUser;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

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
	//tinh rate theo nguoi dung
	public void caculateRate(ArrayList<MyUser> users){
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
		System.out.println("Start preexcutedata");
//		for(int i=0; i<users.size(); i++){
//			MyUser user = users.get(i);
//			if(user.getnumcheckin()<MIN_CHECKIN || user.getPlaceList().size()<MIN_PLACE) //users.remove(i--);
//				user=null;
//		}
//		int dem=0;
		
				try {
					MongoClient mongoClient 	= new MongoClient( "localhost" , 27017 );
					DB db 						= mongoClient.getDB( "mydb" );
					DBCollection collection		= db.getCollection("places");
					
					for(int i=0;i <users.size();i++){
						MyUser user = users.get(i);
//						dem+=user.getPlaceList().size();
						for(int j=0;j<user.getPlaceList().size(); j++){
							
							MyPlace place = user.getPlaceList().get(j);
							
							BasicDBObject data = new BasicDBObject(MyPlace.PLACE_ID, place.getId());
							data.put(MyUser.USER_ID, user.getId());
							data.put(MyPlace.PLACE_NUMCHECK, place.getNumCheck());
							collection.insert(data);
							
						}
					}
					
					
					
					DBCursor cursorDoc = collection.find();
					System.out.println(cursorDoc.count());
					
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			
//		System.out.println("dem: "+dem);
		//MongoClient mongoClient = new MongoClient();
		// or
		System.out.println("End preexcute data");
		
	}
}

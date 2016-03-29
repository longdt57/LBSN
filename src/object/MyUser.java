package object;

import java.util.ArrayList;
import java.util.Date;

public class MyUser {
	
	public static final String USER_ID 		= "userid";
	public static final String FRIEND_LIST_ID	= "friendlistid";
	String id;
	int id2;
	boolean isvalue = true;
	ArrayList<MyUser> friendlist;
	ArrayList<MyPlace> placelist;
	ArrayList<Double> friendsimilarities = new ArrayList<Double>();
	
	
	public MyUser(String id, ArrayList<MyUser> friends, ArrayList<MyPlace> places){
		this.id			= id;
		this.friendlist = friends;
		this.placelist	= places;
	}
	public MyUser(String id){
		this.id 	= id;
		friendlist 	= new ArrayList<MyUser>();
		placelist	= new ArrayList<MyPlace>();
	}
	
	public void setId(String id){this.id = id;}
	public void setFriendlist(ArrayList<MyUser> friends){this.friendlist = friends;}
	public void setPlaceList(ArrayList<MyPlace> places){this.placelist = places;}
	
	public String getId(){return id;}
	public ArrayList<MyUser> getFriendlist(){return friendlist;}
	public ArrayList<MyPlace> getPlaceList(){return placelist;}
	
	public String toString(){
		return "id: " + id + "\tfriendlist size: " + friendlist.size() + "\tplacelist size: " + placelist.size();
	}
	
	public void addOrIncreasePlace(MyPlace place){
		for(int i=0; i<placelist.size(); i++){
			MyPlace placetmp = placelist.get(i);
			if(placetmp.isSame(place)){
				placetmp.setNumcheck(placetmp.getNumCheck()+1);
				return;
			}
		}
		placelist.add(place);
	}
	public int getnumcheckin(){
		int num=0;
		for(int i=0; i<placelist.size(); i++){
			num+=placelist.get(i).getNumCheck();
		}
		return num;
	}
	public void setId2(int id){this.id2 = id;}
	public int getId2(){return id2;}
	public void setIsvalue(boolean isvalue){ this.isvalue = isvalue;}
	public boolean getIsvalue(){
		return isvalue;
	}
	public void addPlace(MyPlace place){
		placelist.add(place);
	}
	
	public void sortPlacelistByDate(){
		for(int i=0; i<placelist.size()-1; i++){
			for(int j = i+1; j<placelist.size(); j++){
				Date date1 = FunctionConstant.getDatefromString(placelist.get(i).getCheckinTime());
				Date date2 = FunctionConstant.getDatefromString(placelist.get(j).getCheckinTime());
				if(date1.after(date2)){
					MyPlace placetmp = placelist.get(j);
					placelist.remove(j);
					placelist.add(j, placelist.get(i));
					placelist.remove(i);
					placelist.add(i, placetmp);
				}
			}
		}
	}
	public ArrayList<Double> getfriendsim(){return friendsimilarities;}
}

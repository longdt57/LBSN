package object;

import java.util.ArrayList;

public class MyUser {
	String id;
	ArrayList<MyUser> friendlist;
	ArrayList<MyPlace> placelist;
	ArrayList<Float> similarities;
	
	
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
}

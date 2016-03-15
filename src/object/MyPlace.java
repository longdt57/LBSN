package object;

import java.util.ArrayList;

public class MyPlace {
	String id;
	double latitude;
	double longitude;
	String checkinTime;
	int numcheck = 1;
	
	double rate;
	ArrayList<MyUser> userlist = new ArrayList<MyUser>();
	
	public MyPlace(String id, double lat, double log, String checkin, double rate){
		this.id				= id;
		this.latitude		= lat;
		this.longitude		= log;
		this.checkinTime	= checkin;
		this.rate			= rate;
	}
	
	public MyPlace(String id, double rate){
		this.id		= id;
		this.rate	= rate;
	}
	
	public MyPlace(String id){
		this.id = id;
	}
	
	public MyPlace(String id, double lat, double log, String checkin){
		this.id				= id;
		this.latitude		= lat;
		this.longitude		= log;
		this.checkinTime	= checkin;
	}
	public MyPlace(String id, double lat, double log, String checkin, int numcheck){
		this.id				= id;
		this.latitude		= lat;
		this.longitude		= log;
		this.checkinTime	= checkin;
		this.numcheck 		= numcheck;
	}
	
	public String getId(){return id;}
	public int getNumCheck(){return numcheck;}
	public double getRate(){return rate;}
	
	public void setNumcheck(int num){numcheck = num;}
	public void setRate(double rate){this.rate = rate;}
	
	public boolean isSame(MyPlace place){
		if(id.compareTo(place.getId())==0) return true;
		return false;
	}
	
	public String toString(){
		return "id: " + id + "\tNumcheck: " + numcheck + "\trate: "+rate;
	}
	public MyPlace clone(){
		return new MyPlace(id, latitude, longitude,checkinTime, numcheck);
	}
}

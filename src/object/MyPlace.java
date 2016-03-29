package object;

import java.util.ArrayList;

public class MyPlace {
	
	public static final String PLACE_ID 		= "placeid";
	public static final String PLACE_NUMCHECK	= "numcheck";
	public static final String LATITUDE			= "latitude";
	public static final String LONGITUDE		= "longitude";
	public static final String CHECKINTIME		= "checkintime";
	public static final String NUMPEOPLE		= "numpeople";
			
	String id;
	double latitude;
	double longitude;
	String checkinTime;
	int numcheck = 1;
	
	double rate;
	ArrayList<String> userlist = new ArrayList<String>();
	int numpeoplecheck = 0;
	
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
	public MyPlace(String id, int numcheck){
		this.id 		= id;
		this.numcheck 	= numcheck;
	}
	
	public String getId(){return id;}
	public int getNumCheck(){return numcheck;}
	public double getRate(){return rate;}
	
	public void setNumcheck(int num){numcheck = num;}
	public void setRate(double rate){this.rate = rate;}
	public void setNumPeople(int numpeople){
		this.numpeoplecheck = numpeople;
	}
	
	public boolean isSame(MyPlace place){
		if(id.compareTo(place.getId())==0) return true;
		return false;
	}
	
	public String toString(){
		return "id: " + id + "\tDate:"+checkinTime;
	}
	public MyPlace clone(){
		return new MyPlace(id, latitude, longitude,checkinTime, numcheck);
	}
	public void increasenumpeoplecheck(){numpeoplecheck++;}
	public int getNumpeoplecheck(){return numpeoplecheck;}
	public String getCheckinTime(){return checkinTime;}
}

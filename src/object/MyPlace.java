package object;

public class MyPlace {
	String id;
	double latitude;
	double longitude;
	String checkinTime;
	int numcheck = 1;
	
	double rate;
	
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
}

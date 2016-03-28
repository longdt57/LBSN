package main;

import object.MyDatabase;
import object.MyLog;
import algorithms.Algorithms;
import utilities.MyData;

public class main {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		preexcuteData();
		
//		MyData data = new MyData();
//		MyDatabase database = new MyDatabase("localhost", 27017, "mydb");
//		System.out.println("connect to database: " + database.connect());
//		
//		data.setUsersFromFileSource(MyData.FRIENDEDGE_FILENAME);
//		data.readNumPlacecheckinbyPerson(database, MyData.PERSON_PLACE_CHECKIN_COLLECTION_FINAL);
//		data.readPlacefromDB(database, MyData.PERSON_PLACE_CHECKIN_COLLECTION_FINAL);
//		
//		Algorithms.ALGORITHM.refreshFriend(data.getUsers());
//		
//		Algorithms.ALGORITHM.setAverageData(data, database);
//		data.print(database);
	}
	
	public static void preexcuteData(){
		long timestart = System.currentTimeMillis();
		MyLog.log("Entered preexcuteData");
		MyData brightkite_edge = new MyData();
		MyDatabase database = new MyDatabase("localhost", 27017, "mydb");
		System.out.println("connect to database: " + database.connect());
		database.getCollection(MyData.PERSON_PLACE_CHECKIN_COLLECTION).drop();
		database.getCollection(MyData.PLACE_CHECKINS_PEOPLE).drop();
		database.getCollection(MyData.PERSON_PLACE_CHECKIN_COLLECTION_FINAL).drop();
		
		brightkite_edge.setUsersFromFileSource(MyData.FRIENDEDGE_FILENAME);
		brightkite_edge.setPlacesFromFileSource(MyData.CHECKIN_FILE);
		
		
		Algorithms.ALGORITHM.preexcuteUser(brightkite_edge.getUsers(), brightkite_edge.getPlaces(), database);
		//brightkite_edge.saveNumPlacecheckinbyPerson(database, MyData.PERSON_PLACE_CHECKIN_COLLECTION);
		
		//brightkite_edge.readPlacefromDB(database, MyData.PERSON_PLACE_CHECKIN_COLLECTION);
		
		
		//Algorithms.ALGORITHM.preexcutePlace(brightkite_edge.getPlaces());
		//brightkite_edge.savePlacebyCheckinsandPeople(database);
		//Algorithms.ALGORITHM.refreshPlace(brightkite_edge.getUsers(), brightkite_edge.getPlaces(), database);
		Algorithms.ALGORITHM.refreshFriend(brightkite_edge.getUsers());
		
		//brightkite_edge.saveNumPlacecheckinbyPerson(database, MyData.PERSON_PLACE_CHECKIN_COLLECTION_FINAL);
		//Algorithms.ALGORITHM.setAverageData(brightkite_edge,database);
		brightkite_edge.print(database);
		
		brightkite_edge.writefilterfriendToFile(MyData.FRIENDEDGE_FILENAME_FILTER);
		
		long timeend = System.currentTimeMillis();
		MyLog.log("Get out preexcuteData, excute data in: "+(timeend-timestart));

	}

}

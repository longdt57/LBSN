package main;

import object.MyDatabase;
import object.MyLog;
import algorithms.Algorithms;
import utilities.MyData;
import utilities.MyDatabaseHelper;

public class main {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//preexcuteData();
		
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
		
		MyData data = new MyData(830);
		data.setPlacesFromFileSourceWithDate(MyData.CHECKIN_FILE_FILTER);
		MyData trainingdata, testdata = null;
		trainingdata = data.getTrainningAndTestData(testdata);
		trainingdata.setUsersFromFileSource(MyData.FRIENDEDGE_FILENAME_FILTER);
		Algorithms.ALGORITHM.caculateRatebyPerson(trainingdata.getUsers());
		trainingdata.calculateSim();
		trainingdata.print();
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
		MyDatabaseHelper.saveNumPlacecheckinbyPerson(database, MyData.PERSON_PLACE_CHECKIN_COLLECTION, 
				brightkite_edge.getUsers());
		
		MyDatabaseHelper.readPlacefromDB(database, MyData.PERSON_PLACE_CHECKIN_COLLECTION, brightkite_edge.getPlaces());
		
		
		Algorithms.ALGORITHM.preexcutePlace(brightkite_edge.getPlaces());
		MyDatabaseHelper.savePlacebyCheckinsandPeople(database, brightkite_edge.getPlaces());
		//Algorithms.ALGORITHM.refreshPlace(brightkite_edge.getUsers(), brightkite_edge.getPlaces(), database);
		Algorithms.ALGORITHM.refreshFriend(brightkite_edge.getUsers());
		
		MyDatabaseHelper.saveNumPlacecheckinbyPerson(database, MyData.PERSON_PLACE_CHECKIN_COLLECTION_FINAL, 
				brightkite_edge.getUsers());
		Algorithms.ALGORITHM.setAverageData(brightkite_edge,database);
		brightkite_edge.print();
		
		brightkite_edge.writeFilterfriendToFile(MyData.FRIENDEDGE_FILENAME_FILTER);
		brightkite_edge.writeFilterPlacesToFile(MyData.CHECKIN_FILE_FILTER, MyData.CHECKIN_FILE, database);
		
		long timeend = System.currentTimeMillis();
		MyLog.log("Get out preexcuteData, excute data in: "+(timeend-timestart));

	}

}

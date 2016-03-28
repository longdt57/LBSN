package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import object.MyDatabase;
import object.MyLog;
import object.MyPlace;
import object.MyUser;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;


public class MyData {
	public static final String FRIENDEDGE_FILENAME 	= "Brightkite_edges.txt";
	public static final String FRIENDEDGE_FILENAME_FILTER = "Brightkite_edges_filter.txt";
	//public static final String CHECKIN_FILE		= "E:\\documents\\KLTN\\K57_He tu van\\LBSN\\Brightkite_totalCheckins.txt";
	public static final String CHECKIN_FILE			= "F:\\hoctap\\hoctap\\nam 4\\KLTN\\K57_He tu van\\data\\Brightkite_totalCheckins.txt";
	private static final int 	NUMBER_USER = 58228;
	
	public static final String FILERATE				= "C:\\rate-data.txt";
	
	public static final String RELATIONSHIP_COLLECTION 			= "friends";
	public static final String PERSON_PLACE_CHECKIN_COLLECTION	= "person_place_checkin";
	public static final String PLACE_CHECKINS_PEOPLE			= "place_checkins_people";
	public static final String PERSON_PLACE_CHECKIN_COLLECTION_FINAL ="person_place_checkin_final";
	
	public static final int LIST_SIZE_PER_WRITE_NOSQL = 10000;

	
	ArrayList<MyUser> users;
	ArrayList<MyPlace> places;
	
	int maxcheck=0;
	int numplace =0;
	int pos = 0;
	String maxcheckplace = "";
	public MyData(){
		users = new ArrayList<MyUser>();
		places = new ArrayList<MyPlace>();
		for(int i=0;i <NUMBER_USER; i++){
			MyUser user = new MyUser(String.valueOf(i));
			users.add(user);
		}
	}
	
	public ArrayList<MyUser> getUsers(){return users;}
	public ArrayList<MyPlace> getPlaces(){return places;}
	
	public void setUsersFromFileSource(String filesource){
		long timestart = System.currentTimeMillis();
		MyLog.log("Entered setUsersFromfilesource");
		try{
			Scanner sc = new Scanner(new File(filesource));
			while(sc.hasNext()){
				int id = sc.nextInt();
				int friendid = sc.nextInt();
				users.get(id).getFriendlist().add(users.get(friendid));
			}
			sc.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		long timeend = System.currentTimeMillis();
		MyLog.log("Get out setUsersFromFileSource, read data in: "+(timeend-timestart));
	}
	
	//doc du lieu vao tu file txt
	public void setPlacesFromFileSource(String filesource){
		long timestart = System.currentTimeMillis();
		MyLog.log("Entered setPlacesFromFileSource");
		String linedata = "";
		try{
			Scanner sc = new Scanner(new File(filesource));
			while(sc.hasNext()){
				linedata = sc.nextLine();
				String data[]  	= linedata.split("\\s+");
				if(data.length>=5){
					int userid 		= Integer.parseInt(data[0]);
					String checkin	= data[1];
					double lat 		= Double.parseDouble(data[2]);
					double log 		= Double.parseDouble(data[3]);
					String id 		= data[4];
					
					MyPlace place = new MyPlace(id, lat, log, checkin);
					if(id.compareTo("00000000000000000000000000000000")!=0)		//loai bo dia diem nhieu 0.0 0.0
						users.get(userid).addOrIncreasePlace(place);
				}
			}
			sc.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}catch(ArrayIndexOutOfBoundsException e){
			MyLog.log(linedata);
			e.printStackTrace();
		}
		long timeend = System.currentTimeMillis();
		MyLog.log("Get Out setPlacesFromFileSource, read time: "+(timeend-timestart) );
	}
	
	

	public void print(MyDatabase database){
		MyLog.log("Entered print");
		
		int totalpeople = 0, totalplace = 0;
		for(int i=0; i<users.size(); i++)
			if(isvalue(users.get(i))) totalpeople++;
		for(int i=0; i<places.size(); i++)
			if(isvalue(places.get(i))) totalplace++;
		MyLog.log("total people: "+totalpeople);
		MyLog.log("total place: "+totalplace);
		MyLog.log("Get out print");
	}
	
	public void savePlacebyCheckinsandPeople(MyDatabase database){
		long timestart = System.currentTimeMillis();
		MyLog.log("Entered savePlacebyCheckinsandPeople");
		
		MongoCollection<Document> collection		= database.getCollection(PLACE_CHECKINS_PEOPLE);
		if(collection==null){
			MyLog.log("error in saveNumPlacecheckinbyperson");
			return;
		}
		List<Document> documents = new ArrayList<Document>();
		for(int i=0; i<places.size(); i++){
			MyPlace place = places.get(i);
			if(isvalue(place)){
				Document data = new Document(MyPlace.PLACE_ID, place.getId());
				data.put(MyPlace.PLACE_NUMCHECK, place.getNumCheck());
				data.put(MyPlace.NUMPEOPLE, place.getNumpeoplecheck());
				documents.add(data);
			}
			if(documents.size()>= LIST_SIZE_PER_WRITE_NOSQL){
				collection.insertMany(documents);
				documents.clear();
			}
		}
		
		if(documents.size()!=0){
			collection.insertMany(documents);
			documents.clear();
		}
		
		long timeend = System.currentTimeMillis();
		MyLog.log("Get out savePlacebyCheckinsandPeople, write data in: "+(timeend-timestart));
	}
	
	public static boolean isInplaces(String placeid, MyDatabase database){
		Document query = new Document(MyPlace.PLACE_ID, placeid);
		MongoCollection<Document> collection = database.getCollection(PLACE_CHECKINS_PEOPLE);
		MongoCursor<Document> iterable = collection.find(query).iterator();
		
		if(iterable.hasNext()) {
			iterable.close();
			return true;
		}
		iterable.close();
		return false;
	}
	public boolean isInplaces(String placeid){
		for(int i=0; i<places.size(); i++)
			if(places.get(i).getId().equals(placeid)) return true;
		return false;
	}
	
	public void saveNumPlacecheckinbyPerson( MyDatabase database, String tablename){
		long timestart = System.currentTimeMillis();
		MyLog.log("Entered saveNumPlacecheckinbyPerson");
		MongoCollection<Document> collection		= database.getCollection(tablename);
		if(collection==null){
			MyLog.log("error in saveNumPlacecheckinbyperson");
			return;
		}
		int totalplace =0;
		List<Document> documents = new ArrayList<Document>();
		for(int i=0;i <users.size();i++){
			MyUser user = users.get(i);
			if(MyData.isvalue(user)){
				for(int j=0;j<user.getPlaceList().size(); j++){
					totalplace++;
					MyPlace place = user.getPlaceList().get(j);
					
					Document data = new Document(MyUser.USER_ID, user.getId());
					data.put(MyPlace.PLACE_ID, place.getId());
					data.put(MyPlace.PLACE_NUMCHECK, new Integer(place.getNumCheck()));
					documents.add(data);
				}
				if(documents.size()>= LIST_SIZE_PER_WRITE_NOSQL){
					collection.insertMany(documents);
					documents.clear();
				}
			}
			
		}
		if(documents.size()!=0){
			collection.insertMany(documents);
			documents.clear();
		}
		MyLog.log("Total place: "+totalplace);
		long timeend = System.currentTimeMillis();
		MyLog.log("Get out saveNumPlacecheckinbyPerson, write data in: "+(timeend-timestart));
	}
	
	public void readNumPlacecheckinbyPerson(MyDatabase database, String tablename){
		long timestart = System.currentTimeMillis();
		MyLog.log("Entered readNumPlacecheckinbyPerson");
		MongoCollection<Document> collection		= database.getCollection(tablename);
		if(collection==null){
			MyLog.log("error in readNumPlacecheckinbyPerson");
			return;
		}
		
		MongoCursor<Document> cursor = collection.find().iterator();
		try{
			while(cursor.hasNext()){
				Document data 		= cursor.next();
				String userid 		= data.getString(MyUser.USER_ID);
				String placeid 		= data.getString(MyPlace.PLACE_ID);
				int numcheckin		= data.getInteger(MyPlace.PLACE_NUMCHECK);
				
				MyPlace place = new MyPlace(placeid, numcheckin);
				users.get(Integer.parseInt(userid)).getPlaceList().add(place);
			}
		}finally{
			cursor.close();
		}
		
		
		
		long timeend = System.currentTimeMillis();
		MyLog.log("Collection count: "+ collection.count());
		MyLog.log("Get out readNumPlacecheckinbyPerson, read data in: "+(timeend-timestart));

	}
	public static int getNumPeopleCheckin(MyDatabase database, String placeid, String tablename){
		int result = 0;
		Document query = new Document(MyPlace.PLACE_ID, placeid);
		MongoCollection<Document> collection = database.getCollection(tablename);
		MongoCursor<Document> iterable = collection.find(query).iterator();
		try{
			while(iterable.hasNext()){
				result += 1;
				iterable.next();
			}
		}finally{
			iterable.close();
		}
		return result;
	}
	
	public static int getNumCheckin(MyDatabase database, String placeid, String tablename){
		int result = 0;
		Document query = new Document(MyPlace.PLACE_ID, placeid);
		MongoCollection<Document> collection = database.getCollection(tablename);
		MongoCursor<Document> iterable = collection.find(query).iterator();
		try{
			while(iterable.hasNext()){
				result += iterable.next().getInteger(MyPlace.PLACE_NUMCHECK, 0);
			}
		}finally{
			iterable.close();
		}
		return result;
	}
	
	public void readPlacefromDB(MyDatabase database, String tablename){
		MongoCollection<Document> collection = database.getCollection(tablename);
		//Document sort 	= new Document("$sort", new Document(MyPlace.PLACE_ID, 1));
		Document group_getnumpeople 	= new Document("$group", new Document("_id", "$"+MyPlace.PLACE_ID)
			.append(MyPlace.NUMPEOPLE, new Document("$sum",1)));
		Document group_getnumcheckin 	= new Document("$group", new Document("_id", "$"+MyPlace.PLACE_ID)
			.append(MyPlace.PLACE_NUMCHECK, new Document("$sum","$"+MyPlace.PLACE_NUMCHECK)));
			
		AggregateIterable<Document> iterable_getpeople 		= collection.aggregate(Arrays.asList(group_getnumpeople));
		AggregateIterable<Document> iterable_getnumckeck 	= collection.aggregate(Arrays.asList(group_getnumcheckin));
		
		iterable_getnumckeck.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        //System.out.println(document.toJson());
		    	MyPlace place = new MyPlace(document.getString("_id"), document.getInteger(MyPlace.PLACE_NUMCHECK, 0));
		    	places.add(place);
		    }
		});
		pos = 0;
		iterable_getpeople.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	MyPlace place = places.get(pos);
		    	place.setNumPeople(document.getInteger(MyPlace.NUMPEOPLE, 0));
		    	pos += 1;
		    }
		});
	}
	
	public static boolean isvalue(MyUser user){
		if(user == null ||user.getFriendlist().size()<=0 || user.getPlaceList().size()<=0) return false;
		return true;
	}
	public static boolean isvalue(MyPlace place){
		if(place==null || place.getNumCheck()==0 || place.getNumpeoplecheck()==0) return false;
		return true;
	}
	
	public void writefilterfriendToFile(String filesource){
		long timestart = System.currentTimeMillis();
		MyLog.log("Entered writefilterfriendToFile");
		try{
			File file = new File(filesource);
			FileWriter fr = new FileWriter(file);
			BufferedWriter br = new BufferedWriter(fr);
			for(int i = 0; i<users.size(); i++){
				if(MyData.isvalue(users.get(i))){
					for(int j=0; j<users.get(i).getFriendlist().size(); j++){
						if(MyData.isvalue(users.get(i).getFriendlist().get(j))){
							String out = users.get(i).getId()+ " "+ users.get(i).getFriendlist().get(j).getId()+"\n";
							br.write(out);
						}
					}
				}
			}
			
			br.close();
			fr.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		long timeend = System.currentTimeMillis();
		MyLog.log("Get out writefilterfriendToFile, write data in: "+(timeend-timestart));
	}
	
	public void writePlacesToFile(String filesource){
		try{
			File file = new File(filesource);
			FileWriter fr = new FileWriter(file);
			BufferedWriter br = new BufferedWriter(fr);
			
			br.close();
			fr.close();
		}catch(Exception e){
			MyLog.log(e.toString());
		}
	}
}

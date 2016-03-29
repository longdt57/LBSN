package utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import object.MyDatabase;
import object.MyLog;
import object.MyPlace;
import object.MyUser;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class MyDatabaseHelper {
	public static int pos;

	public static boolean isInplaces(String placeid, MyDatabase database){
		Document query = new Document(MyPlace.PLACE_ID, placeid);
		MongoCollection<Document> collection = database.getCollection(MyData.PLACE_CHECKINS_PEOPLE);
		MongoCursor<Document> iterable = collection.find(query).iterator();
		
		if(iterable.hasNext()) {
			iterable.close();
			return true;
		}
		iterable.close();
		return false;
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
	
	public static void savePlacebyCheckinsandPeople(MyDatabase database, ArrayList<MyPlace> places){
		long timestart = System.currentTimeMillis();
		MyLog.log("Entered savePlacebyCheckinsandPeople");
		
		MongoCollection<Document> collection		= database.getCollection(MyData.PLACE_CHECKINS_PEOPLE);
		if(collection==null){
			MyLog.log("error in saveNumPlacecheckinbyperson");
			return;
		}
		List<Document> documents = new ArrayList<Document>();
		for(int i=0; i<places.size(); i++){
			MyPlace place = places.get(i);
			if(MyData.isvalue(place)){
				Document data = new Document(MyPlace.PLACE_ID, place.getId());
				data.put(MyPlace.PLACE_NUMCHECK, place.getNumCheck());
				data.put(MyPlace.NUMPEOPLE, place.getNumpeoplecheck());
				documents.add(data);
			}
			if(documents.size()>= MyData.LIST_SIZE_PER_WRITE_NOSQL){
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
	
	public static void saveNumPlacecheckinbyPerson( MyDatabase database, String tablename, ArrayList<MyUser> users){
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
				if(documents.size()>= MyData.LIST_SIZE_PER_WRITE_NOSQL){
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
	public static void readNumPlacecheckinbyPerson(MyDatabase database, String tablename, 
			ArrayList<MyUser> users){
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
	
	public static void readPlacefromDB(MyDatabase database, String tablename, ArrayList<MyPlace> places
			){
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
	
}

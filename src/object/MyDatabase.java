package object;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MyDatabase {
	
	MongoDatabase database;
	String host;
	int port;
	String dbname;
	private MongoClient mongoClient;
	
	public MyDatabase(String host, int port, String dbname){
		this.host 	= host;
		this.port	= port;
		this.dbname = dbname;
	}
	
	public boolean connect(){
		mongoClient = new MongoClient( host , port );
		database 							= mongoClient.getDatabase(dbname);
		return true;
	}
	
//	public void saveRelationshipToDB(ArrayList<MyUser> users){
//		
//		DBCollection collection		= db.getCollection(RELATIONSHIP_TABLE);
//		if(collection==null) {
//			System.out.println("error when save relationshiptodb");
//			return;
//		}
//		
//		for(int i=0;i <users.size(); i++){
//			MyUser user = users.get(i);
//			String listfriendid = "";
//			for(int j=0; j<user.getFriendlist().size(); j++){
//				listfriendid +=user.getFriendlist().get(j).getId()+" ";
//			}
//			BasicDBObject data = new BasicDBObject(MyUser.USER_ID, user.getId());
//			data.put(MyUser.FRIEND_LIST_ID, listfriendid);
//			collection.insert(data);
//		}
//	}
	public MongoCollection<Document> getCollection(String collectioname){
		return database.getCollection(collectioname);
	}
	
}

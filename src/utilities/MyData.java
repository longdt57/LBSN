package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import algorithms.Algorithms;
import object.MyDatabase;
import object.MyLog;
import object.MyPlace;
import object.MyUser;


public class MyData {
	public static final String FRIENDEDGE_FILENAME 	= "Brightkite_edges.txt";
	public static final String FRIENDEDGE_FILENAME_FILTER = "Brightkite_edges_filter.txt";
	public static final String CHECKIN_FILE_FILTER 	= "Brightkite_totalCheckins_filter.txt";
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
	
	public MyData(int numpeople){
		users = new ArrayList<MyUser>();
		places = new ArrayList<MyPlace>();
		for(int i=0;i <numpeople; i++){
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
					
					
					if(id.compareTo("00000000000000000000000000000000")!=0){		//loai bo dia diem nhieu 0.0 0.0
						MyPlace place = new MyPlace(id, lat, log, checkin);
						users.get(userid).addOrIncreasePlace(place);
					}
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
	
	public void setPlacesFromFileSourceWithDate(String filesource){
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
					users.get(userid).addPlace(place);
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
	
	

	public void print(){
		MyLog.log("Entered print");
		
		int totalpeople = 0;
		for(int i=0; i<users.size(); i++)
			if(isvalue(users.get(i))) totalpeople++;
		MyLog.log("total people: "+totalpeople);
		
		for(int i=0; i<users.get(1).getfriendsim().size(); i++)
			MyLog.log(users.get(1).getFriendlist().get(i).getId()+" "+users.get(0).getfriendsim().get(i)+"");
		MyLog.log("Get out print");
	}
	
	
	public static boolean isvalue(MyUser user){
		return user.getIsvalue();
	}
	public static boolean isvalue(MyPlace place){
		if(place==null || place.getNumCheck()==0 || place.getNumpeoplecheck()==0) return false;
		return true;
	}
	
	public void writeFilterfriendToFile(String filesource){
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
							String out = users.get(i).getId2()+ " "+ users.get(i).getFriendlist().get(j).getId2()+"\n";
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
	
	public void writeFilterPlacesToFile(String fileout, String filein, MyDatabase database){
		long timestart = System.currentTimeMillis();
		MyLog.log("Entered writeplaceToFile");
		try{
			String linedata = "";
			File file = new File(fileout);
			FileWriter fr = new FileWriter(file);
			BufferedWriter br = new BufferedWriter(fr);
			
			Scanner sc = new Scanner(new File(filein));
			while(sc.hasNext()){
				linedata = sc.nextLine();
				String data[]  	= linedata.split("\\s+");
				if(data.length>=5){
					int userid 		= Integer.parseInt(data[0]);
					String checkin	= data[1];
					double lat 		= Double.parseDouble(data[2]);
					double log 		= Double.parseDouble(data[3]);
					String id 		= data[4];
					
					if(id.compareTo("00000000000000000000000000000000")!=0){		//loai bo dia diem nhieu 0.0 0.0
						boolean isvalue = false;
						for(int i=0; i<users.size(); i++){
							if(isvalue(users.get(i)) && users.get(i).getId().equals(userid+"")){ 
								isvalue = true;
								break;
							}
							//if(i== users.size()-1) isvalue = false;
						}
						if(isvalue){
							isvalue = MyDatabaseHelper.isInplaces(id, database);
						}
						if(isvalue){
							br.write(users.get(userid).getId2()+"\t"+checkin+"\t"+lat+"\t"+log+"\t"+id+"\n");
						}
					}
				}
			}
			br.close();
			fr.close();
			sc.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		long timeend = System.currentTimeMillis();
		MyLog.log("Get out writeplaceToFile, write data in: "+(timeend-timestart));
	}
	
	public void sortUsersPlacesByDate(){
		for(int i=0; i<users.size(); i++){
			users.get(i).sortPlacelistByDate();
		}
	}
	public MyData getTrainningAndTestData(MyData testdata){
		testdata = new MyData(users.size());
		MyData trainningdata = new MyData(users.size());
		for(int i=0; i<users.size(); i++){
			if(users.get(i).getPlaceList().size()!=0)
			for(int j=users.get(i).getPlaceList().size()/10; j>=0; j--){
				testdata.getUsers().get(i).getPlaceList().add(users.get(i).getPlaceList().get(j));
				users.get(i).getPlaceList().remove(j);
			}
			for(int j=0; j<users.get(i).getPlaceList().size(); j++){
				trainningdata.getUsers().get(i).addOrIncreasePlace(users.get(i).getPlaceList().get(j));
			}
		}
		return trainningdata;
	}
	
	public void calculateSim(){
		for(int i=0; i<users.size(); i++){
			for(int j=0; j<users.get(i).getFriendlist().size(); j++){
				users.get(i).getfriendsim().add(Algorithms.ALGORITHM.getSimilarybyCosin(
						users.get(i), users.get(i).getFriendlist().get(j)));
			}
		}
	}
}

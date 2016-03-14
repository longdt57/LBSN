package utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import algorithms.CaculateRate;


import object.MyPlace;
import object.MyUser;

public class MyData {
	public static final String FRIENDEDGE_FILENAME 	= "Brightkite_edges.txt";
	public static final String CHECKIN_FILE		= "E:\\documents\\KLTN\\K57_He tu van\\LBSN\\Brightkite_totalCheckins.txt";
	private static final int 	NUMBER_USER = 58228;

	
	ArrayList<MyUser> users;
	
	public MyData(){
		users = new ArrayList<MyUser>();
		for(int i=0;i <NUMBER_USER; i++){
			MyUser user = new MyUser(String.valueOf(i));
			users.add(user);
		}
	}
	
	public ArrayList<MyUser> getUsers(){return users;}
	
	public void setUsersFromFileSource(String filesource){
		try{
			Scanner sc = new Scanner(new File(filesource));
			while(sc.hasNext()){
				int id = sc.nextInt();
				int friendid = sc.nextInt();
				users.get(id).getFriendlist().add(users.get(friendid));
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void setPlacesFromFileSource(String filesource){
		String linedatabefore = "";
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
					users.get(userid).addOrIncreasePlace(place);
					linedatabefore = linedata;
				}
				
//				int userid 		= sc.nextInt();
//				String checkin 	= sc.next();
//				double lat		= sc.nextDouble();
//				double log 		= sc.nextDouble();
//				String id		= sc.next();
				
				
			}
			CaculateRate.CRATE.caculateRate(users);
			//sortbynumcheckin();
		}catch(IOException e){
			e.printStackTrace();
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println(linedatabefore);
			System.out.println(linedata);
			e.printStackTrace();
		}
	}
	
	private void sortbynumcheckin(){
		
	}
	
	
	public void print(){
		for(int i=0;i<1000; i++){
			int max=0, maxpos=0;
			for(int j =1; j<users.size(); j++){
				if(max<users.get(j).getnumcheckin()){
					max = users.get(j).getnumcheckin();
					maxpos = j;
				}
			}
			System.out.println(i+" "+max);
			users.remove(maxpos);
		}
	}
}

package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import algorithms.Algorithms;


import object.MyPlace;
import object.MyUser;

public class MyData {
	public static final String FRIENDEDGE_FILENAME 	= "Brightkite_edges.txt";
	//public static final String CHECKIN_FILE		= "E:\\documents\\KLTN\\K57_He tu van\\LBSN\\Brightkite_totalCheckins.txt";
	public static final String CHECKIN_FILE			= "F:\\hoctap\\hoctap\\nam 4\\KLTN\\K57_He tu van\\data\\Brightkite_totalCheckins.txt";
	private static final int 	NUMBER_USER = 58228;
	
	public static final String FILERATE				= "rate-data.txt";

	
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
			sc.close();
			
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
			sc.close();
			Algorithms.ALGORITHM.caculateRate(users);
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
	
	public boolean savePlacestoFile(String filename){
		File file 				= new File(filename);
		try {
			FileWriter fwrite 		= new FileWriter(file);
			BufferedWriter bwrite 	= new BufferedWriter(fwrite);
			bwrite.write(users.size()+"\n");
			for(int i=0; i<users.size(); i++){
				MyUser user		= users.get(i);
				String title 	= user.getId()+" "+user.getPlaceList().size()+"\n";
				bwrite.write(title);
				for(int j=0; j<user.getPlaceList().size(); j++){
					MyPlace place	= user.getPlaceList().get(j);
					String line 	= place.getId()+" "+ place.getRate()+"\n";
					bwrite.write(line);
				}
				bwrite.write("\n");
			}
			
			
			bwrite.close();
			fwrite.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean readPlacesfromFile(String filename){
		File file 					= new File(filename);
		try {
//			FileReader fread 		= new FileReader(file);
//			BufferedReader bread	= new BufferedReader(fread);
//			
//			
//			bread.close();
//			fread.close();
			Scanner sc = new Scanner(file);
			int size = sc.nextInt();
			sc.nextLine();
			for(int i=0; i<size; i++){
				int userid = sc.nextInt();
				int placesize = sc.nextInt();
				sc.nextLine();
				
				MyUser user = users.get(userid);
				for(int j=0; j<placesize; j++){
					String id 	= sc.next();
					double rate = sc.nextDouble();
					sc.nextLine();
					MyPlace place = new MyPlace(id, rate);
					user.getPlaceList().add(place);
				}
				sc.nextLine();
			}
			
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e){
			e.printStackTrace();
		}
		return true;
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

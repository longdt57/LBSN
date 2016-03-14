package main;

import utilities.MyData;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyData brightkite_edge = new MyData();
		brightkite_edge.setUsersFromFileSource(MyData.FRIENDEDGE_FILENAME);
		brightkite_edge.setPlacesFromFileSource(MyData.CHECKIN_FILE);
		brightkite_edge.print();
	}

}

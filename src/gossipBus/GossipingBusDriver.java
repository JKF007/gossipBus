package gossipBus;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GossipingBusDriver {
	
	public static void main  (String args[])throws IOException{	
		
		BufferedReader inputFile = new BufferedReader(new FileReader("gossipTestInput.txt"));
		ArrayList<driver> driverList = new ArrayList<driver>();
		int driverCount = 0;
		HashMap <Integer, ArrayList<driver> > stops;
		
		while(inputFile.ready()){
			String line = inputFile.readLine();
			String [] inputTokens = line.split(" ");
			driverList.add(new driver(driverCount, inputTokens));
			driverCount ++;
		}
		
		inputFile.close();
		
		int maxStop= 480; // 480 minutes = 8 hours
		
		int currentStop = 1;
		boolean finished = false;
		while( currentStop <= maxStop && !finished){
			
			stops = new HashMap <Integer, ArrayList<driver> >();
			
			for (driver currentDriver : driverList){
				
				int driverCurrentLocation = currentDriver.currentStop();		
							
				if (!stops.containsKey(driverCurrentLocation) ){
					stops.put(driverCurrentLocation, new ArrayList<driver>() );				
				}
				stops.get(driverCurrentLocation).add(currentDriver);
				currentDriver.nextStop();				
			}	
			
			ArrayList < ArrayList<driver> > stoppedDriver = new ArrayList < ArrayList<driver> > (  stops.values() );
			
			for ( ArrayList<driver> talkingDrivers : stoppedDriver){
				
				driver firstDriver = talkingDrivers.get(0);
				
				for (int x = 1; x < talkingDrivers.size(); x++){										
					firstDriver.addGossip( talkingDrivers.get(x) );
				}
				
				for (int x = 1; x < talkingDrivers.size(); x++){
					driver tempDriver = talkingDrivers.get(x);
					tempDriver.addGossip(firstDriver);
				}						
			}		
			
			finished = true;
			for (driver currentDriver : driverList){
				if (currentDriver.getGossipsCount() < driverList.size()){
					finished = false;
				}					
			}
			
			if (finished){
				System.out.println(currentStop);
			}
		currentStop ++;	
		}//end of main loop
		
		if (!finished){
			System.out.println("never");
		}
		
		
	}//end of main
	
	static class driver{
		private int name;
		private HashSet knownGossip;
		private int [] route;
		private int currentStop;//refer to indices in the route array
		
		public driver(int inputName, String []inputRoute){
			currentStop = 0;
			name = inputName;
									
			route = new int [inputRoute.length];
			
			for (int x = 0; x < route.length; x++){
				route[x] = Integer.parseInt(inputRoute[x]);
			}	
			
			knownGossip = new HashSet();
			knownGossip.add(name);			
		}//end of assignment constructor
		
		public void addGossips(HashSet inputGossip){
			knownGossip.addAll(inputGossip);			
		} 
		
		public HashSet getGossips(){
			return knownGossip;			
		}
		
		public int getGossipsCount(){
			return knownGossip.size();
		}
		
		public int getName(){
			return name;
		}
		public void nextStop(){
			currentStop ++;
			if(currentStop >= route.length){
				currentStop=0;
			}
		}
		
		public int currentStop(){
			return route[currentStop];
		}
		
		public void addGossip(HashSet inputGossip){
			knownGossip.addAll(inputGossip);			
		}
		
		public void addGossip(driver otherDriver){
			this.addGossip( otherDriver.getGossips());			
		}
		
	}//end of class driver

}//end of class gossipingBusDriver

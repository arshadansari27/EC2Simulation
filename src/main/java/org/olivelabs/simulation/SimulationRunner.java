package org.olivelabs.simulation;

import java.util.Date;
import java.util.List;

public class SimulationRunner implements Runnable {

	public static volatile boolean running;

	@Override
	public void run() {
		running = true;
		Event event = null;
		EventGenerator.generateNextArrivalEvent();
		System.out.println("Simulation Begin Time : " + new Date());
		System.out.print("[");
		while ((event = EventManager.getInstance().getNextEvent()) != null) {
			event.processEvent();
		}
		System.out.println("]");
		System.out.println("Simulation End Time : " + new Date());
		for(String history : serverDetails()){
			System.out.println(history);
		}
		running = false;
	}

	public static double percentComplete(){
		return (EventGenerator.REQUEST_COUNT*1.0/EventGenerator.TOTAL_REQUEST) * 100;
	}

	public static List<String> serverDetails(){
		return ServerManager.getInstance().getServerHistories();
	}

}

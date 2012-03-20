package org.olivelabs.simulation;

public class EventGenerator {

	public static long TOTAL_REQUEST = 10000L;
	private static long REQUEST_COUNT = 0;
	
	public static void generateNextArrivalEvent(){
		if(REQUEST_COUNT == TOTAL_REQUEST){
			//EventManager.getInstance().addEvent(new TerminalEvent(Long.MAX_VALUE));
			return;
		}
		Request request = new Request();
		request.id = REQUEST_COUNT++;
		request.url = "/testUrl/"+ (REQUEST_COUNT%300);
		request.arrivalTime = getNextArrivalTime();
		request.serviceTime = getNextServiceTime();
		EventManager.getInstance().addEvent(new ArrivalEvent(request.arrivalTime, request));
	}
	
	public static void generateDispatchEvent(Request request, Server server){
		
		EventManager.getInstance().addEvent(new DispatchEvent(request.serviceBeginTime+request.serviceTime, request, server));
	}
	
	private static long getNextArrivalTime(){
		long interArrivalTime = (long)(Math.random()*10);
		
		return SimulationClock.CurrentTime + interArrivalTime;
	}
	
	private static long getNextServiceTime(){
		long serviceTime = (long)(Math.random()*10000);
		
		return serviceTime;
	}
}

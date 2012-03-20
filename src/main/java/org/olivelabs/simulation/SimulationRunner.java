package org.olivelabs.simulation;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimulationRunner implements Runnable {

	BlockingQueue<StatisticsCollector> statistics = new LinkedBlockingQueue<StatisticsCollector>();
	
	public SimulationRunner(BlockingQueue<StatisticsCollector> statistics){
		this.statistics = statistics;
	}
	
	@Override
	public void run() {
		Event event = null;
		EventGenerator.generateNextArrivalEvent();
		System.out.println("Simulation Begin Time : " + new Date());
		double percentComplete = 0.0;
		System.out.print("[");
		int lastPrintVal = 0;
		while ((event = EventManager.getInstance().getNextEvent()) != null) {
			
			if (event instanceof ArrivalEvent) {
				((ArrivalEvent) event).processEvent();
			} else{
				((DispatchEvent) event).processEvent();
			}
			StatisticsCollector data = new StatisticsCollector();
			data.simulationClock = SimulationClock.CurrentTime;
			data.requestDipatchedCount= OutputStatistics.REQUEST_DISPATCHED;
			data.requestRejectedCount = OutputStatistics.REQUEST_REJECTED;
			data.requestInWaitQueue = RequestWaitQueue.getInstance().size();
			data.serversInUse = ServerManager.getInstance().serversInUse.size();
			data.averageServiceTime = OutputStatistics.AVERAGE_SERVICE_TIME;
			data.averageWaitTime = OutputStatistics.AVERAGE_WAIT_TIME;
			this.statistics.add(data);
			percentComplete = (((OutputStatistics.REQUEST_REJECTED + OutputStatistics.REQUEST_DISPATCHED) * 1.0)  / EventGenerator.TOTAL_REQUEST) * 100;
			 
			if( lastPrintVal < (int) percentComplete %10){
				System.out.print("==");
				lastPrintVal = (int) percentComplete %10;
				
			}
		}
		System.out.println("]");
		System.out.println("Simulation End Time : " + new Date());
	}

}

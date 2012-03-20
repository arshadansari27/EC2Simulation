package org.olivelabs.simulation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DisplayOutput implements Runnable {

BlockingQueue<StatisticsCollector> statistics = new LinkedBlockingQueue<StatisticsCollector>();
	
	public DisplayOutput(BlockingQueue<StatisticsCollector> statistics){
		this.statistics = statistics;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.currentThread().sleep(5000);
				StatisticsCollector data = this.statistics.take();
				/*System.out.println("Clock : "+data.simulationClock
						+", Request Dipatched["+data.requestDipatchedCount+"], Rejected["+data.requestRejectedCount+"]"
						+", Avg Wait ["+data.averageWaitTime+"], Avg Service ["+data.averageServiceTime+"]"
						+", Servers ["+data.serversInUse+"], QueueSize["+data.requestInWaitQueue+"]"
						);*/
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

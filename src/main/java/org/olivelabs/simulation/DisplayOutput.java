package org.olivelabs.simulation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DisplayOutput implements Runnable {


	public DisplayOutput(){
	}

	@Override
	public void run() {
		int count = 0;
		while(SimulationRunner.running){
			try {
				Thread.sleep(1000);
				StatisticsCollector data = OutputStatistics.getStats();
				System.out.println(data);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

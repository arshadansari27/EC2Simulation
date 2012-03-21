package org.olivelabs.simulation;


public class DisplayOutput implements Runnable {

	SimulationRunner simulator;
	public DisplayOutput(SimulationRunner simulator){
		this.simulator = simulator;
	}

	@Override
	public void run() {
		int count = 0;
		while(simulator.RUNNING){
			try {
				Thread.sleep(1000);
				StatisticsCollector data = simulator.getRequestStats().getStats();
				System.out.println(data);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

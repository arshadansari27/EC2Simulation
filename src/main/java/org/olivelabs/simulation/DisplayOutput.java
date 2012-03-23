package org.olivelabs.simulation;


public class DisplayOutput implements Runnable {

	SimulationRunner simulator;
	public DisplayOutput(SimulationRunner simulator){
		this.simulator = simulator;
	}

	@Override
	public void run() {
		while(simulator.RUNNING.get()){
			try {
				Thread.sleep(1500);
				StatisticsCollector data = simulator.getRequestStats().getStats();
				System.out.println(data);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

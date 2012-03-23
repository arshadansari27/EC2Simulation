package org.olivelabs.simulation;


public class Main {


	public static void main(String[] args) throws Exception{

		Parameters params = new Parameters();

		params.MAX_CLOCK = 3600*24L;
		SimulationRunner simulator = new SimulationRunner(params);
		simulator.start();
	}

}

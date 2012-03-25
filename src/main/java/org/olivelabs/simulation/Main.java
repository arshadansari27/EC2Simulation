package org.olivelabs.simulation;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


public class Main {


    static Logger log = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws Exception{

		Parameters params = new Parameters();

		long time = System.currentTimeMillis();
		params.MAX_CLOCK = 24 * 3600*1L;
		SimulationRunner simulator = new SimulationRunner(params);
		simulator.start();
		simulator.getStatistics();
		time= System.currentTimeMillis() - time;
		log.info(String.format("Simulation time : %d sec [%d]\t",TimeUnit.SECONDS.convert(time,TimeUnit.MILLISECONDS),time));

	}

}

package org.olivelabs.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventGenerator.TOTAL_REQUEST = 10000000L;
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		executor.execute(new SimulationRunner());
		executor.execute(new DisplayOutput());
		executor.shutdown();
	}

}

package org.olivelabs.simulation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class OutputStatistics {
	public volatile BigInteger REQUEST_DISPATCHED = new BigInteger("0");
	public volatile BigInteger REQUEST_REJECTED = new BigInteger("0");
	public volatile BigInteger TOTAL_SERVICE_TIME = new BigInteger("0");
	public volatile BigInteger TOTAL_WAIT_TIME = new BigInteger("0");
	public volatile double AVERAGE_WAIT_TIME = 0D;
	public volatile double AVERAGE_SERVICE_TIME = 0D;
	SimulationRunner simulator;

	public OutputStatistics(SimulationRunner simulator){
		this.simulator = simulator;
	}
	public void collectStatisticsForDispatched(Request request){
		REQUEST_DISPATCHED = REQUEST_DISPATCHED.add(BigInteger.ONE);
		TOTAL_SERVICE_TIME = TOTAL_SERVICE_TIME.add(request.serviceTime);
		TOTAL_WAIT_TIME = TOTAL_WAIT_TIME.add(request.waitTime());

		AVERAGE_WAIT_TIME    = (new BigDecimal(TOTAL_WAIT_TIME   ).divide(new BigDecimal( REQUEST_DISPATCHED), RoundingMode.HALF_UP)).doubleValue();
		AVERAGE_SERVICE_TIME = (new BigDecimal(TOTAL_SERVICE_TIME).divide(new BigDecimal( REQUEST_DISPATCHED), RoundingMode.HALF_UP)).doubleValue();
	}
	public void collectStatisticsForRejected(Request request){
			REQUEST_REJECTED.add(BigInteger.ONE);
	}

	public  StatisticsCollector getStats(){
		StatisticsCollector data = new StatisticsCollector();
		data.simulationClock = simulator.getClock().CurrentTime;
		data.requestDipatchedCount= REQUEST_DISPATCHED;
		data.requestRejectedCount = REQUEST_REJECTED;
		data.requestInWaitQueue = simulator.getWaitQueue().size();
		data.serversInUse = simulator.getServerManager().serversInUse.size();
		data.averageServiceTime = AVERAGE_SERVICE_TIME;
		data.averageWaitTime = AVERAGE_WAIT_TIME;
		return data;
	}
}

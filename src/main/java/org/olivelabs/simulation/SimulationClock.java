package org.olivelabs.simulation;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SimulationClock {
	//public volatile BigInteger CurrentTime = new BigInteger("0");
	public volatile AtomicReference<BigInteger> CurrentTime = new AtomicReference<BigInteger>(new BigInteger("0"));
}

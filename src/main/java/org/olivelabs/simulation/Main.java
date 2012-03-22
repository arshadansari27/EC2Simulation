package org.olivelabs.simulation;

import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class Main {

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws NullPointerException
	 * @throws MalformedObjectNameException
	 * @throws NotCompliantMBeanException
	 * @throws MBeanRegistrationException
	 * @throws InstanceAlreadyExistsException
	 */
	public static void main(String[] args) throws Exception{

		//MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

		Parameters params = new Parameters();
		params.MAX_CLOCK = new BigInteger("100000000");
		SimulationRunner simulator = new SimulationRunner(params);
		simulator.start();
	}

}

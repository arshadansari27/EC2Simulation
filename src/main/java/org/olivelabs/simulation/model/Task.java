package org.olivelabs.simulation.model;

import org.olivelabs.simulation.Parameters;
import org.olivelabs.simulation.StatisticsCollector;

public class Task {
	
	public String taskId;
	public String status;
	public String label;
	public String description;
	public Parameters inputData;
	public StatisticsCollector outputData;
}

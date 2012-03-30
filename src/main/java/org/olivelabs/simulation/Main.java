package org.olivelabs.simulation;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.olivelabs.simulation.model.Task;
import org.olivelabs.util.integration.RedisListener;
import org.olivelabs.util.marshalling.JSONUtil;

import redis.clients.jedis.Jedis;


public class Main {


    static Logger log = Logger.getLogger(Main.class.getName());
    
	public static void main(String[] args) throws Exception{

		Parameters params = new Parameters();
		if(args.length < 1){
			log.error("Invalid input parameters");
			return;
		}
		Jedis jedis = new Jedis("localhost");
//		Thread t = new Thread(new Runnable(){
//			public void run(){
//				System.out.println("PUblishing...");
//				try {
//					synchronized(this){
//						wait(1000);
//					}
//				} catch (InterruptedException e) {
//				}
//				System.out.println("really publishin...");
//				
//				Jedis jedis = new Jedis("localhost");
//				for(int i =1 ; i<=3; i++){
//					Parameters params = new Parameters();
//					params.MAX_CLOCK = (new Random().nextInt(3)+1) * 3600L;
//					jedis.publish("simulation", new String(JSONUtil.objectToJson(params)));
//				}
//			}
//		});
//		t.start();
		System.out.println("Listening...");
		RedisListener listener = new RedisListener();
		jedis.subscribe(listener, "simulation");
		
		
	}
	
	public static void startSimulation(Parameters params){
		Jedis jedis = new Jedis("localhost");
		long time = System.currentTimeMillis();
//		if(!jedis.exists(params.taskId)){
//			Task t  = new Task();
//			t.taskId = params.taskId;
//			t.inputData = params;
//			jedis.set(new String(t.taskId),new String(JSONUtil.objectToJson(t)));
//		}
		String taskString = jedis.get(params.taskId);
		Task task = null;
		if(taskString != null || !("").equals(taskString)){
			task = (Task) JSONUtil.jsonToObject(taskString.getBytes(), Task.class);
		}
		else{
			log.error("Task is not found!");
			return;
		}
		SimulationRunner simulator = new SimulationRunner(params);
		StatisticsCollector stats = null;
		try{
			task.status = "RUNNING...";
			jedis.set(task.taskId,new String(JSONUtil.objectToJson(task)));
			simulator.start();
			stats = simulator.getStatistics();
			
		}
		catch(Exception e){
			log.error(e);
		}
		task.status = (stats!=null)?"DONE":"FAILED";
		task.outputData = stats;
		jedis.set(params.taskId,new String(JSONUtil.objectToJson(task)));
		time= System.currentTimeMillis() - time;
		log.info(String.format("Simulation time : %d sec [%d]\t",TimeUnit.SECONDS.convert(time,TimeUnit.MILLISECONDS),time));
		
		

	}

}

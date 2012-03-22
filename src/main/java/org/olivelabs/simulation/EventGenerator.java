package org.olivelabs.simulation;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventGenerator implements Runnable{

    public long totalRequest = 10000L;
    public long requestCount = 0;
    private SimulationRunner simulator;
    AtomicBoolean stopGenerating = new AtomicBoolean();
    public EventGenerator(SimulationRunner simulator){
        this.simulator = simulator;
    	stopGenerating.set(false);

    }

    public synchronized void generateNextArrivalEvent(){

        if(simulator.getClock().CurrentTime.get().compareTo(simulator.params.MAX_CLOCK) >=0){
        	simulator.getEventManager().addEvent(new TerminalEvent(simulator.params.MAX_CLOCK));
        	//Logger.log(this.getClass() + ": Generated terminal event",1);
        	stopGenerating.set(true);
            return;
        }
        Request request = new Request();
        request.id = requestCount++;
        request.url = "/testUrl/"+ (requestCount%300);
        request.arrivalTime = getNextArrivalTime();
        request.serviceTime = getNextServiceTime();
        simulator.getEventManager().addEvent(new ArrivalEvent(request.arrivalTime, request, simulator));
        //Logger.log(this.getClass() + ": Generated arrival event",10000);
    }

    public synchronized void generateDispatchEvent(Request request, Server server){

    	BigInteger dispatchTime = new BigInteger(request.serviceBeginTime.toByteArray()).add(request.serviceTime);
    	simulator.getEventManager().addEvent(new DispatchEvent(dispatchTime, request, server, simulator));
    	//Logger.log(this.getClass() + ": Generated dispatch event",10000);
    }

    private BigInteger getNextArrivalTime(){

        BigInteger interArrivalTime = new BigInteger((long)(Math.random()*10) +"");
        return interArrivalTime.add(simulator.getClock().CurrentTime.get());
    }

    private BigInteger getNextServiceTime(){
    	BigInteger serviceTime = new BigInteger((long)(Math.random()*1000000) +"");

        return serviceTime;
    }

	@Override
	public void run() {
		while(simulator.RUNNING.get() && !stopGenerating.get()){
			if(simulator.getEventManager().getCurrentArrivalCount() > 5000){
				synchronized(this){
					try {
						wait(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			generateNextArrivalEvent();
		}

	}
}

package org.olivelabs.simulation;

import java.math.BigInteger;

public class EventGenerator {

    public long totalRequest = 10000L;
    public long requestCount = 0;
    private SimulationRunner simulator;

    public EventGenerator(SimulationRunner simulator){
        this.simulator = simulator;
    }

    public synchronized void generateNextArrivalEvent(){
        if(simulator.getClock().CurrentTime.get().compareTo(simulator.params.MAX_CLOCK) >=0){
        	simulator.getEventManager().addEvent(new TerminalEvent(simulator.params.MAX_CLOCK));
            return;
        }
        Request request = new Request();
        request.id = requestCount++;
        request.url = "/testUrl/"+ (requestCount%300);
        request.arrivalTime = getNextArrivalTime();
        request.serviceTime = getNextServiceTime();
        simulator.getEventManager().addEvent(new ArrivalEvent(request.arrivalTime, request, simulator));
    }

    public synchronized void generateDispatchEvent(Request request, Server server){

    	BigInteger dispatchTime = new BigInteger(request.serviceBeginTime.toByteArray()).add(request.serviceTime);
        simulator.getEventManager().addEvent(new DispatchEvent(dispatchTime, request, server, simulator));
    }

    private BigInteger getNextArrivalTime(){

        BigInteger interArrivalTime = new BigInteger((long)(Math.random()*10) +"");
        return interArrivalTime.add(simulator.getClock().CurrentTime.get());
    }

    private BigInteger getNextServiceTime(){
    	BigInteger serviceTime = new BigInteger((long)(Math.random()*10000) +"");

        return serviceTime;
    }
}

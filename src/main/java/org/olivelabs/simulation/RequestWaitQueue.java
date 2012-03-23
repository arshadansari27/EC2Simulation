package org.olivelabs.simulation;

import java.util.ArrayList;

public class RequestWaitQueue {
	ArrayList<Request> queue;
	static RequestWaitQueue requestWaitQueue = null;

	public RequestWaitQueue() {
		queue = new ArrayList<Request>();
	}



	public synchronized void add(Request request){
		if(request==null){
			throw new RuntimeException("You messed with the null request!!");
		}
		queue.add(request);
	}

	public synchronized Request get(){
		if(queue.isEmpty()){
			return null;
		}
		return queue.remove(0);
	}

	public synchronized int size(){
		return queue.size();
	}
}
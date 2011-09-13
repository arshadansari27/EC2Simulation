package org.olivelabs.simulation;

import java.util.ArrayList;

public class RequestWaitQueue {
	ArrayList<Request> queue;
	static RequestWaitQueue requestWaitQueue = null;
	
	private RequestWaitQueue() {
		queue = new ArrayList<Request>();
	}

	public static RequestWaitQueue getInstance() {
		if (requestWaitQueue == null) {
			requestWaitQueue = new RequestWaitQueue();
		}
		return requestWaitQueue;
	}
	
	public void add(Request request){
		
		queue.add(request);
	}
	
	public Request get(){
		return queue.remove(0);
	}
	
	public int size(){
		return queue.size();
	}
}
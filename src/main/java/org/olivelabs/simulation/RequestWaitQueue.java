package org.olivelabs.simulation;

import java.util.ArrayList;

public class RequestWaitQueue {
	ArrayList<Request> queue;
	static RequestWaitQueue requestWaitQueue = null;

	public RequestWaitQueue() {
		queue = new ArrayList<Request>();
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
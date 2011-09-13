package org.olivelabs.simulation;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RequestWaitQueueTest {

	RequestWaitQueue queue;
	@Before
	public void setUp() throws Exception {
		queue = RequestWaitQueue.getInstance();
		queue.queue.clear();
	}

	@Test
	public void testQueue(){
		Request request = null;
		for(int i = 0; i < 100; i++){
			request = new Request();
			request.id = i;
			request.url ="url";
			queue.add(request);
		}
		long currentId = -1;
		Request request2 = null;
		while( queue.size() > 0 && (( request2 = queue.get())!=null)){
			Assert.assertTrue(currentId<request2.id);
			currentId = request2.id;
		}
	}
	
	@After
	public void tearDown() throws Exception {
	}

}

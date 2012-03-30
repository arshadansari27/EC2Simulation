package org.olivelabs.util.marshalling;

import static org.junit.Assert.*;

import org.junit.Test;
import org.olivelabs.simulation.Parameters;

public class JSONUtilTest {

	@Test
	public void test() {
		Parameters pars = new Parameters();
		
		System.out.println(new String(JSONUtil.objectToJson(pars)));
		Parameters pars2 = (Parameters) JSONUtil.jsonToObject(JSONUtil.objectToJson(pars), Parameters.class);
		System.out.println(pars2.MAX_CLOCK);
	}

}

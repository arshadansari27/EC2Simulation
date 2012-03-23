package org.olivelabs.simulation;

import java.util.HashMap;
import java.util.Map;

public class Logger {
	static Map<Class, Object> logFiles = new HashMap<Class, Object>();
	static {

	}
	public static void log(Class className, String message, int oneOutOf){
		if( false && className.equals(EventGenerator.class) && (oneOutOf==1 || (long)(Math.random()*oneOutOf) == 1))
			System.out.println(className + "   " +message);
	}
}

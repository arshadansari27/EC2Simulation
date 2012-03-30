package org.olivelabs.util.integration;

import org.apache.log4j.Logger;
import org.olivelabs.simulation.Main;
import org.olivelabs.simulation.Parameters;
import org.olivelabs.util.marshalling.JSONUtil;

import redis.clients.jedis.JedisPubSub;

public class RedisListener extends JedisPubSub {
	static Logger log = Logger.getLogger(RedisListener.class.getName());
    public void onMessage(String channel, String message) {
    	log.info(message);
    	byte[] buff = message.getBytes();
    	Parameters params = (Parameters) JSONUtil.jsonToObject(buff, Parameters.class);
    	Main.startSimulation(params);
    	log.debug(params.MAX_CLOCK);
    }

    public void onSubscribe(String channel, int subscribedChannels) {
    }

    public void onUnsubscribe(String channel, int subscribedChannels) {
    }

    public void onPSubscribe(String pattern, int subscribedChannels) {
    }

    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    }

    public void onPMessage(String pattern, String channel,
        String message) {
    }
}
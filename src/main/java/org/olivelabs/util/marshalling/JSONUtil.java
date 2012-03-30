package org.olivelabs.util.marshalling;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JSONUtil {

	public static Object jsonToObject(byte[] json, Class cls) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Object object = mapper.readValue(json, cls);
			return object;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] objectToJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			mapper.writeValue(stream, obj);
			return stream.toByteArray();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

package org.caiedea.cryptopression.encrypt;

import java.util.HashMap;
import java.util.Map;

public final class EncryptorConfig {
	private Map<String,Object> config = new HashMap<String,Object>();
	
	public int getIntAttribute(String key) {
		int value = ((Integer)config.get(key)).intValue();
		return value;
	}
	
	public String getStringAttribute(String key) {
		return (String)config.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getTypedAttribute(String key, T t) {
		return (T) config.get(key);
	}
	
	public void setIntAttribute(String key, int value) {
		config.put(key, value);
	}
	
	public void setStringAttribute(String key, String value) {
		config.put(key, value);
	}
	
	public <T> void setTypedAttribute(String key, T value) {
		config.put(key, value);
	}
	
}

package org.caiedea.cryptopression;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.caiedea.cryptopression.encrypt.EncryptorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Utils {
	private static final Logger log = LoggerFactory.getLogger(Utils.class);
	private static final int MIN_ITERATIONS = 1024;
	private static Properties propCache;
	static {
		InputStream propStream =
				Utils.class.getResourceAsStream("cryptopression.properties");
		propCache = new Properties();
		try {
			propCache.load(propStream);
		}
		catch(IOException ioe) {
			log.error("Properties file is MISSING!!!");
			throw new RuntimeException(ioe);
		}
	}
	
	/**
	 * Provides raw access to the libraries properties is the
	 * <code>Encryptor</code> implementation would rather not use the
	 * <code>seedConfigWithProperties</code> helper method.
	 * @return a non-defensively copied instance of the libraries properties
	 */
	public static Properties getProperties() {
		return propCache;
	}
	
	/**
	 * Generates to salt to use for encryption.
	 * @param byteLength size of the salt in bytes
	 * @return the salt
	 */
	public static byte[] generateSalt(final int byteLength) {
		byte[] salt = new byte[byteLength];
		SecureRandom secRand = new SecureRandom();
		secRand.nextBytes(salt);
		return salt;
	}
	
	/**
	 * Randomly selects a number of iterations to perform during
	 * encryption between a pre-defined minimum and the maximum indicated
	 * by the property <code>cryptopression.iterations</code>.
	 * @param upperBound
	 * @return the number iterations to use during encryption
	 */
	public static int generateIterations(int upperBound) {
		if (upperBound < MIN_ITERATIONS) return MIN_ITERATIONS;
		int iterations =
				MIN_ITERATIONS +
					(int)(Math.random() * ((upperBound - MIN_ITERATIONS) + 1));
		return iterations;
	}
	
	/**
	 * Takes everything defined in the the libraries property file and drops
	 * it into the specified <code>EncryptorConfig</code>. Each of the
	 * <code>Encryptor</code> implementations are the only entities that have
	 * the knowledge of its internal implementation enough to configure itself.
	 * @param config
	 */
	public static void seedConfigWithProperties(EncryptorConfig config) {
		Set<Map.Entry<Object, Object>> propEntries = propCache.entrySet();
		for(Map.Entry<Object, Object> entry : propEntries) {
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			try {
				int i = Integer.parseInt(val);
				config.setIntAttribute(key, i);
			}
			catch(NumberFormatException notAnIntValue) {
				config.setStringAttribute(key, val);
			}
		}
	}
	
}

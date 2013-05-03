package org.caiedea.cryptopression;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.caiedea.cryptopression.decrypt.DecryptorConfig;
import org.caiedea.cryptopression.encrypt.EncryptorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Utils {
	private static final Logger log = LoggerFactory.getLogger(Utils.class);
	private static final String PROPS_FILE_PATH = "/cryptopression.properties";
	private static final String CONFIG_KEEP_RAW_ENC = "cryptopression.keepRawInput";
	private static final int MIN_ITERATIONS = 1024;
	private static Properties propCache;
	static {
		InputStream propStream =
				Utils.class.getResourceAsStream(PROPS_FILE_PATH);
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
	 * Convenience method that determines whether the decryptors input
	 * should be deleted after use or maintained.
	 * @param file
	 * @param config
	 */
	public static void deleteDecryptorInputFile(File file, DecryptorConfig config) {
		boolean shouldDelete = config.getIntAttribute(CONFIG_KEEP_RAW_ENC) == 0;
		Utils.deleteInputFileIfNeeded(file, shouldDelete);
	}
	
	/**
	 * Convenience method that determines whether the encryptors input
	 * should be deleted after use or maintained.
	 * @param file
	 * @param config
	 */
	public static void deleteEncryptorInputFile(File file, EncryptorConfig config) {
		boolean shouldDelete = config.getIntAttribute(CONFIG_KEEP_RAW_ENC) == 0;
		Utils.deleteInputFileIfNeeded(file, shouldDelete);
	}
	
	/*
	 * Actually performs the deletion of the input data.
	 */
	private static void deleteInputFileIfNeeded(File file, boolean shouldDelete) {
		if (shouldDelete) {
			boolean deleteSuccessful = file.delete();
			if (!deleteSuccessful) {
				String msg =
						String.format("Input file (%s) could NOT be deleted even though deletion was requested.", file.getAbsolutePath());
				log.error(msg);
			}
		}
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
	 * Provided so that programmatic changes to the configuration are possible.
	 * <p>
	 * If the key provided is already associated with a property that key/value
	 * pair are removed before adding the key/value pair specified here.
	 * <p>
	 * NOTE: Changes will be reflected in newly instantiated
	 * <code>Encryptor</code> and <code>Decryptor</code> classes.
	 * @param key the key of the property to add
	 * @param value the value of the property to add
	 */
	public static void propertyOverride(String key, String value) {
		if (propCache.containsKey(key)) {
			propCache.remove(key);
		}
		propCache.put(key, value);
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

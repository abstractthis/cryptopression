package org.caiedea.cryptopression.decrypt;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Decryptor<T> {
	private static Logger log = LoggerFactory.getLogger(Decryptor.class);
	// Check to make sure that the JRE supports the encryption strength we must use
	static {
		try {
			int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
			if (maxKeyLen == 128) {
				log.error("JCE Policy Files need to be upgraded to UNLIMITED. Max Key Length is only 128-bit!!");
				Decryptor.UNLIMITED_JCE_POLICY_ENABLED = false;
			}
		}
		catch(NoSuchAlgorithmException algoEx) {
			log.error("Java Cryptography Extension version is dated or not available!!!");
			throw new RuntimeException(algoEx);
		}
	}
	protected static final String CHARSET_KEY = "config.charset";
	protected static boolean UNLIMITED_JCE_POLICY_ENABLED = true;
	
	protected DecryptorConfig config;
	protected InputStream inStream;
	protected OutputStream outStream;
	
	/**
	 * Forces configuration to be performed.
	 */
	public Decryptor() {
		this.configure();
	}
	
	protected void configure() {
		this.config = this.configSpecifics();
		String charsetName = config.getStringAttribute("cryptopression.charset");
		Charset chSet = charsetName == null ? 
				Charset.forName("UTF-8") : Charset.forName(charsetName.toUpperCase());
		this.config.setTypedAttribute(CHARSET_KEY, chSet);
	}
	
	protected abstract DecryptorConfig configSpecifics();
	
	protected abstract void readHeader(T t);
	
	public abstract T decrypt();
}

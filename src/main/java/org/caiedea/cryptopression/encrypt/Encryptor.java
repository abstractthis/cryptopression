package org.caiedea.cryptopression.encrypt;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the base class for all encryption implementations. The
 * decorator pattern is used to provide encryption capabilities and
 * all of the decorators must extend this class.
 * @author dsmith
 *
 */
public abstract class Encryptor<T> {
	private static Logger log = LoggerFactory.getLogger(Encryptor.class);
	// Check to make sure that the JRE supports the encryption strength we must use
	static {
		try {
			int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
			if (maxKeyLen != 128) {
				log.error("JCE Policy Files need to be upgraded to UNLIMITED. Max Key Length is only 128-bit!!");
				Encryptor.UNLIMITED_JCE_POLICY_ENABLED = true;
			}
		}
		catch(NoSuchAlgorithmException algoEx) {
			log.error("Java Cryptography Extension version is dated or not available!!!");
			throw new RuntimeException(algoEx);
		}
	}
	protected static final String CHARSET_KEY = "config.charset";
	protected static boolean UNLIMITED_JCE_POLICY_ENABLED = false;
	
	protected EncryptorConfig config;
	protected InputStream inStream;
	protected OutputStream outStream;
	
	/**
	 * Forces configuration to be performed.
	 */
	public Encryptor() {
		this.configure();
	}
	
	protected void configure() {
		this.config = this.configSpecifics();
		String charsetName = config.getStringAttribute("cryptopression.charset");
		Charset chSet = charsetName == null ? 
				Charset.forName("UTF-8") : Charset.forName(charsetName.toUpperCase());
		config.setTypedAttribute(CHARSET_KEY, chSet);
	}
	
	/**
	 * Called to initialize the <code>Encryptor</code>s attributes needed
	 * to perform the encryption. Because the attributes needed are implementation
	 * specific the <code>EncryptorConfig</code> is a basic key-value map.
	 */
	protected abstract EncryptorConfig configSpecifics();
	
	/**
	 * Actually performs the encryption of the data specified by
	 * the call to <code>setEncryptTarget</code>. The return type
	 * is specified by the parameterized implementation of this
	 * abstract class. No checks are performed for "proper" return
	 * types so the decorators are responsible for providing the
	 * client a useful result type. As of this writing the thought
	 * is that one of three return types will be typical:
	 * <ol>
	 * <li>String</li>
	 * <li>File</li>
	 * <li>byte[]</li>
	 * </ol>
	 * 
	 * @return determined by concrete implementation
	 */
	public abstract T encrypt();
	
	/**
	 * Provide the data that needs to be encrypted by this
	 * <code>Encryptor</code>.
	 * @param is a stream to any data source.
	 */
	public void setEncryptTarget(InputStream is) {
		this.inStream = is;
	}
	
	/**
	 * Provide the data that needs to be encrypted by this
	 * <code>Encryptor</code>.
	 * <p>
	 * The default implementation converts the provided <code>String</code>
	 * into its byte array representation (UTF-8 charset) and initialize the
	 * <code>Encryptor</code>s InputStream to be a <code>ByteArrayInputStream</code>
	 * pointing at the byte array representation of the <code>String</code>.
	 * @param s text that needs to be encrypted.
	 */
	public void setEncryptTarget(String s) {
		final Charset charSet = config.getTypedAttribute(CHARSET_KEY, Charset.defaultCharset());
		byte[] strUtf8Bytes = s.getBytes(charSet);
		ByteArrayInputStream bis = new ByteArrayInputStream(strUtf8Bytes);
		this.setEncryptTarget(bis);
	}
}

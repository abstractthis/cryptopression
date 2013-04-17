package org.caiedea.cryptopression.decrypt.aes;

import org.caiedea.cryptopression.Utils;
import org.caiedea.cryptopression.decrypt.Decryptor;
import org.caiedea.cryptopression.decrypt.DecryptorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AesDecryptor<T> extends Decryptor<T> {
	private static final Logger log = LoggerFactory.getLogger(AesDecryptor.class);
	
	/* Shared config keys for class hierarchy */
	protected static final String SALT_SIZE_KEY = "cryptopression.saltLength";
	protected static final String ITERATIONS_KEY = "cryptopression.iterations";
	protected static final String KEY_BIT_LENGTH_KEY = "cryptopression.keyLength";
	protected static final String DEC_PWD_KEY = "cryptopression.password";
	protected static final String DEC_ALGO_KEY = "cryptopression.algorithm";
	protected static final String SECRET_TYPE = "PBKDF2WithHmacSHA1";
	protected static final String CIPHER_TYPE = "AES/CBC/PKCS5Padding";
	protected static final String CONFIG_SALT_KEY = "aes.salt";
	protected static final String CONFIG_CIPHER_KEY = "aes.decrypt.cipher";
	protected static final String CONFIG_INITVEC_KEY = "aes.initVector";
	protected static final String CONFIG_FILE_BUFFER_SIZE = "cryptopression.fileBuffer";
	
	protected Decryptor<T> decryptor;
	
	public AesDecryptor(Decryptor<T> d) {
		super();
		this.decryptor = d;
	}
	
	@Override
	protected DecryptorConfig configSpecifics() {
		DecryptorConfig decConfig = new DecryptorConfig();
		Utils.seedConfigWithProperties(decConfig);
		String algo = decConfig.getStringAttribute(DEC_ALGO_KEY).toUpperCase();
		if (!"AES".equals(algo)) {
			String msg = String.format("Wrong algorithm implementation being used. Requested: %s Used: AES", algo);
			throw new RuntimeException(msg);
		}
		return decConfig;
	}
	
	@Override
	protected void readHeader(T t) {
		// NOP - Default is to do nothing
	}

	@Override
	public T decrypt() {
		log.error("AES decryption base class has no implementation; use one of the subclasses.");
		return null;
	}

}

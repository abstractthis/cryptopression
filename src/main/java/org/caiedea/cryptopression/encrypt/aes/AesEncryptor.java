package org.caiedea.cryptopression.encrypt.aes;

import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.caiedea.cryptopression.Utils;
import org.caiedea.cryptopression.encrypt.Encryptor;
import org.caiedea.cryptopression.encrypt.EncryptorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AesEncryptor<T> extends Encryptor<T> {
	private static final Logger log = LoggerFactory.getLogger(AesEncryptor.class);
	private static final String SALT_SIZE_KEY = "cryptopression.saltLength";
	private static final String ITERATIONS_MAX_KEY = "cryptopression.iterations";
	private static final String KEY_BIT_LENGTH_KEY = "cryptopression.keyLength";
	private static final String ENC_PWD_KEY = "cryptopression.password";
	private static final String ENC_ALGO_KEY = "cryptopression.algorithm";
	private static final String SECRET_TYPE = "PBKDF2WithHmacSHA1";
	private static final String CIPHER_TYPE = "AES/CBC/PKCS5Padding";
	protected static final String CONFIG_SALT_KEY = "aes.salt";
	protected static final String CONFIG_ITER_KEY = "aes.iterations";
	protected static final String CONFIG_CIPHER_KEY = "aes.encrypt.cipher";
	protected static final String CONFIG_INITVEC_KEY = "aes.initVector";
	protected static final String CONFIG_FILE_BUFFER_SIZE = "cryptopression.fileBuffer";
	
	protected Encryptor<T> encryptor;
	
	public AesEncryptor(Encryptor<T> e) {
		super();
		encryptor = e;
	}
	
	@Override
	protected EncryptorConfig configSpecifics() {
		// Build up the configuration that AES needs
		EncryptorConfig aesConfig = new EncryptorConfig();
		Utils.seedConfigWithProperties(aesConfig);
		int saltByteLength = aesConfig.getIntAttribute(SALT_SIZE_KEY);
		byte[] salt = Utils.generateSalt(saltByteLength);
		aesConfig.setTypedAttribute(CONFIG_SALT_KEY, salt);
		int maxIterations = aesConfig.getIntAttribute(ITERATIONS_MAX_KEY);
		int iterations = Utils.generateIterations(maxIterations);
		aesConfig.setIntAttribute(CONFIG_ITER_KEY, iterations);
		
		// Create the Ciphers and the Initialization Vector and place
		// in the configuration
		try {
			SecretKeyFactory secretFactory = SecretKeyFactory.getInstance(SECRET_TYPE);
			int keyBitLength = aesConfig.getIntAttribute(KEY_BIT_LENGTH_KEY);
			String password = aesConfig.getStringAttribute(ENC_PWD_KEY);
			String algo = aesConfig.getStringAttribute(ENC_ALGO_KEY).toUpperCase();
			KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyBitLength);
			SecretKey encodingKey = secretFactory.generateSecret(keySpec);
			SecretKey actualSecret = new SecretKeySpec(encodingKey.getEncoded(), algo);
			Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
			cipher.init(Cipher.ENCRYPT_MODE, actualSecret);
			AlgorithmParameters algoParams = cipher.getParameters();
			byte[] initVector = algoParams.getParameterSpec(IvParameterSpec.class).getIV();
			aesConfig.setObjectAttribute(CONFIG_CIPHER_KEY, cipher);
			aesConfig.setTypedAttribute(CONFIG_INITVEC_KEY, initVector);
		}
		catch(NoSuchAlgorithmException algoEx) {
			log.error("Algorithm specified NOT supported!");
			throw new RuntimeException(algoEx);
		}
		catch(InvalidKeySpecException keySpecEx) {
			log.error("Key Spec specified NOT supported!");
			throw new RuntimeException(keySpecEx);
		}
		catch(NoSuchPaddingException paddingEx) {
			log.error("Cipher padding specified NOT supported!");
			throw new RuntimeException(paddingEx);
		}
		catch(InvalidKeyException badKeyEx) {
			log.error("The key being used is invalid!");
			throw new RuntimeException(badKeyEx);
		}
		catch(InvalidParameterSpecException badSpecEx) {
			log.error("The init vector spec is invalid!");
			throw new RuntimeException(badSpecEx);
		}
		
		return aesConfig;
	}

	@Override
	public T encrypt() {
		log.error("AES encryption base class has no implementation; use one of the subclasses.");
		return null;
	}
}

package org.caiedea.cryptopression.decrypt.aes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.caiedea.cryptopression.Utils;
import org.caiedea.cryptopression.decrypt.Decryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileAesDecryptor extends AesDecryptor<File> {
	private static final Logger log = LoggerFactory.getLogger(FileAesDecryptor.class);
	
	private File decryptedFile;
	private FileInputStream fis;

	public FileAesDecryptor(Decryptor<File> fileDecryptor, File file) {
		super(fileDecryptor);
		this.decryptedFile = file;
		this.initAndSetStreams(file);
	}
	
	public FileAesDecryptor(Decryptor<File> fileDecryptor, String path) {
		this(fileDecryptor, new File(path));
	}
	
	private void initAndSetStreams(File file) {
		try {
			this.outStream = new FileOutputStream(file);
		}
		catch(FileNotFoundException file404Ex) {
			log.error("File specified for decryption output not found!!");
			throw new RuntimeException(file404Ex);
		}
	}
	
	@Override
	protected void readHeader(File target) {
		// Header size is always the first 4 bytes
		byte[] rawHeaderSize = new byte[4];
		try {
			fis.read(rawHeaderSize);
			int headerSize = ByteBuffer.wrap(rawHeaderSize).getInt();
			byte[] fullHeader = new byte[headerSize];
			fis.read(fullHeader);
			int saltSize = config.getIntAttribute(SALT_SIZE_KEY);
			byte[] salt = new byte[saltSize];
			System.arraycopy(fullHeader, 0, salt, 0, saltSize);
			this.config.setTypedAttribute(CONFIG_SALT_KEY, salt);
			byte[] initVec = new byte[fullHeader.length - saltSize];
			System.arraycopy(fullHeader, saltSize, initVec, 0, initVec.length);
			this.config.setTypedAttribute(CONFIG_INITVEC_KEY, initVec);
		}
		catch(IOException ioe) {
			log.error("Problems reading file header!!");
			throw new RuntimeException(ioe);
		}
	}
	
	@Override
	public File decrypt() {
		CipherInputStream cis = null;
		try {
			File targetFile = this.decryptor.decrypt();
			fis = new FileInputStream(targetFile);
			this.finalizeConfiguration(targetFile);
			Cipher cipher = (Cipher) this.config.getObjectAttribute(CONFIG_CIPHER_KEY);
			cis = new CipherInputStream(fis, cipher);
			int bytesRead = 0;
			int bufferSize = this.config.getIntAttribute(CONFIG_FILE_BUFFER_SIZE);
			byte[] buffer = new byte[bufferSize];
			
			while ((bytesRead = cis.read(buffer)) != -1) {
				this.outStream.write(buffer, 0, bytesRead);
				log.debug("Bytes decrypted: " + bytesRead);
			}
			Utils.deleteDecryptorInputFile(targetFile, this.config);
		}
		catch(FileNotFoundException file404Ex) {
			log.error("File specified for decryption not found!!");
			throw new RuntimeException(file404Ex);
		}
		catch(IOException ioe) {
			log.error("IO issue while trying to decrypt!!");
			throw new RuntimeException(ioe);
		}
		finally {
			try {
				if (cis != null) cis.close();
				if (this.outStream != null ) {
					this.outStream.flush();
					this.outStream.close();
				}
			}
			catch(IOException ioe) {
				log.error("Problems trying to cleanup the decryptor!!");
			}
		}
		return decryptedFile;
	}
	
	protected void finalizeConfiguration(File target) {
		this.readHeader(target);
		try {
			int keyBitLength = this.config.getIntAttribute(KEY_BIT_LENGTH_KEY);
			String password = this.config.getStringAttribute(DEC_PWD_KEY);
			char[] pwdRaw = password.toCharArray();
			byte[] salt = this.config.getTypedAttribute(CONFIG_SALT_KEY, new byte[0]);
			int iterations = this.config.getIntAttribute(ITERATIONS_KEY);
			KeySpec keySpec = new PBEKeySpec(pwdRaw, salt, iterations, keyBitLength);
			SecretKeyFactory secretFactory = SecretKeyFactory.getInstance(SECRET_TYPE);
			SecretKey decodingKey = secretFactory.generateSecret(keySpec);
			SecretKey actualSecret = new SecretKeySpec(decodingKey.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
			byte[] initVec = this.config.getTypedAttribute(CONFIG_INITVEC_KEY, new byte[0]);
			cipher.init(Cipher.DECRYPT_MODE, actualSecret, new IvParameterSpec(initVec));
			this.config.setObjectAttribute(CONFIG_CIPHER_KEY, cipher);
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
		catch(InvalidAlgorithmParameterException badVecEx) {
			log.error("The init vector is invalid!");
			throw new RuntimeException(badVecEx);
		}
	}
	
}

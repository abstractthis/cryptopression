package org.caiedea.cryptopression.encrypt.aes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

import org.caiedea.cryptopression.Utils;
import org.caiedea.cryptopression.encrypt.Encryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileAesEncryptor extends AesEncryptor<File> {
	private static final Logger log = LoggerFactory.getLogger(FileAesEncryptor.class);
	
	private File encryptedFile;
	
	public FileAesEncryptor(Encryptor<File> fileEncryptor, File file) {
		super(fileEncryptor);
		encryptedFile = file;
		this.initAndSetStreams(file);
	}
	
	public FileAesEncryptor(Encryptor<File> fileEncryptor, String path) {
		this(fileEncryptor, new File(path));
	}
	
	private void initAndSetStreams(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			Cipher cipher = (Cipher) config.getObjectAttribute(CONFIG_CIPHER_KEY);
			CipherOutputStream cos = new CipherOutputStream(fos, cipher);
			this.outStream = cos;
		}
		catch(FileNotFoundException file404Ex) {
			log.error("File specified for encryption not found!!");
			throw new RuntimeException(file404Ex);
		}
	}

	@Override
	public File encrypt() {
		FileInputStream fis = null;
		try {
			File targetFile = this.encryptor.encrypt();
			fis = new FileInputStream(targetFile);
			int bytesRead = 0;
			int bufferSize = this.config.getIntAttribute(CONFIG_FILE_BUFFER_SIZE);
			byte[] buffer = new byte[bufferSize];
			
			// Setup cipher stream which performs the encryption
			while ((bytesRead = fis.read(buffer)) != -1) {
				this.outStream.write(buffer, 0, bytesRead);
				log.debug("Bytes encrypted: " + bytesRead);
			}
			Utils.deleteEncryptorInputFile(targetFile, this.config);
		}
		catch(IOException ioe) {
			log.error("IO issue while trying to encrypt!!");
			throw new RuntimeException(ioe);
		}
		finally {
			try {
				if (this.outStream != null) { this.outStream.close(); }
				if (fis != null) { fis.close(); }
			}
			catch(IOException ioe) {
				log.error("Problems trying to cleanup the encryptor!!");
			}
		}
		return encryptedFile;
	}
	
}

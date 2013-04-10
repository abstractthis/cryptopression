package org.caiedea.cryptopression.encrypt.aes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.CipherOutputStream;

import org.caiedea.cryptopression.encrypt.Encryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileAesEncryptor extends AesEncryptor<File> {
	private static final Logger log = LoggerFactory.getLogger(FileAesEncryptor.class);
	
	public FileAesEncryptor(Encryptor<File> fileEncryptor) {
		super(fileEncryptor);
	}

	@Override
	public File encrypt() {
		String path = System.getProperty("user.dir") + "/encTest";
		File encryptedFile = new File(path);
		try {
			FileOutputStream fos = new FileOutputStream(encryptedFile);
			this.outStream = fos;
			int bytesRead = 0;
			int bufferSize = config.getIntAttribute("cryptopression.fileBuffer");
			byte[] buffer = new byte[bufferSize];
			
			// Setup cipher stream which performs the encryption
			//Cipher cipher = config.getTypedAttribute(CONFIG_CIPHER_KEY, new Cipher());
			CipherOutputStream cos = new CipherOutputStream(fos, this.aesCipher);
			while ((bytesRead = this.inStream.read(buffer)) != -1) {
				cos.write(buffer);
				log.debug("Bytes encrypted: " + bytesRead);
			}
			cos.close();
		}
		catch(FileNotFoundException file404Ex) {
			
		}
		catch(IOException ioe) {
			
		}
		return encryptedFile;
	}
	
}

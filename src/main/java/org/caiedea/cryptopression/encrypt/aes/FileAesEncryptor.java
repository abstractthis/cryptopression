package org.caiedea.cryptopression.encrypt.aes;

import java.io.File;

import org.caiedea.cryptopression.encrypt.Encryptor;

public class FileAesEncryptor extends AesEncryptor<File> {
	
	public FileAesEncryptor(Encryptor<File> fileEncryptor) {
		super(fileEncryptor);
	}

	@Override
	public File encrypt() {
		return null;
	}
	
}

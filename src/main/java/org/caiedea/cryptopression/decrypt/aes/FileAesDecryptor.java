package org.caiedea.cryptopression.decrypt.aes;

import java.io.File;

import org.caiedea.cryptopression.decrypt.Decryptor;

public class FileAesDecryptor extends AesDecryptor<File> {

	public FileAesDecryptor(Decryptor<File> fileDecryptor) {
		super(fileDecryptor);
	}
	
	@Override
	public File decrypt() {
		return null;
	}
	
}

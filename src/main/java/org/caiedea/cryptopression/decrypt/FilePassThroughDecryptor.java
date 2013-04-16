package org.caiedea.cryptopression.decrypt;

import java.io.File;

public class FilePassThroughDecryptor extends Decryptor<File> {
	private File targetFile;
	
	public FilePassThroughDecryptor(File file) {
		super();
		targetFile = file;
	}
	
	@Override
	protected DecryptorConfig configSpecifics() {
		return new DecryptorConfig();
	}
	
	@Override
	public File decrypt() {
		return targetFile;
	}

}

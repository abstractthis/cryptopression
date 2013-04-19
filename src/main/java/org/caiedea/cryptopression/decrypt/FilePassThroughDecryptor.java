package org.caiedea.cryptopression.decrypt;

import java.io.File;

public class FilePassThroughDecryptor extends Decryptor<File> {
	private File targetFile;
	
	public FilePassThroughDecryptor(File file) {
		super();
		targetFile = file;
	}
	
	public FilePassThroughDecryptor(String path) {
		this(new File(path));
	}
	
	@Override
	protected DecryptorConfig configSpecifics() {
		return new DecryptorConfig();
	}
	
	@Override
	protected void readHeader(File file) {
		// NOP
	}
	
	@Override
	public File decrypt() {
		return targetFile;
	}

}

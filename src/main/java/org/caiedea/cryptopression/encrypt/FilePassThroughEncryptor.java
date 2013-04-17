package org.caiedea.cryptopression.encrypt;

import java.io.File;

public class FilePassThroughEncryptor extends Encryptor<File> {
	private File targetFile;
	
	public FilePassThroughEncryptor(File file) {
		super();
		targetFile = file;
	}
	
	public FilePassThroughEncryptor(String path) {
		this(new File(path));
	}
	
	@Override
	protected EncryptorConfig configSpecifics() {
		return new EncryptorConfig();
	}
	
	@Override
	protected void writeHeader(File file) {
		// NOP
	}

	@Override
	public File encrypt() {
		return targetFile;
	}
}

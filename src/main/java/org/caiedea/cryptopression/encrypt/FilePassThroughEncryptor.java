package org.caiedea.cryptopression.encrypt;

import java.io.File;

public class FilePassThroughEncryptor extends Encryptor<File> {
	
	@Override
	protected EncryptorConfig configSpecifics() {
		return new EncryptorConfig();
	}

	@Override
	public File encrypt() {
		return null;
	}
}

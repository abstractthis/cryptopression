package org.caiedea.cryptopression.encrypt;

public class MemoryPassThroughEncryptor extends Encryptor<byte[]> {
	
	@Override
	protected EncryptorConfig configSpecifics() {
		return new EncryptorConfig();
	}

	@Override
	public byte[] encrypt() {
		throw new UnsupportedOperationException("Memory Encryptor not implemented yet.");
	}

}

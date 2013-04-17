package org.caiedea.cryptopression.decrypt;


public class MemoryPassThroughDecryptor extends Decryptor<byte[]> {
	
	@Override
	protected DecryptorConfig configSpecifics() {
		return new DecryptorConfig();
	}
	
	@Override
	protected void readHeader(byte[] bytes) {
		// NOP
	}

	@Override
	public byte[] decrypt() {
		throw new UnsupportedOperationException("Memory Decryption not implemented yet.");
	}

}

package org.caiedea.cryptopression.decrypt;

public class StringPassThroughDecryptor extends Decryptor<String> {
	
	@Override
	protected DecryptorConfig configSpecifics() {
		return new DecryptorConfig();
	}
	
	@Override
	protected void readHeader(String str) {
		// NOP
	}

	@Override
	public String decrypt() {
		throw new UnsupportedOperationException("String Decryption not implemented yet.");
	}

}

package org.caiedea.cryptopression.encrypt.aes;

import org.caiedea.cryptopression.encrypt.Encryptor;

public class MemoryAesEncryptor extends AesEncryptor<byte[]> {
	
	public MemoryAesEncryptor(Encryptor<byte[]> memEncryptor) {
		super(memEncryptor);
	}

	@Override
	public byte[] encrypt() {
		return null;
	}

}

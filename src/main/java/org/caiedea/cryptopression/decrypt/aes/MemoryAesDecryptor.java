package org.caiedea.cryptopression.decrypt.aes;

import org.caiedea.cryptopression.decrypt.Decryptor;

public class MemoryAesDecryptor extends AesDecryptor<byte[]> {
	
	public MemoryAesDecryptor(Decryptor<byte[]> memDecryptor) {
		super(memDecryptor);
	}
	
	@Override
	public byte[] decrypt() {
		throw new UnsupportedOperationException("Memory AES Decryptor not implemented yet.");
	}

}

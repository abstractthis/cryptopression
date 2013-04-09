package org.caiedea.cryptopression.decrypt.aes;

import org.caiedea.cryptopression.decrypt.Decryptor;

public class AesDecryptor<T> extends Decryptor<T> {
	protected Decryptor<T> decryptor;
	
	public AesDecryptor(Decryptor<T> d) {
		this.decryptor = d;
	}

	@Override
	public T decrypt() {
		return null;
	}

}

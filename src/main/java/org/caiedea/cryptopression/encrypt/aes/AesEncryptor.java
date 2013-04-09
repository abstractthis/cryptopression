package org.caiedea.cryptopression.encrypt.aes;

import org.caiedea.cryptopression.encrypt.Encryptor;

public class AesEncryptor<T> extends Encryptor<T> {
	protected Encryptor<T> encryptor;
	
	public AesEncryptor(Encryptor<T> e) {
		encryptor = e;
	}

	@Override
	public T encrypt() {
		return null;
	}

}

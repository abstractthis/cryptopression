package org.caiedea.cryptopression.decrypt.aes;

import org.caiedea.cryptopression.decrypt.Decryptor;
import org.caiedea.cryptopression.decrypt.DecryptorConfig;

public class AesDecryptor<T> extends Decryptor<T> {
	protected Decryptor<T> decryptor;
	
	public AesDecryptor(Decryptor<T> d) {
		super();
		this.decryptor = d;
	}
	
	@Override
	protected DecryptorConfig configSpecifics() {
		return null;
	}

	@Override
	public T decrypt() {
		return null;
	}

}

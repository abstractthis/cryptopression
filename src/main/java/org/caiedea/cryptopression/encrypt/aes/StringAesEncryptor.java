package org.caiedea.cryptopression.encrypt.aes;

import org.caiedea.cryptopression.encrypt.Encryptor;

public class StringAesEncryptor extends AesEncryptor<String> {
	
	public StringAesEncryptor(Encryptor<String> strEncryptor) {
		super(strEncryptor);
	}

	@Override
	public String encrypt() {
		throw new UnsupportedOperationException("String AES Encryptor not implemented yet.");
	}

}

package org.caiedea.cryptopression.decrypt.aes;

import org.caiedea.cryptopression.decrypt.Decryptor;


public class StringAesDecryptor extends AesDecryptor<String> {
	
	public StringAesDecryptor(Decryptor<String> strDecryptor) {
		super(strDecryptor);
	}

	@Override
	public String decrypt() {
		throw new UnsupportedOperationException("String AES Decryptor not implemented yet.");
	}

}

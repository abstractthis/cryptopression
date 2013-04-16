package org.caiedea.cryptopression.encrypt;


/**
 * The <i>PassThrough</i> group of <code>Encryptor</code>s do nothing. They
 * take the input and apply zero encryption on the data, hence the name.
 * @author dsmith
 *
 */
public class StringPassThroughEncryptor extends Encryptor<String> {
	
	@Override
	protected EncryptorConfig configSpecifics() {
		return new EncryptorConfig();
	}

	@Override
	public String encrypt() {
		throw new UnsupportedOperationException("Memory Encryptor not implemented yet.");
	}
	
}

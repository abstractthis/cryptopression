package org.caiedea.cryptopression.decrypt;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public abstract class Decryptor<T> {
	protected static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	protected InputStream inStream;
	protected OutputStream outStream;
	
	public abstract T decrypt();
	
	/**
	 * Provide the data that needs to be decrypted by this
	 * <code>Decryptor</code>.
	 * @param is a stream to any data source.
	 */
	public void setDecryptTarget(InputStream is) {
		this.inStream = is;
	}
	
	/**
	 * Provide the data that needs to be decrypted by this
	 * <code>Decryptor</code>.
	 * <p>
	 * The default implementation converts the provided <code>String</code>
	 * into its byte array representation (UTF-8 charset) and initialize the
	 * <code>Decryptor</code>s InputStream to be a <code>ByteArrayInputStream</code>
	 * pointing at the byte array representation of the <code>String</code>.
	 * @param s text that needs to be encrypted.
	 */
	public void setEncryptTarget(String s) {
		byte[] strUtf8Bytes = s.getBytes(DEFAULT_CHARSET);
		ByteArrayInputStream bis = new ByteArrayInputStream(strUtf8Bytes);
		this.setDecryptTarget(bis);
	}
}

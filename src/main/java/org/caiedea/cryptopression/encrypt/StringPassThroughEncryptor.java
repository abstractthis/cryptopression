package org.caiedea.cryptopression.encrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * The <i>PassThrough</i> group of <code>Encryptor</code>s do nothing. They
 * take the input and apply zero encryption on the data, hence the name.
 * @author dsmith
 *
 */
public class StringPassThroughEncryptor extends Encryptor<String> {
	private static final int BUFFER_SIZE = 4 * 1024;

	@Override
	public String encrypt() {
		InputStream is = this.inStream;
		Reader inStreamReader = null;
		try {
			inStreamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			char[] clearTxt = new char[BUFFER_SIZE];
			StringBuilder sb = new StringBuilder(BUFFER_SIZE);
			int charsRead = inStreamReader.read(clearTxt);
			while (charsRead != -1) {
				sb.append(clearTxt);
				Arrays.fill(clearTxt, (char)0x0000);
				charsRead = inStreamReader.read(clearTxt);
			}
			return sb.toString();
		}
		catch(UnsupportedEncodingException encEx) {
			// TODO: Add SLF4J logging statement
		}
		catch(IOException ioe) {
			// TODO: Add SLF4J logging statement
		}
		return "";
	}
	
}

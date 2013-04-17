package org.caiedea.cryptopression.decrypt.decompress;

import org.caiedea.cryptopression.Utils;
import org.caiedea.cryptopression.decrypt.Decryptor;
import org.caiedea.cryptopression.decrypt.DecryptorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecompressDecryptor<T> extends Decryptor<T> {
	private static final Logger log = LoggerFactory.getLogger(DecompressDecryptor.class);
	
	protected static final String CONFIG_BUFFER_SIZE = "cryptopression.compressBuffer";
	
	protected Decryptor<T> decryptor;
	
	public DecompressDecryptor(Decryptor<T> d) {
		super();
		this.decryptor = d;
	}
	
	@Override
	protected DecryptorConfig configSpecifics() {
		DecryptorConfig decompressConfig = new DecryptorConfig();
		Utils.seedConfigWithProperties(decompressConfig);
		return decompressConfig;
	}

	@Override
	protected void readHeader(T t) {
		// NOP - Default is to do nothing
	}

	@Override
	public T decrypt() {
		log.error("Decompress decryptor base class has no implementation; use one of the subclasses.");
		return null;
	}

}

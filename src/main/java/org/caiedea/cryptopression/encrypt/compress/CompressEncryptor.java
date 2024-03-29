package org.caiedea.cryptopression.encrypt.compress;

import org.caiedea.cryptopression.Utils;
import org.caiedea.cryptopression.encrypt.Encryptor;
import org.caiedea.cryptopression.encrypt.EncryptorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompressEncryptor<T> extends Encryptor<T> {
	private static final Logger log = LoggerFactory.getLogger(CompressEncryptor.class);
	
	protected static final String CONFIG_BUFFER_SIZE = "cryptopression.compressBuffer";
	
	protected Encryptor<T> encryptor;
	
	public CompressEncryptor(Encryptor<T> e) {
		super();
		this.encryptor = e;
	}

	@Override
	protected EncryptorConfig configSpecifics() {
		EncryptorConfig compressConfig = new EncryptorConfig();
		Utils.seedConfigWithProperties(compressConfig);
		return compressConfig;
	}
	
	@Override
	protected void writeHeader(T t) {
		// NOP - Default is to do nothing
	}

	@Override
	public T encrypt() {
		log.error("Compress encryptor base class has no implementation; use one of the subclasses.");
		return null;
	}

}

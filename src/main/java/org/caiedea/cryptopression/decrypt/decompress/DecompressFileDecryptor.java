package org.caiedea.cryptopression.decrypt.decompress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.caiedea.cryptopression.Utils;
import org.caiedea.cryptopression.decrypt.Decryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecompressFileDecryptor extends DecompressDecryptor<File> {
private static final Logger log = LoggerFactory.getLogger(DecompressFileDecryptor.class);
	
	private File decompressedFile;
	
	public DecompressFileDecryptor(Decryptor<File> fileEncryptor) {
		super(fileEncryptor);
	}
	
	public DecompressFileDecryptor(Decryptor<File> fileEncryptor, File file) {
		this(fileEncryptor);
		this.initAndSetStreams(file);
	}
	
	public DecompressFileDecryptor(Decryptor<File> fileEncryptor, String path) {
		this(fileEncryptor, new File(path));
	}
	
	private void initAndSetStreams(File file) {
		decompressedFile = file;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			this.outStream = fos;
		}
		catch(FileNotFoundException file404Ex) {
			log.error("Problems initializing decompression stream!");
			throw new RuntimeException(file404Ex);
		}
	}
	
	@Override
	public File decrypt() {
		ZipInputStream zis = null;
		try {
			File compressedFile = this.decryptor.decrypt();
			// If the default constructor was used (so no result filename provided)
			// just append the dec extension to the input filename
			if (this.decompressedFile == null) {
				this.initAndSetStreams(new File(compressedFile.getAbsolutePath() + ".dec"));
			}
			zis = new ZipInputStream(new FileInputStream(compressedFile));
			int bufferSize = this.config.getIntAttribute(CONFIG_BUFFER_SIZE);
			byte[] buffer = new byte[bufferSize];
			ZipEntry ze = null;
			while ((ze = zis.getNextEntry()) != null) {
				log.debug("Extracting: "+ze);
				int bytesRead = 0;
				while ((bytesRead = zis.read(buffer)) != -1) {
					this.outStream.write(buffer, 0, bytesRead);
				}
	            zis.closeEntry();
			}
			Utils.deleteDecryptorInputFile(compressedFile, this.config);
		}
		catch(FileNotFoundException file404Ex) {
			log.error("File specified for decompression not found!!");
			throw new RuntimeException(file404Ex);
		}
		catch(IOException ioe) {
			log.error("IO issue while trying to decompress!!");
			throw new RuntimeException(ioe);
		}
		finally {
			try {
				if (zis != null) { zis.close(); }
				if (this.outStream != null) {
					this.outStream.flush();
					this.outStream.close();
				}
			}
			catch(IOException ioe) {
				log.error("Problems cleaning up decompression decryptor!!");
			}
		}
		return this.decompressedFile;
	}
}

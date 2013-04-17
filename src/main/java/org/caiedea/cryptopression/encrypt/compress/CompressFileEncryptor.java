package org.caiedea.cryptopression.encrypt.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.caiedea.cryptopression.Utils;
import org.caiedea.cryptopression.encrypt.Encryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompressFileEncryptor extends CompressEncryptor<File> {
	private static final Logger log = LoggerFactory.getLogger(CompressFileEncryptor.class);
	
	private File compressedFile;
	
	public CompressFileEncryptor(Encryptor<File> fileEncryptor) {
		super(fileEncryptor);
	}
	
	public CompressFileEncryptor(Encryptor<File> fileEncryptor, File file) {
		this(fileEncryptor);
		this.initAndSetStreams(file);
	}
	
	public CompressFileEncryptor(Encryptor<File> fileEncryptor, String path) {
		this(fileEncryptor, new File(path));
	}
	
	private void initAndSetStreams(File file) {
		compressedFile = file;
		try {
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
			this.outStream = zos;
		}
		catch(FileNotFoundException file404Ex) {
			log.error("Problems initializing compression stream!");
			throw new RuntimeException(file404Ex);
		}
	}
	
	@Override
	public File encrypt() {
		File encryptedFile = null;
		FileInputStream fis = null;
		try {
			encryptedFile = this.encryptor.encrypt();
			fis = new FileInputStream(encryptedFile);
			
			// If the default constructor was used (so no result filename provided)
			// just append the zip extension to the input filename
			if (this.compressedFile == null) {
				this.initAndSetStreams(new File(encryptedFile.getAbsolutePath() + ".zip"));
			}
			
			int bufferSize = this.config.getIntAttribute(CONFIG_BUFFER_SIZE);
			byte[] buffer = new byte[bufferSize];
			ZipOutputStream zos = (ZipOutputStream) this.outStream;
			zos.putNextEntry(new ZipEntry("encData"));
			int bytesRead = 0;
			while ((bytesRead = fis.read(buffer)) != -1) {
				zos.write(buffer, 0, bytesRead);
			}
			Utils.deleteEncryptorInputFile(encryptedFile, this.config);
		}
		catch(FileNotFoundException encFile404Ex) {
			log.error("Encrypted File not found at " + encryptedFile.getAbsolutePath());
			throw new RuntimeException(encFile404Ex);
		}
		catch(IOException ioe) {
			log.error("Problems compressing!!");
			throw new RuntimeException(ioe);
		}
		finally {
			try {
				if (this.outStream != null) {
					((ZipOutputStream) this.outStream).closeEntry();
					this.outStream.close();
				}
				
				if (fis != null) {
					fis.close();
				}
			}
			catch(IOException ioe) {
				log.error("Problems cleaning up!!");
			}
		}
		return compressedFile;
	}

}

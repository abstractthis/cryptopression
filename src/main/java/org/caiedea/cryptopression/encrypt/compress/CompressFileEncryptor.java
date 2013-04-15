package org.caiedea.cryptopression.encrypt.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.caiedea.cryptopression.encrypt.Encryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompressFileEncryptor extends CompressEncryptor<File> {
	private static final Logger log = LoggerFactory.getLogger(CompressFileEncryptor.class);
	private static final String CONFIG_BUFFER_SIZE = "cryptopression.compressBuffer";
	private static final String CONFIG_KEEP_RAW_ENC = "cryptopression.keepRawInput";
	
	private File compressedFile;
	
	public CompressFileEncryptor(Encryptor<File> fileEncryptor, File file) {
		super(fileEncryptor);
		compressedFile = file;
		this.initAndSetStreams(file);
	}
	
	public CompressFileEncryptor(Encryptor<File> fileEncryptor, String path) {
		this(fileEncryptor, new File(path));
	}
	
	private void initAndSetStreams(File file) {
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
		try {
			encryptedFile = this.encryptor.encrypt();
			this.setEncryptTarget(new FileInputStream(encryptedFile));
			
			int bufferSize = this.config.getIntAttribute(CONFIG_BUFFER_SIZE);
			byte[] buffer = new byte[bufferSize];
			ZipOutputStream zos = (ZipOutputStream) this.outStream;
			zos.putNextEntry(new ZipEntry("encData"));
			int bytesRead = 0;
			while ((bytesRead = this.inStream.read(buffer)) != -1) {
				zos.write(buffer, 0, bytesRead);
			}
			boolean deleteInputFile = config.getIntAttribute(CONFIG_KEEP_RAW_ENC) != 0;
			if (deleteInputFile) {
				boolean deleteSuccessful = encryptedFile.delete();
				if (!deleteSuccessful) {
					String msg =
							String.format("Input file (%s) could NOT be deleted even though deletion was requested.", encryptedFile.getAbsolutePath());
					log.error(msg);
				}
			}
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
				
				if (this.inStream != null) {
					this.inStream.close();
				}
			}
			catch(IOException ioe) {
				log.error("Problems cleaning up!!");
			}
		}
		return compressedFile;
	}

}
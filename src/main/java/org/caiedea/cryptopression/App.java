package org.caiedea.cryptopression;

import java.io.File;

import org.caiedea.cryptopression.encrypt.Encryptor;
import org.caiedea.cryptopression.encrypt.FilePassThroughEncryptor;
import org.caiedea.cryptopression.encrypt.aes.FileAesEncryptor;
import org.caiedea.cryptopression.encrypt.compress.CompressFileEncryptor;

/**
 * Test harness for the Encryptor/Decryptor library
 *
 */
public class App {
    public static void main( String[] args ) {
    	// The file we want to encrypt
    	String exeDir = System.getProperty("user.dir");
    	System.out.println(exeDir);
    	//System.exit(0);
    	File file = new File(exeDir + "/encTest.pdf");
    	File result = new File(exeDir + "/encryptResult");
    	File resultCompressed = new File(exeDir + "/encryptResult.zip");
    	try {
    		Encryptor<File> noAlgoEncryptor = new FilePassThroughEncryptor(file);
    		Encryptor<File> fileEncryptor =
    				new FileAesEncryptor(noAlgoEncryptor, result);
    		Encryptor<File> compressEncryptor = new CompressFileEncryptor(fileEncryptor, resultCompressed);
    		System.err.println("Performing encryption...");
    		compressEncryptor.encrypt();
    		System.err.println( "encryption DONE!" );
    	}
    	catch(RuntimeException re) {
    		System.err.println(re);
    	}
    }
}

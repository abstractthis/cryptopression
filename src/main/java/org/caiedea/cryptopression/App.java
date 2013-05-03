package org.caiedea.cryptopression;



/**
 * Test harness for the Encryptor/Decryptor library
 *
 */
public class App {
    public static void main( String[] args ) {
    	/*
    	// The file we want to encrypt
    	String exeDir = System.getProperty("user.dir");
    	System.out.println(exeDir);
    	//System.exit(0);
    	File file = new File(exeDir + "/dtl_CD4.xml");
    	File result = new File(exeDir + "/encryptResult");
    	File decryptedResult = new File(exeDir + "/decrypted.zip");
    	//File resultCompressed = new File(exeDir + "/encryptResult.zip");
    	try {
    		Encryptor<File> noAlgoEncryptor = new FilePassThroughEncryptor(file);
    		Encryptor<File> compressEncryptor = new CompressFileEncryptor(noAlgoEncryptor);
    		Encryptor<File> fileEncryptor =
    				new FileAesEncryptor(compressEncryptor, result);
    		System.err.println("Performing encryption...");
    		fileEncryptor.encrypt();
    		System.err.println( "encryption DONE!" );
    		Decryptor<File> noAlgoDecryptor = new FilePassThroughDecryptor(result);
    		Decryptor<File> fileDecryptor = new FileAesDecryptor(noAlgoDecryptor, decryptedResult);
    		Decryptor<File> decompressionDecryptor = new DecompressFileDecryptor(fileDecryptor);
    		System.err.println("Performing decryption...");
    		decompressionDecryptor.decrypt();
    		System.err.println("decryption DONE!!!");
    	}
    	catch(RuntimeException re) {
    		System.err.println(re);
    	}
    	*/
    }
}

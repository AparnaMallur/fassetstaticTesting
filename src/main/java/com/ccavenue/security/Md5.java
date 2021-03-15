package com.ccavenue.security;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class Md5 {

	private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";
    
    public static String decrypt(String encstr) {

		   String decrypt=null;   
		   try 
	        {
				
				Key key = generateKey();
		        Cipher cipher = Cipher.getInstance(ALGORITHM);
		        cipher.init(Cipher.DECRYPT_MODE, key);
		        byte [] decryptedValue64 = new BASE64Decoder().decodeBuffer(encstr.trim());
		        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
		        decrypt = new String(decryptedByteValue,"utf-8");
	       }
	        catch(Exception e) 
	        {
	            e.printStackTrace();
	        }
		   
		   return decrypt.trim();
	   }
    
    public static String md5(String input) {
    	String md5=null;
    	try 
        {
    		
    		Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] encryptedByteValue = cipher.doFinal(input.trim().getBytes("utf-8"));
            md5= new BASE64Encoder().encode(encryptedByteValue);
        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
    	return md5.trim() ;
    	}

    private static Key generateKey() throws Exception 
    {
        Key key = new SecretKeySpec(KEY.getBytes(),ALGORITHM);
        return key;
    }
}

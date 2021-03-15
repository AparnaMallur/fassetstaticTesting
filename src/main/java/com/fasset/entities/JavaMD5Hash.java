package com.fasset.entities;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class JavaMD5Hash {

	private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";
    
	   public static String decrypt(String encstr) {

		   String decrypt = "";
		 /*  if (encstr.length() > 12) {

		 String cipher = encstr.substring(12);

		 BASE64Decoder decoder = new BASE64Decoder();

		 try {

		     return new String(decoder.decodeBuffer(cipher));

		 } catch (IOException e) {

		     //  throw new InvalidImplementationException(
			 	System.out.println(e);
		     //Fail

		 }

		   }

		   return null;
		     }*/
		   
		   try 
	        {
				
				Key key = generateKey();
		        Cipher cipher = Cipher.getInstance(ALGORITHM);
		        cipher.init(Cipher.DECRYPT_MODE, key);
		        byte [] decryptedValue64 = new BASE64Decoder().decodeBuffer(encstr);
		        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
		        decrypt = new String(decryptedByteValue,"utf-8");
	        }
	        catch(Exception e) 
	        {
	            e.printStackTrace();
	        }
		   
		   return decrypt;
	   }

public static String md5(String input) {
	String md5 = "";
		/*
		
		if(null == input) return null;
		
		try {
			
		//Create MessageDigest object for MD5
		MessageDigest digest = MessageDigest.getInstance("MD5");
		
		//Update input string in message digest
		digest.update(input.getBytes(), 0, input.length());

		//Converts message digest value in base 16 (hex) 
		md5 = new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return md5;*/
	
	try 
    {
		
		Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(input.getBytes("utf-8"));
        md5= new BASE64Encoder().encode(encryptedByteValue);
    }
    catch(Exception e) 
    {
        e.printStackTrace();
    }
	return md5 ;
	}


private static Key generateKey() throws Exception 
{
    Key key = new SecretKeySpec(KEY.getBytes(),ALGORITHM);
    return key;
}

}

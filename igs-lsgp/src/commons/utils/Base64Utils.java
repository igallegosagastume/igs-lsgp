/**
 * Creation date: 31/08/2016
 * 
 */
package commons.utils;

import org.apache.commons.codec.binary.Base64;

import commons.model.latinsquares.ILatinSquare;

/**
 * @author igallego
 *
 */
public class Base64Utils {
	
	public static String getLSAsBase64String(ILatinSquare ls) throws Exception {
		String result = "";
		byte[] byteArr = new byte[65536];//para guardar el LS de 256*256
		char[] charArr  = new char[65536];//para guardar el LS de 256*256
		int k = 0;
		for (int i=0; i<ls.size(); i++) {
			for(int j=0; j<ls.size(); j++) {
				//result+=(Character.toString((char)(ls.getValueAt(i, j).intValue())));//this is to return a string
				//byte signedByte = (byte)(ls.getValueAt(i, j).intValue());
				//int unsignedByte = signedByte & (0xff);
				char c = (char)(ls.getValueAt(i, j).intValue());
				charArr[k] = c;
				k++;
			}	
		}
		
		//lo codifico base64
		byteArr = new String(charArr).getBytes();
		
		Base64 base64 = new Base64();//esta clase esta en el Jar common-codec-1.6.jar
		result = base64.encodeToString(byteArr);
		return result;
	}
	
	public static String getBase64OfString(String str) throws Exception {
		byte[] byteArr = str.getBytes();
		Base64 base64 = new Base64();//esta clase esta en el Jar common-codec-1.6.jar
		return base64.encodeToString(byteArr);
	}

}

/**
 * Creation date: 22/04/2016
 * 
 */
package cipher.model;

/**
 * @author igallego
 *
 */
public interface ILatinSquareCipher {

	public String crypt(String plainText) throws Exception;
	public String decrypt(String cihperText) throws Exception;
	
}

/**
 * Creation date: 22/04/2016
 * 
 */
package cipher.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;

import basicImpl.model.generators.SimpleGenWithReplGraph;
import commons.generators.IRandomLatinSquareGenerator;
import commons.model.ILatinSquare;

/**
 *  This is another LS-based cipher, created by IGS for Blue Montag Software.
 * 
 * @author igallego
 *
 */
public class AlexCipher implements ILatinSquareCipher {

	private final int MODE_USE_SAME_ROW = 0;
	private final int MODE_USE_SAME_COL = 1;

	private int mode = MODE_USE_SAME_ROW;
	private int current_row_col = 0;
	private int textPosition = 0;
	private int row = 0;
	private int col = 0;
	
	private ILatinSquare ls = null;
	private int n = 0;
	
	public static void main(String[] args) throws Exception {
		IRandomLatinSquareGenerator generator = new SimpleGenWithReplGraph(256);
		ILatinSquare ls = generator.generateLS();
		
		AlexCipher cipher = new AlexCipher(ls);
		
		//cipher.showPrivateKey();
		
		Scanner input = new Scanner(System.in);
        System.out.print("Enter some plain text: ");
        String inputString = input.nextLine();
        
        
		String encrypted = cipher.crypt(inputString);
		System.out.println(encrypted);
		
		writeTextToFile("c:\\users\\igallego\\Dropbox\\Blue Montag Software\\2016-08-10_Desafio\\2016-08-11_cipherText.txt", cipher.getBase64OfString(encrypted));
		writeTextToFile("c:\\users\\igallego\\Dropbox\\Blue Montag Software\\2016-08-10_Desafio\\2016-08-11_LS.txt", cipher.getLSAsBase64String());
		
		String plain = cipher.decrypt(encrypted);
		
		System.out.println("Press a key to decrypt:");
		System.in.read();
		
		System.out.print(plain);
		input.close();
	}
	
	private static void writeTextToFile(String path, String text) throws Exception {
		File logFile=new File(path);

	    BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
	    writer.write (text);

	    //Close writer
	    writer.close();
	}
	
	/**
	 * Constructs a cipher with the LS passed as parameter as the private key.
	 * 
	 * @param ls
	 * @throws Exception
	 */
	public AlexCipher(ILatinSquare ls) throws Exception {
		this.ls = ls;
		this.n = ls.size();
	}
	
	/**
	 * Ciphers a plaintext using the private LS
	 * 
	 * @param plaintext
	 * @return
	 * @throws Exception
	 */
	@Override
	public String crypt(String plaintext) throws Exception {
		char currChar = plaintext.charAt(this.textPosition); 
		String cipheredText = "";
		
		while ((int)currChar<256 && this.textPosition<plaintext.length()) {
			this.searchForCharInSameRowOrColumn(new Character(currChar).toString());
			
			this.nextRowOrColumn();
			
			String cipherChar;
			
			if (this.mode==this.MODE_USE_SAME_ROW)
				cipherChar = Character.toString((char)(ls.getValueAt(this.current_row_col, this.col).intValue()));
			else
				cipherChar = Character.toString((char)(ls.getValueAt(this.row, this.current_row_col).intValue()));
			
			cipheredText += cipherChar;
			
			this.textPosition++;
			
			if (this.textPosition<plaintext.length())
				currChar = plaintext.charAt(this.textPosition);
		}
	
		return cipheredText;
	}
	
	/**
	 * Decipher the ciphered text into a plain text
	 * 
	 * @param cipheredText
	 * @return
	 * @throws Exception
	 */
	@Override
	public String decrypt(String cipheredText) throws Exception {
		this.textPosition = cipheredText.length()-1;
		
		char curCipherChar = cipheredText.charAt(this.textPosition); 
		String plainText = "";
		
		while (this.textPosition>=0) {
			this.searchForCharInSameRowOrColumn(new Character(curCipherChar).toString());
			this.previousRowOrColumn();
			
			String plainChar;
			if (this.mode==this.MODE_USE_SAME_ROW)
				plainChar = Character.toString((char)(ls.getValueAt(this.current_row_col, this.col).intValue()));
			else
				plainChar = Character.toString((char)(ls.getValueAt(this.row, this.current_row_col).intValue()));
			 
			plainText = plainChar + plainText;
			this.textPosition--;
			if (this.textPosition>=0)
				curCipherChar = cipheredText.charAt(textPosition);

		}
		
		this.changeMode();
		
		return plainText;
	}
	
	/**
	 * 
	 *  Searchs for the next character in the same row / column
	 *  
	 **/
	private void searchForCharInSameRowOrColumn(String car) throws Exception {
		if (this.mode==this.MODE_USE_SAME_COL) {
			String lsCar = Character.toString((char)(ls.getValueAt(this.row, this.current_row_col).intValue()));
			while (!car.equals(lsCar)) {
				this.move();
				lsCar = Character.toString((char)(ls.getValueAt(this.row, this.current_row_col).intValue()));
			}
		} else {
			String lsCar = Character.toString((char)(ls.getValueAt(this.current_row_col, this.col).intValue()));
			while (!car.equals(lsCar)) {
				this.move();
				lsCar = Character.toString((char)(ls.getValueAt(this.current_row_col, this.col).intValue()));
			}
		}

		//now the position in the LS is in the same character that car
		return;
	}
	
	/**
	 * Changes the row/column
	 */
	private void nextRowOrColumn() {
		this.current_row_col++;
		if (this.current_row_col==n) {
			this.current_row_col = 0;
		}
	}
	
	/**
	 * Changes the row/column
	 */
	private void previousRowOrColumn() {
		this.current_row_col--;
		if (this.current_row_col==-1) {
			this.current_row_col = n-1;
		}
	}
	
	/**
	 * Moves inside the same row column in LS one char
	 */
	private void move() {
		if (this.mode==this.MODE_USE_SAME_ROW) {
			this.col++;
			if (this.col==n)
				this.col = 0;
		} else { // MODE COL
			this.row++;
			if (this.row==n)
				this.row = 0;
		}
	}
	
	/**
	 * After decrypting a word, the mode is changed
	 */
	private void changeMode() {
		if (this.mode==this.MODE_USE_SAME_ROW)
			this.mode = this.MODE_USE_SAME_COL;
		else
			this.mode = this.MODE_USE_SAME_ROW;
	}
	
	@Override
	public void showPrivateKey() throws Exception {
		for (int i=0; i<ls.size(); i++) {
			for(int j=0; j<ls.size(); j++) {
				System.out.print(Character.toString((char)(ls.getValueAt(i, j).intValue()))+" ");
			}
			System.out.println("");
		}
	}

	private String getLSAsBase64String() throws Exception {
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
	
	private String getBase64OfString(String str) throws Exception {
		byte[] byteArr = str.getBytes();
		Base64 base64 = new Base64();//esta clase esta en el Jar common-codec-1.6.jar
		return base64.encodeToString(byteArr);
	}

}

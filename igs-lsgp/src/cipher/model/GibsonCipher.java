/**
 * Creation date: 18/05/2015
 * 
 */
/**
 * © Copyright 2012-2015 Ignacio Gallego Sagastume
 * 
 * This file is part of IGS-ls-generation package.
 * IGS-ls-generation package is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * IGS-ls-generation package is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with IGS-ls-generation package.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */
package cipher.model;

import java.util.Scanner;

import basicImpl.model.generators.SimpleGenWithReplGraph;
import commons.model.ILatinSquare;
import commons.model.OrderedPair;

/**
 * This simple Cipher shows how a LS can be used to crypt and decrypt information.
 * 
 *  The mechanism is due to Gibson, and it's called "Off the Grid".
 * 
 * 
 * @author igallego
 *
 */
public class GibsonCipher implements ILatinSquareCipher {

	private final int RIGHT = 0;
	private final int DOWN  = 1;
	private final int LEFT  = 2;
	private final int UP    = 3;
	
	private OrderedPair lsPosition = new OrderedPair(0, 0);
	private int textPosition = 0;
	private int direction = RIGHT;//left to right
	private ILatinSquare ls = null;
	private int n = 0;
	
	public static void main(String[] args) throws Exception {
		SimpleGenWithReplGraph generator = new SimpleGenWithReplGraph(256);
		ILatinSquare ls = generator.generateLS();
		
		GibsonCipher cipher = new GibsonCipher(ls);
//		cipher.add(64);
		cipher.showPrivateKey();
		
		Scanner input = new Scanner(System.in);
		System.out.print("Enter some plain text: ");
		String inputString = input.nextLine();

		
		String encrypted = cipher.crypt(inputString);
		System.out.println(encrypted);
		
		String plain = cipher.decrypt(encrypted);
		
		System.out.println("Press a key to decrypt:");
		System.in.read();
		
		System.out.print(plain);
		input.close();
	}
	
	/**
	 * Constructs a cipher with the LS passed as parameter as the private key.
	 * 
	 * @param ls
	 * @throws Exception
	 */
	public GibsonCipher(ILatinSquare ls) throws Exception {
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
		textPosition = 0;
		
		char currChar = plaintext.charAt(textPosition); 
		String cipheredText = "";
		
		while ((int)currChar<256 && this.textPosition<plaintext.length()) {
			this.searchForChar(new Character(currChar).toString());
			
			this.move();//take the next character
			
			String cipherChar = Character.toString((char)(ls.getValueAt(lsPosition.x, lsPosition.y).intValue()));
			
			//change direction to the next 
			this.nextDirection();
			
			cipheredText += cipherChar;
			this.textPosition++;
			if (this.textPosition<plaintext.length())
				currChar = plaintext.charAt(textPosition);
		}
		this.prevDirection();//undo last change of direction
	
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
		textPosition = cipheredText.length()-1;
		
		this.invertDirection();
		
		char currChar = cipheredText.charAt(textPosition); 
		String plainText = "";
		
		while (this.textPosition>=0) {
			/**
			 * mover
			   carplano = tomar caracter en posicion actual
			   tomar siguiente encriptado
			   buscar car encriptado en misma direccion
			   girar
			 */
			this.move();
			String plainChar = Character.toString((char)(ls.getValueAt(lsPosition.x, lsPosition.y).intValue()));
			plainText = plainChar + plainText;
			this.textPosition--;
			if (this.textPosition>=0)
				currChar = cipheredText.charAt(textPosition);

			this.searchForChar(new Character(currChar).toString());
			
			this.prevDirection();
			
			
		}
		return plainText;
	}
	
	/**
	 * 
	 *  Searchs for the next character in the same direction
	 *  
	 **/
	private void searchForChar(String car) throws Exception {
		String lsCar = Character.toString((char)(ls.getValueAt(lsPosition.x, lsPosition.y).intValue()));
		while (!car.equals(lsCar)) {
			this.move();
			lsCar = Character.toString((char)(ls.getValueAt(lsPosition.x, lsPosition.y).intValue()));
		}
		
		return;
	}
	
	/**
	 * Changes the direction for the next clockwise
	 */
	private void nextDirection() {
		direction = (direction+1)%4;
	}
	
	/**
	 * Changes the direction for the next anti-clockwise
	 */
	private void prevDirection() {
		direction = (direction-1);
		if (direction==-1)
			direction = UP; 
	}
	
	/**
	 * Turns 180 degrees
	 */
	private void invertDirection() {
		switch (direction) {
		case RIGHT:
			direction = LEFT;
			break;
		case DOWN:
			direction = UP;
			break;
		case LEFT:
			direction = RIGHT;
			break;
		case UP:
			direction = DOWN;
			break;
		default:
			break;
		}
	}
	
	/**
	 * Moves inside the LS one position
	 */
	private void move() {
		switch (direction) {
		case RIGHT:
			lsPosition.y++;
			if (lsPosition.y==n)
				lsPosition.y = 0;
			break;
		case DOWN:
			lsPosition.x++;
			if (lsPosition.x==n)
				lsPosition.x = 0;
			break;
		case LEFT:
			lsPosition.y--;
			if (lsPosition.y==-1)
				lsPosition.y = n-1;
			break;
		case UP:
			lsPosition.x--;
			if (lsPosition.x==-1)
				lsPosition.x = n-1;
			break;
		default:
			break;
		}
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
//	
//	private void add(int n) throws Exception {
//		for (int i=0; i<ls.size(); i++) {
//			for(int j=0; j<ls.size(); j++) {
//				int k = ls.getValueAt(i, j)+n;
//				ls.setValueAt(i, j, k);
//			}
//		}
//	}
}

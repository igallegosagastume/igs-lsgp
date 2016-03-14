/**
 * Creation date: 14/03/2016
 * 
 */
package basicImpl.model.latinsquares;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import commons.ILatinSquare;

/**
 * @author igallego
 *
 */
public class primitiveIntArrayLatinSquare implements ILatinSquare {
	protected int[][] ls;
	
	protected int n = 0;
	
	protected MessageDigest md = null;
	
	public primitiveIntArrayLatinSquare(int n) {
		
		 this.ls = new int[n][n];
		 this.n = n;
		 
		//initialize the md
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No such algorithm: md5");
		}
	}

	@Override
	public int size() throws Exception {
		return n;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Latin square of order "+n+":\n");
		for (int x=0; x<n ; x++) {
			//sb.append("Row "+x+":");
			for (int y=0; y<n ; y++) {
				try {
					Integer elem = ls[x][y];
					sb.append(elem); 
					sb.append("    ".substring(elem.toString().length()));
					
				} catch (Exception e) {
					sb.append("--  ");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public Integer getValueAt(int row, int col) {
		return ls[row][col];
	}

	@Override
	public void setValueAt(int row, int col, int value) {
		ls[row][col] = value;
	}

	@Override
	public void writeToFile(String fileName) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				Integer elem = ls[i][j];
				bw.write(elem.toString());
				bw.write("    ".substring(elem.toString().length()));
			}
			bw.write("\n");
		}
		bw.close();
	}

	/**
	 * Inefficient, but we'll se for other methods.
	 * 
	 * @param i
	 * @param row
	 */
	@Override
	public void setRow (int i, List<Integer> row) {
		for (int j=0; j<n; j++)
			ls[i][j] = row.get(j);
	}
	
	public boolean equals(ILatinSquare ls2) throws Exception {
		int n2 = ls2.size();
		if (this.size()!=n2) return false;
		boolean eq = true;
		for (int i=0; i<n2 && eq; i++) {
			for (int j=0; j<n2 && eq; j++) {
				if (ls[i][j]!=ls2.getValueAt(i, j)) {
					eq = false;
				}
				
			}
		}
		return eq;
	}

	public byte[] hashCodeOfStructure() {
		String str1 = this.serializeStructure();
		return md.digest(str1.getBytes());
	}
	
	
	public String serializeStructure() {
		StringBuffer sb = new StringBuffer();
		for (int x=0; x<n ; x++) {
			for (int y=0; y<n ; y++) {
				Integer elem = ls[x][y];
				sb.append(elem); 
			}
		}
		return sb.toString();
	}
	
	
	public static boolean equalHash(byte[] dig1, byte[] dig2) {
		return MessageDigest.isEqual(dig1, dig2);
	}
}

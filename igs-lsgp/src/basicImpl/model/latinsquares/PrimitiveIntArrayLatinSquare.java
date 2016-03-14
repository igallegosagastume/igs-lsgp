/**
 * Creation date: 14/03/2016
 * 
 */
package basicImpl.model.latinsquares;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author igallego
 *
 */
public class PrimitiveIntArrayLatinSquare extends AbstractLatinSquare {
	protected int[][] ls;
	
	protected int n = 0;
	
	protected MessageDigest md = null;
	
	public PrimitiveIntArrayLatinSquare(int n) {
		super(n);
		this.ls = new int[n][n];
		
		 
		//initialize the md
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No such algorithm: md5");
		}
	}

	@Override
	public Integer getValueAt(int row, int col) {
		return ls[row][col];
	}

	@Override
	public void setValueAt(int row, int col, int value) {
		ls[row][col] = value;
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
}

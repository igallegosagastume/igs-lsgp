/**
 * Creation date: 14/03/2016
 * 
 */
package basicImpl.model.latinsquares;

import java.security.MessageDigest;
import java.util.List;

/**
 *  This is an optional implementation for class AbstractLatinSquare.
 * 
 * 
 * @author igallego
 *
 */
public class PrimitiveIntArrayLatinSquare extends AbstractLatinSquare {
	protected int[][] ls;
	
	protected int n = 0;
	
	protected MessageDigest md = null;
	
	/**
	 * Constructs the LS instance.
	 * @param n
	 */
	public PrimitiveIntArrayLatinSquare(int n) {
		super(n);
		this.ls = new int[n][n];
	}

	/**
	 * Gets the value at specified row and column indexes.
	 */
	@Override
	public Integer getValueAt(int row, int col) {
		return ls[row][col];
	}

	/**
	 * Sets the value at specified row and column indexes.
	 */
	@Override
	public void setValueAt(int row, int col, int value) {
		ls[row][col] = value;
	}

	/**
	 * Inefficient, but we'll search for other methods.
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

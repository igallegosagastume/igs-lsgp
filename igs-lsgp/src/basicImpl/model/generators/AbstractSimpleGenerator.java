/**
 * Creation date: 14/03/2016
 * 
 */
package basicImpl.model.generators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import basicImpl.model.latinsquares.ArrayListLatinSquare;
import commons.generators.IRandomLatinSquareGenerator;
import commons.model.ILatinSquare;
import commons.utils.RandomUtils;

/**
 * This class abstracts the common behaviour of all generators that operate sequentially, generating one random symbol at the time,
 * and completing the LS by rows (up to down) and columns (left to write).
 * 
 * @author igallego
 *
 */
public abstract class AbstractSimpleGenerator implements IRandomLatinSquareGenerator {

	
	protected int n = 0;//the size of the LSs to be generated
	
	//auxiliary structures
	protected Set<Integer>[] availableInCol;
	protected ILatinSquare ls;
	protected Integer[] failedAttemptsPerRow;
	protected int[][] collisions;
	
	//the set of all possible symbols
	protected Set<Integer> symbols = null;

	@SuppressWarnings("unchecked")
	public AbstractSimpleGenerator(int n) {
		this.n = n;
		
		RandomUtils.initRand();
		this.symbols = RandomUtils.oneToN(n);
		
		availableInCol = new HashSet[n];
		failedAttemptsPerRow = new Integer[n];
		collisions = new int[n][n];
		
		//initially available in each column
	    for (int i=0; i<n; i++) {
	    	availableInCol[i] = new HashSet<Integer>();
	
	    	availableInCol[i].addAll(symbols);
	    }
	    
	    ls = new ArrayListLatinSquare(n);//default implementation
	    
	    for (int i=0; i<n; i++) {
	    	failedAttemptsPerRow[i] = 0;
	    }
	    
	    for (int i=0; i<n; i++) 
	    	for (int j=0; j<n; j++) {
	    		collisions[i][j]=0;
	    	}

	}
	
	@Override
	public ILatinSquare generateLS() { 
	    for (int i=0; i<n; i++) {
	    	List<Integer> row = this.generateRow(i);
	    	ls.setRow(i, row);
	    }

	    return ls;
	  }

	/** 
	 * Generates row i_row of LS. 
	 * 
	 * This default implementation does not take into account the conflicts in the generated row with previous columns.
	 *  
	 * @param i_row
	 * @return
	 */
	protected List<Integer> generateRow(int i_row) {
	    HashSet<Integer> availableInRow = new HashSet<Integer>();
	    availableInRow.addAll(this.symbols);//all symbols initially available in the row 
	    
	    ArrayList<Integer> row = new ArrayList<Integer>();
	    int i_col = 0;
	    
	    while (i_col < n) {
            Integer symbol = RandomUtils.randomChoice(availableInRow);

            i_col = i_col + 1;
            availableInRow.remove(symbol);//to avoid repetition in the generated row
            row.add(symbol);
	    }

	    return row;
	}
	        
	@Override
	public String getMethodName() {
		return "Generation row by row abstract.";
	}

}
	        
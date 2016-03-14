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
import commons.ILatinSquare;
import commons.RandomUtils;
import commons.generators.IRandomLatinSquareGenerator;

/**
 * @author igallego
 *
 */
public abstract class AbstractSimpleGenerator implements IRandomLatinSquareGenerator {

	
	protected int n = 0;
	protected Set<Integer>[] availableInCol;
	protected ILatinSquare ls;
	protected Integer[] failedAttemptsPerRow;
	protected int[][] collisions;
	
	protected HashSet<Integer> symbols = null;

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
	    	if (failedAttemptsPerRow[i]>1000000) {
		        System.out.print("Collisions in row "+i+"="+failedAttemptsPerRow[i]);
		    }
	    	ls.setRow(i, row);
	    }

	    return ls;
	  }

	/** 
	 * Generates row i_row of LS
	 * @param i_row
	 * @return
	 */
	protected List<Integer> generateRow(int i_row) {
	    HashSet<Integer> availableInRow = new HashSet<Integer>();//    = [ i for i in range(1,n+1) ]
	    
	    availableInRow.addAll(RandomUtils.oneToN(n));
	    
	    ArrayList<Integer> row = new ArrayList<Integer>();
	    int i_col = 0;
	    
	    while (i_col < n) {
            Integer symbol = RandomUtils.randomChoice(availableInRow);

            i_col = i_col + 1;
            availableInRow.remove(symbol);
            row.add(symbol);
	    }

	    return row;
	}
	        
	@Override
	public String getMethodName() {
		return "Generation row by row abstract.";
	}

}
	        
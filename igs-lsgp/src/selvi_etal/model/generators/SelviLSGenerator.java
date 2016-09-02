/**
 * Creation date: 02/09/2016
 * 
 */
package selvi_etal.model.generators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import seqgen.model.generators.AbstractSequentialGenerator;
import commons.generators.IRandomLatinSquareGenerator;
import commons.model.latinsquares.ArrayListLatinSquare;
import commons.model.latinsquares.ILatinSquare;
import commons.utils.RandomUtils;

/**
 * @author igallego
 *
 */
public class SelviLSGenerator extends AbstractSequentialGenerator implements IRandomLatinSquareGenerator {
	
	public SelviLSGenerator(int n) {
		super(n);
	}
	
	public static void main(String[] args) throws Exception {
		SelviLSGenerator generator = new SelviLSGenerator(10);
		
		generator.generateLS();
	}
	
	
	@Override
	public String getMethodName() {
		return "Selvi et.al. row by row generation.";
	}

	@Override
	protected List<Integer> generateRow(int i_row) {
		HashSet<Integer> availableInRow = new HashSet<Integer>(this.symbols);//all symbols initially available in the row 
	    
	    ArrayList<Integer> row = new ArrayList<Integer>(this.n);
	    
	    List<Integer> S = new ArrayList<Integer>(2*n);//from 0 to n-1 is Avail Symbol Count at Column i
	    											  //from n to (2*n)-1 is Possibilities for Symbol i
	    
	    //initialize S (ASCAI)
	    for (int i=0; i<=n-1; i++) {
	    	row.add(new Integer(-1));
	    	S.add(this.availableInCol[i].size());
	    }
	    //initialize S (PSI)
	    for (int i=n; i<=(2*n)-1; i++) {
	    	S.add(n-i_row);
	    }
	    
	    System.out.println(S);
	    
	    return row;
	}

	/*@Override
	public ILatinSquare generateLS() {
		ILatinSquare ls = new ArrayListLatinSquare(this.n);
		
		
		System.out.println(ls.toString());
		
		return ls;
	}*/
	
	

}

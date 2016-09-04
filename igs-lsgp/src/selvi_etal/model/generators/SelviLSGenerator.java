/**
 * Creation date: 02/09/2016
 * 
 */
package selvi_etal.model.generators;

import java.util.ArrayList;
import java.util.List;

import seqgen.model.generators.AbstractSequentialGenerator;

import commons.generators.IRandomLatinSquareGenerator;
import commons.utils.RandomUtils;

/**
 * 
 * TODO: The method is not debugged... is in development 
 * @author igallego
 *
 */
public class SelviLSGenerator extends AbstractSequentialGenerator implements IRandomLatinSquareGenerator {
	
	public SelviLSGenerator(int n) {
		super(n);
	}
	
	public static void main(String[] args) throws Exception {
		SelviLSGenerator generator = new SelviLSGenerator(5);
		
		generator.generateLS();
	}
	
	
	@Override
	public String getMethodName() {
		return "Selvi et.al. row by row generation.";
	}

	@Override
	protected List<Integer> generateRow(int i_row) {
		//HashSet<Integer> availableInRow = new HashSet<Integer>(this.symbols);//all symbols initially available in the row 
	    
	    ArrayList<Integer> row = new ArrayList<Integer>(this.n);
	    
	    List<Integer> a = new ArrayList<Integer>(2*n);//from 0 to n-1 is Avail Symbol Count at Column i
	    											  //from n to (2*n)-1 is Possibilities Count for Symbol i
	    @SuppressWarnings("unchecked")
		List<Integer>[] availSymbolInCol = new ArrayList[n];
	    @SuppressWarnings("unchecked")
		List<Integer>[] availColumnsForSymbol = new ArrayList[n];
	    
	    //initialize the array "posibilities (available columns) for each symbol"
	    for (int i=0; i<=n-1; i++) {
	    	availColumnsForSymbol[i] = new ArrayList<Integer>();
	    }
	    
	    //initialize a (ASCAI)
	    for (int i=0; i<=n-1; i++) {//iterate columns
	    	row.add(new Integer(-1));//this is to achieve the final length of the array (n)
	    	a.add(this.availableInCol[i].size());
	    	availSymbolInCol[i] = new ArrayList<Integer>();
	    	availSymbolInCol[i].addAll(this.availableInCol[i]);
	    	
	    	for (int j=0; j<this.availableInCol[i].size(); j++) {//iterate through available in column i
				Integer symbol = availSymbolInCol[i].get(j);//take a symbol not used in column i
	    		availColumnsForSymbol[symbol.intValue()].add(new Integer(i));
	    	}
	    }
	    //initialize a (PCSI)
	    for (int i=n; i<=(2*n)-1; i++) {
	    	a.add(n-i_row);
	    }
	    
	    //take the smallest non-zero value index S of A1 A2 ... A2n
	    int s=0;
	    int rowLength = 0;
	    while (s!=-1 && rowLength<n) {
		    s = this.takeSmallestValueIndex(a);
		    
		    if (s==-1) {
		    	System.out.println("O'Carroll algorithm fails... now I will try with backtracking!");
		    	break;
		    }
		    int position;
		    int element; 
		    
		    //CHECK: 1) If S <= N, insert in the Sth position in the Rth row the Bth letter among those that can be entered in this position
		    //       2) If S >  N, insert the (S - N)th letter of the alphabet in the Bth position among those still open to it in the Rth row
		    if (s<=(n-1)) {
		    	position = s;
		    	element = RandomUtils.randomChoice(availSymbolInCol[position]);
		    } else {
		    	element = s-n;
		    	position = RandomUtils.randomChoice(availColumnsForSymbol[element]);
		    }
		    //count the choice: update array "a" and availSymbolInCol
	    	
	    	//iterate through available symbols in the column before erasing the collection
	    	for (int j=0; j<availSymbolInCol[position].size(); j++) {
	    		Integer symbol = availSymbolInCol[position].get(j);
	    		
		    	a.set(symbol+n, a.get(symbol+n)-1);
		    	availColumnsForSymbol[symbol].remove(new Integer(position));//the column "position" is no longer available for the symbol "symbol"
		    }
	    	//as the column "position" is now used, there are no available symbols in it
	    	availSymbolInCol[position].clear();
		    a.set(position, 0);
		    //as the symbol "element" is now used, there are no posible columns for it
		    availColumnsForSymbol[element].clear();
		    a.set(element+n, 0);
		    
		    //last but not least: remove element "element" from available of all columns, as it is now used in the row
		    for (int i=0; i<n-1; i++) {
		    	
		    	if (availSymbolInCol[i].remove(new Integer(element))) {//if the element existed in the collection, decrement count
		    		a.set(i, a.get(i)-1);//decrement count	
		    	}
		    }
		    
		    row.set(position, element);
		    rowLength++;
	    }
	    System.out.println(a);
	    
	    return row;
	}

	private int takeSmallestValueIndex(List<Integer> a) {
		int index = -1;
		int minor = Integer.MAX_VALUE;
		
		for (int i=0; i<=(2*n)-1; i++) {
			if (a.get(i).intValue()==0)
				continue;//if it is 0, discard
			if (a.get(i).intValue() < minor) {
				minor = a.get(i);
				index = i;
			}
		}
		return index;
	}

}

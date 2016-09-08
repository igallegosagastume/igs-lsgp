/**
 * Creation date: 04/09/2016
 * 
 */
package selvi_et_al.model.generators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import seqgen.model.generators.AbstractSequentialGenerator;

import commons.generators.IRandomLatinSquareGenerator;
import commons.model.latinsquares.ILatinSquare;
import commons.utils.RandomUtils;

/**
 * TODO: This method is under development ...
 *  
 * @author igallego
 *
 */
public class SelviEtAlLSGenerator extends AbstractSequentialGenerator implements IRandomLatinSquareGenerator {
	
	boolean verbose = false;
	
	
	private ArrayList<Integer> row;//the current row that is being generated in the method "generateRow"
    
	private List<Integer> a;//from 0 to n-1 is Avail Symbol Count at Column i (SFC: "Symbols for Column" Count)
							//from n to (2*n)-1 is Possibilities Count for Symbol i (CFS: "Columns for Symbol" Count)
    
	private List<Integer>[] availSymbolsInColumn;//SFC: "Symbols For Column"
	private List<Integer>[] availColumnsForSymbol;//CFS: "Columns For Symbol"
	
	private List<Integer> path;
	
	private List<Integer>[] initiallyAvailInColumn;
	
	int rowLength = 0;
	int failedAttempts = 0;
	
	
	public SelviEtAlLSGenerator(int n) {
		super(n);
	}
	
	public static void main(String[] args) throws Exception {
		SelviEtAlLSGenerator generator;// = new SelviEtAlLSGenerator(9);
		int i=1;
		while (i<100) {
			generator = new SelviEtAlLSGenerator(9);
			
			generator.setVerbose(true);
			ILatinSquare ls = generator.generateLS();
			
			System.out.println();
			System.out.println("Generation number "+i++);
			System.out.println(ls);
			
//			try {
//				System.in.read();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}
	
	public void setVerbose(boolean show) {
		this.verbose = show;
	}
	
	@Override
	public String getMethodName() {
		return "Selvi's (et.al.) row by row generation.";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Integer> generateRow(int i_row) {
		path = new ArrayList<Integer>();
		initiallyAvailInColumn = new ArrayList[n];
		
		for (int i=0; i<n; i++) {
			initiallyAvailInColumn[i] = new ArrayList<Integer>();
			initiallyAvailInColumn[i].addAll(this.availableInCol[i]);
		}

		int position=0;
	    int element=0;
	    
		this.initializeAuxiliaryStructures(i_row);
	    
	    //take the smallest non-zero value index S of A1 A2 ... A2n
	    int s=0;
	    rowLength = 0;
	    failedAttempts = 0;
	    while (rowLength<n) {
		    s = this.takeSmallestValueIndex(a);
		    
		    if (s==-1) {//O'Carroll's method has failed. Begin again or backtrack.
		    	System.out.println();
		    	System.out.println("O'Carroll's method failed. Begin again with row "+i_row+". Press a key to continue.");
		    	
		    	failedAttempts++;
//		    	try {
//					System.in.read();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
		    	this.initializeAuxiliaryStructures(i_row);
		    	
		    	this.printVariables(i_row, element, position);
		    	
		    	rowLength = 0;//begin the row again
		    	
		    	s = this.takeSmallestValueIndex(a);
		    }
		    //TAKE AN ELEMENT (SYMBOL) AND POSITION (COLUMN)
		    //CHECK (From Selvi's PAPER):
		    //  1) If S <= N, insert in the Sth position the Bth letter among those that can be entered in this position (0<B<S B RANDOM) 
		    //  2) If S >  N, insert the (S - N)th letter of the alphabet in the Bth position among those still open to it in the Rth row (B RANDOM) 
		    if (s<=(n-1)) {
		    	position = s;
		    	element = RandomUtils.randomChoice(availSymbolsInColumn[position]);
		    } else {
		    	element = s-n;
		    	position = RandomUtils.randomChoice(availColumnsForSymbol[element]);
		    }
		    //count the choice: update array "a" and auxiliary structures
	    	this.countTheChosenMove(element, position);
	    	
		    rowLength++;
		    
		    if (this.verbose) {
		    	this.printVariables(i_row, element, position);
		    }
	    }
	    return row;
	}

	private int takeSmallestValueIndex(List<Integer> a) {
		int index = -1;
		int minor = Integer.MAX_VALUE;
		List<Integer> posibleColumns = new ArrayList<Integer>();
		
		for (int i=0; i<=(2*n)-1; i++) {
			if (a.get(i).intValue()==0)
				continue;//if it is 0, discard
			if (a.get(i).intValue() < minor) {
				minor = a.get(i);
				index = i;
			}
		}
		if (index!=-1) {
			for (int i=0; i<=(2*n)-1; i++) {
				if (a.get(i).intValue() == minor) {
					posibleColumns.add(new Integer(i));
				}
			}
		
			return RandomUtils.randomChoice(posibleColumns);
		} else
			return -1;
	}

	@SuppressWarnings("unchecked")
	private void initializeAuxiliaryStructures(int i_row) {
		row = new ArrayList<Integer>(this.n);
	    
	    a = new ArrayList<Integer>(2*n);//from 0 to n-1 is Avail Symbol Count at Column i
	    											  //from n to (2*n)-1 is Possibilities Count for Symbol i
		availSymbolsInColumn = new ArrayList[n];
		availColumnsForSymbol = new ArrayList[n];
	    
	    //initialize the array of posibilities (available columns) for each symbol (CFS: "Columns For Symbol")
	    for (int i=0; i<=n-1; i++) {
	    	availColumnsForSymbol[i] = new ArrayList<Integer>();
	    	
	    	//initialize the working set
	    	availableInCol[i] = new HashSet<Integer>();
	    	availableInCol[i].addAll(initiallyAvailInColumn[i]);
	    }
	    
	    //initialize a (SFC: "Symbols For Colum")
	    for (int i=0; i<=n-1; i++) {//iterate columns
	    	row.add(new Integer(-1));//this is to achieve the final length of the array (n)
	    	a.add(this.availableInCol[i].size());
	    	availSymbolsInColumn[i] = new ArrayList<Integer>();
	    	availSymbolsInColumn[i].addAll(this.availableInCol[i]);
	    	
	    	for (int j=0; j<this.availableInCol[i].size(); j++) {//iterate through available in column i
				Integer symbol = availSymbolsInColumn[i].get(j);//take a symbol not used in column i
	    		availColumnsForSymbol[symbol.intValue()].add(new Integer(i));
	    	}
	    }
	    //initialize "A" (CFS COUNT)
	    for (int i=n; i<=(2*n)-1; i++) {
	    	a.add(n-i_row);
	    }
	}
	
	private void countTheChosenMove(int element, int position) {
		//iterate through available symbols in the column before erasing the collection
    	for (int j=0; j<availSymbolsInColumn[position].size(); j++) {
    		Integer symbol = availSymbolsInColumn[position].get(j);
    		
	    	a.set(symbol+n, a.get(symbol+n)-1);
	    	availColumnsForSymbol[symbol].remove(new Integer(position));//the column "position" is no longer available for the symbol "symbol"
	    }
    	//as the column "position" is now used, there are no available symbols in it
    	availSymbolsInColumn[position].clear();
	    a.set(position, 0);
	    //as the symbol "element" is now used, there are no posible columns for it
	    availColumnsForSymbol[element].clear();
	    a.set(element+n, 0);
	    
	    //last but not least: remove element "element" from available of all columns, as it is now used in the row
	    for (int i=0; i<n; i++) {
	    	
	    	if (availSymbolsInColumn[i].remove(new Integer(element))) {//if the element existed in the collection, decrement count
	    		a.set(i, a.get(i)-1);//decrement count	
	    	}
	    }
	    
	    row.set(position, element);
	    this.availableInCol[position].remove(new Integer(element));
	    
	    path.add(position);
	}
	
	private void printVariables(int i_row, int element, int position) {
		System.out.println();
	    
    	System.out.println("-----------Iteration "+rowLength+" of "+i_row+"th row.");
    	System.out.println("The symbol "+element+" is selected for column "+position);
	    System.out.println("A:"+a);
	    System.out.println("ROW:"+row);
	    for (int i=0; i<n; i++)
	    	System.out.print("CFS "+i+":"+availColumnsForSymbol[i]);
	    System.out.println("");
	    for (int i=0; i<n; i++)
	    	System.out.print("SFC "+i+":"+availSymbolsInColumn[i]);
	    System.out.println("PATH:"+path);
	}
}

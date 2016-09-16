/**
 * Creation date: 02/09/2016
 * 
 */
package selvi_et_al.model.generators;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import seqgen.model.generators.AbstractSequentialGenerator;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import commons.generators.IRandomLatinSquareGenerator;
import commons.model.latinsquares.ILatinSquare;
import commons.utils.RandomUtils;

/**
 *  This method was developed by O'Carroll in 1963. It occasionally fails (very often). When it fails, it plays a sound and prints a message in the console.
 *  
 * @author igallego
 *
 */
public class OCarrollLSGenerator extends AbstractSequentialGenerator implements IRandomLatinSquareGenerator {
	
	protected boolean verbose = false;
	protected ArrayList<Integer> row;//the current row that is being generated in the method "generateRow"
    
	protected List<Integer> a;//from 0 to n-1 is Avail Symbol Count at Column i (SFC: "Symbols for Column" Count)
							  //from n to (2*n)-1 is Possibilities Count for Symbol i (CFS: "Columns for Symbol" Count)
    
	protected List<Integer>[] availSymbolsInColumn;//SFC: "Symbols For Column"
	protected List<Integer>[] availColumnsForSymbol;//CFS: "Columns For Symbol"
	protected List<Integer>[] initiallyAvailInColumn;
	
	protected int rowLength = 0;
	
	public OCarrollLSGenerator(int n) {
		super(n);
	}
	
	public static void main(String[] args) throws Exception {
		OCarrollLSGenerator generator;
		int i=1;
		while (true) {
			generator = new OCarrollLSGenerator(9);
			
			generator.setVerbose(true);
			ILatinSquare ls = generator.generateLS();
			
			System.out.println("Generation number "+i++);
			System.out.println(ls);
			
			if (!ls.preservesLatinProperty()) {
				System.out.println("ERROR: The square does not preserves the Latin property.");
				System.exit(0);
			}
		}
	}
	
	@Override
	public void setVerbose(boolean show) {
		this.verbose = show;
	}
	
	@Override
	public String getMethodName() {
		return "O'Carroll's row by row generation.";
	}

	@Override
	protected List<Integer> generateRow(int i_row) {
		
		this.restoreInitiallyAvailable();

		int position=0;
	    int element=0;
	    
		this.initializeAuxiliaryStructures(i_row);
	    int iteration=0;
	    //take the smallest non-zero value index S of A1 A2 ... A2n
	    int s=0;
	    int rowLength = 0;
	    while (s!=-1 && rowLength<n) {
	    	iteration++;
		    s = this.takeSmallestValueIndex(a);
		    
		    if (s==-1) {
		    	System.out.println("");
		    	System.out.println("O'Carroll's algorithm fails... exiting.");
		    	this.playSound();
		    	
		    	try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    	System.out.println(this.ls);
		    	System.exit(0);
		    }
		    //TAKE AN ELEMENT (SYMBOL) AND POSITION (COLUMN)
		    //CHECK (From Selvi's PAPER):
		    // 1) If S <= N, insert in the Sth position in the Rth row the Bth letter among those that can be entered in this position
		    // 2) If S >  N, insert the (S - N)th letter of the alphabet in the Bth position among those still open to it in the Rth row
		    if (s<=(n-1)) {
		    	position = s;
		    	element = RandomUtils.randomChoice(availSymbolsInColumn[position]);
		    } else {
		    	element = s-n;
		    	position = RandomUtils.randomChoice(availColumnsForSymbol[element]);
		    }
		    //count the choice: update array "a" and availSymbolInCol
	    	this.countTheChosenMove(element, position);
	    	
	    	rowLength++;
		    if (this.verbose) {
		    	this.printVariables(iteration, i_row, element, position);
		    }
	    }
	    return row;
	}

	protected int takeSmallestValueIndex(List<Integer> a) {
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
	protected void initializeAuxiliaryStructures(int i_row) {
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
	
	protected void countTheChosenMove(int symbol, int column) {
		//iterate through available symbols in the column before erasing the collection
		Iterator<Integer> iter = availSymbolsInColumn[column].iterator();
		while (iter.hasNext()) {
			Integer availSymbol = iter.next();
			a.set(availSymbol+n, a.get(availSymbol+n)-1);
	    	availColumnsForSymbol[availSymbol].remove(new Integer(column));//the column "position" is no longer available for the symbol "symbol"
		}   	
	    //remove element "element" from available of all columns, as it is now used in the row
	    iter = availColumnsForSymbol[symbol].iterator();
	    while (iter.hasNext()) {
			Integer availColumn = iter.next();
			//if (availSymbolsInColumn[column].remove(new Integer(element))) {//if the element existed in the collection, decrement count
			
			availSymbolsInColumn[availColumn].remove(new Integer(symbol));
	    	a.set(availColumn, a.get(availColumn)-1);//decrement count
		}
	    
		//as the symbol "element" is now used, there are no posible columns for it
	    availColumnsForSymbol[symbol].clear();
	    a.set(symbol+n, 0);
	    
	    //as the column "position" is now used, there are no available symbols in it
    	availSymbolsInColumn[column].clear();
	    a.set(column, 0);
	    
	    //finally, place the element in the row
	    row.set(column, symbol);
	    this.availableInCol[column].remove(new Integer(symbol));
	}
	
	@SuppressWarnings("unchecked")
	protected void restoreInitiallyAvailable() {
		//restore this collection
		initiallyAvailInColumn = new ArrayList[n];
		
		for (int i=0; i<n; i++) {
			initiallyAvailInColumn[i] = new ArrayList<Integer>();
			initiallyAvailInColumn[i].addAll(this.availableInCol[i]);
		}
	}
	
	protected void printVariables(int iteration, int i_row, Integer element, Integer position) {
		System.out.println();
	    
    	System.out.println("-----------Iteration "+iteration+" of "+i_row+"th row.");
    	System.out.println("The symbol "+element+" is selected for column "+position);
	    System.out.println("A:"+a);
	    System.out.println("ROW:"+row);
	    for (int i=0; i<n; i++)
	    	System.out.print("SFC "+i+":"+availSymbolsInColumn[i]);
	    System.out.println("");
	    for (int i=0; i<n; i++)
	    	System.out.print("CFS "+i+":"+availColumnsForSymbol[i]);
	    System.out.println("");
	    for (int i=0; i<n; i++)
	    	System.out.print("AIC "+i+":"+availableInCol[i]);
	    System.out.println("");
	}

	protected void playSound() {
		try {
			InputStream in = new FileInputStream("c:\\users\\ignacio\\ding.wav");
			AudioStream as = new AudioStream(in);
			// Use the static class member "player" from class AudioPlayer to play clip.
			AudioPlayer.player.start(as);
		} catch (Exception e) {
			System.out.println("Could not play sound.");
		}
	}
}

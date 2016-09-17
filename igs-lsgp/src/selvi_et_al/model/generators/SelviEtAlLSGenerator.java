/**
 * Creation date: 04/09/2016
 * 
 */
package selvi_et_al.model.generators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import commons.generators.IRandomLatinSquareGenerator;
import commons.model.OrderedPair;
import commons.model.latinsquares.ILatinSquare;
import commons.utils.RandomUtils;

/**
 * This is Selvi (et.al.) methos. It's the O'Carroll's modification to do backtracking when the method fails.
 * 
 *  
 * @author igallego
 *
 */
public class SelviEtAlLSGenerator extends OCarrollLSGenerator implements IRandomLatinSquareGenerator {
	
	protected List<OrderedPair> path;//save pairs of chosen (symbol, column) to do backtracking
	
	protected Set<Set<OrderedPair>> failedPaths = new HashSet<Set<OrderedPair>>(); 
	
	public SelviEtAlLSGenerator(int n) {
		super(n);
	}
	
	public static void main(String[] args) throws Exception {
		SelviEtAlLSGenerator generator = new SelviEtAlLSGenerator(20);
		
		int i = 1;
		while (i < 100) {
			generator.setVerbose(false);
			ILatinSquare ls = generator.generateLS();

			System.out.println();
			System.out.println("Generation number " + i++);
			System.out.println(ls);
			
			if (!ls.preservesLatinProperty()) {
				System.out.println("ERROR: The square does not preserves the Latin property.");
				System.exit(0);
			}
		}
	}
	
	@Override
	public String getMethodName() {
		return "Selvi (et.al.) row by row generation.";
	}

	@Override
	protected List<Integer> generateRow(int i_row) {
		this.path = new ArrayList<OrderedPair>();
		this.restoreInitiallyAvailable();
	    
		this.initializeAuxiliaryStructures(i_row);
	    int iteration=0;
	    rowLength = 0;
	    OrderedPair p = null;
	    while (rowLength<n) {
	    	iteration++;
	    	if (iteration%1000==0)
	    		System.out.println("Iteration nº "+iteration);
	    	
		    p = this.takeASymbolAndPosition(a);
		    
		    if (p==null) {
		    	//O'Carroll's method failed. Backtracking is needed.
		    	if (this.verbose) {
		    		System.out.println("PATH LENGTH:"+this.path.size());
		    	}
		    	
		    	//this.playSound();
		    	
		    	if (this.verbose) {
			    	this.printVariables(iteration, i_row, null, null);
			    }
		    	//Backtrack to previous move
		    	this.uncountOneMove();
		    	rowLength--;
		    	
		    	if (this.verbose) {
			    	this.printVariables(iteration, i_row, null, null);
			    }
		    } else {
		    	if (this.verbose) {
			    	this.printVariables(iteration, i_row, p.x, p.y);
			    }
		    	
			    //count the choice: update array "a" and auxiliary structures
		    	this.countTheChosenMove(p.x, p.y);
			    rowLength++;
			    
			    if (this.verbose) {
			    	this.printVariables(iteration, i_row, p.x, p.y);
			    }
		    }
	    }
	    return row;
	}

	@Override
	protected void countTheChosenMove(int element, int position) {
		super.countTheChosenMove(element, position);
		path.add(new OrderedPair(element, position));//save (symbol, column) 
	}
	
	protected void uncountOneMove() {
		//save the bad path, not to repeated in any order:
		Set<OrderedPair> newPath = new HashSet<OrderedPair>();
		newPath.addAll(this.path);
		this.failedPaths.add(newPath);
				
		OrderedPair p = this.path.remove(this.path.size()-1);//take last element in path (last movement)
		int symbol = p.x;
		int column = p.y;
		
		//inverse order of count move: first, erase element in the row
		row.set(column, new Integer(-1));
		this.availableInCol[column].add(symbol);//the element symbol is available again in that column
		
		//return "symbol" to available in all columns
		for (int j=0; j<n; j++) {
			if (this.availableInCol[j].contains(new Integer(symbol)) ) {//if symbol was initially available, return it
				if (row.get(j).intValue()==-1) {//if the place is NOT used
					availSymbolsInColumn[j].add(symbol);//return it to the available in each column
					a.set(column, a.get(column)+1);
				
					//now "symbol" is also in column j
					availColumnsForSymbol[symbol].add(j);
					a.set(symbol+n, a.get(symbol+n)+1);
				}
			}
		}
		
		//now iterate through available in column "column" != "symbol"
		Iterator<Integer> availAtColumn = this.availableInCol[column].iterator();
		while(availAtColumn.hasNext()) {
			Integer aSymbol = availAtColumn.next();
			if (!row.contains(aSymbol) && aSymbol.intValue()!=symbol) {
				//restore the collection availSymbolsInColumn that was erased
				availSymbolsInColumn[column].add(aSymbol);
				//after restoring the element, update "a"
				a.set(column, a.get(column)+1);

				//restore the collection availColumnsForSymbol that was erased
				availColumnsForSymbol[aSymbol].add(column);
				//after restoring the previous collection, set a(symbol)
				a.set(aSymbol+n, a.get(aSymbol+n)+1);		
			}
		}
		
	}
	
	protected OrderedPair takeASymbolAndPosition(List<Integer> a) {
		int index = -1;
		int minor = Integer.MAX_VALUE;
		
		Set<Integer> possibleColumns = new HashSet<Integer>();
		//first: search for the lowest non-zero value
		for (int i=0; i<=(2*n)-1; i++) {
			if (a.get(i).intValue()==0)
				continue;//if it is 0, discard
			if (a.get(i).intValue() < minor) {
				minor = a.get(i);
				index = i;
			}
		}
		if (index!=-1) {//if an index is found, take all the columns with the same value
			for (int i=0; i<=(2*n)-1; i++) {
				if (a.get(i).intValue() == minor) {
					possibleColumns.add(new Integer(i));
				}
			}
			//take the smallest non-zero value index S of A1 A2 ... A2n
			int s;
			boolean found = false;
			OrderedPair p = null;
			while(!found) {
				if (possibleColumns.isEmpty())
					return null;
				s = RandomUtils.randomChoice(possibleColumns);
				
				int position;
				int element;
				//TAKE AN ELEMENT (SYMBOL) AND POSITION (COLUMN)
			    //CHECK (From Selvi's PAPER):
			    //  1) If S <= N, insert in the Sth position the Bth letter among those that can be entered in this position (0<B<S B RANDOM) 
			    //  2) If S >  N, insert the (S - N)th letter of the alphabet in the Bth position among those still open to it in the Rth row (B RANDOM) 
			    if (s<=(n-1)) {
			    	position = s;
			    	if (availSymbolsInColumn[position].isEmpty())
			    		return null;
			    	
			    	element = RandomUtils.randomChoice(availSymbolsInColumn[position]);
			    } else {
			    	element = s-n;
			    	if (availColumnsForSymbol[element].isEmpty())
			    		return null;
			    	
			    	position = RandomUtils.randomChoice(availColumnsForSymbol[element]);
			    }
			    
			    p = new OrderedPair(element, position);
				Set<OrderedPair> newPath = new HashSet<OrderedPair>();
				newPath.addAll(this.path);
				newPath.add(p);
				
				//check if path is not in bad paths set
				Iterator<Set<OrderedPair>> badPaths = this.failedPaths.iterator();
				boolean itsAGoodPath = true;
				while (badPaths.hasNext()) {
					Set<OrderedPair> badPath = badPaths.next();
					
					if (newPath.containsAll(badPath)) {
						//it is a bad path
						possibleColumns.remove(new Integer(s));
						if (possibleColumns.isEmpty()) //if there are no more chances, must backtrack
							return null;
						itsAGoodPath = false;
						break;
					}
				}
				//it is a good path:
				found = itsAGoodPath;
			}
			return p;
		} else
			return null;
	}

	@Override
	protected void printVariables(int iteration, int i_row, Integer element, Integer position) {
		super.printVariables(iteration, i_row, element, position);
		
		System.out.println("PATH:"+path);
//		Iterator<Set<OrderedPair>> iterator = this.failedPaths.iterator();
//		while (iterator.hasNext()) {
//			Set<OrderedPair> badPath = iterator.next();
//			System.out.println("BAD PATH:"+badPath);
//		}
	}
}

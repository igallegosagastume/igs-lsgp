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
	
	protected List<List<OrderedPair>> failedPaths = new ArrayList<List<OrderedPair>>(); 
	
	public SelviEtAlLSGenerator(int n) {
		super(n);
		this.verbose = false;//default
	}
	
	public static void main(String[] args) throws Exception {
		SelviEtAlLSGenerator generator = new SelviEtAlLSGenerator(7);
		generator.setVerbose(true);
		int i = 1;
		while (i < 100000) {
			
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
	    	
	    	if (this.verbose && iteration%1000==0)
	    		System.out.println("Iteration nº "+iteration);
	    	
		    p = this.takeASymbolAndPosition();
		    
		    if (p==null) {
		    	//O'Carroll's method failed. Backtracking is needed.
		    	
		    	if (this.verbose) {
			    	this.printVariables(iteration, i_row, null, null);
			    }
		    	//Backtrack to previous move
		    	this.uncountOneMove();
		    	
		    	if (this.verbose) {
			    	this.printVariables(iteration, i_row, null, null);
			    }
		    } else {
		    	if (this.verbose) {
			    	this.printVariables(iteration, i_row, p.x, p.y);
			    }
		    	
			    //count the choice: update array "a" and auxiliary structures
		    	this.countTheChosenMove(p.x, p.y);
			    
			    if (this.verbose) {
			    	this.printVariables(iteration, i_row, p.x, p.y);
			    }
		    }
	    }
	    return row;
	}

	@Override
	protected void initializeAuxiliaryStructures(int i_row) {
		super.initializeAuxiliaryStructures(i_row);
		this.failedPaths = new ArrayList<List<OrderedPair>>();//when starting a new row, all the bad paths are no longer valid. Begin again.
	}
	
	private boolean checkA() {
		for (int i=0; i<n; i++) {
			if (this.availSymbolsInColumn[i].size()!=a.get(i) ||
				this.availColumnsForSymbol[i].size()!=a.get(i+n)) {
				return false;
			}
		}
		return true;
	}
	@Override
	protected void countTheChosenMove(int element, int position) {
		super.countTheChosenMove(element, position);
		path.add(new OrderedPair(element, position));//save (symbol, column)
		rowLength++;
	}
	
	protected void uncountOneMove() {
		//save the bad path, not to be repeated 
		List<OrderedPair> newPath = new ArrayList<OrderedPair>();
		newPath.addAll(this.path);
		
		this.failedPaths.add(newPath);
		
		OrderedPair p = this.path.remove(this.path.size()-1);//take last element in path (last movement)
		int symbol = p.x;
		int column = p.y;
		
		//inverse order of count move: first, erase element in the row
		row.set(column, new Integer(-1));
		this.availableInCol[column].add(symbol);//the element symbol is available again in that column
		
		
		//iterate all symbols available in the column now free
		Iterator<Integer> availAtColumn = this.availableInCol[column].iterator();
		while(availAtColumn.hasNext()) {
			//return "symbolNowAvail" to available in all columns
			Integer symbolNowAvail = availAtColumn.next();
			for (int j=0; j<n; j++) {									//iterate all columns
				if ((row.get(j).intValue()==-1) &&						//if the place is NOT used 
					(!row.contains(symbolNowAvail)) &&  				//if not used in the row
					this.availableInCol[j].contains(symbolNowAvail) 	//if symbol was initially available, return it
//					(availSymbolsInColumn[j].contains(symbolNowAvail))  //the symbol is not already in the collection
					) {                    
						
						//return it to the available in column j 
						if (availSymbolsInColumn[j].add(symbolNowAvail)) {
							a.set(j, a.get(j)+1);	
						}
					
						//now "symbol" is also in column j
						if (availColumnsForSymbol[symbolNowAvail].add(j)) {
							a.set(symbolNowAvail+n, a.get(symbolNowAvail+n)+1);	
						}
						
				}
			}
		}
		rowLength--;
	}
	
	protected OrderedPair takeASymbolAndPosition() {
		int index = -1;
		int minor = Integer.MAX_VALUE;
		
		Set<Integer> possibleColumns = new HashSet<Integer>();
		//first: search for the lowest non-zero value
		for (int i=0; i<=(2*n)-1; i++) {
			if (a.get(i).intValue()==0)
				continue;//if it is 0, discard
			if (a.get(i).intValue() < minor) {
				minor = a.get(i).intValue();
				index = i;
			}
		}
		if(index==-1) {//if no non-zero value is found, return null to backtrack
			if (this.verbose)
				System.out.println("No non-zero value is found... backtracking.");
			return null;
		}

		//if an index is found, take all the columns with the same value
		for (int i=0; i<=(2*n)-1; i++) {
			if (a.get(i).intValue() == minor) {
				possibleColumns.add(new Integer(i));
			}
		}
//		if (possibleColumns.size()>=(2*n) && minor==1) {
//			System.out.println("Veamos...");
//		}
		//take the smallest non-zero value index S of A1 A2 ... A2n
		int s;
		boolean found = false;
		OrderedPair p = null;
		int position;
		int element;
		while(!found) {
			s = RandomUtils.randomChoice(possibleColumns);
			
			//TAKE AN ELEMENT (SYMBOL) AND POSITION (COLUMN)
		    //CHECK (From Selvi's PAPER):
		    //  1) If S <= N, insert in the Sth position the Bth letter among those that can be entered in this position (0<B<S B RANDOM) 
		    //  2) If S >  N, insert the (S - N)th letter of the alphabet in the Bth position among those still open to it in the Rth row (B RANDOM)
			
//				System.out.println("S:"+s);
//				System.out.println("minor:"+minor);
//				System.out.println("index:"+index);
		    if (s<=(n-1)) {
		    	position = s;
		    	element = RandomUtils.randomChoice(availSymbolsInColumn[position]);
		    } else {
		    	element = s-n;
		    	position = RandomUtils.randomChoice(availColumnsForSymbol[element]);
		    }
			
		    p = new OrderedPair(element, position);
			List<OrderedPair> newPath = new ArrayList<OrderedPair>();
			newPath.addAll(this.path);
			newPath.add(p);
			
			//check if path is not in bad paths set
			Iterator<List<OrderedPair>> badPaths = this.failedPaths.iterator();
			boolean itsAGoodPath = true;
			while (badPaths.hasNext()) {
				List<OrderedPair> badPath = badPaths.next();
				
				if (this.pathsExactlyEqual(newPath, badPath)) {
					//it is a bad path
					possibleColumns.remove(new Integer(s));
					if (possibleColumns.isEmpty()) {//if there are no more chances, must backtrack
						if (this.verbose)
							System.out.println("There are no more possible columns... backtracking.");
						return null;
					}
					itsAGoodPath = false;
					break;
				}
			}
			//it is a good path:
			found = itsAGoodPath;
		}
		if (this.verbose)
			System.out.println("Chosen move is: "+p.toString());
		return p;
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
		if (!this.checkA()) {
    		System.out.println("Anomalia en actualizacion");
    		System.exit(0);
    	} /*else 
    		System.out.println("CheckA OK!");*/
	}
	
	private boolean pathsExactlyEqual(List<OrderedPair> newPath, List<OrderedPair> badPath) {
		//Compare paths exactly equal in size and order.
		
		if (newPath.size()!=badPath.size())
			return false;
		
		for (int i=0; i<newPath.size(); i++) {
			if (!newPath.get(i).equals(badPath.get(i))) {
				return false;
			}
		}
		return true;
	}
}

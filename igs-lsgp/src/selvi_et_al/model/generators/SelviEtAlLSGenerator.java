/**
 * Creation date: 04/09/2016
 * 
 */
package selvi_et_al.model.generators;

import java.util.ArrayList;
import java.util.List;

import commons.generators.IRandomLatinSquareGenerator;
import commons.model.OrderedPair;
import commons.model.latinsquares.ILatinSquare;
import commons.utils.RandomUtils;

/**
 * TODO: This method is under development ...
 *  
 * @author igallego
 *
 */
public class SelviEtAlLSGenerator extends OCarrollLSGenerator implements IRandomLatinSquareGenerator {
	
	protected List<OrderedPair> path;//save pairs of chosen (symbol, column) to do backtracking
	
	public SelviEtAlLSGenerator(int n) {
		super(n);
	}
	
	public static void main(String[] args) throws Exception {
		SelviEtAlLSGenerator generator;// = new SelviEtAlLSGenerator(9);
		
		int i = 1;
		while (i < 100) {
			generator = new SelviEtAlLSGenerator(9);

			generator.setVerbose(false);
			ILatinSquare ls = generator.generateLS();

			System.out.println();
			System.out.println("Generation number " + i++);
			System.out.println(ls);
		}
	}
	
	@Override
	public String getMethodName() {
		return "Selvi's (et.al.) row by row generation.";
	}

	@Override
	protected List<Integer> generateRow(int i_row) {
		this.path = new ArrayList<OrderedPair>();
		this.restoreInitiallyAvailable();

		int position=0;
	    int element=0;
	    
		this.initializeAuxiliaryStructures(i_row);
	    
	    //take the smallest non-zero value index S of A1 A2 ... A2n
	    int s=0;
	    rowLength = 0;
	    
	    while (rowLength<n) {
		    s = this.takeSmallestValueIndex(a);
		    
		    if (s==-1) {//O'Carroll's method has failed. Begin again or backtrack.
		    	System.out.println();
		    	System.out.println("O'Carroll's method failed. Backtracking is done.");
		    
		    	this.printVariables(i_row, element, position);
		    	//Backtrack to previous move
		    	OrderedPair p = this.path.remove(this.path.size()-1);//take last element in path (last movement)
		    	
		    	this.uncountOneMove(p.x, p.y);
		    	rowLength--;
		    	
		    	this.printVariables(i_row, element, position);
		    	
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

	@Override
	protected void countTheChosenMove(int element, int position) {
		super.countTheChosenMove(element, position);
		path.add(new OrderedPair(element, position));//save (symbol, column) 
	}
	
//	@Override
//	protected int takeSmallestValueIndex(List<Integer> a) {
//		int index = -1;
//		int minor = Integer.MAX_VALUE;
////		List<Integer> posibleColumns = new ArrayList<Integer>();
//		
//		for (int i=0; i<=(2*n)-1; i++) {
//			if (a.get(i).intValue()==0)
//				continue;//if it is 0, discard
//			if (a.get(i).intValue() < minor) {
//				minor = a.get(i);
//				index = i;
//			}
//		}
////		if (index!=-1) {
////			for (int i=0; i<=(2*n)-1; i++) {
////				if (a.get(i).intValue() == minor) {
////					posibleColumns.add(new Integer(i));
////				}
////			}
////		
////			return RandomUtils.randomChoice(posibleColumns);
////		} else
////			return -1;
//		return index;
//	}

	@Override
	protected void printVariables(int i_row, int element, int position) {
		super.printVariables(i_row, element, position);
		System.out.println("");
		System.out.println("PATH:"+path);
	}
}

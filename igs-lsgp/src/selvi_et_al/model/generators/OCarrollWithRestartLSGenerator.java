/**
 * Creation date: 04/09/2016
 * 
 */
package selvi_et_al.model.generators;

import java.io.IOException;
import java.util.List;

import commons.generators.IRandomLatinSquareGenerator;
import commons.model.latinsquares.ILatinSquare;
import commons.utils.RandomUtils;

/**
 * This method, is the O'Carroll method with a variation: when the method fails, it erases the row and begins again.
 *  
 * @author igallego
 *
 */
public class OCarrollWithRestartLSGenerator extends OCarrollLSGenerator implements IRandomLatinSquareGenerator {
	
	
	public OCarrollWithRestartLSGenerator(int n) {
		super(n);
	}
	
	public static void main(String[] args) throws Exception {
		OCarrollWithRestartLSGenerator generator;
		int i=1;
		while (i<100) {
			generator = new OCarrollWithRestartLSGenerator(30);
			
			generator.setVerbose(false);
			ILatinSquare ls = generator.generateLS();
			
			System.out.println();
			System.out.println("Generation number "+i++);
			System.out.println(ls);
			
			if (!ls.preservesLatinProperty()) {
				System.out.println("ERROR: The square does not preserves the Latin property.");
				System.exit(0);
			}
		}
	}
	
	@Override
	public String getMethodName() {
		return "OCarroll with restarting row generation.";
	}

	@Override
	protected List<Integer> generateRow(int i_row) {
		this.restoreInitiallyAvailable();

		int position=0;
	    int element=0;
	    
		this.initializeAuxiliaryStructures(i_row);
	    
	    //take the smallest non-zero value index S of A1 A2 ... A2n
	    int s=0;
	    int iteration=0;
	    rowLength = 0;
	    while (rowLength<n) {
	    	iteration++;
		    s = this.takeSmallestValueIndex(a);
		    
		    if (s==-1) {//O'Carroll's method has failed. Begin again or backtrack.
		    	if (this.verbose) {
		    		System.out.println();
		    		System.out.println("O'Carroll's method failed. Begin again with row "+i_row+".");
		    		System.out.println();
		    		    	
			    	try {
						System.in.read();
					} catch (IOException e) {
						e.printStackTrace();
					}
		    	}
		    	this.initializeAuxiliaryStructures(i_row);
		    	
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
		    	this.printVariables(iteration, i_row, element, position);
		    }
	    }
	    return row;
	}
}

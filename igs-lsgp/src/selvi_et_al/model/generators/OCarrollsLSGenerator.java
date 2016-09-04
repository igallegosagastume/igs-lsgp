/**
 * Creation date: 02/09/2016
 * 
 */
package selvi_et_al.model.generators;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
public class OCarrollsLSGenerator extends AbstractSequentialGenerator implements IRandomLatinSquareGenerator {
	
	boolean verbose = false;
	
	public OCarrollsLSGenerator(int n) {
		super(n);
	}
	
	public static void main(String[] args) throws Exception {
		OCarrollsLSGenerator generator;
		int i=1;
		while (true) {
			generator = new OCarrollsLSGenerator(9);
			
			generator.setVerbose(true);
			ILatinSquare ls = generator.generateLS();
			
			System.out.println("Generation number "+i++);
			System.out.println(ls);
			
			
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

	@SuppressWarnings("unchecked")
	@Override
	protected List<Integer> generateRow(int i_row) {
		//HashSet<Integer> availableInRow = new HashSet<Integer>(this.symbols);//all symbols initially available in the row 
	    
	    ArrayList<Integer> row = new ArrayList<Integer>(this.n);
	    
	    List<Integer> a = new ArrayList<Integer>(2*n);//from 0 to n-1 is Avail Symbol Count at Column i
	    											  //from n to (2*n)-1 is Possibilities Count for Symbol i
		List<Integer>[] availSymbolsInColumn = new ArrayList[n];
		List<Integer>[] availColumnsForSymbol = new ArrayList[n];
	    
	    //initialize the array "posibilities (available columns) for each symbol"
	    for (int i=0; i<=n-1; i++) {
	    	availColumnsForSymbol[i] = new ArrayList<Integer>();
	    }
	    
	    //initialize a (ASCAI)
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
		    	System.out.println("");
		    	System.out.println("O'Carroll's algorithm fails... exiting.");
		    	this.playSound();
		    	
		    	try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    	
		    	System.exit(0);
		    }
		    int position;
		    int element; 
		    
		    //CHECK: 1) If S <= N, insert in the Sth position in the Rth row the Bth letter among those that can be entered in this position
		    //       2) If S >  N, insert the (S - N)th letter of the alphabet in the Bth position among those still open to it in the Rth row
		    if (s<=(n-1)) {
		    	position = s;
		    	element = RandomUtils.randomChoice(availSymbolsInColumn[position]);
		    } else {
		    	element = s-n;
		    	position = RandomUtils.randomChoice(availColumnsForSymbol[element]);
		    }
		    //count the choice: update array "a" and availSymbolInCol
	    	
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
		    rowLength++;
		    if (this.verbose) {
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
		    }
	    }
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

	private void playSound() {
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

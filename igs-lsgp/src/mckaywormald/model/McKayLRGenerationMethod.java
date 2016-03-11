/**
 * Creation date: 09/03/2016
 * 
 */
package mckaywormald.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import commons.OrderedTriple;
import commons.RandomUtils;

/**
 * @author igallego
 *
 */
public class McKayLRGenerationMethod {
	
	//auxiliary variables for the algorithm
	private Set<Integer> initiallyAvInRow = null;
//	private HashSet<Integer>[] availableInCol = null;
	
	
	private int conflictsCount = 0;
	private boolean overlappingConflicts = false;
	private int[][] timesSymbolOccursInColumn = null; 
	private List<OrderedTriple> conflicts = null; 
	
	
	public McKayLRGenerationMethod() {
		RandomUtils.initRand();
	}
	
	public LatinRectangle generateLR(int k, int n) {
		this.initiallyAvInRow = RandomUtils.oneToN(n);
		boolean rejected = false;
		LatinRectangle a = null;
		double pow = Math.pow(n, 2);
		
		int outerLoop = 0;
		int maxInnerLoop = 0;
		do {
			if (outerLoop%1000==0)
				System.out.println("Outer:"+outerLoop);
			
			outerLoop++;
			int matrixCount = 0;
			do {
		    	a = randomMemberOfMkn(k, n);
		    	matrixCount++;
		    	System.out.println("Generated matrix A N°"+matrixCount+" with conflicts count:"+this.conflictsCount+" (>"+pow+"?) Overlapping:"+this.overlappingConflicts);
		    } while (this.conflictsCount>pow || this.overlappingConflicts);
	
		    rejected = false;
		    
		    
//		    System.out.println("");
//		    System.out.print("Begin inner loop");
		    int innerLoop = 0;
		    
		    while (this.conflictsCount>0 && !rejected) {
		    	
		    	innerLoop++;
		    	if (innerLoop>maxInnerLoop) {
		    		maxInnerLoop=innerLoop;
		    		System.out.println("MaxInnerLoop:"+maxInnerLoop);
		    	}
		    	OrderedTriple conflict = RandomUtils.randomTriple(this.conflicts);
		    	
		    	int i1 = conflict.x;
		    	int i2 = conflict.y;
		    	int j1 = conflict.z;
		    	
		    	Set<Integer> nMinusj1 = new HashSet<Integer>();
		    	nMinusj1.addAll(this.initiallyAvInRow);
		    	nMinusj1.remove(j1);
		    	
		    	int j2 = RandomUtils.randomChoice(nMinusj1);
		    	
		    	nMinusj1.remove(j2);
		    	
		    	int j3 = RandomUtils.randomChoice(nMinusj1);
		    	
		    	//test if (i1,i2,j1,j2,j3) \in sw(a)
		    	int y = a.getValueAt(i1, j1);
		    	int u = a.getValueAt(i1, j2);
		    	int v = a.getValueAt(i1, j3);
		    	
		    	//x1
		    	boolean x1 = (y == a.getValueAt(i2, j1));
		    	
		    	//x2 : u \notIn A[C_{j2} - {i1,j2}]
		    	boolean x2 = (this.timesSymbolOccursInColumn[u][j2]==1);
		    	//x3 : v \notIn A[C_{j3} - {i1,j3}]
		    	boolean x3 = (this.timesSymbolOccursInColumn[v][j3]==1);
		    	//x4 : y \notIn A[C_{j2}]
		    	boolean x4 = (this.timesSymbolOccursInColumn[y][j2]==0);
		    	//x5 : u \notIn A[C_{j3}] 
		    	boolean x5 = (this.timesSymbolOccursInColumn[u][j3]==0);
		    	//x6 : v \notIn A[C_{j1}]
		    	boolean x6 = (this.timesSymbolOccursInColumn[v][j1]==0);
		    	
		    	if (x1 && x2 && x3 && x4 && x5 && x6) {// (i1,i2,j1,j2,j3) \in sw(a), apply the switching
		    	  	a.setValueAt(i1, j1, v);
		    	  	a.setValueAt(i1, j2, y);
		    	  	a.setValueAt(i1, j3, u);
		    	  	rejected = false;
		    	} else {
		    		rejected = true;
		    		//System.out.println("");
		    	}
		    	this.conflicts.remove(conflict);
	    	  	this.conflictsCount--;
		    }
		} while (rejected);
	    return a;
	}
	
	private LatinRectangle randomMemberOfMkn(int k, int n) {
		this.conflictsCount = 0;
		this.overlappingConflicts = false;
		this.conflicts = new ArrayList<OrderedTriple>();
		
		//from mckay
		int[][] columnOfRowValue = new int[k][n];
		this.timesSymbolOccursInColumn = new int[n][n]; //tells how many times a symbol occurs in the column
	    
		LatinRectangle lr = new LatinRectangle(k, n);
	    
	    for (int i=0; i<k; i++) {
	    	ArrayList<Integer> row = this.generateRow(i, n, lr, columnOfRowValue);
	    	
	    	lr.setRow(i, row);
	    }
	    return lr;
	}
	        
	private ArrayList<Integer> generateRow(int rowIndex, int n, LatinRectangle ls, int[][] columnOfRowValue) {
	    HashSet<Integer> availableInRow = new HashSet<Integer>();
	    availableInRow.addAll(this.initiallyAvInRow);
	    
	    ArrayList<Integer> row = new ArrayList<Integer>();
	    int colIndex = 0;
	    while (colIndex < n) {
	    	//select symbol
	    	Integer symbol = RandomUtils.randomChoice(availableInRow);

	    	this.timesSymbolOccursInColumn[symbol][colIndex]++;
	    	
	    	//conflict checks
            if (this.timesSymbolOccursInColumn[symbol][colIndex]>1) {//!this.availableInCol[colIndex].contains(symbol)) {
            	this.conflictsCount++;
            	int last = this.lastRowIndexOf(symbol, ls, rowIndex, colIndex);
            	this.conflicts.add(new OrderedTriple(last, rowIndex, colIndex));
            	if (this.timesSymbolOccursInColumn[symbol][colIndex]>2) {
            		this.overlappingConflicts=true;
            	}
            }

            columnOfRowValue[rowIndex][symbol] = colIndex;
            
            //remove from available
            availableInRow.remove(symbol);

            //put symbol in result
            row.add(symbol);

            colIndex++;
	    }
	    
	    return row;
	}
	
	private int lastRowIndexOf(int symbol, LatinRectangle ls, int row, int col) {
		int result = -1;
		boolean found = false;
		int i = row-1;
		while (!found && i>=0) {
			found = (ls.getValueAt(i,col)==symbol);
			if (found)
				result = i;
			i--;
		}
		return result;
	}
	        
	public String getMethodName() {
		return "McKay generation of Latin rectangle.";
	}

}
		        
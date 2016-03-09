/**
 * Creation date: 09/03/2016
 * 
 */
package mckaywormald.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import mckaywormald.model.LatinRectangle;

import commons.RandomUtils;

/**
 * @author igallego
 *
 */
public class McKayLRGenerationMethod {
	
		Set<Integer> initiallyAvInRow = null;
	
		public McKayLRGenerationMethod() {
			RandomUtils.initRand();
		}
		
		public LatinRectangle generateLS(int n) {
			this.initiallyAvInRow = RandomUtils.oneToN(n);
			
		    LatinRectangle ls = new LatinRectangle(n, n);
		    	    
//		    //disponibles en cada columna
//		    HashSet<Integer>[] availableInCol = new HashSet[n];
//		    //availableInCol = [ [y for y in range(1,n+1) ] for x in range(1, n+1) ] //inicialmente todos los numeros en cada columna
//		    for (int i=0; i<n; i++) {
//		    	availableInCol[i] = new HashSet<Integer>();
//		    	for (int j=0; j<n; j++) {
//		    		availableInCol[i].add(j);
//		    	}
//		    }

//		    Integer[] failedAttemptsPerRow = new Integer[n];
//		    for (int i=0; i<n; i++) {
//		    	failedAttemptsPerRow[i] = 0;
//		    }
		    
//		    int[][] collisions = new int[n][n];// = [ [0 for k in range(n)] for j in range(n)]
//		    for (int i=0; i<n; i++) 
//		    	for (int j=0; j<n; j++) {
//		    		collisions[i][j]=0;
//		    	}

		    for (int i=0; i<n; i++) {
		    	ArrayList<Integer> row = this.generateRow(i, n, ls);
		    	
		    	ls.setRow(i, row);
		    }

		    return ls;
		  }
		        
		        
		private ArrayList<Integer> generateRow(int i_row, int n, LatinRectangle ls) {
		    //disponibles en row actual
		    HashSet<Integer> availableInRow = new HashSet<Integer>();//    = [ i for i in range(1,n+1) ]
		    
		    //inicialmente los elementos de 0 a n-1
		    availableInRow.addAll(initiallyAvInRow);
		    
		    //resultado del algoritmo
		    ArrayList<Integer> row = new ArrayList<Integer>();
		    int i_col = 0;
		    
		    while (i_col < n) {//cuando llega a n, elegi n numeros
		    	
		            Integer symbol = RandomUtils.randomChoice(availableInRow);
		            i_col = i_col + 1;
		            availableInRow.remove(symbol);
		            row.add(symbol);
		    }
		    
		    return row;
		}
		        
		public String getMethodName() {
			return "McKay generation of Latin rectangle.";
		}

	}
		        
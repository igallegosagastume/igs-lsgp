/**
 * Creation date: 08/07/2014
 * 
 * Master thesis on Latin Squares generation
 * 
 */

/**
 * © Copyright 2012-2015 Ignacio Gallego Sagastume
 * 
 * This file is part of IGS-ls-generation package.
 * IGS-ls-generation package is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * IGS-ls-generation package is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with IGS-ls-generation package.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */
package basicImpl.model;

import java.util.ArrayList;
import java.util.HashSet;

import basicImpl.utils.RandomUtils;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
@Deprecated
public class SimpleGenWithSwapping extends SimpleGen {

	public SimpleGenWithSwapping(int n) {
		super(n);
	}
	/*public LatinSquare genLS(int n) { 
	    //ls inicial, n filas vacias
	    ArrayList<Integer>[] ls = new ArrayList[n];// = [ [0 for k in range(1,n+1)] for j in range(1, n+1)]
	    
	    for (int i=0; i<n; i++) {
	    	ls[i] = new ArrayList<Integer>();
	    }
	    
	    //disponibles en cada columna
	    HashSet<Integer>[] availableInCol = new HashSet[n];
	    //availableInCol = [ [y for y in range(1,n+1) ] for x in range(1, n+1) ] //inicialmente todos los numeros en cada columna
	    for (int i=0; i<n; i++) {
	    	availableInCol[i] = new HashSet<Integer>();
	    	for (int j=0; j<n; j++) {
	    		availableInCol[i].add(j);
	    	}
	    }

	    Integer[] failedAttemptsPerRow = new Integer[n];
	    for (int i=0; i<n; i++) {
	    	failedAttemptsPerRow[i] = 0;
	    }
	    
	    int[][] collisions = new int[n][n];// = [ [0 for k in range(n)] for j in range(n)]
	    for (int i=0; i<n; i++) 
	    	for (int j=0; j<n; j++) {
	    		collisions[i][j]=0;
	    	}

	    for (int i=0; i<n; i++) {
	    	ArrayList<Integer> row = this.generateRow(i, n, ls, availableInCol, failedAttemptsPerRow, collisions);
	    	if (failedAttemptsPerRow[i]>1000000) {
		        System.out.print("Failed attempts in row "+i+"="+failedAttemptsPerRow[i]);
		    }
	    	ls[i] = row;
	    }
	    LatinSquare ls1 = new LatinSquare(n);
	    ls1.setLs(ls);
	    return ls1;
	  }*/	        
	     
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Integer> generateRow(int i_row, int n, LatinSquare ls, HashSet<Integer>[] availableInCol, Integer[] failedAttemptsPerRow, int[][] collisions) {
	    //genero row de tamanio n en la posicion i

	    //disponibles en row actual
	    HashSet<Integer> availableInRow = new HashSet<Integer>();//    = [ i for i in range(1,n+1) ]
	    
	    //inicialmente los elementos de 0 a n-1
	    for (int j=0; j<n; j++) {
	    		availableInRow.add(j);
	    }
	    
	    boolean haveSwappedBefore = false;
	    
	    //resultado del algoritmo
	    ArrayList<Integer> row = new ArrayList<Integer>();
	    int i_col = 0;
	    
	    //intentos fallidos por cada columna en la row actual
	    HashSet<Integer>[] failedAttemptsInCol = new HashSet[n];// = [[] for i in range(1,n+1)]
	    for (int i=0; i<n; i++) {
	    	failedAttemptsInCol[i] = new HashSet<Integer>();
	    }
	    int failedInRowCount = 0;
	    while (i_col < n) {//cuando llega a n, elegi n numeros
	        //conjunto de disponibles es:
	        HashSet<Integer> available = new HashSet<Integer>();
	        available.addAll(availableInCol[i_col]);//list((set(availableInCol[i_col]) & set(availableInRow)) - set(failedAttemptsInCol[i_col]))
	    	available.retainAll(availableInRow);
	    	available.removeAll(failedAttemptsInCol[i_col]);
	    	
	        if (!available.isEmpty()) { //si me quedan disponibles
	            //saco un simbolo al azar:
	            Integer symbol = RandomUtils.randomChoice(available);
	            //System.out.print("Saco simbolo:"+repr(simbolo)+" de available: "+repr(available))
	            
	            //contabilizo el elegido
	            availableInCol[i_col].remove(symbol);
	            availableInRow.remove(symbol);
	            row.add(symbol);
	            i_col++;
	        } else {
	            /*;*/
	        	
	        	boolean couldSwap = this.swap(row, ls, availableInRow, availableInCol, failedAttemptsInCol, i_col, n);
	        	if (couldSwap) {
	        		i_col++;
	        		haveSwappedBefore = true;
	        	} else {
	        		//collision
	        		failedInRowCount = failedInRowCount + 1;
		            collisions[i_row][i_col] = collisions[i_row][i_col] + 1;
		            
		            //limpio los fallidos de las columnas de mas a la derecha
		            for (int i=i_col; i<n; i++) {
		            	failedAttemptsInCol[i] = new HashSet<Integer>();
		            }
		            //hago backtracking
		            i_col = i_col - 1;
		            //saco el ultimo simbolo de la row
		            Integer symbol = row.get(row.size()-1);//tomo el ultimo
		            row.remove(row.size()-1);
		            //guardo el intento fallido
		            failedAttemptsInCol[i_col].add(symbol);
		            //vuelvo a agregar el simbolo a disponibles
		            availableInRow.add(symbol);
		            availableInCol[i_col].add(symbol);
	        		
	        		if (haveSwappedBefore) {
	        			
	        		}
	        	}
	        }
	    }
	    failedAttemptsPerRow[i_row] = failedInRowCount;
	    return row;
}
	
	public boolean swap(ArrayList<Integer> row, 
						LatinSquare ls, 
						HashSet<Integer> availableInRow, 
						HashSet<Integer>[] availableInCol, 
						HashSet<Integer>[] failedAttemptsInCol,
						int i_col, int n) {
		boolean found = false;
		int i_swap1 = 0;
		HashSet<Integer> leftToTry = new HashSet<Integer>();
		leftToTry.addAll(availableInRow);
		
		Integer elemInHand = null;
		while (!leftToTry.isEmpty() && !found) {
			
			elemInHand = RandomUtils.randomChoice(leftToTry);
			leftToTry.remove(elemInHand);
	
			//inicio busqueda de columna anterior para hacer swap
			while (!found && i_swap1<row.size()) {
				HashSet<Integer> availableInColISwap = new HashSet<Integer>();
				availableInColISwap.addAll(availableInCol[i_swap1]);
		        availableInColISwap.add(row.get(i_swap1));
		        
		        HashSet<Integer> availableInCurrentCol = new HashSet<Integer>();
		        availableInCurrentCol.addAll(availableInCol[i_col]);
		        
				found = availableInColISwap.contains(elemInHand) && availableInCurrentCol.contains(row.get(i_swap1));
				if (!found) 
					i_swap1 ++;
			}
			
		}
		if (found) {// do swap
			Integer simbAnt = row.get(i_swap1);
	    	//swap
	    	row.set(i_swap1, elemInHand);
	    	row.add(simbAnt);
	    	//available in col i_swap1
	    	availableInCol[i_swap1].remove(elemInHand);
	    	availableInCol[i_swap1].add(simbAnt);
	    	//available in col i_col
	    	availableInCol[i_col].remove(simbAnt);
	    	availableInRow.remove(elemInHand);
	    	
	    	//limpio los fallidos de las columnas de mas a la derecha
	        for (int i=i_swap1; i<n; i++) {
	        	failedAttemptsInCol[i] = new HashSet<Integer>();
	        }
			return true;
		} else {
			return false; //could not swap
		}
	}
}
	        
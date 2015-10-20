/**
 * Creation date: 04/08/2014
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
public class SimpleGenWithCycleSwapping extends SimpleGenWithSwapping {

	public SimpleGenWithCycleSwapping(int n) {
		super(n);
	}
	
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
	        	
	        	boolean couldSwap = this.swap(row, ls, availableInRow, availableInCol, i_col, n);
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

	
	public boolean swap(ArrayList<Integer> row, LatinSquare ls,
			HashSet<Integer> availableInRow, HashSet<Integer>[] availableInCol, int i_col, int n) {
		boolean finished = false;
		int i_swap1 = 0;//empiezo desde la columna 0
		//tomo un elemento de los disponibles en la fila
		Integer elemInHand = RandomUtils.randomChoice(availableInRow);
		row.add(elemInHand);//dejo elemento con colision en la fila i_col;
		availableInRow.remove(elemInHand);

		boolean found = availableInCol[i_col].remove(elemInHand);
		
		// inicio busqueda de columna anterior para hacer swap
		while (!finished && i_swap1 < i_col) {
			if (availableInCol[i_swap1].contains(elemInHand)) {//si puedo intercambiar en i_swap1
				//do swap
				Integer simbAnt = row.get(i_swap1);
				// swap
				row.set(i_swap1, elemInHand);
				row.set(i_col, simbAnt);
				// corrijo disponibles en fila anterior
				availableInCol[i_swap1].remove(elemInHand);
				availableInCol[i_swap1].add(simbAnt);
				// corrijo disponibles en ultima col
				
				/*en la columna i_col: si el que saco de la columna esta repetido (habia colision) no agrego a disponibles.
				 *                     lo que si hago es agregar a la columna, entonces intento eliminar de disponibles:
				 *                                 si elimino, quiere decir que puse un elemento distinto,
				 *                                 si no elimino , sigo probando porque hay colision en i_col. 
				 */
				
				found = availableInCol[i_col].remove(simbAnt);//si logro borrar, elimine la impropiedad
				
				if (!found) {
					i_swap1++;//si no elimine la impropiedad, empiezo desde la ultima columna
					if (i_swap1==i_col) {
						i_swap1=0;
					}
				}
				//tomo ultimo elemento
				elemInHand = row.get(i_col);
			} else {//si no puedo intercambiar en esa columna, muevo i_swap1
					i_swap1++;
					if (i_swap1==i_col) {
						i_swap1=0;
					}
			}
			
			finished = found;//availableInCol[i_col].contains(elemInHand);//si hay colision, sigo
			
		}
	if (!finished)
		i_swap1=0;
	return finished;
	}
}

/**
 * Creation date: 07/06/2014
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
package jacomatt.model;

import jacomatt.utils.ArrayUtils;
import jacomatt.utils.DrawingOptions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import commons.OrderedTriple;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */

public class EfficientIncidenceCube extends IncidenceCube {
	private final int nullInt = -99999; //a number outside the scope of symbols
	private final int minus0  = -10000;
	//Each view is stored as a two-dimensional array, 
	//to avoid sequential searches for "1" elements along the cube
	//the lists store all possible values of the third coordinate
	private final int max = 3;
	protected int[][][] xyMatrix = new int[n][n][max];//maximum of 3 elements in the row or column (-z, z, t)
	protected int[][][] yzMatrix = new int[n][n][max];
	protected int[][][] xzMatrix = new int[n][n][max];
	
	public EfficientIncidenceCube(int n) {
		super(n);
		this.init();
	}
	
	@Override
	public int size() {
		return this.n;
	}
	@Override
	public void init() {
		//initialize the md
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No such algorithm: md5");
		}
		//initialize the ls as cyclic 
		xyMatrix = new int[n][n][max];
		yzMatrix = new int[n][n][max];
		xzMatrix = new int[n][n][max];		
		//first initialize the 2-dimensional arrays
		for (int x=0; x<n; x++) {
			for (int y=0; y<n; y++) {
				for (int z=0; z<max; z++) {
					xyMatrix[x][y][z] = nullInt;
					yzMatrix[x][y][z] = nullInt;
					xzMatrix[x][y][z] = nullInt;
				}
			}
		}
		
		drawingOptions = new DrawingOptions();
		//fill the 1s
		int lastSymbol = -1;
		for (int i=0; i<n; i++) {//for all rows
			
			lastSymbol = (lastSymbol + 1) % n;
			
			for (int j=0; j<n; j++) {

				this.xyzStore(i, j, lastSymbol);
				
				lastSymbol = (lastSymbol + 1) % n;
			}
		}
	}
	
	public int getEmptySpace(int[] arr) {
		return ArrayUtils.indexOf(arr, nullInt);
	}
	
	/**
	 * Stores an 1 at position (x,y,z) in the incidence cube.
	 *  This is done by 
	 * @param x
	 * @param y
	 * @param z
	 */
	protected void xyzStore(int x, int y, int z) {
		this.add(xyMatrix[x][y], z);
		this.add(yzMatrix[y][z], x);
		this.add(xzMatrix[x][z], y);
	}
	
	private int add(int[] arr, int elem) {
		int idx = ArrayUtils.indexOf(arr, minus(elem));//look for the negative element
		if (idx>=0) { //if -element is found
			arr[idx] = nullInt;//-elem+elem = 0
			return idx;
		} else {
			idx = this.getEmptySpace(arr);//look for empty space for the new element
			if (idx==-1) {//if full , fail
				return -1;
			} else {//add the new element
				arr[idx] = elem;
				return idx;//if successful, returns the index of the new element
			}
		}
	}
	
	private int remove(int[] arr, int elem) {
		int idx = ArrayUtils.indexOf(arr, elem);//look for the element to remove
		if (idx>=0) { // if element is found
			arr[idx] = nullInt;
			return idx;			
		} else {//if elem is not found
			idx = this.getEmptySpace(arr);
			if (idx==-1) {//if full, fail
				return -1;
			} else {
				arr[idx] = minus(elem);//add the negative
				return idx;
			}
		}
		
	}
	
	protected void xyzRemove(int x, int y, int z) {
		this.remove(xyMatrix[x][y], z);		
		this.remove(yzMatrix[y][z], x);//if exists, removes
		this.remove(xzMatrix[x][z], y);
	}
	
	private int minus(int a) {
		if (a==0)
			return minus0;
		else if (a==minus0) 
				return 0;
		else return (-a);
	}
	@Override
	public int coordOf(int x , int y, int z) {
		if (ArrayUtils.contains(xyMatrix[x][y], z)) {
			return 1;
		} else if (ArrayUtils.contains(xyMatrix[x][y], minus(z))) {
			return -1;
		} else {
			return 0;
		}
	}
	
	@Override
	public int plusOneZCoordOf(int x, int y) {
		int z = ArrayUtils.indexOfFirstPositiveElem(xyMatrix[x][y]);
		if (z>=0)
			return xyMatrix[x][y][z];
		else
			return -1;
	}
	@Override
	public int secondPlusOneZCoordOf(int x, int y) {
		int z = ArrayUtils.indexOfSecondPositiveElem(xyMatrix[x][y]);
		if (z>=0)
			return xyMatrix[x][y][z];
		else
			return -1;
	}
	
	@Override
	public int plusOneXCoordOf(int y, int z) {
		int x = ArrayUtils.indexOfFirstPositiveElem(yzMatrix[y][z]);
		if (x>=0)
			return yzMatrix[y][z][x];
		else
			return -1;
	}
	@Override
	public int plusOneYCoordOf(int x, int z) {
		int y = ArrayUtils.indexOfFirstPositiveElem(xzMatrix[x][z]);
		if (y>=0)
			return xzMatrix[x][z][y];
		else
			return -1;
	}
	@Override
	public int minusOneCoordOf(int x, int y) {
		int z = ArrayUtils.indexOfFirstNegativeElem(xyMatrix[x][y], nullInt);
		if (z>=0)
			return xyMatrix[x][y][z];
		else
			return -1;
	}
	@Override
	public void doPlusMinus1Move(OrderedTriple t, int x1, int y1, int z1) {
		//changes in chosen sub-cube
		//sum 1 to the selected "0" cell
		this.xyzStore(t.x, t.y, t.z);
		this.xyzStore(t.x, y1, z1);
		this.xyzStore(x1, y1, t.z);
		this.xyzStore(x1, t.y, z1);
				
		//subtract 1 to the "1" cell
		this.xyzRemove(t.x, t.y, z1);
		this.xyzRemove(t.x, y1, t.z);
		this.xyzRemove(x1, t.y, t.z);
		this.xyzRemove(x1, y1, z1);
	}
	
	@Override
	public void moveFromProper() {
		OrderedTriple t = this.select0Cell();
		
		int x1 = this.plusOneXCoordOf(t.y, t.z);
		int z1 = this.plusOneZCoordOf(t.x, t.y);
		int y1 = this.plusOneYCoordOf(t.x, t.z);
		
		this.doPlusMinus1Move(t, x1, y1, z1);
				
		//check if improper
		//(only one cell can be -1)
		if (this.coordOf(x1, y1, z1)==-1) {
			proper = false;
			improperCell = new OrderedTriple(x1, y1, z1);
		}
	}
	
	@Override
	public void moveFromImproper() {
		//get the improper cell:
		OrderedTriple t = this.improperCell;
		
		int x1 = this.choosePlusOneXCoordOf(t.y, t.z);
		int y1 = this.choosePlusOneYCoordOf(t.x, t.z);
		int z1 = this.choosePlusOneZCoordOf(t.x, t.y);
		
		this.doPlusMinus1Move(t, x1, y1, z1);
		
		//this is the only cell that can result -1
		if (this.coordOf(x1, y1, z1)==-1) {
			this.proper = false;
			this.improperCell = new OrderedTriple(x1, y1, z1);
		} else {
			proper = true;
			improperCell = null;
		}
		
	}
	
	@Override
	public int choosePlusOneZCoordOf(int x, int y) {
		boolean takeFirst = (this.pickAnInt(2)==1);
		int z = ArrayUtils.indexOfFirstPositiveElem(xyMatrix[x][y]);
		if (z==-1)
			return -1;
		if (takeFirst)
			return xyMatrix[x][y][z]; 
		else {
			z = ArrayUtils.indexOfFirstPositiveElemStartingAt(xyMatrix[x][y], z+1);
			if (z==-1)
				return -1;
			return xyMatrix[x][y][z];
		}
	}
	@Override
	public int choosePlusOneXCoordOf(int y, int z) {
		boolean takeFirst = (this.pickAnInt(2)==1);
		int x = ArrayUtils.indexOfFirstPositiveElem(yzMatrix[y][z]);
		if (x==-1)
			return -1;
		if (takeFirst)
			return yzMatrix[y][z][x];
		else {
			x = ArrayUtils.indexOfFirstPositiveElemStartingAt(yzMatrix[y][z], x+1);
			if (x==-1)
				return -1;
			return yzMatrix[y][z][x];
		}
	}
	@Override
	public int choosePlusOneYCoordOf(int x, int z) {
		boolean takeFirst = (this.pickAnInt(2)==1);
		int y = ArrayUtils.indexOfFirstPositiveElem(xzMatrix[x][z]);
		if (y==-1)
			return -1;
		if (takeFirst)
			return xzMatrix[x][z][y]; 
		else {
			y = ArrayUtils.indexOfFirstPositiveElemStartingAt(xzMatrix[x][z], y+1);
			if (y==-1)
				return -1;
			return xzMatrix[x][z][y];
		}
	}

	@Override
	protected OrderedTriple select0Cell() {
		int x = this.pickAnInt(n);
		int y = this.pickAnInt(n);
		int z = this.pickAnInt(n);
		
		while (this.coordOf(x, y, z)!=0) {
			x = this.pickAnInt(n);
			y = this.pickAnInt(n);
			z = this.pickAnInt(n);
		}
		return new OrderedTriple(x,y,z);
	}
	
	public DrawingOptions getDrawingOptions() {
		return drawingOptions;
	}

	public void setDrawingOptions(DrawingOptions drawingOptions) {
		this.drawingOptions = drawingOptions;
	}
}


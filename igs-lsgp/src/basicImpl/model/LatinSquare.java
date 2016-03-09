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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import commons.ILatinSquare;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class LatinSquare implements ILatinSquare {

	protected ArrayList<Integer>[] ls;
	
	protected int n = 0;
	
	protected MessageDigest md = null;
	
	@SuppressWarnings("unchecked")
	public LatinSquare(int n) {
		
		 this.ls = new ArrayList[n];
		 this.n = n;
		 
		 for (int i=0; i<n; i++) {
		    ls[i] = new ArrayList<Integer>(n);
		    for (int j=0; j<n; j++) {
		    	ls[i].add(0); //add initial n 0s
		    }
		    
		 }
		//initialize the md
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No such algorithm: md5");
		}
	}

	@Override
	public int size() throws Exception {
		return n;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Latin square of order "+n+":\n");
		for (int x=0; x<n ; x++) {
			//sb.append("Row "+x+":");
			for (int y=0; y<n ; y++) {
				try {
					Integer elem = ls[x].get(y);
					sb.append(elem); 
					sb.append("    ".substring(elem.toString().length()));
					
				} catch (Exception e) {
					sb.append("--  ");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public Integer getValueAt(int row, int col) {
		return ls[row].get(col);
	}

	@Override
	public void setValueAt(int row, int col, int value) {
		ls[row].set(col, value);
	}

	@Override
	public void writeToFile(String fileName) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				Integer elem = this.getValueAt(i, j);
				bw.write(elem.toString());
				bw.write("    ".substring(elem.toString().length()));
			}
			bw.write("\n");
		}
		bw.close();
	}

	public void setRow (int i, ArrayList<Integer> row) {
		ls[i] = row;
	}
	
	public boolean equals(ILatinSquare ls2) throws Exception {
		int n2 = ls2.size();
		if (this.size()!=n2) return false;
		boolean eq = true;
		for (int i=0; i<n2 && eq; i++) {
			for (int j=0; j<n2 && eq; j++) {
				if (this.getValueAt(i, j).intValue()!=ls2.getValueAt(i, j).intValue()) {
					eq = false;
				}
				
			}
		}
		return eq;
	}

	public byte[] hashCodeOfStructure() {
		String str1 = this.serializeStructure();
		return md.digest(str1.getBytes());
	}
	
	
	public String serializeStructure() {
		StringBuffer sb = new StringBuffer();
		for (int x=0; x<n ; x++) {
			for (int y=0; y<n ; y++) {
				Integer elem = ls[x].get(y);
				sb.append(elem); 
			}
		}
		return sb.toString();
	}
	
	
	public static boolean equalHash(byte[] dig1, byte[] dig2) {
		return MessageDigest.isEqual(dig1, dig2);
	}
}

/**
 * Creation date: 09/03/2016
 * 
 */
package mckaywormald;



/**
 * � Copyright 2012-2015 Ignacio Gallego Sagastume
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


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import commons.ILatinRectangle;
import commons.ILatinSquare;

/**
 * @author igallego
 *
 */
public class LatinRectangle implements ILatinRectangle {

		protected ArrayList<Integer>[] lr;
		
		protected int colSize = 0;
		protected int rowSize = 0;
		
		protected MessageDigest md = null;
		
		@SuppressWarnings("unchecked")
		public LatinRectangle(int k, int n) {
			
			 this.lr = new ArrayList[k];
			 this.colSize = n;
			 this.rowSize = k;
			 
			 //initialization with 0s to reach size
			 for (int i=0; i<k; i++) {
			    lr[i] = new ArrayList<Integer>(n);
			    for (int j=0; j<n; j++) {
			    	lr[i].add(0); //add initial n 0s
			    }
			    
			 }
			//initialize the md
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				System.out.println("No such algorithm: md5");
			}
		}

		public int colSize() {
			return colSize;
		}
		
		public int rowSize() {
			return rowSize;
		}
		
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("Latin rectangle of "+rowSize+" by "+colSize+":\n");
			for (int x=0; x<rowSize ; x++) {
				//sb.append("Row "+x+":");
				for (int y=0; y<colSize ; y++) {
					try {
						Integer elem = lr[x].get(y);
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
			return lr[row].get(col);
		}

		@Override
		public void setValueAt(int row, int col, int value) {
			lr[row].set(col, value);
		}

		@Override
		public void writeToFile(String fileName) throws Exception {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			
			for (int i=0; i<colSize; i++) {
				for (int j=0; j<colSize; j++) {
					Integer elem = this.getValueAt(i, j);
					bw.write(elem.toString());
					bw.write("    ".substring(elem.toString().length()));
				}
				bw.write("\n");
			}
			bw.close();
		}

		public void setRow (int i, ArrayList<Integer> row) {
			lr[i] = row;
		}
		
		@Override
		public boolean equals(ILatinRectangle lr2) throws Exception {
			int k2 = lr2.rowSize();
			int n2 = lr2.colSize();
			if (this.rowSize!=k2 || this.colSize!=n2) return false;
			boolean eq = true;
			for (int i=0; i<k2 && eq; i++) {
				for (int j=0; j<n2 && eq; j++) {
					if (this.getValueAt(i, j).intValue()!=lr2.getValueAt(i, j).intValue()) {
						eq = false;
					}
					
				}
			}
			return eq;
		}

		@Override
		public byte[] hashCodeOfStructure() {
			String str1 = this.serializeStructure();
			return md.digest(str1.getBytes());
		}
		
		@Override
		public String serializeStructure() {
			StringBuffer sb = new StringBuffer();
			for (int x=0; x<colSize ; x++) {
				for (int y=0; y<colSize ; y++) {
					Integer elem = lr[x].get(y);
					sb.append(elem); 
				}
			}
			return sb.toString();
		}
		
		
		public static boolean equalHash(byte[] dig1, byte[] dig2) {
			return MessageDigest.isEqual(dig1, dig2);
		}

		@Override
		public int size() throws Exception {
			if (this.rowSize==this.colSize)
				return this.rowSize;//if it is a square, return n=order
			throw new Exception("Don't know how to respond: rows or columns?");
		}

		@Override
		public boolean equals(ILatinSquare ls) throws Exception {
			return this.equals(ls);
		}
	}


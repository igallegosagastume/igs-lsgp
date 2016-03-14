/**
 * Creation date: 09/03/2016
 * 
 */
package mckaywormald.model;



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


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import commons.ILatinRectangle;
import commons.ILatinSquare;

/**
 * @author igallego
 *
 */
public class LatinRectangle implements ILatinRectangle {

		protected int[][] lr;
		
		protected int colSize = 0;
		protected int rowSize = 0;
		
		protected MessageDigest md = null;
		
		public LatinRectangle(ILatinSquare ls) throws Exception {
			int n = ls.size();
			this.colSize = n;
			this.rowSize = n;
			this.lr = new int[n][n];
			
			// initialization
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					lr[i][j] = ls.getValueAt(i, j);
				}
			}
			// initialize the md
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				System.out.println("No such algorithm: md5");
			}
		}
		
		public LatinRectangle(int k, int n) {
			this.lr = new int[k][n];
			this.rowSize = k;
			this.colSize = n;

			// initialize the md
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
						Integer elem = lr[x][y];
						
						if (elem==null) {
							sb.append(" -- ");
						} else {
							sb.append(elem); 
							sb.append("    ".substring(elem.toString().length()));
						}
						
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
			return lr[row][col];
		}

		@Override
		public void setValueAt(int row, int col, int value) {
			lr[row][col] = value;
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

		@Override
		public boolean equals(ILatinRectangle lr2) throws Exception {
			int k2 = lr2.rowSize();
			int n2 = lr2.colSize();
			if (this.rowSize!=k2 || this.colSize!=n2) return false;
			boolean eq = true;
			for (int i=0; i<k2 && eq; i++) {
				for (int j=0; j<n2 && eq; j++) {
					if (this.getValueAt(i, j)!=lr2.getValueAt(i, j)) {
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
			for (int x=0; x<rowSize ; x++) {
				for (int y=0; y<colSize ; y++) {
					Integer elem = lr[x][y];
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
			if (this.isASquare())
				return this.rowSize;//if it is a square, return n=order
			throw new Exception("Don't know how to respond: rows or columns?");
		}

		@Override
		public boolean equals(ILatinSquare ls) throws Exception {
			if (this.isASquare() && this.size()==ls.size())
				return this.equals(new LatinRectangle(ls));
			else
				return false;
		}

		@Override
		public boolean isASquare() {
			return (this.rowSize==this.colSize);
		}

		@Override
		public void setRow(int i, List<Integer> row) {
			for (int j=0; j<this.colSize; j++)
				lr[i][j] = row.get(j);
		}
}
/**
 * Creation date: 14/03/2016
 * 
 */
package basicImpl.model.latinsquares;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import commons.model.ILatinSquare;

/**
 *  This class provides the default behaviour for ILatinSquare implementations.
 * 
 * @author igallego
 *
 */
public abstract class AbstractLatinSquare implements ILatinSquare {

	protected int n = 0;
	protected MessageDigest md = null;
	
	public AbstractLatinSquare(int n) {
		this.n = n;
		
		//initialize the md
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No such algorithm: md5");
		}
	}
	
	@Override
	public int size() {
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
					Integer elem = this.getValueAt(x, y);
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
	public void writeToFile(String fileName) {
		try {
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
		} catch (Exception e) {
			System.out.println("Could not write to file "+fileName);
		}
	}

	@Override
	public boolean equals(ILatinSquare ls2) {
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
	
	@Override
	public byte[] hashCodeOfStructure() {
		String str1 = this.serializeStructure();
		return md.digest(str1.getBytes());
	}
	
	@Override
	public String serializeStructure() {
		StringBuffer sb = new StringBuffer();
		for (int x=0; x<n ; x++) {
			for (int y=0; y<n ; y++) {
				Integer elem = this.getValueAt(x, y);
				sb.append(elem); 
			}
		}
		return sb.toString();
	}
	
	@Override
	public boolean equalHash(byte[] dig1, byte[] dig2) {
		return MessageDigest.isEqual(dig1, dig2);
	}
	
	@Override
	public boolean preservesLatinProperty() {
		boolean result = true;
		
		Set<Integer> symbols = new HashSet<Integer>();
		
		//row verification
		for(int i=0; i<n; i++) {
			symbols = new HashSet<Integer>();
			for(int j=0; j<n; j++) {
				symbols.add(this.getValueAt(i, j));	
			}
			if (symbols.size()!=n)
				return false;
		}
		
		//column verification
		for(int j=0; j<n; j++) {
			symbols = new HashSet<Integer>();
			for(int i=0; i<n; i++) {
				symbols.add(this.getValueAt(i, j));	
			}
			if (symbols.size()!=n)
				return false;
		}
		return result;
	}

}

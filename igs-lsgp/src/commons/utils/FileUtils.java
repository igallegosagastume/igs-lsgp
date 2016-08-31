/**
 * Creation date: 24/02/2015
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
package commons.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import commons.model.ILatinSquare;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags 
 */
public class FileUtils {

	public static void writeLS(ILatinSquare ic, String path) {
		if (path!=null && path.length()>3) {
			try {
				ic.writeToFile(path); //"C:\\Users\\ignacio\\JyMIncidenceCube.txt");
				System.out.println("LS written to path: "+path);//C:\\Users\\ignacio\\JyMEfficientCube.txt");
			} catch (Exception e) {
				System.out.println("Could not write LS to file. Exception: "+e.getMessage());
			}
		}
	}
	
	
	public static void writeTextToFile(String path, String text) throws Exception {
		File logFile=new File(path);

	    BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
	    writer.write (text);

	    //Close writer
	    writer.close();
	}
	
}

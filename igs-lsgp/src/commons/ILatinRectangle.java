/**
 * Creation date: 09/03/2016
 * 
 */
package commons;

/**
 * @author igallego
 *
 */
public interface ILatinRectangle extends ILatinSquare {
		//properties
		public int colSize();
		public int rowSize();
		
		//querys
		public boolean equals(ILatinRectangle lr) throws Exception; 
}

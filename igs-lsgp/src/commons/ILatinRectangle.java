/**
 * Creation date: 09/03/2016
 * 
 */
package commons;

/**
 * @author igallego
 *
 */
public interface ILatinRectangle {
		//getters and setters
		public Integer getValueAt(int row, int column) throws Exception;
		public void setValueAt(int row, int column, int value) throws Exception;

		//utils
		public void writeToFile(String fileName) throws Exception;
		public String toString();

		//properties
		public int colSize();
		public int rowSize();
		
		//querys
		public boolean equals(ILatinRectangle lr) throws Exception; 
}

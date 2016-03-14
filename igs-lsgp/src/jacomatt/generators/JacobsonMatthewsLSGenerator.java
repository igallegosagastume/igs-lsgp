/**
 * Creation date: 14/03/2016
 * 
 */
package jacomatt.generators;

import jacomatt.model.EfficientIncidenceCube;
import commons.ILatinSquare;
import commons.generators.IRandomLatinSquareGenerator;

/**
 * @author igallego
 *
 */
public class JacobsonMatthewsLSGenerator implements IRandomLatinSquareGenerator {

	private EfficientIncidenceCube cube;
	private int n;
	
	
	public JacobsonMatthewsLSGenerator(int n) {
		this.n = n;
	}
	
	
	@Override
	public String getMethodName() {
		return "Optimized Jacobson & Matthews generation method.";
	}

	@Override
	public ILatinSquare generateLS() {
		cube = new EfficientIncidenceCube(n);
		cube.shuffle();
		
		return cube;
	}

}

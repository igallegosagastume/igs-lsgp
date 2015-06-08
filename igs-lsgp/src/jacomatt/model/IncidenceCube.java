/**
 * Creation date: 07/06/2014
 * 
 * Master thesis on Latin Squares generation
 * 
 */

/**
 * © Copyright 2012-2014 Ignacio Gallego Sagastume
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

import jacomatt.opengl.DrawIncidenceCube;
import jacomatt.utils.DrawingOptions;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

import commons.ILatinSquare;
import commons.OrderedTriple;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class IncidenceCube implements ILatinSquare {
	protected int n = 5;
	protected int[][][] cube = {};//the incidence cube, each cell containing 0, 1, or -1 (for improper cubes)
	protected DrawingOptions drawingOptions;
	protected boolean proper = true; //it all starts from a proper (possibly cyclic) cube
	protected OrderedTriple improperCell = null;
	//protected Random random = new Random();
	protected SecureRandom random = new SecureRandom();
	
	
	protected MessageDigest md = null;
	
	
	public IncidenceCube() {
		this.init();
	}
	
	public IncidenceCube(int n) {
		this.n = n;
		this.init();
	}
	
	public int size() {
		return this.n;
	}
	
	public void init() {
		//initialize the md
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No such algorithm: md5");
		}
		//initialize the ls as cyclic 
		cube = new int[n][n][n];
		drawingOptions = new DrawingOptions();
		//first put all 0s
		for (int row=0; row<n; row++) {
			for (int col=0; col<n; col++) {
				for (int sym=0; sym<n; sym++)
					cube[row][col][sym] = 0;
			}
		}
		//fill the 1s
		int lastSymbol = -1;
		for (int i=0; i<n; i++) {//for all rows
			lastSymbol = (lastSymbol + 1) % n;
			for (int j=0; j<n; j++) {
				cube[i][j][lastSymbol] = 1;
				lastSymbol = (lastSymbol + 1) % n;
			}
		}
	}
	
	public int coordOf(int x , int y, int z) {
		return cube[x][y][z];
	}
	
	public int plusOneZCoordOf(int x, int y) {
		int z = 0;
		while (cube[x][y][z]!=1) {
			z++;
			if (z==n) 
				return -1;
		}
		return z;
	}
	
	
	public int secondPlusOneZCoordOf(int x, int y) {
		int z = 0;
		
		while (cube[x][y][z]!=1) {
			z++;
			if (z==n) 
				return -1;
		}
		z++;
		while(cube[x][y][z]!=1) {
			z++;
			if (z==n) 
				return -1;
		}
		return z;
	}
	
	
	public int plusOneXCoordOf(int y, int z) {
		int x = 0;
		while (cube[x][y][z]!=1) {
			x++;
			if (x==n) 
				return -1;
		}
		return x;
	}
	
	public int plusOneYCoordOf(int x, int z) {
		int y = 0;
		while (cube[x][y][z]!=1) {
			y++;
			if (y==n) 
				return -1;
		}
		return y;
	}
	
	public int minusOneCoordOf(int x, int y) {
		int sym = 0;
		while (cube[x][y][sym]!=-1) {
			sym++;
			if (sym==n) {
				return -1;
			}
		}
		return sym;
	}
	
	protected void doPlusMinus1Move(OrderedTriple t, int x1, int y1, int z1) {
		cube[t.x][t.y][t.z]++; //sum 1 to the selected "0" cell
		cube[t.x][y1][z1]++;
		cube[x1][y1][t.z]++;
		cube[x1][t.y][z1]++;
		
		cube[t.x][t.y][z1]--; //subtract 1 to the "1" cell	
		cube[t.x][y1][t.z]--;
		cube[x1][t.y][t.z]--;
		cube[x1][y1][z1]--;
	}
	
	public void moveFromProper() {
		OrderedTriple t = this.select0Cell();
		
		int x1 = this.plusOneXCoordOf(t.y, t.z);
		int z1 = this.plusOneZCoordOf(t.x, t.y);
		int y1 = this.plusOneYCoordOf(t.x, t.z);
		
		//changes in chosen sub-cube
		this.doPlusMinus1Move(t, x1, y1, z1);
		
		//check if improper
		//(only one cell can be -1)
		if (cube[x1][y1][z1]  ==-1) {
			proper = false;
			improperCell = new OrderedTriple(x1, y1, z1);
		}
	}
	
	/**
	 * Chooses an integer in [0,n-1]
	 * @param n
	 * @return
	 */
	public int pickAnInt(int n) {
		return random.nextInt(n); 
	}
	
	public void moveFromImproper() {
		//get the improper cell:
		OrderedTriple t = this.improperCell;
		
		int x1 = this.choosePlusOneXCoordOf(t.y, t.z);
		int y1 = this.choosePlusOneYCoordOf(t.x, t.z);
		int z1 = this.choosePlusOneZCoordOf(t.x, t.y);
		
		//changes in chosen sub-cube
		this.doPlusMinus1Move(t, x1, y1, z1);

		//this is the only cell that can result -1
		if (cube[x1][y1][z1]==-1) {
			this.proper = false;
			this.improperCell = new OrderedTriple(x1, y1, z1);
		} else {
			proper = true;
			improperCell = null;
		}
		
	}
	
	public int choosePlusOneZCoordOf(int x, int y) {
		int z = 0;
		boolean takeFirst = (this.pickAnInt(2)==1);
		
		while (cube[x][y][z]!=1) {
			z++;
			if (z==n) 
				return -1;
		}
		
		if (takeFirst)
			return z;
		else {
			z++;
			while(cube[x][y][z]!=1) {
				z++;
				if (z==n) 
					return -1;
			}
			return z;
		}
	}
	
	public int choosePlusOneXCoordOf(int y, int z) {
		int x = 0;
		boolean takeFirst = (this.pickAnInt(2)==1);
		
		while (cube[x][y][z]!=1) {
			x++;
			if (x==n) 
				return -1;
		}
		
		if (takeFirst)
			return x;
		else {
			x++;
			while(cube[x][y][z]!=1) {
				x++;
				if (x==n) 
					return -1;
			}
			return x;
		}
	}
	
	public int choosePlusOneYCoordOf(int x, int z) {
		int y = 0;
		boolean takeFirst = (this.pickAnInt(2)==1);
		
		while (cube[x][y][z]!=1) {
			y++;
			if (y==n) 
				return -1;
		}
		
		if (takeFirst)
			return y;
		else {
			y++;
			while(cube[x][y][z]!=1) {
				y++;
				if (y==n) 
					return -1;
			}
			return y;
		}
			
	}

	
	protected OrderedTriple select0Cell() {
		int x = this.pickAnInt(n);
		int y = this.pickAnInt(n);
		int z = this.pickAnInt(n);
		
		while (cube[x][y][z]!=0) {
			x = this.pickAnInt(n);
			y = this.pickAnInt(n);
			z = this.pickAnInt(n);
		}
		return new OrderedTriple(x,y,z);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Incidence cube of size "+n+":\n");
		for (int x=0; x<n ; x++) {
			//sb.append("Row "+x+":");
			for (int y=0; y<n ; y++) {
				try {
					String z;
					
					int coordZ = this.minusOneCoordOf(x, y);
					
					if (coordZ!=-1) {//if has negative (3 elements)
						z = "{";
						
						if(coordZ>=0) 
							z+="-"+Integer.toString(coordZ);
						else
							z+=Integer.toString(coordZ);
						
						int z2,z3;//TODO: Complete
						z2 = this.plusOneZCoordOf(x, y);
						z3 = this.secondPlusOneZCoordOf(x, y);	
						
						z+= ","+Integer.toString(z2);
						z+= ","+Integer.toString(z3)+"}";
						
					} else {//IF NOT, it has only one element
						coordZ = this.plusOneZCoordOf(x, y);
						z = ""+coordZ;
						
					}
					
					sb.append(z); 
					sb.append("    ".substring(z.length()));
					
				} catch (Exception e) {
					sb.append("ERROR IN COORD");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public boolean proper() {
		return this.proper;
	}
	
	/**
	 * Mixes the initial ls into a ic uniformly distributed, doing at least (n^3)/8 iterations,
	 * so each element has a chance to be moved (see Brown). 
	 * This is because each +-1-movement moves 8 elements and there are n^3 elements. 
	 * 
	 * @return int , the number of iterations that took to get a proper ic
	 */

	public int shuffle() {
		int iterations;
		for (iterations=0; (iterations<Math.pow((double)this.size(), (double)3))
							|| !this.proper(); 
			iterations++) {
			if (this.proper()) {
				this.moveFromProper();
			} else {
				this.moveFromImproper();
			}
		}
		return iterations;
	}

	public DrawingOptions getDrawingOptions() {
		return drawingOptions;
	}

	public void setDrawingOptions(DrawingOptions drawingOptions) {
		this.drawingOptions = drawingOptions;
	}
	
	public void drawIncidenceCube() {
		// Create the OpenGL rendering canvas
		GLCanvas canvas = new GLCanvas(); // heavy-weight GLCanvas
		
		DrawingOptions opts = this.getDrawingOptions();
		
		//canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		DrawIncidenceCube renderer = new DrawIncidenceCube(this);
		canvas.addGLEventListener(renderer);
		canvas.addKeyListener(renderer);
		canvas.setFocusable(true); // To receive key event
		canvas.requestFocus();

		// Create a animator that drives canvas' display() at the specified FPS.
		final FPSAnimator animator = new FPSAnimator(canvas, opts.getFPS(), true);

		// Create the top-level container frame
		final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame
		frame.getContentPane().add(canvas);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Use a dedicate thread to run the stop() to ensure that the
				// animator stops before program exits.
				new Thread() {
					@Override
					public void run() {
						animator.stop(); // stop the animator loop
						System.exit(0);
					}
				}.start();
			}
		});
		
		
		frame.setTitle(opts.getWindowTitle());
		frame.pack();
		frame.setVisible(true);
		if (opts.isFullScreen())
			frame.setExtendedState(Frame.MAXIMIZED_BOTH); // full screen mode
		else
			frame.setBounds(opts.getFrameXPosition(), opts.getFrameYPosition(),
							opts.getFrameWidth(), opts.getFrameHeight());
	
		animator.start(); // start the animation loop
	}
	
	@Override
	public void writeToFile(String fileName) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				Integer elem = this.plusOneZCoordOf(i, j);
				bw.write(elem.toString());
				bw.write("    ".substring(elem.toString().length()));
			}
			bw.write("\n");
		}
		bw.close();
	}

	@Override
	public Integer getValueAt(int row, int column) throws Exception {
		return this.plusOneZCoordOf(row, column);
	}

	@Override
	public void setValueAt(int row, int column, int value) throws Exception {
		this.cube[row][column][value] = 1;		
	}

	/* (non-Javadoc)
	 * @see commons.ILatinSquare#equals(commons.ILatinSquare)
	 */
	@Override
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
	
	
	public byte[] hashCodeOfLS() {
		String str1 = this.serializeLS();
		return md.digest(str1.getBytes());
	}
	
	public String serializeLS() {
		StringBuffer sb = new StringBuffer();
		for (int x=0; x<n ; x++) {
			for (int y=0; y<n ; y++) {
				
				Integer elem = null;
				try {
					elem = this.getValueAt(x, y);
				} catch (Exception e) {
					System.out.println("Exception trying to serialize the cube");
				}
				sb.append(elem); 
			}
		}
		return sb.toString();
	}
	
}


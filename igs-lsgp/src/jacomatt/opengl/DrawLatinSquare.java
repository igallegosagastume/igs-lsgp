/**
 * Creation date: 08/07/2014
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
package jacomatt.opengl;


import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_PAGE_DOWN;
import static java.awt.event.KeyEvent.VK_PAGE_UP;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_UP;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST; // GL constants
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_LINE_STRIP;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT1;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import jacomatt.model.LatinSquare;
import jacomatt.utils.DrawingOptions;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.awt.TextRenderer;
// GL2 constants

/**
 * Draws a Latin Square as an Incidence Cube of 0's and 1's.
 * 
 * Source code based on "NeHe Lesson #18: Quadrics"
 * 
 * space: switch into the next object 'l': toggle light on/off 'f': switch to
 * the next texture filters (nearest, linear, mipmap) Page-up/Page-down: zoom
 * in/out decrease/increase z up-arrow/down-arrow: decrease/increase x
 * rotational speed left-arrow/right-arrow: decrease/increase y rotational speed
 */
/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class DrawLatinSquare implements GLEventListener,
		KeyListener {
	TextRenderer textRenderer;
	private GLU glu; // for the GL Utility

	private GLUquadric quadric;

	private static float rotateAngleX = 0.0f; // rotational angle for x-axis in
											  // degree
	private static float rotateAngleY = 0.0f; // rotational angle for y-axis in
											  // degree
	private static float z = -5.0f; // z-location
	private static float rotateSpeedX = 0.0f; // rotational speed for x-axis
	private static float rotateSpeedY = 0.0f; // rotational speed for y-axis

	private static float zIncrement = 0.5f; // for zoom in/out
	private static float rotateSpeedXIncrement = 0.5f; // adjusting x rotational
														// speed
	private static float rotateSpeedYIncrement = 0.5f; // adjusting y rotational
														// speed

	private float right;
	
	private LatinSquare ls = null;
	
	private int n = 0;//dimension
	
	private float textScaleFactor;

	// Lighting
	private static boolean lightOn = false;

	
	public DrawLatinSquare(LatinSquare ls) {
		this.ls = ls;
		this.n = ls.size();
	}
	
	// ------ Implement methods declared in GLEventListener ------
	/**
	 * Called back immediately after the OpenGL context is initialized. Can be
	 * used to perform one-time initialization. Run only once.
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL graphics context
		glu = new GLU(); // get GL Utilities
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		gl.glClearDepth(1.0f); // set clear depth value to farthest
		gl.glEnable(GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL_LEQUAL); // the type of depth test to do
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best
																// perspective
																// correction
		gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out
									// lighting

		// inicializo el TextRenderer (solo una vez en toda la ejecucion, sino se cuelga!)
		textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));
		// Compute the scale factor of the largest string which will make
		// them all fit on the faces of the cube
		// Rectangle2D bounds = renderer.getBounds("0");
		right = 8.0f;// (float) bounds.getWidth(); //8.0 es para escribir
						// siempre de a una letra
		// top = (float) bounds.getHeight();
		// left = 0.0f;
		// bottom = 0.0f;
		textScaleFactor = 1.0f / (right * 1.1f);

		// Set up the lighting for light named GL_LIGHT1

		// Ambient light does not come from a particular direction. Need some
		// ambient
		// light to light up the scene. Ambient's value in RGBA
		float[] lightAmbientValue = { 0.5f, 0.5f, 0.5f, 1.0f };
		// Diffuse light comes from a particular location. Diffuse's value in
		// RGBA
		float[] lightDiffuseValue = { 1.0f, 1.0f, 1.0f, 1.0f };
		// Diffuse light location xyz (in front of the screen).
		float[] lightDiffusePosition = { 0.0f, 0.0f, 2.0f, 1.0f };

		gl.glLightfv(GL_LIGHT1, GL_AMBIENT, lightAmbientValue, 0);
		gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, lightDiffuseValue, 0);
		gl.glLightfv(GL_LIGHT1, GL_POSITION, lightDiffusePosition, 0);
		gl.glEnable(GL_LIGHT1);

		// Set up the Quadrics
		quadric = glu.gluNewQuadric(); // create a pointer to the Quadric object
										// ( NEW )
		glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH); // create smooth normals
														// ( NEW )
		glu.gluQuadricTexture(quadric, true); // create texture coords ( NEW )
	}

	/**
	 * Call-back handler for window re-size event. Also called when the drawable
	 * is first set to visible.
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context

		if (height == 0)
			height = 1; // prevent divide by zero
		float aspect = (float) width / height;

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL_PROJECTION); // choose projection matrix
		gl.glLoadIdentity(); // reset projection matrix
		glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear,
														// zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset
	}

	/**
	 * Called back by the animator to perform rendering.
	 */
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color
																// and depth
																// buffers
		gl.glLoadIdentity(); // reset the model-view matrix

		// Check whether light shall be turn on (toggle via the 'L' key)
		// This LIGHTING is different form LIGHT1 (??)
		if (lightOn) {
			gl.glEnable(GL_LIGHTING);
		} else {
			gl.glDisable(GL_LIGHTING);
		}

		gl.glTranslatef(0.0f, 0.0f, z); // translate into the screen
		gl.glRotatef(rotateAngleX, 1.0f, 0.0f, 0.0f); // rotate about the x-axis
		gl.glRotatef(rotateAngleY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis


		//draws grid in blue
		DrawingOptions opts = ls.getDrawingOptions();
		if (opts.isShowGrid()) {
			for (int x = 0; x < n; x++)
				for (int y = 0; y < n; y++)
					for (int z = 0; z < n; z++) {
						drawCube1By1(gl, (float) x, (float) y, (float) z);
					}
		}
		//draw positive axis x, y and z
		if (opts.isShowAxis()) {
			this.drawAxis(gl);
		}
		
		if (opts.isShowAxisLetters())
			this.axisLetters(gl);

		//this loop draws the 1's in the incidence cube
		for (int x=0; x < n; x ++) {
			for (int y=0; y < n; y++) {
				this.pointPlus1(gl, x, y, ls.zCoordOf(x, y));
			}
		}
		
		// Update the rotational position after each refresh, based on
		// rotational speed.
		rotateAngleX += rotateSpeedX;
		rotateAngleY += rotateSpeedY;
	}
	
	
	private void drawCube1By1Solid(GL2 gl, float x, float y, float z, float red, float green, float blue) {
		
	    //cara de adelante
		gl.glBegin(GL2.GL_QUADS);//apoyo el lapiz
		gl.glColor3f(red, green, blue);
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 1.0f);
		gl.glVertex3f(x + 1.0f, y + 0.0f, z + 1.0f);
		gl.glVertex3f(x + 1.0f, y + 1.0f, z + 1.0f);
		gl.glVertex3f(x + 0.0f, y + 1.0f, z + 1.0f);
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 1.0f);
		
		//cara inferior
		gl.glVertex3f(x + 1.0f, y + 0.0f, z + 1.0f);
		gl.glVertex3f(x + 1.0f, y + 0.0f, z + 0.0f);
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 0.0f);
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 1.0f);
		
		//cara lateral izquierda
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 0.0f);
		gl.glVertex3f(x + 0.0f, y + 1.0f, z + 0.0f);
		gl.glVertex3f(x + 0.0f, y + 1.0f, z + 1.0f);
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 1.0f);
		gl.glEnd();//levanto el lapiz
		
		//cara de atras
		gl.glBegin(GL2.GL_QUADS);//apoyo el lapiz
		gl.glColor3f(red, green, blue);
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 0.0f);
		gl.glVertex3f(x + 1.0f, y + 0.0f, z + 0.0f);
		gl.glVertex3f(x + 1.0f, y + 1.0f, z + 0.0f);
		gl.glVertex3f(x + 0.0f, y + 1.0f, z + 0.0f);
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 0.0f);
		gl.glEnd();//levanto el lapiz
		
		//cara superior
		gl.glBegin(GL2.GL_QUADS);//apoyo el lapiz
		gl.glColor3f(red, green, blue);
		gl.glVertex3f(x + 0.0f, y + 1.0f, z + 0.0f);
		gl.glVertex3f(x + 1.0f, y + 1.0f, z + 0.0f);
		gl.glVertex3f(x + 1.0f, y + 1.0f, z + 1.0f);
		gl.glVertex3f(x + 0.0f, y + 1.0f, z + 1.0f);
		gl.glVertex3f(x + 0.0f, y + 1.0f, z + 0.0f);
		gl.glEnd();//levanto el lapiz
		
		//cara lateral derecha
		gl.glBegin(GL2.GL_QUADS);//apoyo el lapiz
		gl.glColor3f(red, green, blue);
		gl.glVertex3f(x + 1.0f, y + 0.0f, z + 0.0f);
		gl.glVertex3f(x + 1.0f, y + 1.0f, z + 0.0f);
		gl.glVertex3f(x + 1.0f, y + 1.0f, z + 1.0f);
		gl.glVertex3f(x + 1.0f, y + 0.0f, z + 1.0f);
		gl.glVertex3f(x + 1.0f, y + 0.0f, z + 0.0f);
		gl.glEnd();//levanto el lapiz
	}
	
	@SuppressWarnings("unused")
	private void pointMinus1(GL2 gl, float x, float y, float z) {
		this.drawCube1By1Solid(gl, x, y, z, 1, 0, 0);
	}
	
	private void pointPlus1(GL2 gl, float x, float y, float z) {
		//gradient colors as functions of coordinates 
		float greenGrad = (-y/n)+1;
		if (greenGrad < 0 || greenGrad>1)
			System.out.println("Bad grad: "+greenGrad);
		float blueGrad = (-z/n)+1;
		float redGrad = (-x/n)+1;
		
		this.drawCube1By1Solid(gl, x, y, z, redGrad, greenGrad, blueGrad);
		this.drawCube1By1(gl, x, y, z);
	}
	
	private void axisLetters(GL2 gl) {
		// labels sobre los ejes
		// String letras = "abcdefghijklmnopqrstuvwxy01234567890";

		// label del eje x
		String digit;
		for (float x = 0; x < n; x = x + 1) {
			int car = ((int)x)%10;
			digit = Integer.toString(car);
			
			drawText(gl, digit, x, (float) n,	(float) n);
			// System.out.print(Character.toString((char)indice));
		}
		// label del eje y
		gl.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);// rotacion sobre el eje z
		for (float x = 0; x < n; x = x + 1) {
			int car = ((int)x)%10;
			digit = Integer.toString(car);
			
			drawText(gl, digit, x, 0.0f, (float) n);
		}
		gl.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);// vuelvo el eje z como estaba
		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);// roto 90 el eje y
		// label del eje z
		int caracter = 96;
		for (float x = -1; x >= -n; x = x - 1) {
			caracter = (caracter + 1); // cambio el caracter actual modulo n
			drawText(gl, Character.toString((char) caracter), x, (float) n,	0.0f);
		}
		gl.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);// vuelvo el eje y como estaba

	}

	private void drawText(GL2 gl, String text, float x, float y, float z) {
		textRenderer.begin3DRendering();
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDisable(GL2.GL_CULL_FACE);
		textRenderer.draw3D(text, x, y, z, textScaleFactor);
		textRenderer.end3DRendering();
	}

	private void drawCube1By1(GL2 gl, float x, float y, float z) {
		// lineas que unen los dos cuadrados en azul
		gl.glBegin(GL_LINE_STRIP);
		gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 0.0f);
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 1.0f);
		gl.glEnd();

		gl.glBegin(GL_LINE_STRIP);
		gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
		gl.glVertex3f(x + 1.0f, y + 0.0f, z + 0.0f);
		gl.glVertex3f(x + 1.0f, y + 0.0f, z + 1.0f);
		gl.glEnd();

		gl.glBegin(GL_LINE_STRIP);
		gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
		gl.glVertex3f(x + 0.0f, y + 1.0f, z + 0.0f);
		gl.glVertex3f(x + 0.0f, y + 1.0f, z + 1.0f);
		gl.glEnd();

		gl.glBegin(GL_LINE_STRIP);
		gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
		gl.glVertex3f(x + 1.0f, y + 1.0f, z + 0.0f);
		gl.glVertex3f(x + 1.0f, y + 1.0f, z + 1.0f);
		gl.glEnd();

		gl.glBegin(GL_LINE_STRIP);
		gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 1.0f);
		gl.glVertex3f(x + 1.0f, y + 0.0f, z + 1.0f);
		gl.glVertex3f(x + 1.0f, y + 1.0f, z + 1.0f);
		gl.glVertex3f(x + 0.0f, y + 1.0f, z + 1.0f);
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 1.0f);
		gl.glEnd();

		// parte delantera en rojo
		gl.glBegin(GL_LINE_STRIP);
		gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 0.0f);
		gl.glVertex3f(x + 1.0f, y + 0.0f, z + 0.0f);
		gl.glVertex3f(x + 1.0f, y + 1.0f, z + 0.0f);
		gl.glVertex3f(x + 0.0f, y + 1.0f, z + 0.0f);
		gl.glVertex3f(x + 0.0f, y + 0.0f, z + 0.0f);
		gl.glEnd();

	}

	private void drawAxis(GL2 gl) {
		//axis in blue
		float maxVal = 300;
		gl.glBegin(GL_LINE_STRIP);
		gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(maxVal, 0.0f, 0.0f);
		gl.glEnd();
		
		gl.glBegin(GL_LINE_STRIP);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, maxVal, 0.0f);
		gl.glEnd();
		
		gl.glBegin(GL_LINE_STRIP);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, maxVal);
		gl.glEnd();
	}
	
	/**
	 * Called back before the OpenGL context is destroyed. Release resource such
	 * as buffers.
	 */
	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	// ----- Implement methods declared in KeyListener -----

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case VK_SPACE: // switch into the next object
			// object = (object + 1) % numObjects;
			break;
		case VK_L: // toggle light on/off
			lightOn = !lightOn;
			break;
		case VK_F: // switch to the next filter (NEAREST, LINEAR, MIPMAP)
			// currTextureFilter = (currTextureFilter + 1) % textures.length;
			break;
		case VK_PAGE_UP: // zoom-out
			z -= zIncrement;
			break;
		case VK_PAGE_DOWN: // zoom-in
			z += zIncrement;
			break;
		case VK_UP: // decrease rotational speed in x
			rotateSpeedX -= rotateSpeedXIncrement;
			break;
		case VK_DOWN: // increase rotational speed in x
			rotateSpeedX += rotateSpeedXIncrement;
			break;
		case VK_LEFT: // decrease rotational speed in y
			rotateSpeedY -= rotateSpeedYIncrement;
			break;
		case VK_RIGHT: // increase rotational speed in y
			rotateSpeedY += rotateSpeedYIncrement;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}

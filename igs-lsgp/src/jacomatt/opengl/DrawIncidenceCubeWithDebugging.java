/**
 * Creation date: 09/06/2014
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
import jacomatt.model.IncidenceCubeWithDebugging;
import jacomatt.utils.DrawingOptions;

import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.media.opengl.GL2;

import commons.OrderedTriple;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */

public class DrawIncidenceCubeWithDebugging extends DrawIncidenceCube {

	
	public DrawIncidenceCubeWithDebugging(IncidenceCubeWithDebugging ic) {
		super(ic);
	}

	protected void drawSelectedCells(GL2 gl) {
		IncidenceCubeWithDebugging ic = ((IncidenceCubeWithDebugging)this.cube);//cast to subclass
		Iterator<OrderedTriple> i = ic.getSelectedCells().iterator();
		while (i.hasNext()) {
			OrderedTriple t = (OrderedTriple) i.next();
			super.selectCube(gl, t.x, t.y, t.z);
			
			int value = this.cube.coordOf(t.x, t.y, t.z); 
			if (value==0)
				this.drawCube1By1Solid(gl, t.x, t.y, t.z, 0, 0, 1);//blue for 0
			else if (value==1)
				this.drawCube1By1Solid(gl, t.x, t.y, t.z, 0, 1, 0);//green for 1
			else if (value==-1)
				this.drawCube1By1Solid(gl, t.x, t.y, t.z, 1, 0, 0);//red for -1
			else
				this.drawCube1By1Solid(gl, t.x, t.y, t.z, 0, 0, 0);//not needed
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case VK_SPACE: // switch into the next object
			IncidenceCubeWithDebugging ic = ((IncidenceCubeWithDebugging)this.cube);//cast to subclass
			int s = ic.getState();
			if (s==0) {
				ic.moveFromProperStep1();
			} else if (s==1){
				ic.moveFromProperStep2();
			} else if (s==2){
				ic.moveFromImproperStep3();
			} else if (s==3) {
				ic.moveFromImproperStep4();
			}
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
		case 65://letter A: toggle show axis letters
			DrawingOptions dwo = cube.getDrawingOptions();
			dwo.setShowAxisLetters(!dwo.isShowAxisLetters());
			break;
		case 66:
			System.out.println(this.cube);
			break;
		case 67:
			IncidenceCubeWithDebugging ic2 = ((IncidenceCubeWithDebugging)this.cube);
			int state = ic2.getState();
			if (state==0)
				ic2.shuffle();
			break;
		}
	}

}

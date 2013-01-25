/**
Marvin Project <2007-2009>

Initial version by:

Danilo Rosetto Munoz
Fabio Andrijauskas
Gabriel Ambrosio Archanjo

site: http://marvinproject.sourceforge.net

GPL
Copyright (C) <2007>  

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package image.loadAndSave;

import javax.swing.JFrame;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;

/**
 * Load and save sample.
 * @author Gabriel Ambrosio Archanjo
 */
public class LoadAndSave extends JFrame
{
	public LoadAndSave()
	{
		super("Load and Save Sample");
		process();		
		setSize(800,600);
		setVisible(true);		
	}
	
	private void process(){
		// 1. Load Image
		MarvinImage l_image;
		l_image = MarvinImageIO.loadImage("./res/arara.jpg");
		
		MarvinImagePlugin l_pluginImage = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.invert.jar");
		l_pluginImage.process(l_image, l_image);
		l_image.update();
		
		// 2. Load plug-in
		if(l_image != null){
			MarvinImageIO.saveImage(l_image, "./res/araraNegative.jpg");
		}		
	}
	
	public static void main(String args[]){
		LoadAndSave las = new LoadAndSave();
		las.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
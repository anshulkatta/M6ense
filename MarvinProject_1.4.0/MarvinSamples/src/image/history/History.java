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

package image.history;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginHistory;
import marvin.util.MarvinPluginLoader;

/**
 * History sample
 * @author Gabriel Ambrosio Archanjo
 *
 */
public class History extends JFrame {
	
	// GUI
	JButton buttonShowHistory;
	JButton buttonApply;
	
	// Marvin Objects
	MarvinPluginHistory history;
	MarvinImagePlugin 	tempPlugin;
	MarvinImage 		originalImage;
	MarvinImage			resultImage;
	
	MarvinImagePanel 	imagePanelOriginal,
						imagePanelNew;
	
	public History(){
		super("Plug-in History Sample");
		
		ButtonHandler buttonHandler = new ButtonHandler();
		buttonShowHistory = new JButton("Show History");
		buttonShowHistory.addActionListener(buttonHandler);
		buttonApply = new JButton("Apply");
		buttonApply.addActionListener(buttonHandler);
		
		JPanel l_panelBottom = new JPanel();
		l_panelBottom.add(buttonApply);
		l_panelBottom.add(buttonShowHistory);
		
		imagePanelOriginal = new MarvinImagePanel();
		imagePanelNew = new MarvinImagePanel();
		
		JPanel l_panelTop = new JPanel();
		l_panelTop.add(imagePanelOriginal);
		l_panelTop.add(imagePanelNew);		
		
		Container l_c = getContentPane();
		l_c.setLayout(new BorderLayout());
		l_c.add(l_panelTop, BorderLayout.NORTH);
		l_c.add(l_panelBottom, BorderLayout.SOUTH);
		
		originalImage = MarvinImageIO.loadImage("./res/tucano.jpg");
		imagePanelOriginal.setImage(originalImage);
		imagePanelNew.setPreferredSize(imagePanelOriginal.getPreferredSize());
		
		setSize(765,630);
		setVisible(true);		
	}
	
	private void process(){
		history = new MarvinPluginHistory();
	
		resultImage = originalImage.clone();
		MarvinImage l_tempImage = new MarvinImage(resultImage.getWidth(), resultImage.getHeight());
		
		history.addEntry("Original", resultImage, null);
		
		tempPlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.edge.edgeDetector.jar");
		tempPlugin.process(resultImage, l_tempImage);
		l_tempImage.update();
		resultImage = l_tempImage.clone();
		history.addEntry("Edge", resultImage, tempPlugin.getAttributes());
		
		tempPlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.brightnessAndContrast.jar");
		tempPlugin.setAttribute("brightness", -127);
		tempPlugin.setAttribute("contrast", 127);
		
		tempPlugin.process(resultImage, resultImage);
		resultImage.update();
		history.addEntry("BrightenessContrast", resultImage, tempPlugin.getAttributes());
		
		tempPlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.invert.jar");
		tempPlugin.process(resultImage, resultImage);
		resultImage.update();
		history.addEntry("Invert", resultImage, tempPlugin.getAttributes());
		
		
		imagePanelNew.setImage(resultImage);
	}
	
	public static void main(String args[]){
		History s = new History();
		s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == buttonApply){
				process();
			}
			else if(e.getSource() == buttonShowHistory){
				if(history != null){
					history.showThumbnailHistory();
				}
			}			
		}		
	}
}

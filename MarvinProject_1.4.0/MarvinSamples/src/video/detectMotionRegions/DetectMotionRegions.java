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

package video.detectMotionRegions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;
import marvin.video.MarvinVideoManager;

/**
 * Detect the motion regions considering the difference between frames.
 * @author Gabriel Ambrosio Archanjo
 */
public class DetectMotionRegions extends JFrame implements Runnable{
	
	private JPanel				panelSlider;
	private JSlider				sliderSensibility;
	private JLabel				labelSlider;
	
	private MarvinVideoManager 	videoManager;
	private MarvinImagePanel 	videoPanel;
	
	private MarvinImage 		imageIn,
								imageOut,
								imageLastFrame;
	
	private int					imageWidth,
								imageHeight;
								
	
	private MarvinAttributes 	attributesOut;
	
	private Thread 				thread;
	
	private MarvinImagePlugin 	pluginMotionRegions;
	
	private int					sensibility=30;
	
	public DetectMotionRegions(){
		
		videoPanel = new MarvinImagePanel();
		videoManager = new MarvinVideoManager(videoPanel);	
		videoManager.connect();
		
		imageWidth = videoManager.getCameraWidth();
		imageHeight = videoManager.getCameraHeight();
		
		imageLastFrame = new MarvinImage(imageWidth,imageHeight);
		
		attributesOut = new MarvinAttributes(null);
		
		pluginMotionRegions = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.difference.differentRegions.jar");
		pluginMotionRegions.setAttribute("comparisonImage", imageLastFrame);
		
		loadGUI();
		
		thread = new Thread(this);
		thread.start();
	}
	
	private void loadGUI(){
		setTitle("Video Sample - Detect Motion Regions");
		
		sliderSensibility = new JSlider(JSlider.HORIZONTAL, 0, 60, 30);
		sliderSensibility.setMinorTickSpacing(2);
		sliderSensibility.setPaintTicks(true);
		sliderSensibility.addChangeListener(new SliderHandler());
		
		labelSlider = new JLabel("Sensibility");
		
		panelSlider = new JPanel();
		panelSlider.add(labelSlider);
		panelSlider.add(sliderSensibility);
		
		Container l_container = getContentPane();
		l_container.setLayout(new BorderLayout());
		l_container.add(videoPanel, BorderLayout.NORTH);
		l_container.add(panelSlider, BorderLayout.SOUTH);
		
		setSize(imageWidth,imageHeight+90);
		setVisible(true);	
	}
	
	public void run(){
		boolean first = true;
		
		Graphics l_graphics = videoManager.getResultGraphics();

		int[] l_tempRect;
		
		while(true){			
			imageIn = videoManager.getCapturedImage();
			imageOut = videoManager.getResultImage();
			
			if(l_graphics == null){
				l_graphics = imageOut.getBufferedImage().getGraphics();
			}
			
			if(!first){
				pluginMotionRegions.setAttribute("comparisonImage", imageLastFrame);
				pluginMotionRegions.setAttribute("colorRange", sensibility);
				
				MarvinImage.copyColorArray(imageIn, imageOut);
				pluginMotionRegions.process(imageIn, imageOut, attributesOut, MarvinImageMask.NULL_MASK, false);
				
				Vector<int[]> regions = (Vector<int[]>)attributesOut.get("regions");
				
				for(int i=0; i<regions.size(); i++){					
					l_tempRect = regions.get(i);					
					imageOut.drawRect(l_tempRect[0],l_tempRect[1], l_tempRect[2]-l_tempRect[0],l_tempRect[3]-l_tempRect[1], Color.red);
				}
				
				videoManager.updatePanel();
				MarvinImage.copyColorArray(imageIn, imageLastFrame);
			}
			
			first=false;
		}
	}
	
	public static void main(String args[]){
		DetectMotionRegions dmr = new DetectMotionRegions();
		dmr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class SliderHandler implements ChangeListener{
		public void stateChanged(ChangeEvent a_event){
			sensibility = (60-sliderSensibility.getValue());
		}
	}
}

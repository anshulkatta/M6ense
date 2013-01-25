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

package video.chromaKey;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;
import marvin.video.MarvinVideoManager;

/**
 * Subtract the background and combine other image.
 * @author Gabriel Ambrosio Archanjo
 */
public class ChromaKey extends JFrame implements Runnable{
	
	private JPanel 				panelBottom,
								panelSlider;	
	private JSlider				sliderColorRange;
	private JButton 			buttonCaptureBackground,
								buttonStart;
	private JLabel				labelColorRange;
				
	private MarvinVideoManager 	videoManager;
	private MarvinImagePanel 	videoPanel;
	
	private boolean 			playing;
	private boolean 			removeBackground;
	
	private Thread 				thread;
	
	private MarvinImage 		imageIn,
								imageOut,
								imageBackground;
	
	private MarvinImagePlugin 	pluginChroma, 
								pluginCombine;
	
	private int 				colorRange=30;
	
	public ChromaKey(){
		
		videoPanel = new MarvinImagePanel();
		videoManager = new MarvinVideoManager(videoPanel);	
		videoManager.connect();
		
		loadGUI();
		
		pluginChroma = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.subtract.jar");
		pluginCombine = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.combine.combineByMask.jar");
		
		MarvinImage l_imageParadise = MarvinImageIO.loadImage("./res/paradise.jpg");
		Integer cameraWidth = videoManager.getCameraWidth();
		Integer cameraHeight = videoManager.getCameraHeight();
		 		
		MarvinImagePlugin pluginScale = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.transform.scale.jar");
		pluginScale.setAttribute("newWidth", cameraWidth);
		pluginScale.setAttribute("newHeight", cameraHeight);
		
		MarvinImage l_imageParadiseResize = new MarvinImage(1,1); 
		pluginScale.process(l_imageParadise, l_imageParadiseResize);
		l_imageParadise = l_imageParadiseResize;
		
		pluginCombine.setAttribute("combinationImage", l_imageParadise);
		pluginCombine.setAttribute("colorMask", new Color(0,0,255));
		
		imageBackground = new MarvinImage(cameraWidth, cameraHeight);
		
		thread = new Thread(this);
		thread.start();
		playing = true;
		removeBackground = false;
	}
	
	private void loadGUI(){
		setTitle("Chroma Key Sample");
		
		panelBottom = new JPanel();
		
		ButtonHandler l_buttonHandler = new ButtonHandler();
		buttonCaptureBackground = new JButton("Capture Background");
		buttonStart = new JButton("Start");
		buttonStart.setEnabled(false);
		buttonCaptureBackground.addActionListener(l_buttonHandler);
		buttonStart.addActionListener(l_buttonHandler);
		
		sliderColorRange = new JSlider(JSlider.HORIZONTAL, 0, 50, 30);
		sliderColorRange.setMinorTickSpacing(1);
		sliderColorRange.setPaintTicks(true);
		sliderColorRange.addChangeListener(new SliderHandler());
		
		labelColorRange = new JLabel("Color Range");
		
		panelSlider = new JPanel();
		panelSlider.add(labelColorRange);
		panelSlider.add(sliderColorRange);
		
		panelBottom.add(buttonCaptureBackground);
		panelBottom.add(buttonStart);
		
		Container l_container = getContentPane();
		l_container.setLayout(new BorderLayout());
		l_container.add(videoPanel, BorderLayout.NORTH);
		l_container.add(panelSlider, BorderLayout.CENTER);
		l_container.add(panelBottom, BorderLayout.SOUTH);
		
		
		setSize(videoManager.getCameraWidth(),videoManager.getCameraHeight()+100);
		setVisible(true);
	}
	
	public void run(){
		while(true){			
			if(playing)
			{	
				imageIn = videoManager.getCapturedImage();
				imageOut = videoManager.getResultImage();
				
				if(removeBackground){
					pluginChroma.setAttribute("colorRange", colorRange);
					pluginChroma.process(imageIn, imageOut);
					pluginCombine.process(imageOut, imageOut);					
				}
				else{
					MarvinImage.copyColorArray(imageIn, imageOut);
				}
				videoManager.updatePanel();
			}
		}
	}
	
	public static void main(String[] args){
		ChromaKey ck = new ChromaKey();
		ck.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent a_event){
			if(a_event.getSource() == buttonCaptureBackground){
				MarvinImage.copyColorArray(videoManager.getCapturedImage(), imageBackground);
				pluginChroma.setAttribute("backgroundImage", imageBackground);
				buttonStart.setEnabled(true);
			}
			else if(a_event.getSource() == buttonStart){
				removeBackground = true;
			}
		}
	}
	
	private class SliderHandler implements ChangeListener{
		public void stateChanged(ChangeEvent a_event){
			colorRange = (50-sliderColorRange.getValue());
		}
	}

}

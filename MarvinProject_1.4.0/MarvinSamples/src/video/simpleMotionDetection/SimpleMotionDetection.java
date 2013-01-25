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

package video.simpleMotionDetection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;
import marvin.video.MarvinVideoManager;

/**
 * Simple motion detection sample
 * @author Gabriel Ambrosio Archanjo
 */
public class SimpleMotionDetection extends JFrame implements Runnable{

	private JPanel 				panelCenter,
								panelSlider;
	
	
	private JLabel 				labelMotion,
								labelSlider;
	
	private JSlider 			sliderSensibility;
	
	private MarvinVideoManager	videoManager;
	private MarvinImagePanel 	videoPanel;
	
	private int					imageWidth,
								imageHeight;
	
	private Thread 				thread;
	
	private MarvinImage 		imageIn, 
								imageOut, 
								imageLastFrame;
	
	private double 				differencePercentage;
	
	private int 				sensibility = 7;
	
	public SimpleMotionDetection(){
		
		videoPanel = new MarvinImagePanel();
		videoManager = new MarvinVideoManager(videoPanel);	
		videoManager.connect();
		
		imageWidth = videoManager.getCameraWidth();
		imageHeight = videoManager.getCameraHeight();
		
		imageLastFrame = new MarvinImage(imageWidth,imageHeight);
		
		loadGUI();
		
		thread = new Thread(this);
		thread.start();
	}
	
	private void loadGUI(){
		setTitle("Simple Motion Detection");
		labelMotion = new JLabel("MOTION: NO");
		labelMotion.setOpaque(true);
		labelMotion.setHorizontalAlignment(SwingConstants.CENTER);
		
		labelMotion.setBackground(Color.red);
		labelMotion.setForeground(Color.white);
		
		labelSlider = new JLabel("Sensibility:");
		
		sliderSensibility = new JSlider(JSlider.HORIZONTAL, 0, 13, 10);
		sliderSensibility.setMinorTickSpacing(1);
		sliderSensibility.setPaintTicks(true);
		sliderSensibility.addChangeListener(new SliderHandler());
		
		
		
		panelCenter = new JPanel(new BorderLayout());
		panelCenter.add(videoPanel, BorderLayout.NORTH);
		panelCenter.add(labelMotion, BorderLayout.SOUTH);
		
		panelSlider = new JPanel();
		panelSlider.add(labelSlider);
		panelSlider.add(sliderSensibility);
		
		Container l_container = getContentPane();
		l_container.add(videoPanel, BorderLayout.NORTH);
		l_container.add(labelMotion, BorderLayout.CENTER);
		l_container.add(panelSlider, BorderLayout.SOUTH);
		
		
		setSize(imageWidth,imageHeight+100);
		setVisible(true);
	}
	
	public void run(){
		while(true){
			
			imageIn = videoManager.getCapturedImage();
			imageOut = videoManager.getResultImage();
			
			differencePercentage = getDifference(imageLastFrame, imageIn);
			
			MarvinImage.copyColorArray(imageIn, imageOut);			
			MarvinImage.copyColorArray(imageOut, imageLastFrame);
			videoManager.updatePanel();
			
			if(differencePercentage > sensibility){
				labelMotion.setBackground(Color.green);
				labelMotion.setForeground(Color.white);
				labelMotion.setText("MOTION: YES");
			}
			else{
				labelMotion.setBackground(Color.red);
				labelMotion.setForeground(Color.white);
				labelMotion.setText("MOTION: NO");
			}
		}
	}

	private double getDifference(MarvinImage a_imageA, MarvinImage a_imageB){
		
		int l_redA,
			l_redB,
			l_greenA,
			l_greenB,
			l_blueA,
			l_blueB;
		
		double l_pixels=0;
		
		for(int y=0; y<a_imageA.getHeight(); y++){
			for(int x=0; x<a_imageA.getWidth(); x++){
				
				l_redA = a_imageA.getIntComponent0(x, y);
    			l_greenA = a_imageA.getIntComponent1(x, y);
    			l_blueA = a_imageA.getIntComponent2(x, y);
    			
    			l_redB = a_imageB.getIntComponent0(x, y);
    			l_greenB = a_imageB.getIntComponent1(x, y);
    			l_blueB = a_imageB.getIntComponent2(x, y);
    			
    			if
    			(
    				Math.abs(l_redA-l_redB)> 20 ||
    				Math.abs(l_greenA-l_greenB)> 20 ||
    				Math.abs(l_blueA-l_blueB)> 20
    			)
    			{
    				l_pixels++;
    			}	
			}
		}
		return (l_pixels/(a_imageA.getWidth()*a_imageA.getHeight())*100);
	}
	
	public static void main(String args[]){
		SimpleMotionDetection smd = new SimpleMotionDetection();
		smd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class SliderHandler implements ChangeListener{
		public void stateChanged(ChangeEvent a_event){
			sensibility = (15-sliderSensibility.getValue());
		}
	}
}

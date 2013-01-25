package org.marvinproject.image.edge.roberts;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

/**
 * @author Gabriel Ambr�sio Archanjo
 */
public class Roberts extends MarvinAbstractImagePlugin{

	// Definitions
	double[][] matrixRobertsX = new double[][]{
			{1,		0},
			{0,		-1}
	};
	
	double[][] matrixRobertsY = new double[][]{
			{0,		1},
			{-1,	0}
	};
	
	private MarvinImagePlugin 	convolution;
	
	public void load(){
		convolution = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.convolution.jar");
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}
	
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attrOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
    {
		convolution.setAttribute("matrix", matrixRobertsX);
		convolution.process(imageIn, imageOut, null, mask, previewMode);
		
		convolution.setAttribute("matrix", matrixRobertsY);
		convolution.process(imageIn, imageOut, null, mask, previewMode);
    }
}

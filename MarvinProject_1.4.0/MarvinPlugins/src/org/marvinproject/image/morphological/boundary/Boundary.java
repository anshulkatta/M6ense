package org.marvinproject.image.morphological.boundary;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class Boundary extends MarvinAbstractImagePlugin{

	private MarvinImagePlugin	pluginErosion;
	private boolean[][]			matrix;
	
	
	@Override
	public void load() {
		matrix = new boolean[][]
		{
			{true,true,true},
			{true,true,true},
			{true,true,true},
		};
		
		pluginErosion = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.morphological.erosion.jar");
		pluginErosion.setAttribute("matrix", matrix);
	}
	
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut,
		MarvinAttributes attrOut, 
		MarvinImageMask mask, 
		boolean previewMode
	)
	{	
		if(imgIn.getColorModel() == MarvinImage.COLOR_MODEL_BINARY){
			pluginErosion.process(imgIn, imgOut, attrOut, mask, previewMode);
			diff(imgIn, imgOut);
		}
	}
	
	private void diff(MarvinImage imgIn, MarvinImage imgOut){
		for(int y=0; y<imgIn.getHeight(); y++){
			for(int x=0; x<imgIn.getWidth(); x++){
				if(imgIn.getBinaryColor(x, y) != imgOut.getBinaryColor(x, y)){
					imgOut.setBinaryColor(x, y, true);
				}
				else{
					imgOut.setBinaryColor(x, y, false);
				}
			}
		}
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}
	
}

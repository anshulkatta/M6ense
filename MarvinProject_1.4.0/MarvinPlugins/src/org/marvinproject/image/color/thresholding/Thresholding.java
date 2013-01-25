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

package org.marvinproject.image.color.thresholding;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

/**
 * Thresholding
 * @author Gabriel Ambrosio Archanjo
 */
public class Thresholding extends MarvinAbstractImagePlugin{

	private MarvinAttributesPanel	attributesPanel;
	private MarvinAttributes		attributes;
	private int 					threshold,
									neighborhood,
									range;
	
	private MarvinImagePlugin pluginGray;
	
	public void load(){
		
		// Attributes
		attributes = getAttributes();
		attributes.set("threshold", 125);
		attributes.set("neighborhood", -1);
		attributes.set("range", -1);
		
		pluginGray = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale.jar");
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblThreshold", "Threshold");
			attributesPanel.addTextField("txtThreshold", "threshold", attributes);		
			attributesPanel.addLabel("lblNeighborhood", "Neighborhood");
			attributesPanel.addTextField("txtNeighborhood", "neighborhood", attributes);
			attributesPanel.addLabel("lblRange", "Range");
			attributesPanel.addTextField("txtRange", "range", attributes);
		}
		return attributesPanel;
	}
	
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attributesOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
	{
		threshold = (Integer)attributes.get("threshold");
		neighborhood = (Integer)attributes.get("neighborhood");
		range = (Integer)attributes.get("range");
		
		pluginGray.process(imageIn, imageOut, attributesOut, mask, previewMode);
		
		boolean[][] bmask = mask.getMaskArray();
		
		if(neighborhood == -1 && range == -1){
			hardThreshold(imageIn, imageOut, bmask);
		}
		else{
			contrastThreshold(imageIn, imageOut);
		}
				
	}
	
	private void hardThreshold(MarvinImage imageIn, MarvinImage imageOut, boolean[][] mask){
		for(int y=0; y<imageIn.getHeight(); y++){
			for(int x=0; x<imageIn.getWidth(); x++){
				if(mask != null && !mask[x][y]){
					continue;
				}
				
				if(imageIn.getIntComponent0(x,y) < threshold){
					imageOut.setIntColor(x, y, 0,0,0);
				}
				else{
					imageOut.setIntColor(x, y, 255,255,255);
				}				
			}
		}	
	}
	
	private void contrastThreshold
	(
		MarvinImage imageIn,
		MarvinImage imageOut
	){
		range = 1;
		for (int x = 0; x < imageIn.getWidth(); x++) {
			for (int y = 0; y < imageIn.getHeight(); y++) {
				if(checkNeighbors(x,y, neighborhood, neighborhood, imageIn)){
					imageOut.setIntColor(x,y,0,0,0);
				}
				else{
					imageOut.setIntColor(x,y,255,255,255);
				}
			}
		}
	}
	
	private boolean checkNeighbors(int x, int y, int neighborhoodX, int neighborhoodY, MarvinImage img){
		
		int color;
		int z=0;
		
		color = img.getIntComponent0(x, y);
		
		for(int i=0-neighborhoodX; i<=neighborhoodX; i++){
			for(int j=0-neighborhoodY; j<=neighborhoodY; j++){
				if(i == 0 && j == 0){
					continue;
				}
				
				if(color < getSafeColor(x+i,y+j, img)-range && getSafeColor(x+i,y+j, img) != -1){
					z++;
				}
			}
		}
		
		if(z > (neighborhoodX*neighborhoodY)*0.5){
			return true;
		}
		
		return false;
	}
	
	private int getSafeColor(int x, int y, MarvinImage img){
		
		if(x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()){
			return img.getIntComponent0(x, y);
		}
		return -1;
	}
}

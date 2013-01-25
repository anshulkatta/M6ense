package org.marvinproject.image.fill.boundaryFill;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class BoundaryFill extends MarvinAbstractImagePlugin{
	
	@Override
	public void load() {
		setAttribute("x", 0);
		setAttribute("y", 0);
		setAttribute("color", Color.red.getRGB());
		setAttribute("tile", null);
		setAttribute("image", null);
		
		
	}

	@Override
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut,
		MarvinAttributes attrOut, 
		MarvinImageMask mask, 
		boolean previewMode
	)
	{
		LinkedList<Point> l_list = new LinkedList<Point>();
    	Point 	l_point,
    			l_pointW,
    			l_pointE;
    
    	MarvinImage.copyColorArray(imgIn, imgOut);
    	
    	int x = (Integer)getAttribute("x");
    	int y = (Integer)getAttribute("y");
    	MarvinImage tileImage = (MarvinImage)(getAttribute("tile"));
    	
    	if(!imgOut.isValidPosition(x, y)){
    		return;
    	}
    	
    	int targetColor = imgIn.getIntColor(x, y);
    	int newColor = (Integer)getAttribute("color");
    	
    	boolean fillMask[][] = new boolean[imgOut.getWidth()][imgOut.getHeight()];
    	fillMask[x][y] = true;
    	
    	
    	l_list.add(new Point(x, y));
    	
    	//for(int l_i=0; l_i<l_list.size(); l_i++){
    	while(l_list.size() > 0){
    		l_point = l_list.poll();
    		l_pointW = new Point(l_point.x, l_point.y);
    		l_pointE = new Point(l_point.x, l_point.y);
    		
    		// west
    		while(true){
    			if(l_pointW.x-1 >= 0 && imgOut.getIntColor(l_pointW.x-1, l_pointW.y) == targetColor && !fillMask[l_pointW.x-1][l_pointW.y]){
    				l_pointW.x--;
    			}
    			else{
    				break;
    			}
    		 }
    		
    		// east
    		while(true){
    			if(l_pointE.x+1 < imgOut.getWidth() && imgOut.getIntColor(l_pointE.x+1, l_pointE.y) == targetColor && !fillMask[l_pointE.x+1][l_pointE.y]){
    				l_pointE.x++;
    			}
    			else{
    				break;
    			}
    		 }
    		
    		// set color of pixels between pointW and pointE
    		for(int l_px=l_pointW.x; l_px<=l_pointE.x; l_px++){
    			//imgOut.setIntColor(l_px, l_point.y, -1);
    			//drawPixel(imgOut, l_px, l_point.y, newColor, tileImage);
    			fillMask[l_px][l_point.y] = true;
    			
    			if(l_point.y-1 >= 0 && imgOut.getIntColor(l_px, l_point.y-1) == targetColor && !fillMask[l_px][l_point.y-1]){
    				l_list.add(new Point(l_px, l_point.y-1));
    			}
    			if(l_point.y+1 < imgOut.getHeight() && imgOut.getIntColor(l_px, l_point.y+1) == targetColor && !fillMask[l_px][l_point.y+1]){
    				l_list.add(new Point(l_px, l_point.y+1));
    			}
    		}
    	}    
    	
    	if(tileImage != null){
    		MarvinImagePlugin p = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.texture.tileTexture.jar");
    		p.setAttribute("lines", (int)(Math.ceil((double)imgOut.getHeight()/tileImage.getHeight())));
    		p.setAttribute("columns", (int)(Math.ceil((double)imgOut.getWidth()/tileImage.getWidth())));
    		p.setAttribute("tile", tileImage);
    		MarvinImageMask newMask = new MarvinImageMask(fillMask);    		
    		p.process(imgOut, imgOut, null, newMask, false);
    	}
    	else{
    		for(int j=0; j<imgOut.getHeight(); j++){
    			for(int i=0; i<imgOut.getWidth(); i++){
    				if(fillMask[i][j]){
    					imgOut.setIntColor(i, j, newColor);
    				}
    			}
    		}
    	}
    }
	
	@Override
	public MarvinAttributesPanel getAttributesPanel(){ return null; }

}

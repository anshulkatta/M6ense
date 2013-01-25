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

package marvin.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadWarningListener;
import javax.imageio.stream.ImageInputStream;

import marvin.image.MarvinImage;
import marvin.util.MarvinErrorHandler;

/**
 * Methods to load and save images.
 * @author Danilo Roseto Munoz
 * @author Fabio Andrijaukas
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinImageIO {
	
	/**
	 * Loads a MarvinImage from a filesystem path.
	 * @param a_filePath	- image´s path
	 * @return
	 */
	public static MarvinImage loadImage(String a_filePath){
		MarvinImage l_marvinImage = null;
		BufferedImage l_bufferedImage=null;
		ImageInputStream l_imageInputStream;
		
		//1. Load File
		File l_file = new File(a_filePath);
		
		if(!l_file.exists()){
			throw MarvinErrorHandler.handle(MarvinErrorHandler.TYPE.ERROR_FILE_NOT_FOUND,a_filePath);
		}
		
		// 2. Create ImageReader
		Iterator<?> l_ittReaders = ImageIO.getImageReadersByFormatName(a_filePath.substring(a_filePath.lastIndexOf(".") + 1));
		ImageReader l_reader = (ImageReader) l_ittReaders.next();
		
		l_reader.addIIOReadWarningListener(new IIOReadWarningListener() {
			public void warningOccurred(ImageReader source, String warning) {
				MarvinErrorHandler.handleDialog(MarvinErrorHandler.TYPE.BAD_FILE, warning);
			}
		});		
		
		// 3. Load image
		try{
			l_imageInputStream = ImageIO.createImageInputStream(l_file);
			l_reader.setInput(l_imageInputStream);
			l_bufferedImage = l_reader.read(0);
		}catch(Exception e){
			throw MarvinErrorHandler.handle(MarvinErrorHandler.TYPE.ERROR_FILE_OPEN, a_filePath, e);
			
			//MarvinErrorHandler.handle(, e);
			//return null;
		}
		
		// 4. Get format
		String l_format = "";
		try{
			l_format = l_reader.getFormatName();
		} catch(IOException e){
			e.printStackTrace();
			return null;
		}
		
		// 5. Create MarvinImage object
		l_marvinImage = new MarvinImage(l_bufferedImage, l_format);
		return l_marvinImage;
	}
	
	/**
	 * Saves a MarvinImage via file system path. 
	 * @param a_marvinImage	- MarvinImage object
	 * @param a_filePath	- file path
	 */
	public static void saveImage(MarvinImage a_marvinImage, String a_filePath){
		File l_file = new File(a_filePath);
		String l_fileFormat = a_filePath.substring(a_filePath.lastIndexOf('.')+1);
		
		try{
			ImageIO.write(a_marvinImage.getBufferedImage(), l_fileFormat, l_file);			
		} catch(Exception e){
			throw MarvinErrorHandler.handle(MarvinErrorHandler.TYPE.ERROR_FILE_SAVE, a_filePath, e);
		}
	}
}

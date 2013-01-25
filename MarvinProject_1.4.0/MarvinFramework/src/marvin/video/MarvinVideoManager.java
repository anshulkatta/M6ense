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

package marvin.video;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Vector;

import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Codec;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.PlugInManager;
import javax.media.control.FormatControl;
import javax.media.format.JPEGFormat;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.plugin.MarvinImagePlugin;

/**
 * MarvinVideoManager is a class that manages all video operations. 
 * 
 * @version 1.0 07/01/2009
 * @author Hugo Slepicka
 * @author Danilo Rosetto Muñoz
 *
 */
public class MarvinVideoManager{

	//Definitions
	private static int formatIdx = 0; //The first format supported by the camera
	
	//Declarations
	private static volatile boolean active;

	private DataSource dataSource;
	private PushBufferStream pbs;
	
	private Vector camCapDevice = new Vector();
	private Vector camCapFormat = new Vector();
	private Vector camImgSize   = new Vector();
	private Vector camConverter = new Vector();

	private Codec converter;
	
	private static int cameraWidth;
	private static int cameraHeight;
	
	private int cameraFPS = 30;

	private Buffer buffer;
	private Buffer inputBuffer;
	
	private int[] inpix;
	private int[] outpix;
	
	private Image resultImage;
	private Graphics resultGraphics;
	
	private MemoryImageSource sourceImage;
	
	private Image outputImage;
	
	
	private MarvinImage imageIn, imageOut;

	private MarvinImagePlugin filtro;
	
	private int imgX, imgY;
	
	private MarvinImagePanel videoPanel;
	
	public MarvinVideoManager(MarvinImagePanel a_videoPanel){
		videoPanel = a_videoPanel;
	}
	
	public static boolean isActive() {
		return active;
	}

	public static void setActive(boolean active) {
		MarvinVideoManager.active = active;
	}
	
	/**
	 * @return camera´s width
	 */
	public Integer getCameraWidth(){
		return cameraWidth;
	}
	
	/**
	 * @return camera´s height
	 */
	public Integer getCameraHeight(){
		return cameraHeight;
	}
	
	/**
	 * Gathers info on a camera
	 * 
	 */
	boolean fetchDeviceFormats()
	{
		Vector deviceList = CaptureDeviceManager.getDeviceList(new VideoFormat(null));
				
		CaptureDeviceInfo CapDevice = null;
		Format CapFormat = null;
		String type = "N/A";
		Codec convcodec = null;

		CaptureDeviceInfo deviceInfo=null;boolean VideoFormatMatch=false;
		for(int i=0;i<deviceList.size();i++)
		{
			// search for video device
			deviceInfo = (CaptureDeviceInfo)deviceList.elementAt(i);
			
			Format deviceFormat[] = deviceInfo.getFormats();
			RGBFormat imgFormat = new RGBFormat(null,-1,Format.intArray,-1,32,0x00FF0000,0x0000FF00,0x000000FF,-1,-1,Format.FALSE,-1);
			for (int f=0;f<deviceFormat.length;f++)
			{
				// search for all video formats
				if(deviceFormat[f].matches(imgFormat))
				{
					convcodec = null;	// does not need one
				}
				else
				{
					// find a converter
					convcodec = fetchCodec(deviceFormat[f], imgFormat);
					if(convcodec==null)continue;	// failed to open one
				}				
				
				if(deviceFormat[f] instanceof RGBFormat)type="RGB";
				if(deviceFormat[f] instanceof YUVFormat)type="YUV";
				if(deviceFormat[f] instanceof JPEGFormat)type="JPG";

				Dimension size = ((VideoFormat)deviceFormat[f]).getSize();
				camImgSize.addElement(type+" "+size.width+"x"+size.height);

				CapDevice = deviceInfo;
				camCapDevice.addElement(CapDevice);
				
				CapFormat = (VideoFormat)deviceFormat[f];
				camCapFormat.addElement(CapFormat);
			
				camConverter.addElement(convcodec);	// valid can be null or not

				VideoFormatMatch=true;	// at least one			
			}
		}
		if(VideoFormatMatch==false)
		{
			if(deviceInfo!=null)System.out.println(deviceInfo);
			System.out.println("Video Format not found");
			return false;
		}

		return true;
	}
	
	
	public void connect(){
		// Select webcam format
		fetchDeviceFormats();

		String camS = (String)camImgSize.elementAt(formatIdx);
		int pos_x = camS.indexOf("x");
		String widS = camS.substring(4,pos_x);
		String hgtS = camS.substring(pos_x+1,camS.length());

		cameraWidth = Integer.parseInt(widS.trim());
		cameraHeight = Integer.parseInt(hgtS.trim());
		
		// Setup data source
		fetchDeviceDataSource();
		if(camConverter.elementAt(formatIdx)!=null)
		{
			converter = (Codec)camConverter.elementAt(formatIdx);
		}
		
		Thread.yield();
		createPushBufferDataSource();
		
		Thread.yield();
		
		try{
			dataSource.connect();
			dataSource.start();
		}catch(Exception e){
			System.out.println("Exception dataSource.start() "+e);
		}
		
		Thread.yield();
		try{Thread.sleep(1000);}catch(Exception e){}
		// to let camera settle ahead of processing
			
		Thread.yield();		
		
		resultImage = videoPanel.createImage(cameraWidth,cameraHeight);
		resultGraphics = resultImage.getGraphics();
		
		//Seta parâmetros para a imagem de saída tais como fonte, cor da fonte, etc...
		resultGraphics.setFont(new Font("Arial", 0, 12));
		resultGraphics.setColor(Color.red);

		inpix=new int[cameraWidth * cameraHeight];
		outpix=new int[cameraWidth * cameraHeight];

		//Declaração do buffer que receberá o input da câmera
		buffer = new Buffer();
		
		//Preciso ver o que esse DIRECTCOLORMODEL faz realmente
		DirectColorModel dcm = new DirectColorModel(32, 0x00FF0000, 0x0000FF00, 0x000000FF);
		sourceImage = new MemoryImageSource(cameraWidth, cameraHeight, dcm, outpix, 0, cameraWidth);
		
		//MarvinVideoFilter filtro = new Tracking();
		//@@@filtro = new Seeker();
		//@@@((Seeker) filtro).setLargura(camWidth);
		
		imageIn = new MarvinImage(cameraWidth,cameraHeight);
		imageOut = new MarvinImage(cameraWidth,cameraHeight);
	}
	
	/**
	 * Tidy exit
	 * 
	 */
	public void disconnect()
	{
		active = false;		
		try
		{
			dataSource.stop();
			dataSource.disconnect();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public MarvinImage getCapturedImage(){
		boolean l_frameOK = false;
		while(true){
			l_frameOK = camFetchFrame();
			
			if(l_frameOK){
				break;
			}
			else if(l_frameOK==false)
			{
				try{Thread.sleep(5);}catch(Exception e){}
				continue;
			}
		}

		imageIn.setIntColorArray((int[]) buffer.getData());
		return imageIn;
	}
	
	public MarvinImage getResultImage(){
		imageOut.setIntColorArray(outpix);
		return imageOut;
	}
	
	public void updatePanel(){
		outputImage = videoPanel.createImage(sourceImage);
		resultGraphics.drawImage(outputImage,0,0,videoPanel);
	}
	
	public Graphics getResultGraphics(){
		return resultGraphics;
	}
	
	/**
	 * Finds a camera and sets it up 
	 * 
	 */
	void fetchDeviceDataSource()
	{
		CaptureDeviceInfo CapDevice = (CaptureDeviceInfo)camCapDevice.elementAt(formatIdx);
		Format CapFormat = (Format)camCapFormat.elementAt(formatIdx);

		MediaLocator loc = CapDevice.getLocator();
		try
		{
			dataSource = Manager.createDataSource(loc);
		}
		catch(Exception e){}
		
		try
		{
			// ensures 30 fps or as otherwise preferred
			FormatControl formCont=((CaptureDevice)dataSource).getFormatControls()[0];
			VideoFormat formatVideoNew = new VideoFormat(null,null,-1,null,(float)cameraFPS);
			formCont.setFormat(CapFormat.intersects(formatVideoNew));
		}
		catch(Exception e){}
	}

	/**
	 * Gets a stream from the camera and sets debug
	 * 
	 */
	void createPushBufferDataSource()
	{
		try
		{
			pbs = ((PushBufferDataSource)dataSource).getStreams()[0];
		}
		catch(Exception e){}
	}

	/**
	 * Gets the correct codec from the camera
	 * 
	 */	
    public Codec fetchCodec(Format inFormat, Format outFormat)
	{		
		Vector codecs = PlugInManager.getPlugInList(inFormat,outFormat,PlugInManager.CODEC);
		if(codecs==null)return null;
		
		for(int i=0;i<codecs.size();i++)
		{
			Codec codec = null;
			try
			{
				Class codecClass = Class.forName((String)codecs.elementAt(i));
				if(codecClass!=null)codec = (Codec)codecClass.newInstance();
			}
			catch(Exception e){continue;}
			if(codec==null || codec.setInputFormat(inFormat)==null)continue;

			Format[] outFormats = codec.getSupportedOutputFormats(inFormat);
			if(outFormats==null)continue;
			for(int j=0;j<outFormats.length;j++)
			{
				if(outFormats[j].matches(outFormat))
				{
					try
					{
						codec.setOutputFormat(outFormats[j]);
						codec.open();
						return codec;
					}
					catch(Exception e){}
				}
			}
		}
		return null;
    }	

    /**
     *  Gets an image via the cam
	 * 
	 */
	boolean camFetchFrame()
	{
		try
		{
			if(converter == null){
				pbs.read(buffer);
			}else{
				if(inputBuffer == null){
					inputBuffer = new Buffer();
				}
				pbs.read(inputBuffer);
				converter.process(inputBuffer,buffer);
			}
			if(buffer.isDiscard()){
				buffer.setDiscard(false);
				return false;
			}			
			if(buffer.getData() == null){
				return false;
			}
		}
		catch(Exception e){
			return false;
		}

		return true;
	}
}

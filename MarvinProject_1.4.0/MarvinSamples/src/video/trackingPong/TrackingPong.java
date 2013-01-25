package video.trackingPong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;
import marvin.video.MarvinVideoManager;

/**
 * Tracking game sample
 * @author Gabriel Ambrosio Archanjo
 */
public class TrackingPong extends JFrame implements Runnable{

	private final static int 	BALL_INITIAL_PX=100;
	private final static int 	BALL_INITIAL_PY=100;
	private final static int 	BALL_INITIAL_SPEED=3;
	
	private MarvinVideoManager	videoManager;
	private MarvinImagePanel 	videoPanel;
	
	private Thread 				thread;
	
	private MarvinImage 		imageIn, 
								imageOut;
	
	private JPanel				panelSlider;
	
	private JSlider				sliderSensibility;
	
	private JLabel				labelSlider;

	private int					regionPx,
								regionPy,
								regionWidth,
								regionHeight;
			
	private boolean				regionSelected=false;
	private int[]				arrInitialRegion;
	
	private int					sensibility=30;
	
	
	
	// Pong Game Attributes
	private double				ballPx=BALL_INITIAL_PX,
								ballPy=BALL_INITIAL_PY;
	
	private int					ballSide=15;
								
	
	double						ballIncX=3;
	private double				ballIncY=3;	
	
	private int					screenWidth,
								screenHeight;
	
	private Paddle				paddlePlayer,
								paddleComputer;
	
	private int					playerPoints=4,
								computerPoints=1;
	
	private MarvinImagePlugin 	findColorPattern;
	private MarvinImagePlugin	text;
	
	private MarvinImage			imageBall,
								imagePaddlePlayer,
								imagePaddleComputer;
	
	private MarvinAttributes	attributesOut;
	
	public TrackingPong(){
		videoPanel = new MarvinImagePanel();
		videoManager = new MarvinVideoManager(videoPanel);	
		videoManager.connect();
				
		screenWidth = videoManager.getCameraWidth();
		screenHeight = videoManager.getCameraHeight();
		
		loadGUI();
		
		findColorPattern 	= MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.pattern.findColorPattern.jar");
		text				= MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.render.text.jar");
		text.setAttribute("fontFile", MarvinImageIO.loadImage("./res/font.png"));
		text.setAttribute("color", 0xFFFFFFFF);
		
		imageBall = MarvinImageIO.loadImage("./res/ball.png");
		imagePaddlePlayer = MarvinImageIO.loadImage("./res/paddleA.png");
		imagePaddleComputer = MarvinImageIO.loadImage("./res/paddleB.png");
		
		attributesOut = new MarvinAttributes(null);
		
		paddlePlayer = new Paddle();
		paddlePlayer.px=100;
		paddlePlayer.py=420;
		paddlePlayer.width=100;
		paddlePlayer.height=30;
		
		paddleComputer = new Paddle();
		paddleComputer.px=100;
		paddleComputer.py=30;
		paddleComputer.width=100;
		paddleComputer.height=30;
		
		thread = new Thread(this);
		thread.start();
	}
	
	private void loadGUI(){	
		setTitle("Video Sample - Tracking Pong");
		
		videoPanel.addMouseListener(new MouseHandler());
		
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
		
		setSize(videoManager.getCameraWidth()+20,videoManager.getCameraHeight()+100);
		setVisible(true);
	}
	
	public void run(){
		long time = System.currentTimeMillis();
		int ticks=0;
		
		while(true){
			
			ticks++;
			if(System.currentTimeMillis() - time > 1000){
				System.out.println("FPS: "+ticks+"       ");
				ticks=0;
				time = System.currentTimeMillis();					
			}
			
			imageIn = videoManager.getCapturedImage();
			imageOut = videoManager.getResultImage();
						
			MarvinImage.copyColorArray(imageIn, imageOut);
			
			if(regionSelected){
				findColorPattern.setAttribute("differenceColorRange", sensibility);
				findColorPattern.process(imageIn, imageOut, attributesOut, MarvinImageMask.NULL_MASK, false);
				regionPx 		= (Integer)attributesOut.get("regionPx");
				regionPy 		= (Integer)attributesOut.get("regionPy");
				regionWidth 	= (Integer)attributesOut.get("regionWidth");
				regionHeight	= (Integer)attributesOut.get("regionHeight");
				pongGame();
				
				imageOut.drawRect(regionPx, regionPy, regionWidth, regionHeight, Color.red);
				
				text.setAttribute("x", 105);
				text.setAttribute("y", 3);
				text.setAttribute("text", "PLAYER:"+playerPoints);
				text.process(imageOut, imageOut);
				
				text.setAttribute("x", 105);
				text.setAttribute("y", 460);
				text.setAttribute("text", "COMPUTER:"+computerPoints);
				text.process(imageOut, imageOut);
			}

			videoManager.updatePanel();
		}
	}
	
	private void pongGame(){
		ballIncX*=1.001;
		ballIncY*=1.001;
		ballPx+=ballIncX;
		ballPy+=ballIncY;
		
		paddlePlayer.px = regionPx+((regionWidth-paddlePlayer.width)/2);
		
		computerAI();
		checkPaddlePosition(paddlePlayer);
		checkPaddlePosition(paddleComputer);
		collisionScreen();
		collisionTap();
		
		imageOut.fillRect(horizontalMargin, 0, 5, screenHeight, Color.black);
		imageOut.fillRect(screenWidth-horizontalMargin, 0, 5, screenHeight, Color.black);
		
		//imageOut.fillRect(paddlePlayer.px, paddlePlayer.py, paddlePlayer.width, paddlePlayer.height, Color.green);
		//imageOut.fillRect(paddleComputer.px, paddleComputer.py, paddleComputer.width, paddleComputer.height, Color.red);
		//imageOut.fillRect((int)ballPx, (int)ballPy, ballSide, ballSide, Color.yellow);
		combineImage(imagePaddlePlayer, paddlePlayer.px, paddlePlayer.py);
		combineImage(imagePaddleComputer, paddleComputer.px, paddleComputer.py);
		combineImage(imageBall,(int)ballPx, (int)ballPy);
	}
	
	private void checkPaddlePosition(Paddle a_paddle){
		if(a_paddle.px < horizontalMargin){
			a_paddle.px = horizontalMargin;
		}
		if(a_paddle.px+a_paddle.width > screenWidth-horizontalMargin){
			a_paddle.px = screenWidth-horizontalMargin-a_paddle.width;
		}		
	}
	
	private void computerAI(){
		if(ballPx < paddleComputer.px+(paddleComputer.width/2)-10){
			paddleComputer.px-=4;
		}
		if(ballPx > paddleComputer.px+(paddleComputer.width/2)+10){
			paddleComputer.px+=4;
		}
	}
	
	private int horizontalMargin = 100;
	private void collisionScreen(){
			
		if(ballPx < horizontalMargin){
			ballPx = horizontalMargin;
			ballIncX*=-1;
		}
		if(ballPx+ballSide >= screenWidth-horizontalMargin){
			ballPx=(screenWidth-horizontalMargin)-ballSide;
			ballIncX*=-1;
		}
		if(ballPy < 0){
			playerPoints++;
			ballPx = BALL_INITIAL_PX;
			ballPy = BALL_INITIAL_PY;
			ballIncY=BALL_INITIAL_SPEED;
			ballIncX=BALL_INITIAL_SPEED;
		} else if(ballPy+ballSide >= screenHeight){
			computerPoints++;
			ballPx = BALL_INITIAL_PX;
			ballPy = BALL_INITIAL_PY;
			ballIncY=BALL_INITIAL_SPEED;
			ballIncX=BALL_INITIAL_SPEED;
		}
	}
	
	private void collisionTap(){
		if(ballCollisionTap(paddlePlayer)){
			ballIncY*=-1;
			ballPy = paddlePlayer.py-ballSide;
		}
		if(ballCollisionTap(paddleComputer)){
			ballIncY*=-1;
			ballPy = paddleComputer.py+paddleComputer.height;
		}
	}
	
	private boolean ballCollisionTap(Paddle a_tap){
		
		if
		(
			(
				ballPx >= a_tap.px && ballPx <= a_tap.px+a_tap.width ||
				ballPx <= a_tap.px && ballPx+ballSide >= a_tap.px
			)
			&&
			(
				ballPy >= a_tap.py && ballPy <= a_tap.py+a_tap.height ||
				ballPy <= a_tap.py && ballPy+ballSide >= a_tap.py
			)
		)
		{
			return true;
		}
		return false;
	}
	
	private void combineImage(MarvinImage img, int x, int y){
		int rgb;
		int width = img.getWidth();
		int height = img.getHeight();
		
		for(int iy=0; iy<height; iy++){
			for(int ix=0; ix<width; ix++){
				if
				(
					ix+x > 0 && ix+x < screenWidth &&
					iy+y > 0 && iy+y < screenHeight
				)
				{
					rgb=img.getIntColor(ix, iy);				
					if(rgb != 0xFFFFFFFF){
						imageOut.setIntColor(ix+x, iy+y, rgb);
					}
				}
			}
		}		
	}
	
	public static void main(String args[]){
		TrackingPong l_trackingPong = new TrackingPong();
		l_trackingPong.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class SliderHandler implements ChangeListener{
		public void stateChanged(ChangeEvent a_event){
			sensibility = (60-sliderSensibility.getValue());
		}
	}
	
	private class MouseHandler implements MouseListener{
		public void mouseEntered(MouseEvent a_event){}
		public void mouseExited(MouseEvent a_event){}
		public void mousePressed(MouseEvent a_event){}
		public void mouseClicked(MouseEvent a_event){}
		
		public void mouseReleased(MouseEvent a_event){
			if(!regionSelected){
				if(arrInitialRegion == null){
					arrInitialRegion = new int[]{a_event.getX(), a_event.getY(),0,0};
				}
				else{
					arrInitialRegion[2] = a_event.getX()-arrInitialRegion[0];
					arrInitialRegion[3] = a_event.getY()-arrInitialRegion[1];
					
					findColorPattern.setAttribute("regionPx", arrInitialRegion[0]);
					findColorPattern.setAttribute("regionPy", arrInitialRegion[1]);
					findColorPattern.setAttribute("regionWidth", arrInitialRegion[2]);
					findColorPattern.setAttribute("regionHeight", arrInitialRegion[3]);
					
					regionSelected = true;
				}	
			}
		}		
	}
	
	private class Paddle{
		public int px,py,width,height;
	}
}


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * Author: Nick McRae
 * Purpose: This is the core functionality of the applet that allows the game to run
 * Revision History:
 * Aug 4: Initial Implementation, laid the basic framework of the applet, had falling bombs, a laser, and rough splitters
 * Aug 11: Finished the game, corrected splitter dynamics, tweaked some of the numbers, added a score and a game-over message
 * Aug 16: Added comments
 */

public class blasteroids extends JApplet implements Runnable
{
	//bombarray holds all of the bombs, splitters holds the splitters when they're created
	Bomb[] bombArray;
	Bomb[] splitters;
	
	int screenWidth = 600;
	int screenHeight = 600;
	
	Thread calcThread;
	boolean isRunning = true;
	
	//counter that increments the falling bomb every time a bomb is destroyed.. once bomb is destroyed, another one falls
	int incrementBomb = 0;	

	private final int BASE_X = 0;
	private final int BASE_Y = 600;
	
	//laser position
	private int currentX = 0;
	private int currentY = 0;
	
	//for calculating hits on the bombs from the lazer ... the "two" variables are for splitters
	double radius;
	double radiusTwo;
	double laserDistance;
	double laserDistanceTwo;
	
	//when this gets to 10 the user gets a nuclear bomb and he is told on screen
	int getsNuclear = 0;
	
	//used as a score to display on screen, when a bomb is destroyed it is decremented by one until the user reaches zero
	int score = 50;
	
	//variable used to shake background on a nuke explosion in the paint method
	int shakeBackground = 0;
	
	//boolean used in the dynamics of the nuke explosion, if converted to true the background shakes in the paint method
	boolean explosion = false;
	
	//when a bomb hits the bottom of the screen this is changed to true and the game is over
	boolean humanityDestroyed = false;
	
	public void init()
	{
		//add listeners to the app
		this.addMouseListener(new MyMouseListener() );
		this.addKeyListener(new MyKeyListener());
		
		setBackground(Color.black); 
		
		//create bomb arrays
		bombArray = new Bomb[50];
		splitters = new Bomb[2];
		
        offScreen = createImage(screenWidth, screenHeight); 
         
        bufferGraphics = offScreen.getGraphics();
		
        //create the initial bomb array which needs to be cleared to win the game
		for( int i = 0; i < 50; i++ )
		{
			bombArray[i] = new Bomb();
		}		
		
		//start the game
		this.calcThread = new Thread(this);
		this.calcThread.start();
	}
	
	public void stop()
	{		
		isRunning = false;
		calcThread = null;
	}
	
	public void calcPositions(Bomb bomb)
	{		
		//the bomb relevant to the calcpositions function starts falling in the y direction
		bomb.yCoord += bomb.velocity;	
		
		//calculate lazer distance and radius upon mouse hits
		laserDistance = (Math.sqrt(Math.pow(Math.abs(currentX - bomb.xCoord), 2) + Math.pow(Math.abs(currentY - bomb.yCoord), 2)));		
		radius = (double)bomb.diameter/2;
				
		//if hit decrement strength
		if(laserDistance < radius)
		{
			bomb.strength -= 20;			
		}		
		
		//if bomb destroyed
		if(bomb.strength <= 0)
		{	
			//if bomb is splitter create bomblets and get rid of the original bomb, break method
			if(bomb.isSplitter == true)
			{
				Bomb bombTwo = new Bomb(bomb, -50);
				Bomb bombThree = new Bomb(bomb, 50);
				splitters[0] = bombTwo;
				splitters[1] = bombThree;
				getsNuclear++;
				bomb.yCoord = -200;
				bomb.velocity = 0;				
				return;
			}
			
			bomb.yCoord = -200;
			bomb.velocity = 0;
			incrementBomb++;
			getsNuclear++;	
			score--;
		}
		
		if(bomb.yCoord > 600)
		{
			humanityDestroyed = true;
		}
	}
	
	//calc position for splitters, basically the same as the last calcposition but for two bombs instead of one
	public void calcSplitterPositions(Bomb bomb, Bomb bombTwo)
	{		
		bomb.yCoord += bomb.velocity;	
		bombTwo.yCoord += bombTwo.velocity;		
		
		laserDistance = (Math.sqrt(Math.pow(Math.abs(currentX - bomb.xCoord), 2) + Math.pow(Math.abs(currentY - bomb.yCoord), 2)));		
		radius = (double)bomb.diameter/2;
		
		laserDistanceTwo = (Math.sqrt(Math.pow(Math.abs(currentX - bombTwo.xCoord), 2) + Math.pow(Math.abs(currentY - bombTwo.yCoord), 2)));
		radiusTwo = (double)bombTwo.diameter/2;
		
		if(laserDistance < radius)
		{
			bomb.strength -= 20;			
		}	
		
		if(laserDistanceTwo < radiusTwo)
		{
			bombTwo.strength -= 20;
		}
		
		if(bomb.strength <= 0)
		{
			bomb.yCoord = -200;
			bomb.velocity = 0;
		}
		
		if(bombTwo.strength <= 0)
		{
			bombTwo.yCoord = -200;
			bombTwo.velocity = 0;
		}
		
		//if both bombs have been destroyed
		if(bomb.strength <= 0 && bombTwo.strength <= 0)
		{				
			incrementBomb++;
			getsNuclear += 2;			
			splitters = new Bomb [2];
			score--;
		}
		
		if(bomb.yCoord > 600 || bombTwo.yCoord > 600)
		{
			humanityDestroyed = true;
		}
	}
	
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		bufferGraphics.clearRect(0, 0, screenWidth, screenHeight);
		
		this.setSize(screenWidth, screenHeight);
		Color blue = new Color(0.0f, 0.0f, 1.0f);
		this.getContentPane().setBackground(Color.BLACK);
		
		//if the nuclear bomb has been set off shake the background
		if(explosion)
		{
			shakeBackground++;
			
			if(shakeBackground % 2 == 0)
			{
				this.getContentPane().setBackground(Color.RED);
			}
			else
			{
				this.getContentPane().setBackground(Color.BLUE);
			}
			
			if(shakeBackground > 150)
			{
				explosion = false;
				shakeBackground = 0;
			}
		}
		
		this.setSize(screenWidth, screenHeight);
		//set the color of the bombs
		g.setColor(blue);
		
		for(int i = 0; i < bombArray.length; ++i)
		{	
			bufferGraphics.setColor(Color.BLUE);
			g.fillOval(bombArray[i].xCoord, bombArray[i].yCoord, bombArray[i].diameter, bombArray[i].diameter);			
		}
		
		//set the colour of the splitters if there is any
		if(splitters[1] != null)
		{
			for(int i = 0; i < splitters.length; ++i)
			{
				g.fillOval(splitters[i].xCoord, splitters[i].yCoord, splitters[i].diameter, splitters[i].diameter);
			}
		}
		
		//set the colour of the laswer
		g.setColor(Color.RED);
		
		g.drawLine(BASE_X, BASE_Y, currentX, currentY);
		
		//write the score and nuclear bomb stuff
		String s = "Balls left " + Integer.toString(score);
		String n = "Nuclear bomb present!";
		Font font = new Font("Serif", Font.BOLD, 32);
		g.setFont(font);
		g.drawString(s, getWidth() - 250, 50);
		
		//if nuclear is above 10 notify them that they have a bomb
		if(getsNuclear >= 10)
		{
		g.drawString(n, getWidth() - 250, 90);
		}		
		
		//if humanity is destroyed tell the user
		if(humanityDestroyed)
		{
			String exitMessage = "Humanity has been destroyed!";
			Font exitFont = new Font("Serif", Font.BOLD, 32);
			g.setFont(exitFont);
			g.drawString(exitMessage, getWidth() / 2 - 150, getHeight() / 2);
		}
		
		repaint();
	}	
	
	public void run() 
	{	
		//controlled with the while loop
		while(isRunning)
		{	
			
			if(!humanityDestroyed)
			{	
				if(splitters[1] == null)
				{
					calcPositions(bombArray[incrementBomb]);
				}
				else
				{
					calcSplitterPositions(splitters[0], splitters[1]);
				}
			}
			try
			{
				calcThread.sleep(20);
				
			}
			catch(InterruptedException ex)
			{
				System.out.println(ex.getMessage());
			}			
			
		}
		
		
	}
	
	private class MyMouseListener implements MouseListener
	{	
		public void mouseReleased(MouseEvent e)
		{
			currentX = e.getX();
			currentY = e.getY();			
			
			repaint();
		}
	}
	
	private class MyKeyListener implements KeyListener
	{	
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_SPACE && getsNuclear >= 10)
			{  
				bombArray[incrementBomb].velocity = 0;
				bombArray[incrementBomb].yCoord = -200;
				incrementBomb++;
				getsNuclear = 0;
				explosion = true;
		    } 
		}
	}	
}



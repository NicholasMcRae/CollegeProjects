/*
 * Author: Nick McRae
 * Purpose: Create class for bombs and splitters
 * Revision History:
 * Aug. 4: Initial Implementation, created the basic functionality of the class
 * Aug. 11: tweaked it a little bit to make playability better, namely lowering strength to make up for a laggy laser calculator
 */

public class Bomb
{
	int xCoord;
	int yCoord;
	int diameter;	
	int strength;
	int velocity;
	boolean isSplitter;	
	
	public Bomb()
	{
		xCoord = 50 + (int)(Math.random() * 500);
		yCoord = -60;
		diameter = 20 + (int)(Math.random() * 40);
		strength = 10 + (int)(Math.random() * 1);
		velocity = 1 + (int)(Math.random() * 1);
		
		int intSplitter = 0 + (int)(Math.random() * 100);
		
		if ( intSplitter <= 10)
			isSplitter = true;
		else
			isSplitter = false;	
	}	
	
	//splitter constructor
	public Bomb(Bomb other, int x)
	{
		xCoord = x + other.xCoord;
		yCoord = other.yCoord;
		diameter = 20;
		strength = 10;
		velocity = 1;
		isSplitter = false;			
	}	
	
	double getY()
	{
		return this.yCoord;
	}
	
}

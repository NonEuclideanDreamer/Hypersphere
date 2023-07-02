import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

//******************************************************************
// Observer.java
// @author Non-Euclidean Dreamer
// Represents an observer in S^3 with the geodesic as the viewing direction,
// *right as the right border of the image and a,b, width and height of the image.
//********************************************************************

public class Observer
{
	Spherline viewPoint; //The Point of view, including the direction
	public E4 right; //marks which direction is right in the picture. (Should not be parallel to viewPoint)
	Spherline rightward; //Geodesic from viewPoint to the right(,up)
	public int height, //Height of the picture in pixels
		 width; //Width of the picture in pixels
	public double angleRange; //How far can one see
	public static double range;
	public double screenDistance;//how far in front of the observer is the screen
	public static double dotsize=2; 
	public double exactness=0.5*dotsize/width, shield=0.1;
	public static String dm; //hf=hereforward, tb=there backward
	public static String format="png";
	public static boolean flatscreen=true; //if false, the screen is cylindrical, bent left-right
	
	
	
	//*********************************************************************
	// Constructor: Random observer, image 1000x650pixel, angleRange pi/2
	//*********************************************************************
	public Observer()
	{
		viewPoint=new Spherline();
		Random random=new Random();
		double phi=random.nextDouble()*2*Math.PI-Math.PI, psi=random.nextDouble()*Math.PI-Math.PI/2,
				theta=random.nextDouble()*Math.PI-Math.PI/2, x=random.nextDouble()-1/2, y=random.nextDouble()-1/2,
				z=random.nextDouble()-1/2, w=random.nextDouble()-1/2, xr=random.nextDouble()-1/2, yr=random.nextDouble()-1/2,
				zr=random.nextDouble()-1/2, wr=random.nextDouble()-1/2;
		Spherpoint origin=new Spherpoint(phi,psi,theta);
		viewPoint.setOrigin(origin);
		E4 momentum=new E4(x,y,z,w);
		viewPoint.setMomentum(momentum);
		right=new E4(xr,yr,zr,wr);
		right=viewPoint.getOrigin().toEuclid().normComp(right);//Assures that right lies in the tangential space
		right=viewPoint.getMomentum().normComp(right);//Assures right is normal to viewingdirection
		right=right.normalize();
		rightward=new Spherline((Spherpoint) viewPoint.getOrigin(),right);
		
		height=1000;//3508;
		width=1000;//4961;
		angleRange=2*Math.PI/3;
		screenDistance=((double) width)/2/Math.tan(angleRange/2);
		System.out.print(screenDistance);
	}
	
	//*********************************************************************
	// Constructor: Random observer, image data given
	//*********************************************************************
	public Observer(int width0, int height0, double angleRange0)
	{
		
		Random random=new Random();
		double phi=random.nextDouble()*2*Math.PI-Math.PI, psi=random.nextDouble()*Math.PI-Math.PI/2,
				theta=random.nextDouble()*Math.PI-Math.PI/2, x=random.nextDouble()-1/2, y=random.nextDouble()-1/2,
				z=random.nextDouble()-1/2, w=random.nextDouble()-1/2, xr=random.nextDouble()-1/2, yr=random.nextDouble()-1/2,
				zr=random.nextDouble()-1/2, wr=random.nextDouble()-1/2;
		Spherpoint origin=new Spherpoint(phi,psi,theta);
			
		E4 momentum=new E4(x,y,z,w);
		momentum=origin.toEuclid().normComp(momentum).normalize();
		viewPoint=new Spherline(origin,momentum);
		right=new E4(xr,yr,zr,wr);
		right=viewPoint.getOrigin().toEuclid().normComp(right);//Assure thet right is in the tangential space
		right=viewPoint.getMomentum().normComp(right);//Assure right is normal to viewingdirection
		right=right.normalize();
		rightward=new Spherline((Spherpoint) viewPoint.getOrigin(),right);
		height=height0;
		width=width0;
		angleRange=angleRange0;
		screenDistance=((double) width)/2/Math.tan(angleRange/2);
		if(!flatscreen) screenDistance=( height)/2.0/Math.tan(angleRange/2/width*height);
		System.out.println("screenDistance="+screenDistance);
		exactness=0.5*dotsize/width;
	}
	
	//*************************************************************************
	//Get the rightward geodesic
	//************************************************************
	public Spherline getRightward()
	{
		return rightward;
	}
	
	public Spherpoint position()
	{
		return viewPoint.start;
	}
	
	public E4 direction()
	{
		return viewPoint.direction;
	}
	
	public Spherline getForward()
	{
		return viewPoint;
	}

	
	//*************************************************************************
	//Get the upward geodesic or downward
	//*************************************************************************
	public Spherline getUpward()
	{
		double[][] rot=E4.rotationmatrix(position().toEuclid(),direction(), Math.PI/2);
		E4 up=right.copy().times(-1);up.transform(rot);
		return new Spherline(viewPoint.getOrigin(),up);
	}
	
	//*********************************************************************
	// Constructor: Sets the given values
	//*********************************************************************
	public Observer(Spherline pov, E4 right0,  int width0,int height0, double angle)
	{
		viewPoint=pov;
		right=viewPoint.getMomentum().normComp(right0);
		right=viewPoint.getOrigin().toEuclid().normComp(right);
		right.normalize();
		rightward=new Spherline((Spherpoint)viewPoint.getOrigin(),right);
		height=height0;
		width=width0;
		angleRange=angle;
		screenDistance=((double) width)/2/Math.tan(angleRange/2);
		if(!flatscreen)
			screenDistance=( height)/2.0/Math.tan(angleRange/2/width*height);
		exactness=0.5*dotsize/width;
	}

	//************************************************************************
	// Set up zBuffer
	//************************************************************************
	public double[][] InitiateBuffer()
	{
		double[][] zBuffer=new double[height][width];
		for (int i=0; i<width; i++)
		{
			for (int j=0; j<height; j++)
				zBuffer[j][i]=range;
		}
		return zBuffer;	
	}
	
	//*********************************************************************
	// Make a copy
	//*********************************************************************
	public Observer copy()
	{
		return new Observer(viewPoint.copy(),right.copy(),width,height, angleRange);
	
	}
	
	//***********************************************
	//Print location & Focus
	//***********************************************
	public void println()
	{
		Spherpoint focus=focus(), origin=viewPoint.getOrigin();
		System.out.println("observer ("+Math.toDegrees(origin.getPhi())+", "+
				Math.toDegrees(origin.getPsi())+", "+
				Math.toDegrees(origin.getTheta())+") looking at ("+Math.toDegrees(focus.getPhi())+", "
				+Math.toDegrees(focus.getPsi())+", "+Math.toDegrees(focus.getTheta())+")");
	}
	
	//*********************************************************************
	// Set up the graphic
	//*********************************************************************
	public BufferedImage setup(int background)
	{
		BufferedImage image= new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
		for (int xPixel=0; xPixel<width; xPixel++)
		{
			for (int yPixel=0; yPixel<height;yPixel++)
			{
				image.setRGB(xPixel,yPixel,background);
			}
		}
		
		println();
		
		return image;
	}
	
	//*********************************************************************
	// Draws a point, keeping track of the pixel 
	//*********************************************************************
	public double drawPoint(BufferedImage image, double[][] zBuffer, Spherpoint point)
	{	
		double depth=0, radial, alpha;// depth into the image; radial and alpha the spherical coordinates of the direction

			
		if (point.equals(position()))
		{
			for (int i=0; i<width; i++)
			{
				for (int j=0; j<height; j++)
				{
					if (zBuffer[j][i]>2*Math.PI)
					{
						zBuffer[j][i]=2*Math.PI;
						image.setRGB(i,j,point.getColor());
					}
				}
			}
			return 2*Math.PI;
		}
		if (point.equals(((Spherpoint) position()).getAntipodal()))// colors everything if image point antipodal
		{
			for (int i=0; i<width; i++)
			{
				for (int j=0; j<height; j++)
				{
					if (zBuffer[j][i]>Math.PI)
					{
						zBuffer[j][i]=Math.PI;
						image.setRGB(i,j,point.getColor());
					}
				}
			}
			return Math.PI;
		}
			
		if(flatscreen) 
		{
			int xPixel, yPixel;
			E4 direction=position().direction(point);
			radial=Math.abs(getForward().angle(point));
			depth=point.distance(position());
			
			if (radial>Math.PI/2)
			{
				depth=2*Math.PI-depth;
				radial=Math.PI-radial;
				direction=direction.times(-1.0);
			}
			else if(radial>Math.PI) return -depth;
			alpha=Math.signum(position().toEuclid().det(direction(), right, direction))*getRightward().angle(direction().normComp(direction)); //think about sign!
			xPixel=width/2+(int) Math.round((Math.cos(alpha)*screenDistance*Math.tan(radial)));
			yPixel=height/2-(int) Math.round((Math.sin(alpha)*screenDistance*Math.tan(radial)));
			double pointradius=(int)(Math.abs(dotsize/Math.sin(depth)));
			
				
			int color=point.getColor();
			for(int i=yPixel-(int)pointradius;i<yPixel+pointradius;i++)
			{
				double r= Math.sqrt(Math.pow(pointradius,2)-Math.pow(yPixel-i,2));
				
				for(int j=xPixel-(int)r;j<xPixel+r;j++)
			//Check if the point is inside the image
					if (i>=0 && j>=0 && i<height && j<width)
					{
		
						// Check if the pixel is already used closer
						
						if(zBuffer[i][j]>depth)//test whether that hits the right Pixel!
						{
							zBuffer[i][j]=depth;
							// Draw the pixel
							image.setRGB(j,i,color);
						}
					}
			}
		}
		else
		{
			int xPixel, yPixel, pi=(int)(width/angleRange*Math.PI);
			E4 direction=position().direction(point);
			E4 horComp=direction().paraComp(direction).add(right.paraComp(direction));
				
			double hangle=Math.signum(Math.cos(getRightward().angle(horComp)))*getForward().angle(horComp),
					vangle=Math.signum(position().toEuclid().det(direction(), right, direction))*horComp.angle(direction);
				
			depth=point.distance(position());
				
			xPixel=(int)Math.round(hangle/angleRange*width+width/2);
			yPixel=height/2-(int)Math.round(screenDistance*Math.tan(vangle));
			int altX=xPixel+pi;if(altX>width)altX=xPixel-pi;
			
			double pointradius=(int)(Math.abs(dotsize/Math.sin(depth)));
			int color=point.getColor();
			for(int i=yPixel-(int)pointradius;i<yPixel+pointradius;i++)
			{
				double r= Math.sqrt(Math.pow(pointradius,2)-Math.pow(yPixel-i,2));int k;
				
				for(int j=xPixel-(int)r;j<xPixel+r;j++)	
				//Check if the point is inside the image
				{		
					if(j>width-1)k=j-2*pi;else if(j<0)k=j+2*pi;else k=j;
					
					if (i>=0 && k>=0 && i<height && k<width)
					{
			
						// Check if the pixel is already used closer
						if(zBuffer[i][k]>depth)//test whether that hits the right Pixel!
						{
							zBuffer[i][k]=depth;
							// Draw the pixel
							image.setRGB(k,i,color);
						}
					}
				}
			}
			depth=2*Math.PI-depth;
				
			for(int i=-yPixel-(int)pointradius;i<-yPixel+pointradius;i++)
			{
				double r= Math.sqrt(Math.pow(pointradius,2)-Math.pow(yPixel-i,2));int k;
				
				for(int j=altX-(int)r;j<altX+r;j++)
				{
					if(j>width-1)k=j-2*pi;else if(j<0)k=j+2*pi;else k=j;//Check if the point is inside the image
					
					if (i>=0 && k>=0 && i<height && k<width)
					{
			
						// Check if the pixel is already used closer
				
						if(zBuffer[i][k]>depth)//test whether that hits the right Pixel!
						{
							zBuffer[i][k]=depth;
							// Draw the pixel
							image.setRGB(k,i,color);
						}
					}
				}
			}
		}
		return depth;
	}
	//*********************************************************************
	// Draws a point, keeping track of the pixel, in finite-speed universe, light traveled >revolutions*PI
	//*********************************************************************
		public void drawPoint(BufferedImage image, double[][] zBuffer, Spherpoint point, double d, Color background)
		{	
			double depth=0, radial, alpha,// depth into the image; radial and alpha the spherical coordinates of the direction

			reldepth=point.distance(position());depth=d;
			if(depth>range)return;
			if(flatscreen) 
			{
				
				int xPixel, yPixel;
				E4 direction=position().direction(point);
				radial=Math.abs(getForward().angle(point));
				
				//double reldepth=Math.abs(depth%Math.PI);
				if(reldepth<2*exactness||Math.PI-reldepth<2*exactness)//Point is here or antipodal->color everything
				{
					int color=Spherpoint.combine(new Color(point.getColor()), background, 1-depth/range).getRGB();
					for (int i=0; i<width; i++)
					{
						for (int j=0; j<height; j++)
						{
							if (zBuffer[j][i]>depth)
							{
								zBuffer[j][i]=depth;
								image.setRGB(i,j,color);
							}
						}
					}
					return;
				}
				if (radial>Math.PI/2)//peripheral
				{
					return;
				}
				alpha=Math.signum(position().toEuclid().det(direction(), right, direction))*getRightward().angle(direction().normComp(direction)); //think about sign!
				xPixel=width/2+(int) Math.round((Math.cos(alpha)*screenDistance*Math.tan(radial)));
				yPixel=height/2-(int) Math.round((Math.sin(alpha)*screenDistance*Math.tan(radial)));
				double pointradius=(Math.abs(dotsize/Math.sin(reldepth)));
				
//	System.out.println("depth="+depth%Math.PI+", reldepth="+reldepth);
	
				int color=(Spherpoint.combine(new Color(point.color), background, 1-depth/range)).getRGB();

				for(int i=yPixel-(int)pointradius;i<yPixel+pointradius;i++)
				{
					double r= Math.sqrt(Math.pow(pointradius,2)-Math.pow(yPixel-i,2));
					
					for(int j=xPixel-(int)r;j<xPixel+r;j++)
				//Check if the point is inside the image
						if (i>=0 && j>=0 && i<height && j<width)
						{
			
							// Check if the pixel is already used closer
							
							if(zBuffer[i][j]>depth)//test whether that hits the right Pixel!
							{
								zBuffer[i][j]=depth;
								// Draw the pixel
								image.setRGB(j,i,color);
							}
						}
				}
			}
			else
			{
				int xPixel, yPixel, pi=(int)(width/angleRange*Math.PI);
				E4 direction=position().direction(point);
				E4 horComp=direction().paraComp(direction).add(right.paraComp(direction));
					
				double hangle=Math.signum(Math.cos(getRightward().angle(horComp)))*getForward().angle(horComp),
						vangle=Math.signum(position().toEuclid().det(direction(), right, direction))*horComp.angle(direction);
				//	System.out.println("vangle="+vangle);
				depth=point.distance(position())+d*Math.PI;
				if(depth%Math.PI<5*exactness||depth%Math.PI>Math.PI-5*exactness)//Points blocking the view
				{
					int color=Spherpoint.combine(new Color(point.getColor()), background, 1-depth/range).getRGB();
					for (int i=0; i<width; i++)
					{
						for (int j=0; j<height; j++)
						{
							if (zBuffer[j][i]>depth)
							{
								zBuffer[j][i]=depth;
								image.setRGB(i,j,color);
							}
						}
					}
					return;
				}
				//	System.out.println(range);
				xPixel=(int)Math.round(hangle/angleRange*width+width/2);
				yPixel=height/2-(int)Math.round(screenDistance*Math.tan(vangle));
			//	System.out.println("yPixel="+yPixel+", screendistance="+screenDistance);
				int altX=xPixel+pi;if(altX>width)altX=xPixel-pi;
				
				double pointradius=(Math.abs(dotsize/Math.sin(depth)));
				int color=Spherpoint.combine(new Color (point.getColor()),background, Math.pow(255, -depth/range)).getRGB();
				for(int i=yPixel-(int)pointradius;i<yPixel+pointradius;i++)
				{
					double r= Math.sqrt(Math.pow(pointradius,2)-Math.pow(yPixel-i,2));int k;
					
					for(int j=xPixel-(int)r;j<xPixel+r;j++)	
					//Check if the point is inside the image
					{		
						if(j>width-1)k=j-2*pi;else if(j<0)k=j+2*pi;else k=j;
						
						if (i>=0 && k>=0 && i<height && k<width)
						{
				
							// Check if the pixel is already used closer
							if(zBuffer[i][k]>depth)//test whether that hits the right Pixel!
							{
								zBuffer[i][k]=depth;
								// Draw the pixel
								image.setRGB(k,i,color);
							}
						}
					}
				}
			}
			
		}
	//*********************************************************************
	// Draws a Geodesic up to t0, keeping track of the pixels
	//*********************************************************************
	public void drawLine(BufferedImage image, double[][] zBuffer, Spherline line, double t0)
	{
		double speed=line.speed();
		Spherpoint start=line.getOrigin(),end=line.location(t0);
		Spherline line0=line.copy();
		line0.setMomentum(line.getMomentum().normalize());
		double t=0;
				
		while( t<=t0*speed)
		{
			Spherpoint location=line0.location(t);
			location.setColor(line0.getOrigin().getColor());
			drawPoint(image,zBuffer,location);
			t=t+exactness;
		}
			
	}
		
	//*********************************************************************
	// Draws a filled circle given by center, normal vector and radius
	//*********************************************************************
	public double fullCircle(BufferedImage image, double[][] zBuffer, Spherpoint center, E4 normal, double radius)
	{
		drawPoint(image,zBuffer,center);
		E4 central=center.toEuclid();
		E4 normal0=central.normComp(normal);
		normal0=normal0.normalize();
		E4 right0=central.normComp(right);//random starting direction for drawing(in the circleplane)
		right0=normal0.normComp(right0);
		right0=right0.normalize();
		double dangle=exactness/Math.sin(radius);
		double[][] rot=E4.rotationmatrix(central, normal0, dangle);
		double angle;
		for ( int k=3;k<100;k++)
		{
			angle=radius/k;
			E4 vector=right0.times(Math.sin(angle)).add(central.times(Math.cos(angle)));
			Spherpoint point=vector.toSphere();
			point.setColor(center.getColor());
			drawPoint(image, zBuffer, point);
		
			for(int i=1;i<2*Math.PI/dangle;i++)
			{
				vector.transform(rot);
				point=vector.toSphere();
				point.setColor(center.getColor());
				drawPoint(image, zBuffer, point);
			}
			angle=angle*1.2;
		}
		E4 vector=right0.times(Math.sin(0)).add(central.times(Math.cos(0)));
		Spherpoint point=vector.toSphere();
		point.setColor(center.getColor());
		double depth=drawPoint(image, zBuffer, point);
		return depth;
	}
		
	//*********************************************************************
	// Draws a Sphere with the appointed Center and radius (Should be smaller PI/2) //In Progress!
	//*********************************************************************
	public double drawSphere(BufferedImage image, double[][] zBuffer, Spherpoint center, double radius, double perforation)
	{
		E4 vector=center.direction(position());
		if (radius>=center.distance(position()))
		{
			vector=vector.times(-1);//Assures the vector shows to the center of the Sphere, that is actually seen
		}
		if (getForward().angle(center)>Math.PI/2)
		{
			vector=vector.times(-1);
		}
		vector.normalize();
		E4 central=center.toEuclid();
		double[][] rot=E4.rotationmatrix(vector, central, perforation);//rotates around the vector pointing to the observer
			
		E4 right0=vector.normComp(right);
		right0=right0.normalize();
		Spherline searchline=new Spherline(center,vector);
		drawPoint(image,zBuffer,searchline.location(radius));
			
		for(double rad=perforation; rad<=Math.PI/2;rad=rad+perforation)
		{
			E4 searchvector=vector.times(Math.cos(rad)).add(right0.times(Math.sin(rad)));
			drawPoint(image,zBuffer,center.translate(searchvector.times(radius)));
			for(double angle=perforation;angle<2*Math.PI;angle=angle+perforation)
			{
				searchvector.transform(rot);
				drawPoint(image,zBuffer,center.translate(searchvector.times(radius)));
			}
		}
		double depth=drawPoint(image,zBuffer,center);
		return depth;
	}
		
	//*********************************************************************
	//Save the finished image
	//*********************************************************************
	public void finish(BufferedImage image, String name)
	{
			/*JFrame frame= new JFrame("name");
			frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
			JPanel panel=new JPanel();
			panel.setBackground(background);
			panel.setPreferredSize(new Dimension(width,height));
			Graphics2D graphic=image.createGraphics();*/
		File outputfile = new File(name+"."+format);
		try 
		{
			ImageIO.write(image, format, outputfile);
		} 
		catch (IOException e) 
		{
				System.out.println("IOException");
				e.printStackTrace();
		}
			
			
	}
		
	//*********************************************************
	// Moves the Observer into the watching direction
	//*********************************************************
	public void moveForward(double time)
	{
		right=right.transport(time, getForward());
		getForward().transport(time);
		Spherline line=getForward().copy();
		line.setMomentum(right);
		setRightward(line);
	}
		
	//********************************************************
	// Moves the Observer to the right
	//********************************************************
	public void moveRightward(double time)
	{
		E4 momentum=direction().transport(time,getRightward());
		getRightward().transport(time);
		right=getRightward().getMomentum();
		getForward().setOrigin(getRightward().getOrigin());
		getForward().setMomentum(momentum);
	}
		
	//********************************************************
	// Moves the Observer along the line Does not make sence if line doesnt start at pov
	//********************************************************
	public void moveAlong(Spherline line,double time)
	{
		E4 momentum=direction().transport(time,line);
		right=right.transport(time, line);
		line.transport(time);
		getForward().setOrigin(line.getOrigin());
		getForward().setMomentum(momentum);
				
		getRightward().setOrigin(line.getOrigin());
		getRightward().setMomentum(right);
	}
		
	//**************************	
	// Rewinds the buffer by 2PI
	//**************************
	public static void rewind(double[][] zBuffer)
	{
		for (int i=0; i<zBuffer[0].length;i++)
		{
			for (int j=0; j<zBuffer.length;j++)
			{
				zBuffer[j][i]=zBuffer[j][i]+2*Math.PI;
			}
		}
	}
		
	//****************************************
	// Gives you the point you are looking at 90° from you
	//*******************************************
	public Spherpoint focus()
	{
		Spherpoint focus=getForward().location(Math.PI/2/getForward().speed());
		return focus;
	}
			
	//******************************************************************************************************
	// When drawing a point from to opposing points of view, this gives the depth from the second perspective
	//*******************************************************************************************************
	public static double codepth(double depth)
	{
		double codepth=0;
		if(dm.equals("hf"))codepth=depth;
		else if(dm.equals("hb"))codepth=2*Math.PI-depth;
		else if(dm.equals("tb"))codepth=(3*Math.PI-depth)%(2*Math.PI);
		else if(dm.equals("tf"))codepth=(Math.PI+depth)%(2*Math.PI);
		return codepth;
	}

	//******************************************
	// Don't remember what I did here?
	//********************************************
	public void divfinish(BufferedImage image, String name, int i)
	{
		int width=image.getWidth()/i, height=image.getHeight();

		for(int n=0;n<i;n++)
		{
			File outputfile = new File(name+n+"."+format);
			BufferedImage im=new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
			for(int k=0;k<width;k++)
			{
				for(int l=0; l<height;l++)
				{
					im.setRGB(k, l, image.getRGB((n*width+k), l));
				}
			}
			try
			{
				ImageIO.write(im, format, outputfile);
			} catch (IOException e) {	System.out.println("IOException"); e.printStackTrace();}
		}				
	}
	
	//******************************************
	//Draw geodesic from two opposing povs
	//******************************************
	public void doubleDrawLine(BufferedImage image1,BufferedImage image2, double[][] zBuffer1,double[][] zBuffer2, Spherline line, double t0)
	{
		double speed=line.speed();
		Spherline line0=new Spherline(line.getOrigin(),line.getMomentum().normalize());
		double t=0;
		while( t<=t0*speed)
		{
			Spherpoint location=line0.location(t);
			doubleDrawPoint(image1,image2,zBuffer1,zBuffer2,location);
			t=t+exactness;
		}
		
	}
	
	//*********************************************************************
	// Draws a point,in two images of two observers at opposite locations looking in opposite directions, with same colors keeping track of the pixell
	//*********************************************************************
	public double doubleDrawPoint(BufferedImage image1,BufferedImage image2, double[][] zBuffer1,double[][] zBuffer2, Spherpoint point)
	{
		double depth, radial, alpha,depth2;// depth into the image; radial and alpha the spherical coordinates of the direction
		if (point.equals(viewPoint.getOrigin()))
		{
			for (int i=0; i<width; i++)
			{
				for (int j=0; j<height; j++)
				{
					if (zBuffer1[j][i]>2*Math.PI)
					{
						zBuffer1[j][i]=2*Math.PI;
						image1.setRGB(i,j,point.getColor());
					}
					depth2=codepth(2*Math.PI);
					if(zBuffer2[j][i]>depth2)
					{
						zBuffer2[j][i]=depth2;
						image2.setRGB(i, j, point.getColor());
					}
				}
			}
			return 2*Math.PI;
		}
		if (point.equals(((Spherpoint) viewPoint.getOrigin()).getAntipodal()))// colors everything if image point antipodal
		{
			for (int i=0; i<width; i++)
			{
				for (int j=0; j<height; j++)
				{
					if (zBuffer1[j][i]>Math.PI)
					{
						zBuffer1[j][i]=Math.PI;
						image1.setRGB(i,j,point.getColor());
					}
					depth2=codepth(Math.PI);
					if(zBuffer2[j][i]>depth2)
					{
						zBuffer2[j][i]=depth2;
						image2.setRGB(i,j,point.getColor());
					}
				}
			}
			return Math.PI;
		}
		int xPixel, yPixel;
		
		E4 direction=viewPoint.getOrigin().direction(point);
		radial=Math.abs(viewPoint.angle(point));
		depth=point.distance(viewPoint.getOrigin());
		if (radial>Math.PI/2)
		{
			depth=2*Math.PI-depth;
			radial=Math.PI-radial;
			direction=direction.times(-1.0);
		}
		depth2=codepth(depth);
		alpha=Math.signum(viewPoint.getOrigin().toEuclid().det(viewPoint.getMomentum(), right, direction))*rightward.angle(viewPoint.getMomentum().normComp(direction)); //think about sign!
		xPixel=width/2+(int) Math.round((Math.cos(alpha)*screenDistance*Math.tan(radial)));
		yPixel=height/2-(int) Math.round((Math.sin(alpha)*screenDistance*Math.tan(radial)));
		double pointradius=(int)(Math.abs(dotsize/Math.sin(depth)));
		
		int color=point.getColor();
		for(int i=yPixel-(int)pointradius;i<yPixel+pointradius;i++)
		{
			double r= Math.sqrt(Math.pow(pointradius,2)-Math.pow(yPixel-i,2));
			
			
			for(int j=xPixel-(int)r;j<xPixel+r;j++)	
				//Check if the point is inside the image
				if (i>=0 && j>=0 && i<height && j<width)
				{
	
					// Check if the pixel is already used closer
		
					if(zBuffer1[i][j]>depth)//test wether that hits the right Pixel!
					{
						zBuffer1[i][j]=depth;
						// Draw the pixel
						image1.setRGB(j,i,color);
					}
			
					if(zBuffer2[i][j]>depth2)//test wether that hits the right Pixel!
					{
				
						zBuffer2[i][j]=depth2;
						// Draw the pixel
						image2.setRGB(width-1-j,i,color);
					}
				}
		}
		return depth;
	}

	//************************************************************************
	//sets right to the direczion of line. line should start at the same point
	//************************************************************************
	public void setRightward(Spherline line) 
	{
		rightward=line;
		right=line.direction;
		
	}
	
	//***********************************************************************
	//Checks whether the distance is less than shield
	//***********************************************************************
	public boolean closeTo(Spherline spherline) 
	{
		return position().distance(spherline.getOrigin())<shield;
	}

	//************************************************************************
	// Maps the photon to the screen depending on what direction its moving in
	//************************************************************************
	public boolean map(Spherline line, double[][][]screen) 
	{
		E4 v=line.direction.transport(1, line.start.geodesic(position()));
		
		double radial=v.angle(direction());
		//System.out.print("radial="+radial);
		if(radial>Math.PI/2)return false;
		boolean out=false;
		double alpha=Math.signum(position().toEuclid().det(direction(), right, v))*getRightward().angle(direction().normComp(v)); //think about sign!
	//	System.out.println(", alpha="+alpha);
		int	xPixel=width/2+(int) Math.round((Math.cos(alpha)*screenDistance*Math.tan(radial))),
				yPixel=height/2-(int) Math.round((Math.sin(alpha)*screenDistance*Math.tan(radial)));
		
		for(int i=xPixel-(int)dotsize;i<xPixel+dotsize;i++)for(int j=yPixel-(int)dotsize;j<yPixel+dotsize;j++)
		{if(i>-1&&i<width&&j>-1&&j<height)
		{
			for(int k=0;k<3;k++)
			screen[i][j][k]+=line.getColor()[k];
		//	if(line.getColor()[0]<255)System.out.print(xPixel+","+yPixel);
			out=true;
		}}
	//	if(out)System.out.println("x="+xPixel+", y="+yPixel+" ("+screen[xPixel][yPixel][0]+",");
		return out;
	}

	//**********************************
	//Turn rightward by the given angle
	//**********************************
	public void turn(double angle) 
	{
		E4 v1=direction().copy(),
			v2=right;
		viewPoint.setMomentum(v1.times(Math.cos(angle)).add(v2.times(Math.sin(angle))));
		setRightward(new Spherline(position(),v2.times(Math.cos(angle)).add(v1.times(-Math.sin(angle)))));
	}
}

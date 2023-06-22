import java.awt.Color;
import java.awt.image.BufferedImage;

public class FiniteSpeed 
{
	static int denseness=11, filling=10, dotsize=4, width=1080,height=1080, revolutions=1;
	static Color background=ForwardVideo.randomColorMatt(100);
	static boolean empty=true,
			foggy=false;//foggy: range how far light can be seen, increases
	static double speed=2*Math.PI/1440, 
			c=1,
			exactness=0.5*dotsize/width,
			depth=Math.PI/2,
			angle=Math.PI/2;//angle between moving direction and looking direction
	static Observer eye, eye2;
	static Cellcomplex[] subject;
	static double[][]zBuffer,
		transformation;
	static BufferedImage image;
	static String name="cont1c2depth90angle", 
			format="png";
	public static void main(String[] args)
	{
		Observer.dotsize=dotsize;
		Observer.range=depth;
		eye=new Observer(width, height,Math.PI/2);
		
		subject=Polychoron.cont();//new Cellcomplex[] {(Polychoron.tiling335())};
		transformation=E4.randomrot();
		
		for(int i=0;i<1440;i++)
		{
			if(foggy)
			Observer.range=depth;
			
			zBuffer=eye.InitiateBuffer();
			image=eye.setup(background.getRGB());
			System.out.print(i);
			draw();//Draw the Cellcomplex
			print(i);
			eye.moveForward(speed);
			if(foggy)
			{
				depth+=c*speed;
				revolutions=1+(int)(depth/Math.PI);
			}
			
		}
		
	}
	
	//********************************************************
	// Print the buffered image to a file with the chosen name
	//********************************************************
	private static void print() 
	{
		eye.finish(image, name);
	}
	//********************************************************************************
	// Print the buffered image to a file with the chosen name , appending the index t
	//********************************************************************************
	private static void print(int t) 
	{
		eye.finish(image, name+t);
	}
	
	//****************************************************************
	// Drawing the cellcomplex to the BufferedImage, using the zBuffer
	//****************************************************************
	public static void draw()
	{
		int i,j,k;
		double l;
		Spherline line;
		Spherline[] tocenter;
		Color color;
			

		System.out.print("faces");
		for(int m=0;m<subject.length;m++)
		for(i=0;i<subject[m].center.length;i++)
		{
			System.out.print(i+".");
			color=(subject[m].center[i].transform(transformation).standardcolor());
			tocenter=new Spherline[subject[m].sidepoint[i].length];
			for(j=0;j<subject[m].sidepoint[i].length;j++)
			{
				tocenter[j]=subject[m].vertex[subject[m].sidepoint[i][j]].geodesic(subject[m].center[i]);
			}
				
			for(j=0;j<subject[m].sidepoint[i].length;j++)
			{
				k=j-1; if(k<0){k=subject[m].sidepoint[i].length-1;}
					
				for (l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					line=tocenter[k].location(l).geodesic(tocenter[j].location(l));
					line.setColor(color);
					drawLine(line);
							
					if(empty==false)
					{
						line=tocenter[k].location(1-l).geodesic(tocenter[j].location(1-l));
						line.setColor(color);
						drawLine(line);
					}
						
				}
			}
		}
	System.out.println();	
	}

	//******************************
	// Draw the geodesic from 0 to 1
	//******************************
	private static void drawLine(Spherline line) 
	{
		double sped=line.speed();
		line.setMomentum(line.getMomentum().normalize());
	
		double t=0;
				
		while( t<=sped)
		{
			Spherpoint location=line.location(t);
			location.setColor(line.getOrigin().getColor());
			drawPoint(location);
			t=t+exactness;
		} 
			
	}

	//*****************************
	// Draw a point
	//*************
	private static void drawPoint(Spherpoint point) 
	{
		
		E4 dir=eye.direction().normComp(eye.position().direction(point)).normalize();
		//If the geodesic is the equator of the great spher containing the point. this is the closer pole
		Spherpoint pole=new Spherline(eye.position(),dir).location(Math.PI/2), antipodal=point.getAntipodal();

		double dmin=Math.PI/2-pole.distance(point),
				t0=pole.geodesic(point).location(1+dmin/(1+dmin)).distance(eye.position()),
				t,lastt=0;
		for(int i=0;i<2*revolutions;i++)
		{
			t=findT(t0,dmin,i);
			if(t==-1 || t-lastt<0.000001) {}
			else {
				lastt=t;
			//System.out.println("t="+t+", dmin="+dmin+", t0="+t0);
			eye2=eye.copy();
			eye2.moveForward(-t); 
			eye2.turn(angle);
			if(i%2==0)
				eye2.drawPoint(image, zBuffer, point,i,background);
			else
				eye2.drawPoint(image, zBuffer, antipodal, i,background);}
		}
		
	}

	//*********************************************************************************************
	// At what time did the light start at the relative location to the observer, to reach it now.
	//********************************************************************************************
	private static double findT(double t0, double dmin, int i) 
	{//bruteforce
	/*	double step=0.25/c, min=3, t=i*Math.PI/c, d, tmin=t, tmax=(i+1)*Math.PI/c;
	//	System.out.println("step="+step);
		while(step>Math.max(0.00000000000001,1.0/width/c*Math.pow(dmin, 3)))
		{
			while(t<tmin+16*step&&t<tmax)
			{
				d=Math.abs(Math.cos(c*t)-(Math.cos(dmin)*Math.cos(t0-t)));
				if(d<min)
				{
					min=d;
					tmin=t;
					//System.out.print("tmin="+tmin);
				}
				t+=step;
			}
			//System.out.println();
			t=tmin-step;
			step/=8;
		//	System.out.println("t="+t+", step="+step);
		}*/
		//Newton's mehod
		double t=Math.PI/2*c*(i+0.5),f=Math.cos(c*t)-Math.cos(dmin)*Math.cos(t0-t),f1;
		int counter=0;
		while(Math.abs(f)>0.00000000000001&&counter<200)
		{
			f1=Math.cos(dmin)*Math.sin(t-t0)-Math.sin(c*t)*c;
			t=t-f/f1;
			f=Math.cos(c*t)-Math.cos(dmin)*Math.cos(t0-t);
			counter++;
		}
		if(counter==200)return -1;
		return t;
	}
	

}

import java.awt.Color;
import java.awt.image.BufferedImage;

public class FiniteSpeed 
{
	static int denseness=6, filling=5, dotsize=10, width=2560,height=1440, revolutions=1;
	static Color background=ForwardVideo.randomColorMatt(100);
	static boolean empty=true,
			acc=false,//do we accelerate?
			foggy=false;//foggy: range how far light can be seen, increases
	static double speed=2*Math.PI/1440, 
			c=2,
			exactness=0.5*dotsize/width,
			a=4,//for acceleration
			s=0.8,//for acceleration
			depth=Math.PI*2,
			angle=Math.PI/6;//angle between moving direction and looking direction
	static Observer eye, eye2;
	static Cellcomplex[] subject;
	static double[][]zBuffer,
		transformation;
	static BufferedImage image;
	static String name="rectdodAcc2depth30angle", 
			format="png";
	public static void main(String[] args)
	{
		Observer.dotsize=dotsize;
		Observer.range=depth;
		eye=new Observer(width, height,Math.PI/2);
		
		subject=Polychoron.rect(Polychoron.tiling533(),4);//new Cellcomplex[] {(Polychoron.tiling335())};//
		transformation=E4.randomrot();
		
		for(int i=0;i<10000;i++)
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
			if(acc)
				s+=speed;
			
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
				t0=pole.geodesic(point).location(Math.PI/2/(Math.PI/2-dmin)).distance(eye.position()),
				t,lastt=0;
		
		if(eye.viewPoint.location(t0).distance(point)>eye.viewPoint.location(-t0).distance(point))t0*=-1;
		
		//System.out.println(dmin+"="+eye.viewPoint.location(t0).distance(point));
		boolean goOn=true;
		while(goOn)
		{
		
			t=findT(t0,dmin,lastt);
			if(t==-1 || t-lastt<0.000001) {}
			else {
				
			lastt=t;
			
			double d=t*c;	
			if(acc) d=a*Math.log(s/(s-t));
			int i=(int)(d/Math.PI);
			//System.out.println("t="+t+", dmin="+dmin+", t0="+t0);
			eye2=eye.copy();
			eye2.moveForward(-t); 
			
			//System.out.println(eye2.position().distance(point)+"="+d);
			eye2.turn(angle);
			if(i%2==0)
				eye2.drawPoint(image, zBuffer, point,d,background);
			else
				eye2.drawPoint(image, zBuffer, antipodal, d,background);}
			if(acc) {if(a*Math.log(s/(s-t))>depth||t>s)goOn=false;}
			else if(t*c>depth)goOn=false;
		}
		
	}

	//*********************************************************************************************
	// At what time did the light start at the relative location to the observer, to reach it now.
	//********************************************************************************************
	private static double findT(double t0, double dmin, double t) 
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
	/*	double t=Math.PI/2*c*(i+0.5),f=Math.cos(c*t)-Math.cos(dmin)*Math.cos(t0-t),f1;
		int counter=0;
		while(Math.abs(f)>0.00000000000001&&counter<200)
		{
			f1=Math.cos(dmin)*Math.sin(t-t0)-Math.sin(c*t)*c;
			t=t-f/f1;
			f=Math.cos(c*t)-Math.cos(dmin)*Math.cos(t0-t);
			counter++;
		}
		if(counter==200)return -1;
		return t;*/
		
		t+=0.0000001;
		double tstep=0.02;if(acc)tstep=Math.min(tstep, s*(1-Math.exp(-Math.PI/a))/100);
		double sign0, sign1,mid;
	if(acc) {sign0=Math.signum(accf(t,t0,dmin));sign1=Math.signum(accf(t+tstep,t0,dmin));}
	else {sign0=Math.signum(f(t,t0,dmin)); sign1=Math.signum(f(t+tstep,t0,dmin));}
		while(sign0==sign1)
		{
			t+=tstep;
			if(acc)sign1=Math.signum(accf(t+tstep,t0,dmin));
			else sign1=Math.signum(f(t+tstep,t0,dmin));
		}
		double b=t+tstep;
		while(b-t>0.00000000000001)
		{
			mid=(b+t)/2;
			if (acc)sign1=Math.signum(accf(mid,t0,dmin));
			else sign1=Math.signum(f(mid,t0,dmin));
			if (sign0==sign1)
				t=mid;
			else b=mid;
		}
		return t;
	}
	
	private static double f(double t, double t0, double dmin)
	{
		return (Math.cos(c*t)-Math.cos(dmin)*Math.cos(t0+t));
	}
	private static double accf(double t, double t0, double dmin)
	{
		return Math.cos(a*Math.log(s/(s-t)))-Math.cos(dmin)*Math.cos(t0+t);
	}
	
}


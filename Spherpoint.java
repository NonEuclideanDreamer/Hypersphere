//******************************************************************
// Spherpoint.java
// @author Non-Euclidean Dreamer
//Represents one point in S^3 with coordinates phi,psi, theta.
//*****************************************************************
import java.awt.Color;
import java.util.Random;


public class Spherpoint
{
	final static double exactness=0.0005;
	final static int black=Color.black.getRGB();

	public double phi; // Determines the meridian, range: [-pi,pi]
	public double psi; // Determines the parallel, range: [-pi/2,pi/2]
	public double theta; // Determines the hypermeridian, range: [-pi/2,pi/2]
	public int color; // Determines the color of the point as an rgb-value

	//****************************************************
	// special points from hyperspherical coordinates
	//****************************************************
	public static final Spherpoint northpole=new Spherpoint(0,0,Math.PI/2);
	public static final Spherpoint southpole=new Spherpoint(0,0,-Math.PI/2);
	public static final Spherpoint nullpoint=new Spherpoint(0,0,0);
	public static final Spherpoint eqnorthpole=new Spherpoint(0,Math.PI/2,0);
	public static final Spherpoint eqsouthpole=new Spherpoint(0,-Math.PI/2,0);
	
	//******************************************************
	// special point configurations
	//******************************************************
	private static double psi0=-Math.asin(1.0/3), psi1=Math.atan(Math.sqrt(0.5)), psi2=Math.asin(1/Math.sqrt(5)), psi3=Math.asin(Math.sqrt((5+2*Math.sqrt(5))/15)), psi4=0.188710531, psi6=0.553574359,
			psi5=Math.acos(Math.sqrt((5-Math.sqrt(5))/10)),psi7=Math.acos(Math.sqrt((36-8*Math.sqrt(5))/61));
	public static final Spherpoint[] tetrahedron=new Spherpoint[]
			{new Spherpoint(Math.PI/4,psi1,0),new Spherpoint(-3*Math.PI/4,psi1,0),new Spherpoint (3*Math.PI/4,-psi1,0),new Spherpoint(-Math.PI/4,-psi1,0)};
	public static final Spherpoint[] hexahedron=new Spherpoint[]
			{new Spherpoint(Math.PI/4,psi1,0),new Spherpoint(3*Math.PI/4,psi1,0),new Spherpoint(-3*Math.PI/4,psi1,0),new Spherpoint(-Math.PI/4,psi1,0),
			new Spherpoint(Math.PI/4,-psi1,0),new Spherpoint (3*Math.PI/4,-psi1,0),new Spherpoint(-3*Math.PI/4,-psi1,0),new Spherpoint(-Math.PI/4,-psi1,0)};
	public static final Spherpoint[] octahedron=new Spherpoint[]
			{eqnorthpole, nullpoint, new Spherpoint(Math.PI/2,0,0),new Spherpoint(Math.PI,0,0),new Spherpoint(-Math.PI/2,0,0),eqsouthpole};
	public static final Spherpoint[] hexahedronEdge=new Spherpoint[]
			{new Spherpoint(0,Math.PI/4,0),new Spherpoint(Math.PI/2,Math.PI/4,0),new Spherpoint(Math.PI,Math.PI/4,0),new Spherpoint(-Math.PI/2,Math.PI/4,0),
		   new Spherpoint(0,-Math.PI/4,0),new Spherpoint(Math.PI/2,-Math.PI/4,0),new Spherpoint(Math.PI,-Math.PI/4,0),new Spherpoint(-Math.PI/2,-Math.PI/4,0)
			,new Spherpoint(Math.PI/4,0,0),new Spherpoint(3*Math.PI/4,0,0),new Spherpoint(-3*Math.PI/4,0,0),new Spherpoint(-Math.PI/4,0,0)};
	public static final Spherpoint[] icosahedron=new Spherpoint[]
			{eqnorthpole,new Spherpoint(0,psi2,0),new Spherpoint(2*Math.PI/5,psi2,0),new Spherpoint(4*Math.PI/5,psi2,0),
			new Spherpoint(-4*Math.PI/5,psi2,0),new Spherpoint(-2*Math.PI/5,psi2,0), new Spherpoint(Math.PI/5,-psi2,0),new Spherpoint(3*Math.PI/5,-psi2,0),
			new Spherpoint(-Math.PI,-psi2,0),new Spherpoint(-3*Math.PI/5,-psi2,0),new Spherpoint(-Math.PI/5,-psi2,0),eqsouthpole};
	public static final Spherpoint[] dodecahedron=new Spherpoint[]
			{new Spherpoint(Math.PI/5,psi3,0),new Spherpoint(3*Math.PI/5,psi3,0),new Spherpoint(Math.PI,psi3,0),new Spherpoint(-3*Math.PI/5,psi3,0),
			new Spherpoint(-Math.PI/5,psi3,0),new Spherpoint(Math.PI/5,psi4,0),new Spherpoint(3*Math.PI/5,psi4,0),new Spherpoint(Math.PI,psi4,0),
			new Spherpoint(-3*Math.PI/5,psi4,0),new Spherpoint(-Math.PI/5,psi4,0),new Spherpoint(0,-psi4,0),new Spherpoint(2*Math.PI/5,-psi4,0),
			new Spherpoint(4*Math.PI/5,-psi4,0),new Spherpoint(-4*Math.PI/5,-psi4,0),new Spherpoint(-2*Math.PI/5,-psi4,0),new Spherpoint(0,-psi3,0),
			new Spherpoint(2*Math.PI/5,-psi3,0),new Spherpoint(4*Math.PI/5,-psi3,0),new Spherpoint(-4*Math.PI/5,-psi3,0),new Spherpoint(-2*Math.PI/5,-psi3,0)};
	public static final Spherpoint[] dodecahedronEdge=new Spherpoint[]
			{new Spherpoint(0,psi5,0),new Spherpoint(2*Math.PI/5,psi5,0),new Spherpoint(4*Math.PI/5,psi5,0),new Spherpoint(-4*Math.PI/5,psi5,0),
			new Spherpoint(-2*Math.PI/5,psi5,0),new Spherpoint(Math.PI/5,psi6,0),new Spherpoint(3*Math.PI/5,psi6,0),new Spherpoint(Math.PI,psi6,0),
			new Spherpoint(-3*Math.PI/5,psi6,0),new Spherpoint(-Math.PI/5,psi6,0),new Spherpoint(Math.PI/10,0,0),new Spherpoint(3*Math.PI/10,0,0),
			new Spherpoint(5*Math.PI/10,0,0),new Spherpoint(7*Math.PI/10,0,0),new Spherpoint(9*Math.PI/10,0,0),new Spherpoint(-9*Math.PI/10,0,0),
			new Spherpoint(-7*Math.PI/10,0,0),new Spherpoint(-5*Math.PI/10,0,0),new Spherpoint(-3*Math.PI/10,0,0),new Spherpoint(-Math.PI/10,0,0),
			new Spherpoint(0,-psi6,0),new Spherpoint(2*Math.PI/5,-psi6,0),new Spherpoint(4*Math.PI/5,-psi6,0),new Spherpoint(-4*Math.PI/5,-psi6,0),
			new Spherpoint(-2*Math.PI/5,-psi6,0),new Spherpoint(Math.PI/5,-psi5,0),new Spherpoint(3*Math.PI/5,-psi5,0),new Spherpoint(Math.PI,-psi5,0),
			new Spherpoint(-3*Math.PI/5,-psi5,0),new Spherpoint(-Math.PI/5,-psi5,0)};
	public static final Spherpoint[] truncDodecahedron=new Spherpoint[]
			{new Spherpoint(-Math.PI/10,psi7,0),new Spherpoint(Math.PI/10,psi7,0),new Spherpoint(3*Math.PI/10,psi7,0),new Spherpoint(5*Math.PI/10,psi7,0),
			new Spherpoint(7*Math.PI/10,psi7,0),new Spherpoint(9*Math.PI/10,psi7,0),new Spherpoint(-9*Math.PI/10,psi7,0),new Spherpoint(-7*Math.PI/10,psi7,0),
			new Spherpoint(-5*Math.PI/10,psi7,0),new Spherpoint(-3*Math.PI/10,psi7,0)
			};
	//public static final Spherpoint[] hexaoctahedron=new Spherpoint[] {		
	
	//******************************************
	//Constructor: Set null coordinates
	//******************************************
	public Spherpoint ()
	{
		phi=0;
		psi=0;
		theta=0;
		color=black;
	}
	
	//******************************************
	//Constructor: Set chosen initial values, with default color black
	//******************************************
	public Spherpoint (double iphi, double ipsi, double itheta)
	{
		phi=iphi;
		psi=ipsi;
		theta=itheta;
		color=black;
		}
	//******************************************
	//Constructor: Set chosen initial values, including color
	//******************************************
	public Spherpoint (double iphi, double ipsi, double itheta, int icolor)
	{
		phi=iphi;
		psi=ipsi;
		theta=itheta;
		color=icolor;
	}
	
	//*******************************************
	//Random Spherpoint
	//*******************************************
	public static Spherpoint random()
	{
		E4 out=E4.random();
		return out.toSphere();
	}
	
	//******************************************
	// Returns the phi-coordinate of this point
	//******************************************
	public double getPhi()
	{
		return phi;
	}
	
	//******************************************
	// Returns the psi-coordinate of this point
	//******************************************
	public double getPsi()
	{
		return psi;
	}

	//******************************************
	// Returns the theta-coordinate of this point
	//******************************************
	public double getTheta()
	{
		return theta;
	}
	
	//********************************************
	// Returns Color of the Point as an rgb-value
	//********************************************
	public int getColor() 
	{
		
		return color;
	}
	
	
	//******************************************
	// Sets the phi coordinate to chosen value,
	// allowing out-of-range numbers
	//******************************************
	public void setPhi(double newphi)
	{
		while (newphi>Math.PI)
		{
			newphi=newphi-2*Math.PI;
		}
		while (newphi<-Math.PI)
		{
			newphi=newphi+2*Math.PI;
		}
		phi=newphi;
	}
	
	//********************************************
	// Sets the psi coordinate to chosen value,
	// warning from out-of-range numbers
	//********************************************
	public void setPsi(double newpsi)
	{ 
		if (Math.abs(newpsi)>Math.PI/2)
		{
			System.out.println("Warning: There was an attempt to set psi out of its domain");
		}
		else
			psi=newpsi;
	}

	//*******************************************
	// Sets the theta coordinate to chosen value,
	// warning from out-of-range numbers
	//*******************************************
	public void setTheta(double newtheta)
	{ 
		if (Math.abs(newtheta)>Math.PI/2)
		{
			System.out.println("Warning: There was an attempt to set theta out of its domain");
		}
		else
			theta=newtheta;
	}
	
	//*********************************************
	//Sets the given color
	//*********************************************
	public void setColor(int c)
	{
		color=c;
	}

	
	//*******************************************
	//Checks for equality with another point (not color)
	//*******************************************
	public boolean equals (Spherpoint point)
	{   
		if (Math.abs(theta)>=Math.PI/2-exactness)
		{
			if (Math.abs(theta-point.getTheta())<=exactness)
			{
				return true;
			}
			else
				return false;
		}
		else if (Math.abs(theta-point.getTheta())<=exactness)
		
		{
			if (Math.abs(psi)>=Math.PI/2-exactness)
			{
				if (Math.abs(psi-point.getPsi())<=exactness)
				{
					return true;
				}
				else
					return false;
			}
			else if (Math.abs(psi-point.getPsi())<=exactness)
			{
				if (Math.abs(phi-point.getPhi())<=exactness)
				{
					return true;
				}
				else
					return false;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	//*******************************************
	//Constructs the antipodal object of the same color
	//*******************************************
	public Spherpoint getAntipodal()
	{
		double aphi;
		Spherpoint antipodal;
		if (phi>0)
		{
			aphi=phi-Math.PI;
		}
		else
			aphi=phi+Math.PI;
		antipodal=new Spherpoint(aphi,-psi,-theta,color);
		
		return antipodal;
	}

	//********************************************
	// Returns the distance to another point 
	//*********************************************
	public double distance(Spherpoint point)
	{	
		if (equals(point))
		{
			return 0;
		}
		else
		{
			double sintheta, costheta, sinpsi, cospsi, sinphi, cosphi;
			sintheta=Math.sin(theta)*Math.sin(point.getTheta());
			costheta=Math.cos(theta)*Math.cos(point.getTheta());
			sinpsi=Math.sin(psi)*Math.sin(point.getPsi());
			cospsi=Math.cos(psi)*Math.cos(point.getPsi());
			sinphi=Math.sin(phi)*Math.sin(point.getPhi());
			cosphi=Math.cos(phi)*Math.cos(point.getPhi());
			return Math.acos(sintheta+costheta*(sinpsi+cospsi*(sinphi+cosphi)));
		}
	}

	//*******************************************
	// Returns the point in E4
	//*******************************************
	public E4 toEuclid()
	{
		double x,y,z,w;
		w=Math.sin(theta);
		z=Math.cos(theta)*Math.sin(psi);
		y=Math.cos(theta)*Math.cos(psi)*Math.sin(phi);
		x=Math.cos(theta)*Math.cos(psi)*Math.cos(phi);
		E4 euc=new E4 (x,y,z,w,color);
		return euc;
	}
	
	//********************************************
	// Calculates the angle between the two points
	// from the point of view
	//********************************************
	public double angle(Spherpoint point1, Spherpoint point2)
	{
		double leg1, leg2, base;
		leg1=distance(point1);
		leg2=distance(point2);
		base=point1.distance(point2);
		if (leg1==0 || leg2==0 || leg1==Math.PI || leg2==Math.PI)
		{
			return 0;
		}
		else
		return Math.acos((Math.cos(base)-Math.cos(leg1)*Math.cos(leg2))/Math.sin(leg1)/Math.sin(leg2));
	}

	//********************************************
	// Calculates the Geodesic to the point
	//********************************************
	public Spherline geodesic(Spherpoint destination)
	{
		E4 momentum=destination.toEuclid();
		momentum=toEuclid().normComp(momentum);
		momentum=momentum.times(distance(destination)/momentum.norm());
	
		Spherline geodesic=new Spherline(this.copy(),momentum);
		return geodesic;
	}
	
	//**********************************
	// middlepoint between this and that
	//**********************************
	public Spherpoint middle(Spherpoint that)
	{
		return geodesic(that).location(0.5);
	}
	
		
	//********************************************
	// Gives a point in the direction of the vector
	//********************************************
	public Spherpoint translate(E4 direction)
	{
		Spherline line=new Spherline(this,direction);
		return line.location(1);
	}
	
	//*********************************************
	// Maps the point with a linear transformation in E4
	//*********************************************
	public Spherpoint transform(double[][] matrix)
	{
		E4 vector=toEuclid();
		vector.transform(matrix);
		Spherpoint point=vector.toSphere();
		point.color=color;
		return point;
	}


	public Spherpoint copy()
	{
		Spherpoint point=new Spherpoint(getPhi(),getPsi(),getTheta(),getColor());
		return point;
	}
	
	//******************************************
	// Prints the Coordinates of the point in degrees going to a new line after
	//******************************************
	public void println()
	{
		System.out.println("point: ("+(phi/Math.PI*180)+", "+psi/Math.PI*180+", "+theta/Math.PI*180+")");
	}
	
	//******************************************
	// Prints the Coordinates of the point in radian
	//******************************************
	public void print()
	{
		System.out.print("point: ("+(phi)+", "+psi+", "+theta+")");
	}
		
	
	//Gives the color as in "Imagecoordinates"
	public Color standardcolor()
	{
		if(Math.abs(theta)>=Math.PI/2)
		{phi=0;psi=0;}
		else if(Math.abs(psi)>=Math.PI/2)
			phi=0;
		return new Color((int)((phi+Math.PI)*127.9/Math.PI),(int)((psi+Math.PI/2)*255.9/Math.PI),((int)((theta+Math.PI/2)*255.9/Math.PI)));
	}
	
	//Gives the color as in "Imagecoordinates"2
	public double[] stcolor()
	{
		if(Math.abs(theta)>=Math.PI/2)
		{phi=0;psi=0;}
		else if(Math.abs(psi)>=Math.PI/2)
			phi=0;
		return new double[] {(Math.toDegrees(phi)+180)*255/360,(Math.toDegrees(psi)+90)*255/180,(Math.toDegrees(theta)+90)*255/180};
	}
	
	//********************************************
	// Gives the direction to the given point
	//********************************************
	public E4 direction(Spherpoint point)
	{
		return geodesic(point).getMomentum();
	}
	
	//*********************************************************
	//which of the given points is next to the current location?
	//**********************************************************
	public int nextTo(Spherpoint[] point)
	{
			int k=0;
			double dist=distance(point[0]),dist2;
			for(int i=1;i<point.length;i++)
			{
				dist2=distance(point[i]);
				if(dist2<dist)
				{
					k=i;
					dist=dist2;
				}
			}
			return k;
	}
	
	//auxiliary methods
	
	//*****************************************************************
	//Gives a linear comb. of the colors, with a times the first color
	//******************************************************************
	public static Color combine(Color color1, Color color2, double a)
	{
		if((a==1))return color1;
			
		a=a-(int)a;
		int red, green, blue;
		red=(int) (color1.getRed()*a+color2.getRed()*(1-a));
		green=(int) (color1.getGreen()*a+color2.getGreen()*(1-a));
		blue=(int) (color1.getBlue()*a+color2.getBlue()*(1-a));
		try{Color color=new Color(red,green,blue);return color;}catch(java.lang.IllegalArgumentException e) {
			System.out.println("Error: a="+a);
		}
		return color2;
	}

	public int nextTo(Spherpoint[] point, int i, int[][] nb) 
	{
		int k=i;
		double dist=distance(point[i]),dist2;
		for(int j=0;j<nb[i].length;j++)
		{
			dist2=distance(point[nb[i][j]]);
			if(dist2<dist)
			{
				k=nb[i][j];
				dist=dist2;
			}
		}
		return k;
	}

}



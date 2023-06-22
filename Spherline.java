//********************************************************************
// Spherline.java
// @author Non-Euclidean Dreamer
// Represents one geodesic in S^3 by a Spherpoint and a direction in E4
//**********************************************************************
import java.awt.Color;
import java.util.Random;

public class Spherline 
{

	Spherpoint start;
	E4 direction;
	double[] color;
	
	//**************************************************
	// Special & important geodesics
	//**************************************************
	public static final Spherline nullmeridian=new Spherline(Spherpoint.northpole,Spherpoint.northpole.direction(Spherpoint.nullpoint));
	public static final Spherline polemeridian=new Spherline(Spherpoint.northpole,Spherpoint.northpole.direction(Spherpoint.eqnorthpole));
	public static final Spherline eqnullmeridian=new Spherline(Spherpoint.eqnorthpole,Spherpoint.eqnorthpole.direction(Spherpoint.nullpoint));
	public static final Spherline equator=new Spherline(Spherpoint.nullpoint,Spherpoint.nullpoint.direction(new Spherpoint(Math.PI/2,0,0)));

	//**************************************************
	// Constructor: Set null Geodesic
	//**************************************************
	public Spherline()
	{
		start=new Spherpoint();
		direction=new E4();
	}
		
	//****************************************************
	// Constructor: Set chosen values
	//****************************************************
	public Spherline(Spherpoint st, E4 dir) 
	{
		start=st;
		direction=dir;
		Color c=new Color(st.getColor());
		color=new double[] {c.getRed(),c.getGreen(),c.getBlue()};
		
	}
	
	//******************************************************
	// Returns a random geodesic ToDo: make more uniform
	//******************************************************
	public static Spherline random()
	{
		Spherline out=new Spherline();
		Random random=new Random();
		double phi=random.nextDouble()*2*Math.PI-Math.PI, psi=Math.asin(random.nextDouble()*Math.PI-1),
				theta=Math.asin(random.nextDouble()*Math.PI-1);
		Spherpoint origin=new Spherpoint(phi,psi,theta);
		out.setOrigin(origin);
		E4 momentum=E4.random();
		momentum=origin.toEuclid().normComp(momentum).normalize();
		out.direction=momentum;
		
		return out;
	}

	//*****************************************************
	// Returns the position at t=0
	//******************************************************
	public Spherpoint getOrigin()
	{
		return (Spherpoint) start;
	}
	
	
	//*****************************************************
	// Returns the direction at t=0
	//*****************************************************
	public E4 getMomentum()
	{
		return direction;
	}
	
	//********************************************************
	// Set s as origin, letting direction & color as is
	//********************************************************
	public void setOrigin(Spherpoint s)
	{
		start=s;
		//color=s.getColor();
	}
	
	//***************************************************************
	// Sets the direction to the normal component of the given vector
	//***************************************************************
	public void setMomentum(E4 vector)
	{
		E4 origin=start.toEuclid();
		vector=origin.normComp(vector);
		direction=vector;
	}
	
	//*******************************************************
	// Sets the (initial) Color of the Geodesic
	//*****************************************************
	public void setColor(Color color2)
	{
		start.setColor(color2.getRGB());
		setOrigin(start);
		color=Cellcomplex.toDouble(color2);
	}
	
	//*******************************************************
	// Sets the (initial) Color of the Geodesic
	//*****************************************************
	public void setColor(double[] color1, int color2)
	{
		start.setColor(color2);
		setOrigin(start);
		color=color1;
	}
	
	//************************************************************************* unfinished
	//gives the midpoint of the line chain with given lengths that gets closest to given point, starting at the origin and ending at position(1)
	//*************************************************************************
	/*public Spherpoint(double l1,double l2, Spherpoint direction)
	{
		double d=vector0.norm();
		double x=Math.atan((Math.cos(l2)-Math.cos(d)*Math.cos(l1))/(Math.cos(l1)*Math.sin(d)));
		Spherpoint p;
	}*/
	

	
	//****************************************************
	// Gets the location at time t 
	//****************************************************
	public Spherpoint location(double t)
	{
		E4 origin=start.toEuclid();
		double speed=direction.norm();
		E4 normal=direction.normalize();
		E4 Eloc=origin.times(Math.cos(speed*t));
		Eloc=Eloc.add(normal.times(Math.sin(speed*t)));
		Spherpoint location= Eloc.toSphere();
		location.setColor(start.getColor());
		return location;
	}
	
	//****************************************************
	// Gets the speed of the geodesic
	//****************************************************
	public double speed()
	{
		return getMomentum().norm();
	}
	
	//******************************************************
	// Gets the direction at time t
	//******************************************************
	public E4 momentum(double t)
	{
		E4 origin=start.toEuclid();
		E4 normal=direction.normalize();
		double speed=direction.norm();
		E4 Eloc=origin.times(-Math.sin(speed*t));
		Eloc=Eloc.add(normal.times(Math.cos(speed*t)));
		return Eloc.times(speed);
	}
	

	
	//*******************************************************
	// Calculates the angle between this geodesic and the one from the Origin to the given point
	//*******************************************************
	public double angle(Spherpoint point)
	{
		return start.angle(direction.toSphere(), point);
	}

	//*******************************************************
	//transforms the line by the given matrix
	//*******************************************************
	public Spherline transform(double[][] transformation)
	{
		Spherpoint origin=(start).transform(transformation);
		E4 momentum=direction;
		momentum.transform(transformation);
		return new Spherline(origin,momentum);
	}
	
	//******************************************************
	// Calculates the angle between this geodesic and the one with the same origin and the given momentum
	//******************************************************
	public double angle(E4 momentum)
	{
		return start.angle(direction.toSphere(), momentum.toSphere());
	}
	
	
	//************************
	//Copies start & direction
	//************************
	public Spherline copy()
	{
		Spherline line=new Spherline(new Spherpoint(getOrigin().getPhi(),getOrigin().getPsi(),getOrigin().getTheta()),getMomentum().copy());
		line.setColor(color,start.getColor());
		return line;
	}
	
	//*******************************************************
	// Same geodesic, same speed but different start
	//*******************************************************
	public void transport(double time)
	{
		Spherpoint spher=location(time);
		direction=momentum(time);
		start=spher;
	}
	
	//*******************************************************
	// Transports the given line with coinciding origin along this line by time t
	//*******************************************************
	public Spherline transport(Spherline line, double t)
	{
		Spherline out=new Spherline(location(t), line.getMomentum().transport(t, this));
		return out;
	}
	
	//********************************************************
	// Returns the color array
	//*******************************************************
	public double[] getColor() 
	{
		return color;
	}
	
	
	
}

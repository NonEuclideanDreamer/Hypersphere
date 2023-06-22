//***************************************************
// E4.java
// @author Non-Euclidean Dreamer
// Describes points/vectors in 4-dimensional Euclidian space
//***************************************************

import java.awt.Color;
import java.util.Random;



public class E4 
{
	//attributes
	
	private double x; // x-coordinate
	private double y; // y-coordinate
	private double z; // z-coordinate
	private double w; // 4d-coordinate
	private int color; // the point's color as an rgb-value
	
	//base vectors
	final static public E4 e1=new E4(1,0,0,0);
	final static public E4 e2=new E4(0,1,0,0);
	final static public E4 e3=new E4(0,0,1,0);
	final static public E4 e4=new E4(0,0,0,1);
	
	final static public double [][] idmatrix=new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}},
			mirmatrix= {{-1,0,0,0},{0,-1,0,0},{0,0,-1,0},{0,0,0,-1}};
	final static int black=Color.black.getRGB(), white=Color.white.getRGB();
	
	//*****************************************
	// Constructor: Set origin, black
	//*******************************************
	public E4 ()
	{
		x=0;
		y=0;
		z=0;
		w=0;
		color=black;
	}
	
	//********************************************
	// Constructor: Set chosen values, black
	//********************************************
	public E4 (double newx, double newy, double newz, double neww)
	{
		x=newx;
		y=newy;
		z=newz;
		w=neww;
		color=black;
	}
	
	//**********************************************
	// Constructor:  Set chosen values, including color
	//***********************************************
	public E4 (double newx, double newy, double newz, double neww, int newcolor)
	{
		x=newx;
		y=newy;
		z=newz;
		w=neww;
		color=newcolor;
	}
	
	//*******************************
	// Constructor from array
	//******************************
	public E4(double[] v) 
	{
		x=v[0];
		y=v[1];
		z=v[2];
		w=v[3];
		color=white;
	}

	//***************************************************
	// Returns the x-coordinate of the given point
	//***************************************************
	public double getX ()
	{
		return x;
	}
	
	//***************************************************
	// Returns the y-coordinate of the given point
	//***************************************************
	public double getY ()
	{
		return y;
	}	
	
	//***************************************************
	// Returns the z-coordinate of the given point
	//***************************************************
	public double getZ ()
	{
		return z;
	}		
	
	//*********************************
	// Returns the needed component
	//*********************************
	public double get(int i)
	{
		if(i==0)return x;
		if(i==1)return y;
		if(i==2)return z;
		else return w;
	}
	
	//***************************************************
	// Returns the w-coordinate of the given point
	//***************************************************
	public double getW ()
	{
		return w;
	}	
	
	//***************************************************
	// Returns the color of the given point
	//***************************************************
	public int getColor ()
	{
		return color;
	}	
	
	//***************************************************
	// Sets the x-coordinate of the given point to the given value
	//***************************************************
	public void setX (double newx)
	{
		x=newx;
	}
	
	//***************************************************
	// Sets the y-coordinate of the given point to the given value
	//***************************************************
	public void setY (double newy)
	{
		y=newy;
	}	
	
	//***************************************************
	// Sets the z-coordinate of the given point to the given value
	//***************************************************
	public void setZ (double newz)
	{
		z=newz;
	}		
	
	//***************************************************
	// Sets the w-coordinate of the given point to the given value
	//***************************************************
	public void setW (double neww)
	{
		w=neww;
	}	
	
	//***************************************************
	// Sets the color of the given point to the given shade
	//***************************************************
	public void  setColor (int newcolor)
	{
		color=newcolor;
	}	
	
	//*****************************************************
	// Gives a copy of the vector
	//*****************************************************
	public E4 copy()
	{
		return new E4(x,y,z,w);
	}
	
	//*****************************************************
	// Calculates the Euclidian norm of the point
	//*****************************************************
	public double norm ()
	{
		return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2)+Math.pow(w, 2));
	}
	
	//*****************************************************
	// Calculates the Scalar product with the given point
	//*******************************************************
	public double dot(E4 point)
	{
		return x*point.x+y*point.y+z*point.z+w*point.w;
	}
	
	//*****************************************************
	// Calculates the product with the given scalar
	//*****************************************************
	public E4 times(double scalar)
	{
		E4 output=new E4(scalar*x,scalar*y,scalar*z,scalar*w, color);
		return output;
	}
	
	//*******************************************************
	// Calculates the sum with the given vector (black)
	//*******************************************************
	public E4 add (E4 vector)
	{
		E4 sum=new E4(x+vector.x,y+vector.y,z+vector.z,w+vector.w);
		return sum;
	}

	//******************************************************************************
	// Generalized cross-product: returns a vector normal to all 3 involved vectors
	//******************************************************************************
	public E4 cross (E4 v, E4 t)
	{
		
		double[] c=new double[4];
		for(int i=0;i<4;i++)
		{
			int i1=(i+1)%4,i2=(i+2)%4,i3=(i+3)%4;
			c[i]=Math.pow(-1, i)*(t.get(i1)*(get(i2)*v.get(i3)-get(i3)*v.get(i2))-t.get(i2)*(get(i1)*v.get(i3)-get(i3)*v.get(i1))-t.get(i3)*(get(i1)*v.get(i2)-get(i2)*v.get(i1)));
		}
		E4 out=new E4(c[0],c[1],c[2],c[3]);
		return out;
	}
	
	//*****************************************************
	// Returns the parallel component of vector
	//*****************************************************
	public E4 paraComp(E4 vector)
	{
		double factor=dot(vector)/Math.pow(norm(), 2);
		E4 parallel=times(factor);
		return parallel;
	}
	
	//*****************************************************
	// Returns the normal component of vector
	//*****************************************************
	public E4 normComp(E4 vector)
	{
		E4 normal=vector.add(paraComp(vector).times(-1));
		return normal;
	}
	
	//******************************************************
	// Returns the normalized vector (nullvector for null vector)
	//******************************************************
	public E4 normalize()
	{
		E4 unit=new E4();
		unit=times(1/norm());
		return unit;
	}

	//*******************************************************
	// Returns the according Spherpoint, putting N and a warning for null
	//*******************************************************
	public Spherpoint toSphere ()
	{
		Spherpoint spher=new Spherpoint();
		E4 unit=normalize();
		spher.setTheta(Math.asin(unit.w));
		double argument=unit.z/Math.sqrt(1.0-Math.pow(unit.w, 2));
		if(Math.abs(argument)>1)
		{
			spher.setPsi(Math.asin(Math.signum(argument)));
		}
		else
		{
			spher.setPsi(Math.asin(argument));
		}
		/*else
		{
			spher.setPsi(0);
		}*/
		spher.setPhi(Math.atan2(y, x));
		spher.setColor(color);
		return spher;
	}
	
	//************************************************************
	// calculates the determinante
	//***********************************************************
	public double det(E4 col1, E4 col2, E4 col3)
	{
		return x*(col1.getY()*col2.getZ()*col3.getW()+col1.getZ()*col2.getW()*col3.getY()+
				col1.getW()*col2.getY()*col3.getZ()-col3.getY()*col2.getZ()*col1.getW()-col3.getZ()*col2.getW()*col1.getY()-
				col3.getW()*col2.getY()*col1.getZ())-y*(col1.getX()*col2.getZ()*col3.getW()+
				col1.getZ()*col2.getW()*col3.getX()+col1.getW()*col2.getX()*col3.getZ()-
				col3.getX()*col2.getZ()*col1.getW()-col3.getZ()*col2.getW()*col1.getX()-
				col3.getW()*col2.getX()*col1.getZ())+z*(col1.getX()*col2.getY()*col3.getW()+
				col1.getY()*col2.getW()*col3.getX()+col1.getW()*col2.getX()*col3.getY()-col3.getX()*col2.getY()*col1.getW()-
				col3.getY()*col2.getW()*col1.getX()-col3.getW()*col2.getX()*col1.getY())-w*
				(col1.getX()*col2.getY()*col3.getZ()+col1.getY()*col2.getZ()*col3.getX()+
				col1.getZ()*col2.getX()*col3.getY()-col3.getX()*col2.getY()*col1.getZ()-col3.getY()*col2.getZ()*col1.getX()-
				col3.getZ()*col2.getX()*col1.getY());
	}

	//************************************************************
	// Creates the rotationmatrix around the plane given by vector1 and vector2
	//************************************************************
	public static double[][] rotationmatrix(E4 vector1, E4 vector2,double angle)//To do: if det(transfmatrix)=0...
	{
		//give orthonormal transfmatrix
		vector1=vector1.normalize();
		vector2=vector1.normComp(vector2);
		vector2=vector2.normalize();
		E4 vector3=vector1.normComp(e1.add(e3.times(0.3467).add(e2.times(1.111))));
		vector3=vector2.normComp(vector3);
		vector3=vector3.normalize();
		E4 vector4=vector1.normComp(e2.add(e4.times(3.4).add(e1.times(0.5667))));
		vector4=vector2.normComp(vector4);
		vector4=vector3.normComp(vector4);
		vector4=vector4.normalize();
		double[][] transfmatrix=new double[4][4];
		for (int i=0; i<4; i++)
		{
			transfmatrix[i][0]=vector1.array()[i];
			transfmatrix[i][1]=vector2.array()[i];
			transfmatrix[i][2]=vector3.array()[i];
			transfmatrix[i][3]=vector4.array()[i];
		}
		
		double[][] inv=new double[4][4];
		double[][] rot=new double[][]{{1,0,0,0},{0,1,0,0},{0,0,Math.cos(angle),Math.sin(angle)},{0,0,-Math.sin(angle),Math.cos(angle)}};//generische rotationmatrix
		
		for (int i=0;i<4;i++)//lines
		{
			for (int j=0;j<4;j++)//rows
			{
				double[][] copy =new double[][]{{transfmatrix[0][0],transfmatrix[0][1],transfmatrix[0][2],transfmatrix[0][3]},
						{transfmatrix[1][0],transfmatrix[1][1],transfmatrix[1][2],transfmatrix[1][3]},
						{transfmatrix[2][0],transfmatrix[2][1],transfmatrix[2][2],transfmatrix[2][3]},
						{transfmatrix[3][0],transfmatrix[3][1],transfmatrix[3][2],transfmatrix[3][3]}};
				
				for (int k=0;k<4;k++)
				{
					copy[k][j]=0;
					copy[i][k]=0;
				}
				copy[i][j]=1;
				
				inv[j][i]=getColumn(copy,0).det(getColumn(copy,1), getColumn(copy,2), getColumn(copy,3));
			}
		}
		
		double[][] AR=new double[4][4];//transfmatrix*rot
		for (int i=0;i<4;i++)
		{
			for (int j=0;j<4;j++)
			{
				AR[i][j]=getRow(transfmatrix,i).dot(getColumn(rot,j));
			}
		}
		double[][] rotation=new double[4][4];//wanted rotationmatrix
		for (int i=0;i<4;i++)
		{
			for (int j=0;j<4;j++)
			{
				rotation[i][j]=getRow(AR,i).dot(getColumn(inv,j));
			}
		}

		return rotation;
	}
	
	//*****************************************
	// Returns a random rotationmatrix
	//*****************************************
	public static double[][] randomrot()
	{
		Random random=new Random();
		E4 vector1=new E4(random.nextDouble()-0.5,random.nextDouble()-0.5,random.nextDouble()-0.5,random.nextDouble()-0.5);
		E4 vector2=new E4(random.nextDouble()-0.5,random.nextDouble()-0.5,random.nextDouble()-0.5,random.nextDouble()-0.5);
		double angle=random.nextDouble()*Math.PI;
		
		return rotationmatrix(vector1,vector2,angle);
	}
	
	//****************************************
	// Gives an array with the coordinates
	//***************************************
	public double[] array()
	{
		double[] array=new double[4];
		array[0]=x;
		array[1]=y;
		array[2]=z;
		array[3]=w;
		return array;
	}
	
	//********************************************************
	//Transforms the vector with the given matrix
	//********************************************************
	public void transform(double[][] matrix)
	{
		E4 vector=new E4(getRow(matrix,0).dot(this),getRow(matrix,1).dot(this),getRow(matrix,2).dot(this),getRow(matrix,3).dot(this));
		x=vector.x;
		y=vector.y;
		z=vector.z;
		w=vector.w;
	}
	
	//********************************************************
	// Transports the vector by t along the given geodesic
	//********************************************************
	public E4 transport(double t, Spherline geodesic)
	{
		E4 momentum=geodesic.getMomentum();
		double hypernormpart=dot(geodesic.getOrigin().toEuclid()),
				parapart=dot(momentum.normalize());
		E4 rest=this.add(geodesic.getOrigin().toEuclid().times(hypernormpart*(-1))).
				add(momentum.normalize().times(parapart*(-1)));
		E4 newvector=rest.add(geodesic.location(t).toEuclid().times(hypernormpart).
				add(geodesic.momentum(t).normalize().times(parapart)));
		
		return newvector;
		
		//sphercomp=geodesic.getOrigin().toEuclid().normComp(this);
	}
	
	
	//****************************************************
	// gets the indexth column of the given matrix as a E4
	//****************************************************
	public static E4 getColumn(double[][] matrix, int index)
	{
		E4 vector=new E4(matrix[0][index],matrix[1][index],matrix[2][index],matrix[3][index]);
		return vector;
	}
	
	//*************************************************
	// gets the indexth row of the given matrix as a E4
	//**************************************************
	public static E4 getRow(double[][] matrix, int index)
	{
		E4 vector=new E4(matrix[index][0],matrix[index][1],matrix[index][2],matrix[index][3]);
		return vector;
	}

	//*******************************
	//Prints the vector coordinates
	//******************************
	public void println()
	{
		System.out.println("vector: ("+x+", "+y+", "+z+", "+w+")");
	}

	//*************************************
	// Returns the angle between this and v
	//*************************************
	public double angle(E4 v) 
	{
		return Math.acos(dot(v)/norm()/v.norm());
	}

	//****************************
	// Returns a random vector
	//****************************
	public static E4 random() 
	{
		double[]v=new double[4];
		Random rand=new Random();
		
		for(int i=0;i<4;i++)
		v[i]=rand.nextGaussian();
		
		return new E4(v);
	}
	
	
	
}

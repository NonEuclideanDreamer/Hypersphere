import java.awt.Color;
import java.awt.image.BufferedImage;

//***********************************************************************************
// Cellcomplex.java
// author @Non-Euclidean Dreamer
// Creates the 2-cellcomplex by the given set of vertices and edge- and side combinations, with a colortransformation
// Can't deal with antipodal edgepoints
//***********************************************************************************

public class Cellcomplex 
{
	Spherpoint[] vertex;
	Spherpoint[] center;
	int[][] edgepoint,
		sidepoint;
	
	//draw config
	public static boolean gap=false, empty=false;
	public static int filling=5,denseness=26;
	public static double radius=Math.PI/180, perforation=radius;
	public static boolean vertices=false;//Shall we draw vertices as spheres?
	
	//****************************************
	// Constructor
	//****************************************
	public Cellcomplex(Spherpoint[] vertex0, Spherpoint[] center0, int[][] edgepoint0, int[][] sidepoint0)
	{
		vertex=vertex0;
		center=center0;
		edgepoint=edgepoint0;
		sidepoint=sidepoint0;
	}
	
	//*****************************************
	// Gives you an array of the edges' centers
	//*****************************************
	public Spherpoint[] edgemiddles()
	{
		Spherpoint[] out=new Spherpoint[edgepoint.length];
		for(int i=0;i<edgepoint.length;i++)
		{
			out[i]=vertex[edgepoint[i][0]].middle(vertex[edgepoint[i][1]]);
		}
		return out;
	}
	
	//**********************************************************
	//Draws the Cellcomplex with given rotation
	//**********************************************************
	public void draw(Observer eye,BufferedImage image,double[][] zBuffer,double[][] transformation, Color background, double blurRatio)
	{
		int i,j,k;
		double l;
		Spherline line;
		Spherline[] tocenter;
		Color color;
		
		System.out.print("vertices...");

		if(vertices)
			for(i=0;i<vertex.length;i++)
			{
				vertex[i].setColor(Spherpoint.combine(vertex[i].transform(transformation).standardcolor(),background,blurRatio).getRGB());
				eye.drawPoint(image, zBuffer, vertex[i]);//, radius, perforation
			}
		
		if(gap)
		{
			System.out.println("edges...");
			for (i=0;i<edgepoint.length;i++)
			{
				line=vertex[edgepoint[i][0]].geodesic(vertex[edgepoint[i][1]]);
				line.setColor((Spherpoint.combine(line.location(0.5).transform(transformation).standardcolor(), background, blurRatio)));
				eye.drawLine(image, zBuffer, line, 1);
			}
		}
		int to;
		if(sidepoint[0].length==3)to=56; else to=30;
		System.out.print("faces");
		for(i=0;i<center.length;i++)
		{
			System.out.print(i+".");
			color=(Spherpoint.combine(center[i].transform(transformation).standardcolor(), background, blurRatio));
			tocenter=new Spherline[sidepoint[i].length];
			for(j=0;j<sidepoint[i].length;j++)
			{
				tocenter[j]=vertex[sidepoint[i][j]].geodesic(center[i]);
			}
			
			for(j=0;j<sidepoint[i].length;j++)
			{
				k=j-1; if(k<0){k=sidepoint[i].length-1;}
				
				for (l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					line=tocenter[k].location(l).geodesic(tocenter[j].location(l));
					line.setColor(color);
					eye.drawLine(image, zBuffer, line, 1);
						
					if(empty==false)
					{
						line=tocenter[k].location(1-l).geodesic(tocenter[j].location(1-l));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 1);
					}
					
				}
			}
		}
	}
	
	//***********************************
	//Draw from two opposing povs
	//************************************
	public void doubledraw(Observer eye,BufferedImage image1, BufferedImage image2,double[][] zBuffer1,double[][]zBuffer2,double[][] transformation, Color background, double blurRatio)
	{
		int i,j,k;
		double l;
		Spherline line;
		Spherline[] tocenter;
		Color color;
		if(vertices)
		System.out.print("vertices...");
	/*	for(i=0;i<vertex.length;i++)
		{
			vertex[i].setColor(Spherpoint.combine(vertex[i].transform(transformation).standardcolor(),background,blurRatio));
			eye.doubleDrawPoint(image1,image2, zBuffer1,zBuffer2, vertex[i]);
		}
		
		if(gap)
		{
			System.out.println("edges...");
			for (i=0;i<edgepoint.length;i++)
			{
				line=vertex[edgepoint[i][0]].geodesic(vertex[edgepoint[i][1]]);
				line.setColor(Spherpoint.combine(line.location(0.5).transform(transformation).standardcolor(), background, blurRatio));
				eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 1);
			}
		}*/
		System.out.print("faces");int r=0;
		for(i=0;i<center.length;i++)
		{/*if(sidepoint[i][0]!=0 && sidepoint[i][1]!=0)*/{
			System.out.print(".");
			
			color=(Spherpoint.combine(center[i].transform(transformation).standardcolor(), background, blurRatio));
			//System.out.print(sidepoint[i].length);
			tocenter=new Spherline[sidepoint[i].length];
			for(j=0;j<sidepoint[i].length;j++)
			{
				
				tocenter[j]=vertex[sidepoint[i][j]].geodesic(center[i]);
			}
			
			for(j=0;j<sidepoint[i].length;j++)
			{
				k=j-1; if(k<0){k=sidepoint[i].length-1;}
				System.out.print("l");
				for (l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					line=tocenter[k].location(l).geodesic(tocenter[j].location(l));
					line.setColor(color);
					eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 1);
						
					if(empty==false)
					{
						line=tocenter[k].location(1-l).geodesic(tocenter[j].location(1-l));
						line.setColor(color);
						eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 1);
					}
					
				}
			}
		}
		}
		verMirror(image2);
		System.out.println();
	}
	
	private void verMirror(BufferedImage image2)
	{
		int x=image2.getWidth(),
			y=image2.getHeight();
		BufferedImage temp=new BufferedImage(x,y,BufferedImage.TYPE_BYTE_INDEXED);
		for(int i=0;i<x;i++)
		{
			for(int j=0;j<y;j++)
			{
				temp.setRGB(i, j, image2.getRGB(i, y-j-1));
			}
		}
	}

	//*************************
	//Draws the subject twice from antipodal points of view looking to the same point
	//************************
	public void doubleRevdraw(Observer eye,BufferedImage image1, BufferedImage image2,double[][] zBuffer1,double[][]zBuffer2,double[][] transformation, Color background, double blurRatio)
	{
		int i,j,k;
		double l;
		Spherline line;
		Spherline[] tocenter;
		Color color;
		
		
		if(gap)
		{
			System.out.println("edges...");
			for (i=0;i<edgepoint.length;i++)
			{
				line=vertex[edgepoint[i][0]].geodesic(vertex[edgepoint[i][1]]);
				line.setColor((Spherpoint.combine(line.location(0.5).transform(transformation).standardcolor(), background, blurRatio)));
				eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 1);
			}
		}
		System.out.print("sides");int r=0;
		for(i=0;i<center.length;i++)
		{
			System.out.print(".");
			color=(Spherpoint.combine(center[i].transform(transformation).standardcolor(), background, blurRatio));
			tocenter=new Spherline[sidepoint[i].length];
			for(j=0;j<sidepoint[i].length;j++)
			{
				
				tocenter[j]=vertex[sidepoint[i][j]].geodesic(center[i]);
			}
			
			for(j=0;j<sidepoint[i].length;j++)
			{
				k=j-1; if(k<0){k=sidepoint[i].length-1;}
				System.out.print("l");
				for (l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					line=tocenter[k].location(l).geodesic(tocenter[j].location(l));
					line.setColor(color);
					eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 1);
						
					if(empty==false)
					{
						line=tocenter[k].location(1-l).geodesic(tocenter[j].location(1-l));
						line.setColor(color);
						eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 1);
					}
			}
		}
		}
		System.out.println();
	}

	
	//****************************
	// For regular faces: creates the center array
	//*****************************
	static Spherpoint[] centers(Spherpoint[] vertex,int[][]sidepoint)
	{
		int n=sidepoint.length,
			l=sidepoint[0].length;
		double s=vertex[sidepoint[0][2]].distance(vertex[sidepoint[0][1]]);
		boolean even=(l%2==0);
		Spherpoint[]c=new Spherpoint[n];
		if(even)
		{
			for (int i=0;i<n;i++)
			{
				System.out.print(i);
				c[i]=vertex[sidepoint[i][0]].geodesic(vertex[sidepoint[i][l/2]]).location(0.5);
				print(sidepoint[i]);
				System.out.println("s="+vertex[sidepoint[i][0]].distance(vertex[sidepoint[i][1]]));
			}
		}
		else
		{
			for (int i=0;i<n;i++)
			{
				System.out.print(i);
				Spherline line=vertex[sidepoint[i][1]].geodesic(vertex[sidepoint[i][0]].geodesic(vertex[sidepoint[i][2]]).location(0.5));
				line.setMomentum(line.getMomentum().times(1/line.speed()));
				c[i]=line.location((Math.atan(Math.tan(s/2)/Math.cos(Math.PI/n))+Math.PI)%Math.PI);
				print(sidepoint[i]);
				System.out.println("s="+vertex[sidepoint[i][1]].distance(vertex[sidepoint[i][0]]));

			}
		}
	
	return c;
	
	}

	//****************************
	//Print int-array to terminal
	//****************************
	static void print(int[] is) 
	{
		System.out.print("{");
		for(int i=0;i<is.length;i++)
		{
			System.out.print(is[i]+",");
		}
		System.out.println("}");
	}
	
	public static double[] toDouble(Color col)
	{
		return new double[] {col.getRed(),col.getGreen(),col.getBlue()};
	}

	//********************************************************
	// Returns minimal distance from each vertex to the others
	//********************************************************
	public double[] vertexDistances() 
	{
		int n=vertex.length;
		double[]out=new double[n];
		for(int i=0;i<n;i++)
		{
			double k=2*Math.PI,l;
			for(int j=0;j<n;j++)if(i!=j)
			{
				l=vertex[i].distance(vertex[j]);
				if(l<k)
				{
					k=l;
				}
			}
			out[i]=k;
		}
			
				
		return out;
	}
	
	public int[][] neighbourList()
	{
		int n=edgepoint.length*2/vertex.length;System.out.println(n+" edges per vertex");
		int[][]out=new int[vertex.length][n];
		for(int i=0;i<vertex.length;i++)
		{
			int c=0, k=0;
			while(c<n && k<edgepoint.length)
			{
				int j=0;
				if (edgepoint[k][j]!=i)j++;
				if(edgepoint[k][j]==i)
				{
					out[i][c]=edgepoint[k][1-j];
					c++;
				}
				k++;
			}
		}
		return out;
	}
	
	
}
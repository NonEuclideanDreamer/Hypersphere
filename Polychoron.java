//***********************************************************************************
// Polychoron.java
// author @Laura Kuttnig
// Collection of Polychora in S3, mostly as Cellcomplex
//***********************************************************************************


import java.awt.image.*;
import java.util.ArrayList;
import java.awt.Color;


public class Polychoron
{
	private static final double radius=Math.PI/180, perforation=radius;
	public static final int filling=10; 
	private static boolean gap=false, empty=false;
	final static int[][] tetrahedronEdgepoint=new int[][] {{0,1},{0,2},{0,3},{1,2},{1,3},{2,3}};
	final static int[][] hexahedronEdgepoint=new int[][] {{0,3},{0,1},{1,2},{2,3},{7,4},{5,4},{6,5},{7,6},{0,4},{1,5},{2,6},{7,3}};
	final static int[][] octahedronEdgepoint=new int[][]{{0,1},{0,2},{0,3},{0,4},{5,1},{5,2},{5,3},{5,4},{1,2},{2,3},{3,4},{4,1}};
	final static int[][] dodecahedronEdgepoint=new int[][]{{4,0},{0,1},{1,2},{2,3},{3,4},{0,5},{1,6},{2,7},{3,8},{4,9},{5,10},{5,11},{6,11},{6,12},{7,12},
			{7,13},{8,13},{8,14},{9,14},{9,10},{10,15},{11,16},{12,17},{13,18},{14,19},{15,16},{16,17},{17,18},{18,19},{19,15}};
	final static int[][] icosahedronEdgepoint=new int[][]{{0,1},{0,2},{0,3},{0,4},{0,5},{1,2},{2,3},{3,4},{4,5},{5,1},{1,6},{2,6},{2,7},{3,7},{3,8},
			{4,8},{4,9},{5,9},{5,10},{1,10},{10,6},{6,7},{7,8},{8,9},{9,10},{6,11},{7,11},{8,11},{9,11},{10,11}};
	final static int[][] tetrahedronSidepoint=new int[][] {{1,2,3},{0,2,3},{0,1,3},{0,1,2}};
	final static int[][] hexahedronSidepoint=new int[][]{{1,0,3,2},{3,0,4,7},{0,1,5,4},{1,2,6,5},{2,3,7,6},{4,5,6,7}};
	final static int[][] octahedronSidepoint=new int[][]{{0,2,1},{0,3,2},{0,4,3},{0,1,4},{5,1,2},{5,2,3},{5,3,4},{5,4,1}};
	final static int[][] dodecahedronSidepoint=new int[][]{{0,1,2,3,4},{4,9,10,5,0},{0,5,11,6,1},{1,6,12,7,2},{2,7,13,8,3},{3,8,14,9,4},
			{5,10,15,16,11},{6,11,16,17,12},{7,12,17,18,13},{8,13,18,19,14},{9,14,19,15,10},{15,19,18,17,16}};
	final static int[][] icosahedronSidepoint=new int[][]{{0,1,2},{0,2,3},{0,3,4},{0,4,5},{0,5,1},{1,6,2},{2,7,3},{3,8,4},{4,9,5},{5,10,1},
			{1,10,6},{2,6,7},{3,7,8},{4,8,9},{5,9,10},{10,11,6},{6,11,7},{7,11,8},{8,11,9},{9,11,10}};
	final static int[][] icosahedronSideedge=new int[][]{{0,5,1},{1,6,2},{2,7,3},{3,8,4},{4,9,0},{10,11,5},{12,13,6},{14,15,7},{16,17,8},{18,19,9},
			{19,20,10},{11,21,12},{13,22,14},{15,23,16},{17,24,18},{29,25,20},{25,26,21},{26,27,22},{27,28,23},{28,29,24}};//checkorientation
	final static int[][] dodecahedronSideedge=new int[][]{{0,1,2,3,4},{0,9,19,10,5},{1,5,11,12,6},{2,6,13,14,7},{3,7,15,16,8},{4,8,17,18,9},
			{11,10,20,25,21},{13,12,21,26,22},{15,14,22,27,23},{17,16,23,28,24},{19,18,24,29,20},{25,29,28,27,26}};
	final static int[][] hexahedronSideedge=new int[][] {{1,0,3,2},{0,8,4,11},{1,9,5,8},{2,10,6,9},{3,11,7,10},{5,6,7,4}};
	final static int[][] octahedronSideedge=new int[][] {{1,8,0},{2,9,1},{3,10,2},{0,11,3},{4,8,5},{5,9,6},{6,10,7},{7,11,4}};
	final static int[][] dodecahedronEdgenumber=new int[][] {{0,1},{1,1},{2,1},{3,1},{4,1},{2,0},{2,0},{2,0},{2,0},{2,0},{3,1},{4,0},{3,1},{4,0},{3,1},
			{4,0},{3,1},{4,0},{3,1},{4,0},{4,2},{4,2},{4,2},{4,2},{4,2},{3,1},{3,2},{3,3},{3,4},{3,0}};
	final static int[][] dodecahedronVertexnumber=new int[][] {{0,4,0},{1,4,0},{2,4,0},{3,4,0},{4,4,0},{3,0,1},{3,0,1},{3,0,1},{3,0,1},{3,0,1},
		{2,4,1},{2,4,1},{2,4,1},{2,4,1},{2,4,1},{3,0,2},{3,4,2},{3,3,2},{3,2,2},{3,1,2}},
			hexahedronVertexnumber= {{1,0,1},{0,0,1},{3,0,1},{2,0,1},{0,2,3},{1,2,3},{2,2,3},{3,2,3}};
	final static int[] hexaoctaTetraVertexnumber= {0,0,1,1,3,2,2,3,0,2,1,3},
					revHotVn= {2,3,3,2,1,1,0,0,1,3,0,2};
	final static int[][] thovn=new int[][] {{0,1,8},{2,3,10},{5,6,9},{4,7,11}},
					rthovn=new int[][] {{6,7,10},{4,5,8},{0,3,11},{1,2,9}};
	public static double t0,phi0,psi;
	//******************************************************
	// Draws Schläflix2y with a color transformation
	//******************************************************
 	public static String schläflix2y (Observer eye,BufferedImage image,double[][] zBuffer,int x, int y, double[][] transformation,int denseness)
	{
		Spherline line;
		int color;
		
		if (x==1)
		{
			Spherpoint vertex=Spherpoint.nullpoint;
			/*System.out.println("Drawing vertex...");
			vertex.setColor(vertex.transform(transformation).standardcolor());
			eye.drawSphere(image, zBuffer, vertex, radius, perforation);
			*/
			if(gap)
			{
				System.out.println("Drawing edge");
				Spherline edge=Spherline.equator;
				edge.setColor(edge.location(2).transform(transformation).standardcolor());
				eye.drawLine(image, zBuffer, edge, 4);
			}
			Spherpoint[] center=new Spherpoint[y];
			Spherline tocenter;
			Spherpoint point,middlepoint=new Spherpoint(), borderpoint=new Spherpoint();
			
			for(int j=0;j<y;j++)
			{
				System.out.println("Drawing "+(j+1)+". side");
				center[j]=new Spherpoint(0,Math.PI/2*Math.sin(2*Math.PI*j/y),Math.PI/2*Math.cos(2*Math.PI*j/y));
				color=center[j].transform(transformation).standardcolor().getRGB();
				
				tocenter=vertex.geodesic(center[j]);
				
				for (double l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					point=tocenter.location(l);
					point.setColor(color);
					if(empty==false){
					middlepoint=tocenter.location(1-l);
					middlepoint.setColor(color);}
					/*if (gap==false&&l%4==0)
					{borderpoint=tocenter.location(l/10000.0);
					borderpoint.setColor(color);}*/
					
					for(double k=0;k<2*Math.PI;k=k+0.001)
					{
						point.setPhi(k);
						eye.drawPoint(image, zBuffer, point);
						
						if(empty==false)
						{
							middlepoint.setPhi(k);
							eye.drawPoint(image, zBuffer, middlepoint);
						}
						
						/*if(gap==false&&l%4==0)
						{
							borderpoint.setPhi(k);
							eye.drawPoint(image, zBuffer, borderpoint);
						}*/
					}
				}
			}
		}
		
		if (x==2)
		{
			Spherpoint rightpoint=new Spherpoint(Math.PI/2,0,0);
			Spherpoint[] vertex=new Spherpoint[] {Spherpoint.nullpoint, new Spherpoint(Math.PI,0,0)},
					center=new Spherpoint[y],
					edgemiddle=new Spherpoint[]{rightpoint,rightpoint.getAntipodal()};
				
			Spherline[] tocenter=new Spherline[2],
					edge=new Spherline[]{vertex[0].geodesic(rightpoint),vertex[0].geodesic(rightpoint.getAntipodal())};
			
			
			for(int i=0;i<x;i++)
			{
				//System.out.println((i+1)+". vertex ... ");
				//vertex[i].setColor(vertex[i].transform(transformation).standardcolor());
				//eye.drawSphere(image, zBuffer, vertex[i], radius, perforation);
				
				if(gap)
				{
					System.out.println((i+1)+". edge...");
					edge[i].setColor(edge[i].location(1).transform(transformation).standardcolor());
					eye.drawLine(image, zBuffer, edge[i], 2);
				}
				
			}
			
			for(int j=0;j<y;j++)
			{
				System.out.println("Drawing "+(j+1)+". side...");
				center[j]=new Spherpoint(0,Math.PI/2*Math.sin(2*Math.PI*j/y),Math.PI/2*Math.cos(2*Math.PI*j/y));
				Color colo=center[j].transform(transformation).standardcolor();
				
				for(int i=0;i<2;i++)
				{
					tocenter[i]=edgemiddle[i].geodesic(center[j]);
					
					for(double l=1.0/denseness; l<1.0/filling;l=l+1.0/denseness)
					{
						line=vertex[0].geodesic(tocenter[i].location(l));
						line.setColor(colo);
						eye.drawLine(image, zBuffer, line, 1);
						
						line=vertex[1].geodesic(tocenter[i].location(l));
						line.setColor(colo);
						eye.drawLine(image, zBuffer, line, 1);
						
						/*if(gap==false && l%4==0)
						{
							line=vertex[0].geodesic(tocenter[i].location(l/10000.0));
							line.setColor(color);
							eye.drawLine(image, zBuffer, line, 1);
							
							line=vertex[1].geodesic(tocenter[i].location(l/10000.0));
							line.setColor(color);
							eye.drawLine(image, zBuffer, line, 1);
						}*/
						
						if(empty==false)
						{
							line=vertex[0].geodesic(tocenter[i].location(1-l));
							line.setColor(colo);
							eye.drawLine(image, zBuffer, line, 1);
							
							line=vertex[1].geodesic(tocenter[i].location(1-l));
							line.setColor(colo);
							eye.drawLine(image, zBuffer, line, 1);
						}
					}
				}
				
			}
		
		}
		
		if (x>2)
		{
			Spherpoint[] vertex=new Spherpoint[x],
					center=new Spherpoint[y];
			Spherline[] edge=new Spherline[x],
					tocenter=new Spherline[x];
			
			for(int i=0;i<x;i++)
			{
				System.out.println("Drawing "+(i+1)+". vertex");
				vertex[i]=new Spherpoint(2*Math.PI*i/x,0,0);
				vertex[i].setColor(vertex[i].transform(transformation).standardcolor().getRGB());
				eye.drawSphere(image, zBuffer, vertex[i], radius, perforation);
			}
			int k;
			if(gap)
			{
				
				for(int i=0;i<x;i++)
				{
					System.out.println("Drawing "+(i+1)+". edge");
					k=i-1;
					if (k<0){k=x-1;}
				
					edge[i]=vertex[k].geodesic(vertex[i]);
					edge[i].setColor(edge[i].location(0.5).standardcolor());
					eye.drawLine(image, zBuffer, edge[i], 1);
				
				}
			}
			
			for (int j=0;j<y;j++)
			{
				System.out.println("Drawing "+(j+1)+". side");
				center[j]=new Spherpoint(0,Math.PI/2*Math.sin(2*Math.PI*j/y),Math.PI/2*Math.cos(2*Math.PI*j/y));
				Color colo=center[j].transform(transformation).standardcolor();
				for (int i=0;i<x;i++)
				{
					tocenter[i]=vertex[i].geodesic(center[j]);
				}
				
				for (int i=0;i<x;i++)
				{
					k=i-1;
					if (k<0){k=x-1;}
					for(double l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
					{
					
						line=tocenter[k].location(l).geodesic(tocenter[i].location(l));
						line.setColor(colo);
						eye.drawLine(image, zBuffer, line, 1);
						
						/*if (gap==false && l%4==0)
						{
							line=tocenter[k].location(l/10000.0).geodesic(tocenter[i].location(l/10000.0));
							line.setColor(color);
							eye.drawLine(image, zBuffer, line, 1);
						}*/
						
						if (empty==false)
						{
							line=tocenter[k].location(1-l).geodesic(tocenter[i].location(1-l));
							line.setColor(colo);
							eye.drawLine(image, zBuffer, line, 1);
						}
					}
				}
			}
			
		}
		
		Spherpoint focus=eye.viewPoint.location(1);
		return new String ("Schläfli("+x+","+2+","+y+") from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")");

	}
 	
 	
 
 	public static String doubleSchläflix2y (Observer eye,BufferedImage image1,BufferedImage image2,double[][] zBuffer1,double[][]zBuffer2,int x, int y, double[][] transformation,int denseness,int distance)
	{
		Spherline line;
		Color color;
		
		if (x==1)
		{
			Spherpoint vertex=Spherpoint.nullpoint;
			/*System.out.println("Drawing vertex...");
			vertex.setColor(vertex.transform(transformation).standardcolor());
			eye.drawSphere(image, zBuffer, vertex, radius, perforation);
			*/
			if(gap)
			{
				System.out.println("Drawing edge");
				Spherline edge=Spherline.equator;
				edge.setColor(edge.location(2).transform(transformation).standardcolor());
				eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, edge, 4);
			}
			Spherpoint[] center=new Spherpoint[y];
			Spherline tocenter;
			Spherpoint point,middlepoint=new Spherpoint(), borderpoint=new Spherpoint();
			
			for(int j=0;j<y;j++)
			{
				System.out.println("Drawing "+(j+1)+". side");
				center[j]=new Spherpoint(0,Math.PI/2*Math.sin(2*Math.PI*j/y),Math.PI/2*Math.cos(2*Math.PI*j/y));
				color=center[j].transform(transformation).standardcolor();
				
				tocenter=vertex.geodesic(center[j]);
				
				for (double l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					point=tocenter.location(l);
					point.setColor(color.getRGB());
					if(empty==false){
					middlepoint=tocenter.location(1-l);
					middlepoint.setColor(color.getRGB());}
					/*if (gap==false&&l%4==0)
					{borderpoint=tocenter.location(l/10000.0);
					borderpoint.setColor(color);}*/
					
					for(double k=0;k<2*Math.PI;k=k+0.001)
					{
						point.setPhi(k);
						eye.doubleDrawPoint(image1,image2,zBuffer1, zBuffer2, point);
						
						if(empty==false)
						{
							middlepoint.setPhi(k);
							eye.doubleDrawPoint(image1,image2,zBuffer1, zBuffer2, middlepoint);
						}
						
						/*if(gap==false&&l%4==0)
						{
							borderpoint.setPhi(k);
							eye.drawPoint(image, zBuffer, borderpoint);
						}*/
					}
				}
			}
		}
		
		if (x==2)
		{
			Spherpoint rightpoint=new Spherpoint(Math.PI/2,0,0);
			Spherpoint[] vertex=new Spherpoint[] {Spherpoint.nullpoint, new Spherpoint(Math.PI,0,0)},
					center=new Spherpoint[y],
					edgemiddle=new Spherpoint[]{rightpoint,rightpoint.getAntipodal()};
				
			Spherline[] tocenter=new Spherline[2],
					edge=new Spherline[]{vertex[0].geodesic(rightpoint),vertex[0].geodesic(rightpoint.getAntipodal())};
			
			
			for(int i=0;i<x;i++)
			{
				System.out.println((i+1)+". vertex ... ");
				vertex[i].setColor(vertex[i].transform(transformation).standardcolor().getRGB());
				//eye.drawSphere(image, zBuffer, vertex[i], radius, perforation);
				
				if(gap)
				{
					System.out.println((i+1)+". edge...");
					edge[i].setColor(edge[i].location(1).transform(transformation).stcolor());
					eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, edge[i], 2);
				}
				
			}
			
			for(int j=0;j<y;j++)
			{
				System.out.println("Drawing "+(j+1)+". side...");
				center[j]=new Spherpoint(0,Math.PI/2*Math.sin(2*Math.PI*j/y),Math.PI/2*Math.cos(2*Math.PI*j/y));
				Color colo=center[j].transform(transformation).standardcolor();
				
				for(int i=0;i<2;i++)
				{
					tocenter[i]=edgemiddle[i].geodesic(center[j]);
					
					for(double l=1.0/denseness; l<1.0/filling;l=l+1.0/denseness)
					{
						line=vertex[0].geodesic(tocenter[i].location(l));
						line.setColor(colo);
						eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 1);
						
						line=vertex[1].geodesic(tocenter[i].location(l));
						line.setColor(colo);
						eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 1);
						
						/*if(gap==false && l%4==0)
						{
							line=vertex[0].geodesic(tocenter[i].location(l/10000.0));
							line.setColor(color);
							eye.drawLine(image, zBuffer, line, 1);
							
							line=vertex[1].geodesic(tocenter[i].location(l/10000.0));
							line.setColor(color);
							eye.drawLine(image, zBuffer, line, 1);
						}*/
						
						if(empty==false)
						{
							line=vertex[0].geodesic(tocenter[i].location(1-l));
							line.setColor(colo);
							eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 1);
							
							line=vertex[1].geodesic(tocenter[i].location(1-l));
							line.setColor(colo);
							eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 1);
						}
					}
				}
				
			}
		
		}
		
		if (x>2)
		{
			Spherpoint[] vertex=new Spherpoint[x],
					center=new Spherpoint[y];
			Spherline[] edge=new Spherline[x],
					tocenter=new Spherline[x];
			
			for(int i=0;i<x;i++)
			{
				System.out.println("Drawing "+(i+1)+". vertex");
				vertex[i]=new Spherpoint(2*Math.PI*i/x,0,0);
				/*vertex[i].setColor(vertex[i].transform(transformation).standardcolor());
				eye.drawSphere(image, zBuffer, vertex[i], radius, perforation);*/
			}
			int k;
			if(gap)
			{
				
				for(int i=0;i<x;i++)
				{
					System.out.println("Drawing "+(i+1)+". edge");
					k=i-1;
					if (k<0){k=x-1;}
				
					edge[i]=vertex[k].geodesic(vertex[i]);
					edge[i].setColor(edge[i].location(0.5).stcolor());
					eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, edge[i], 1);
				
				}
			}
			
			for (int j=0;j<y;j++)
			{
				System.out.println("Drawing "+(j+1)+". side");
				center[j]=new Spherpoint(0,Math.PI/2*Math.sin(2*Math.PI*j/y),Math.PI/2*Math.cos(2*Math.PI*j/y));
				color=center[j].transform(transformation).standardcolor();
				for (int i=0;i<x;i++)
				{
					tocenter[i]=vertex[i].geodesic(center[j]);
				}
				
				for (int i=0;i<x;i++)
				{
					k=i-1;
					if (k<0){k=x-1;}
					for(double l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
					{
					
						line=tocenter[k].location(l).geodesic(tocenter[i].location(l));
						line.setColor(color);
						eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 1);
						
						/*if (gap==false && l%4==0)
						{
							line=tocenter[k].location(l/10000.0).geodesic(tocenter[i].location(l/10000.0));
							line.setColor(color);
							eye.drawLine(image, zBuffer, line, 1);
						}*/
						
						if (empty==false)
						{
							line=tocenter[k].location(1-l).geodesic(tocenter[i].location(1-l));
							line.setColor(colo);
							eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 1);
						}
					}
				}
			}
			
		}
		
		Spherpoint focus=eye.viewPoint.location(1);
		return new String ("Schläfli("+x+","+2+","+y+") from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")");

	}
	
	//******************************************************
	// Draws Schäffel2x2 with a color transformation
	//******************************************************
	public static String doubleSchläfli2x2 (Observer eye,BufferedImage image1, BufferedImage image2,double[][] zBuffer1, double[][]zBuffer2,int x, double[][] transformation, int denseness)
	{
		Spherpoint point;
		Spherline line;
		Spherpoint northpole=Spherpoint.eqnorthpole;
		northpole.setColor(northpole.transform(transformation).standardcolor().getRGB());
		Spherpoint southpole=Spherpoint.eqsouthpole;
		southpole.setColor(southpole.transform(transformation).standardcolor().getRGB());
		/*System.out.println("Drawing first vertex...");
		eye.drawSphere(image, zBuffer, northpole,radius,perforation);
		System.out.println("Drawing second vertex...");
		eye.drawSphere(image, zBuffer, southpole,radius,perforation);*/
		
		
		Spherpoint[] eqpoint=new Spherpoint[x];
		for(int i=0; i<x; i++)
		{
			if (gap)
			{
			System.out.println("Drawing "+(i+1)+". edge...");
			eqpoint[i]=new Spherpoint(2*Math.PI*i/x,0,0);
			line=northpole.geodesic(eqpoint[i]);
			line.setColor(eqpoint[i].transform(transformation).standardcolor());
			eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 2);//or 2?!
			}
			
			System.out.println("Drawing "+(i+1)+". side...");
			Spherpoint center=new Spherpoint(2*Math.PI*(i+0.5)/x,0,0);
			center.setColor(center.transform(transformation).standardcolor().getRGB());
			//eye.fullCircle(image, zBuffer, center, new E4=E4.e4.transform(transformation), Math.PI/x);
			
			for (double l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
			{
				point=new Spherpoint(2*Math.PI*(i+0.5*l)/x,0,0);
				line=northpole.geodesic(point);
				line.setColor(new Color(center.getColor()));
				eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 2); //or 2?!
				
				point=new Spherpoint(2*Math.PI*(i+1-0.5*l)/x,0,0);
				line=northpole.geodesic(point);
				line.setColor(new Color(center.getColor()));
				eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 2); //or 2?!
				
				/*if(gap==false && l%5==0)
				{
					point=new Spherpoint(2*Math.PI*(i+l/20000.0)/x,0,0);
					line=northpole.geodesic(point);
					line.setColor(center.getColor());
					eye.drawLine(image, zBuffer, line, 2); //or 2?!
					
					point=new Spherpoint(2*Math.PI*(i+1-l/20000.0)/x,0,0);
					line=northpole.geodesic(point);
					line.setColor(center.getColor());
					eye.drawLine(image, zBuffer, line, 2); //or 2?!
				}*/
				
				if(empty==false)
				{
				point=new Spherpoint(2*Math.PI*(i+0.5+0.5*l)/x,0,0);
				line=northpole.geodesic(point);
				line.setColor(toDouble(center.getColor()));
				eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 2); //or 2?!
				
				point=new Spherpoint(2*Math.PI*(i+0.5-0.5*l)/x,0,0);
				line=northpole.geodesic(point);
				line.setColor(toDouble(center.getColor()));
				eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 2); //or 2?!
				}
			}
				
		}
		
		
		
		Spherpoint focus=eye.viewPoint.location(1);
		return new String("Schläfli(2,"+x+",2)from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")");
	}
	//******************************************************
		// Draws Schäffel2x2 with a color transformation
		//******************************************************
		public static String schäffel2x2 (Observer eye,BufferedImage image,double[][] zBuffer,int x, double[][] transformation, int denseness)
		{
			Spherpoint point;
			Spherline line;
			Spherpoint northpole=Spherpoint.eqnorthpole;
			northpole.setColor(northpole.transform(transformation).standardcolor().getRGB());
			Spherpoint southpole=Spherpoint.eqsouthpole;
			southpole.setColor(southpole.transform(transformation).standardcolor().getRGB());
			/*System.out.println("Drawing first vertex...");
			eye.drawSphere(image, zBuffer, northpole,radius,perforation);
			System.out.println("Drawing second vertex...");
			eye.drawSphere(image, zBuffer, southpole,radius,perforation);*/
			
			
			Spherpoint[] eqpoint=new Spherpoint[x];
			for(int i=0; i<x; i++)
			{
				if (gap)
				{
				System.out.println("Drawing "+(i+1)+". edge...");
				eqpoint[i]=new Spherpoint(2*Math.PI*i/x,0,0);
				line=northpole.geodesic(eqpoint[i]);
				line.setColor(eqpoint[i].transform(transformation).stcolor());
				eye.drawLine(image, zBuffer, line, 2);//or 2?!
				}
				
				System.out.println("Drawing "+(i+1)+". side...");
				Spherpoint center=new Spherpoint(2*Math.PI*(i+0.5)/x,0,0);
				center.setColor(center.transform(transformation).standardcolor().getRGB());
				//eye.fullCircle(image, zBuffer, center, new E4=E4.e4.transform(transformation), Math.PI/x);
				
				for (double l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					point=new Spherpoint(2*Math.PI*(i+0.5*l)/x,0,0);
					line=northpole.geodesic(point);
					line.setColor(toDouble(center.getColor()));
					eye.drawLine(image, zBuffer, line, 2); //or 2?!
					
					point=new Spherpoint(2*Math.PI*(i+1-0.5*l)/x,0,0);
					line=northpole.geodesic(point);
					line.setColor(toDouble(center.getColor()));
					eye.drawLine(image, zBuffer, line, 2); //or 2?!
					
					/*if(gap==false && l%5==0)
					{
						point=new Spherpoint(2*Math.PI*(i+l/20000.0)/x,0,0);
						line=northpole.geodesic(point);
						line.setColor(center.getColor());
						eye.drawLine(image, zBuffer, line, 2); //or 2?!
						
						point=new Spherpoint(2*Math.PI*(i+1-l/20000.0)/x,0,0);
						line=northpole.geodesic(point);
						line.setColor(center.getColor());
						eye.drawLine(image, zBuffer, line, 2); //or 2?!
					}*/
					
					if(empty==false)
					{
					point=new Spherpoint(2*Math.PI*(i+0.5+0.5*l)/x,0,0);
					line=northpole.geodesic(point);
					line.setColor(toDouble(center.getColor()));
					eye.drawLine(image, zBuffer, line, 2); //or 2?!
					
					point=new Spherpoint(2*Math.PI*(i+0.5-0.5*l)/x,0,0);
					line=northpole.geodesic(point);
					line.setColor(toDouble(center.getColor()));
					eye.drawLine(image, zBuffer, line, 2); //or 2?!
					}
				}
					
			}
			
			
			
			Spherpoint focus=eye.viewPoint.location(1);
			return new String("Schläfli(2,"+x+",2)from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
					Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
					Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
					Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
					Math.round(Math.toDegrees(focus.getTheta()))+")");
		}
	
		public static Cellcomplex[] icosidodec()
		{
			Spherpoint[] vertex=Spherpoint.dodecahedronEdge,
					center3=Spherpoint.dodecahedron,
					center5=Spherpoint.icosahedron;
			
			int[][]sidepoint3=icosahedronSideedge,
					sidepoint5=dodecahedronSideedge,
					edgepoint=new int[60][2];
			for(int i=0;i<30;i++)
			{
				edgepoint[i]=dodecahedronEdgepoint[i];
				edgepoint[i+30]=icosahedronEdgepoint[i];
			}
			return new Cellcomplex[] {new Cellcomplex(vertex,center3,edgepoint,sidepoint3),
									new Cellcomplex(vertex,center5,edgepoint,sidepoint5)};
			
		}
		public static Cellcomplex[] octahexa()
		{
			Spherpoint[] vertex=Spherpoint.hexahedronEdge,
					center4=Spherpoint.octahedron,
					center3=Spherpoint.hexahedron;
			
			int[][]sidepoint4=hexahedronSideedge,
					sidepoint3=octahedronSideedge,
					edgepoint=new int[24][2];
			for(int i=0;i<6;i++)
			{
				for(int j=0;j<4;j++)
				{
					edgepoint[4*i+j][0]=hexahedronSideedge[i][j];
					edgepoint[4*i+j][1]=hexahedronSideedge[i][(j+1)%4];
					
				}
				
			}
			return new Cellcomplex[] {new Cellcomplex(vertex,center3,edgepoint,sidepoint3),
					new Cellcomplex(vertex,center4,edgepoint,sidepoint4)};
		}
		//Could be used as template for any duals of medium truncations??
		public static Cellcomplex dualTesseractihexadecachoron()
	 	{
	 		Spherpoint[] vertex=new  Spherpoint[16+8];
	 		Cellcomplex tes=schäffel433(),hex=schäffel334();
	 		int[][]edgepoint=new int[32+24+64][2], sidepoint=new int[96+96][3];//edgepoint:ToDo!
	 		for(int i=0;i<16;i++)
	 		{
	 			vertex[i]=tes.vertex[i];
	 		}
	 		for(int i=0;i<8;i++)
	 		{
	 			vertex[i+16]=hex.vertex[i];
	 		}
	 		int s=32, t=tes.sidepoint.length;
	 		for(int j=0;j<3;j++)
	 		{
	 			for(int i=0;i<s;i++)
	 			{
	 				sidepoint[3*i+j][0]=tes.edgepoint[i][0];
	 				sidepoint[3*i+j][1]=tes.edgepoint[i][1];
	 				sidepoint[3*i+j][2]=hex.sidepoint[i][j]+16;
	 			}
	 			
	 		}
	 		for(int j=0;j<4;j++)
	 		{
	 			for(int i=0;i<t;i++)
	 			{
	 				sidepoint[3*s+4*i+j][0]=hex.edgepoint[i][0]+16;
	 				sidepoint[3*s+4*i+j][1]=hex.edgepoint[i][1]+16;
	 				sidepoint[3*s+4*i+j][2]=tes.sidepoint[i][j];
	 			}
	 		}
	 		return new Cellcomplex(vertex,Cellcomplex.centers(vertex, sidepoint),edgepoint,sidepoint);
	 	}
		
	//******************************************************
	// Draws Schäffel332 with a color transformation
	//*****************************************************
	public static Cellcomplex schäffel332 ()
	{
		Spherpoint[] vertex=Spherpoint.tetrahedron;
		Spherpoint[] center=new Spherpoint[4];
		for (int i=0;i<4;i++)
		{
			center[i]=Spherpoint.tetrahedron[i].getAntipodal();
		}
		int[][] edgepoint=new int[6][2], sidepoint=new int[4][3];
		int edge =0;
		int side=3;
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<i;j++)
			{
				edgepoint[edge][0]=i;
				edgepoint[edge][1]=j;
				edge++;
				for(int k=0;k<j;k++)
				{
					sidepoint[side][0]=i;
					sidepoint[side][1]=j;
					sidepoint[side][2]=k;
					side=side-1;
				}
			}
		}
			
		
			
		
		/*int edge=0;
		for(int i=0;i<4;i++)
		{
			System.out.println("Drawing "+(i+1)+". vertex");
			point[i].setColor(point[i].transform(transformation).standardcolor());
			//double distance=eye.drawSphere(image, zBuffer, point[i], radius, perforation);
			//System.out.println("Distance: "+distance);
			
			if(gap){
			for(int j=0;j<i;j++)
			{
				edge++;
				System.out.println("Drawing "+edge+". edge");
				Spherline line=point[i].geodesic(point[j]);
				line.setColor(line.location(0.5).transform(transformation).standardcolor());
				eye.drawLine(image, zBuffer, line, 1);
			}}
				
			System.out.println("Drawing "+(i+1)+". side");
			center[i]=point[i].getAntipodal();
			color[i]=center[i].transform(transformation).standardcolor();
			center[i].setColor(color[i]);
			
				
			for(int j=0;j<4;j++)
			{
				if(j!=i)
				{
					//vector[j][i]=point[j].direction(center[i]);
					tocenter[i][j]=point[j].geodesic(center[i]);
						
					for(int m=0;m<j;m++)
					{
						if(i!=m)
						{
							for (int l=filling;l<100;l++)
							{
								
								Spherline line=tocenter[i][j].location(1.0/l).geodesic(tocenter[i][m].location(1.0/l));
								line.setColor(color[i]);
								eye.drawLine(image, zBuffer, line , 1);
								
								if(empty==false)
								{
								line=tocenter[i][j].location(1-1.0/l).geodesic(tocenter[i][m].location(1-1.0/l));
								line.setColor(color[i]);
								eye.drawLine(image, zBuffer, line , 1);
								}
								
								if(gap==false && l%4==0)
								{
									line=tocenter[i][j].location(l/10000.0).geodesic(tocenter[i][m].location(l/10000.0));
									line.setColor(color[i]);
									eye.drawLine(image, zBuffer, line , 1);
								}								
							
									
							}
							
						}
					}
				}
			}
		}*/
		Cellcomplex schläfli332=new Cellcomplex(vertex,center,edgepoint,sidepoint);
		return schläfli332;
	}
	
	//****************************************************************************************************************
	// Draws Schäffel233 with a color transformation
	//****************************************************************************************************************
	
	public static String schäffel233(Observer eye,BufferedImage image,double[][] zBuffer, double[][] transformation, int denseness)
	{
		Spherpoint[] vertex=new Spherpoint[]{Spherpoint.northpole,Spherpoint.southpole};
		
	/*	for (int i=0;i<2;i++)
		{
			System.out.println("Drawing "+(i+1)+". vertex");
			vertex[i].setColor(vertex[i].transform(transformation).standardcolor());
			eye.drawSphere(image, zBuffer, vertex[i], radius, perforation);
		}*/
		Spherline line, sideline;
		Spherpoint[] tetrahedron=Spherpoint.tetrahedron,
				center=new Spherpoint[6];
		Spherline[] edges=new Spherline[4];
		int k=0;
		for (int j=0;j<4;j++)
		{
			if(gap)
			{
			System.out.println("Drawing "+(j+1)+". edge");
			edges[j]=vertex[1].geodesic(tetrahedron[j]);
			edges[j].setColor(tetrahedron[j].transform(transformation).standardcolor());
			eye.drawLine(image, zBuffer, edges[j], 2);
			}
		
			for(int i=0;i<j;i++)
			{
				k++;
				System.out.println("Drawing "+(k)+". side");
				line=tetrahedron[j].geodesic(tetrahedron[i]);
				center[k-1]=line.location(0.5);
				center[k-1].setColor(center[k-1].transform(transformation).standardcolor().getRGB());
				for (double l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					Color c=new Color(center[k-1].getColor());
					sideline=vertex[1].geodesic(line.location(l));
					sideline.setColor(c);
					eye.drawLine(image, zBuffer, sideline, 2);
					
					sideline=vertex[1].geodesic(line.location(1-l));
					sideline.setColor(c);
					eye.drawLine(image, zBuffer, sideline, 2);
					
					if(empty==false)
					{
						sideline=vertex[1].geodesic(line.location(0.5+l));
						sideline.setColor(c);
						eye.drawLine(image, zBuffer, sideline, 2);
					
						sideline=vertex[1].geodesic(line.location(0.5-l));
						sideline.setColor(c);
						eye.drawLine(image, zBuffer, sideline, 2);
					}
					
					if (gap==false && l%4==0)
					{
						sideline=vertex[1].geodesic(line.location(l/20000.0));
						sideline.setColor(c);
						eye.drawLine(image, zBuffer, sideline, 2);
						
						sideline=vertex[1].geodesic(line.location(1-l/20000.0));
						sideline.setColor(c);
						eye.drawLine(image, zBuffer, sideline, 2);
					}
					
				}
			}
				
		}
		
			
		
		Spherpoint focus=eye.viewPoint.location(1);
		return new String("Schläfli(2,3,3)from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")");
	}
	
	//****************************************************************
	// Draws a Pentachoron with a color transformation cellcomplex
	//****************************************************************
	public static Cellcomplex pentachoron()
	{
		double theta0=0.252680255,theta1=0.659057329, theta2=-0.420534335;
	
	

		int[][]sidepoint=new int[][]{{1,2,3},{2,3,0},{3,0,1},{0,1,2},{0,1,4},{0,2,4},{1,2,4},{0,3,4},{1,3,4},{2,3,4}},
				edgepoint=new int[10][2];
		Spherpoint[] vertex=new Spherpoint[5], center=new Spherpoint[10];
		vertex[4]=Spherpoint.southpole;
		int k=0;
		for(int i=0;i<4;i++)
		{
			vertex[i]=Spherpoint.tetrahedron[i];
			
			center[i]=vertex[i].getAntipodal();
			center[i].setTheta(theta1);
			
			vertex[i].setTheta(theta0);
			
			for(int j=0;j<i;j++)
			{
				edgepoint[k][0]=i;
				edgepoint[k][1]=j;
				k++;
			}
			edgepoint[6+i]=new int[] {i,4};
		}
		
		
		
		k=0;
		for(int i=0;i<4;i++)
		{
			for (int j=0;j<i; j++)
			{
					center[4+k]=vertex[i].geodesic(vertex[j]).location(0.5);
					center[4+k].setTheta(theta2);
					k++;
			}
		}
		
		/*
		for(int i=0;i<10;i++)
		{	
			if(i<5)
			{
				System.out.println("Drawing "+(i+1)+". vertex");
				vertex[i].setColor(vertex[i].transform(transformation).standardcolor());
				eye.drawSphere(image, zBuffer, vertex[i], radius, perforation);
			}
		if(gap){
			System.out.println("Drawing "+(i+1)+". edge");
			edge[i].setColor(edge[i].location(0.5).transform(transformation).standardcolor());
			eye.drawLine(image, zBuffer, edge[i], 1);}
			
			
			System.out.println("Drawing "+(i+1)+". side");
			color=center[i].transform(transformation).standardcolor();
			for(int j=0;j<3;j++)
			{
				tocenter[j]=vertex[sidepoint[i][j]].geodesic(center[i]);
			}
			for(int j=0;j<3;j++)
			{
				/*if(Math.abs(vertex[sidepoint[i][j]].distance(center[i])-(theta2+Math.PI/2))>0.01)
				{
					System.out.println("vertex "+sidepoint[i][j]+" to center "+i+" distance:"+vertex[sidepoint[i][j]].distance(center[i]));
				}
				k=j-1;
				if(k<0)
				{
					k=2;
				}
				for (int l=filling;l<100;l++)
				{
					line=tocenter[j].location(1.0/l).geodesic(tocenter[k].location(1.0/l));
					line.setColor(color);
					eye.drawLine(image, zBuffer, line, 1);
					
					if(empty==false)
					{
						line=tocenter[j].location(1-1.0/l).geodesic(tocenter[k].location(1-1.0/l));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 1);
					}
					
					
					if(gap==false && l%4==0)
					{
						line=tocenter[j].location(l/10000.0).geodesic(tocenter[k].location(l/10000.0));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line , 1);
					}
				}
			}
		}
		
		Spherpoint focus=eye.viewPoint.location(1);
		return new String("Schläfli(3,3,3)from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")");*/
		
		return new Cellcomplex(vertex, center, edgepoint, sidepoint);
	}
	
	//****************************************************************
	// Draws Schäffel432 with a color transformation cellcomplex
	//******************************************************************
	
	public static Cellcomplex schäffel432()
	{
		Spherpoint[] vertex=Spherpoint.hexahedron.clone();
		int[][] sidepoint=hexahedronSidepoint;
		int[][] edgepoint=hexahedronEdgepoint;
		Spherpoint[] center=Spherpoint.octahedron.clone();
	
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
		
	}
	
	//*****************************************************************************
	// Draws Schäffel234 with a color transformation
	//*************************************************************************
	public static String schäffel234(Observer eye,BufferedImage image,double[][] zBuffer, double[][] transformation, int denseness)
	{
	
		Spherpoint[] vertex=new Spherpoint[]{Spherpoint.northpole,Spherpoint.southpole},
				center=Spherpoint.hexahedronEdge,
				octahedron=Spherpoint.octahedron;
		Spherline[] edge=new Spherline[6];
		Spherline line, tocenter;
		Color color;
		int[][] sideline=octahedronEdgepoint;
		
		/*for(int i=0;i<2;i++)
		{
			System.out.println("Drawing "+(i+1)+". vertex");
			vertex[i].setColor(vertex[i].transform(transformation).standardcolor());
			eye.drawSphere(image, zBuffer, vertex[i], radius, perforation);
		}*/
		if (gap){
		for (int i=0;i<6;i++)
		{
			System.out.println("Drawing "+(i+1)+". edge");
			edge[i]=vertex[0].geodesic(octahedron[i]);
			edge[i].setColor(octahedron[i].transform(transformation).standardcolor());
			eye.drawLine(image, zBuffer, edge[i], 2);
		}}
		
		for (int i=0;i<12;i++)
		{
			System.out.println("Drawing "+(i+1)+". side");
			Color c=center[i].transform(transformation).standardcolor();
			for (int j=0;j<2;j++)
			{
				tocenter=octahedron[sideline[i][j]].geodesic(center[i]);
				for (double l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					line=vertex[0].geodesic(tocenter.location(1.0/l));
					line.setColor(c);
					eye.drawLine(image, zBuffer, line, 2);
					
					/*if (gap==false && l%4==0)
					{
						line=vertex[0].geodesic(tocenter.location(l/10000.0));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 2);
					}*/
					
					if (empty==false)
					{
						line=vertex[0].geodesic(tocenter.location(1-l));
						line.setColor(c);
						eye.drawLine(image, zBuffer, line, 2);
					}
				}
				

			}
		}
		
		
		Spherpoint focus=eye.viewPoint.location(1);
		return new String("Schläfli(2,3,4)from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")");
		
	}
	
	//*****************************************************************
	// Draws Schäffel342 with a color transformation Cellcomplex
	//*****************************************************************
	public static Cellcomplex schäffel342()
	{
	
		
		Spherpoint[] vertex=Spherpoint.octahedron,
				center=Spherpoint.hexahedron;
		int[][] sidepoint=octahedronSidepoint,
				edgepoint=octahedronEdgepoint;
		/*Spherline[] tocenter=new Spherline[3];
		Spherline line;
		Color color;
		
		for(int i=0;i<6;i++)
		{
			System.out.println("Drawing "+(i+1)+". vertex");
			vertex[i].setColor(vertex[i].transform(transformation).standardcolor());
			eye.drawSphere(image, zBuffer, vertex[i], radius, perforation);
		}
		
		if (gap)
		{
			Spherline[] edge=new Spherline[12];
			for (int i=0;i<4;i++)
			{
				edgepoint[i][0]=0;
				edgepoint[i][1]=i+1;
				
				int j=i;
				if(j==0)
				{
					j=4;
				}
				edgepoint[i+4][0]=i+1;
				edgepoint[i+4][1]=j;
						
				edgepoint[i+8][0]=5;
				edgepoint[i+8][1]=i+1;
			}
			
			/*for(int i=0;i<12;i++)
			{
				edge[i].setColor(edge[i].location(0.5).transform(transformation).standardcolor());
				eye.drawLine(image, zBuffer, edge[i], 1);
			}
		}
		
		int k;
		
		for(int i=0;i<8;i++)
		{
			color=center[i].transform(transformation).standardcolor();
			
			for (int j=0;j<3;j++)
			{
				tocenter[j]=vertex[sidepoint[i][j]].geodesic(center[i]);
			}
			
			for (int j=0;j<3;j++)
			{
				k=j-1;
				if (k<0){k=2;}
				
				for(int l=filling;l<100;l++)
				{
					line=tocenter[j].location(1.0/l).geodesic(tocenter[k].location(1.0/l));
					line.setColor(color);
					eye.drawLine(image, zBuffer, line, 1);
					
					if(empty==false)
					{
						line=tocenter[j].location(1-1.0/l).geodesic(tocenter[k].location(1-1.0/l));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 1);
					}

					
					if(gap==false)
					{
						line=tocenter[j].location(l/10000.0).geodesic(tocenter[k].location(l/10000.0));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 1);
					}
				
					
				}
			}
			
		}
		
		Spherpoint focus=eye.viewPoint.location(1);
		return new String("Schläfli(4,3,2)from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")");*/
			return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	
	//*****************************************************************
	// Draws Schäffel243 with a color transformation
	//*****************************************************************
	public static String schäffel243(Observer eye,BufferedImage image,double[][] zBuffer, double[][] transformation,double denseness)
	{
		Spherpoint[] vertex=new Spherpoint[]{Spherpoint.northpole,Spherpoint.southpole},
				center=Spherpoint.hexahedronEdge,
				hexahedron=Spherpoint.hexahedron;
		int[][] sidepoint=new int[][]{{0,3},{0,1},{1,2},{2,3},{4,7},{4,5},{5,6},{6,7},{0,4},{1,5},{2,6},{3,7}};
		Spherline[] tocenter=new Spherline[2];
		Spherline line;
		Color color;
		System.out.println("Drawing vertices");
		for(int i=0;i<2;i++)
		{
			vertex[i].setColor(vertex[i].transform(transformation).standardcolor().getRGB());
			eye.drawSphere(image, zBuffer, vertex[i], radius, perforation);
		}
		if(gap)
		{
			System.out.println("Drawing edges");
			for (int i=0;i<8;i++)
			{
				line=vertex[0].geodesic(hexahedron[i]);
				line.setColor(hexahedron[i].transform(transformation).standardcolor());
				eye.drawLine(image, zBuffer, line, 2);
			}
		}
		for(int j=0;j<12;j++)
		{
			System.out.println("Drawing "+(j+1)+". side");
			Color c=center[j].transform(transformation).standardcolor();
			for (int i=0;i<2;i++)
			{
				
				tocenter[i]=hexahedron[sidepoint[j][i]].geodesic(center[j]);
				
				for(double l=1.0/denseness;l<1.0/filling;l+=1.0/denseness)
				{
					line=vertex[0].geodesic(tocenter[i].location(l));
					line.setColor(c);
					eye.drawLine(image, zBuffer, line, 2);
					
					/*if(gap==false && l%4==0)
					{
						line=vertex[0].geodesic(tocenter[i].location(l/10000.0));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 2);
					}*/
					
					if(empty==false)
					{
						line=vertex[0].geodesic(tocenter[i].location(1-l));
						line.setColor(c);
						eye.drawLine(image, zBuffer, line, 2);
					}
				}
			}
			
		}
		
		Spherpoint focus=eye.viewPoint.location(1);
		return new String("Schläfli(2,4,3)from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")");
	}
	
	//*****************************************************************
	// Draws Schäffel334 with a color transformation Cellcomplex
	//*****************************************************************
	public static Cellcomplex tiling334()
	{	
		double theta0=0.615479709;
		Spherpoint[] vertex=new Spherpoint[8],
			center=new Spherpoint[32];
		int[][] sidepoint=new int[32][3]/*{{0,2,3},{0,2,4},{0,2,5},{0,2,6},{0,7,3},{0,7,4},{0,7,5},{0,7,6},{0,3,4},{0,4,5},
				{0,5,6},{0,6,3},{1,2,3},{1,2,4},{1,2,5},{1,2,6},{1,7,3},{1,7,4},{1,7,5},{1,7,6},{1,3,4},{1,4,5},
				{1,5,6},{1,6,3},{2,3,4},{2,4,5},{2,5,6},{2,6,3},{7,3,4},{7,4,5},{7,5,6},{7,6,3}}*/,
				edgepoint=new int[28][2];//ToDo:It should only have 24 edges?*
		int i,j,k;
		vertex[0]=Spherpoint.northpole;
		vertex[1]=Spherpoint.southpole;
		
		for(i=2;i<8;i++)
		{
			vertex[i]=Spherpoint.octahedron[i-2];
		}
		
		
		for(j=0;j<2;j++)
		{
			for(i=0;i<6;i++)
			{
				
					edgepoint[i+18*j][0]=j;
					edgepoint[i+18*j][1]=i+2;
				
			}
			for(i=0;i<12;i++)
			{
				edgepoint[i+6][j]=octahedronEdgepoint[i][j]+2;
			}
		}
		for(j=0;j<12;j++)
		{
			center[j]=Spherpoint.hexahedronEdge[j].copy();
			center[j].setTheta(theta0);
			sidepoint[j][0]=0;
			sidepoint[j][1]=octahedronEdgepoint[j][0]+2;
			sidepoint[j][2]=octahedronEdgepoint[j][1]+2;
			
			center[j+12]=Spherpoint.hexahedronEdge[j].copy();
			center[j+12].setTheta(-theta0);
			sidepoint[j+12][0]=1;
			sidepoint[j+12][1]=octahedronEdgepoint[j][0]+2;
			sidepoint[j+12][2]=octahedronEdgepoint[j][1]+2;
		}
		for(j=24;j<32;j++)
		{
			center[j]=Spherpoint.hexahedron[j-24];
			for(i=0;i<3;i++)
			{
				sidepoint[j][i]=octahedronSidepoint[j-24][i]+2;
			}
		}
		/*for(j=0;j<32;j++)
		{
			System.out.println("Drawing "+(j+1)+". side");
			for(i=0;i<j;i++)
			{
				System.out.println("Distance to "+(i+1)+". side:"+center[i].distance(center[j]));
			}
			color=center[j].transform(transformation).standardcolor();
			
			for(i=0;i<3;i++)
			{
				tocenter[i]=vertex[sidepoint[j][i]].geodesic(center[j]);
			}
			for(i=0;i<3;i++)
			{
				k=i-1; if(k<0){k=2;}
				
				for (l=filling;l<100;l++)
				{
					line=tocenter[k].location(1.0/l).geodesic(tocenter[i].location(1.0/l));
					line.setColor(color);
					eye.drawLine(image, zBuffer, line, 1);
					
					if(gap==false && l%4==0)
					{
						line=tocenter[k].location(l/10000.0).geodesic(tocenter[i].location(l/10000.0));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 1);
					}
					
					if (empty==false)
					{
						line=tocenter[k].location(1-1.0/l).geodesic(tocenter[i].location(1-1.0/l));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 1);
					}
				}
			}
		}
		
		
		
		Spherpoint focus=eye.viewPoint.location(1);
		return new String("Schläfli(3,3,4)from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")");*/
		
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	
	//*****************************************************************
	// Draws Schäffel343 with a color transformation Cellcomplex
	//*****************************************************************
	public static Cellcomplex tiling343()
	{	
		double theta0=0.523598776, theta1=0.955316619, rh=0.661470431; //rh=ratio of circumradius to height of triangle
		Spherpoint[] vertex=new Spherpoint[24],
				center=new Spherpoint[96];
		int[][] sidepoint=new int[96][3];
		int [][] edgepoint=new int[96][2];
		int i,j,k;
		/*Spherline line;
		Spherline[] tocenter=new Spherline[3];
		Color color;
		
		System.out.println("Drawing vertices...");*/
		
		vertex[0]=Spherpoint.northpole.copy();
		vertex[23]=Spherpoint.southpole.copy();
		
		for (i=0;i<8;i++)
		{
			vertex[i+1]=Spherpoint.hexahedron[i].copy();
			vertex[i+1].setTheta(theta0);
			
			vertex[i+15]=Spherpoint.hexahedron[i].copy();
			vertex[i+15].setTheta(theta0*(-1));
		}
		
		for (i=0;i<6;i++)
		{
			vertex[i+9]=Spherpoint.octahedron[i].copy();
		}
		
		/*for (i=0;i<24;i++)
		{
			vertex[i].setColor(vertex[i].transform(transformation).standardcolor());
			eye.drawSphere(image, zBuffer, vertex[i], radius, perforation);
		}
		
		if(gap)
		{
			System.out.println("Drawing edges...");
			*/
	
		for(i=0;i<8;i++)
			{
				edgepoint[i][0]=0;
				edgepoint[i][1]=i+1;
				
				edgepoint[i+8][0]=i+1;
				edgepoint[i+8][1]=i+15;
				
				edgepoint[i+16][0]=i+15;
				edgepoint[i+16][1]=23;
				
				
				for(j=0;j<3;j++)
				{
					edgepoint[3*i+24+j][0]=i+1;
					edgepoint[3*i+24+j][1]=octahedronSidepoint[i][j]+9;
					
					edgepoint[3*i+48+j][0]=i+15;
					edgepoint[3*i+48+j][1]=octahedronSidepoint[i][j]+9;
				}
			}
			for(i=0;i<12;i++)
			{
				edgepoint[i+72][0]=hexahedronEdgepoint[i][0]+1;
				edgepoint[i+72][1]=hexahedronEdgepoint[i][1]+1;
				
				edgepoint[i+84][0]=hexahedronEdgepoint[i][0]+15;
				edgepoint[i+84][1]=hexahedronEdgepoint[i][1]+15;
				
			}
		//}
		
		for (i=0;i<12;i++)
		{
			center[i]=Spherpoint.hexahedronEdge[i].copy();
			center[i].setTheta(theta1);
			sidepoint[i][0]=0;
			sidepoint[i][1]=hexahedronEdgepoint[i][0]+1;
			sidepoint[i][2]=hexahedronEdgepoint[i][1]+1;
			
			center[i+84]=Spherpoint.hexahedronEdge[i].copy();
			center[i+84].setTheta(theta1*(-1));
			sidepoint[i+84][0]=23;
			sidepoint[i+84][1]=hexahedronEdgepoint[i][0]+15;
			sidepoint[i+84][2]=hexahedronEdgepoint[i][1]+15;
		}
		for(i=0;i<6;i++)
		{
			for(j=0;j<4;j++)
			{
				k=j+1; if(k==4){k=0;}
				
				sidepoint[12+4*i+j][0]=hexahedronSidepoint[i][j]+1;
				sidepoint[12+4*i+j][1]=hexahedronSidepoint[i][k]+1;
				sidepoint[12+4*i+j][2]=i+9;
				center[12+4*i+j]=vertex[sidepoint[12+4*i+j][0]].geodesic(vertex[sidepoint[12+4*i+j][1]]).
						location(0.5).geodesic(vertex[sidepoint[12+4*i+j][2]]).location(1-rh);
				
				sidepoint[36+4*i+j][0]=hexahedronSidepoint[i][j]+1;
				sidepoint[36+4*i+j][1]=hexahedronSidepoint[i][j]+15;
				sidepoint[36+4*i+j][2]=i+9;
				center[36+4*i+j]=vertex[sidepoint[36+4*i+j][0]].geodesic(vertex[sidepoint[36+4*i+j][1]]).
						location(0.5).geodesic(vertex[sidepoint[36+4*i+j][2]]).location(1-rh);
				
				sidepoint[60+4*i+j][0]=hexahedronSidepoint[i][j]+15;
				sidepoint[60+4*i+j][1]=hexahedronSidepoint[i][k]+15;
				sidepoint[60+4*i+j][2]=i+9;
				center[60+4*i+j]=vertex[sidepoint[60+4*i+j][0]].geodesic(vertex[sidepoint[60+4*i+j][1]]).
						location(0.5).geodesic(vertex[sidepoint[60+4*i+j][2]]).location(1-rh);			
			}
		}
		
		/*for(i=0;i<96;i++)
		{
			System.out.println("Drawing "+(i+1)+". side...");
			for(j=0;j<i;j++)
			{
				System.out.println("Distance to "+(j+1)+". side: "+center[i].distance(center[j]));
			}
			color=center[i].transform(transformation).standardcolor();
			for(j=0;j<3;j++)
			{
				tocenter[j]=vertex[sidepoint[i][j]].geodesic(center[i]);
				
				for(k=0;k<j;k++)
				{
					for (l=filling;l<100;l++)
					{
						line=tocenter[k].location(1.0/l).geodesic(tocenter[j].location(1.0/l));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 1);
						
						if(gap==false)
						{
							line=tocenter[k].location(l/10000.0).geodesic(tocenter[j].location(l/10000.0));
							line.setColor(color);
							eye.drawLine(image, zBuffer, line, 1);							
						}
						
						if(empty==false)
						{
							line=tocenter[k].location(1-1.0/l).geodesic(tocenter[j].location(1-1.0/l));
							line.setColor(color);
							eye.drawLine(image, zBuffer, line, 1);
						}
					}
				}
			}
		}
		*/

		
		
		//Spherpoint focus=eye.viewPoint.location(1);
		return new Cellcomplex(vertex, center, edgepoint, sidepoint) /*new String("Schläfli(3,4,3)from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")")*/;
	}
	
	
	//*****************************************************************
	// Draws Schäffel433 with a color transformation
	//*****************************************************************
	public static Cellcomplex tiling433()
	{	
		double theta0=0.523598776, theta1=0.7853981638612928;
		Spherpoint[] vertex=new Spherpoint[16],
				center=new Spherpoint[24];
		/*Spherline[] tocenter=new Spherline[4];*/
		int[][] edgepoint=new int[32][2];
		int[][] sidepoint=new int[24][4];
		/*Spherline line;
		
		Color color;*/int i,j;
		
		System.out.println("Drawing verices...");
		for(i=0;i<8;i++)
		{
			vertex[i]=Spherpoint.hexahedron[i].copy();
			vertex[i].setTheta(theta0);
			//vertex[i].setColor(vertex[i].transform(transformation).standardcolor());
			//eye.drawSphere(image, zBuffer, vertex[i], radius, perforation);
			
			vertex[i+8]=Spherpoint.hexahedron[i].copy();
			vertex[i+8].setTheta(0-theta0);
			//vertex[i+8].setColor(vertex[i].transform(transformation).standardcolor());
			//eye.drawSphere(image, zBuffer, vertex[i+8], radius, perforation);
		}
		
		
		
			
			for(i=0;i<12;i++)
			{
				edgepoint[i][0]=hexahedronEdgepoint[i][0];
				edgepoint[i][1]=hexahedronEdgepoint[i][1];
				
				edgepoint[i+12][0]=hexahedronEdgepoint[i][0]+8;
				edgepoint[i+12][1]=hexahedronEdgepoint[i][1]+8;
						
			}
			
			for(i=0;i<8;i++)
			{
				edgepoint[i+24][0]=i;
				edgepoint[i+24][1]=i+8;
				
			}
		
		
		for(i=0;i<6;i++)
		{
			center[i]=Spherpoint.octahedron[i].copy();
			center[i].setTheta(theta1);
			
			center[i+18]=Spherpoint.octahedron[i].copy();
			center[i+18].setTheta(-theta1);
			for(j=0;j<4;j++)
			{
				sidepoint[i][j]=hexahedronSidepoint[i][j];
				sidepoint[i+18][j]=hexahedronSidepoint[i][j]+8;
			}
			
		}
		
		for(i=0;i<12;i++)
		{
			center[i+6]=Spherpoint.hexahedronEdge[i];
			for(j=0;j<2;j++)
			{
				sidepoint[i+6][j]=hexahedronEdgepoint[i][j];
				sidepoint[i+6][3-j]=hexahedronEdgepoint[i][j]+8;
			}
		}
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
		/*for(i=0;i<24;i++)
		{
			System.out.println("Drawing "+(i+1)+". side");
			color=center[i].transform(transformation).standardcolor();
			for(j=0;j<4;j++)
			{
				tocenter[j]=vertex[sidepoint[i][j]].geodesic(center[i]);
				
			}
			
			for(j=0;j<4;j++)
			{
				k=j+1; if(k==4){k=0;}
				
				for (l=filling;l<100;l++)
				{
					line=tocenter[k].location(1.0/l).geodesic(tocenter[j].location(1.0/l));
					line.setColor(color);
					eye.drawLine(image, zBuffer, line, 1);
						
					if(gap==false)
					{
						line=tocenter[k].location(l/10000.0).geodesic(tocenter[j].location(l/10000.0));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 1);							
					}
						
					if(empty==false)
					{
						line=tocenter[k].location(1-1.0/l).geodesic(tocenter[j].location(1-1.0/l));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 1);
					}
				}
			}
		}
		
		Spherpoint focus=eye.viewPoint.location(1);*/
		/*return new String("Schläfli(4,3,3)from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")");*/
	}
	
	//*****************************************************************
	// Returns the Cellcomlex of Schäffel352  unfinished
	//*****************************************************************
	public static Cellcomplex schäffel352()
	{		
		Spherpoint[] vertex=Spherpoint.icosahedron,
			center=Spherpoint.dodecahedron;
		int[][] edgepoint=icosahedronEdgepoint,
			sidepoint=icosahedronSidepoint;
		
		
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	
	public static Cellcomplex schäffel532()
	{
		Spherpoint[] vertex=Spherpoint.dodecahedron,
				center=Spherpoint.icosahedron;
			int[][] edgepoint=dodecahedronEdgepoint,
				sidepoint=dodecahedronSidepoint;
			
			
			return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	//*****************************************************************
	// Draws Schäffel235 with a color transformation unfinished
	//*****************************************************************
 	public static String schäffel235(Observer eye,BufferedImage image,double[][] zBuffer, double[][] transformation, Color background, int denseness)
	{	
		Spherpoint focus=eye.viewPoint.location(1);
		Spherpoint[] vertex=new Spherpoint[]{Spherpoint.northpole,Spherpoint.southpole},
				center=Spherpoint.dodecahedronEdge,
				icosahedron=Spherpoint.icosahedron;
		int[][] sidepoint=icosahedronEdgepoint;
		Spherline[] tocenter=new Spherline[2];
		Spherline line;
		Color color;
	/*	System.out.println("Drawing vertices");
		for(int i=0;i<2;i++)
		{
			vertex[i].setColor(vertex[i].transform(transformation).standardcolor());
			eye.drawSphere(image, zBuffer, vertex[i], radius, perforation);
		}*/
		if(gap)
		{
			System.out.println("Drawing edges");
			for (int i=0;i<8;i++)
			{
				line=vertex[0].geodesic(icosahedron[i]);
				line.setColor(icosahedron[i].transform(transformation).standardcolor());
				eye.drawLine(image, zBuffer, line, 2);
			}
		}
		for(int j=0;j<30;j++)
		{
			System.out.println("Drawing "+(j+1)+". side");
			Color c=center[j].transform(transformation).standardcolor();
			for (int i=0;i<2;i++)
			{
				
				tocenter[i]=icosahedron[sidepoint[j][i]].geodesic(center[j]);
				
				for(double l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					line=vertex[0].geodesic(tocenter[i].location(l));
					line.setColor(c);
					eye.drawLine(image, zBuffer, line, 2);
					
					if(gap==false )
					{
						line=vertex[0].geodesic(tocenter[i].location(1-l));
						line.setColor(c);
						eye.drawLine(image, zBuffer, line, 2);

					}
					
					if(empty==false)
					{
						line=vertex[0].geodesic(tocenter[i].location(1-1.0/l));
						line.setColor(c);
						eye.drawLine(image, zBuffer, line, 2);
					}
				}
			}
			
		}
		
		return new String("Schläfli(2,3,5)from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")");
	}

	public static String tiling253(Observer eye,BufferedImage image1,BufferedImage image2,double[][] zBuffer1,double[][]zBuffer2, double[][] transformation, Color background, int denseness)
	{	
		Spherpoint focus=eye.viewPoint.location(1);
		Spherpoint[] vertex=new Spherpoint[]{Spherpoint.northpole,Spherpoint.southpole},
				center=Spherpoint.dodecahedronEdge,
				dodecahedron=Spherpoint.dodecahedron;
		int[][] sidepoint=dodecahedronEdgepoint;
		Spherline[] tocenter=new Spherline[2];
		Spherline line;
		Color color;
		for(int j=0;j<30;j++)
		{
			System.out.println("Drawing "+(j+1)+". side");
			color=center[j].transform(transformation).standardcolor();
			for (int i=0;i<2;i++)
			{
				
				tocenter[i]=dodecahedron[sidepoint[j][i]].geodesic(center[j]);
				
				for(double l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					line=vertex[0].geodesic(tocenter[i].location(l));
					line.setColor(color);
					eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 2);
					
					if(/*gap==*/false )
					{
						line=vertex[0].geodesic(tocenter[i].location(1-l));
						line.setColor(color);
						eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 2);

					}
					
					if(empty==false)
					{
						line=vertex[0].geodesic(tocenter[i].location(1-1.0/l));
						line.setColor(color);
						eye.doubleDrawLine(image1,image2, zBuffer1,zBuffer2, line, 2);
					}
				}
			}
			
		}
		
		return new String("Schläfli(2,5,3)from ("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
				Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") to ("+
				Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
				Math.round(Math.toDegrees(focus.getTheta()))+")");
	}
	
	//*****************************************************************
	// Draws Schläfli335  with a color transformation Cellcomplex
	//*****************************************************************
	public static Cellcomplex tiling335()
	{
		double h=Math.acos(Math.sqrt((5+Math.sqrt(5))/10)),
		x=Math.atan(2-(Math.sqrt(5)+1)/2),y=h-x;
		Spherline line;
		double theta0=3*Math.PI/10, theta1=Math.PI/6, theta2=Math.PI/10, theta3=Math.acos((Math.sqrt(5)-1)/(2*Math.sqrt(3)))/*1.205932499*/, theta4=Math.PI/3/*1.047197551*/,theta5=Math.asin((Math.sqrt(5)+3)/(4*Math.sqrt(3)))/*0.83682916*/, theta6=0.831048332, 
				theta7=0.639174743,theta8=0.617502010, theta9=x, theta10=0.315473388, theta11=0.189821951, theta12=0.129029536; 
		Spherpoint[] vertex=new Spherpoint[120],
				center=new Spherpoint[1200];
		int[][] sidepoint=new int[1200][3];//1200
		int [][] edgepoint=new int[720][2];
		int i,j,k;
		
		vertex[0]=Spherpoint.northpole.copy();
		vertex[0].setColor(vertex[0].standardcolor().getRGB());
		vertex[119]=Spherpoint.southpole.copy();
		vertex[119].setColor(vertex[119].standardcolor().getRGB());
		
		for (i=0;i<12;i++)
		{
			vertex[i+1]=Spherpoint.icosahedron[i].copy();
			vertex[i+1].setTheta(theta0);
			vertex[i+1].setColor(vertex[i+1].standardcolor().getRGB());
			vertex[i+107]=Spherpoint.icosahedron[i].copy();
			vertex[i+107].setTheta(-theta0);
			vertex[i+107].setColor(vertex[i+107].standardcolor().getRGB());
			
			vertex[i+33]=Spherpoint.icosahedron[i].copy();
			vertex[i+33].setTheta(theta2);
			vertex[i+33].setColor(vertex[i+33].standardcolor().getRGB());
			vertex[i+75]=Spherpoint.icosahedron[i].copy();
			vertex[i+75].setTheta(-theta2);
			vertex[i+75].setColor(vertex[i+75].standardcolor().getRGB());
			
			
			
			edgepoint[i][0]=0;
			edgepoint[i][1]=i+1;
			edgepoint[i+708][0]=119;
			edgepoint[i+708][1]=i+107;
			
			edgepoint[i+102][0]=i+1;
			edgepoint[i+102][1]=1+33;
			edgepoint[i+606][0]=i+107;
			edgepoint[i+606][1]=i+75;
			
			edgepoint[i+324][0]=i+33;
			edgepoint[i+324][1]=i+75;
			
			for(j=0;j<5;j++)
			{
				/*line=Spherpoint.dodecahedron[dodecahedronSidepoint[i][j]].copy().geodesic(Spherpoint.icosahedron[i]);
				center[i+170+12*j]=line.location(x/(x+y));
				center[i+170+12*j].setTheta(theta7);
				center[i+970+12*j]=center[i+170+12*j].copy();
				center[i+970+12*j].setTheta(-theta7);
				
				line=Spherpoint.icosahedron[i].geodesic(Spherpoint.dodecahedronEdge[dodecahedronSideedge[i][j]]);
				center[i+560+j*12]=line.location(y/(x+y));
*/
				
				
				sidepoint[i+170+j*12][0]=i+1;
				sidepoint[i+170+j*12][1]=dodecahedronSidepoint[i][j]+13;
				sidepoint[i+170+j*12][2]=i+33;
				sidepoint[i+970+j*12][0]=i+107;
				sidepoint[i+970+j*12][1]=dodecahedronSidepoint[i][j]+87;
				sidepoint[i+970+j*12][2]=i+75;
				
				sidepoint[i+560+j*12][0]=i+33;
				sidepoint[i+560+j*12][2]=i+75;
				sidepoint[i+560+j*12][1]=dodecahedronSideedge[i][j]+45;
				
				k=j+1;if(k==5){k=0;}	
				
					/*line=Spherpoint.dodecahedronEdge[dodecahedronSideedge[i][k]].geodesic(Spherpoint.icosahedron[i]);
					center[5*i+500+j]=line.location(0.5);
					line=Spherpoint.dodecahedronEdge[dodecahedronSideedge[i][j]].geodesic(center[5*i+500+j]);
					center[5*i+500+j]=line.location(x/(x+y));
					center[5*i+500+j].setTheta(theta12);
					center[5*i+640+j]=center[5*i+500+j].copy();
					center[5*i+640+j].setTheta(-theta12);*/
				
					sidepoint[5*i+500+j][0]=i+33;
					sidepoint[5*i+500+j][1]=dodecahedronSideedge[i][k]+45;
					sidepoint[5*i+500+j][2]=dodecahedronSideedge[i][j]+45;	
					sidepoint[5*i+640+j][0]=i+75;
					sidepoint[5*i+640+j][1]=dodecahedronSideedge[i][k]+45;
					sidepoint[5*i+640+j][2]=dodecahedronSideedge[i][j]+45;	
				
			}
		}
		
		for(i=0;i<20;i++)
		{
			vertex[i+13]=Spherpoint.dodecahedron[i].copy();
			vertex[i+13].setTheta(theta1);
			vertex[i+13].setColor(vertex[i+13].standardcolor().getRGB());
			vertex[i+87]=Spherpoint.dodecahedron[i].copy();
			vertex[i+87].setTheta(-theta1);
			vertex[i+87].setColor(vertex[i+87].standardcolor().getRGB());

			/*center[i+30]=Spherpoint.dodecahedron[i].copy();
			center[i+30].setTheta(theta4);
			center[i+1150]=Spherpoint.dodecahedron[i].copy();
			center[i+1150].setTheta(-theta4);
			
			center[i+620]=Spherpoint.dodecahedron[i].copy();*/
			
			for(j=1;j<3;j++)
			{
				
				for(k=0;k<j;k++)
				{
					/*line=Spherpoint.dodecahedronEdge[icosahedronSideedge[i][k]].geodesic(Spherpoint.dodecahedron[i]);
					center[3*i+440+j+k-1]=line.location(0.5);
					line=Spherpoint.dodecahedronEdge[icosahedronSideedge[i][j]].geodesic(center[3*i+440+j+k-1]);
					center[3*i+440+j+k-1]=line.location(x/(x+y));
					center[3*i+440+j+k-1].setTheta(theta11);
					center[3*i+700+j+k-1]=center[3*i+440+j+k-1].copy();
					center[3*i+700+j+k-1].setTheta(-theta11);*/
					
					sidepoint[3*i+440+j+k-1][0]=i+13;
					sidepoint[3*i+440+j+k-1][1]=icosahedronSideedge[i][k]+45;
					sidepoint[3*i+440+j+k-1][2]=icosahedronSideedge[i][j]+45;
					sidepoint[3*i+700+j+k-1][0]=i+87;
					sidepoint[3*i+700+j+k-1][1]=icosahedronSideedge[i][k]+45;
					sidepoint[3*i+700+j+k-1][2]=icosahedronSideedge[i][j]+45;
					
					edgepoint[3*i+j+k-1+336][0]=icosahedronSideedge[i][k]+45;
					edgepoint[3*i+j+k-1+336][1]=icosahedronSideedge[i][j]+45;
					
				}
			}
			
			
			for(j=0;j<3;j++)
			{
				sidepoint[i+30][j]=icosahedronSidepoint[i][j]+1;
				sidepoint[i+1150][j]=icosahedronSidepoint[i][j]+107;
				
				sidepoint[i+620][j]=icosahedronSideedge[i][j]+45;
				
				edgepoint[3*i+42+j][0]=icosahedronSidepoint[i][j]+1;
				edgepoint[3*i+42+j][1]=i+13;
				edgepoint[3*i+j+618][0]=icosahedronSidepoint[i][j]+107;
				edgepoint[3*i+j+618][1]=i+87;
				
				edgepoint[3*i+144+j][0]=i+13;
				edgepoint[3*i+144+j][1]=icosahedronSidepoint[i][j]+33;
				edgepoint[3*i+516+j][0]=i+87;
				edgepoint[3*i+516+j][1]=icosahedronSidepoint[i][j]+75;
				
				edgepoint[3*i+204+j][0]=i+13;
				edgepoint[3*i+204+j][1]=icosahedronSideedge[i][j]+45;
				edgepoint[3*i+456+j][0]=i+87;
				edgepoint[3*i+456+j][1]=icosahedronSideedge[i][j]+45;
			}
		}
		//theta5=vertex[1].geodesic(vertex[2]).location(0.5).getTheta();//System.out.println(theta5);
		
		//theta6=vertex[13].geodesic(vertex[14]).location(0.5).getTheta();//System.out.println(theta6);
		/*Spherline li0=.geodesic(vertex[13]);
			Spherline li1=vertex[13].geodesic(vertex[2]).location(0.5).geodesic(vertex[1]);
			for(double t=0.33901;t<0.33903;t=t+0.000001)
			{System.out.println("t="+t+" , dist="+li0.location(t).distance(li1.location(t)));}
			double d=Math.abs(li.location(t).distance(vertex[1])-li.location(t).distance(vertex[13]));
			while(d>li.location(t+0.00000000000000005).distance(vertex[1])-li.location(t+0.00000000000000005).distance(vertex[13]))
			{
				t=t+0.00000000000000005;
				d=Math.abs(li.location(t).distance(vertex[1])-li.location(t).distance(vertex[13]));
			}
			theta5=li0.location(0.399023).getTheta();
			
				*/ 
		for (i=0;i<30;i++)
		{
			vertex[i+45]=Spherpoint.dodecahedronEdge[i].copy();
			vertex[i+45].setColor(vertex[i+45].standardcolor().getRGB());
			
			/*center[i]=Spherpoint.dodecahedronEdge[i].copy();
			center[i].setTheta(theta3);
			center[i+1170]=Spherpoint.dodecahedronEdge[i].copy();
			center[i+1170].setTheta(-theta3);
			
			center[i+290]=Spherpoint.dodecahedronEdge[i].copy();
			center[i+290].setTheta(theta9);
			center[i+880]=center[i+290].copy();
			center[i+880].setTheta(-theta9);*/
			
			sidepoint[i][0]=0;
			sidepoint[i+1170][0]=119;
			
			sidepoint[i+50][2]=dodecahedronEdgepoint[i][0]+13;
			sidepoint[i+80][2]=dodecahedronEdgepoint[i][1]+13;
			sidepoint[i+1120][2]=dodecahedronEdgepoint[i][1]+87;
			sidepoint[i+1090][2]=dodecahedronEdgepoint[i][0]+87;
			
			sidepoint[i+110][0]=icosahedronEdgepoint[i][0]+1;
			sidepoint[i+140][0]=icosahedronEdgepoint[i][1]+1;
			sidepoint[i+1060][0]=icosahedronEdgepoint[i][1]+107;
			sidepoint[i+1030][0]=icosahedronEdgepoint[i][0]+107;
			
			sidepoint[i+230][2]=icosahedronEdgepoint[i][0]+33;
			sidepoint[i+260][2]=icosahedronEdgepoint[i][1]+33;
			sidepoint[i+940][2]=icosahedronEdgepoint[i][1]+75;
			sidepoint[i+910][2]=icosahedronEdgepoint[i][0]+75;
			
			sidepoint[i+290][2]=i+45;
			sidepoint[i+880][2]=i+45;
			Spherpoint p=Spherpoint.dodecahedronEdge[i].copy();
			for(j=0;j<2;j++)
			{	
				p.setTheta(theta5);
				//line=p.geodesic(vertex[dodecahedronEdgepoint[i][j]+13]);
			
				//center[i+50+30*j]=line.location(y/(x+y));
				
				//center[i+1090+30*j]=center[i+50+30*j].copy();
				//center[i+1090+30*j].setTheta(-center[i+1090+30*j].getTheta());
				
				
				//line=vertex[13+dodecahedronEdgepoint[i][0]].geodesic(vertex[13+dodecahedronEdgepoint[i][1]]).location(0.5).geodesic(vertex[icosahedronEdgepoint[i][j]+1]);
				//center[i+110+30*j]=line.location(y/(x+y));
				
				//center[i+1030+30*j]=center[i+110+30*j].copy();
				//center[i+1030+30*j].setTheta(-center[i+1030+30*j].getTheta());
				
				//line=vertex[13+dodecahedronEdgepoint[i][0]].geodesic(vertex[13+dodecahedronEdgepoint[i][1]]).location(0.5).geodesic(vertex[icosahedronEdgepoint[i][j]+33]);
				//center[i+230+30*j]=line.location(y/(x+y));
				//center[i+910+30*j]=center[i+230+30*j].copy();
				//center[i+910+30*j].setTheta(-center[i+910+30*j].getTheta());
				
				sidepoint[i][j+1]=icosahedronEdgepoint[i][j]+1;
				sidepoint[i+1170][j+1]=icosahedronEdgepoint[i][j]+107;
				
				sidepoint[i+50][j]=icosahedronEdgepoint[i][j]+1;
				sidepoint[i+80][j]=icosahedronEdgepoint[i][j]+1;
				sidepoint[i+1120][j]=icosahedronEdgepoint[i][j]+107;
				sidepoint[i+1090][j]=icosahedronEdgepoint[i][j]+107;
				
				sidepoint[i+110][j+1]=dodecahedronEdgepoint[i][j]+13;
				sidepoint[i+140][j+1]=dodecahedronEdgepoint[i][j]+13;
				sidepoint[i+1060][j+1]=dodecahedronEdgepoint[i][j]+87;
				sidepoint[i+1030][j+1]=dodecahedronEdgepoint[i][j]+87;
				
				sidepoint[i+230][j]=dodecahedronEdgepoint[i][j]+13;
				sidepoint[i+260][j]=dodecahedronEdgepoint[i][j]+13;
				sidepoint[i+940][j]=dodecahedronEdgepoint[i][j]+87;
				sidepoint[i+910][j]=dodecahedronEdgepoint[i][j]+87;
				
				sidepoint[i+290][j]=dodecahedronEdgepoint[i][j]+13;
				sidepoint[i+880][j]=dodecahedronEdgepoint[i][j]+87;
				
				edgepoint[i+12][j]=icosahedronEdgepoint[i][j]+1;
				edgepoint[i+678][j]=icosahedronEdgepoint[i][j]+107;
			
				edgepoint[i+114][j]=dodecahedronEdgepoint[i][j]+13;
				edgepoint[i+576][j]=dodecahedronEdgepoint[i][j]+87;
				
				edgepoint[2*i+264+j][0]=icosahedronEdgepoint[i][j]+33;
				edgepoint[2*i+264+j][1]=i+45;
				edgepoint[2*i+396+j][0]=icosahedronEdgepoint[i][j]+75;
				edgepoint[2*i+396+j][1]=i+45;
				
				for(k=0;k<2;k++)
				{
					/*line=Spherpoint.dodecahedronEdge[i].geodesic(Spherpoint.dodecahedron[dodecahedronEdgepoint[i][j]]);
					center[i+320+j*30+k*60]=line.location(0.5);
					line=Spherpoint.icosahedron[icosahedronEdgepoint[i][k]].geodesic(center[i+320+j*30+k*60]);
					center[i+320+j*30+k*60]=line.location(x/(x+y));
					center[i+320+j*30+k*60].setTheta(theta10);
					center[i+760+j*30+k*60]=center[i+320+j*30+k*60].copy();
					center[i+760+j*30+k*60].setTheta(-theta10);*/
					
					sidepoint[i+320+j*30+k*60][0]=dodecahedronEdgepoint[i][j]+13;
					sidepoint[i+320+j*30+k*60][1]=icosahedronEdgepoint[i][k]+33;
					sidepoint[i+320+j*30+k*60][2]=i+45;
					sidepoint[i+760+j*30+k*60][0]=dodecahedronEdgepoint[i][j]+87;
					sidepoint[i+760+j*30+k*60][1]=icosahedronEdgepoint[i][k]+75;
					sidepoint[i+760+j*30+k*60][2]=i+45;
				}
				
			}
		}
		center=Cellcomplex.centers(vertex, sidepoint);
		return new Cellcomplex(vertex,center,edgepoint,sidepoint) ;
	}
	
	//*****************************************
	//Returns the Cellcomplex 533
	//******************************************
	public static Cellcomplex tiling533()
	{
		double theta1=Math.asin((Math.sqrt(5)+3)/(4*Math.sqrt(2))), theta2=Math.asin(Math.sqrt(10)/4),
				theta3=Math.PI/4,theta4=Math.asin((Math.sqrt(5)+1)/(4*Math.sqrt(2))),
				theta5=Math.asin(1/Math.sqrt(8)),theta6=Math.asin((Math.sqrt(5)-1)/Math.sqrt(32)),
				theta7=Math.asin((3-Math.sqrt(5))/Math.sqrt(32));
		
		Spherpoint[] vertex=new Spherpoint[600],
				center;
		int[][] sidepoint=new int[720][5],
				edgepoint=new int[1200][2];
		
		for(int i=0;i<12;i++)
		{
			for(int j=0;j<5;j++)
			{
				int k=(j+4)%5;
				int h=0,h1=j;
				if(i==icosahedronEdgepoint[dodecahedronSideedge[i][j]][h]) 
				{
					h=1;
				}
				if(dodecahedronSidepoint[i][h1]!=dodecahedronEdgepoint[dodecahedronSideedge[i][j]][1])
				{
					h1=k;
				}
				Spherline line=Spherpoint.icosahedron[i].geodesic(Spherpoint.icosahedron[icosahedronEdgepoint[dodecahedronSideedge[i][j]][h]]);
				vertex[5*i+70+j]=line.location(0.256437);
				vertex[5*i+70+j].setTheta(theta4);
				vertex[5*i+470+j]=line.location(0.256437);
				vertex[5*i+470+j].setTheta(-theta4);
				
				vertex[5*i+130+j]=line.location(0.369145);
				vertex[5*i+130+j].setTheta(theta5);
				vertex[5*i+410+j]=line.location(0.369145);
				vertex[5*i+410+j].setTheta(-theta5);
				
				line=Spherpoint.icosahedron[i].geodesic(Spherpoint.dodecahedron[dodecahedronSidepoint[i][j]]);
				vertex[5*i+190+j]=line.location(0.5991);//??time completely guessed between 0.5 and 0.75
				vertex[5*i+190+j].setTheta(theta6);
				vertex[5*i+350+j]=line.location(0.5991);
				vertex[5*i+350+j].setTheta(-theta6);
				
				vertex[5*i+270+j]=line.location(0.3553627);//??time competely guessed
				
				
				sidepoint[i][j]=dodecahedronSidepoint[i][j];
				sidepoint[i+708][j]=dodecahedronSidepoint[i][j]+580;
				
				sidepoint[5*i+j+42][0]=dodecahedronSidepoint[i][k]+20;
				sidepoint[5*i+j+42][1]=dodecahedronSideedge[i][j]+40;
				sidepoint[5*i+j+42][4]=dodecahedronSideedge[i][k]+40;
				sidepoint[5*i+j+42][2]=5*i+j+70;
				sidepoint[5*i+j+42][3]=5*i+k+70;
				sidepoint[5*i+j+618][0]=dodecahedronSidepoint[i][k]+560;
				sidepoint[5*i+j+618][1]=dodecahedronSideedge[i][j]+530;
				sidepoint[5*i+j+618][4]=dodecahedronSideedge[i][k]+530;
				sidepoint[5*i+j+618][2]=5*i+j+470;
				sidepoint[5*i+j+618][3]=5*i+k+470;
				
				sidepoint[i+102][j]=5*i+70+j;
				sidepoint[i+606][j]=5*i+470+j;
				
				sidepoint[dodecahedronSideedge[i][j]+114][0]=40+dodecahedronSideedge[i][j];
				sidepoint[dodecahedronSideedge[i][j]+576][0]=530+dodecahedronSideedge[i][j];
	
				sidepoint[144+5*i+j][0]=70+5*i+j;
				sidepoint[144+5*i+j][4]=70+5*i+k;
				sidepoint[144+5*i+j][1]=130+5*i+j;
				sidepoint[144+5*i+j][3]=130+5*i+k;
				sidepoint[144+5*i+j][2]=190+5*i+k;
				sidepoint[516+5*i+j][0]=470+5*i+j;
				sidepoint[516+5*i+j][4]=470+5*i+k;
				sidepoint[516+5*i+j][1]=410+5*i+j;
				sidepoint[516+5*i+j][3]=410+5*i+k;
				sidepoint[516+5*i+j][2]=350+5*i+k;
				
				sidepoint[324+i][j]=270+5*i+j;
				
				sidepoint[336+5*i+j][0]=250+dodecahedronSidepoint[i][j];
				sidepoint[336+5*i+j][1]=190+5*i+j;
				sidepoint[336+5*i+j][2]=270+5*i+j;
				sidepoint[336+5*i+j][3]=350+5*i+j;
				sidepoint[336+5*i+j][4]=330+dodecahedronSidepoint[i][j];
				
				sidepoint[264+5*i+j][0]=270+5*i+j;
				sidepoint[264+5*i+j][1]=270+5*i+k;
				sidepoint[264+5*i+j][2]=190+5*i+k;
				sidepoint[264+5*i+j][3]=130+5*i+j;
				sidepoint[264+5*i+j][4]=190+5*i+j;
				sidepoint[396+5*i+j][0]=270+5*i+j;
				sidepoint[396+5*i+j][1]=270+5*i+k;
				sidepoint[396+5*i+j][2]=350+5*i+k;
				sidepoint[396+5*i+j][3]=410+5*i+j;
				sidepoint[396+5*i+j][4]=350+5*i+j;
				
	
				edgepoint[5*i+110+j][0]=dodecahedronSideedge[i][j]+40;
				edgepoint[5*i+110+j][1]=5*i+70+j;
				edgepoint[5*i+1030+j][0]=dodecahedronSideedge[i][j]+530;
				edgepoint[5*i+1030+j][1]=5*i+470+j;
				
				
				edgepoint[5*i+j+170][0]=5*i+70+k;
				edgepoint[5*i+j+170][1]=5*i+70+j;
				edgepoint[5*i+j+970][0]=5*i+470+k;
				edgepoint[5*i+j+970][1]=5*i+470+j;
				
				edgepoint[5*i+j+230][0]=5*i+70+j;
				edgepoint[5*i+j+230][1]=5*i+130+j;
				edgepoint[5*i+j+910][0]=5*i+470+j;
				edgepoint[5*i+j+910][1]=5*i+410+j;
				
				edgepoint[320+5*i+j][0]=130+5*i+j;
				edgepoint[380+5*i+j][0]=130+5*i+j;
				edgepoint[320+5*i+j][1]=190+5*i+j;
				edgepoint[380+5*i+j][1]=190+5*i+k;
				edgepoint[760+5*i+j][0]=410+5*i+j;
				edgepoint[820+5*i+j][0]=410+5*i+j;
				edgepoint[760+5*i+j][1]=350+5*i+j;
				edgepoint[820+5*i+j][1]=350+5*i+k;
				
				edgepoint[440+5*i+j][0]=190+5*i+j;
				edgepoint[440+5*i+j][1]=250+dodecahedronSidepoint[i][j];
				edgepoint[700+5*i+j][0]=350+5*i+j;
				edgepoint[700+5*i+j][1]=330+dodecahedronSidepoint[i][j];
				
				edgepoint[500+5*i+j][0]=190+5*i+j;
				edgepoint[500+5*i+j][1]=270+5*i+j;
				edgepoint[640+5*i+j][0]=350+5*i+j;
				edgepoint[640+5*i+j][1]=270+5*i+j;
				
				edgepoint[560+5*i+j][0]=270+5*i+j;
				edgepoint[560+5*i+j][1]=270+5*i+k;
				
				if(edgepoint[290+dodecahedronSideedge[i][j]][0]==0)
				{
					sidepoint[dodecahedronSideedge[i][j]+114][1]=70+5*i+j;
					sidepoint[dodecahedronSideedge[i][j]+114][2]=130+5*i+j;
					sidepoint[dodecahedronSideedge[i][j]+576][1]=470+5*i+j;
					sidepoint[dodecahedronSideedge[i][j]+576][2]=410+5*i+j;
	
					sidepoint[2*dodecahedronSideedge[i][j]+204][0]=130+5*i+j;
					sidepoint[2*dodecahedronSideedge[i][j]+204][1]=190+5*i+h1;
					sidepoint[2*dodecahedronSideedge[i][j]+204][2]=250+dodecahedronSidepoint[i][h1];
					sidepoint[2*dodecahedronSideedge[i][j]+204+1][0]=130+5*i+j;
					sidepoint[2*dodecahedronSideedge[i][j]+204+1][1]=190+5*i+j+k-h1;
					sidepoint[2*dodecahedronSideedge[i][j]+204+1][2]=250+dodecahedronSidepoint[i][j+k-h1];
					sidepoint[2*dodecahedronSideedge[i][j]+456][0]=410+5*i+j;
					sidepoint[2*dodecahedronSideedge[i][j]+456][1]=350+5*i+h1;
					sidepoint[2*dodecahedronSideedge[i][j]+456][2]=330+dodecahedronSidepoint[i][h1];
					sidepoint[2*dodecahedronSideedge[i][j]+456+1][0]=410+5*i+j;
					sidepoint[2*dodecahedronSideedge[i][j]+456+1][1]=350+5*i+j+k-h1;
					sidepoint[2*dodecahedronSideedge[i][j]+456+1][2]=330+dodecahedronSidepoint[i][j+k-h1];
					
					edgepoint[290+dodecahedronSideedge[i][j]][0]=130+5*i+j;
					edgepoint[880+dodecahedronSideedge[i][j]][0]=410+5*i+j;
				}
				else
				{
					sidepoint[dodecahedronSideedge[i][j]+114][4]=70+5*i+j;
					sidepoint[dodecahedronSideedge[i][j]+114][3]=130+5*i+j;
					sidepoint[dodecahedronSideedge[i][j]+576][4]=470+5*i+j;
					sidepoint[dodecahedronSideedge[i][j]+576][3]=410+5*i+j;
	
					sidepoint[2*dodecahedronSideedge[i][j]+204][4]=130+5*i+j;
					sidepoint[2*dodecahedronSideedge[i][j]+204][3]=190+5*i+h1;
					sidepoint[2*dodecahedronSideedge[i][j]+204+1][4]=130+5*i+j;
					sidepoint[2*dodecahedronSideedge[i][j]+204+1][3]=190+5*i+j+k-h1;
					sidepoint[2*dodecahedronSideedge[i][j]+456][4]=410+5*i+j;
					sidepoint[2*dodecahedronSideedge[i][j]+456][3]=350+5*i+h1;
					sidepoint[2*dodecahedronSideedge[i][j]+456+1][4]=410+5*i+j;
					sidepoint[2*dodecahedronSideedge[i][j]+456+1][3]=350+5*i+j+k-h1;
	
					edgepoint[290+dodecahedronSideedge[i][j]][1]=130+5*i+j;
					edgepoint[880+dodecahedronSideedge[i][j]][1]=410+5*i+j;
				}
			}
		}
		for(int i=0;i<20;i++)
		{
			vertex[i]=Spherpoint.dodecahedron[i].copy();
			vertex[i].setTheta(theta1);
			vertex[i+580]=Spherpoint.dodecahedron[i].copy();
			vertex[i+580].setTheta(-theta1);
			
			vertex[i+20]=Spherpoint.dodecahedron[i].copy();
			vertex[i+20].setTheta(theta2);
			vertex[i+560]=Spherpoint.dodecahedron[i].copy();
			vertex[i+560].setTheta(-theta2);
			
			vertex[i+250]=Spherpoint.dodecahedron[i].copy();
			vertex[i+250].setTheta(theta7);
			vertex[i+330]=Spherpoint.dodecahedron[i].copy();
			vertex[i+330].setTheta(-theta7);
			
			edgepoint[i+30][0]=i;
			edgepoint[i+30][1]=i+20;
			edgepoint[i+1150][0]=i+580;
			edgepoint[i+1150][1]=i+560;
			
			edgepoint[i+620][0]=i+250;
			edgepoint[i+620][1]=i+330;
		
			
			
		}
		for(int i=0;i<30;i++)
		{
			
			vertex[i+40]=Spherpoint.dodecahedronEdge[i].copy();
			vertex[i+40].setTheta(theta3);
			vertex[i+530]=Spherpoint.dodecahedronEdge[i].copy();
			vertex[i+530].setTheta(-theta3);
			
			sidepoint[i+12][4]=i+40;
			sidepoint[i+678][4]=i+530;
			
			for(int j=0;j<2;j++)
			{
				
				
				sidepoint[i+12][1+j]=dodecahedronEdgepoint[i][j];
				sidepoint[i+12][3*j]=dodecahedronEdgepoint[i][j]+20;
				sidepoint[i+678][1+j]=dodecahedronEdgepoint[i][j]+580;
				sidepoint[i+678][3*j]=dodecahedronEdgepoint[i][j]+560;
	
				edgepoint[i][j]=dodecahedronEdgepoint[i][j];
				edgepoint[i+1170][j]=dodecahedronEdgepoint[i][j]+580;
				
				edgepoint[i+50+30*j][1]=i+40;
				edgepoint[i+50+30*j][0]=dodecahedronEdgepoint[i][j]+20;
				edgepoint[i+1090+30*j][1]=i+530;
				edgepoint[i+1090+30*j][0]=dodecahedronEdgepoint[i][j]+560;
				
				
			}
		}
		center=Cellcomplex.centers(vertex, sidepoint);
		
		
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	//****************************************
	// Draws the dual of x y-prisms and y x-prisms linking
	//******************************************
	public static Cellcomplex tetraprismxy(int x, int y)
	{
		double psi,theta;
		Spherpoint[] vertex=new Spherpoint[x+y],
			center;
		int[][]sidepoint=new int[2*x*y][3],
				edgepoint=new int[x+y+x*y][2];

		for(int i=0;i<x;i++)
		{
			theta=(2.0*i/x-1/2)*Math.PI;
			if(theta>=Math.PI/2)
			{
				theta=Math.PI-theta;
				psi=-Math.PI/2;
			}
			else 
			{
				psi=Math.PI/2;
			}
			vertex[i]=new Spherpoint(0,psi,theta);
			vertex[i].println();
			for(int j=0;j<y;j++)
			{
				sidepoint[j*x+i][0]=i;
				if(i==x-1)sidepoint[j*x+i][1]=0;
				else sidepoint[j*x+i][1]=i+1;
				sidepoint[j*x+i][2]=x+j;
				
				sidepoint[x*y+i*y+j][0]=x+j;
				if(j==y-1)	sidepoint[x*y+i*y+j][1]=x;
				else 	sidepoint[x*y+i*y+j][1]=x+j+1;
				sidepoint[x*y+i*y+j][2]=i;
			}
			
			edgepoint[i][0]=i;
			if(i==x-1)edgepoint[i][1]=0;
			else edgepoint[i][1]=i+1;
		}
		for(int j=0;j<y;j++)
		{
			vertex[j+x]=new Spherpoint((2.0*j/y-1)*Math.PI,0,0);
			vertex[j+x].println();
		}
		center=Cellcomplex.centers(vertex, sidepoint);
		return new Cellcomplex(vertex, center, edgepoint, sidepoint);
	}
	//**************************************************
	//Cellcomplex consisting of tetrahedra with vertex figures tetrahedron-prism-prism-prism
	//***********************************************
	public static Cellcomplex tiling33tppp()
	{
		Spherpoint[] vertex=new Spherpoint[6],
				center;
		int[][] sidepoint=new int[16][3],
				edgepoint=new int[14][2];
		vertex[0]=Spherpoint.northpole.copy();
		vertex[5]=Spherpoint.southpole.copy();
	
		for(int i=0;i<4;i++)
		{
			vertex[i+1]=Spherpoint.tetrahedron[i].getAntipodal();
			
			edgepoint[i][0]=0;
			edgepoint[i][1]=i+1;
			edgepoint[i+10][0]=5;
			edgepoint[i+10][1]=i+1;
			
			
			for(int j=0;j<3;j++)
			{
				sidepoint[i+6][j]=tetrahedronSidepoint[i][j]+1;
			}
		}
		for(int i=0;i<6;i++)
		{
			sidepoint[i][0]=0;
			sidepoint[i+10][0]=5;
			
			for(int j=0;j<2;j++)
			{
				sidepoint[i][j+1]=tetrahedronEdgepoint[i][j]+1;
				sidepoint[i+10][j+1]=tetrahedronEdgepoint[i][j]+1;
			
				edgepoint[i+4][j]=tetrahedronEdgepoint[i][j]+1;
			}
			
		}
		center=Cellcomplex.centers(vertex, sidepoint);
		
		
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	
	//**************************************************
		//Cellcomplex consisting of tetrahedra with vertex figures dodecahedron-prism-prism-prism -->A icosahedral hyperbipyramid
		//***********************************************
	public static Cellcomplex tiling33dppp()
	{
		Spherpoint[] vertex=new Spherpoint[14],
				center;
		int[][] sidepoint=new int[80][3],
				edgepoint=new int[54][2];
		vertex[0]=Spherpoint.northpole.copy();
		vertex[13]=Spherpoint.southpole.copy();
	
		for(int i=0;i<12;i++)
		{
			vertex[i+1]=Spherpoint.icosahedron[i].copy();
			
			edgepoint[i][0]=0;
			edgepoint[i][1]=i+1;
			edgepoint[i+42][0]=13;
			edgepoint[i+42][1]=i+1;
			
			
			for(int j=0;j<3;j++)
			{
				sidepoint[i+30][j]=icosahedronSidepoint[i][j]+1;
			}
		}
		for(int i=0;i<30;i++)
		{
			sidepoint[i][0]=0;
			sidepoint[i+50][0]=13;
			
			for(int j=0;j<2;j++)
			{
				sidepoint[i][j+1]=icosahedronEdgepoint[i][j]+1;
				sidepoint[i+50][j+1]=icosahedronEdgepoint[i][j]+1;
			
				edgepoint[i+12][j]=icosahedronEdgepoint[i][j]+1;
			}
			
		}
		center=Cellcomplex.centers(vertex, sidepoint);
		
		
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	
	//*********************************************
	//creates the hyperbipyramid with th given base
	//*********************************************
	public static Cellcomplex bipyramid(Cellcomplex base) 
	{
		Spherpoint[] vertex=new Spherpoint[base.vertex.length+2];
		int[][]edgepoint=new int[base.vertex.length*2+base.edgepoint.length][2];
		int[][]sidepoint=new int[2*base.edgepoint.length+base.sidepoint.length][3];
		vertex[0]=Spherpoint.northpole;
		vertex[vertex.length-1]=Spherpoint.southpole;
		
		for(int i=0;i<base.vertex.length;i++)
		{
			vertex[i+1]=base.vertex[i];
			
			edgepoint[i][0]=0;
			edgepoint[i][1]=i+1;
			
			edgepoint[i+base.vertex.length][0]=vertex.length-1;
			edgepoint[i+base.vertex.length][1]=i+1;
		}
		for(int i=0;i<base.edgepoint.length;i++)
		{
			edgepoint[i+2*base.vertex.length]=base.edgepoint[i];
			
			sidepoint[i][0]=base.edgepoint[i][0]+1;
			sidepoint[i][1]=base.edgepoint[i][1]+1;
			sidepoint[i][2]=0;
			
			sidepoint[i+base.edgepoint.length][0]=base.edgepoint[i][0]+1;
			sidepoint[i+base.edgepoint.length][1]=base.edgepoint[i][1]+1;
			sidepoint[i+base.edgepoint.length][2]=vertex.length-1;
		}
		for(int i=0;i<base.sidepoint.length;i++)
		{
			sidepoint[i+2*base.edgepoint.length][0]=base.sidepoint[i][0]+1;
			sidepoint[i+2*base.edgepoint.length][1]=base.sidepoint[i][1]+1;
			sidepoint[i+2*base.edgepoint.length][2]=base.sidepoint[i][2]+1;
		}
		Spherpoint[] center=Cellcomplex.centers(vertex, sidepoint);
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}

	//**********************************************
	//dual to tiling made from 3 trunc. tetrahedra per edge
	//************************************************
	public static Cellcomplex tiling3trunct()
	{
		double theta0=0.5236;
		Spherpoint[] vertex=new Spherpoint[10];
		int[][] edgepoint=new int[40][2]; 
		int[][]	sidepoint=new int[60][3];
		
		vertex[0]=Spherpoint.northpole;
		vertex[9]=Spherpoint.southpole;
		
		int k=0,l=0;
		
		for(int i=0;i<4;i++)
		{
			vertex[i+1]=Spherpoint.tetrahedron[i].copy();
			vertex[i+1].setTheta(theta0);
			
			vertex[i+5]=Spherpoint.tetrahedron[i].getAntipodal();
			vertex[i+5].setTheta(-theta0);
			
			edgepoint[i][0]=0;
			edgepoint[i][1]=i+1;
					
			edgepoint[36+i][0]=9;
			edgepoint[36+i][1]=i+5;
			
			edgepoint[i+10][0]=0;
			edgepoint[i+10][1]=i+5;
			
			edgepoint[i+26][0]=9;
			edgepoint[i+26][1]=i+1;
			
			
			for(int j=i+1;j<4;j++)
			{
				edgepoint[k+4][0]=i+1;
				edgepoint[k+4][1]=j+1;
				edgepoint[k+30][0]=i+5;
				edgepoint[k+30][1]=i+5;
				
				edgepoint[k+14][0]=i+1;
				edgepoint[k+14][1]=j+5;
				edgepoint[k+20][0]=i+5;
				edgepoint[k+20][1]=j+1;
				
				sidepoint[k][0]=0;
				sidepoint[k][1]=i+1;
				sidepoint[k][2]=j+5;
				sidepoint[k+54][0]=9;
				sidepoint[k+54][1]=i+5;
				sidepoint[k+54][2]=j+1;
				
				sidepoint[k+6][0]=0;
				sidepoint[k+6][1]=i+5;
				sidepoint[k+6][2]=j+1;
				sidepoint[k+48][0]=9;
				sidepoint[k+48][1]=i+1;
				sidepoint[k+48][2]=j+5;
				
				sidepoint[k+12][0]=0;
				sidepoint[k+12][1]=i+1;
				sidepoint[k+12][2]=j+1;
				sidepoint[k+42][0]=9;
				sidepoint[k+42][1]=i+5;
				sidepoint[k+42][2]=j+5;
				
				for(int m=j+1;m<4;m++)
				{
					sidepoint[l+18][0]=i+1;
					sidepoint[l+18][1]=j+1;
					sidepoint[l+18][2]=k+5;
					sidepoint[l+38][0]=i+5;
					sidepoint[l+38][1]=j+5;
					sidepoint[l+38][2]=k+1;
					
					sidepoint[l+22][0]=i+1;
					sidepoint[l+22][1]=j+5;
					sidepoint[l+22][2]=k+1;
					sidepoint[l+34][0]=i+5;
					sidepoint[l+34][1]=j+1;
					sidepoint[l+34][2]=k+5;
					
					sidepoint[l+26][0]=i+5;
					sidepoint[l+26][1]=j+1;
					sidepoint[l+26][2]=k+1;
					sidepoint[l+30][0]=i+1;
					sidepoint[l+30][1]=j+5;
					sidepoint[l+30][2]=k+5;
					
					l++;
				}
				
				k++;
			}
		
		}
		
		Spherpoint[] center=Cellcomplex.centers(vertex, sidepoint);
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	
	
	
	
	//***********************************************
	// Cellcomplex with just an equatorial x-prism
	//**********************************************
 	public static Cellcomplex[] eqprism(int x)
	{
		Spherpoint[] vertex=new Spherpoint[2*x];
		int[][]edgepoint=new int[3*x][2];
		int[][] sidepointx=new int[2][x],
				sidepoint4=new int[x][4];
		double psi=Math.PI/2-Math.atan(1/Math.sin(Math.PI/x));
		
		for(int i=0;i<x;i++)
		{
			vertex[i]=new Spherpoint(2*Math.PI*i/x,psi,0);
			vertex[i+x]=new Spherpoint(2*Math.PI*i/x,-psi,0);
			
			edgepoint[i][0]=i;
			edgepoint[i][1]=x+i;
			
			int j=(i+1)%x;
			
			edgepoint[x+i][0]=i;
			edgepoint[x+1][1]=j;
			
			edgepoint[2*x+i][0]=x+i;
			edgepoint[2*x+i][1]=x+j;
			
			sidepoint4[i][0]=i;
			sidepoint4[i][1]=j;
			sidepoint4[i][2]=x+j;
			sidepoint4[i][3]=x+i;
			
			sidepointx[0][i]=i;
			sidepointx[1][i]=x+i;
		}
		
		Spherpoint[] centerx=Cellcomplex.centers(vertex, sidepointx),
				center4=Cellcomplex.centers(vertex, sidepoint4);
	return new Cellcomplex[] {new Cellcomplex(vertex,centerx,edgepoint,sidepointx),
							new Cellcomplex(vertex,center4,edgepoint,sidepoint4)};
	}
	
 	public static Cellcomplex[]EqSnubIcosidod()
 	{
 		Spherpoint[]vertex=new Spherpoint[60];
 		int[][] edgepoint=new int[60+90][2],
 				sidepoint5=new int[12][5],
 				sidepoint3=new int[20+2*30][3];
 		double r1=0.20062, r2=0.31359;
 		Spherpoint[]dod=Spherpoint.dodecahedron,
 					ico=Spherpoint.icosahedron;
 		for(int i=0;i<12;i++)
 		{
 			for(int j=0;j<5;j++)
 			{
 				vertex[5*i+j]=dod[dodecahedronSidepoint[i][j]].geodesic(
 						dod[dodecahedronSidepoint[i][(j+1)%5]]).location(r1).geodesic(
 								ico[i]).location(r2);
 				
 				edgepoint[5*i+j][0]=5*i+j;
 				edgepoint[5*i+j][1]=5*i+((j+1)%5);
 				
 				sidepoint5[i][j]=5*i+j;
 			}
 		}
 		int counter=0;
 		for(int k=0;k<20;k++)
 		{
 			for(int l=0;l<3;l++)
 			{
 				int i=icosahedronSidepoint[k][l],m=(l+1)%3,ip=icosahedronEdgepoint[icosahedronSideedge[k][l]][0];
 				if(ip==i)ip=icosahedronEdgepoint[icosahedronSideedge[k][l]][1];
 				
 				edgepoint[60+3*k+l][0]=5*i+dodecahedronVertexnumber[k][l];
 				edgepoint[60+3*k+l][1]=5*ip+dodecahedronVertexnumber[k][m];
 				
 				sidepoint3[k][l]=5*i+dodecahedronVertexnumber[k][l];
 				
 				if(k==dodecahedronEdgepoint[icosahedronSideedge[k][l]][0])
 				{
 					edgepoint[120+counter][0]=5*i+(dodecahedronVertexnumber[k][l]+1)%5;
 					edgepoint[120+counter][1]=5*ip+dodecahedronVertexnumber[k][m];
 					
 					sidepoint3[20+2*counter][0]=5*i+dodecahedronVertexnumber[k][l];
 					sidepoint3[20+2*counter][1]=5*ip+dodecahedronVertexnumber[k][m];
 					sidepoint3[20+2*counter][2]=5*i+(dodecahedronVertexnumber[k][l]+4)%5;//
 					
 					sidepoint3[20+2*counter+1][0]=5*ip+((dodecahedronVertexnumber[k][m]+1)%5);
 					sidepoint3[20+2*counter+1][1]=5*i+(dodecahedronVertexnumber[k][l]+4)%5;//
 					sidepoint3[20+2*counter+1][2]=5*ip+dodecahedronVertexnumber[k][m];
 					
 					counter++;
 				}
 			}
 		}
 		Spherpoint[] center5=Cellcomplex.centers(vertex, sidepoint5),
				center3=Cellcomplex.centers(vertex, sidepoint3);
	return new Cellcomplex[] {new Cellcomplex(vertex,center5,edgepoint,sidepoint5),
							new Cellcomplex(vertex,center3,edgepoint,sidepoint3)};
 	}
 	
	public static Cellcomplex[] prismrings(int x, int y)
	{
		Spherpoint[] vertex=new Spherpoint[x*y];
		int[][] edgepoint=new int[2*x*y][2];
		int[][] sidepointx=new int[y][x],
				sidepointy=new int[x][y],
				sidepoint4=new int[x*y][4];
		double r=horizontal(x,y);//;//edgelength0.7853981595
		double phi;
		Spherline line,meridian=Spherline.polemeridian.copy();
		meridian.transport(1);
		for(int j=0;j<y;j++)
		{
			phi=Math.PI*(2.0/y*(0.5+j)-1);
			line=Spherpoint.eqnorthpole.geodesic(new Spherpoint(phi,Math.PI/2-r,0));
			for(int i=0;i<x;i++)
			{
				vertex[i+x*j]=meridian.transport(line,4*(i+0.5)/x-2).location(1);
				
				edgepoint[i+x*j][0]=i+x*j;
				edgepoint[i+x*j][1]=(i+1)%x+x*j;
				
				edgepoint[x*y+i+x*j][0]=i+x*j;
				edgepoint[x*y+i+x*j][1]=(i+x*(j+1))%(x*y);
				
				sidepointx[j][i]=i+x*j;
				sidepointy[i][j]=i+x*j;
				
				sidepoint4[i+x*j][0]=i+x*j;
				sidepoint4[i+x*j][1]=(i+1)%x+x*j;
				sidepoint4[i+x*j][2]=((i+1)%x+x*(j+1))%(x*y);
				sidepoint4[i+x*j][3]=(i+x*(j+1))%(x*y);
			}
		}
		Spherpoint[] centerx=Cellcomplex.centers(vertex, sidepointx),
					centery=Cellcomplex.centers(vertex, sidepointy),
					center4=Cellcomplex.centers(vertex, sidepoint4);
		return new Cellcomplex[] {new Cellcomplex(vertex,centerx,edgepoint,sidepointx),
								new Cellcomplex(vertex,centery,edgepoint,sidepointy),
								new Cellcomplex(vertex,center4,edgepoint,sidepoint4)};
		
	}
	public static double horizontal(int x, int y)//needed for prismrings
	{
		//Spherpoint up=new Spherpoint(0,0,Math.PI/y);
		double k=0, l=0;
		double opt=Math.PI;
		Spherline line,meridian=Spherline.polemeridian.copy();
		meridian.transport(1);
				for(int i=1;i<10;i++)
				{
					for(double j=k;j<k+2*Math.pow(10, 1-i);j=j+Math.pow(10, -i))
					{
						line=Spherpoint.eqnorthpole.geodesic(new Spherpoint(0,Math.PI/2-j,0));
						Spherpoint vertex=meridian.transport(line,2.0/x).location(1);
						Spherpoint vertex2=vertex.copy();
						vertex2.setPhi(2*Math.PI/y);
						double d=Math.abs(Math.abs(vertex.getTheta())*2-vertex.distance(vertex2));
						if (d<opt)
						{
							opt=d;
							l=j;
						}
						System.out.println("exactness="+opt+", s="+l);
					}
					k=l-Math.pow(10, -i);
				}
					return k;
	}


	//*********************************
	// vertex type hexahedron with 2octahedra and 4tetragonal antiprisms
	//*********************************
	public static Cellcomplex[] coolswirlchoron(int three1, int three2, int revolvings)
	{
		Spherpoint[] vertex=new Spherpoint[three1*three2*(three1+three2)];
		int[][]edgepoint=new int[6+3*18+6*18][2],//3vertex
				sidepoint3=new int[3*3*6*3][3],
				sidepoint4=new int[27][4],//done
				sidepoint31=new int[three1*three2][three1],//done
				sidepoint32=new int[three1*three2][three2];//done
		
		double r=0.3;  //psi=0.77;//To be decided
		Spherpoint center;
		E4 direction;//t0=-1.070484987;phi0=-0.960446702;psi=0.485861527;
	for(int i=0;i<three1*three2;i++)
		{
			//t[1]=5.175783051;t[5]=0.637926995;t[3]=0;
			//Die Ecken der three1-antiprismenentlang des Polehypermeridians
			double position=4.0*i/(three1*three2)+1;//4=full revolution
			center=Spherline.polemeridian.location(position); 
			for(int j=0;j<three1;j++)
			{
				int j1=(j+1)%3-1;
				double angle=2*Math.PI*(j+(i*revolvings)/three2)/three1;
				direction=new E4(Math.cos(angle),Math.sin(angle),0,0);
				vertex[three1*i+j]=//new Spherline(center,direction.transport(position, Spherline.polemeridian)).location(r);
				HopfFibration.hopf(-2*Math.PI*j1/3, psi,Math.PI*(2*i+3*j1)/9);
				//System.out.println("vertex"+(3*i+j)+"=("+(j1/3.0+2.0*i/9)+","+(-2.0*j1/3)+","+(psi)+")");
				edgepoint[three1*i+j]=new int[] {three1*i+j,three1*i+((j+1)%three1)};
				
				edgepoint[three1*three2*(three1+three2)+i*2*three1+2*j]=new int[] {three1*i+j,three1*((i+1)%(three1*three2))+j};
				edgepoint[three1*three2*(three1+three2)+i*2*three1+2*j+1]=new int[] {three1*i+j,three1*((i+1)%(three1*three2))+((j-1)%three1)};

				//edgepoint[3*three1*three2*(three1+three2)+three1*i+j][0]=three1*i+j;
				//edgepoint[three1*three2*(4*three1+3*three2)+three1*i+j][1]=three1*i+j;
				
				sidepoint31[i][j]=three1*i+j;
				
				sidepoint4[3*i+j][0]=three1*(i)+j;//0
				sidepoint4[3*i+j][1]=three1*((i+8)%(three1*three2))+((j+1)%three1);//25
				sidepoint4[3*i+j][2]=27+three1*(((three1)*j+i+1)%(three1*three2))+(three1-j-1)%three1;//32
				sidepoint4[3*i+j][3]=27+three1*(((three1)*j+i)%(three1*three2))+(three1-j)%three1;//27
				
				
				sidepoint3[6*i+2*j][0]=three1*i+j;
				sidepoint3[6*i+2*j][1]=three1*((i+1)%(three1*three2))+j;
				sidepoint3[6*i+2*j][2]=three1*i+((j+1)%three1);
				
				sidepoint3[6*i+2*j+1][0]=three1*i+j;
				sidepoint3[6*i+2*j+1][1]=three1*((i+8)%(three1*three2))+j;
				sidepoint3[6*i+2*j+1][2]=three1*i+((j+2)%three1);
				
				sidepoint3[54+6*i+2*j][0]=27+three1*i+j;
				sidepoint3[54+6*i+2*j][1]=27+three1*((i+1)%(three1*three2))+j;
				sidepoint3[54+6*i+2*j][2]=27+three1*i+((j+1)%three1);
				
				sidepoint3[54+6*i+2*j+1][0]=27+three1*i+j;
				sidepoint3[54+6*i+2*j+1][1]=27+three1*((i-1+9)%(three1*three2))+j;
				sidepoint3[54+6*i+2*j+1][2]=27+three1*i+((j+2)%three1);
				
				sidepoint3[108+6*i+2*j][0]=three1*i+j;
				sidepoint3[108+6*i+2*j][1]=three1*((i+1)%(three1*three2))+(j)%three1;
				sidepoint3[108+6*i+2*j][2]=27+three1*((three1*j+i)%(three1*three2))+(three1-j)%three1;
				
				sidepoint3[108+6*i+2*j+1][0]=three1*((i)%(three1*three2))+j;
				sidepoint3[108+6*i+2*j+1][1]=27+three1*((three1*j+i+8)%(three1*three2))+(three1-j)%three1;
				sidepoint3[108+6*i+2*j+1][2]=27+three1*((three1*j+i)%(three1*three2))+(three1-j)%three1;
			}
			
			//Die Ecken der three2-antiprismen entlang der Equators
			position--;
			center=Spherline.equator.location(position);
			for(int j=0;j<three2;j++)
			{
				int j1=(j+1)%3-1;
				double angle=2*Math.PI*(j+(i*revolvings)/three1)/three2;
				direction=new E4(0,0,Math.cos(angle),-Math.sin(angle));
				vertex[three2*(three1*three1+i)+j]=//new Spherline(center,direction.transport(position, Spherline.equator)).location(r);
HopfFibration.hopf(2*Math.PI*j1/3+phi0, -psi,Math.PI*j1/3+2*Math.PI*i/9+t0/*Math.PI/9*/);
				//System.out.println("vertex"+(27+3*i+j)+"=("+(j1/3.0+2.0*i/9)+","+(2.0*j1/3)+","+(-psi)+")");
				edgepoint[three2*(three1*three1+i)+j]=new int[] {three2*(three1*three1+i)+j,three2*(three1*three1+i)+((j+1)%three2)};
				
				edgepoint[three1*three2*(3*three1+three2)+i*2*three2+2*j]=new int[] {three2*i+j,three2*((i+1)%(three1*three2))+j};//to adjust!(how many edges have we had?
				
				edgepoint[three1*three2*(3*three1+three2)+i*2*three2+2*j+1]=new int[] {three2*i+j,three2*((i+1)%(three1*three2))+((j-1)%three2)};

				sidepoint32[i][j]=three2*(three1*three1+i)+j;
				
				
				
				int jay=i/3, ai=i%3;
			}
		}
		Spherpoint[] center3=Cellcomplex.centers(vertex, sidepoint3),
				center31=Cellcomplex.centers(vertex, sidepoint31),
				center32=Cellcomplex.centers(vertex, sidepoint32),
				center4=Cellcomplex.centers(vertex, sidepoint4);
	return new Cellcomplex[] {new Cellcomplex(vertex,center3,edgepoint,sidepoint3),
							new Cellcomplex(vertex,center31,edgepoint,sidepoint31),
							new Cellcomplex(vertex,center32,edgepoint,sidepoint32),
							new Cellcomplex(vertex,center4,edgepoint,sidepoint4)};
	}
	/*public static Cellcomplex[] swirlchoron(Cellcomplex[] seed, int revolvings)//unfinished
	{
		Spherpoint[] vertex=new Spherpoint[seed[0].vertex.length*revolvings];
		int[][]edgepoint=new int[vertex.length*4][2],//3vertex
				sidepoint3=new int[vertex.length*3][3];
		ArrayList<int[][]>		sidepointn=new ArrayList<int[][]>();
		
		for(int i=0; i<seed.length;i++)
			sidepointn.add(new int[seed[i].sidepoint.length*revolvings][seed[i].sidepoint[0].length]);
		
		for(int i=0; i<seed[0].vertex.length;i++)
		{
			for (int j=0;j<revolvings; j++)
			{
				
			}
		}
	}*/
	
	//*********************************
	// vertex type octahedron with 1 tetrahedron 3 trigonprisms, 2 cubes and 2 m- or n- prisms
	//***********************************
	public static Cellcomplex[] expprismrings(int m, int n)
	{
		Spherpoint[] vertex=new Spherpoint[4*m*n];
		int[][] edgepoint=new int[12*m*n][2],
				sidepointm=new int[2*n][m],
				sidepointn=new int[2*m][n],
				sidepoint3=new int[4*m*n][3],
				sidepoint4=new int[8*m*n][4];
		double r1=horizontal(2*m,n), r2=horizontal(m, 2*n);
		double phi;
		Spherline line,line2,line3,meridian=Spherline.polemeridian.copy();
		meridian.transport(1);
		for(int j=0;j<n;j++)
		{
			phi=Math.PI*(2.0/n*(0.5+j)-1);
			line=Spherpoint.eqnorthpole.geodesic(new Spherpoint(phi, Math.PI/2-r1,0));
			line2=Spherpoint.eqnorthpole.geodesic(new Spherpoint(phi-Math.PI/(2*n), Math.PI/2-r2,0));
			line3=Spherpoint.eqnorthpole.geodesic(new Spherpoint(phi+Math.PI/(2*n),Math.PI/2-r2,0));
			for(int i=0;i<m;i++)
			{
				vertex[4*(i+m*j)]=meridian.transport(line,4*(i+0.25)/m-2).location(1);
				vertex[4*(i+m*j)+1]=meridian.transport(line, 4*(i+0.75)/m-2).location(1);
				vertex[4*(i+m*j)+2]=meridian.transport(line2, 4*(i+0.5)/m-2).location(1);
				vertex[4*(i+m*j)+3]=meridian.transport(line3, 4*(i+0.5)/m-2).location(1);
				int c=0;
				for(int k=0;k<4;k++)
				{
					sidepoint3[4*(i+m*j)+k]=new int[] {(k+1)%4+4*(i+m*j),(k+2)%4+4*(i+m*j),(k+3)%4+4*(i+m*j)};
					
					for(int l=0;l<k;l++)
					{
						edgepoint[6*(i+m*j)]=new int[] {4*(i+m*j)+k,4*(i+m*j)+l};//all 6*m*n tetrahedron edges
						
						c++;
					}
				//Querverbindungen between tetrahedra
				edgepoint[6*m*n+6*(i+m*j)]=new int[] {4*(i+m*j)+1,4*((i+1)%m+m*j)};
				edgepoint[6*m*n+6*(i+m*j)+1]=new int[] {4*(i+m*j)+2,4*((i+1)%m+m*j)+2};
				edgepoint[6*m*n+6*(i+m*j)+2]=new int[] {4*(i+m*j)+3,4*((i+1)%m+m*j)+3};
				edgepoint[6*m*n+6*(i+m*j)+3]=new int[] {4*(i+m*j),4*(i+m*((j+1)%n))};
				edgepoint[6*m*n+6*(i+m*j)+4]=new int[] {4*(i+m*j)+1,4*(i+m*((j+1)%n))+1};
				edgepoint[6*m*n+6*(i+m*j)+5]=new int[] {4*(i+m*j)+3,4*(i+m*((j+1)%n))+2};
				
				sidepointm[2*j][i]=4*(i+m*j)+2;
				sidepointm[2*j+1][i]=4*(i+m*j)+3;
				
				sidepointn[2*i][j]=4*(i+m*j);
				sidepointn[2*i+1][j]=4*(i+m*j)+1;
				
				sidepoint4 [8*(i+m*j)]   = new int[] { 4*(i+m*j) , 4*(i+m*j)+1 , 4*(i+m*((j+1)%n))+1 , 4*(i+m*((j+1)%n)) };
				sidepoint4 [8*(i+m*j)+1] = new int[] { 4*(i+m*j) , 4*(i+m*j)+3 ,4*(i+m*((j+1)%n))+2 , 4*(i+m*((j+1)%n)) };
				sidepoint4 [8*(i+m*j)+2] = new int[] { 4*(i+m*j)+1 , 4*(i+m*j)+3 , 4*(i+m*((j+1)%n))+2 , 4*(i+m*((j+1)%n))+1 };
				sidepoint4 [8*(i+m*j)+3] = new int[] { 4*(i+m*j)+1 , 4*(i+m*j)+2 , 4*((i+1)%m+m*j)+2 , 4*((i+1)%m+m*j) };
				sidepoint4 [8*(i+m*j)+4] = new int[] { 4*(i+m*j)+1 , 4*(i+m*j)+3 , 4*((i+1)%m+m*j)+3 , 4*((i+1)%m+m*j) };
				sidepoint4 [8*(i+m*j)+5] = new int[] { 4*(i+m*j)+2 , 4*(i+m*j)+3 , 4*((i+1)%m+m*j)+3 , 4*((i+1)%m+m*j)+2 };
				sidepoint4 [8*(i+m*j)+6] = new int[] { 4*(i+m*j)+1,4*(i+m*((j+1)%n))+1,4*((i+1)%m+m*((j+1)%n)), 4*((i+1)%m+m*j)};
				sidepoint4 [8*(i+m*j)+7] = new int[] { 4*(i+m*j)+3,4*(i+m*((j+1)%n))+2,4*((i+1)%m+m*((j+1)%n))+2, 4*((i+1)%m+m*j)+3};

				}
			}
		}
		Spherpoint[] centerm=Cellcomplex.centers(vertex, sidepointm),
				centern=Cellcomplex.centers(vertex, sidepointn),
				center4=Cellcomplex.centers(vertex, sidepoint4),
				center3=Cellcomplex.centers(vertex, sidepoint3);
	return new Cellcomplex[] {new Cellcomplex(vertex,centerm,edgepoint,sidepointm),
							new Cellcomplex(vertex,centern,edgepoint,sidepointn),
							new Cellcomplex(vertex,center4,edgepoint,sidepoint4),
							new Cellcomplex(vertex,center3,edgepoint,sidepoint3)};
	}
	
	//***************************************************
	//Cellcomplex with dice-vertex type and tetrahedra touching 3-prisms
	//****************************************************
	public static Cellcomplex[] decaIcosachoron()
	{
		double theta0=0.91;
		Spherpoint[] vertex=new Spherpoint[20];
		int[][]edgepoint=new int[60][2];
		int[][]sidepoint3=new int[40][3];
		int[][]sidepoint4=new int[30][4];
		Cellcomplex[]middle=octahexa();
		int l=0;
		for (int i=0;i<4;i++)
		{
			vertex[i]=Spherpoint.tetrahedron[i];
			vertex[i].setTheta(theta0);
			
			vertex[i+16]=vertex[i].getAntipodal();
			
			sidepoint3[i]=tetrahedronSidepoint[i];
			
			for(int j=0;j<3;j++)
			{
				sidepoint3[i+36][j]=tetrahedronSidepoint[i][j]+16;
				
				sidepoint3[3*i+j+4][0]=i;
				sidepoint3[i*3+j+4][1]=4+thovn[i][j];
				sidepoint3[i*3+j+4][2]=4+thovn[i][(j+1)%3];
				
				sidepoint3[3*i+j+24][0]=16+i;
				sidepoint3[3*i+j+24][1]=4+rthovn[i][j];
				sidepoint3[3*i+j+24][2]=4+rthovn[i][(j+1)%3];
			}
			
			for(int j=i+1;j<4;j++)
			{
				
				for(int k=0;k<4;k++)
				{
					if(k!=i&&k!=j)
					{
						sidepoint4[l][0]=i;
						sidepoint4[l][1]=j;
						sidepoint4[l][2]=commonEntry(thovn[j],rthovn[k])+4;
						sidepoint4[l][3]=commonEntry(thovn[i],rthovn[k])+4;
						
						sidepoint4[l+18][0]=i+16;
						sidepoint4[l+18][1]=j+16;
						sidepoint4[l+18][2]=commonEntry(rthovn[j],thovn[k])+4;
						sidepoint4[l+18][3]=commonEntry(rthovn[i],thovn[k])+4;
						
						l++;
					}
				}
			}
		}
		for(int i=0;i<6;i++)
		{
			edgepoint[i]=tetrahedronEdgepoint[i];
			edgepoint[i+54][0]=tetrahedronEdgepoint[i][0]+16;
			edgepoint[i+54][1]=tetrahedronEdgepoint[i][1]+16;
			
			sidepoint4[i+12]=middle[1].sidepoint[i];
			
			for(int j=0;j<4;j++)
				sidepoint4[i+12][j]+=4;
		}
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<3;j++)
			sidepoint3[i+16][j]=middle[0].sidepoint[i][j]+4;
		}
		for(int i=0;i<12;i++)
		{
			vertex[i+4]=middle[0].vertex[i];
			
			edgepoint[i+6][0]=hexaoctaTetraVertexnumber[i];
			edgepoint[i+6][1]=i+4;
			
			edgepoint[i+42][0]=revHotVn[i];
			edgepoint[i+42][1]=i+4;
		}
		for(int i=0;i<24;i++)
		{
			edgepoint[i+18]=middle[0].edgepoint[i];
			
		
			
			
		}
		
		Spherpoint[]center3=Cellcomplex.centers(vertex, sidepoint3),
				center4=Cellcomplex.centers(vertex, sidepoint4);
		return new Cellcomplex[] {new Cellcomplex(vertex,center3,edgepoint,sidepoint3),
								new Cellcomplex(vertex,center4,edgepoint,sidepoint4)};
		
	}
	

	
	private static int commonEntry(int[] is, int[] is2) 
	{
		for(int i=0;i<is.length;i++)
		{
			for(int j=0;j<is2.length;j++)
			{
				if(is[i]==is2[j])
					return is[i];
			}
		}
		return 1000;
	}
	public static Cellcomplex[] tetraprism()
	{
		Spherpoint[] vertex=new Spherpoint[8];
		int[][] edgepoint=new int[16][2];
		int[][]sidepoint3=new int[8][3],
				sidepoint4=new int[6][4];
		double theta=0.68472;//todo
		for(int i=0;i<4;i++)
		{
			vertex[i]=Spherpoint.tetrahedron[i].copy();
			vertex[i].setTheta(theta);
			vertex[i+4]=vertex[i].copy();
			vertex[i+4].setTheta(-theta);
			
			edgepoint[i+6][0]=i;
			edgepoint[i+6][1]=i+4;
		
			for(int j=0;j<3;j++)
			{
				sidepoint3[i][j]=tetrahedronSidepoint[i][j];
				sidepoint3[i+4][j]=tetrahedronSidepoint[i][j]+4;
			}
		
		}
		for(int i=0;i<6;i++)
		{
			for(int j=0;j<2;j++)
			{
				edgepoint[i][j]=tetrahedronEdgepoint[i][j];
				edgepoint[i+10][j]=tetrahedronEdgepoint[i][j]+4;
				
				sidepoint4[i][j]=tetrahedronEdgepoint[i][j];
				sidepoint4[i][3-j]=tetrahedronEdgepoint[i][j]+4;
			}
		}
		Spherpoint[] center3=Cellcomplex.centers(vertex,  sidepoint3),
				center4=Cellcomplex.centers(vertex, sidepoint4);
		
		return new Cellcomplex[] {new Cellcomplex(vertex,center3,edgepoint,sidepoint3),
				new Cellcomplex(vertex,center4,edgepoint,sidepoint4)};
		
	}
	
	public static Cellcomplex[] dodecaprism()
	{
		Spherpoint[] vertex=new Spherpoint[40];
		int[][] edgepoint=new int[80][2],
				sidepoint5=new int[24][5],
				sidepoint4=new int[30][4];
		double theta=0.3427395;//todo
		for(int i=0;i<20;i++)
		{
			vertex[i]=Spherpoint.dodecahedron[i].copy();
			vertex[i].setTheta(theta);
			vertex[i+20]=Spherpoint.dodecahedron[i].copy();
			vertex[i+20].setTheta(-theta);
			
			edgepoint[i+30][0]=i;
			edgepoint[i+30][1]=i+20;
		}
		for(int i=0;i<12;i++)
		{
			for(int j=0;j<5;j++)
			{
				sidepoint5[i][j]=dodecahedronSidepoint[i][j];
				sidepoint5[i+12][j]=dodecahedronSidepoint[i][j]+20;
			}
			
		}
		for(int i=0;i<30;i++)
		{
			for(int j=0;j<2;j++)
			{
				edgepoint[i][j]=dodecahedronEdgepoint[i][j];
				edgepoint[i+50][j]=dodecahedronEdgepoint[i][j]+20;
				
				sidepoint4[i][j]=dodecahedronEdgepoint[i][j];
				sidepoint4[i][3-j]=dodecahedronEdgepoint[i][j]+20;
			}
		}
		
		Spherpoint[] center5=Cellcomplex.centers(vertex,sidepoint5),
					center4=Cellcomplex.centers(vertex, sidepoint4);
		System.out.println("horizontal: "+vertex[0].distance(vertex[1])+", vertical: "+vertex[0].distance(vertex[20]));
		
		
		return new Cellcomplex[] {new Cellcomplex(vertex,center5,edgepoint,sidepoint5),
								new Cellcomplex(vertex,center4, edgepoint,sidepoint4)};
		
		
		
		
	}
	
	//*************************************************************************************************
	// Rectified: Rectifie original Cellcomplex. (Cut off vertices up to edgemiddle) Adjust for nin-tetrahedral vertexfigures
	//************************************************************************************************
	public static Cellcomplex[] rect(Cellcomplex original, int edgespervertex)
	{	int cellspervertex=4, facespervertex=6;//adjust for non-tetrahedral vertexfigures
		Spherpoint[] vertex=new Spherpoint[original.edgepoint.length];
		int[][] edgepoint=new int[original.sidepoint.length*original.sidepoint[0].length][2],
				sidepointf=new int[original.sidepoint.length][original.sidepoint[0].length], 
				sidepointv=new int[original.vertex.length*cellspervertex][3];
		
		int[][][] pointedge=pointedge(original,edgespervertex);
		int[][][] sideedge=sideedge(original);

		Spherline line;
		

		for(int i=0;i<original.edgepoint.length;i++)
		{
			line=original.vertex[original.edgepoint[i][0]].geodesic(original.vertex[original.edgepoint[i][1]]);
			vertex[i]=line.location(0.5);
			
			
		}
		
		for(int i=0;i<original.sidepoint.length;i++)
		{
			for(int j=0;j<original.sidepoint[0].length;j++)
			{
				int k=(j+1)%original.sidepoint[0].length;
				edgepoint[i*original.sidepoint[0].length+j]=new int[] {sideedge[i][j][0],sideedge[i][k][0]};
				sidepointf[i][j]=sideedge[i][j][0];
			}
		}
		for(int i=0;i<original.vertex.length;i++)//To be adapted for non-tetrahedralvertex figures
		{
			int m=0;
			for(int j=0;j<2;j++)
			{
				for(int k=j+1;k<3;k++)
				{
					for(int l=k+1;l<4;l++)
					{
						sidepointv[i*cellspervertex+m]=new int[] {pointedge[i][j][0],pointedge[i][k][0],pointedge[i][l][0]};
						m++;
					}
				}
			}
		}
		Spherpoint[] centerf=Cellcomplex.centers(vertex,sidepointf),
				centerv=Cellcomplex.centers(vertex, sidepointv);
	
	return new Cellcomplex[] {new Cellcomplex(vertex,centerf,edgepoint,sidepointf),
							new Cellcomplex(vertex,centerv, edgepoint,sidepointv)};
	}
	
	//*************************************************************************
	// Truncated : Truncate original cellcomplex. Need adjustement for non tetrahedral vertexfigures
	//*************************************************************************
	public static Cellcomplex[] trunc(Cellcomplex original, int edgespervertex)
	{
		double setin=0.27714;//Experimentally find original=(3,3,3):0.27714
		Spherpoint[] vertex=new Spherpoint[2*original.edgepoint.length];
		int[][] edgepoint=new int[original.sidepoint.length*original.sidepoint[0].length+original.edgepoint.length][2],
				sidepointf=new int[original.sidepoint.length][2*original.sidepoint[0].length], 
				sidepointv=new int[original.vertex.length*4][3];
		
		Spherline line;

		int[][][] pointedge=pointedge(original,edgespervertex);
		
		for(int i=0;i<original.edgepoint.length;i++)
		{
			line=original.vertex[original.edgepoint[i][0]].geodesic(original.vertex[original.edgepoint[i][1]]);
			vertex[2*i]=line.location(setin);
			vertex[2*i+1]=line.location(1-setin);
			
			 edgepoint[i][0]=2*i;
			 edgepoint[i][1]=2*i+1;
		}
		for(int i=0;i<original.vertex.length;i++)
		{
			int l=0;
			for(int j=0;j<edgespervertex;j++)
			{
				for(int k=j+1;k<edgespervertex;k++)
				{
					edgepoint[vertex.length/2+binomcoeff(edgespervertex,2)*i+l][0]=2*pointedge[i][j][0]+pointedge[i][j][1];
					edgepoint[vertex.length/2+binomcoeff(edgespervertex,2)*i+l][1]=2*pointedge[i][k][0]+pointedge[i][k][1];
					l++;
				}
				
				for(int m=0;m<edgespervertex-1;m++)
				{
					int n=(j+m)%edgespervertex;
				
					sidepointv[4*i+j][m]=2*pointedge[i][n][0]+pointedge[i][n][1];
				}
			}
		}
		for(int i=0;i<original.sidepoint.length;i++)
		{
			for(int j=0;j<original.sidepoint[0].length;j++)
			{
				int k=(j+1)%original.sidepoint[0].length;
				boolean foundedge=false; int l=0;
				
				while(!foundedge)
				{
					 System.out.println("sidepoint[i][j]="+original.sidepoint[i][j]+",sidepoit[i][k]="+original.sidepoint[i][k]);
					 if(original.sidepoint[i][k]==original.edgepoint[pointedge[original.sidepoint[i][j]][l][0]][1-pointedge[original.sidepoint[i][j]][l][1]])
					 {
						 foundedge=true;
					 }
					 else
						 l++;
				}
				
				sidepointf[i][2*j]=2*pointedge[original.sidepoint[i][j]][l][0]+pointedge[original.sidepoint[i][j]][l][1];
				 sidepointf[i][2*j+1]=2*pointedge[original.sidepoint[i][j]][l][0]+1-pointedge[original.sidepoint[i][j]][l][1];//edgepoint[pointedge[[i][l][0]]]{[pointedge[i][l]]
			
			}
		}
		 System.out.println("vertexnedge="+vertex[sidepointv[0][1]].distance(vertex[sidepointv[0][0]]));
		 System.out.println("faceedge="+vertex[sidepointf[0][0]].distance(vertex[sidepointf[0][1]]));
		 Spherpoint[] centerf=Cellcomplex.centers(vertex,sidepointf),
					centerv=Cellcomplex.centers(vertex, sidepointv);
		 
		 return new Cellcomplex[] {new Cellcomplex(vertex,centerf,edgepoint,sidepointf),
				 new Cellcomplex(vertex,centerv,edgepoint,sidepointv)};
				 
		
	}
	
	
	//***********************************************************************
	//Truncated (5,3,3) - Tetrahedra and truncated Dodecahedra
	//*************************************************************************
	public static Cellcomplex[] trunc533()
	{
		double setin=0.2752934213;
		Spherpoint[] vertex=new Spherpoint[2400];
		int[][] edgepoint=new int[4800][2],
				sidepoint10=new int[720][10], sidepoint3=new int[2400][3];
		
		 Spherline line;
		 Cellcomplex t533=tiling533();
		 int[][][] pointedge=pointedge(t533,4);
		 for(int i=0;i<1200;i++)
		 {
			 line=t533.vertex[t533.edgepoint[i][0]].geodesic(t533.vertex[t533.edgepoint[i][1]]);
			 vertex[2*i]=line.location(setin);
			 vertex[2*i+1]=line.location(1-setin);
			 
			 edgepoint[i][0]=2*i;
			 edgepoint[i][1]=2*i+1;
			 
			
		 }
		 for(int i=0;i<600;i++)
		 {
			 int l=0;
			 for (int j=0;j<4;j++)
			 {
				 System.out.println("pointedge["+i+"]["+j+"]=("+pointedge[i][j][0]+","+pointedge[i][j][1]+")");
				 for(int k=j+1;k<4;k++)
				 {
					 edgepoint[1200+6*i+l][0]=2*pointedge[i][j][0]+pointedge[i][j][1];
					 edgepoint[1200+6*i+l][1]=2*pointedge[i][k][0]+pointedge[i][k][1];
							 l++;
				 }
				 int m=(j+1) %4,n=(j+2)%4;
				 sidepoint3[4*i+j]=new int[] {2*pointedge[i][j][0]+pointedge[i][j][1],2*pointedge[i][m][0]+pointedge[i][m][1],2*pointedge[i][n][0]+pointedge[i][n][1]};
			 }
		 }
		 for(int i=0;i<720;i++)
		 {
			 for(int j=0;j<5;j++)
			 {
				int k=(j+1)%5;
				 boolean foundedge=false;int l=0;
				 while(!foundedge)
				 {
					 System.out.println("sidepoint[i][j]="+t533.sidepoint[i][j]+",sidepoit[i][k]="+t533.sidepoint[i][k]);
					 if(t533.sidepoint[i][k]==t533.edgepoint[pointedge[t533.sidepoint[i][j]][l][0]][1-pointedge[t533.sidepoint[i][j]][l][1]])
					 {
						 foundedge=true;
					 }
					 else
						 l++;
				 }
				 sidepoint10[i][2*j]=2*pointedge[t533.sidepoint[i][j]][l][0]+pointedge[t533.sidepoint[i][j]][l][1];
				 sidepoint10[i][2*j+1]=2*pointedge[t533.sidepoint[i][j]][l][0]+1-pointedge[t533.sidepoint[i][j]][l][1];//edgepoint[pointedge[[i][l][0]]]{[pointedge[i][l]]
			 }
		 }
		 System.out.println("Trigonedge="+vertex[sidepoint3[0][1]].distance(vertex[sidepoint3[0][0]]));
		 System.out.println("Decagonedge="+vertex[sidepoint10[0][0]].distance(vertex[sidepoint10[0][1]]));
		 Spherpoint[] center10=Cellcomplex.centers(vertex,sidepoint10),
					center3=Cellcomplex.centers(vertex, sidepoint3);
		 
		 return new Cellcomplex[] {new Cellcomplex(vertex,center10,edgepoint,sidepoint10),
				 new Cellcomplex(vertex,center3,edgepoint,sidepoint3)};
				 
		 
	}
	//*******************************************************************************************
	// Archimedean with tetreahedral vf 388388 consisting of truncated Hexahedra
	//*******************************************************************************************
	public static Cellcomplex[] cont()
	{
		int[][] edgepoint=new int[576][2],sidepoint3=new int[192][3],sidepoint8=new int[144][8];
		Spherpoint[]vertex= new Spherpoint[288], cellcenter=new Spherpoint[48];
		double theta1=Math.PI/2-.548,theta2=0.7854,theta3=.524,theta4=Math.PI/4,theta5=.5, b=0.154, a=0.3;
		Cellcomplex[] tic=eqTruncHexahedron();
		//int[][][]ose=sideedge(schäffel342());
		
		cellcenter[0]=Spherpoint.northpole.copy();
		cellcenter[47]=Spherpoint.southpole.copy();
		for(int i=0;i<36;i++)
		{
			edgepoint[i]=tic[0].edgepoint[i];
			
			for(int j=0;j<2;j++)
			edgepoint[540+i][j]=edgepoint[i][j]+264;
			
		}
		for(int i=0; i<24;i++)
		{
			vertex[i]=tic[0].vertex[i].copy();
			vertex[i].setTheta(theta1);
			
			vertex[264+i]=vertex[i].copy();
			vertex[264+i].setTheta(-theta1);
		}
		for(int i=0;i<12;i++)
		{
			vertex[i+24]=Spherpoint.hexahedronEdge[i].copy();
			vertex[i+24].setTheta(theta2);
			System.out.println(vertex[i+24].distance(vertex[3*hexahedronEdgepoint[i][0]]));
			vertex[252+i]=vertex[i+24].copy();
			vertex[252+i].setTheta(-theta2);
			
			sidepoint3[i+8][2]=i+24;
			sidepoint3[i+172][2]=i+252;
			
			sidepoint3[i+20][2]=i+24;
			sidepoint3[i+160][2]=i+252;
			
			for(int j=0;j<2;j++)
			{
				vertex[2*i+j+36]=vertex[i+24].geodesic(Spherpoint.octahedron[octahedronEdgepoint[i][j]]).location(b);
				vertex[2*i+j+36].setTheta(theta3);
				vertex[2*i+j+228]=vertex[2*i+j+36].copy();
				vertex[2*i+j+228].setTheta(-theta3);
				
				edgepoint[2*i+j+60][0]=i+24;
				edgepoint[2*i+j+60][1]=2*i+j+36;
				edgepoint[2*i+j+492][0]=i+252;
				edgepoint[2*i+j+492][1]=2*i+j+228;
				
				edgepoint[i+84][j]=2*i+j+36;
				edgepoint[i+480][j]=2*i+j+228;
				
				sidepoint3[i+20][j]=2*i+j+36;
				sidepoint3[i+160][j]=2*i+j+228;
				
				sidepoint3[2*i+j+32][2]=2*i+j+36;
				sidepoint3[2*i+j+136][2]=2*i+j+228;
				
				int i0=hexahedronEdgepoint[i][j],k=0;
				while(octahedronSideedge[i0][k]!=i)k++;
				
				sidepoint3[i+8][j]=3*i0+k;
				sidepoint3[i+172][j]=3*i0+k+264;
				
				edgepoint[2*i+j+36][0]=i+24;
				edgepoint[2*i+j+36][1]=3*i0+k;
				edgepoint[2*i+j+516][0]=i+252;
				edgepoint[2*i+j+516][1]=3*i0+k+264;
				
				for(int l=0;l<2;l++)
				{
					int m=l; if (octahedronEdgepoint[i0][l]!=octahedronSidepoint[i0][(l+k)%3])m=1-l;
					
					edgepoint[96+4*i+2*j+l][0]=2*i+l+36;
					edgepoint[96+4*i+2*j+l][1]=2*(3*i0+k)+l+60;
					edgepoint[432+4*i+2*j+l][0]=2*i+l+228;
					edgepoint[432+4*i+2*j+l][1]=2*(3*i0+k)+l+180;
					
					edgepoint[144+2*i+l][j]=2*(3*i0+k)+l+60;
					edgepoint[408+2*i+l][j]=2*(3*i0+k)+l+180;
				
					sidepoint3[2*i+l+32][j]=2*(3*i0+k)+l+60;
					sidepoint3[2*i+l+136][j]=2*(3*i0+k)+l+180;
				}
				
			}
		}
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<3;j++)
			{
				sidepoint3[i][j]=tic[0].sidepoint[i][j];
				sidepoint3[i+184][j]=tic[0].sidepoint[i][j]+264;
				
				for(int k=0;k<2;k++)
				{
					vertex[2*(3*i+j)+k+60]=tic[0].vertex[3*i+j].geodesic(Spherpoint.octahedron[octahedronEdgepoint[octahedronSideedge[i][j]][k]]).location(a);
					vertex[2*(3*i+j)+k+60].setTheta(theta5);
					vertex[2*(3*i+j)+k+180]=vertex[2*(3*i+j)+k+60].copy();
					vertex[2*(3*i+j)+k+180].setTheta(-theta5);
				//	System.out.println("i="+i+", j="+j+", k="+k+": "+octahedronEdgepoint[octahedronSideedge[i][(j+k)%3]][0]+"=?="+octahedronSidepoint[i][(j+1)%3]+octahedronEdgepoint[octahedronSideedge[i][(j+k)%3]][1]);
					int l=0;if(octahedronEdgepoint[octahedronSideedge[i][(j+k)%3]][l]!=octahedronSidepoint[i][(j+1)%3])l=1;System.out.println(l);
					
					sidepoint8[6+3*i+j][k]=3*i+(j+k)%3;
					sidepoint8[6+3*i+j][7-k*5]=24+octahedronSideedge[i][(j+k)%3];
					sidepoint8[6+3*i+j][6-3*k]=36+2*octahedronSideedge[i][(j+k)%3]+l;
					sidepoint8[6+3*i+j][5-k]=60+2*(3*i+(j+k)%3)+l;
					sidepoint8[114+3*i+j][k]=264+3*i+(j+k)%3;
					sidepoint8[114+3*i+j][7-k*5]=252+octahedronSideedge[i][(j+k)%3];
					sidepoint8[114+3*i+j][6-3*k]=228+2*octahedronSideedge[i][(j+k)%3]+l;
					sidepoint8[114+3*i+j][5-k]=180+2*(3*i+(j+k)%3)+l;
				}
			}
		}
		for(int i=0;i<6;i++)
		{
			cellcenter[i+1]=Spherpoint.octahedron[i].copy();
			cellcenter[i+1].setTheta(theta4);
			cellcenter[i+41]=cellcenter[i+1].copy();
			cellcenter[i+41].setTheta(-theta4);
			//int k=0;while(octahedronSidepoint[hexahedronSidepoint[i][0]][k]!=i)k++;
			//System.out.println(cellcenter[i+1].distance(vertex[3*hexahedronSidepoint[i][0]+k]));
			for(int j=0;j<8;j++)
			{
				sidepoint8[i][j]=tic[1].sidepoint[i][j];
				sidepoint8[i+138][j]=tic[1].sidepoint[i][j]+264;
				
				//sidepoint8[i+30]
			}
		}
		for(int i=0;i<84;i++)
		{
			System.out.print(i+":");vertex[i].println();
		}
		Spherpoint[] center3=Cellcomplex.centers(vertex, sidepoint3),
				center8=Cellcomplex.centers(vertex, sidepoint8);
		return new Cellcomplex[] {new Cellcomplex(vertex,center3,edgepoint,sidepoint3),
				new Cellcomplex(vertex,center8,edgepoint,sidepoint8)};
	}
	public static Cellcomplex[] eqTruncHexahedron()
	{
		int[][] edgepoint=new int[12+24][2], sidepoint3=new int[8][3],sidepoint8=new int[6][8];
		Spherpoint[] vertex=new Spherpoint[24];
		
		Cellcomplex hexa=schäffel432();
		
		double a=0.26854;int k=0;
		for(int i=0;i<8;i++)
			for(int j=0;j<3;j++)
			{
				k=hexahedronEdgepoint[octahedronSideedge[i][j]][0];
				if(k==i)k=hexahedronEdgepoint[octahedronSideedge[i][j]][1];
				vertex[3*i+j]=hexa.vertex[i].geodesic(hexa.vertex[k]).location(a);
				//System.out.print("Vertex nr "+(4*i+j));
				vertex[3*i+j].println();
				edgepoint[3*i+j][0]=3*i+j;
				edgepoint[3*i+j][1]=3*i+(j+1)%3;
				
				
				sidepoint3[i][j]=3*i+j;
			}
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<3;j++)
			{
				int io=octahedronSidepoint[i][j],im=octahedronSidepoint[i][(j+1)%3],jo=hexahedronVertexnumber[i][j],jm=hexahedronVertexnumber[i][(j+1)%3];
				sidepoint8[io][2*jo]=3*i+j;
				sidepoint8[im][2*jm+1]=3*i+j;
			}
		}
		int[][][]se=sideedge(schäffel342());
		for(int i=0;i<6;i++)
			for(int j=0;j<2;j++)
				edgepoint[24+i][j]=3*hexahedronEdgepoint[i][j]+se[i][j][0];
		Spherpoint[] center3=Cellcomplex.centers(vertex, sidepoint3),
				center8=Cellcomplex.centers(vertex,sidepoint8);
		
		return new Cellcomplex[] {new Cellcomplex(vertex,center3,edgepoint,sidepoint3),new Cellcomplex(vertex,center8,edgepoint,sidepoint8)};

	}
	public static Cellcomplex[] eqTruncOctahedron()
	{
		int[][] edgepoint=new int[12+24][2], sidepoint4=new int[6][4],sidepoint6=new int[8][6];
		Spherpoint[] vertex=new Spherpoint[24];
		
		Cellcomplex octa=schäffel342();
		
		double a=0.295167;int k=0;
		for(int i=0;i<6;i++)
			for(int j=0;j<4;j++)
			{
				k=octahedronEdgepoint[hexahedronSideedge[i][j]][0];
				if(k==i)k=octahedronEdgepoint[hexahedronSideedge[i][j]][1];
				vertex[4*i+j]=octa.vertex[i].geodesic(octa.vertex[k]).location(a);
				//System.out.print("Vertex nr "+(4*i+j));
				vertex[4*i+j].println();
				edgepoint[4*i+j][0]=4*i+j;
				edgepoint[4*i+j][1]=4*i+(j+1)%4;
				
				
				sidepoint4[i][j]=4*i+j;
			}
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<3;j++)
			{
				int io=octahedronSidepoint[i][j],jo=hexahedronVertexnumber[i][j];
				sidepoint6[i][2*j]=4*io+jo;
				sidepoint6[i][2*j+1]=4*io+(jo+3)%4;
			}
		}
		int[][][]se=sideedge(schäffel432());
		for(int i=0;i<8;i++)
			for(int j=0;j<2;j++)
				edgepoint[24+i][j]=4*octahedronEdgepoint[i][j]+se[i][j][0];
		Spherpoint[] center4=Cellcomplex.centers(vertex, sidepoint4),
				center6=Cellcomplex.centers(vertex,sidepoint6);
		
		return new Cellcomplex[] {new Cellcomplex(vertex,center4,edgepoint,sidepoint4),new Cellcomplex(vertex,center6,edgepoint,sidepoint6)};

	}
	public static Cellcomplex[] eqTruncTetrahedron()
	{
		Spherpoint[] vertex=new Spherpoint[12];
		int[][] edgepoint=new int[18][2],sidepoint3=new int[4][3],sidepoint6= {{0,1,7,8,3,5},{1,2,9,11,6,7},{0,2,9,10,4,5},{3,4,10,11,6,8}};
		
		Cellcomplex tetra=schäffel332();
		
		double a=0.26944;
		int k=0;
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<3;j++)
			{
				k=(i+j+1)%4;
				
				vertex[3*i+j]=tetra.vertex[i].geodesic(tetra.vertex[k]).location(a);
				
				edgepoint[3*i+j][0]=3*i+j;
				edgepoint[3*i+j][1]=3*i+(j+1)%3;
				
				sidepoint3[i][j]=3*i+j;
			}	
		}
		Spherpoint[] center3=Cellcomplex.centers(vertex, sidepoint3),
				center6=Cellcomplex.centers(vertex,sidepoint6);
		
		return new Cellcomplex[] {new Cellcomplex(vertex,center3,edgepoint,sidepoint3),new Cellcomplex(vertex,center6,edgepoint,sidepoint6)};
	}
	
	public static Cellcomplex[] eqTruncOctaHexahedron()
	{
		Spherpoint vertex[]=new Spherpoint[48];
		int[][] edgepoint=new int[72][2], sidepoint4=new int[12][4], sidepoint6=new int[8][6],sidepoint8=new int[6][8];
		
		double halfside=Math.PI/16+0.0211, psi1=Math.acos(Math.sin(halfside)*Math.sqrt(4+2*Math.sqrt(2))),psi2=Math.asin(Math.sin(halfside)*(Math.sqrt(2)+1)),psi3=halfside, 
				offset1=Math.asin(Math.tan(psi2)*(Math.sqrt(2)-1)),offset2=Math.asin(Math.tan(halfside)*(Math.sqrt(2)+1));
		System.out.println("psi="+psi1+" and "+psi2 +" and "+psi3+", offset="+offset1+" and "+offset2);
		for(int i=0;i<8;i++)
		{
			vertex[i]=new Spherpoint(2*Math.PI*(i-0.5)/8,psi1,0);
			vertex[40+i]=new Spherpoint(2*Math.PI*(i-0.5)/8,-psi1,0);
			
			sidepoint8[0][i]=i;
			sidepoint8[5][i]=40+i;
			
			sidepoint4[i/2][i%2]=i;
			sidepoint4[8+i/2][i%2]=40+i;
			
			int j=(i+1)%8;
			sidepoint6[i/2][i%2]=j;
			sidepoint6[4+i/2][i%2]=40+j;
			
			edgepoint[i][0]=i;
			edgepoint[i][1]=j;
			edgepoint[64+i][0]=40+i;
			edgepoint[64+i][1]=40+j;
			
			edgepoint[i+8][0]=i;
			edgepoint[i+8][1]=i+8;
			edgepoint[i+56][0]=40+i;
			edgepoint[i+56][1]=32+i;
			
			edgepoint[i+20][0]=8+i;
			edgepoint[i+44][0]=32+i;
			
			edgepoint[i+20][1]=16+i;
			edgepoint[i+44][1]=24+i;
			
			edgepoint[j/2+28][j%2]=16+i;
			edgepoint[j/2+40][j%2]=24+i;
			
			edgepoint[i+32][0]=16+i;
			edgepoint[i+32][1]=24+i;
		}
		
		for(int i=0;i<4;i++)
		{
			vertex[8+2*i]=new Spherpoint((i)*Math.PI/2-offset1,psi2,0);
			vertex[8+2*i+1]=new Spherpoint((i)*Math.PI/2+offset1,psi2,0);
			
			vertex[32+2*i]=new Spherpoint((i)*Math.PI/2-offset1,-psi2,0);
			vertex[32+2*i+1]=new Spherpoint((i)*Math.PI/2+offset1,-psi2,0);
			
			vertex[16+2*i]=new Spherpoint(i*Math.PI/2-offset2,psi3,0);
			vertex[16+2*i+1]=new Spherpoint(i*Math.PI/2+offset2,psi3,0);
			
			vertex[24+2*i]=new Spherpoint(i*Math.PI/2-offset2,-psi3,0);
			vertex[24+2*i+1]=new Spherpoint(i*Math.PI/2+offset2,-psi3,0);
			
			sidepoint8[i+1][0]=8+2*i;
			sidepoint8[i+1][1]=8+2*i+1;
			sidepoint8[i+1][4]=32+2*i+1;
			sidepoint8[i+1][5]=32+2*i;
			
			sidepoint8[i+1][2]=16+2*i+1;
			sidepoint8[i+1][7]=16+2*i;
			sidepoint8[i+1][3]=24+2*i+1;
			sidepoint8[i+1][6]=24+2*i;
			
			sidepoint4[i][2]=8+2*i+1;
			sidepoint4[i][3]=8+2*i;
			sidepoint4[8+i][2]=32+2*i+1;
			sidepoint4[8+i][3]=32+2*i;
			
			int j=(i+1)%4;
			sidepoint4[i+4][0]=16+2*i+1;
			sidepoint4[i+4][1]=16+2*j;
			sidepoint4[i+4][2]=24+2*j;
			sidepoint4[i+4][3]=24+2*i+1;
			
			sidepoint6[i][5]=8+2*i+1;
			sidepoint6[i][2]=8+2*j;
			sidepoint6[4+i][5]=32+2*i+1;
			sidepoint6[4+i][2]=32+2*j;
			
			sidepoint6[i][4]=16+2*i+1;
			sidepoint6[i][3]=16+2*j;
			sidepoint6[4+i][4]=24+2*i+1;
			sidepoint6[4+i][3]=24+2*j;
			
			edgepoint[i+16][0]=8+2*i;
			edgepoint[i+16][1]=8+2*i+1;
			edgepoint[i+48][0]=32+2*i;
			edgepoint[i+48][1]=32+2*i+1;
		}
		
		 System.out.println("Octagonedge="+vertex[sidepoint8[0][1]].distance(vertex[sidepoint8[0][0]]));
		 System.out.println("Hexagonedge="+vertex[sidepoint6[0][1]].distance(vertex[sidepoint6[0][2]]));
		 System.out.println("Tetragonedge="+vertex[sidepoint4[0][3]].distance(vertex[sidepoint4[0][2]]));
		 Spherpoint[] center8=Cellcomplex.centers(vertex,sidepoint8),
					center6=Cellcomplex.centers(vertex, sidepoint6),
					center4=Cellcomplex.centers(vertex, sidepoint4);;
		 
		 return new Cellcomplex[] {new Cellcomplex(vertex,center8,edgepoint,sidepoint8),
				 new Cellcomplex(vertex,center6,edgepoint,sidepoint6),new Cellcomplex(vertex,center4,edgepoint,sidepoint4)};
		
	}
	
	//The dual to the runcinated Pentachoron
	public static Cellcomplex dualSpid()
	{
		Cellcomplex pent=pentachoron();
		Spherpoint[] edgemiddles=pent.edgemiddles();
		int[][][]pointedge=pointedge(pent,4);
		Spherpoint[] vertex=new Spherpoint[30];
		int[][] sidepoint=new int[60][4],edgepoint=new int[70][2];
		for(int i=0;i<5;i++)
		{
			vertex[i]=pent.vertex[i];
			vertex[25+i]=pent.vertex[i].getAntipodal();
			int l=0;
			for(int j=0;j<4;j++)
			{
				for(int k=0;k<j;k++)
				{
					int m=0;
					while(commonEntry(pent.edgepoint[m],pent.edgepoint[pointedge[i][j][0]])!=1000 || commonEntry(pent.edgepoint[m],pent.edgepoint[pointedge[i][k][0]])!=1000)
							m++;
					sidepoint[6*i+l][0]=i;
					sidepoint[6*i+l][1]=pointedge[i][j][0]+5;
					sidepoint[6*i+l][2]=m+15;
					sidepoint[6*i+l][3]=pointedge[i][k][0]+5;
					
					sidepoint[30+6*i+l][0]=i+25;
					sidepoint[30+6*i+l][1]=pointedge[i][j][0]+15;
					sidepoint[6*i+l+30][2]=m+5;
					sidepoint[30+6*i+l][3]=pointedge[i][k][0]+15;
					
					
					l++;
				}
			}
		}
		for(int i=0;i<10;i++)
		{
			vertex[5+i]=edgemiddles[i];
			vertex[15+i]=edgemiddles[i].getAntipodal();
			
			edgepoint[i][0]=pent.edgepoint[i][0];
			edgepoint[i][1]=i+5;
			
			edgepoint[10+i][0]=pent.edgepoint[i][1];
			edgepoint[i][1]=i+5;
			
			edgepoint[50+i][0]=pent.edgepoint[i][0]+25;
			edgepoint[50+i][1]=i+15;
			
			edgepoint[60+i][0]=pent.edgepoint[i][1]+25;
			edgepoint[60+1][1]=i+15;
			
			int k=0;
			for(int j=0;j<3;j++)
			{
				while(commonEntry(pent.edgepoint[i],pent.edgepoint[k])!=1000)
				{
					k++;
				}
				/*int l=0;
				while(commonEntry(pent.edgepoint[l],pent.edgepoint[i])!=1000&&commonEntry(pent.edgepoint[l],pent.edgepoint[k])!=1000)
				{
					l++;
				}*/
				edgepoint[20+j+3*i][0]=i+5;
				edgepoint[20+j+3*i][1]=k+15;
				
				/*sidepoint[3*i+j][0]=pent.edgepoint[i][0];
				sidepoint[3*i+j][1]=i+5;
				sidepoint[3*i+j][2]=k+15;
				sidepoint[3*i+j][3]=l+5;
				
				sidepoint[30+3*i+j][0]=pent.edgepoint[i][0]+25;
				sidepoint[30+3*i+j][1]=i+15;
				sidepoint[30+3*i+j][2]=k+5;
				sidepoint[30+3*i+j][3]=l+15;
						*/
				k++;
			}
			
		}
		Spherpoint[] centers=Cellcomplex.centers(vertex, sidepoint);
		return new Cellcomplex(vertex,centers, edgepoint,sidepoint);
	}
	
	
	public static int[][][] pointedge(Cellcomplex complex, int edgespervertex)//point edge gives all the edges a point is in with the position of the point in it
	{
		int[][][]out=new int[complex.vertex.length][edgespervertex][2];
		int[] counter=new int[complex.vertex.length]; // counts how many edges where already added for each vertex
		for(int i=0 ; i<complex.edgepoint.length;i++)
		{
			//System.out.println("i="+i);
			for( int j=0 ; j<2 ;j++)
			{
				//System.out.println("j="+j);
				out[complex.edgepoint[i][j]][counter[complex.edgepoint[i][j]]]=new int[] {i,j};
				counter[complex.edgepoint[i][j]]++;
			}
		}
		return out;		
	}
	//gives all the edges of a side in a right order, the last digit is 0,1 depending on the orientation of the face(which is first?)
	public static int[][][] sideedge(Cellcomplex complex)
	{
		System.out.print("calculating sideedge...");
		int[][][]out=new int[complex.sidepoint.length][complex.sidepoint[0].length][2];
		for(int i=0;i<complex.sidepoint.length;i++)
		{
			boolean foundfirst=false;int j=0;
			
			while(!foundfirst)
			{
				if(isSubset(complex.edgepoint[j],complex.sidepoint[i]))
						{
							out[i][0]=new int[] {j,0}; foundfirst=true;
						}
				else j++;
			}
			int m=j;
			for(int k=1;k<complex.sidepoint[0].length;k++)
			{
				System.out.print(k+"...");
				int l=j+1; foundfirst=false; int[] v= {complex.edgepoint[m][1-out[i][k-1][1]]};
				while(!foundfirst)
				{
					if(isSubset(v,complex.edgepoint[l])&&isSubset(complex.edgepoint[l],complex.sidepoint[i])&&(l!=m))
					{
						int n=0;
						if(v[0]==complex.edgepoint[l][1])n=1;
						
						out[i][k]=new int[] {l,n};
						foundfirst=true;
					}
					else l++;
				}
				m=l;
			}
			
		}
		System.out.println("and done!");
		return out;
	}
	
	public static int binomcoeff(int a, int b)
	{
		if(a<b) return 0;
		float out=1;
		for(int i=0;i<b;i++)
		{
			out=out*(a-i)/(i+1);
		}
		return Math.round(out);
	}
	
	public static boolean isSubset(int[] a, int[]m)
	{
		boolean out=true;
		for(int i=0;i<a.length;i++)
		{
			boolean in=false;
			for(int j=0;j<m.length;j++)
			{
				if(a[i]==m[j])in=true;
			}
			if(out==true)out=in;
		}
		return out;
	}
	
	public static double[] toDouble(int color)
	{
		Color col=new Color(color);
		return new double[] {col.getRed(),col.getGreen(),col.getBlue()};
	}
	
}

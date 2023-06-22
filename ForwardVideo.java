import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import javax.imageio.ImageIO;

public class ForwardVideo {
static int	impermin=720, color=randomColorMatt(70).getRGB(); static double blurRatio=0.95;
static int step=5, black=Color.black.getRGB();static String format="png";
public static void main(String[] args)
{ 
	double faintness=0.04; 
	int step=2, exactness=1200, denseness=15;
	Observer.dm="tf"; 
	Observer.dotsize=5;  
	Observer.format="png";
	Cellcomplex.empty=false;  
	Cellcomplex.filling=7;  
	Cellcomplex.denseness=denseness;
	//Bend observer to my will:ForwardrotateCB_(-70, -61, -77) - (-1, -17, -70)110
	Random rand=new Random();  
	//Spherline line=new Spherpoint(Math.PI*2*rand.nextDouble(),Math.PI/2*Math.pow(-1, rand.nextInt(2))*(1-rand.nextDouble()/9),Math.PI/2*Math.pow(-1, rand.nextInt(2))*(1-rand.nextDouble()/9)).geodesic(new Spherpoint(Math.PI*2*rand.nextDouble(),Math.PI/2*Math.pow(-1, rand.nextInt(2))*(1-rand.nextDouble()/9),Math.PI/2*Math.pow(-1, rand.nextInt(2))*(1-rand.nextDouble()/9)));
	//E4 right=new E4(0.5-rand.nextDouble(),0.5-rand.nextDouble(),0.5-rand.nextDouble(),0.5-rand.nextDouble());
	//\Bend observer to my will*/
	
	Observer eye=new Observer(1080,1080,Math.PI/2);
	double time=2, st=impermin, speed=2*Math.PI/eye.viewPoint.getMomentum().norm()/time/impermin /*images per minute*/;
	double[][] transformation=E4.idmatrix, mirror=E4.mirmatrix;//E4.randomrot();

	Spherpoint focus=eye.viewPoint.location(1); 
	String name= "dodecacontachoron_("+Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPhi()))+", "+
			Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getPsi()))+", "+
			Math.round(Math.toDegrees(eye.viewPoint.getOrigin().getTheta()))+") - ("+
			Math.round(Math.toDegrees(focus.getPhi()))+", "+Math.round(Math.toDegrees(focus.getPsi()))+", "+
			Math.round(Math.toDegrees(focus.getTheta()))+")";
	DecimalFormat df=new DecimalFormat("0000"); 
	for(double i=0;i<2*Math.PI;i=i+0.1) 
	{
		eye.viewPoint.location(i).println();
		
	}
	//Für rotate vor loop, sonst drin
	BufferedImage image1=eye.setup(color);
	double[][] zBuffer1=eye.InitiateBuffer();	
	BufferedImage image2=eye.setup(color);
	double[][] zBuffer2=eye.InitiateBuffer();
	//Cellcomplex.vertices=true;
	Cellcomplex subject =Polychoron.tiling533();//new Cellcomplex[2]; subject[0]=Polychoron.tiling433(); subject[1]=Polychoron.tiling334();//
	//System.out.println( subject.sidepoint[0].length);
	for(int i=100;i<2*impermin+100;i++)
	{
		
		System.out.print(i+". ");
		//defaultFrame(eye, i, denseness, transformation,name,(int) impermin,true);
		//eye.finish(mirror(image), name+df.format(i+impermin));
		
		dim(image1);dim(image2);//for Forwardrotate!
		
		//ForwardVideo.CPdoubleFrame(eye,i,step,(int)impermin, color,name,true);
	subject.doubledraw(eye, image1, image2, zBuffer1, zBuffer2, transformation, Color.black, 1);
	//subject[1].doubledraw(eye, image1, image2, zBuffer1, zBuffer2, transformation, Color.black, 1);
	//subject[2].doubledraw(eye, image1, image2, zBuffer1, zBuffer2, transformation, Color.black, 1);
	//subject[3].doubledraw(eye, image1, image2, zBuffer1, zBuffer2, transformation, Color.black, 1);

		eye.finish(image1, name+df.format(i));
		eye.finish(image2, name+df.format(i+impermin));
		ForwardVideo.mirrorFile(name+df.format(i+impermin)+"."+format, name+df.format(i+impermin),false,true);
		
		 
		//CPdraw(image1,zBuffer1,eye);
		//eye.finish(image1, name+i); 
		Observer.rewind(zBuffer1);Observer.rewind(zBuffer2);//for Forwardrotate
		eye.moveForward(speed);
	}
}
private static BufferedImage mirror(BufferedImage image) {
	BufferedImage out=new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
	for(int i=0;i<image.getWidth();i++)
	{
		for(int j=0;j<image.getHeight();j++)
		{
			out.setRGB(i, j, image.getRGB(image.getWidth()-i-1, image.getHeight()-j-1));
		}
	}
	return out;
	// TODO Auto-generated method stub
	
}
public static void dim(BufferedImage image)
{
	for(int i=0;i<image.getWidth();i++)
	{
		for(int j=0;j<image.getHeight();j++)
		{
			image.setRGB(i, j, Spherpoint.combine(new Color(image.getRGB(i, j)), new Color(color), blurRatio).getRGB());
		}
	}
}
//degree between 0 and 256
public static Color randomColorMatt(int degree)
{
	Random rand=new Random();
	int red=rand.nextInt(256);
	int green=rand.nextInt(Math.min(256, 2*degree)-Math.abs(128-red))+Math.max(0, red-degree);
	int blue=Math.max(0, Math.max(red-degree, green-degree))+rand.nextInt(Math.min(256+degree-red, Math.min(256+degree-green,Math.min(red+256-degree, Math.min(red-green+2*degree, Math.min(green+256-degree, green-red+2*degree))))));
	return new Color(red,green,blue);
}
public static void CPdraw(BufferedImage image,double[][] zBuffer,Observer eye)
{
	for (int phi=-180; phi<180; phi=phi+step)
	{
		for (int psi=-90; psi<=90; psi=psi+step)
		{
			for (int theta=-90; theta<=90; theta=theta+step)
					{
				
						int c=new Color((phi+180)*255/360,(psi+90)*255/180,(theta+90)*255/180).getRGB();
						eye.drawPoint(image, zBuffer, new Spherpoint(Math.toRadians(phi),Math.toRadians(psi),
								Math.toRadians(theta),c));
					}
		}
	}
}
//**********************************************
// Draws a frame with Coordinate points 
//**********************************************
public static void CPFrame(Observer eye,int i, int step,int color,String name)
{
	
	BufferedImage image=eye.setup(color);
	double[][] zBuffer=eye.InitiateBuffer();
	for (int phi=-180; phi<180; phi=phi+step)
	{
		for (int psi=-90; psi<=90; psi=psi+step)
		{
			for (int theta=-90; theta<=90; theta=theta+step)
					{
				
						int c=new Color((phi+180)*255/360,(psi+90)*255/180,(theta+90)*255/180).getRGB();
						eye.drawPoint(image, zBuffer, new Spherpoint(Math.toRadians(phi),Math.toRadians(psi),
								Math.toRadians(theta),c));
					}
		}
	}
	//eye.drawPoint(image, new Spherpoint(Math.toRadians(-180),Math.toRadians(-90),Math.toRadians(-90),Color.red));
//	Spherpoint focus=eye.viewPoint.location(1);
	DecimalFormat df=new DecimalFormat("0000");
		String name0=name+df.format(i);	
	eye.finish(image, name0);
}

//*******************************************
// Draws two CP-Frames with two observers on antipodal locations looking at the same point
// Condition: antipodal points appear simultanuously with opposite colors
//**********************************************
public static void CPdoubleFrame(Observer eye,int i, int step,int distance ,int color,String name,boolean up)
{
	Observer.dotsize=1;
	BufferedImage image1=eye.setup(color);
	BufferedImage image2=eye.setup(color);
	double[][] zBuffer1=eye.InitiateBuffer();
	int width=eye.width, height=eye.height;
	
	double[][] zBuffer2=eye.InitiateBuffer();


	
	
	for (int phi=-180; phi<180; phi=phi+step)
	{
		for (int psi=-90; psi<=90; psi=psi+step)
		{
			for (int theta=-90; theta<=90; theta=theta+step)
					{
				
						int c=new Color((phi+180)*255/360,(psi+90)*255/180,(theta+90)*255/180).getRGB();
						eye.doubleDrawPoint(image1,image2, zBuffer1,zBuffer2, new Spherpoint(Math.toRadians(phi),Math.toRadians(psi),
								Math.toRadians(theta),c));
					}
		}
	}
	DecimalFormat df=new DecimalFormat("0000");
	String name1=name+df.format(i);	
	eye.finish(image1, name1);
	String name2=name+df.format(i+distance);
	if(up)
	{
		for(int k=0;k<width;k++)
		{
			for(int j=0;j<height;j++)
			{
				image1.setRGB(k, j, image2.getRGB(width-1-k, height-1-j));
			}
		}
		eye.finish(image1, name2);
	}

	else eye.finish(image2, name2);
}

public static void schläfliFrame(Observer eye,int i,int distance,Cellcomplex subject,double[][] transformation, String name,boolean up)
{
	Observer.dotsize=10;
	double[][] zBuffer1=eye.InitiateBuffer();
	BufferedImage image1=eye.setup(black);
	double[][] zBuffer2=eye.InitiateBuffer();
	BufferedImage image2=eye.setup(black);
	subject.doubleRevdraw(eye, image1,image2, zBuffer1,zBuffer2, transformation, Color.blue, 1);//ToDo: Fix it!
	//subject.draw(eye, image1, zBuffer1, transformation, Color.black,1);
	DecimalFormat df=new DecimalFormat("0000");
	String name0=name+df.format(i),
			name1=name+df.format(i+distance);	
	eye.finish(image1, name0);
	if(up)
	{
		int width=eye.width,
				height=eye.height;
		
		for(int k=0;k<width;k++)
		{
			for(int j=0;j<height;j++)
			{
				image1.setRGB(k, j, image2.getRGB(width-1-k, height-1-j));
			}
		}
		eye.finish(image1, name1);
	}

	else eye.finish(image2, name1);
	
}

//Makes a Frame with that depicted, what I manually insert into the method
public static void defaultFrame(Observer eye,int i, int denseness,double[][] transformation, String name,int distance, boolean up)
{
	double[][] zBuffer1=eye.InitiateBuffer();
	BufferedImage image1=eye.setup(black);
	double[][] zBuffer2=eye.InitiateBuffer();
	BufferedImage image2=eye.setup(black);
	//To change as pleased:
	//Schläfli.doubleSchläfli2x2(eye, image1,image2 , zBuffer1,zBuffer2,1, transformation,denseness);
	Polychoron.doubleSchläfli2x2(eye, image1 , image2, zBuffer1,zBuffer2, 9, transformation,denseness);
	DecimalFormat df=new DecimalFormat("0000");
	String name1=name+df.format(i);	
	String name2=name+df.format(i+distance);
	eye.finish(image1, name1);
	if(up)  
	{
		int width=eye.width,
				height=eye.height;
		
		for(int k=0;k<width;k++)
		{
			for(int j=0;j<height;j++)
			{
				image1.setRGB(k, j, image2.getRGB(width-1-k, height-1-j));
			}
		}
		eye.finish(image1, name2);
	}
	else eye.finish(image2, name2);
}

public static void copyFile(String filename, int n)
{
	File f=new File(filename);
	DecimalFormat df=new DecimalFormat("0000");
	int i=filename.lastIndexOf('.');
	System.out.print(i);
	
	String newname=filename.substring(0, i-4)+df.format(n)+filename.substring(i, filename.length());
	//String newname="PiruetteCB2(-61, -32, -25) - (3, 10, 31)omega"+df.format(n)+".bmp";
	File f2=new File(newname);
	try {
		copyFile(f,f2);
	} catch (IOException e) {
		System.out.println("Problems copying "+newname);
		e.printStackTrace();
	}

}
public static void copyFile(String filename, String newname)
{
	File f=new File(filename);
	DecimalFormat df=new DecimalFormat("0000");
	
	
	
	
	//String newname="PiruetteCB2(-61, -32, -25) - (3, 10, 31)omega"+df.format(n)+".bmp";
	File f2=new File(newname);
	try {
		copyFile(f,f2);
	} catch (IOException e) {
		System.out.println("Problems copying "+newname);
		e.printStackTrace();
	}

}
public static void mirrorFile(String filename,String name, boolean horizontal,boolean vertical)
{
	
	File sourceFile=new File(filename);
	int b=Color.black.getRGB();
	int i0,j0;
	try {
		
		final BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		int width=sourceImage.getWidth();
		int heigth=sourceImage.getHeight();
		BufferedImage destImage=new BufferedImage(width,heigth,BufferedImage.TYPE_3BYTE_BGR);
		
		for(int i=0;i<width;i++)
		{
			i0=i;
			if(horizontal)i0=width-i-1;
			for(int j=0;j<heigth;j++)
			{
				j0=j;
				if(vertical)j0=heigth-1-j;
				int c=sourceImage.getRGB(i,j);
				
					destImage.setRGB(i0, j0, c);
				
				
				
			}
		}	
			File outputfile = new File(name+"."+format);
				try {
					ImageIO.write(destImage, format, outputfile);
				} catch (IOException e) {
					System.out.println("IOException: Problems saving file "+name);
					e.printStackTrace();
				}
		} catch (IOException e) {
		System.out.println("Problems mirroring file "+sourceFile.getName());
		e.printStackTrace();}
	
	
}

public static void mirrorcompFile(String filename,String name,Color color)
{
	
	File sourceFile=new File(filename);
	int b=color.getRGB();
	try {
		
		final BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		int width=sourceImage.getWidth();
		int heigth=sourceImage.getHeight();
		BufferedImage destImage=new BufferedImage(width,heigth,BufferedImage.TYPE_3BYTE_BGR);
		
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<heigth;j++)
			{
				int c=sourceImage.getRGB(i,j);
				if(c==b)
				{
					destImage.setRGB(width-i-1, heigth-j-1, b);
				}
				
				else 
					{
					int compcolor=compColor(c);
					destImage.setRGB(width-i-1, heigth-j-1, compcolor);
					}
				
			}
		}	
			File outputfile = new File(name+".bmp");
				try {
					ImageIO.write(destImage, "bmp", outputfile);
				} catch (IOException e) {
					System.out.println("IOException: Problems saving file "+name);
					e.printStackTrace();
				}
		} catch (IOException e) {
		System.out.println("Problems mirroring file "+sourceFile.getName());
		e.printStackTrace();}
	
	
}
public static int compColor(int c)
{
	Color color=new Color(c);
	int r=color.getRed(), g=color.getGreen(),blue=color.getBlue();
	Color comp=new Color((r+128)%256,255-g,255-blue);
	return comp.getRGB();
}

public static void copyFile(File sourceFile, File destFile) throws IOException {
    if(!destFile.exists()) {
        destFile.createNewFile();
    }
 
    FileChannel origin = null;
    FileChannel destination = null;
    try {
        origin = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
 
        long count = 0;
        long size = origin.size();              
        while((count += destination.transferFrom(origin, count, size-count))<size);
    }
    finally {
        if(origin != null) {
            origin.close();
        }
        if(destination != null) {
            destination.close();
        }
    }
}
public static void schläfliFrame(Observer eye, int i, int distance, Cellcomplex[] subject, double[][] transformation,
		String name, boolean up) 
{
	
	double[][] zBuffer1=eye.InitiateBuffer();
	BufferedImage image1=eye.setup(black);
	double[][] zBuffer2=eye.InitiateBuffer();
	BufferedImage image2=eye.setup(black);
	for(int j=0;j<subject.length;j++)
	subject[j].doubleRevdraw(eye, image1,image2, zBuffer1,zBuffer2, transformation, Color.blue, 1);//ToDo: Fix it!
	//subject.draw(eye, image1, zBuffer1, transformation, Color.black,1);
	DecimalFormat df=new DecimalFormat("0000");
	String name0=name+df.format(i),
			name1=name+df.format(i+distance);	
	eye.finish(image1, name0);
	if(up)
	{
		int width=eye.width,
				height=eye.height;
		
		for(int k=0;k<width;k++)
		{
			for(int j=0;j<height;j++)
			{
				image1.setRGB(k, j, image2.getRGB(width-1-k, height-1-j));
			}
		}
		eye.finish(image1, name1);
	}

	else eye.finish(image2, name1);
	
}
}
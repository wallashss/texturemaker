package br.attalea.process;


import java.awt.Color;
import java.awt.Point;

import br.attalea.core.Vector3;
import br.attalea.core.Image;

// TODO: Maybe don't work anymore
public class Phong
{
	
	public static Image phong(Image inputImage, Vector3 normal, Vector3 lightPosition, float intensity)
	{
		int w = inputImage.getWidth();
		int h = inputImage.getHeight();
		Image outputImage = new Image(w,h,inputImage.getType());
		
		for(int i=0;i<w;i++)
		{
			for(int j=0;j<h;j++)
			{
				if(i < w/2 && j < h/2)
				{
					outputImage.setRGB(i, j, Color.blue.getRGB());
				}
				else
				{
					outputImage.setRGB(i, j, Color.green.getRGB());
				}
			}
		}
		
		return outputImage;
	}
	
	private static int diffuse(Vector3 point, Vector3 n, Vector3 lightDir, float lightDistance, float intensity)
	{
		float diff =  n.dot(lightDir);
		
		float quadraticAttenuation=1;
        float linearAttenuation =1;
        float constantAttenuation =1;
        float attenuation = 1.0f / (constantAttenuation
                			     + linearAttenuation * lightDistance
                			   	 + quadraticAttenuation * lightDistance * lightDistance);
		
        diff *=attenuation * intensity;
        diff = Math.max(diff, 0);
        diff = Math.min(diff, 1);
        
        if(diff > 0.05)
        {
        	return Image.getRGB(diff, diff, diff);
        }
        else
        {
        	return 0;
        }
        
				
	}
	
	public static Image diffuse(Image inputImage, Vector3 lightPosition, float intensity)
	{
		int w = inputImage.getWidth();
		int h = inputImage.getHeight();
		Image outputImage = new Image(w,h,inputImage.getType());
		
		Vector3 normal=new Vector3(0,0,-1);
		
		for(int i=0;i<w;i++)
		{
			for(int j=0;j<h;j++)
			{
				Vector3 point = new Vector3(-w/2+i, -h/2+j, 0);
				Vector3 lightDir = lightPosition.diff(point).normalized();
				float distance = lightPosition.distance(point);
				outputImage.setRGB(i, j, diffuse(point,normal, lightDir,distance,intensity));
			}
		}
		return outputImage;
	}

	// "Gaussian"
	public static Image gaussianTexture(int w, int h, float max)
	{
		Image outImage = Image.createColorImage(w, h);
		
		float radius = w/2;
		float step = 1.0f/radius;
		
		for(int i=0;i<w;i++)
		{
			for(int j=0;j<h;j++)
			{
				Point p = new Point(i-w/2,j-h/2);
				float dist = (float)p.distance(0.0,0.0);
				if(dist < radius)
				{
					float light = 1.0f - dist * step;
					light *= max;
					outImage.setRGB(i,j, Color.white.getRGB());
//					outImage.setRGB(i, j, new Image.RGBF(light).getRGB());
//					outImage.setRGB(i, j, Color.white.getRGB());
					
				}
			}
		}
		return outImage;
		
	}

	// "Gaussian"
	public static Image gaussianTexture(int w, int h, float minRadius, float max)
	{
		Image outImage = Image.createColorImage(w, h);
		
		float radius = w/2;
		float step = 1.0f/(radius-minRadius);
		
		for(int i=0;i<w;i++)
		{
			for(int j=0;j<h;j++)
			{
				Point p = new Point(i-w/2,j-h/2);
				float dist = (float)p.distance(0.0,0.0);
				if(dist < minRadius)
				{
					float light = max;
					outImage.setRGB(i, j, Image.getRGB(light, light, light));
				}
				else if(dist < radius )
				{
					float light = 1.0f - (dist-minRadius) * step;
					light *= max;
					light =Math.min(light, 1);
					light =Math.max(light, 0);
					outImage.setRGB(i, j, Image.getRGB(light, light, light));
//					outImage.setRGB(i, j, Color.white.getRGB());
				}
				else
				{
					outImage.setRGB(i, j, 0);
				}
				
			}
		}
		return outImage;
		
	}

	// "Gaussian"
	public static Image gaussianTexture(int w, int h, float minRadius, float maxRadius, float max)
	{
		Image outImage = Image.createGrayscaleImage(w, h);
		
		float radius = w/2;
		float step1 = 0.5f/(maxRadius-minRadius);
		float step2 = 0.5f/(radius-maxRadius);
		
		for(int i=0;i<w;i++)
		{
			for(int j=0;j<h;j++)
			{
				Point p = new Point(i-w/2,j-h/2);
				float dist = (float)p.distance(0.0,0.0);
				if(dist < minRadius)
				{
					float light = max;
					outImage.setRGB(i, j, Image.getRGB(light, light, light));
//					outImage.setRGB(i, j, Color.yellow.getRGB());
				}
				else if(dist <  maxRadius )
				{
					float light = 1.0f - (dist-minRadius) * step1;
					light *= max;
					light =Math.min(light, 1);
					light =Math.max(light, 0);
					outImage.setRGB(i, j, Image.getRGB(light, light, light));
//					outImage.setRGB(i, j, Color.red.getRGB());
//					outImage.setRGB(i, j, Color.white.getRGB());
				}
				else if(dist < radius)
				{
					float light = 0.5f - (dist-maxRadius) * step2;
					light *= max;
					light =Math.min(light, 1);
					light =Math.max(light, 0);
					outImage.setRGB(i, j, Image.getRGB(light, light, light));
//					outImage.setRGB(i, j, Color.green.getRGB());
				}
				else
				{
					outImage.setRGB(i, j, 0);
				}
				
			}
		}
		return outImage;
		
	}

	public static void main(String [] args)
	{
		Image image = Image.createColoredCleanImage(100, 100);
		Image newImg = Phong.phong(image, new Vector3(0, 0, 1), new Vector3(0,0,-10), 10);

		Image diffuse = Phong.diffuse(image, new Vector3(0,100,-100), 6000);
		Image gaussian = Phong.gaussianTexture(400, 400, 1f);
		Image gaussian2 = Phong.gaussianTexture(500, 500, 60.0f, 1f);
		Image gaussian3 = Phong.gaussianTexture(500, 500, 60.0f, 170.0f, 1f);

		gaussian.save("gaussian.png");
		gaussian2.save("gaussian_2.png");
		gaussian3.save("gaussian_3.png");
		newImg.save("phong.png");
		image.save("test.png");
		diffuse.save("diffuse.png");
	}
	
	

}

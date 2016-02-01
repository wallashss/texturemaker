package br.attalea.process;


import java.awt.image.BufferedImage;

import br.attalea.core.Vector3;
import br.attalea.core.Image;

public class ClippingTexture {
	
	public static Image circularMask(int w, int h)
	{
		Image outputImage = new Image(w,h,BufferedImage.TYPE_BYTE_GRAY); 
		
		for(int i=0;i<w;i++)
		{
			for(int j=0;j<h;j++)
			{
				Vector3 point = new Vector3( j-(h/2.0f), w/2.0f - i, 0);
				Vector3 diff = point.minus(Vector3.up);
				double angle = Math.atan2( diff.y, diff.x);
				
				
				double level;
				if(angle > 0 )
				{
					level = angle/(2.0*Math.PI);
				}
				else
				{
					level = (angle + 2*Math.PI )/(2.0*Math.PI);
				}
				outputImage.setGrayLevel(i, j, (int) (level * 255.0) );
				
				System.out.println(Math.toDegrees(angle)+ " ");
				
				
			}
		}
		
	
		
		return outputImage;
	}
	
	public static Image clipRadius(Image img,  int radius)
	{
		Image output = new Image(img.getImage());
		
		int w = img.getWidth();
		int h = img.getHeight();
		
		for(int i=0;i<w;i++)
		{
			for(int j=0;j<h;j++)
			{
				Vector3 point = new Vector3( j-(h/2.0f), w/2.0f - i, 0);
				if(point.distance(Vector3.zero) < radius )
				{
					output.setRGB(i, j, 0);
				}
			}
		}
		
		return output;
	}

	public static void main(String [] args)
	{
		Image newImg = Image.loadImageFromFile("images/Hp Sprite.png");

		if(newImg != null)
		{
			ClippingTexture.clipRadius(newImg, 10).save("images/Clipped.png");
		}
	}
}

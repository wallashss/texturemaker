package br.attalea.core;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image
{
	public static class RGBF
	{
		public float r;
		public float g;
		public float b;
		public float a;

		public RGBF(int rgba)
		{
			// The minus bit... we should avoid it
			// Not sure if this is right
			int bytea = (rgba & 0x7F000000) >> 24;
			int byter = (rgba & 0x00FF0000) >> 16;
			int byteg = (rgba & 0x0000FF00) >> 8;
			int byteb = (rgba & 0x000000FF);


			r = ((float) byter) / 255.0f;
			g = ((float) byteg) / 255.0f;
			b = ((float) byteb) / 255.0f;
			a = ((float) bytea) / 127.0f;
		}

		public RGBF(float r, float g, float b, float a)
		{
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		}

		public RGBF(float v, float a)
		{
			this.r = v;
			this.g = v;
			this.b = v;
			this.a = a;
		}

		public RGBF(float v)
		{
			this.r = v;
			this.g = v;
			this.b = v;
			this.a = 1.0f;
		}

		public int getRGB()
		{
			int byter = (int) (r * 255.0f);
			int byteg = (int) (g * 255.0f);
			int byteb = (int) (b * 255.0f);
			int bytea = (int) (a * 255.0f);

			byter = byter > 255 ? 255 : byter;
			byteg = byteg > 255 ? 255 : byteg;
			byteb = byteb > 255 ? 255 : byteb;
			bytea = bytea > 255 ? 255 : bytea;

			byter = byter < 0 ? 0 : byter;
			byteg = byteg < 0 ? 0 : byteg;
			byteb = byteb < 0 ? 0 : byteb;

			bytea = bytea < 0 ? 0 : bytea;

			bytea <<= 24;
			byter <<= 16;
			byteg <<= 8;
			byteb <<= 0;


			return bytea | byter | byteg | byteb;
		}

	}
	
	private BufferedImage _image; 

	public static int getRGB(float r, float g, float b)
	{
		return new Color(r, g, b).getRGB();
	}
	
	public static int getRGB(float r, float g, float b, float a)
	{
		return new Color(r, g, b,a).getRGB();
	}
	
	public Image (BufferedImage image)
	{
		_image = image;
	}
	
	public Image (int width, int height, int type)
	{
		_image = new BufferedImage(width, height, type);
	}


	public static Image loadImageFromFile(String filename)
	{
		Image img;
		try
		{
			BufferedImage temp =ImageIO.read(new File(filename));
			img = new Image(temp);
		}
		catch (IOException e)
		{
			img = null;
		}
		return img;
	}
	
	public static Image createGrayscaleImage(int w, int h)
	{
		return new Image (w,h, BufferedImage.TYPE_BYTE_GRAY);
	}
	
	public static Image createColorImage(int w, int h)
	{
		return new Image (w,h, BufferedImage.TYPE_4BYTE_ABGR);
	}

	public static Image createColoredWhiteImage(int w, int h)
	{
		Image img = new Image (w,h, BufferedImage.TYPE_4BYTE_ABGR);
		for(int i=0;i<img.getWidth();i++)
		{
			for(int j=0;j<img.getHeight();j++)
			{
				img.setRGB(i, j, Color.white.getRGB());
			}
		}
		return img;
	}

	public static Image createColoredBlackImage(int w, int h)
	{
		Image img = new Image (w,h, BufferedImage.TYPE_4BYTE_ABGR);
		for(int i=0; i<img.getWidth();i++)
		{
			for(int j=0; j<img.getHeight();j++)
			{
				img.setRGB(i, j, Color.black.getRGB());
			}
		}
		return img;
	}

	public static Image createColoredCleanImage(int w, int h)
	{
		return createColoredSingleColorImage(w, h, 0);
	}

	public static Image createColoredSingleColorImage(int w, int h, int color)
	{
		Image img = new Image (w,h, BufferedImage.TYPE_4BYTE_ABGR);
		for(int i=0; i<img.getWidth();i++)
		{
			for(int j=0; j<img.getHeight();j++)
			{
				img.setRGB(i, j, color);
			}
		}
		return img;
	}

	
	public static String getExtension(String s) {
		String ext = "";
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	public void multiply(Image img)
	{
		multiply(img, true);
	}

	public void multiply(Image img, boolean useAlpha)
	{
		if(useAlpha)
		{
			multiply(img, true, true);
		}
		else
		{
			multiply(img, true, false);
		}
	}

	public void multiply(Image img, boolean useAlpha, boolean useOtherAlpha)
	{
		int offsetX = img.getWidth() /2 - getWidth()/2 ;
		int offsetY = img.getHeight() /2 - getHeight()/2 ;

		for(int i=0; i<getWidth();i++)
		{
			for(int j=0; j< getHeight(); j++)
			{
				int x = i + offsetX;
				int y = j + offsetY;

				if(x >=0 && x < img.getWidth() && y >=0 && y < img.getHeight())
				{
					RGBF a = getRGBAF(i, j);
					RGBF b = img.getRGBAF(x, y);

					if(a.a != 0.0f)
					{
						RGBF c = new RGBF(a.r * b.r, a.g * b.g, a.b * b.b, 1.0f);
						if(useAlpha && useOtherAlpha)
						{
							c.a = a.a * b.a;
						}
						else if(useAlpha)
						{
							c.a = a.a;
						}
						else if(useOtherAlpha)
						{
							c.a = b.a;
						}

						setRGB(i, j, c.getRGB());
					}
				}
			}
		}
	}

	public void multiply(float value, boolean multiplyAlpha)
	{
		for(int i=0; i<getWidth();i++)
		{
			for(int j=0; j<getHeight(); j++)
			{
				RGBF a = getRGBAF(i, j);
				RGBF c = new RGBF(a.r * value, a.g * value, a.b * value, multiplyAlpha ? a.a * value : a.a);
				setRGB(i, j, c.getRGB());
			}
		}
	}

	public void multiply(Color color, boolean multiplyAlpha)
	{
		for(int i=0; i<getWidth();i++)
		{
			for(int j=0; j<getHeight(); j++)
			{
				RGBF a = getRGBAF(i, j);
				RGBF b = new RGBF(color.getRGB());
				RGBF c = new RGBF(a.r * b.r, a.g * b.g, a.b * b.b, multiplyAlpha ? a.a * b.a : a.a);
				setRGB(i, j, c.getRGB());

			}
		}
	}

	public void add(float value, boolean addAlpha)
	{
		for(int i=0; i<getWidth();i++)
		{
			for(int j=0; j<getHeight(); j++)
			{
				RGBF a = getRGBAF(i, j);

				if(a.a != 0.0f)
				{
					RGBF c = new RGBF(a.r + value, a.g + value, a.b + value, addAlpha ? a.a + value : a.a);
					setRGB(i, j, c.getRGB());
				}
			}
		}
	}

	public void addLayer(Image img)
	{
		Graphics2D g2d  = createGraphics2D();

		int x = 0;
		int y = 0;


		g2d.drawImage(img.getImage(), x, y, img.getWidth(), img.getHeight(), null);
	}

	public int getWidth()
	{
		return _image.getWidth();
	}
	
	public int getHeight()
	{
		return _image.getHeight();
	}
	
	public int getType()
	{
		return _image.getType();
	}
	
	public byte[] getBytes()
	{
		return ((DataBufferByte) _image.getRaster().getDataBuffer()).getData();
	}

	public Graphics2D createGraphics2D()
	{
		return _image.createGraphics();
	}

	
	public int getRGB(int x, int y)
	{
		return _image.getRGB(x, y);
	}

	public RGBF getRGBAF(int x, int y)
	{
		return new RGBF(_image.getRGB(x, y));
	}

	public void setRGB(int x, int y, int RGB)
	{
		_image.setRGB(x, y, RGB);
	}
	
	public void setGrayLevel(int x, int y, int level)
	{
		if(level < 0)
		{
			level = 0;
		}
		else if(level > 255)
		{
			level = 255;
		}
//		int r = level << 0;
//		int g = level << 8;
//		int b = level << 16;
		
		byte [] bytes = getBytes();
		bytes[x * _image.getHeight() + y] = (byte) level;
//		
//		_image.setRGB(x, y, r | g | b);
	}

	public void thresholdAlpha(float threshold, boolean setOpaque)
	{
		for(int i=0; i<getWidth();i++)
		{
			for(int j=0; j<getHeight(); j++)
			{
				RGBF a = getRGBAF(i, j);

				if(a.a < threshold)
				{
					setRGB(i, j, 0);
				}
				else if(setOpaque)
				{
					a.a = 1.0f;
					setRGB(i, j, a.getRGB());
				}
			}
		}
	}

	public void thresholdAlpha(float threshold, Color color)
	{
		for(int i=0; i<getWidth();i++)
		{
			for(int j=0; j<getHeight(); j++)
			{
				RGBF a = getRGBAF(i, j);

				if(a.a < threshold)
				{
					setRGB(i, j, 0);
				}
				else
				{
					setRGB(i, j, color.getRGB());
				}
			}
		}
	}

	public void toLuminance()
	{
		for(int i=0; i<getWidth();i++)
		{
			for (int j = 0; j < getHeight(); j++)
			{
				RGBF a = getRGBAF(i, j);
				float l = a.r * 0.2126f + a.g * 0.7152f + a.b * 0.0722f;
				setRGB(i, j, new RGBF(l, l ,l, 1.0f).getRGB());
			}
		}
	}

	
	public void save(String filename)
	{
		 String extension = getExtension(filename);
		
		 File file = new File(filename);
		 try
		 {
			 ImageIO.write(_image, extension, file);
		 }
		 catch (IOException e)
		 {
			 e.printStackTrace();
		 }
	}

	public BufferedImage getImage()
	{
		return _image;
	}
	
	
}

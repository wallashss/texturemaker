package br.attalea.process;

import java.awt.Color;

import br.attalea.core.Image;

public class Waves {

	public static Image createWave(int width, int height, float amplitude, float waveLength)
	{
		Image waveImage= Image.createColorImage(width, height);

		float maxHeight = amplitude * (float)Math.sin(Math.PI/2.0f);

		
		for(int i=0; i<width; i++)
		{
//			float waveHeight = ( (1.0f+Math.round((amplitude*Math.sin((float)i*(float)waveLength))))/2.0f ) ;
			float waveHeight = (float) Math.cos((float)i*waveLength);
			waveHeight +=1;
			waveHeight *=0.5;
			waveHeight *= amplitude;
			waveHeight *= height;
//			System.out.println(Math.sin((float) i));
			for(int j=0;j<height;j++)
			{
//				int color = Color.blue.getRGB();
				int color = Color.white.getRGB();
				
				if(j < waveHeight)
				{
					color = 0;
				}
				else
				{
//					int diff = j- (int)waveHeight;
//					if(diff < 20 && diff > 0)
//					{
//						Color cor = new Color(255 / diff, 255 / diff, 255 / diff);
//						color = cor.getRGB();
//					}
					
				}
				waveImage.setRGB(i, j, color);
			}
		}
		
		return waveImage;
	}
}

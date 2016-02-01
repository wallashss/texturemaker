package br.attalea.process;

import br.attalea.core.Image;

/**
 * Created by wallashss on 22/01/16.
 */
public class Dithering
{

    public static Image getDithering(Image input)
    {
        Image  output = new Image(input.getImage());
        for(int y = 1; y < input.getHeight()-1; y++)
        {
            for(int x = 1; x < input.getWidth()-1; x++)
            {
                Image.RGBF oldPixel  = input.getRGBAF(x,y);
                float newPixelValue = oldPixel.r > 0.5f ? 1.0f : 0.0f;
                output.setRGB(x, y, new Image.RGBF(newPixelValue).getRGB());
                float error = oldPixel.r - newPixelValue;

                output.setRGB(x+1, y, new Image.RGBF(output.getRGBAF(x+1, y).r + error * 7.0f/16.0f).getRGB());
                output.setRGB(x-1, y+1, new Image.RGBF(output.getRGBAF(x-1, y+1).r + error * 3.0f/16.0f).getRGB());
                output.setRGB(x, y+1, new Image.RGBF(output.getRGBAF(x, y+1).r + error * 5.0f/16.0f).getRGB());
                output.setRGB(x+1, y+1, new Image.RGBF(output.getRGBAF(x+1, y+1).r + error * 1.0f/16.0f).getRGB());
            }
        }

        return output;
    }

    public static void main(String [] args)
    {
        Image input = Image.loadImageFromFile("images/lenna.png");
        input.toLuminance();
        Image ditheringImage = Dithering.getDithering(input);
        ditheringImage.save("images/lenna_dithering.png");
    }
}

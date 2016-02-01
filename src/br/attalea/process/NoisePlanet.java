package br.attalea.process;

import br.attalea.core.Image;

import java.awt.*;

/**
 * Created by wallashss on 31/01/16.
 */
public class NoisePlanet
{

    public static Image getPlanet(int width, int height, Color color)
    {
        Image planetImage = Image.createColoredCleanImage(width, height);

        int planetSize = Math.min(width, height);

        Graphics2D g2d = planetImage.createGraphics2D();

        g2d.setColor(color);
        g2d.fillOval(0, 0, planetSize, planetSize);

        return planetImage;

    }
    public static void main(String [] args)
    {
        int width = 800;
        int height = 600;

        PerlinNoise perlinNoise = new PerlinNoise();
        Image noise  = perlinNoise.getNoiseTexture(width, height, true);
        Image noise2 = perlinNoise.getNoiseTexture(width, height, true, new PerlinNoise.Seed(500, 500, 0.05f, 0.05f));
        Image planet = NoisePlanet.getPlanet(width, height, Color.yellow);

        noise2.save("images/noise.png");

        Image planetMask = NoisePlanet.getPlanet(width, height, Color.white);

        noise2.thresholdAlpha(0.5f, Color.white);



        noise2.multiply(planetMask);

        noise2.save("images/noise2002.png");

        Image chalk = Image.loadImageFromFile("images/texture_giz_3.png");

        planet.addLayer(noise2);
//        planet.multiply(noise);



        planet.multiply(chalk, false, true);
        planet.add(0.3f, false);


        noise.save("images/noise200.png");
        planet.save("images/planet.png");

    }
}

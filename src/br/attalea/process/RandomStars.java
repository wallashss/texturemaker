package br.attalea.process;

import br.attalea.core.Image;

import java.awt.*;
import java.util.Random;

public class RandomStars
{

    private static void drawDotStar(int x, int y, int dotSizeX, int dotSizeY, Image img , Color color)
    {
        for(int i =0 ;i < dotSizeX; i++)
        {
            for(int j = 0; j < dotSizeY ; j++)
            {
                int ix = x - dotSizeX/2 +i;
                int iy = y - dotSizeY/2 +j;

                if(ix >= 0 && ix < img.getWidth() && iy >= 0 && iy < img.getHeight())
                {
                    if(! ((i == 0 || i == dotSizeX-1) && (j ==0 || j == dotSizeY-1)) )
                    {
                        Image.RGBF c = new Image.RGBF(color.getRGB());
                        img.setRGB(ix, iy, c.getRGB());
                    }
                }
            }
        }
    }

    private static void drawCross(int x, int y, int size, Image img, Color color)
    {
        for(int i = 0; i < size ; i++)
        {


            for(int j = 0 ; j < 3;j++)
            {
                int ix = x - size/2 +i;

                if(ix >= 0 && ix < img.getWidth())
                {
                    int jy = y - 1 + j;
                    if(jy >= 0 && jy < img.getHeight())
                    {
                        img.setRGB(ix, jy, color.getRGB());
                    }

                }

                int iy = y - size/2 +i;
                if(iy >= 0 && iy < img.getHeight())
                {
                    int jx = x - 1 + j;
                    if(jx >= 0 && jx < img.getWidth())
                    {
                        img.setRGB(jx, y - size / 2 + i, color.getRGB());
                    }
                }

            }
        }
    }

    public static void drawShineStars(Image img, int maxCount, int starSize)
    {
        Random r = new Random();
        int w =img.getWidth();
        int h =img.getHeight();

        Graphics2D g2D  = img.createGraphics2D();

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2D.setRenderingHints(rh);

        int maxColorNumber = 5;
        Color [] color = new Color[maxColorNumber];

        color [0] = Color.cyan;
        color [1] = Color.white;
        color [2] = Color.green;
        color [3] = new Color(232, 140, 132);
        color [4] = Color.yellow;

        for(int i =0 ; i < maxCount ; i++)
        {
            int x  = r.nextInt(img.getWidth());
            int y  = r.nextInt(img.getHeight());


            if(x >= 0 && y < w && y >=0 && y < h)
            {
                Color randomColor = color[r.nextInt(maxColorNumber)];
                drawDotStar(x, y, starSize/2, starSize/2, img, randomColor);
                drawCross(x, y, starSize, img, randomColor);
            }
        }
    }

    public static void drawRandomDiamondStars(Image img, int maxCount, int starSize)
    {
        drawRandomDiamondStars(img, maxCount, starSize, starSize);
    }

    public static void drawRandomDiamondStars(Image img, int maxCount, int minSize, int maxSize)
    {
        Random r = new Random();
        int w =img.getWidth();
        int h =img.getHeight();


        Graphics2D g2D  = img.createGraphics2D();

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2D.setRenderingHints(rh);

        Color [] color = {Color.white,  Color.yellow, Color.cyan, new Color(232, 140, 132)} ;
        int maxColorNumber = color.length;


        int rangeSize = maxSize - minSize;
        if(maxSize != minSize)
        {
            rangeSize = maxSize - minSize -1;
        }
        int halfSize = minSize/2;

        for(int i =0 ; i < maxCount ; i++)
        {
            int x  = rangeSize + r.nextInt(img.getWidth()-rangeSize*2);
            int y  = rangeSize + r.nextInt(img.getHeight()-rangeSize*2);

            if(x >= 0 && y < w && y >=0 && y < h)
            {
                Color randomColor = color[r.nextInt(maxColorNumber)];
                g2D.setColor(randomColor);
                int range = r.nextInt(rangeSize);
//                int range = 0;
//                g2D.fillOval(x, y, starSize + range, starSize + range);
                Polygon p = new Polygon();

                if(maxSize != minSize)
                {
                    int horizontalRange = r.nextInt(1);
                    int verticalRange = r.nextInt(1);
                    p.addPoint(x , y - halfSize - range -verticalRange);
                    p.addPoint(x + halfSize + range + horizontalRange, y );
                    p.addPoint(x , y + halfSize + range +verticalRange);
                    p.addPoint(x - range - halfSize - horizontalRange, y );
                }
                else
                {
                    p.addPoint(x , y - halfSize );
                    p.addPoint(x + halfSize , y );
                    p.addPoint(x , y + halfSize );
                    p.addPoint(x - halfSize, y );
                }

                g2D.fillPolygon(p);
            }
        }

    }

    public static void drawRandomDotStars(Image img, int maxCount, int starSize)
    {
        Random r = new Random();
        int w =img.getWidth();
        int h =img.getHeight();


        Graphics2D g2D  = img.createGraphics2D();

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2D.setRenderingHints(rh);

        Color [] color = {Color.white,  Color.yellow, Color.cyan, new Color(232, 140, 132)} ;
        int maxColorNumber = color.length;

//        color [0] = Color.cyan;
//        color [1] = Color.white;
//        color [2] = Color.green;
//        color [3] = new Color(232, 140, 132);
//        color [4] = Color.yellow;

        int rangeSize = 10;

        for(int i =0 ; i < maxCount ; i++)
        {
            int x  = rangeSize + r.nextInt(img.getWidth()-rangeSize*2);
            int y  = rangeSize + r.nextInt(img.getHeight()-rangeSize*2);

            if(x >= 0 && y < w && y >=0 && y < h)
            {
                Color randomColor = color[r.nextInt(maxColorNumber)];
                g2D.setColor(randomColor);
//                drawDotStar(x, y, starSize + r.nextInt(5), starSize + r.nextInt(5), img, randomColor);
//                g2D.fillOval(x, y, starSize + r.nextInt(6), starSize + r.nextInt(6));
                int range = r.nextInt(rangeSize);
//                int range = 0;
                g2D.fillOval(x, y, starSize + range + r.nextInt(1), starSize + range + r.nextInt(1));
            }
        }

    }

    public static void drawRandomSpriteStars(Image img, int maxCount, Image [] sprites)
    {
        Random r = new Random();
        int w =img.getWidth();
        int h =img.getHeight();

        Graphics2D g2D  = img.createGraphics2D();

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2D.setRenderingHints(rh);


        Color [] randomColors = {Color.white, Color.cyan, Color.yellow};

        int maxColors = randomColors.length;
//        randomColors [0] = Color.cyan;
//        randomColors [1] = Color.white;
//        randomColors [2] = Color.green;
//        randomColors [3] = new Color(232, 140, 132);
//        randomColors [4] = Color.yellow;

        for(int i =0 ; i < maxCount ; i++)
        {
            int x  = r.nextInt(img.getWidth());
            int y  = r.nextInt(img.getHeight());


            if(x >= 0 && y < w && y >=0 && y < h)
            {
                int imageIdx = r.nextInt(sprites.length);
                Image sprite = new Image(sprites[imageIdx].getImage());
                Color color = randomColors[r.nextInt(maxColors)];
                sprite.multiply(color, false);
                g2D.drawImage(sprite.getImage(), x, y, null);
            }
        }
    }

    public static void main(String [] args)
    {
        try
        {

            Image noise = Image.loadImageFromFile("Images/texture_giz_3_hd.png");

            Image stars = Image.createColoredCleanImage(noise.getWidth(), noise.getHeight());
            RandomStars.drawRandomDotStars(stars, 300, 3);
            RandomStars.drawRandomDiamondStars(stars, 110, 5, 9);

            noise.save("images/noise.png");
            noise.save("images/noisex15.png");
            stars.multiply(noise, false, true);

            stars.save("images/stars_10.png");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

}

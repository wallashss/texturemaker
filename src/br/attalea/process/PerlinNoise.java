package br.attalea.process;

import br.attalea.core.Image;

import java.awt.image.BufferedImage;

/**
 * Created by wallashss on 12/01/16.
 */

// Reference: http://freespace.virgin.net/hugo.elias/models/m_perlin.htm
public class PerlinNoise
{
    // TODO: make a descent class
    // Global configuration
    public float persistence;

    public PerlinNoise(float persitence)
    {
        this.persistence = persitence;
    }

    public PerlinNoise()
    {
        persistence = 0.5f;
    }

    public static class Seed
    {
        public int offsetx;
        public int offsety;
        public float scalex;
        public float scaley;

        public Seed()
        {
            offsetx = 0;
            offsety = 0;
            scalex = 0.05f;
            scaley = 0.05f;
        }

        public Seed(int offsetx, int offsety, float scalex, float scaley)
        {
            this.offsetx = offsetx;
            this.offsety = offsety;
            this.scalex = scalex;
            this.scaley = scaley;
        }
    }

    public static float noise2D(int x, int y)
    {
        // 38609 is the 4096nth prime, 4096 x 4096 is enough for texture.
        // Thiss number will "guarantee" no repetition for our (x,y)
        int n = x * 38609 + y;
        n = (n<<13) ^ n;
        return ( 1.0f - (((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0f));
    }

    public static float smoothNoise(int x, int y)
    {
        float corners = ( noise2D(x - 1, y - 1)+noise2D(x + 1, y - 1)+noise2D(x - 1, y + 1)+noise2D(x+1, y+1) ) / 16;
        float sides   = ( noise2D(x - 1, y)  +noise2D(x + 1, y)  +noise2D(x, y - 1)  +noise2D(x, y +1) ) /  8;
        float center  =  noise2D(x, y) / 4;
        return corners + sides + center;
    }

    public  static float interpolate(float x, float y, float a)
    {
        return x*(1-a) + y*a;
    }

    public static float interpolatedNoise(float x, float y)
    {
        float fractional_X = x - (float)Math.floor(x);

        float fractional_Y = y - (float)Math.floor(y);

        float v1 = smoothNoise((int) Math.floor(x),    (int) Math.floor(y));
        float v2 = smoothNoise((int) Math.floor(x)+ 1, (int) Math.floor(y));
        float v3 = smoothNoise((int) Math.floor(x),    (int) Math.floor(y) + 1);
        float v4 = smoothNoise((int) Math.floor(x) + 1,(int) Math.floor(y) + 1);

        float i1 = interpolate(v1, v2, fractional_X);
        float i2 = interpolate(v3, v4, fractional_X);

        return interpolate(i1 , i2 , fractional_Y);
    }

    public float octave(float x, float y, int octave)
    {
        float frequency = (float) Math.pow(2, octave);
        float amplitude = (float)Math.pow(persistence, octave+1);

        return interpolatedNoise(x * frequency, y * frequency) * amplitude;
    }

    public float perlinNoise2D(float x, float y)
    {
        return octave(x, y, 0) + octave(x, y, 1) + octave(x, y, 2) + octave(x, y, 3);
    }


    public Image getNoiseTexture(int w, int h, boolean setAlpha)
    {
        PerlinNoise.Seed seed = new PerlinNoise.Seed();
        return getNoiseTexture(w, h, setAlpha, seed);
    }

    public Image getNoiseTexture(int w, int h, boolean setAlpha, Seed seed)
    {
        Image img = new Image(w, h, BufferedImage.TYPE_4BYTE_ABGR);

        float allValues [] = new float[w*h];
        float max = 0;
        float min = 0;

        for(int i =0 ; i < w; i++)
        {
            for(int j =0; j < h ; j++)
            {
                float v = perlinNoise2D(((float) i+ seed.offsety)*seed.scalex, ((float) j + seed.offsetx) *seed.scaley);
                allValues[i*h+j] = v;
                if(v > max)
                {
                    max  = v;
                }
                if(v < min)
                {
                    min = v;
                }
            }
        }

        for(int i =0 ; i < w*h; i++)
        {
            float v  = allValues[i];
            v -= min;
            v /= (max - min);

            allValues[i] = v;
        }

        for(int i =0 ; i < w ; i++)
        {
            for(int j =0 ; j < h ; j++)
            {
                float v = allValues[i*h +j];
                v = v >= 1.0f ? 1.0f : v;
                v = v <= 0.0f ? 0.0f : v;
                img.setRGB(i, j, Image.getRGB(v, v, v, setAlpha ? v : 1.0f));
            }
        }
        return img;
    }

    public Image getOctave(int octave, int w, int h, boolean setAlpha)
    {
        return getOctave(octave, w, h, setAlpha, new Seed());
    }

    public Image getOctave(int octave, int w, int h, boolean setAlpha, Seed seed)
    {
        Image img = new Image(w, h, BufferedImage.TYPE_4BYTE_ABGR);

        float allValues [] = new float[w*h];
        float max = 0;
        float min = 0;

        for(int i =0 ; i < w; i++)
        {
            for(int j =0; j < h ; j++)
            {
                float v = octave(((float) i+ seed.offsety)*seed.scalex, ((float) j + seed.offsetx) *seed.scaley, octave);
                allValues[i*h+j] = v;
                if(v > max)
                {
                    max  = v;
                }
                if(v < min)
                {
                    min = v;
                }
            }
        }

        for(int i =0 ; i < w*h; i++)
        {
            float v  = allValues[i];
            v -= min;
            v /= (max - min);

            allValues[i] = v;
        }

        for(int i =0 ; i < w ; i++)
        {
            for(int j =0 ; j < h ; j++)
            {
                float v = allValues[i*h +j];
                v = v >= 1.0f ? 1.0f : v;
                v = v <= 0.0f ? 0.0f : v;
                img.setRGB(i, j, Image.getRGB(v, v, v, setAlpha ? v : 1.0f));
            }
        }
        return img;

    }

    public static void main(String [] args)
    {
        PerlinNoise perlin = new PerlinNoise();
        Image img = perlin.getNoiseTexture(800, 600, false);
        img.save("images/Perlin.png");

        img.thresholdAlpha(0.1f, true);
        img.save("images/Perlin2.png");

        Image oct = perlin.getOctave(4, 800, 600, false);
        oct.save("images/octave_4.png");

    }

}

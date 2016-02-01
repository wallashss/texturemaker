package br.attalea.core;

public class Vector3 {
	
	public float x, y, z;
	
	public static final Vector3 right = new Vector3(1,0,0);
	public static final Vector3 left = new Vector3(-1,0,0);
	public static final Vector3 up = new Vector3(0,1,0);
	public static final Vector3 down = new Vector3(0,-1,0);
	public static final Vector3 foward = new Vector3(0,0,1);
	public static final Vector3 back = new Vector3(0,0,-1);
	public static final Vector3 zero = new Vector3(0,0,0);

	public Vector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float length()
	{
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	public float dot(Vector3 second)
	{
		return second.x *x + second.y * y + second.z *z;
	}
	
	public Vector3 cross(Vector3 b)
	{
	   // a[1] * b[2] - a[2] * b[1], 
	   // a[2] * b[0] - a[0] * b[2], 
	   // a[0] * b[1] - a[1] * b[0]) 
		return new Vector3(y*b.z - z*b.y,
						   z*b.x - x*b.z,
						   x*b.y - y*b.x);
	}
	public Vector3 diff(Vector3 b)
	{
		return new Vector3(x-b.x,y-b.y,z-b.z);
	}
	
	public float distance(Vector3 b)
	{
		return diff(b).length();
	}
	
	public void normalize()
	{
		float length = length();
		x = x/length;
		y = y/length;
		z = z/length;
	}
	
	public Vector3 mul(float a)
	{
		return new Vector3(a*x,a*y,a*z);
	}
	
	public Vector3 normalized()
	{
		float length = length();
		return new Vector3(x/length,y/length,z/length);
	}
	
	public Vector3 reflect(Vector3 n)
	{
		return diff( diff(n.mul(dot(n)).mul(2)) ); // i - 2. * dot(n, i) * n
	}
	
	public Vector3 minus(Vector3 a)
	{
		return new Vector3(x - a.x, y - a.y , z - a.z);
	}
	
	public Vector3 plus(Vector3 a)
	{
		return new Vector3(x + a.x, y + a.y , z + a.z);
	}
}

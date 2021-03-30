package staticFunctions;



public class FixedPoint 
{
	
	
	public static double findRoot (double epsilon)
	{
		System.out.println("HALO");
		double xn = 3;
		int i=0;
		
		while (Math.abs(xn - func(xn)) > epsilon)
		{
			xn = func(xn);
			System.out.println("i = " + i);
			System.out.println("x_{n} = " + xn +"\n");
		}
		return func(xn);
	}
	
	
	// if x-x^(1/3)-2 = 0 then we need in form x = f(x) ---> func(x) = x^(1/3)-2;
	private static double func (double x)
	{
		return Math.pow(x, 1.0/3.0) + 2.0;
	}
}
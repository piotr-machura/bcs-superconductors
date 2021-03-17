package package1;

public class Test1 
{
	double xVal;
	double yVal;
	
	public Test1(double x, double y) 
	{
		xVal = x;
		yVal = y;
	}
	
	public double returnVectorLength()
	{
		return Math.sqrt(Math.pow(xVal, 2) + Math.pow(yVal, 2));
	}

}

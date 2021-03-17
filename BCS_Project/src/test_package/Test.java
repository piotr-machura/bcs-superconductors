package test_package;

public class Test 
{
	double xVal;
	double yVal;
	
	public Test(double x, double y) 
	{
		xVal = x;
		yVal = y;
	}
	
	public double returnVectorLength()
	{
		return Math.sqrt(Math.pow(xVal, 2) + Math.pow(yVal, 2));
	}
	
}

package staticFunctions;

import java.util.Random;

public class StringAndNumberFunctions 
{
	@SuppressWarnings("deprecation")
	public static Double [] getValueFromArrayOfStrings (String [] arrayOfStrings) 
	{
		if (arrayOfStrings==null)
		{
			System.out.println("ERROR, EMPTY ARRAY OF STRINGS");
			return (null);
		}
		else
		{
			Double arrayOfDoubels [] = new Double [arrayOfStrings.length];
			for (int i=0; i<arrayOfStrings.length; i++)
			{
				arrayOfDoubels[i] = new Double(arrayOfStrings[i]);
			}
			return (arrayOfDoubels);
		}
	}
	
	public static String [] replaceTextInArrayOfStrings (String newText, String oldText, String [] arrayOfStrings)
	{
		for (int i=0; i<arrayOfStrings.length; i++)
		{
			arrayOfStrings[i] = arrayOfStrings[i].replaceAll(oldText, newText);
		}
		return arrayOfStrings;
	}
	
	public static void printArrayOfStrings (String arrayOFStrings[])
	{
		for (int i=0; i<arrayOFStrings.length; i++)
		{
			System.out.println(arrayOFStrings[i]);
		}
	}
	
	public static void printArrayOfDoubles (Double arrayOFDoubles[])
	{
		for (int i=0; i<arrayOFDoubles.length; i++)
		{
			System.out.println(arrayOFDoubles[i]);
		}
	}
	
	public static Double [] mixArrayOfDoubles (Double arrayOfDoubles[])
	{
		Random r = new Random();
		int range = arrayOfDoubles.length;
		Double [] mixedArrayOfDoubles = new Double [arrayOfDoubles.length];
		for (int i=0; i<arrayOfDoubles.length; i++)
		{
			int index = r.nextInt(range);
			mixedArrayOfDoubles[i] = arrayOfDoubles[index];
			for (int j=index ; j < arrayOfDoubles.length - 1; j++)
			{
				arrayOfDoubles[j] = arrayOfDoubles[j+1];
			}
			arrayOfDoubles[arrayOfDoubles.length-1] = mixedArrayOfDoubles[i];
			range--;
		}
			
		return (mixedArrayOfDoubles);
	}
	
	public static String [] replaceCharackterInArrayOfStrings (Character newChar, Character oldChar, String [] arrayOfStrings)
	{
		for (int i=0; i<arrayOfStrings.length; i++)
		{
			arrayOfStrings[i] = arrayOfStrings[i].replace(oldChar, newChar);
		}
		return arrayOfStrings;
	}
	
	public static Double[] getAbsOfDoublesInArray(Double[] arrayOFDoubles)
	{
		Double[] result = new Double[arrayOFDoubles.length];
		for (int i=0; i<arrayOFDoubles.length; i++)
		{
			result[i] = Math.abs(arrayOFDoubles[i]);
		}
		return result;
	}
	
	public static Double[] normalizeArrayOfDoubles(Double[] arrayOFDoubles)
	{
		Double sum=0.0;
		for (int i=0; i<arrayOFDoubles.length; i++)
		{
			sum = sum + arrayOFDoubles[i];
		}
		
		
		if(sum !=0)
		{
			Double[] result = new Double[arrayOFDoubles.length];
			for (int i=0; i<arrayOFDoubles.length; i++)
			{
				result[i] = arrayOFDoubles[i]/sum;
			}
			return result;
		}
		else 
		{
			Double[] result = {0.0};
			return result;
		}
	}
	
	public static Double[] normalizeArrayOfDoublesOfFFT(Double[] arrayOFDoubles)
	{
		Double sum=0.0;
		for (int i=1; i<arrayOFDoubles.length; i++)
		{
			sum = sum + arrayOFDoubles[i];
		}
		
		
		if(sum !=0)
		{
			Double[] result = new Double[arrayOFDoubles.length];
			result[0] = arrayOFDoubles[0]/arrayOFDoubles.length; 
			for (int i=1; i<arrayOFDoubles.length; i++)
			{
				result[i] = arrayOFDoubles[i]/sum;
			}
			return result;
		}
		else 
		{
			Double[] result = {0.0};
			return result;
		}
	}
}

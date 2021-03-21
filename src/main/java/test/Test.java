package test;

import java.io.File;
import java.io.IOException;
import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.utils.IOUtils;

public class Test 
{

	public static void main(String[] args)
	{
		int N = 500;
    	double[] signal = new double [N];
    	//First we define a simple signal containing an addition of two sine waves. One with a frequency of 40 Hz and one with a frequency of 90 Hz.
    	//Let domain to be  interval <0, 0.5> and use N samples
    	for (int i=0; i<N; i++)
    	{
    		double x = (double) i * (0.5/N);
    		signal[i] =0.7+ Math.sin(40*2*Math.PI * x) + 0.5*Math.sin(90*2*Math.PI * x) ;
    		//System.out.println(signal[i] );
    	}
    	Double[] signalTransformed = getArrayOfFFT_1D(signal);
    	signalTransformed= staticFunctions.StringAndNumberFunctions.getAbsOfDoublesInArray(signalTransformed);
    	signalTransformed = staticFunctions.StringAndNumberFunctions.normalizeArrayOfDoublesOfFFT(signalTransformed);
    	staticFunctions.StringAndNumberFunctions.printArrayOfDoubles(signalTransformed);
	}
	
	
	public static Double[] getArrayOfFFT_1D (double[] signal)
	{
		Double[] result;
		
		DoubleFFT_1D dFFT = new DoubleFFT_1D(signal.length);
    	dFFT.realForward(signal);
    	
		String path = "plik.txt";
		IOUtils.writeToFileReal_1D(signal, path);
		
		String[] liczbyJakoTekst = null;
    	try 
    	{
    		liczbyJakoTekst = staticFunctions.FileFunctions.getWordsFromTextFile(path);
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    	liczbyJakoTekst = staticFunctions.StringAndNumberFunctions.replaceCharackterInArrayOfStrings('.', ',', liczbyJakoTekst);
    	result = staticFunctions.StringAndNumberFunctions.getValueFromArrayOfStrings(liczbyJakoTekst);
    	
    	File myObj = new File(path);
    	myObj.delete();
    	
		return result;
	}

}

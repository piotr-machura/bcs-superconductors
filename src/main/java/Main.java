/**
 * The class Main
 *
 * @Author Bartosz Biernacki, Piotr Machura
 **/
import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.utils.IOUtils;

public class Main 
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
    		signal[i] = Math.sin(40*2*Math.PI * x) + 0.5*Math.sin(90*2*Math.PI * x) ;
    		//System.out.println(i + "\t"+ signal[i] );
    	}
    
    
    	DoubleFFT_1D dFFT = new DoubleFFT_1D(signal.length);
    	dFFT.realForward(signal);
    	
    	 // Extract Real part
        double[] result = new double[signal.length/2];
    	for(int s = 0; s < result.length; s++) 
    	{
           double re = signal[s * 2];
           double im = signal[s * 2 + 1];
           result[s] = (double) Math.sqrt(re * re + im * im) / result.length; 
     	}
     	
     	for (int i=0; i<result.length; i++)
     	{
     		//System.out.println(result[i]);
     	}
    
       IOUtils.showReal_1D(signal, "Result");
       IOUtils.writeToFileReal_1D(signal, "src/FOLDER/plik.txt");
    }
}

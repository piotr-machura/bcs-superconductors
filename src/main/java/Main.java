import org.jtransforms.fft.DoubleFFT_1D;
import org.jfree.chart.ChartFrame;



import org.jfree.chart.ChartFactory;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * The class Main
 *
 * @Author Bartosz Biernacki, Piotr Machura
 **/
public class Main {
    public static void main(String[] args) {
        int samples = 1024;
        double[] signal = new double[samples];
        double[] freq = new double[samples];
        double[] time = new double[samples];
        double[] testSignal = new double [2*samples];
        // First we define a simple signal containing an addition of two sine waves. One
        // with a frequency of 40 Hz and one with a frequency of 90 Hz.
        // Let domain to be interval <a, b> and use N samples

        double a=0, b=0.2;



        for (int i = 0; i < samples; i++) {
            double t = a + 2 * (double) i * ((b - a) / samples);
            time[i] = t; // Time is just time
            freq[i] = i; // Sampling rate is frequency

            signal[i] = Math.sin(40 * 2 * Math.PI * t) + 0.5 * Math.sin(90 * 2 * Math.PI * t) + 2 * Math.cos(100 * 2 * Math.PI * t);
            testSignal[2*i] = Math.sin(40 * 2 * Math.PI * t) + 0.5 * Math.sin(90 * 2 * Math.PI * t) + 2 * Math.cos(100 * 2 * Math.PI * t);
            testSignal[2*i +1] = 0;
            //signal[i]= Math.exp(-2*Math.pow(t, 2)); 

            signal[i] = Math.sin(40 * 2 * Math.PI * t) + 0.5 * Math.sin(90 * 2 * Math.PI * t)
                    + 2 * Math.cos(100 * 2 * Math.PI * t);
            // signal[i]= Math.exp(-2*Math.pow(t, 2));
        }

        // The signal will be changed by the FFT but we want to plot it later
        double[] function = signal.clone();

        DoubleFFT_1D dFFT = new DoubleFFT_1D(signal.length);
        dFFT.realForward(signal);
        
        DoubleFFT_1D dFFTComplex = new DoubleFFT_1D(testSignal.length/2);
        dFFTComplex.complexForward(testSignal);
        
        
        // Get the absolute value
        double[] result = new double[signal.length / 2];
        for (int s = 0; s < result.length; s++) {
            double re = signal[s * 2];
            double im = signal[s * 2 + 1];
            result[s] = (double) Math.sqrt(re * re + im * im) / result.length;
        }
        
        
        double[] realPartOfTransformedTestSignal = new double[testSignal.length/2];
        double[] imaginaryPartOfTransformedTestSignal = new double[testSignal.length/2];
        for (int s = 0; s < testSignal.length/2; s++) 
        {
            realPartOfTransformedTestSignal[s] = testSignal[2*s] / result.length;
            imaginaryPartOfTransformedTestSignal[s]=  testSignal[2*s +1] / result.length;
        }

        
        
        // Construct the graph series
        XYSeries fftSeries = new XYSeries("FFT");
        XYSeries initialSeries = new XYSeries("function");
        for (int s = 0; s < result.length; s++) {
            // Because of unknown reason frequency has to be scaled by 1/(2*domain_size)
            fftSeries.add(freq[s] * 1 / (2 * (b - a)), result[s]);
            initialSeries.add(time[s], function[s]);
        }
        
        XYSeries testFftRealSeries = new XYSeries("Test_real");
        XYSeries testFftImaginarySeries = new XYSeries("Test_real");
        for (int s = 0; s < realPartOfTransformedTestSignal.length; s++) 
        {
        	// Because of unknown reason frequency has to be scaled by	1/(2*domain_size)
            testFftRealSeries.add(freq[s] * 1/(2*(b-a)), realPartOfTransformedTestSignal[s]);
            testFftImaginarySeries.add(freq[s] * 1/(2*(b-a)), imaginaryPartOfTransformedTestSignal[s]);
        }
        
     


        // Display the graphs
        ChartFrame frame1 = new ChartFrame("XYLine Chart",
                ChartFactory.createXYLineChart("FFT", "frequency", "amplitude", new XYSeriesCollection(fftSeries)));
        frame1.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        staticFunctions.PlotFunctions.setMeaningfulXAxisRange(frame1, a, b);
        frame1.setVisible(true);
        frame1.setSize(500, 400);
        ChartFrame frame2 = new ChartFrame("XYLine Chart",
                ChartFactory.createXYLineChart("Function", "time", "amplitude", new XYSeriesCollection(initialSeries)));
        frame2.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);
        frame2.setSize(500, 400);
        
        ChartFrame frame3 = new ChartFrame("XYLine Chart",
                ChartFactory.createXYLineChart("Test_FFT_real", "frequency", "amplitude", new XYSeriesCollection(testFftRealSeries)));
        frame3.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
        frame3.setLocationRelativeTo(null);
        staticFunctions.PlotFunctions.setMeaningfulXAxisRange(frame3,a,b);
        frame3.setVisible(true);
        frame3.setSize(500, 400);
        ChartFrame frame4 = new ChartFrame("XYLine Chart",
                ChartFactory.createXYLineChart("Test_FFT_imaginary", "frequency", "amplitude", new XYSeriesCollection(testFftImaginarySeries)));
        frame4.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
        frame4.setLocationRelativeTo(null);
        staticFunctions.PlotFunctions.setMeaningfulXAxisRange(frame3,a,b);
        frame4.setVisible(true);
        frame4.setSize(500, 400);
        }
}

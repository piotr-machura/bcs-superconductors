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
        int samples = 1000;
        double[] signal = new double[samples];
        double[] freq = new double[samples];
        double[] time = new double[samples];
        // First we define a simple signal containing an addition of two sine waves. One
        // with a frequency of 40 Hz and one with a frequency of 90 Hz.
        // Let domain to be interval <0, 0.5> and use N samples
        for (int i = 0; i < samples; i++) {
            double t = (double) i * (0.5 / samples);
            time[i] = t; // Time is just time
            freq[i] = i; // Sampling rate is frequency
            signal[i] = Math.sin(40 * 2 * Math.PI * t) + 0.5 * Math.sin(90 * 2 * Math.PI * t);
        }
        // The signal will be changed by the FFT but we want to plot it later
        double[] function = signal.clone();

        DoubleFFT_1D dFFT = new DoubleFFT_1D(signal.length);
        dFFT.realForward(signal);

        // Get the absolute value
        double[] result = new double[signal.length / 2];
        for (int s = 0; s < result.length; s++) {
            double re = signal[s * 2];
            double im = signal[s * 2 + 1];
            result[s] = (double) Math.sqrt(re * re + im * im) / result.length;
        }

        // Construct the graph series
        XYSeries fftSeries = new XYSeries("FFT");
        XYSeries initialSeries = new XYSeries("function");
        for (int i = 0; i < result.length; i++) {
            // Frequency is doubled since `result` is half the size of initial signal
            fftSeries.add(freq[i] * 2, result[i]);
            initialSeries.add(time[i], function[i]);
        }

        // Display the graphs
        ChartFrame frame1 = new ChartFrame("XYLine Chart",
                ChartFactory.createXYLineChart("FFT", "frequency", "amplitude", new XYSeriesCollection(fftSeries)));
        frame1.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);
        frame1.setSize(500, 400);
        ChartFrame frame2 = new ChartFrame("XYLine Chart",
                ChartFactory.createXYLineChart("Function", "time", "amplitude", new XYSeriesCollection(initialSeries)));
        frame2.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);
        frame2.setSize(500, 400);
    }
}

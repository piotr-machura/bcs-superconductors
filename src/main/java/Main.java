import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * The class Main
 *
 *
 * @Author Bartosz Biernacki, Piotr Machura
 **/
public class Main {
    // The calculation parameters
    final static double g = 1.0, h_bar = 1.0, Boltzman_constant = 1.0;
    final static double deltaPrecision = 1e-6;
    final static double almostZero = 1e-4;
    // Our line space
    final static double dx = 1e-4;
    final static int nX = 2 * (int) 1e4;

    static double epsilonK(double k, double mass) {
        return (h_bar * h_bar * k * k) / (2 * mass);
    }

    static double uK2(double k, double Ek, double mass, double mu) {
        return 0.5 * (1.0 + ((epsilonK(k, mass) - mu) / Ek));
    }

    static double vK2(double k, double Ek, double mass, double mu) {
        return 0.5 * (1.0 - ((epsilonK(k, mass) - mu) / Ek));
    }

    static double uKvK(double k, double Ek, double mass, double mu) {
        double underSqrt = 0.25 * (1.0 - ((epsilonK(k, mass) - mu) / Ek)) * (1.0 + ((epsilonK(k, mass) - mu) / Ek));
        // Guard ourselves against nasty NaNs
        if (Math.abs(underSqrt) < 1e-4) {
            underSqrt = 0;
        }
        return Math.sqrt(underSqrt);
    }

    static double eK(double delta, double k, double mass, double mu) {
        return Math.sqrt(delta * delta + (epsilonK(k, mass) - mu) * (epsilonK(k, mass) - mu));
    }

    static double fermiDirac(double E, double mu, double T) {
        return 1.0 / (1.0 + Math.exp((E - mu) / (Boltzman_constant * T)));
    }

    static double calcDelta(ArrayList<Double> ks, ArrayList<Double> Eks, double mass, double mu, double T) {
        // Arrays are needed for the summing
        double sum = 0;
        for (int i = 0; i < ks.size(); i++) {
            double k = ks.get(i);
            double Ek = Eks.get(i);
            sum += g * (uKvK(k, Ek, mass, mu) * 0.5 * (fermiDirac(-Ek, mu, T) - fermiDirac(Ek, mu, T)));
        }
        return sum;
    }

    static double convergeDelta(double mass, double mu, double T) {
        // calculate the k's (needed only one per one delta )
        ArrayList<Double> ks = new ArrayList<Double>();
        for (int i = 0; i < nX; i++) {
            double kX = 2 * Math.PI / (nX * dx);
            if (i <= nX / 2) {
                kX *= i;
            } else {
                kX *= (i - nX);
            }
            // kY and kZ are the same
            double k = Math.sqrt( 3* kX * kX );
            ks.add(k);
        }

        // Take a guess
        double delta = 1.0;
        double deltaPrev = delta + 1e5; // Not to trigger the stop condition immediately
        // Helpful later
        double currentN = 0;
        int iterations = 0;

        // Delta is calculated in this loop
        while (true) {
            // Get the energies
            ArrayList<Double> eKs = new ArrayList<Double>();
            double newN = 0;
            // Calculate the Ek's and Number of particles
            for (int i = 0; i < ks.size(); i++) {
                eKs.add(eK(delta, ks.get(i), mass, mu));
                newN +=  vK2(ks.get(i), eKs.get(i), mass, mu) * fermiDirac(-eKs.get(i), mu, T)
                        + uK2(ks.get(i), eKs.get(i), mass, mu) * fermiDirac(eKs.get(i), mu, T);
                
              //newN += vK2(ks.get(i), eKs.get(i), mass, mu) - uK2(ks.get(i), eKs.get(i), mass, mu) +1;
            }
            // On the first iteration we don't know what the N is, so dont mix the mu yet
            if (iterations != 0) {
                double beta = 0.25;
                mu = mu + beta * (currentN - newN);
            }
            currentN = newN;
            //System.out.println(currentN);

            // Get our new delta
            delta = calcDelta(ks, eKs, mass, mu, T);
            if (Math.abs(deltaPrev - delta) < deltaPrecision) {
                // We have converged - done
            	System.out.println("Delta converged after " + iterations + " to " + delta);
                return delta;
            } else {
                // Mixed step that should speed up convergence, but it doesn't
                 //double alpha = 0.5;
                 //deltaPrev = alpha * delta + (1 - alpha) * deltaPrev;
                 deltaPrev = delta;
                iterations++;
                if(iterations >500)
                {
                	// System.out.println(delta);
                }
            }
        }
    }

    static void plotDelta(ArrayList<Double> deltas, ArrayList<Double> Ts) {
        // Construct the graph series
        XYSeries xySeries = new XYSeries("Delta vs T");
        for (int i = 0; i < deltas.size(); i++) {
            xySeries.add(Ts.get(i), deltas.get(i));
        }

        // Display the graphs
        ChartFrame frame1 = new ChartFrame("XYLine Chart", ChartFactory.createXYLineChart("Delta vs T", "Temperature",
                "Delta value", new XYSeriesCollection(xySeries)));
        frame1.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);
        frame1.setSize(500, 400);
    }

    static void verifyRatio(double mass, double mu, double Tc) {

    }

    public static void main(String[] args) {
        ArrayList<Double> Ts = new ArrayList<Double>();
        ArrayList<Double> deltas = new ArrayList<Double>();
        double mass = 1.0, mu = 1e-6, T = 1e-6; // T_0 = almost zero
        for (int i = 0; i < 1e7; i++) {
            T = T*1.01;
            Ts.add(T);
            deltas.add(convergeDelta(mass, mu, T));
            /*
             * Delta is almost zero --> break Tc value is highly dependent on numerucal zero
             * for 0=1e-8 -->Tc = 0.1409, for 0=1e-10 -->Tc = 0.6395, for 0=1e-10 -->Tc =
             * 6.0335, for 0=0 -->Tc = 239140.76
             */
            if (deltas.get(i) <= almostZero) {
                System.out.println("For T = " + T + " delta <= 0");
                System.out.println("Checked " + i + " different temperatures to match.");
                plotDelta(deltas, Ts);
                double ratio = convergeDelta(mass, mu, almostZero) / T;
                System.out.println("Ratio delta(T=0) / Tc supposed to be 1.76. In our case it is " + ratio);
                break;
            }

        }
    }
}

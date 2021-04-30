import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * The class Main
 *
 * @Author Bartosz Biernacki, Piotr Machura
 **/
public class Main {
    // The calculation parameters
    final static double gCoefficient = 1.0, hBar = 1.0, kBoltzmannn = 1.0, mass = 1.0;
    final static double deltaPrecision = 1e-6;
    final static double deltaAlmostZero = 1e-4;
    // Spatial grid
    final static double dX = 1e-4;
    final static int nX = 1 * (int) 1e2;
    static ArrayList<Double> kGrid = getKGrid();
    // Other parameters
    static double nParticles = 0;

    /**
     * Establish the wave vector grid used in the calculations. Only needed once per
     * simulation.
     *
     * @return array containing wave vector lengths at each point in the grid.
     */
    static ArrayList<Double> getKGrid() {
        ArrayList<Double> ks = new ArrayList<Double>();
        for (int ix = 0; ix < nX; ix++) {
            double kX = 2 * Math.PI / (nX * dX);
            if (ix <= nX / 2) {
                kX *= ix;
            } else {
                kX *= (ix - nX);
            }
            for (int iy = 0; iy < nX; iy++) {
                double kY = 2 * Math.PI / (nX * dX);
                if (iy <= nX / 2) {
                    kY *= iy;
                } else {
                    kY *= (iy - nX);
                }
                for (int iz = 0; iz < nX; iz++) {
                    double kZ = 2 * Math.PI / (nX * dX);
                    if (iz <= nX / 2) {
                        kZ *= iz;
                    } else {
                        kZ *= (iz - nX);
                    }
                    double k = Math.sqrt(kX * kX + kY * kY + kZ * kZ);
                    ks.add(k);
                }
            }
        }
        return ks;
    }

    /**
     * Calculate the kinetic energy from wave vector k.
     *
     * @param k the wave vector.
     * @return the epsilonK.
     */
    static double epsilonK(double k) {
        return (hBar * hBar * k * k) / (2 * mass);
    }

    /**
     * Calculate the square of the u wave function.
     *
     * @param k  the wave vector.
     * @param eK the energy associated with wave vector k.
     * @param mu the chemical potential.
     * @return the uK^2
     */
    static double uK2(double k, double eK, double mu) {
        return 0.5 * (1.0 + ((epsilonK(k) - mu) / eK));
    }

    /**
     * Calculate the square of the v wave function.
     *
     * @param k  the wave vector.
     * @param eK the energy associated with wave vector k.
     * @param mu the chemical potential.
     * @return the vK^2.
     */
    static double vK2(double k, double eK, double mu) {
        return 0.5 * (1.0 - ((epsilonK(k) - mu) / eK));
    }

    /**
     * Calculate the product of the u and v wave functions.
     *
     * @param k  the wave vector.
     * @param eK the energy associated with wave vector k.
     * @param mu the chemical potential.
     * @return the product uk*vk.
     */
    static double uKvK(double k, double eK, double mu) {
        double underSqrt = 0.25 * (1.0 - ((epsilonK(k) - mu) / eK)) * (1.0 + ((epsilonK(k) - mu) / eK));
        // Guard ourselves against nasty NaNs
        if (Math.abs(underSqrt) < 1e-4) {
            underSqrt = 0;
        }
        return Math.sqrt(underSqrt);
    }

    /**
     * Calculate the energy associated with wave vector k.
     *
     * @param delta the energy gap
     * @param k     the wave vector.
     * @param mu    the chemical potential.
     * @return the energy Ek.
     */
    static double eK(double delta, double k, double mu) {
        return Math.sqrt(delta * delta + (epsilonK(k) - mu) * (epsilonK(k) - mu));
    }

    /**
     * Calculate the energy associated with wave vector k.
     *
     * @param E  the energy.
     * @param T  the temperature.
     * @param mu the chemical potential.
     * @return the value of Fermi-Dirac function.
     */
    static double fermiDirac(double E, double mu, double T) {
        return 1.0 / (1.0 + Math.exp((E - mu) / (kBoltzmannn * T)));
    }

    /**
     * Calculate the energy gap from parameters.
     *
     * @param eKs the energies associated with wave vectors.
     * @param mu  the chemical potential.
     * @param T   the temperature.
     * @return the energy gap delta.
     */
    static double calcDelta(ArrayList<Double> eKs, double mu, double T) {
        // Arrays are needed for the summing
        double sum = 0;
        for (int i = 0; i < kGrid.size(); i++) {
            double k = kGrid.get(i);
            double eK = eKs.get(i);
            sum += gCoefficient * (uKvK(k, eK, mu) * 0.5 * (fermiDirac(-eK, mu, T) - fermiDirac(eK, mu, T)));
        }
        return sum;
    }

    /**
     * Calculate the energy gap until convergence.
     *
     * @param mu the chemical potential.
     * @param T  the temperature.
     * @return the converged energy gap delta.
     */
    static double convergeDelta(double mu, double T) {
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
            for (int i = 0; i < kGrid.size(); i++) {
                eKs.add(eK(delta, kGrid.get(i), mu));
                newN += vK2(kGrid.get(i), eKs.get(i), mu) * fermiDirac(-eKs.get(i), mu, T)
                        + uK2(kGrid.get(i), eKs.get(i), mu) * fermiDirac(eKs.get(i), mu, T);
            }
            // On the first iteration we don't know what the N is, so dont mix the mu yet
            if (iterations != 0) {
                double beta = 0.25;
                mu = mu + beta * (currentN - newN);
            }
            currentN = newN;

            // Get our new delta
            delta = calcDelta(eKs, mu, T);
            if (Math.abs(deltaPrev - delta) < deltaPrecision) {
                // We have converged - done
                System.out.printf("T: %.6f\tDelta: %.6f\t\t[took %d iterations]%n", T, delta, iterations);
                nParticles = currentN;
                return delta;
            } else {
                if (iterations > 100) {
                    // There already have been lots of iterations -> mix
                    double alpha = 0.25;
                    deltaPrev = alpha * delta + (1 - alpha) * deltaPrev;
                } else {
                    deltaPrev = delta;
                }
                iterations++;
            }
        }

    }

    /**
     * Draw and display the delta vs T and delta/Fermi Energy vs T plots.
     *
     * @param deltas the deltas corresponding the temperatures.
     * @param T      the temperatures.
     */
    static void drawPlots(ArrayList<Double> deltas, ArrayList<Double> Ts) {
        // Construct the graph series
        XYSeries deltaTSeries = new XYSeries("Delta vs T");
        for (int i = 0; i < deltas.size(); i++) {
            deltaTSeries.add(Ts.get(i), deltas.get(i));
        }
        double volume = Math.pow(nX * dX, 3);
        double fermiE = hBar * hBar / (2 * mass);
        fermiE *= Math.pow(3 * Math.PI * Math.PI * nParticles / volume, (double) 2 / 3);
        XYSeries fermiDeltaTSeries = new XYSeries("Delta / Fermi energy vs T");
        for (int i = 0; i < deltas.size(); i++) {
            Double deltaOverFermiE = deltas.get(i) / fermiE;
            fermiDeltaTSeries.add(Ts.get(i), deltaOverFermiE);
        }

        // Display the graphs
        ChartFrame frame1 = new ChartFrame("XYLine Chart", ChartFactory.createXYLineChart("Delta vs T", "Temperature",
                "Delta value", new XYSeriesCollection(deltaTSeries)));
        frame1.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);
        frame1.setSize(500, 400);
        ChartFrame frame2 = new ChartFrame("XYLine Chart", ChartFactory.createXYLineChart("Delta / Fermi Energy vs T",
                "Temperature", "Delta / Fermi Energy", new XYSeriesCollection(fermiDeltaTSeries)));
        frame2.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);
        frame2.setSize(500, 400);
    }

    public static void main(String[] args) {
        ArrayList<Double> Ts = new ArrayList<Double>();
        ArrayList<Double> deltas = new ArrayList<Double>();
        double mu = 1e-6, T = 1e-6; // T_0 = almost zero
        System.out.println("BEGIN SIMULATION");
        System.out.println("----------------");
        getKGrid();
        for (int i = 0; i < 1e7; i++) {
            T = T * 1.05;
            Ts.add(T);
            deltas.add(convergeDelta(mu, T));
            if (deltas.get(i) <= deltaAlmostZero) {
                System.out.printf("For T = %.6f delta ~ 0%n", T);
                System.out.println("Checked " + i + " different temperatures.");
                drawPlots(deltas, Ts);
                double ratio = deltas.get(0) / T;
                System.out.printf("Ratio delta(T=0) / Tc: %.6f%n", ratio);
                break;
            }
        }
    }
}

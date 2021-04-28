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
    final static double g = 1.0, h_bar = 1.0, Boltzman_constant = 1.0;
    final static double deltaPrecision = 1e-6;
    final static double almostZero = 1e-4;
    // Our line space
    final static double dx = 1e-4;
    final static int nX = 6 * (int) 1e1;
    static ArrayList<Double> ks = establishKs();

    /**
     * Establishes the wave vector grid used in the calculations. Only needed once
     * per simulation.
     *
     * @return Array containing wave vector lengths at each point in the grid.
     **/
    static ArrayList<Double> establishKs() {
        // calculate the k's (needed only once)
        ArrayList<Double> ks = new ArrayList<Double>();
        for (int ix = 0; ix < nX; ix++) {
            double kX = 2 * Math.PI / (nX * dx);
            if (ix <= nX / 2) {
                kX *= ix;
            } else {
                kX *= (ix - nX);
            }
            for (int iy = 0; iy < nX; iy++) {
                double kY = 2 * Math.PI / (nX * dx);
                if (iy <= nX / 2) {
                    kY *= iy;
                } else {
                    kY *= (iy - nX);
                }
                for (int iz = 0; iz < nX; iz++) {
                    double kZ = 2 * Math.PI / (nX * dx);
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
     * @param k    the wave vector.
     * @param mass the mass of the particle.
     * @return the epsilonK.
     **/
    static double epsilonK(double k, double mass) {
        return (h_bar * h_bar * k * k) / (2 * mass);
    }

    /**
     * Calculate the square of the u wave function.
     *
     * @param k    the wave vector.
     * @param Ek   the energy associated with wave vector k.
     * @param mass the mass of the particle.
     * @param mu   the chemical potential.
     * @return the uK^2
     **/
    static double uK2(double k, double Ek, double mass, double mu) {
        return 0.5 * (1.0 + ((epsilonK(k, mass) - mu) / Ek));
    }

    /**
     * Calculate the square of the v wave function.
     *
     * @param k    the wave vector.
     * @param Ek   the energy associated with wave vector k.
     * @param mass the mass of the particle.
     * @param mu   the chemical potential.
     * @return the vK^2.
     **/
    static double vK2(double k, double Ek, double mass, double mu) {
        return 0.5 * (1.0 - ((epsilonK(k, mass) - mu) / Ek));
    }

    /**
     * Calculate the product of the u and v wave functions.
     *
     * @param k    the wave vector.
     * @param Ek   the energy associated with wave vector k.
     * @param mass the mass of the particle.
     * @param mu   the chemical potential.
     * @return the product uk*vk.
     **/
    static double uKvK(double k, double Ek, double mass, double mu) {
        double underSqrt = 0.25 * (1.0 - ((epsilonK(k, mass) - mu) / Ek)) * (1.0 + ((epsilonK(k, mass) - mu) / Ek));
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
     * @param mass  the mass of the particle.
     * @param mu    the chemical potential.
     * @return the energy Ek.
     **/
    static double eK(double delta, double k, double mass, double mu) {
        return Math.sqrt(delta * delta + (epsilonK(k, mass) - mu) * (epsilonK(k, mass) - mu));
    }

    /**
     * Calculate the energy associated with wave vector k.
     *
     * @param E  the energy.
     * @param T  the temperature.
     * @param mu the chemical potential.
     * @return the value of Fermi-Dirac function.
     **/
    static double fermiDirac(double E, double mu, double T) {
        return 1.0 / (1.0 + Math.exp((E - mu) / (Boltzman_constant * T)));
    }

    /**
     * Calculate the energy gap from parameters.
     *
     * @param ks  the wave vectors.
     * @param eKs  the energies associated with wave vectors.
     * @param mu the chemical potential.
     * @param mass the particle mass.
     * @param T the temperature.
     * @return the energy gap delta.
     **/
    static double calcDelta(ArrayList<Double> ks, ArrayList<Double> eKs, double mass, double mu, double T) {
        // Arrays are needed for the summing
        double sum = 0;
        for (int i = 0; i < ks.size(); i++) {
            double k = ks.get(i);
            double eK = eKs.get(i);
            sum += g * (uKvK(k, eK, mass, mu) * 0.5 * (fermiDirac(-eK, mu, T) - fermiDirac(eK, mu, T)));
        }
        return sum;
    }

    static double convergeDelta(double mass, double mu, double T) {
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
                newN += vK2(ks.get(i), eKs.get(i), mass, mu) * fermiDirac(-eKs.get(i), mu, T)
                        + uK2(ks.get(i), eKs.get(i), mass, mu) * fermiDirac(eKs.get(i), mu, T);
            }
            // On the first iteration we don't know what the N is, so dont mix the mu yet
            if (iterations != 0) {
                double beta = 0.25;
                mu = mu + beta * (currentN - newN);
            }
            currentN = newN;

            // Get our new delta
            delta = calcDelta(ks, eKs, mass, mu, T);
            if (Math.abs(deltaPrev - delta) < deltaPrecision) 
            {
                // We have converged - done
            	System.out.println("Delta converged after " + iterations +"   to " + delta);
                return delta;
            } else {
                // Mixed step that should speed up convergence, but it doesn't
                // double alpha = 0.25;
                // deltaPrev = alpha * delta + (1 - alpha) * deltaPrev;
                deltaPrev = delta;
                iterations++;
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

    public static void main(String[] args) {
        ArrayList<Double> Ts = new ArrayList<Double>();
        ArrayList<Double> deltas = new ArrayList<Double>();
        double mass = 1.0, mu = 1e-6, T = 1e-6; // T_0 = almost zero
        establishKs();
        for (int i = 0; i < 1e7; i++) {
            T = T * 1.05;
            Ts.add(T);
            deltas.add(convergeDelta(mass, mu, T));
            if (deltas.get(i) <= almostZero) {
                System.out.println("For T = " + T + " delta ~ 0");
                System.out.println("Checked " + i + " different temperatures to match.");
                plotDelta(deltas, Ts);
                double ratio = deltas.get(0) / T;
                System.out.println("Ratio delta(T=0) / Tc supposed to be 1.76. In our case it is " + ratio);
                break;
            }
        }
    }
}

import staticFunctions.FixedPoint;
import java.util.ArrayList;

/**
 * The class Main
 *
 *
 * @Author Bartosz Biernacki, Piotr Machura
 **/
public class Main {

    // The calculation parameters
    final static double mu = 0, t = 0, g = 1;
    // 'Close enough' to a zero (name 'epsilon' is taken)
    final static double small = 1e-9;
    // Our line space
    final static double dx = 1e-4;
    final static int nX = 2 * (int) 1e4;

    static double epsilonK(double k) {
        // Who cares about Plancks constant (or mass)?
        return 0.5 * k * k;
    }

    static double uK(double k, double ek) {
        return Math.sqrt(0.5 * (1 + (epsilonK(k) - mu) / ek));
    }

    static double vK(double k, double ek) {
        return Math.sqrt(0.5 * (1 - (epsilonK(k) - mu) / ek));
    }

    static double calcDelta(ArrayList<Double> ks, ArrayList<Double> eks) {
        // Arrays are needed for the summing
        double sum = 0;
        for (int i = 0; i < ks.size(); i++) {
            double k = ks.get(i);
            double ek = eks.get(i);
            sum += uK(k, ek) * vK(k, ek) * 0.5 * (fermiDirac(ek) - fermiDirac(-ek));
        }
        return g * g * sum;
    }

    static double eK(double delta, double k) {
        // The formula as stated (I hope)
        return Math.sqrt(delta * delta + (epsilonK(k) - mu) * (epsilonK(k) - mu));
    }

    static double fermiDirac(double e) {
        // Who cares about Boltzmann constant?
        double denominator = Math.exp((e - mu) / t) + 1;
        return 1 / denominator;
    }

    public static void main(String[] args) {
        System.out.println(FixedPoint.findRoot(0.0001));
        // Take a guess
        double delta = 1;
        double deltaPrev = delta + 1e20; // Not to trigger the stop condition immediately
        while (true) {
            // We need those for the delta
            ArrayList<Double> ks = new ArrayList<Double>();
            ArrayList<Double> eks = new ArrayList<Double>();
            // calculate the ek's and k's
            for (int i = 0; i < nX; i++) {
                // Shamelessly ripped from the script
                double kX = 2 * Math.PI / (nX * dx);
                if (i <= nX / 2) {
                    kX *= i;
                } else {
                    kX *= (i - nX);
                }
                // kY and kZ are the same
                double k = Math.sqrt(3 * kX * kX);
                ks.add(k);
                eks.add(eK(delta, k));
            }
            // Get our new delta
            delta = calcDelta(ks, eks);
            System.out.println(delta);
            if (Math.abs(deltaPrev - delta) < small) {
                // We have converged - done
                break;
            } else {
                // Keep going
                deltaPrev = delta;
            }
        }
    }
}

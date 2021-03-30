import staticFunctions.FixedPoint;
import java.util.ArrayList;

/**
 * The class Main
 *
 * @Author Bartosz Biernacki, Piotr Machura
 **/
public class Main {
    // The calculation parameters
    static double mu = 0, u = 1, v = 1, t = 0, g = 1;
    // 'Close enough' to a zero (name 'epsilon' is taken)
    static double small = 1e-9;

    public static double epsilonK(double k) {
        // Who cares about Plancks constant (or mass)?
        return k * k / 2;
    }

    public static double deltaFromE(double e, double k) {
        // TODO: Implement this
        return 0;
    }

    public static double eFromDelta(double delta, double k) {
        return Math.sqrt(delta * delta + (epsilonK(k) - mu) * (epsilonK(k) - mu));
    }

    public static double fermiDirac(double e) {
        // Who cares about Boltzmann constant?
        double denominator = Math.exp((e - mu) / t) + 1;
        return 1 / denominator;
    }

    public static void main(String[] args) {
        System.out.println(FixedPoint.findRoot(0.0001));
        // Looking for this
        ArrayList<Double> deltas = new ArrayList<Double>();
        ArrayList<Double> energies = new ArrayList<Double>();
        // We may need those as well idk
        ArrayList<Double> ks = new ArrayList<Double>();
        double guessE = 1;
        for (double k = 0; k < 100; k += 0.1) {
            double delta;
            double deltaPrev = 1e20; // Not to trigger the stop condition immediately
            double e = guessE;
            while (true) {
                delta = deltaFromE(e, k);
                e = eFromDelta(delta, k);
                if (Math.abs(deltaPrev - delta) < small) {
                    break;
                }
                deltaPrev = delta;
            }
            // We have converged - add the solutions to solution lists
            energies.add(e);
            // This is wrong (?) since there is a sum over k in the delta
            deltas.add(delta);
            // We probably want the ks for further use as well
            ks.add(k);
        }
        // We now have deltas and energies stored in our ArrayLists for further
        // calculations, graphing and whatnot
    }
}

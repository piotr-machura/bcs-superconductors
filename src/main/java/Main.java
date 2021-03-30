import staticFunctions.FixedPoint;


/**
 * The class Main
 *
 * @Author Bartosz Biernacki, Piotr Machura
 **/
public class Main {
    // The calculation parameters
    static double mu = 0, u = 1, v = 1, t = 0, g = 1;
    static double epsilon = 1e-9;

    public static double deltaFromE(double e) {
        // TODO: Implement this
        return 0;
    }

    public static double eFromDelta(double delta) {
        // TODO: Implement this
        return 0;
    }

    public static double fermiDirac(double e) {
        double denominator = Math.exp((e - mu) / t) + 1;
        return 1 / denominator;
    }

    public static void main(String[] args) {
        System.out.println(FixedPoint.findRoot(0.0001));
        // Looking for this
        double e = 1;
        double delta;
        // This is large not to trigger the stop condition on first loop
        double deltaPrev = 1e10;
        while (true) {
            delta = deltaFromE(e);
            if (Math.abs(deltaPrev - delta) < epsilon) {
                break;
            }
            e = eFromDelta(delta);
            deltaPrev = delta;
        }
    }
}

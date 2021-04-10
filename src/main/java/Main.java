import java.util.ArrayList;
/**
 * The class Main
 *
 *
 * @Author Bartosz Biernacki, Piotr Machura
 **/
public class Main {
    // The calculation parameters
    final static double mu = 0.0001, T = 0.00001, g = 1.0, mass = 1.0, h_bar = 1.0, Boltzman_constant = 1.0;
    // 'Close enough' to a zero (name 'epsilon' is taken)
    final static double small = 1e-9;
    // Our line space
    final static double dx = 1e-4;
    final static int nX = 2 * (int) 1e4;

    static double epsilonK(double k)	 		{ return (h_bar*h_bar*k*k)/(2*mass);}
    
    static double uK(double k, double Ek) 		{  return Math.sqrt(0.5 * (1.0 +( (epsilonK(k) - mu)/Ek) ) );}
       
    static double vK(double k, double Ek)		{ return Math.sqrt(0.5 * (1.0 -( (epsilonK(k) - mu)/Ek) ));}
        
    static double eK(double delta, double k)	{ return Math.sqrt(delta * delta + (epsilonK(k) - mu) * (epsilonK(k) - mu));}
       
    static double fermiDirac(double E) 			{ return  1.0 / (1.0 + Math.exp( (E-mu)/(Boltzman_constant*T) ));}
    
    
    static double calcDelta(ArrayList<Double> ks, ArrayList<Double> Eks) {
        // Arrays are needed for the summing
        double sum = 0;
        for (int i = 0; i < ks.size(); i++) {
            double k = ks.get(i);
            double Ek = Eks.get(i);
            sum += g*( uK(k, Ek) * vK(k, Ek) * 0.5 * (fermiDirac(-Ek) - fermiDirac(Ek)) );
            //System.out.println("fermiDirac(ek ) = " + fermiDirac(Ek));
           //System.out.println("fermiDirac(-ek) = " + fermiDirac(-Ek));
        }
        return sum;
    }




    
    public static void main(String[] args) {
        // Take a guess
    	double N = 100;
        double delta = 1;
        double deltaPrev = delta + 1e5; // Not to trigger the stop condition immediately
        int iterations =0;
        
     // calculate the k's (needed only once)
        ArrayList<Double> ks = new ArrayList<Double>();
        for (int i = 0; i < nX; i++) 
        {
            double kX = 2 * Math.PI / (nX * dx);
            if (i <= nX / 2) {
                kX *= i;
            } else {
                kX *= (i - nX);
            }
            // kY and kZ are the same
            double k = Math.sqrt(3 * kX * kX);
            ks.add(k);
        }
        
        //Delta is calculated in this loop
        while (true)
        {
            // We need those for the delta
            ArrayList<Double> Eks = new ArrayList<Double>();
            double N_sum = 0;
            // calculate the Ek's and Number of particles 
            for (int i = 0; i < ks.size(); i++) 
            {
                Eks.add(eK(delta, ks.get(i)));
                N_sum += vK(ks.get(i), Eks.get(i)) * vK(ks.get(i), Eks.get(i)) * fermiDirac(-Eks.get(i))  + uK(ks.get(i), Eks.get(i)) * uK(ks.get(i), Eks.get(i)) * fermiDirac(Eks.get(i))  ;
            }
            N = N_sum;
            // Get our new delta
            delta = calcDelta(ks, Eks);
            iterations ++;
      
            System.out.println("Delta = " + delta + "    #particels  = " + N);
            if (Math.abs(deltaPrev - delta) < small) 
            {
                // We have converged - done
            	System.out.println("We have converged after " + iterations + " iterations" );
                break;
            } else 
            {
            	//mixed step that should perform faster convergence, but it desn't
            	/*
            	double alpha = 0.5;
            	deltaPrev = alpha*delta + (1-alpha)*deltaPrev;
            	delta = deltaPrev;
            	*/
            	
            	//non mixed step (faster convergence for now) 
                deltaPrev = delta;
            }
        }
    }
}
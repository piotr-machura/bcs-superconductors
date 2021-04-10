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
    // 'Close enough' to a zero (name 'epsilon' is taken)
    final static double deltaPrecision = 1e-9;
    // Our line space
    final static double dx = 1e-4;
    final static int nX = 2 * (int) 1e4;

    static double epsilonK(double k, double mass)	 		{ return (h_bar*h_bar*k*k)/(2*mass);}
    
    static double uK(double k, double Ek, double mass, double mu) 		{  return Math.sqrt(0.5 * (1.0 +( (epsilonK(k, mass) - mu)/Ek) ) );}
       
    static double vK(double k, double Ek, double mass, double mu)		{ return Math.sqrt(0.5 * (1.0 -( (epsilonK(k, mass) - mu)/Ek) ));}
        
    static double Ek(double delta, double k, double mass, double mu)	{ return Math.sqrt(delta * delta + (epsilonK(k, mass) - mu) * (epsilonK(k, mass) - mu));}
       
    static double fermiDirac(double E, double mu, double T) 			{ return  1.0 / (1.0 + Math.exp( (E-mu)/(Boltzman_constant*T) ));}
    
    
    static double calcDeltaOnce(ArrayList<Double> ks, ArrayList<Double> Eks, double mass, double mu, double T) 
    {
        // Arrays are needed for the summing
        double sum = 0;
        for (int i = 0; i < ks.size(); i++) {
            double k = ks.get(i);
            double Ek = Eks.get(i);
            sum += g*( uK(k, Ek, mass, mu) * vK(k, Ek, mass, mu) * 0.5 * (fermiDirac(-Ek, mu, T) - fermiDirac(Ek, mu, T)) );
        }
        return sum;
    }

    
    static double calcDeltaUntillConvergence(double mass, double mu, double T)
    {   
     // calculate the k's (needed only one per one delta )
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
        
        // Take a guess
    	double N = 1.0;
        double delta = 1.0;
        double deltaPrev = delta + 1e5; // Not to trigger the stop condition immediately
        int iterations = 0;
        
        //Delta is calculated in this loop
        while (true)
        {
            // We need those for the delta
            ArrayList<Double> Eks = new ArrayList<Double>();
            double N_sum = 0;
            // calculate the Ek's and Number of particles 
            for (int i = 0; i < ks.size(); i++) 
            {
                Eks.add(Ek(delta, ks.get(i), mass, mu));
                N_sum += vK(ks.get(i), Eks.get(i), mass, mu) * vK(ks.get(i), Eks.get(i), mass, mu) * fermiDirac(-Eks.get(i), mu, T)  + uK(ks.get(i), Eks.get(i), mass, mu) * uK(ks.get(i), Eks.get(i), mass, mu) * fermiDirac(Eks.get(i), mu, T)  ;
            }
           
           //mu = mu + 0.1*(N - N_sum);	//beta = 0.1		//mixed step, but not working, after 2 iterations Ek.get(1) = NaN
            N = N_sum;
            // Get our new delta
            delta = calcDeltaOnce(ks, Eks, mass, mu, T);
            iterations ++;
      
            //System.out.println("Delta = " + delta + "    #particels  = " + N);
            if (Math.abs(deltaPrev - delta) < deltaPrecision) 
            {
                // We have converged - done
            	//System.out.println("We have converged after " + iterations + " iterations" );
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
    	
    	return delta;
    }
    
    static void plotDeltaVsTemperature(ArrayList<Double> deltas, ArrayList<Double> Ts)
    {
    	// Construct the graph series
        XYSeries xySeries = new XYSeries("Delta vs T");
        for (int i = 0; i < deltas.size(); i++) 
        {
            xySeries.add(Ts.get(i), deltas.get(i));
        }

        // Display the graphs
        ChartFrame frame1 = new ChartFrame("XYLine Chart",
                ChartFactory.createXYLineChart("Delta vs T", "Temperature", "Delta value", new XYSeriesCollection(xySeries)));
        frame1.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);
        frame1.setSize(500, 400);
    }


    static void verifyIfProperRatioWasAchived(double mass, double mu, double Tc) 
    {
		double ratio = calcDeltaUntillConvergence(mass, mu, 1e-10) / Tc;	//1e-10 is reasonably zero
		System.out.println("Ratio delta(T=0) / Tc supposed to be 1.76. In our case it is " + ratio);

	}

    
    public static void main(String[] args) 
    {
    	ArrayList<Double> Ts = new ArrayList<Double>();
    	ArrayList<Double> deltas = new ArrayList<Double>();
    	double mass = 1.0, mu = 1e-6, T = 1e-6;		//T_0 = almost zero
    	for (int i=0; i<1e7; i++)
    	{
    		T = T*1.05;
    		Ts.add(T);
    		deltas.add(calcDeltaUntillConvergence(mass, mu, T));
    		System.out.println(i + "Sukces for T = " + Ts.get(i));
    		
    		if(deltas.get(i)<=0.000001)									//Delta is almost zero --> break  // Tc value is highly dependent on numerucal zero
    		{															//for 0=1e-8 -->Tc = 0.1409,    for 0=1e-10 -->Tc = 0.6395,   for 0=1e-10 -->Tc = 6.0335,  for 0=0 -->Tc = 239140.76
    			System.out.println("For T = " + T + " delta <= 0");		//Because delta(T=0)=0.26 I've decided that 0.0001 is zero enough
    			System.out.println("Checked " + i + " different temperatures to match.");
    			plotDeltaVsTemperature(deltas, Ts);
    			verifyIfProperRatioWasAchived(mass, mu, T);
    			break;
    		}
    		
    	}
    }
}
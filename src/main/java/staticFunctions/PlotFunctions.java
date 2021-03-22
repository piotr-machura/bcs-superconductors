package staticFunctions;

import java.util.Arrays;

import org.jfree.chart.ChartFrame;

public class PlotFunctions 
{
	public static void setMeaningfulXAxisRange(ChartFrame frame1, double aa, double bb)
	{
		
		Double[] dataYtoSort = new Double[frame1.getChartPanel().getChart().getXYPlot().getDataset().getItemCount(0)];
		Double[] dataY = new Double[frame1.getChartPanel().getChart().getXYPlot().getDataset().getItemCount(0)];
		for (int i=0; i<dataYtoSort.length; i++)
		{
			dataY[i]=   Math.abs((Double) frame1.getChartPanel().getChart().getXYPlot().getDataset().getY(0, i));
			dataYtoSort[i]=   Math.abs((Double) frame1.getChartPanel().getChart().getXYPlot().getDataset().getY(0, i));
			
		}
		
		Arrays.sort(dataYtoSort);
		Double relativetMin = dataYtoSort[dataY.length-1]/(dataY.length*1);
		
		
		double b = dataY.length;
		for (int i=dataY.length-1; i>=0; i--)
		{
			if(dataY[i] >= relativetMin)
			{
				b=i;
				break;
			}
		}
		
		
		double B = (double) b/(bb-aa)  ;
		B = B + 0.1*B;
		B = B/2;
		
		double A = (-1)*(B/10) ;
		
		
		
		/*
		System.out.println("A = " + A);
		System.out.println("B = " + B);
		*/
		
		frame1.getChartPanel().getChart().getXYPlot().getDomainAxis().setRange(A, B);
	}
}

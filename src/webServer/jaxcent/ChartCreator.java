package webServer.jaxcent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jfree.data.xy.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

public class ChartCreator {

	public static void createXYChart(double [][]data,String filename)
	{
		XYSeries series = new XYSeries("Average Size");
		for (int i = 0; i < data.length;i++)
		{
			series.add(data[i][0],data[i][1]);
		}

		XYDataset xyDataset = new XYSeriesCollection(series);
		JFreeChart chart = ChartFactory.createXYAreaChart
		("Sample XY Chart",  // Title
				"Height",           // X-Axis label
				"Weight",           // Y-Axis label
				xyDataset,          // Dataset
				PlotOrientation.VERTICAL,
				true,false,false                // Show legend
		);
		try {
			FileOutputStream outputStream = new FileOutputStream(filename); 
			ChartUtilities.writeChartAsJPEG(outputStream, chart, 600, 400);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String []args)
	{
		System.out.println("testing create chart");
		double data[][] = {{0,1},{1,2},{2,3.5},{3,20}};
		createXYChart(data, "test.jpg");
		System.out.println("chart image created");
	}
}

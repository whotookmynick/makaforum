package webServer.jaxcent;

import implementation.ControlerFactory;

import java.awt.image.RenderedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import jaxcent.HtmlImage;
import jaxcent.HtmlInputButton;
import jaxcent.HtmlInputText;
import jaxcent.Jaxception;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.demo.BarChartDemo1;
import org.jfree.chart.plot.PlotOrientation;

import domainLayer.TheController;

public class ChartCreator extends jaxcent.JaxcentPage{

	private TheController _controller;

	public ChartCreator()
	{
		_controller = ControlerFactory.getControler();
		HtmlInputButton firstChartButton = new HtmlInputButton(this, "firstGraphButton")
		{
			protected void onClick()
			{
				createFirstChart();
			}
		};
		double[][] dataForSecondChart = _controller.getNumOfMessagesPerHour();
		RenderedImage ri2 = createXYChart(dataForSecondChart, "images/chart2.jpg");
		double[][] dataForThirdChart = _controller.getConnectedUsersPerHour();
		RenderedImage ri3 = createXYChart(dataForThirdChart, "images/chart3.jpg");
		HtmlImage image2 = new HtmlImage(this, "secondChart");
		HtmlImage image3 = new HtmlImage(this, "thirdChart");
		try {
			image2.setSrc(ri2, "png");
			image3.setSrc(ri3,"png");
		} catch (Jaxception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createFirstChart() {
		try {
			HtmlInputText _userNameText = new HtmlInputText(this, "firstGraphText");
			String userNameForChart = _userNameText.getValue();
			long userid = _controller.get_uidFromUserName(userNameForChart);
			double[][] data = _controller.getMessagesForThirtyDays(userid);
			RenderedImage chartImage = createXYChart(data, "images/chart1.jpg");
			HtmlImage firstImage = new HtmlImage(this, "chart1");
			firstImage.setSrc(chartImage, "png");
			firstImage.setVisible(true);
		} catch (Jaxception e) {
			e.printStackTrace();
		} 

	}

	public static RenderedImage createXYChart(double [][]data,String filename)
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
		RenderedImage ri = chart.createBufferedImage(600,400); 
		return ri;
		//			FileOutputStream outputStream = new FileOutputStream(filename); 
		//			ChartUtilities.writeChartAsJPEG(outputStream, chart, 600, 400);
	}

	public static void main(String []args)
	{
		System.out.println("testing create chart");
		double data[][] = {{0,1},{1,2},{2,3.5},{3,20}};
		createXYChart(data, "test.jpg");
		System.out.println("chart image created");
	}
}

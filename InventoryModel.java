import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;


public class InventoryModel {

	public double costOfUnit;
	public double sellingPrice;
	public double amtGainProb;
	public double amtLossProb; 
	private double sum = 0;
	public double index = 0;
	private double difference;
	public File input;
	String line;


	ArrayList<Double> cumulProbSum = new ArrayList<Double>();
	ArrayList<Double> cumulProbVal = new ArrayList<Double>();
	ArrayList<Double> cumulProbActualVal = new ArrayList<Double>();
	ArrayList<Double> demand = new ArrayList<Double>();
	ArrayList<Double> demandChartVal = new ArrayList<Double>();
	//ArrayList<String> demandNameChart = new ArrayList<String>();
	ArrayList<Double> probChartVal = new ArrayList<Double>();



	public InventoryModel(){}


	public double getCostOfUnit() {
		return costOfUnit;
	}


	public void setCostOfUnit(double costOfUnit) {
		this.costOfUnit = costOfUnit;
	}


	public double getSellingPrice() {
		return sellingPrice;
	}



	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}


	public double getAmtGainProb() {
		return amtGainProb;
	}


	public void setAmtGainProb(double amtGainProb) {
		this.amtGainProb = amtGainProb;
	}


	public double getAmtLossProb() {
		return amtLossProb;
	}


	public void setAmtLossProb(double amtLossProb) {
		this.amtLossProb = amtLossProb;
	}


	public double getBreakEvenValue(){

		double gain = sellingPrice - costOfUnit;
		double p = gain/(gain+costOfUnit);

		return p;
	}

	public double getPotGainValue(){

		double gg = sellingPrice - costOfUnit;
		return gg;

	}
	
	public String getPotGainString(){
		double a = getPotGainValue();
		String b = Double.toString(a);
		return b;
	}
	

	public double getPotLossValue(){
		double gl = costOfUnit;
		return gl;
	}
	
	public String getPotLossString(){
		double a = getPotLossValue();
		String b = Double.toString(a);
		return b;
	}

	




	/*
	 * Calculate Cumulative Probability values in csv file. 
	 * 
	 * The cumulative probability is the general method to
	 * determine the quantity to produce up to where
	 * the demand equals the profit margin.
	 * 
	 */
	
	

	public double getCumulativeProb(){

		double num = getBreakEvenValue();


		try {


			FileReader fr = new FileReader(input);
			BufferedReader br = new BufferedReader(fr);


			while((line = br.readLine()) != null)
			{

				String[] fields = line.split(",");
				cumulProbSum.add(Double.parseDouble(fields[1]));
				demand.add(Double.parseDouble(fields[0]));

			}


			for (int c = 0; c < cumulProbSum.size(); c++)
			{
				sum = sum + (cumulProbSum.get(c));
				cumulProbVal.add(sum);
			}

			//Find nearest value to the breakeven result:
			difference = Math.abs(num - cumulProbVal.get(0));

			for (int i=0; i< cumulProbVal.size(); i++ )
			{
				if (difference > Math.abs (num - cumulProbVal.get(i)))
				{
					difference = Math.abs(num - cumulProbVal.get(i));
					index = i;

				}
			}



			double g = cumulProbVal.get((int)index);  //cumulative probability/ probability of failure.
			double e = cumulProbVal.indexOf(g); 

		}


		catch (FileNotFoundException fN)
		{
			fN.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}

		double g = cumulProbVal.get((int)index);
		double e = cumulProbVal.indexOf(g);
		double m = demand.get((int) e);

		return  m; 
	}






	/*
	 * Calculate Probability of Failure whether considering 
	 * to produce n+1 units, where n = # of demand (in csv file). 
	 * 
	 * i.e. If the optimal quantity to produce is 1,800, then
	 * what is the probability of failure if to produce
	 * unit 1,801? 
	 * 
	 */
	public double getProbF(){

		double num = getBreakEvenValue();


		try {

			FileReader fr = new FileReader(input);
			BufferedReader br = new BufferedReader(fr);



			while((line = br.readLine()) != null)
			{

				String[] fields = line.split(",");
				cumulProbSum.add(Double.parseDouble(fields[1]));
				demand.add(Double.parseDouble(fields[0]));

			}


			for (int c = 0; c < cumulProbSum.size(); c++)
			{
				sum = sum + (cumulProbSum.get(c));
				cumulProbVal.add(sum);
			}

			//Find nearest value to breakeven result.
			difference = Math.abs(num - cumulProbVal.get(0));

			for (int i=0; i< cumulProbVal.size(); i++ )
			{
				if (difference > Math.abs (num - cumulProbVal.get(i)))
				{
					difference = Math.abs(num - cumulProbVal.get(i));
					index = i;

				}
			}


			System.out.println("Cumulative Probability: "+ cumulProbVal.get((int)index));

			double g = cumulProbVal.get((int)index);  //cumulative probability/ probability of failure.

			System.out.println( "Cumulative Probability index: " + cumulProbVal.indexOf(g));

			double e = cumulProbVal.indexOf(g); 

			System.out.println("Demand Level associated with cumulative probability: " + demand.get((int) e));  //demand level associated with cumulative probability/ probability of failure in csv file. 

		}

		catch (FileNotFoundException fN)
		{
			fN.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}

		double g = cumulProbVal.get((int)index);
		double e = cumulProbVal.indexOf(g);
		double m = demand.get((int) e);
		return  g; 
	}


	/*
	 * 
	 * Extract Demand Levels in .csv file to create a graph.
	 * 
	 */
	public ArrayList<Double> getValsToChart(){


		try {



			FileReader fr = new FileReader(input);
			BufferedReader br = new BufferedReader(fr);



			while((line = br.readLine()) != null)
			{

				String[] fields = line.split(",");

				demandChartVal.add(Double.parseDouble(fields[0]));
				probChartVal.add(Double.parseDouble(fields[1]));

			}

		}



		catch (FileNotFoundException fN)
		{
			fN.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}

		return  demandChartVal; 
	}







	/*
	 * Calculate Expected Payoff from .csv file.
	 * 
	 * This is to determine whether producing n+1 units
	 * is a favorable decision.
	 */
	public double getExpectedPayoff(){

		double num = getBreakEvenValue();


		try {


			FileReader fr = new FileReader(input);
			BufferedReader br = new BufferedReader(fr);


			while((line = br.readLine()) != null)
			{

				String[] fields = line.split(",");
				cumulProbSum.add(Double.parseDouble(fields[1]));
				demand.add(Double.parseDouble(fields[0]));

			}


			for (int c = 0; c < cumulProbSum.size(); c++)
			{
				sum = sum + (cumulProbSum.get(c));
				cumulProbVal.add(sum);
				cumulProbActualVal.add(sum);
			}

			//Find nearest value to number specified.
			difference = Math.abs(num - cumulProbVal.get(0));

			for (int i=0; i< cumulProbVal.size(); i++ )
			{
				if (difference > Math.abs (num - cumulProbVal.get(i)))
				{
					difference = Math.abs(num - cumulProbVal.get(i));
					index = i;

				}
			}

		}





		catch (FileNotFoundException fN)
		{
			fN.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}


		double gain = getPotGainValue();
		double loss = getPotLossValue();
		double r = cumulProbVal.get((int)index);
		double result1 = r * gain;
		double result2 = num * loss;
		double expectedPayoff = (result1 - result2)/10;
		System.out.println("Expected Payoff: " + expectedPayoff);
		return  expectedPayoff; 


	}
	
}
	










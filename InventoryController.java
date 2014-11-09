
/*Rachelle B. Villalon
 * rvill@mit.edu
 * Games of Chance, Games of Strategy 
 * Harvard Business School
 * Professor Elon Kohlberg
 * Fall 2011
 * 
 * 
 * 
 * INVENTORY DECISIONS FOR UNCERTAIN DEMAND
 * 
 * 
 * The main contribution of this program is to serve as an inventory management tool 
 * for the following: 
 * 
 * 
 * THE SCENARIO: You are in the mail-order business (i.e.  electronics, clothing, home improvement, etc.)
 * and you are about to submit your order for the latest/stylish commodity. If the commodity/unit 
 * costs $14, for example, and you are planning to sell it for $58. At this price and with a
 * demand forecast for the commodity enclosed (.csv), how many units (n) should you produce to
 * maximize expected profit? 
 * 
 * 
 * TAKING A RISK: 
 * Once the number of units is given, what if you want to produce n+1 units? This is like being faced
 * with a gamble. There is a potential loss, potential gain, and a probability of failure involved
 * when deciding to produce n+1 units. The program makes an assessment of that risk and determines
 * a recommendation if producing n+1 units proves favorable. 
 * 
 * 
 * SPREADSHEET DATA:
 * The enclosed spreadsheet (.csv) contains the number of demand in column 1, and the probability 
 * of that demand in column 2.
 * 
 * 
 * INTERFACE HOW-TO (order of operations):
 * 1. Type in unit cost
 * 2. Type in the selling price.
 * 3. Import the csv file.
 * 4. Hit "Compute" 
 * 5. Hit "Graph", just once.
 * 6. Entering new values for cost and selling price, re-hit "Compute" 
 * 
 * 
 * CAVEATS/QUIRKS/BUGS!!!!:
 * 
 * 1. MUST HIT "Compute" TWICE in order for results to update.
 * 2. MUST HIT "Graph" ONCE in order to get demand level graph - clicking graph more than once,
 * re-scales the graph...
 * 
 * 
 * 
 */




import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.SwingUtilities;


public class InventoryController extends JFrame {

	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 800;

	private static final int AREA_ROWS = 10;
	private static final int AREA_COLUMNS = 30;

	private JLabel unitCostLabel, unitSellPriceLabel,resultLabel, pGainLabel, pLoseLabel, breakEvenValueLabel, potentialGainLabel, potentialLossLabel, pOfFailureLabel,recLabel, optimalQuantLabel, gambleLabel;
	private JTextField unitCostField, unitSellPriceField, pGainField, pLoseField;
	private JTextArea historyResultArea;
	private JButton calculateButton, quitButton, openButton, importButton;
	JButton graphButton;
	private JPanel inputPanel, gamblePanel, numOfUnitsProducePanel, recommendationPanel; 
	private JPanel buttonPanel;
	private JPanel controlPanel;
	private JPanel breakevenPanel;
	private InventoryModel model;
	private InventoryView view;
	//private JTextArea log;
	//private JFileChooser fc;

	//JFileChooser chooser; 

	
		String title = "Probability Distribution of Demand";
	public InventoryController() {
		super("GSGC: Inventory Decisions in Face of Uncertain Demand");
		Container cp = getContentPane(); 	
		model = new InventoryModel();
		view = new InventoryView(model);
		cp.add(view, BorderLayout.EAST);
		
	


		setSize(FRAME_WIDTH, FRAME_HEIGHT);

		
	 
		historyResultArea = new JTextArea(AREA_ROWS, AREA_COLUMNS);
		historyResultArea.setEditable(false);


		//Input Panel
		inputPanel = new JPanel();		
		inputPanel.setBorder(new TitledBorder(new EtchedBorder(), "Inventory Decisions"));
		inputPanel.setLayout(new GridLayout(3,1));


		unitCostLabel = new JLabel("Cost of unit = ");
		inputPanel.add(unitCostLabel);
		unitCostField = new JTextField(30);
		inputPanel.add(unitCostField);

		unitSellPriceLabel = new JLabel("Your Selling Price = ");
		inputPanel.add(unitSellPriceLabel);
		unitSellPriceField = new JTextField(30);
		inputPanel.add(unitSellPriceField);

		breakEvenValueLabel = new JLabel("Breakeven probability = ");
		inputPanel.add(breakEvenValueLabel);


		//Number of Units to Produce, if n+1
		numOfUnitsProducePanel = new JPanel();
		numOfUnitsProducePanel.setBorder(new TitledBorder(new EtchedBorder(), "Faced with a Gamble. Number of Units to Produce if n+1: " ));

		numOfUnitsProducePanel.setLayout(new GridLayout(4,1)); 


		pGainLabel = new JLabel("Potential Gain, G = ");
		numOfUnitsProducePanel.add(pGainLabel);
		pLoseLabel = new JLabel("Potential Loss, L = ");
		numOfUnitsProducePanel.add(pLoseLabel);


		pOfFailureLabel = new JLabel ("Probability of Failure = ");
		numOfUnitsProducePanel.add(pOfFailureLabel);


		//Recommendation Panel
		recommendationPanel = new JPanel();
		recommendationPanel.setBorder(new TitledBorder(new EtchedBorder(), "Recommendation"));
		recommendationPanel.setLayout(new GridLayout(1,1));  

		optimalQuantLabel = new JLabel("Optimal Quantity to Produce (n) = ");
		recommendationPanel.add(optimalQuantLabel);

		gambleLabel = new JLabel("Is the gamble favorable? ");
		//recommendationPanel.add(gambleLabel);
		numOfUnitsProducePanel.add(gambleLabel);


		//Button 
		buttonPanel = new JPanel();		// Button holder

		//Import 
		importButton = new JButton("Import Spreadsheet (.csv)");
		buttonPanel.add(importButton);
	
		
	
		importButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
		
				view.getFile();

		
			}

		});




		calculateButton = new JButton("Compute");
		buttonPanel.add(calculateButton);
		calculateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Retrieve and verify user-input unit cost
				String input = unitCostField.getText();
				double unitCost = Double.parseDouble(input);


				breakEvenValueLabel.setText("Breakeven probability = " + model.getBreakEvenValue());
				pGainLabel.setText("Potential Gain, G = " + model.getPotGainValue());
				pLoseLabel.setText("Potential Loss, L = " + model.getPotLossValue());

				optimalQuantLabel.setText("Optimal Quantity to Produce (n) = " + model.getCumulativeProb());
				pOfFailureLabel.setText("Probability of Failure = " + model.getProbF());
				gambleLabel.setText("Is the gamble favorable? "+ model.getExpectedPayoff());

				if (model.getExpectedPayoff() > 0) {
					gambleLabel.setText("Is the gamble favorable? "+ "Yes, the expected payoff is positive: " + model.getExpectedPayoff());
					historyResultArea.append("Is the gamble favorable? "+"Yes, the expected payoff is positive: " + model.getExpectedPayoff() + 
							"(Solution: " + model.cumulProbVal.get((int) model.index) + "*" + model.getPotGainValue()+ "-" + model.getBreakEvenValue()+
							"*" + model.getPotLossValue() + ")" +"\n");
				} else  {
					gambleLabel.setText("Is the gamble favorable? "+ "No, the expected payoff is negative: " + model.getExpectedPayoff());
					historyResultArea.append("Is the gamble favorable? " + "No, the expected payoff is negative: " + model.getExpectedPayoff() + 
							"(Solution: " + model.cumulProbVal.get((int) model.index) + "*" + model.getPotGainValue()+ "-" + model.getBreakEvenValue()+
							"*" + model.getPotLossValue() + ")" +"\n"); 
				}


				//History window
				historyResultArea.append("\n"+ "Cost of Unit = " + model.getCostOfUnit() + "; " + "Selling Price = " + model.getSellingPrice() + "\n");
				
				historyResultArea.append("Breakeven value  = " + model.getBreakEvenValue()   +
						" (Solution: " + model.getPotGainValue() + "/" + model.getPotGainValue() + " + " +  model.getPotLossValue() + 
						" = " + model.getBreakEvenValue() + ") " +  "\n"  );
				
				historyResultArea.append("Optimal Quantity to Produce(n) = "+ model.getCumulativeProb() + "\n" );
				
				
				historyResultArea.append("Probability of Failure if to produce n+1= " + model.getProbF()+  
						" (Solution: " + model.getPotGainValue() + "/" + model.getPotGainValue() + " + " +  model.getPotLossValue() + 
						" = " + model.getBreakEvenValue() + "; Probability of failure just above breakeven value in cumulative probability table = " + 
						model.cumulProbVal.get((int) model.index)+ " )   " + "\n");
				
				historyResultArea.append("Potential Gain, G = " + model.getPotGainValue() +" (Solution: " + model.sellingPrice + "-" + model.costOfUnit + ")" +"\n");
				historyResultArea.append("Potential Loss, L = " + model.getPotLossValue()+ "\n");
			



				if (unitCost < 0.0) {
					JOptionPane.showMessageDialog(null,
							"Price must be positive",
							"Warning Message", JOptionPane.WARNING_MESSAGE);
					return;
				}

				//retrieve and verify user-input selling price of the unit 
				input = unitSellPriceField.getText();
				double sell = Double.parseDouble(input);
				if (sell < 0.0){
					JOptionPane.showMessageDialog(null,
							"Price must be positive",
							"Warning Message", JOptionPane.WARNING_MESSAGE);
					return;

				}
				
				if (sell < unitCost){
					JOptionPane.showMessageDialog(null,
							"Selling price must be larger than the unit cost. Please type in a new value. ",
							"Warning Message", JOptionPane.WARNING_MESSAGE);
					return;
				}

				model.setCostOfUnit(unitCost);
				model.setSellingPrice(sell);
				

			}
		});



		graphButton = new JButton("Graph");

		buttonPanel.add(graphButton);
		graphButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				view.repaint();

			} 


		});

		quitButton = new JButton("Quit");
		buttonPanel.add(quitButton);
		quitButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				System.exit( 0 ) ;
			}
		});



		//Master Panel: 
		controlPanel = new JPanel(); //controlPanel
		controlPanel.setLayout(new GridLayout (5,1));
		controlPanel.add(inputPanel);
		controlPanel.add(buttonPanel);
		controlPanel.add(recommendationPanel);
		controlPanel.add(numOfUnitsProducePanel);


		JScrollPane scrollPane = new JScrollPane(historyResultArea);
		controlPanel.add(scrollPane);



		add(controlPanel, BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.SOUTH);

		pack();
		setResizable(true);
		
	}




	public static void main(String[] args) {
		InventoryController gui = new InventoryController();
		gui.setVisible(true);
		gui.setLocation(0, 0);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		


	}

}











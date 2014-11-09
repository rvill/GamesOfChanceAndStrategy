import java.awt.*;

import javax.swing.*;

import java.awt.geom.*;
import java.io.File;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;


public class InventoryView extends JPanel {


	private InventoryModel model;
	//private InventoryController control;
	final int PAD = 0;




	public InventoryView(InventoryModel model){
		this.model = model;
		int totalWidth = 800;
		int totalHeight =600;
		this.setPreferredSize(new Dimension(totalWidth, totalHeight));

	}
	JFileChooser chooser = new JFileChooser();

	public File getFile(){

		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			model.input = chooser.getSelectedFile();

		} 
		return model.input;
	}

	//Draw the demand level bar chart.
	public void paintComponent(Graphics g){

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Arial",1,14));
		g2.setStroke(new BasicStroke(2));



		if(chooser.getSelectedFile() != null){

			ArrayList<Double> v = model.getValsToChart();
			String title = "Probability Distribution of Demand";


			if (v == null || v.size() == 0)
				return;
			double minValue = 0;
			double maxValue = 0;
			for (int i = 0; i < v.size(); i++) {
				if (minValue > v.get(i))
					minValue = v.get(i);
				if (maxValue < v.get(i))
					maxValue = v.get(i);
			}

			Dimension d = getSize();
			int graphWidth = d.width;
			int graphHeight = d.height;

			int barWidth = graphWidth / v.size();

			Font titleFont = new Font("SansSerif", Font.BOLD, 20);
			FontMetrics titleFontMet = g.getFontMetrics(titleFont);
			Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
			FontMetrics labelFontMet = g.getFontMetrics(labelFont);

			int titleWidth = titleFontMet.stringWidth(title);
			int y = titleFontMet.getAscent();
			int x = (graphWidth - titleWidth) / 2;
			g.setFont(titleFont);
			g.drawString(title, x, y);


			int top = 200;
			int bottom = 200;
			if (maxValue == minValue)
				if (maxValue == minValue)
					return;

			double scale = (graphHeight - top - bottom) / (maxValue - minValue);
			y = graphHeight - labelFontMet.getDescent();
			g.setFont(labelFont);

			for (int i = 0; i < v.size(); i++) {
				int valueX = i * barWidth + 1;
				int valueY = top;
				int height = (int) (v.get(i) * scale);

				if (v.get(i) >= 0)
					valueY += (int) ((maxValue - v.get(i)) * scale);

				else {
					valueY += (int) (maxValue * scale);

					height = -height;
				}

				g.setColor(Color.red);
				g.fillRect(valueX, valueY, barWidth - 2, (int) height);

				g.setColor(Color.black);
				g.drawRect(valueX, valueY, barWidth - 2, (int) height);

				// Draw Y.
				int h = getHeight();
				int w = getWidth();
				g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
				// Draw X.
				g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));

			} 

		}

	}

}

























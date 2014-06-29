package de.uni_jena.min.in0043.nine_mens_morris.test;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class ToyBoard extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JLabel[] p = new JLabel[24];
	

	/**
	 * Create the panel.
	 */
	public ToyBoard() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		p[0] = new JLabel("Free");
		GridBagConstraints gbc_p0 = new GridBagConstraints();
		gbc_p0.insets = new Insets(0, 0, 5, 5);
		gbc_p0.gridx = 0;
		gbc_p0.gridy = 0;
		add(p[0], gbc_p0);
		
		p[1] = new JLabel("Free");
		GridBagConstraints gbc_p1 = new GridBagConstraints();
		gbc_p1.insets = new Insets(0, 0, 5, 5);
		gbc_p1.gridx = 3;
		gbc_p1.gridy = 0;
		add(p[1], gbc_p1);
		
		p[2] = new JLabel("Free");
		GridBagConstraints gbc_p2 = new GridBagConstraints();
		gbc_p2.insets = new Insets(0, 0, 5, 0);
		gbc_p2.gridx = 6;
		gbc_p2.gridy = 0;
		add(p[2], gbc_p2);
		
		p[3]= new JLabel("Free");
		GridBagConstraints gbc_p3 = new GridBagConstraints();
		gbc_p3.insets = new Insets(0, 0, 5, 5);
		gbc_p3.gridx = 1;
		gbc_p3.gridy = 2;
		add(p[3], gbc_p3);
		
		p[4]= new JLabel("Free");
		GridBagConstraints gbc_p4 = new GridBagConstraints();
		gbc_p4.insets = new Insets(0, 0, 5, 5);
		gbc_p4.gridx = 3;
		gbc_p4.gridy = 2;
		add(p[4], gbc_p4);
		
		p[5]= new JLabel("Free");
		GridBagConstraints gbc_p5 = new GridBagConstraints();
		gbc_p5.insets = new Insets(0, 0, 5, 5);
		gbc_p5.gridx = 5;
		gbc_p5.gridy = 2;
		add(p[5], gbc_p5);
		
		p[6]= new JLabel("Free");
		GridBagConstraints gbc_p6 = new GridBagConstraints();
		gbc_p6.insets = new Insets(0, 0, 5, 5);
		gbc_p6.gridx = 2;
		gbc_p6.gridy = 4;
		add(p[6], gbc_p6);
		
		p[7]= new JLabel("Free");
		GridBagConstraints gbc_p7 = new GridBagConstraints();
		gbc_p7.insets = new Insets(0, 0, 5, 5);
		gbc_p7.gridx = 3;
		gbc_p7.gridy = 4;
		add(p[7], gbc_p7);
		
		p[8]= new JLabel("Free");
		GridBagConstraints gbc_p8 = new GridBagConstraints();
		gbc_p8.insets = new Insets(0, 0, 5, 5);
		gbc_p8.gridx = 4;
		gbc_p8.gridy = 4;
		add(p[8], gbc_p8);
		
		p[9]= new JLabel("Free");
		GridBagConstraints gbc_p9 = new GridBagConstraints();
		gbc_p9.insets = new Insets(0, 0, 5, 5);
		gbc_p9.gridx = 0;
		gbc_p9.gridy = 5;
		add(p[9], gbc_p9);
		
		p[10]= new JLabel("Free");
		GridBagConstraints gbc_p10 = new GridBagConstraints();
		gbc_p10.insets = new Insets(0, 0, 5, 5);
		gbc_p10.gridx = 1;
		gbc_p10.gridy = 5;
		add(p[10], gbc_p10);
		
		p[11] = new JLabel("Free");
		GridBagConstraints gbc_p11 = new GridBagConstraints();
		gbc_p11.insets = new Insets(0, 0, 5, 5);
		gbc_p11.gridx = 2;
		gbc_p11.gridy = 5;
		add(p[11], gbc_p11);
		
		p[12]= new JLabel("Free");
		GridBagConstraints gbc_p12 = new GridBagConstraints();
		gbc_p12.insets = new Insets(0, 0, 5, 5);
		gbc_p12.gridx = 4;
		gbc_p12.gridy = 5;
		add(p[12], gbc_p12);
		
		p[13] = new JLabel("Free");
		GridBagConstraints gbc_p13 = new GridBagConstraints();
		gbc_p13.insets = new Insets(0, 0, 5, 5);
		gbc_p13.gridx = 5;
		gbc_p13.gridy = 5;
		add(p[13], gbc_p13);
		
		p[14] = new JLabel("Free");
		GridBagConstraints gbc_p14 = new GridBagConstraints();
		gbc_p14.insets = new Insets(0, 0, 5, 0);
		gbc_p14.gridx = 6;
		gbc_p14.gridy = 5;
		add(p[14], gbc_p14);
		
		p[15] = new JLabel("Free");
		GridBagConstraints gbc_p15 = new GridBagConstraints();
		gbc_p15.insets = new Insets(0, 0, 5, 5);
		gbc_p15.gridx = 2;
		gbc_p15.gridy = 6;
		add(p[15], gbc_p15);
		
		p[16] = new JLabel("Free");
		GridBagConstraints gbc_p16 = new GridBagConstraints();
		gbc_p16.insets = new Insets(0, 0, 5, 5);
		gbc_p16.gridx = 3;
		gbc_p16.gridy = 6;
		add(p[16], gbc_p16);
		
		p[17] = new JLabel("Free");
		GridBagConstraints gbc_p17 = new GridBagConstraints();
		gbc_p17.insets = new Insets(0, 0, 5, 5);
		gbc_p17.gridx = 4;
		gbc_p17.gridy = 6;
		add(p[17], gbc_p17);
		
		p[18] = new JLabel("Free");
		GridBagConstraints gbc_p18 = new GridBagConstraints();
		gbc_p18.insets = new Insets(0, 0, 5, 5);
		gbc_p18.gridx = 1;
		gbc_p18.gridy = 8;
		add(p[18], gbc_p18);
		
		p[19] = new JLabel("Free");
		GridBagConstraints gbc_p19 = new GridBagConstraints();
		gbc_p19.insets = new Insets(0, 0, 5, 5);
		gbc_p19.gridx = 3;
		gbc_p19.gridy = 8;
		add(p[19], gbc_p19);
		
		p[20] = new JLabel("Free");
		GridBagConstraints gbc_p20 = new GridBagConstraints();
		gbc_p20.insets = new Insets(0, 0, 5, 5);
		gbc_p20.gridx = 5;
		gbc_p20.gridy = 8;
		add(p[20], gbc_p20);
		
		p[21] = new JLabel("Free");
		GridBagConstraints gbc_p21 = new GridBagConstraints();
		gbc_p21.insets = new Insets(0, 0, 0, 5);
		gbc_p21.gridx = 0;
		gbc_p21.gridy = 10;
		add(p[21], gbc_p21);
		
		p[22] = new JLabel("Free");
		GridBagConstraints gbc_p22 = new GridBagConstraints();
		gbc_p22.insets = new Insets(0, 0, 0, 5);
		gbc_p22.gridx = 3;
		gbc_p22.gridy = 10;
		add(p[22], gbc_p22);
		
		p[23] = new JLabel("Free");
		GridBagConstraints gbc_p23 = new GridBagConstraints();
		gbc_p23.gridx = 6;
		gbc_p23.gridy = 10;
		add(p[23], gbc_p23);

	}

}

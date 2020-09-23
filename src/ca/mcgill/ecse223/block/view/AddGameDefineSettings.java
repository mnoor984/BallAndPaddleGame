package ca.mcgill.ecse223.block.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOGame;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.NumericShaper;

import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddGameDefineSettings extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2810490576149265059L;
	private JPanel contentPane;
	private JTextField nrOfLevelsTxt;
	private JTextField NrOfBlocksTxt;
	private JTextField minBallSpeedXTxt;
	private JTextField minBallSpeedYTxt;
	private JTextField BallFactorTxt;
	private JTextField maxPaddleLengthTxt;
	private JTextField minePaddleLengthTxt;
	private JLabel minBallSpeedLabel;
	private JLabel ballFactorLabel;
	private JLabel maxPaddleLengthLabel;
	private JLabel minPaddleLengthLabel;
	private JButton createGameButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddGameDefineSettings frame = new AddGameDefineSettings();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AddGameDefineSettings() {
		setTitle("Add Game - Define Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 361, 395);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		nrOfLevelsTxt = new JTextField();
		nrOfLevelsTxt.setColumns(10);
		
		NrOfBlocksTxt = new JTextField();
		NrOfBlocksTxt.setColumns(10);
		
		minBallSpeedXTxt = new JTextField();
		minBallSpeedXTxt.setColumns(10);
		
		minBallSpeedYTxt = new JTextField();
		minBallSpeedYTxt.setColumns(10);
		
		BallFactorTxt = new JTextField();
		BallFactorTxt.setColumns(10);
		
		maxPaddleLengthTxt = new JTextField();
		maxPaddleLengthTxt.setColumns(10);
		
		minePaddleLengthTxt = new JTextField();
		minePaddleLengthTxt.setColumns(10);
		
		JLabel nrOfLevelsLabel = new JLabel("Number of levels:");
		
		JLabel nrOfBlocksLabel = new JLabel("Number of blocks:");
		
		minBallSpeedLabel = new JLabel("Min ball speed (left for X, right for Y)");
		
		ballFactorLabel = new JLabel("Ball increase factor:");
		
		maxPaddleLengthLabel = new JLabel("Max paddle length:");
		
		minPaddleLengthLabel = new JLabel("Min paddle length:");
		
		createGameButton = new JButton("Define settings");
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGap(57)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(68)
							.addComponent(createGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(67))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(minPaddleLengthLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(maxPaddleLengthLabel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
									.addComponent(ballFactorLabel, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
									.addComponent(minBallSpeedXTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
								.addGap(10)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addComponent(minBallSpeedYTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
									.addComponent(minePaddleLengthTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
									.addComponent(maxPaddleLengthTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
									.addComponent(BallFactorTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addComponent(nrOfLevelsLabel)
									.addComponent(nrOfBlocksLabel, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
								.addGap(16)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addComponent(NrOfBlocksTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
									.addComponent(nrOfLevelsTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)))
							.addComponent(minBallSpeedLabel)))
					.addGap(48))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(41, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(nrOfLevelsTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(nrOfLevelsLabel))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(NrOfBlocksTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(nrOfBlocksLabel))
					.addGap(18)
					.addComponent(minBallSpeedLabel)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(minBallSpeedXTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(minBallSpeedYTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(10)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(BallFactorTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(ballFactorLabel))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(maxPaddleLengthTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(maxPaddleLengthLabel))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(minePaddleLengthTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(minPaddleLengthLabel))
					.addGap(18)
					.addComponent(createGameButton)
					.addGap(30))
		);
		contentPane.setLayout(gl_contentPane);
		createGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String error = "";
				int nrLevels = 0; 
				try {
					nrLevels = Integer.parseInt(nrOfLevelsTxt.getText());
				} catch(NumberFormatException e){
					error = "Number of level needs to be a numerical value!";
				}
				int nrBlocksPerLevel = 0;
				try {
					nrBlocksPerLevel = Integer.parseInt(NrOfBlocksTxt.getText());
				} catch (NumberFormatException e) {
					error = error + "Number of blocks needs to be a numrical value!";
				}
				int minBallSpeedX = 0;
				try {
					minBallSpeedX = Integer.parseInt(minBallSpeedXTxt.getText());
				} catch (NumberFormatException e) {
					error = error + "Min ball speed needs to be a numerical value!";
				}
				int minBallSpeedY = 0;
				try {
					minBallSpeedY = Integer.parseInt(minBallSpeedYTxt.getText());
				} catch (NumberFormatException e) {
					error = error + "Min ball speed needs to be a numerical value!";
				}
				double ballSpeedIncreaseFactor = 0;
				try {
					ballSpeedIncreaseFactor = Double.parseDouble(BallFactorTxt.getText());
				} catch (NumberFormatException e) {
					error = error + "Ball increase factor needs to be a numerical value!";
				}
				int maxPaddleLength = 0;
				try {
					maxPaddleLength = Integer.parseInt(maxPaddleLengthTxt.getText());
				} catch (NumberFormatException e) {
					error = error + "Max paddle length needs to be a numerical value!";
				}
				int minPaddleLength = 0; 
				try {
					minPaddleLength = Integer.parseInt(minePaddleLengthTxt.getText());
				} catch (NumberFormatException e) {
					error = error + "Min paddle length needs to be a numerical value!";
				}
				error.trim();
				
				if (error.length() == 0) {
					try {
						Block223Controller.setGameDetails(nrLevels, nrBlocksPerLevel, minBallSpeedX, minBallSpeedY, ballSpeedIncreaseFactor, maxPaddleLength, minPaddleLength);
						JOptionPane.showMessageDialog(AddGameDefineSettings.this, "Game defined!");
						AddGameDefineSettings.this.dispose();
					} catch (InvalidInputException e) {
						JOptionPane.showMessageDialog(contentPane, e.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(AddGameDefineSettings.this, error);
				}
				
				//update visuals
//				refreshDate();
			}
		});
	}
}

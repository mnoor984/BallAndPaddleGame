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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UpdateGameSettings extends JFrame {

	private JPanel contentPane;
	private JTextField name;
	private JTextField nrOfLevelsTxt;
	private JTextField NrOfBlocksTxt;
	private JTextField minBallSpeedXTxt;
	private JTextField minBallSpeedYTxt;
	private JTextField BallFactorTxt;
	private JTextField maxPaddleLengthTxt;
	private JTextField minePaddleLengthTxt;
	private JLabel nameLabel;
	private JLabel minBallSpeedLabelX;
	private JLabel minBallSpeedLabelY;
	private JLabel ballFactorLabel;
	private JLabel maxPaddleLengthLabel;
	private JLabel minPaddleLengthLabel;
	private JButton updateGameButton;
	private ManageGames parentWindow;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateGameSettings frame = new UpdateGameSettings();
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
	public UpdateGameSettings() {
		setTitle("Update Game");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 361, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		name = new JTextField();
		name.setColumns(10);
		
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
		
		try {
			TOGame game = Block223Controller.getCurrentDesignableGame();
			name.setText(game.getName());
			nrOfLevelsTxt.setText(game.getNrLevels()+"");
			NrOfBlocksTxt.setText(game.getNrBlocksPerLevel()+"");
			minBallSpeedXTxt.setText(game.getMinBallSpeedX()+"");
			minBallSpeedYTxt.setText(game.getMinBallSpeedY()+"");
			BallFactorTxt.setText(game.getBallSpeedIncreaseFactor()+"");
			maxPaddleLengthTxt.setText(game.getMaxPaddleLength()+"");
			minePaddleLengthTxt.setText(game.getMinPaddleLength()+"");
		} catch (InvalidInputException e1) {
			e1.printStackTrace();
		}
		
		JLabel nrOfLevelsLabel = new JLabel("Number of levels:");
		
		JLabel nrOfBlocksLabel = new JLabel("Number of blocks:");
		
		minBallSpeedLabelX = new JLabel("Min ball speed Y:");
		
		minBallSpeedLabelY = new JLabel("Min ball speed X:");
		
		ballFactorLabel = new JLabel("Ball increase factor:");
		
		maxPaddleLengthLabel = new JLabel("Max paddle length:");
		
		minPaddleLengthLabel = new JLabel("Min paddle length:");
		
		nameLabel = new JLabel("Game name:");
		
		updateGameButton = new JButton("Update game");
		updateGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Block223Controller.updateGame(
							name.getText(), 
							Integer.valueOf(nrOfLevelsTxt.getText()),
							Integer.valueOf(NrOfBlocksTxt.getText()),
							Integer.valueOf(minBallSpeedXTxt.getText()),
							Integer.valueOf(minBallSpeedYTxt.getText()),
							Double.valueOf(BallFactorTxt.getText()),
							Integer.valueOf(maxPaddleLengthTxt.getText()),
							Integer.valueOf(minePaddleLengthTxt.getText()));
					UpdateGameSettings.this.dispose();
					parentWindow.refreshData();
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(contentPane, e.getMessage());
				} catch (InvalidInputException e) {
					JOptionPane.showMessageDialog(contentPane, e.getMessage());
				}
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(57)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(nameLabel)
						.addComponent(nrOfLevelsLabel)
						.addComponent(nrOfBlocksLabel, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
						.addComponent(minBallSpeedLabelX, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addComponent(minBallSpeedLabelY, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addComponent(ballFactorLabel, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(minPaddleLengthLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(maxPaddleLengthLabel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)))
					.addGap(10)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(minePaddleLengthTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
						.addComponent(maxPaddleLengthTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
						.addComponent(BallFactorTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
						.addComponent(minBallSpeedXTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
						.addComponent(minBallSpeedYTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
						.addComponent(NrOfBlocksTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
						.addComponent(nrOfLevelsTxt, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
						.addComponent(name, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(48, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(123)
					.addComponent(updateGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(115))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(22, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(nameLabel))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(nrOfLevelsTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(nrOfLevelsLabel))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(NrOfBlocksTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(nrOfBlocksLabel))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(minBallSpeedXTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(minBallSpeedLabelX))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(minBallSpeedYTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(minBallSpeedLabelY))
					.addGap(18)
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
					.addGap(41)
					.addComponent(updateGameButton)
					.addGap(19))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void setParent(ManageGames parent)
	{
		this.parentWindow = parent;
	}
}

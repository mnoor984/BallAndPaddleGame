package ca.mcgill.ecse223.block.view;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOGame;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ManageGames extends JFrame {

	private static final long serialVersionUID = -7748537705368152328L;
	private JPanel contentPane;
	private JTextField enterGameNameTxt;
	private JComboBox<String> selectAGameComboBox;
	UpdateGameSettings updateGameSettings;
	AddGameDefineSettings addGameDefineSettings = new AddGameDefineSettings();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManageGames frame = new ManageGames();
					frame.setVisible(true);
					frame.refreshData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void refreshData() throws InvalidInputException {
		selectAGameComboBox.removeAllItems();
		selectAGameComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Select game"}));
		for(TOGame toGame : Block223Controller.getDesignableGames()) {
			selectAGameComboBox.addItem(toGame.getName());
		}
		selectAGameComboBox.setSelectedIndex(0);
	}
	
	/**
	 * Create the frame.
	 */
	public ManageGames() {
		setTitle("Manage Games");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 367, 241);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		enterGameNameTxt = new JTextField();
		enterGameNameTxt.setText("Enter game name");
		enterGameNameTxt.setColumns(10);
		enterGameNameTxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				enterGameNameTxt.setText("");
			}
		});

		
		JButton addNewGameButton = new JButton("Create game / Define settings");
		addNewGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String gameName = enterGameNameTxt.getText();
				try {
					Block223Controller.createGame(gameName);
					Block223Controller.selectGame(gameName);
					JOptionPane.showMessageDialog(contentPane, "Game created. If you do not want to define any settings, close the window.");
					addGameDefineSettings.setVisible(true);
					refreshData();
				} catch (InvalidInputException e1) {
					JOptionPane.showMessageDialog(contentPane, e1.getMessage());
				}
			}
		});

		selectAGameComboBox = new JComboBox<String>();
		selectAGameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// call selectGame pls
				if (selectAGameComboBox.getSelectedItem() != null) {
					if (!((selectAGameComboBox.getSelectedItem()).equals("Select game"))) {
						String selectedGame = (String) selectAGameComboBox.getSelectedItem();
						try {
							Block223Controller.selectGame(selectedGame);
						} catch (InvalidInputException e1) {
							JOptionPane.showMessageDialog(contentPane, e1.getMessage());
						}
					}
				}
			}
		});
		selectAGameComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Select game"}));

		
		JButton updateGameButton = new JButton("Update game");
		updateGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(selectAGameComboBox.getSelectedIndex() > 0)
					{
						Block223Controller.selectGame((String) selectAGameComboBox.getSelectedItem());
						updateGameSettings = new UpdateGameSettings();
						updateGameSettings.setParent(ManageGames.this);
						updateGameSettings.setVisible(true);
					}
				} catch (InvalidInputException e1) {
					JOptionPane.showMessageDialog(contentPane, e1.getMessage());
				}
			}
		});
		
		JButton deleteGameButton = new JButton("Delete game");
		deleteGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(selectAGameComboBox.getSelectedIndex() > 0)
					{
						Block223Controller.deleteGame((String) selectAGameComboBox.getSelectedItem());
						refreshData();
					}
				} catch (InvalidInputException e1) {
					JOptionPane.showMessageDialog(contentPane, e1.getMessage());
				}
			}
		});
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(29, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(enterGameNameTxt, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
						.addComponent(selectAGameComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(addNewGameButton, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
						.addComponent(updateGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(deleteGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(37))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(37)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(enterGameNameTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(addNewGameButton))
					.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(updateGameButton)
						.addComponent(selectAGameComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addComponent(deleteGameButton)
					.addGap(27))
		);
		contentPane.setLayout(gl_contentPane);
	}
}

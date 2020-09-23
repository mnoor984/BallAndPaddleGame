package ca.mcgill.ecse223.block.view;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Set;
import java.awt.event.ActionEvent;

public class Block223Settings extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -700847621562949385L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Block223Settings frame = new Block223Settings();
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
	public Block223Settings() {
		setTitle("Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Block223Controller.saveGame();
				} catch (InvalidInputException e1) {
					JOptionPane.showMessageDialog(contentPane, e1.getMessage());
					// TODO Auto-generated catch block
				}
				Block223Settings.this.dispose();
			}
		});
		
		JButton logOutButton = new JButton("Log out");
		logOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
				for (Thread thread : threadSet) {
					if(thread.getName().contains("Thread-")) { 
						thread.stop(); 
					}
				}
				Block223Page.startGameButton.setVisible(true);
				Block223Controller.logout();
				Block223Page.selectGameToPlayComboBox.removeAllItems();
				Block223Page.selectGameToPlayComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Select game"}));
				Block223Settings.this.dispose();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(55, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(logOutButton, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
						.addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))
					.addGap(44))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(38)
					.addComponent(saveButton)
					.addPreferredGap(ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
					.addComponent(logOutButton)
					.addGap(29))
		);
		gl_contentPane.linkSize(SwingConstants.VERTICAL, new Component[] {saveButton, logOutButton});
		contentPane.setLayout(gl_contentPane);
	}
}

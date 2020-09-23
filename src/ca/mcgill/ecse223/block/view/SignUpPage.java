package ca.mcgill.ecse223.block.view;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import ca.mcgill.ecse223.block.controller.*;
public class SignUpPage extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4261351148267232018L;
	/**
	 * 
	 */
	private JPanel contentPane;
	private JTextField userNameTxt;
	private JPasswordField passwordTxt;
	private JPasswordField adminPasswordTxt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignUpPage frame = new SignUpPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// refresh method, clear the content of JTextField
		private void refreshSignup() {
			userNameTxt.setText(null);
			passwordTxt.setText(null);
			adminPasswordTxt.setText(null);
		}

	/**
	 * Create the frame.
	 */
	public SignUpPage() {
		setTitle("Sign up");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 320, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel userNameLabel = new JLabel("Username:");
		
		JLabel passwordLabel = new JLabel("Password:");
		
		userNameTxt = new JTextField();
		userNameTxt.setColumns(10);
		
		passwordTxt = new JPasswordField();
		passwordTxt.setColumns(10);
		
		JButton signUpButton = new JButton("Sign up");

		
		JLabel playerSignUpTitleLabel = new JLabel("Player sign up");
		playerSignUpTitleLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JLabel adminPasswordLabel = new JLabel("Admin pass:");
		
		adminPasswordTxt = new JPasswordField();
		adminPasswordTxt.setColumns(10);
		
		JLabel AdminRight = new JLabel("Admin rights enabled by defining admin pass:");
		AdminRight.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(45, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(AdminRight)
							.addGap(33))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(adminPasswordLabel)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(adminPasswordTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(65))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(passwordLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(userNameLabel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(passwordTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(userNameTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(53))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(signUpButton, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addGap(87))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(playerSignUpTitleLabel)
							.addGap(79))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(34)
					.addComponent(playerSignUpTitleLabel)
					.addGap(45)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(userNameLabel)
						.addComponent(userNameTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordLabel)
						.addComponent(passwordTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addComponent(AdminRight, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(adminPasswordLabel)
						.addComponent(adminPasswordTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
					.addComponent(signUpButton)
					.addGap(25))
		);
		contentPane.setLayout(gl_contentPane);
		
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = userNameTxt.getText();
				char[] charPassword = passwordTxt.getPassword();
				char[] charAdminPassword = adminPasswordTxt.getPassword();
				String password = new String(charPassword);
				String adminPassword = new String(charAdminPassword);
				try {
					Block223Controller.register(username, password, adminPassword);
					SignUpPage.this.dispose();
				} catch (InvalidInputException e1) {
					JOptionPane.showMessageDialog(contentPane, e1.getMessage());
				}
				refreshSignup();
			}
		});
	}
}

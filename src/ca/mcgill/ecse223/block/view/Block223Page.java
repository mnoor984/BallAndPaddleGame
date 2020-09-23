package ca.mcgill.ecse223.block.view;

import javax.swing.*;
import java.awt.*;

import ca.mcgill.ecse223.block.controller.*;
import ca.mcgill.ecse223.block.controller.TOUserMode.Mode;

import javax.swing.border.MatteBorder;

import acm.graphics.*;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

// Note : need method refresh data

public class Block223Page extends JFrame implements Block223PlayModeInterface  {
	
	private static final long serialVersionUID = -5468712039074806735L;
	Block223PlayModeListener bp;

	// Instantiate the other windows 
	AddGameDefineSettings addGameDefineSettings = new AddGameDefineSettings();
	ManageGames manageGames = new ManageGames();
	LoginPage loginPage = new LoginPage();
	Block223Settings block223Settings = new Block223Settings();
	BlockSettings blockSettings = new BlockSettings();
	JPanel playAreaPanel = new JPanel();
	JPanel rightMenuPanel = new JPanel();
	static JComboBox<String> selectGameToPlayComboBox = new JComboBox<String>();
	GCanvas playAreaCanva;
	GRect block;
	GOval ball;
	GRect paddle;
	public static JButton startGameButton;
	private static JTable hallOfFameTable;
	private static int indexPos = 1;
	private static String game = "";
	
	// Main window UI
	public Block223Page() {
		setResizable(false);
//		setResizable(true);
		setTitle("Block 223 - Music by HeatleyBros");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 490);
		getContentPane().setLayout(null);
		
		JPanel downMenuPanel = new JPanel();
		downMenuPanel.setLocation(0, 390);
		downMenuPanel.setPreferredSize(new Dimension(590, 60));
		downMenuPanel.setSize(new Dimension(734, 71));
		downMenuPanel.setMinimumSize(new Dimension(590, 60));
		downMenuPanel.setMaximumSize(new Dimension(590, 60));
		downMenuPanel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		getContentPane().add(downMenuPanel);
		downMenuPanel.setLayout(null);
		
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(617, 24, 57, 23);
		downMenuPanel.add(loginButton);
		
		JButton block223SettingsButton = new JButton("Settings");
		block223SettingsButton.setBounds(489, 24, 71, 23);
		
				downMenuPanel.add(block223SettingsButton);
				block223SettingsButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						block223Settings.setVisible(true);
					}
				});
		
		JButton manageGamesButton = new JButton("Manage Games");
		manageGamesButton.setBounds(167, 24, 105, 23);

		downMenuPanel.add(manageGamesButton);
		
		JButton manageBlocksButton = new JButton("Manage Blocks");
		manageBlocksButton.setBounds(329, 24, 103, 23);

		downMenuPanel.add(manageBlocksButton);
		
		JButton quitButton = new JButton("Quit");
		quitButton.setBounds(57, 24, 53, 23);

		downMenuPanel.add(quitButton);
		rightMenuPanel.setBounds(540, 0, 194, 390);
		rightMenuPanel.setPreferredSize(new Dimension(200, 330));
		rightMenuPanel.setMinimumSize(new Dimension(200, 330));
		rightMenuPanel.setMaximumSize(new Dimension(200, 330));
		rightMenuPanel.setBorder(new MatteBorder(1, 1, 0, 0, (Color) new Color(0, 0, 0)));
		getContentPane().add(rightMenuPanel);
		
		JLabel levelNumberLabel = new JLabel("Level:");
		
		JLabel livesRemainingLabel = new JLabel("Lives:");
		
		JLabel scoreLabel = new JLabel("Score:");
		
		JLabel hallOfFameLabel = new JLabel("Hall of Fame");
		hallOfFameLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JButton leftArrowButton = new JButton("<");
		leftArrowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					refreshHOF(-10);
				} catch (InvalidInputException e) {
					JOptionPane.showMessageDialog(Block223Page.this, e.getMessage());
				}
			}
		});
		
		JButton rightArrowButton = new JButton(">");
		rightArrowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refreshHOF(10);
				} catch (InvalidInputException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(Block223Page.this, e1.getMessage());
				}
			}
		});
		
		JLabel lblPrevious = new JLabel("Previous"); //.
		lblPrevious.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JLabel lblNext = new JLabel("Next");
		lblNext.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		hallOfFameTable = new JTable();
		hallOfFameTable.setBackground(new Color(240,240,240));
		hallOfFameTable.setShowVerticalLines(false);
		hallOfFameTable.setShowHorizontalLines(false);
		hallOfFameTable.setShowGrid(false);
		hallOfFameTable.setRowSelectionAllowed(false);
		hallOfFameTable.setFillsViewportHeight(true);
		hallOfFameTable.setFocusable(false);
		hallOfFameTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"Name", "Score"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		hallOfFameTable.getColumnModel().getColumn(0).setResizable(false);
		hallOfFameTable.getColumnModel().getColumn(1).setResizable(false);
		
		JCheckBox muteAudioCheckBox = new JCheckBox("Mute");

		GroupLayout gl_rightMenuPanel = new GroupLayout(rightMenuPanel);
		gl_rightMenuPanel.setHorizontalGroup(
			gl_rightMenuPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightMenuPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_rightMenuPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(levelNumberLabel, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
						.addComponent(livesRemainingLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(scoreLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
					.addComponent(muteAudioCheckBox)
					.addContainerGap())
				.addGroup(gl_rightMenuPanel.createSequentialGroup()
					.addGap(25)
					.addGroup(gl_rightMenuPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(leftArrowButton)
						.addComponent(lblPrevious, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
					.addGroup(gl_rightMenuPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_rightMenuPanel.createSequentialGroup()
							.addComponent(lblNext, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_rightMenuPanel.createSequentialGroup()
							.addComponent(rightArrowButton, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addGap(25))))
				.addGroup(gl_rightMenuPanel.createSequentialGroup()
					.addGap(25)
					.addComponent(hallOfFameTable, GroupLayout.PREFERRED_SIZE, 145, 145)
					.addContainerGap(23, Short.MAX_VALUE))
				.addGroup(gl_rightMenuPanel.createSequentialGroup()
					.addGap(62)
					.addComponent(hallOfFameLabel)
					.addContainerGap(62, Short.MAX_VALUE))
		);
		gl_rightMenuPanel.setVerticalGroup(
			gl_rightMenuPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightMenuPanel.createSequentialGroup()
					.addGroup(gl_rightMenuPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_rightMenuPanel.createSequentialGroup()
							.addGap(28)
							.addComponent(levelNumberLabel)
							.addGap(18)
							.addComponent(livesRemainingLabel)
							.addGap(18)
							.addComponent(scoreLabel))
						.addGroup(gl_rightMenuPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(muteAudioCheckBox)))
					.addGap(18)
					.addComponent(hallOfFameLabel)
					.addGap(11)
					.addComponent(hallOfFameTable, 160, 160, 160)
					.addGap(18)
					.addGroup(gl_rightMenuPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(leftArrowButton)
						.addComponent(rightArrowButton))
					.addGap(8)
					.addGroup(gl_rightMenuPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPrevious)
						.addComponent(lblNext)))
		);
		rightMenuPanel.setLayout(gl_rightMenuPanel);
		
		playAreaPanel.setBounds(new Rectangle(150, 0, 390, 390));
		playAreaPanel.setPreferredSize(new Dimension(390, 390));
		playAreaPanel.setMinimumSize(new Dimension(390, 390));
		playAreaPanel.setMaximumSize(new Dimension(390, 390));
		playAreaPanel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		playAreaPanel.setBackground(Color.GRAY);
		getContentPane().add(playAreaPanel);
		playAreaPanel.setLayout(null);

		
		JPanel testGamePanel = new JPanel();
		testGamePanel.setBorder(new MatteBorder(1, 0, 1, 1, (Color) new Color(0, 0, 0)));
		testGamePanel.setBounds(0, 150, 150, 150);
		getContentPane().add(testGamePanel);
		testGamePanel.setLayout(null);
		
		JLabel testAGameLabel = new JLabel("Test a game");
		testAGameLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		testAGameLabel.setBounds(40, 31, 70, 14);
		testGamePanel.add(testAGameLabel);
		
		JButton testGameButton = new JButton("Test Game");
		testGameButton.setBounds(30, 64, 89, 23);
		testGamePanel.add(testGameButton);
		
		JLabel warningLabel1 = new JLabel("Please first launch test mode");
		warningLabel1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		warningLabel1.setBounds(9, 110, 131, 14);
		testGamePanel.add(warningLabel1);
		
		JLabel warningLabel2 = new JLabel("from the block settings");
		warningLabel2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		warningLabel2.setBounds(20, 125, 110, 14);
		testGamePanel.add(warningLabel2);
		
		JPanel playGamePanel = new JPanel();
		playGamePanel.setBounds(0, 0, 150, 150);
		getContentPane().add(playGamePanel);
		playGamePanel.setLayout(null);
		playGamePanel.setBorder(new MatteBorder(1, 0, 0, 1, (Color) new Color(0, 0, 0)));
		
		JLabel playGameLabel = new JLabel("Start a game");
		playGameLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		playGameLabel.setBounds(34, 21, 81, 14);
		playGamePanel.add(playGameLabel);
		
		startGameButton = new JButton("Start Game");
		startGameButton.setBounds(30, 59, 89, 23);
		playGamePanel.add(startGameButton);
		selectGameToPlayComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Block223Controller.getUserMode().getMode().equals(Mode.None)) { return; }
				if (selectGameToPlayComboBox.getSelectedItem() != null) {
					if (!((selectGameToPlayComboBox.getSelectedItem()).equals("Select game"))) {
						String rawName = String.valueOf(selectGameToPlayComboBox.getSelectedItem());
						String[] nameTokens = rawName.split(" ");
						game = new String("");
						int nbTokens = nameTokens.length;
						if (nameTokens[nbTokens - 1].equals("(New)")) {
							for (int i = 0; i < nbTokens - 1; i++) {
								game += nameTokens[i];
								game += " ";
							}
						}
						else {
							for (int i = 0; i < nbTokens - 2; i++) {
								game += nameTokens[i];
								game += " ";
							}
						}
							try {
								if (nameTokens[nbTokens - 1].equals("(New)")) {
									Block223Controller.selectPlayableGame(game.trim(), -1);
								}
								else {
									Block223Controller.selectPlayableGame(null, Integer.valueOf(nameTokens[nbTokens - 1])); 
								}
								indexPos = 1;
								refreshHOF(0);
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(Block223Page.this, "Please login first.");
							}
					}
				}
			}
		});
		
		selectGameToPlayComboBox.setModel(new DefaultComboBoxModel(new String[] {"Select Game"}));
		selectGameToPlayComboBox.setBounds(18, 108, 113, 20);
		playGamePanel.add(selectGameToPlayComboBox);
		
		JPanel stopGamePanel = new JPanel();
		stopGamePanel.setBorder(new MatteBorder(0, 0, 0, 1, (Color) new Color(0, 0, 0)));
		stopGamePanel.setBounds(0, 300, 150, 90);
		getContentPane().add(stopGamePanel);
		stopGamePanel.setLayout(null);
		
		JButton stopGameModeButton = new JButton("Stop");
		stopGameModeButton.setBounds(30, 33, 89, 23);
		stopGamePanel.add(stopGameModeButton);
		
		// Listeners 
		
		muteAudioCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (muteAudioCheckBox.isSelected()) {
					Block223Controller.stopMusic();
				}
				else {
					Block223Controller.playMusic();
				}
			}
		});
		
		stopGameModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Block223Controller.getUserMode().getMode().equals(Mode.None)) { return; }
				if ((playAreaCanva != null)) {
					playAreaCanva.dispatchEvent(new KeyEvent(playAreaCanva, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_SPACE, ' '));					
				}
				else {
					JOptionPane.showMessageDialog(Block223Page.this, "Please login first.");
				}
				try {
					Thread.sleep(110);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (playAreaCanva != null) {
					playAreaPanel.remove(playAreaCanva);
					playAreaCanva.removeAll();
				}
				startGameButton.setVisible(true);
				testGameButton.setVisible(true);
				selectGameToPlayComboBox.setVisible(true);
				levelNumberLabel.setText("Level:");
				livesRemainingLabel.setText("Lives:");
				scoreLabel.setText("Score:");
				Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
				for (Thread thread : threadSet) {
					if (thread.getName().contains("Thread-")) {
						thread.stop();
					}
				}
				gameover = true;
				try {
					if ((BlockSettings.testModeAuthorized == false) && (Block223Controller.getUserMode().equals(new TOUserMode(Mode.Design)))) {
						updatePlayableGamesList();
						refreshHOF(0);
					}
				} catch (InvalidInputException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				BlockSettings.testModeAuthorized = false;
			}
		});
		
		startGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// start game
					if (Block223Controller.getUserMode().getMode().equals(Mode.None)) { return; }
							if (playAreaCanva != null) {
								playAreaPanel.remove(playAreaCanva);
								playAreaCanva.removeAll();
							}
							// initiating a thread to start listening to keyboard inputs
							bp = new Block223PlayModeListener();
							Runnable r1 = new Runnable() {
								@Override
								public void run() {
									// in the actual game, add keyListener to the game window
									playAreaCanva = new GCanvas();
									playAreaCanva.setVisible(true);
									playAreaCanva.setBounds(new Rectangle(0, 0, 390, 390));
									playAreaCanva.setBackground(Color.white);
									playAreaPanel.add(playAreaCanva);
									playAreaCanva.addKeyListener(bp);
									refresh();
								}
							};
							Thread t1 = new Thread(r1);
							t1.start();
							// to be on the safe side use join to start executing thread t1 before executing
							// the next thread
							try {
								t1.join();
							} catch (InterruptedException e1) {
							}

							// initiating a thread to start the game loop
							Runnable r2 = new Runnable() {
								@Override
								public void run() {
									try {
										startGameButton.setVisible(false);
										testGameButton.setVisible(false);
										gameover = false;
										Block223Controller.startGame(Block223Page.this);
										while (Block223Controller.getCurrentPlayableGame().getLives() > 0) {
											if (Block223Controller.getCurrentPlayableGame().isPaused()) {
												try {
													refreshGameStats();
												} catch (InvalidInputException e) {
													e.printStackTrace();
												}
												
												String userInputs = bp.takeInputs();

												if (userInputs.contains(" ")) {
													Block223Controller.startGame(Block223Page.this);
												}
												try {
													Thread.sleep(100);
												} catch (InterruptedException e) {
													e.printStackTrace();
												}
											}
										}
									} catch (InvalidInputException e) {
									}
									startGameButton.setVisible(true);
									testGameButton.setVisible(true);
									gameover = true;
									levelNumberLabel.setText("Level:");
									livesRemainingLabel.setText("Lives:");
									scoreLabel.setText("Score:");
									try {
										updatePlayableGamesList();
										refreshHOF(0);
									} catch (InvalidInputException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							};
							Thread t2 = new Thread(r2);
							t2.start();
							try {
								updatePlayableGamesList();
							} catch (InvalidInputException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								refreshGameStats();
								refreshHOF(0);
							} catch (InvalidInputException e) {
								e.printStackTrace();
							}
					}
		});

		testGameButton.addActionListener(new ActionListener() {
			// need to do buttons.setVisible(false) for all other buttons
			public void actionPerformed(ActionEvent e) {			
				if (BlockSettings.testModeAuthorized) {
					// initiating a thread to start the game loop
					bp = new Block223PlayModeListener();
					
					Runnable r1 = new Runnable() {
						@Override
						public void run() {
							// in the actual game, add keyListener to the game window
							playAreaCanva = new GCanvas();
							playAreaCanva.setVisible(true);
							playAreaCanva.setBounds(new Rectangle(0, 0, 390, 390));
							playAreaCanva.setBackground(Color.white);
							playAreaPanel.add(playAreaCanva);
							playAreaCanva.addKeyListener(bp);
						}
					};
					
					Thread t1 = new Thread(r1);
					t1.start();
					try {
						t1.join();
					} catch (InterruptedException e1) {
					}
					
					
					Runnable r2 = new Runnable() {
						@Override
						public void run() {
							try {
								gameover = false;
								startGameButton.setVisible(false);
								selectGameToPlayComboBox.setVisible(false);
								Block223Controller.testGame(Block223Page.this);
								BlockSettings.testModeAuthorized = false;
								stopGameModeButton.doClick();
							} catch (InvalidInputException e) {
								JOptionPane.showMessageDialog(Block223Page.this, e.getMessage());
							}
						}
					};
			
					Thread t2 = new Thread(r2);
					t2.start();	
					// to be on the safe side use join to start executing thread t1 before executing
					// the next thread
				}
				else {
					JOptionPane.showMessageDialog(Block223Page.this, "Please first launch test mode from the block settings.");
				}
			}
		});
		
		manageGamesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					manageGames.refreshData();
					manageGames.setVisible(true);
				} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(Block223Page.this, e.getMessage());
				}
			}
		});

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginPage.setVisible(true);
			}
		});

		manageBlocksButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					blockSettings.updateGamesList();
					blockSettings.setVisible(true);
				} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(Block223Page.this, e.getMessage());
				}
			}
		});

		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
	}
	
	private void refreshGameStats() throws InvalidInputException {
		TOCurrentlyPlayedGame currentGame = Block223Controller.getCurrentPlayableGame();
		int livesRemaining = currentGame.getLives();
		int currentLevel = currentGame.getCurrentLevel();
		int currentScore = currentGame.getScore();
		
		Component[] components = rightMenuPanel.getComponents();
		for (Component component : components) {
			if (component instanceof JLabel) {
				JLabel label = (JLabel) component;
				if (label.getText().startsWith("Level:")) {
					label.setText("Level: " + currentLevel);
				}
				else if (label.getText().startsWith("Lives:")) {
					label.setText("Lives: " + livesRemaining);
				}
				else if (label.getText().startsWith("Score:")) {
					label.setText("Score: " + currentScore);
				}
			}
		}
	}
	
	private static TOHallOfFame hofData = null;
	public static boolean gameover = true;
	
	public static void refreshHOF(int offset) throws InvalidInputException {
		DefaultTableModel model = (DefaultTableModel) hallOfFameTable.getModel();
		if ((hofData != null) && (model.getRowCount() < 10)) {
			if (offset > 0) { return; }
		}
		if ((hofData != null) && ((indexPos + offset) < 1)) { return; }
		model.setRowCount(0);
		if (!gameover) { 
			hofData = Block223Controller.getHallOfFame(indexPos + offset, indexPos + 9 + offset);
			for (int i = 0; i < hofData.numberOfEntries(); i++) {
				model.addRow(new Object[]{hofData.getEntry(i).getPlayername(), hofData.getEntry(i).getScore()});
			}
		}
		else {
			hofData = Block223Controller.getHallOfFameGO(indexPos + offset, indexPos + 9 + offset, game.trim());
			for (int i = 0; i < hofData.numberOfEntries(); i++) {
				model.addRow(new Object[]{hofData.getEntry(i).getPlayername(), hofData.getEntry(i).getScore()});
			}
		}
		indexPos += offset;
	}
	
	public static void updatePlayableGamesList() throws InvalidInputException {
		selectGameToPlayComboBox.removeAllItems();
		selectGameToPlayComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Select game"}));
		List<TOPlayableGame> games = Block223Controller.getPlayableGames();
		for(TOPlayableGame game : games) {
			if (game.getCurrentLevel() == 0) {
				selectGameToPlayComboBox.addItem(game.getName() + " (New)");
			}
			else {
				selectGameToPlayComboBox.addItem(game.getName() + " ID: " + game.getNumber());
			}
		}
		selectGameToPlayComboBox.setSelectedIndex(0);
	}
	
	@Override
	public String takeInputs() {
		if (bp == null) {
			return "";
		}
		return bp.takeInputs();
	}
	
	@Override
	public void refresh() {
		TOCurrentlyPlayedGame currentGame = null;
		try {
			currentGame = Block223Controller.getCurrentPlayableGame();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<TOCurrentBlock> blocks = null;
		if (currentGame != null) { 
			blocks = currentGame.getBlocks(); 
		}
		else {
			return;
		}
		if (Block223Controller.getBallSpeedX() >= 10 || Block223Controller.getBallSpeedY() >= 10) {
			Block223Controller.resetBallSpeed();
		}
		if (ball != null) {
			if (ball.getX() > 390) Block223Controller.outOfPlayAreaX390();
			if (ball.getX() < 0) Block223Controller.outOfPlayAreaX0();
			if (ball.getY() > 390) Block223Controller.outOfPlayAreaY390();
			if (ball.getY() < 0) Block223Controller.outOfPlayAreaY0();
		}
		playAreaCanva.removeAll();
		ball = new GOval(Block223Controller.getBallDiameter(),Block223Controller.getBallDiameter());
		ball.setLocation(currentGame.getCurrentBallX()-5, currentGame.getCurrentBallY()-5);
		ball.setFillColor(Color.black);
		ball.setFilled(true);
		playAreaCanva.add(ball);
		
		double paddleLength = currentGame.getCurrentPaddleLength();
		paddle = new GRect(paddleLength, Block223Controller.getPaddleWidth());
		paddle.setLocation(currentGame.getCurrentPaddleX(), 355);
		paddle.setFillColor(Color.black);
		paddle.setFilled(true);
		playAreaCanva.add(paddle);
		
		for (TOCurrentBlock cblock : blocks) {
			Color color = new Color(cblock.getRed(), cblock.getGreen(), cblock.getBlue());
			block = new GRect(Block223Controller.getBlockSize(), Block223Controller.getBlockSize());  
			block.setFillColor(color);
			block.setColor(Color.black);
			block.setFilled(true);
			playAreaCanva.add(block, cblock.getX(), cblock.getY());
		}
		try {
			refreshGameStats();
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
	}
}

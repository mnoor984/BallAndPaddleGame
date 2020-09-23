package ca.mcgill.ecse223.block.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import ca.mcgill.ecse223.block.controller.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.MatteBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BlockSettings extends JFrame {

	private static final long serialVersionUID = 3667609650702992689L;
	private JPanel contentPane;
	private JTable table;
	private JColorChooser colorChooserCreateBlock = new JColorChooser();
	private JColorChooser colorChooserUpdateBlock= new JColorChooser();
	private JTextField pointsTxt;
	private JTextField updatePointsTxt;	
	private JComboBox<String> selectBlockComboBox;
	private JComboBox<String> selectGameComboBox = new JComboBox();
	private JComboBox<String> selectBlockToDeleteComboBox;
	private JComboBox selectLevelComboBox;
	List<TOGridCell> selectedLevelBlocks = null;
	int selectedLevel = 0;
	private int oldGridHorizontalPosition;
	private int oldGridVerticalPosition;
	public static boolean testModeAuthorized = false;
	private boolean atLeastOneBlockExists = false;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BlockSettings frame = new BlockSettings();
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
	public BlockSettings() {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 440);
//		setBounds(150, 150, 1000, 500);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		setTitle("Block settings");
		setResizable(false);
		
        AbstractColorChooserPanel[] panelsCreate = colorChooserCreateBlock.getChooserPanels();
        for (AbstractColorChooserPanel x : panelsCreate) {
        	if (!(x.getDisplayName().equals("RGB"))) {
        		colorChooserCreateBlock.removeChooserPanel(x);
        	}
        }
        
        AbstractColorChooserPanel[] panelsUpdate = colorChooserUpdateBlock.getChooserPanels();
        for (AbstractColorChooserPanel x : panelsUpdate) {
        	if (!(x.getDisplayName().equals("RGB"))) {
        		colorChooserUpdateBlock.removeChooserPanel(x);
        	}
        }

		JLabel blockLivePositionLabel = new JLabel("Select a block to get its position.");
		int selectedPos[] = new int[2];
		Color colorEmptyCell = Color.GRAY;

		@SuppressWarnings("serial")
		JTable gridTable = new JTable() {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);
				if ((col % 2 == 1) && (row % 2 == 1)) {
					// get color of blocks and apply here
					if ((selectedLevelBlocks != null)) {
						try {
							if (!((selectGameComboBox.getSelectedItem()).equals("Select game"))) {
								if ((Block223Controller.getBlocksAtLevelOfCurrentDesignableGame(selectedLevel).size() != 0)) {
									// color each block now.
									for (int i = 0; i < selectedLevelBlocks.size(); i++) {
										TOGridCell tempBlock = selectedLevelBlocks.get(i);
										boolean exists = false;
										for (int j = 0; j < selectedLevelBlocks.size(); j++) {
											TOGridCell tempBlock1 = selectedLevelBlocks.get(j);
											if ((tempBlock1.getGridHorizontalPosition() == (col/2)+1) && (tempBlock1.getGridVerticalPosition() == (row/2)+1)) {
												exists = true;
											}
										}
										
										if (exists) {
											if ((tempBlock.getGridHorizontalPosition() == ((col/2)+1)) && (tempBlock.getGridVerticalPosition() == ((row/2)+1))) {
												Color tempColor = new Color(tempBlock.getRed(), tempBlock.getGreen(), tempBlock.getBlue());
												comp.setBackground(tempColor);
											}
										}
										else {
											comp.setBackground(colorEmptyCell);
										}
									}
								}
							}
						} catch (InvalidInputException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}	
					else {
						comp.setBackground(colorEmptyCell);
					}
				} 
				else {
					comp.setBackground(Color.WHITE);
				}
				
				if (getSelectedRow() == row && getSelectedColumn() == col) {
					if ((getColumnModel().getColumn(col).getWidth() == 20) && (getRowHeight(row) == 20)) {
						comp.setBackground(Color.YELLOW);
						blockLivePositionLabel.setText("Selected block is at position " + ((col / 2)+1) + " / " + ((row / 2)+1) + ".");
						selectedPos[0] = (col / 2) +1;
						selectedPos[1] = (row / 2) +1;
					} else {
						blockLivePositionLabel.setText("Select a block to get its position.");
						selectedPos[0] = -1;
						selectedPos[1] = -1;
					}
				}
				return comp;
			}
		};
		gridTable.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (selectLevelComboBox.getSelectedItem() != null) {
					if (!((selectLevelComboBox.getSelectedItem()).equals("Select level"))) {
						try {
							selectedLevelBlocks = Block223Controller.getBlocksAtLevelOfCurrentDesignableGame(selectedLevel);
							if (selectedLevelBlocks.size() == 0) {
								selectedLevelBlocks = null;
							}
							gridTable.repaint();
						} catch (InvalidInputException e1) {
							JOptionPane.showMessageDialog(contentPane, e1.getMessage());
						}
					}
				}
			}
		});
		gridTable.setFocusable(false);
	    gridTable.setCellSelectionEnabled(false);
		gridTable.setShowGrid(false);
		gridTable.setRowSelectionAllowed(false);
		gridTable.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		gridTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"left wall padding", "bc1", "padding", "bc2", "padding", "bc3", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		gridTable.getColumnModel().getColumn(0).setPreferredWidth(10);
		gridTable.getColumnModel().getColumn(0).setMinWidth(10);
		gridTable.getColumnModel().getColumn(0).setMaxWidth(10);
		gridTable.getColumnModel().getColumn(1).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(1).setMinWidth(20);
		gridTable.getColumnModel().getColumn(1).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(2).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(2).setMinWidth(5);
		gridTable.getColumnModel().getColumn(2).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(3).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(3).setMinWidth(20);
		gridTable.getColumnModel().getColumn(3).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(4).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(4).setMinWidth(5);
		gridTable.getColumnModel().getColumn(4).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(5).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(5).setMinWidth(20);
		gridTable.getColumnModel().getColumn(5).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(6).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(6).setMinWidth(5);
		gridTable.getColumnModel().getColumn(6).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(7).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(7).setMinWidth(20);
		gridTable.getColumnModel().getColumn(7).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(8).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(8).setMinWidth(5);
		gridTable.getColumnModel().getColumn(8).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(9).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(9).setMinWidth(20);
		gridTable.getColumnModel().getColumn(9).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(10).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(10).setMinWidth(5);
		gridTable.getColumnModel().getColumn(10).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(11).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(11).setMinWidth(20);
		gridTable.getColumnModel().getColumn(11).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(12).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(12).setMinWidth(5);
		gridTable.getColumnModel().getColumn(12).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(13).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(13).setMinWidth(20);
		gridTable.getColumnModel().getColumn(13).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(14).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(14).setMinWidth(5);
		gridTable.getColumnModel().getColumn(14).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(15).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(15).setMinWidth(20);
		gridTable.getColumnModel().getColumn(15).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(16).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(16).setMinWidth(5);
		gridTable.getColumnModel().getColumn(16).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(17).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(17).setMinWidth(20);
		gridTable.getColumnModel().getColumn(17).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(18).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(18).setMinWidth(5);
		gridTable.getColumnModel().getColumn(18).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(19).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(19).setMinWidth(20);
		gridTable.getColumnModel().getColumn(19).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(20).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(20).setMinWidth(5);
		gridTable.getColumnModel().getColumn(20).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(21).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(21).setMinWidth(20);
		gridTable.getColumnModel().getColumn(21).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(22).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(22).setMinWidth(5);
		gridTable.getColumnModel().getColumn(22).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(23).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(23).setMinWidth(20);
		gridTable.getColumnModel().getColumn(23).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(24).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(24).setMinWidth(5);
		gridTable.getColumnModel().getColumn(24).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(25).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(25).setMinWidth(20);
		gridTable.getColumnModel().getColumn(25).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(26).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(26).setMinWidth(5);
		gridTable.getColumnModel().getColumn(26).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(27).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(27).setMinWidth(20);
		gridTable.getColumnModel().getColumn(27).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(28).setPreferredWidth(5);
		gridTable.getColumnModel().getColumn(28).setMinWidth(5);
		gridTable.getColumnModel().getColumn(28).setMaxWidth(5);
		gridTable.getColumnModel().getColumn(29).setPreferredWidth(20);
		gridTable.getColumnModel().getColumn(29).setMinWidth(20);
		gridTable.getColumnModel().getColumn(29).setMaxWidth(20);
		gridTable.getColumnModel().getColumn(30).setPreferredWidth(10);
		gridTable.getColumnModel().getColumn(30).setMinWidth(10);
		gridTable.getColumnModel().getColumn(30).setMaxWidth(10);
		
		gridTable.setRowHeight(0, 10);
		gridTable.setRowHeight(1, 20);
		gridTable.setRowHeight(2, 2);
		gridTable.setRowHeight(3, 20);
		gridTable.setRowHeight(4, 2);
		gridTable.setRowHeight(5, 20);
		gridTable.setRowHeight(6, 2);
		gridTable.setRowHeight(7, 20);
		gridTable.setRowHeight(8, 2);
		gridTable.setRowHeight(9, 20);
		gridTable.setRowHeight(10, 2);
		gridTable.setRowHeight(11, 20);
		gridTable.setRowHeight(12, 2);
		gridTable.setRowHeight(13, 20);
		gridTable.setRowHeight(14, 2);
		gridTable.setRowHeight(15, 20);
		gridTable.setRowHeight(16, 2);
		gridTable.setRowHeight(17, 20);
		gridTable.setRowHeight(18, 2);
		gridTable.setRowHeight(19, 20);
		gridTable.setRowHeight(20, 2);
		gridTable.setRowHeight(21, 20);
		gridTable.setRowHeight(22, 2);
		gridTable.setRowHeight(23, 20);
		gridTable.setRowHeight(24, 2);
		gridTable.setRowHeight(25, 20);
		gridTable.setRowHeight(26, 2);
		gridTable.setRowHeight(27, 20);
		gridTable.setRowHeight(28, 2);
		gridTable.setRowHeight(29, 20);
		gridTable.setRowHeight(30, 10);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(null);
		
		JLabel deleteBlockFromLevelLabel = new JLabel("Delete a block from level:");
		deleteBlockFromLevelLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JButton deleteFromGameButton = new JButton("Delete");
		
		JLabel deleteBlockFromGameLabel = new JLabel("Delete a block from game:");
		deleteBlockFromGameLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JButton deleteFromLevelButton = new JButton("Delete");
		
		JLabel selectBlockFromGridToDeleteLabel = new JLabel("Select block from grid &");
		
		selectBlockToDeleteComboBox = new JComboBox<String>();
		selectBlockToDeleteComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Select block"}));
		selectBlockToDeleteComboBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		    Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
			try {
				if (selectGameComboBox.getSelectedItem() != null) {
					if (!((selectGameComboBox.getSelectedItem()).equals("Select game"))) {
						List<TOBlock> blocks = Block223Controller.getBlocksOfCurrentDesignableGame();
						if (String.valueOf(value).equals(new String("Select block"))) { return component; }
						String selectedIDS = String.valueOf(value);
						int selectedID = Integer.valueOf(selectedIDS);
						for(TOBlock block: blocks) {
							if (block.getId() == selectedID) {
								component.setBackground(new Color(block.getRed(), block.getGreen(), block.getBlue()));
							}
						}
					}
				}
			} catch (InvalidInputException e) {
				e.printStackTrace();
			}
		
		    return component;
		  }
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(23, Short.MAX_VALUE)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(selectBlockToDeleteComboBox, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(deleteFromGameButton)
							.addGap(24))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(selectBlockFromGridToDeleteLabel, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(deleteFromLevelButton, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addGap(20))))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(44)
					.addComponent(deleteBlockFromLevelLabel)
					.addContainerGap(44, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(41)
					.addComponent(deleteBlockFromGameLabel)
					.addContainerGap(45, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(deleteBlockFromLevelLabel)
					.addGap(11)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(deleteFromLevelButton)
						.addComponent(selectBlockFromGridToDeleteLabel))
					.addPreferredGap(ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
					.addComponent(deleteBlockFromGameLabel)
					.addGap(18)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(deleteFromGameButton)
						.addComponent(selectBlockToDeleteComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18))
		);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(null);
		
		JButton updateColorButton = new JButton("Choose the color");
		
		updateColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			        JDialog dialog = new JDialog();
					dialog.setTitle("Choose the color");
					dialog.setContentPane(colorChooserUpdateBlock);
					dialog.pack();
					dialog.setVisible(true);
			}
		});
		
		
		JLabel updateASelectedBlockLabel = new JLabel("Update a selected block:");
		updateASelectedBlockLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel updatePointsLabel = new JLabel("Points :");
		
		updatePointsTxt = new JTextField();
		updatePointsTxt.setColumns(10);
		
		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Update a block
				boolean blockExists = false;
				int xferI = -1;
				// Get the current selected block
				if ((selectedPos[0] != -1) && (selectedPos[1] != -1)) {
					if (selectedLevelBlocks != null) {
						for (int i = 0; i < selectedLevelBlocks.size(); i++) {
							if ((selectedLevelBlocks.get(i).getGridHorizontalPosition() == selectedPos[0]) && (selectedLevelBlocks.get(i).getGridVerticalPosition() == selectedPos[1])) {
								blockExists = true;
								xferI = i;
							}
						}
						if (blockExists && (xferI != -1)) {
							int points = Integer.valueOf(updatePointsTxt.getText());
							try {
								Block223Controller.updateBlock(selectedLevelBlocks.get(xferI).getId(), colorChooserUpdateBlock.getColor().getRed(), colorChooserUpdateBlock.getColor().getGreen(), colorChooserUpdateBlock.getColor().getBlue(), points);
							} catch (InvalidInputException e) {
								JOptionPane.showMessageDialog(contentPane, e.getMessage());
							}
						}
						else {
							// get the block from the combobox
							if (selectBlockComboBox.getSelectedItem() != null) {
								if (!((selectBlockComboBox.getSelectedItem()).equals("Select block"))) {
									int selectedBlockId = Integer.parseInt(selectBlockComboBox.getSelectedItem().toString());
									int points = Integer.valueOf(updatePointsTxt.getText());
									try {
										Block223Controller.updateBlock(selectedBlockId, colorChooserUpdateBlock.getColor().getRed(), colorChooserUpdateBlock.getColor().getGreen(), colorChooserUpdateBlock.getColor().getBlue(), points);
									} catch (InvalidInputException e) {
										JOptionPane.showMessageDialog(contentPane, e.getMessage());
									}
								}
							}
						}
					}
				}
				else {
					// get the block from the combobox
					if (selectBlockComboBox.getSelectedItem() != null) {
						if (!((selectBlockComboBox.getSelectedItem()).equals("Select block"))) {
							int selectedBlockId = Integer.parseInt(selectBlockComboBox.getSelectedItem().toString());
							int points = Integer.valueOf(updatePointsTxt.getText());
							try {
								Block223Controller.updateBlock(selectedBlockId, colorChooserUpdateBlock.getColor().getRed(), colorChooserUpdateBlock.getColor().getGreen(), colorChooserUpdateBlock.getColor().getBlue(), points);
							} catch (InvalidInputException e) {
								JOptionPane.showMessageDialog(contentPane, e.getMessage());
							}
						}
					}
				}
			}
		});
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(59)
					.addComponent(updateColorButton)
					.addContainerGap(59, Short.MAX_VALUE))
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(78)
					.addComponent(updatePointsLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(updatePointsTxt, 0, 0, Short.MAX_VALUE)
					.addGap(78))
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(83)
					.addComponent(updateButton)
					.addContainerGap(81, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, gl_panel_2.createSequentialGroup()
					.addGap(46)
					.addComponent(updateASelectedBlockLabel)
					.addContainerGap(47, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(updateASelectedBlockLabel)
					.addGap(18)
					.addComponent(updateColorButton)
					.addGap(18)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(updatePointsTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(3)
							.addComponent(updatePointsLabel)))
					.addPreferredGap(ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
					.addComponent(updateButton)
					.addGap(18))
		);
		panel_2.setLayout(gl_panel_2);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(null);
		
		selectBlockComboBox = new JComboBox();
		selectBlockComboBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		    Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
			try {
				if (selectGameComboBox.getSelectedItem() != null) {
					if (!((selectGameComboBox.getSelectedItem()).equals("Select game"))) {
						List<TOBlock> blocks = Block223Controller.getBlocksOfCurrentDesignableGame();
						if (String.valueOf(value).equals(new String("Select block"))) { return component; }
						String selectedIDS = String.valueOf(value);
						int selectedID = Integer.valueOf(selectedIDS);
						for(TOBlock block: blocks) {
							if (block.getId() == selectedID) {
								component.setBackground(new Color(block.getRed(), block.getGreen(), block.getBlue()));
							}
						}
					}
				}
			} catch (InvalidInputException e) {
				e.printStackTrace();
			}
		
		    return component;
		  }
		});
		selectBlockComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Select block"}));
		selectLevelComboBox = new JComboBox();
		selectLevelComboBox.setModel(new DefaultComboBoxModel(new String[] {"Select level"}));
		JLabel placeABlockLabel = new JLabel("Place a block: ");
		placeABlockLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JButton placeBlockButton = new JButton("Place");
		placeBlockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectBlockComboBox.getSelectedItem() != null) {
					if (!((selectBlockComboBox.getSelectedItem()).equals("Select block"))) {
						int selectedBlockId = Integer.parseInt(selectBlockComboBox.getSelectedItem().toString());
						if(selectLevelComboBox.getSelectedIndex()>0) {
							try {
								Block223Controller.positionBlock(selectedBlockId, (int)selectLevelComboBox.getSelectedItem(), selectedPos[0], selectedPos[1]);
								gridTable.repaint();
							} catch (InvalidInputException e1) {
								JOptionPane.showMessageDialog(contentPane, e1.getMessage());
							}
						} else {
							JOptionPane.showMessageDialog(contentPane, "Please select Game/Level.");
						}

					}
				}
			}
		});
		
		JLabel moveABlockLabel = new JLabel("Move a block:");
		moveABlockLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel selectPositionLabel = new JLabel("Select position.");
		
		JButton moveBlockButton = new JButton("Move");
		
		JLabel selectLabel = new JLabel("Select");
		
		JLabel newPositionLabel = new JLabel("new position.");
		
		JLabel andLabel = new JLabel("&");
		
		JCheckBox blockSelectedCheckBox = new JCheckBox("Selected?");

		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(placeBlockButton)
						.addComponent(selectPositionLabel, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
						.addComponent(selectBlockComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(placeABlockLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup()
									.addComponent(moveBlockButton)
									.addGap(4))
								.addComponent(selectLabel, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
								.addComponent(newPositionLabel, GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
								.addComponent(andLabel)
								.addComponent(blockSelectedCheckBox, Alignment.TRAILING))
							.addGap(21))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(moveABlockLabel)
							.addContainerGap())))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap(129, Short.MAX_VALUE)
					.addComponent(placeBlockButton)
					.addGap(14))
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap(13, Short.MAX_VALUE)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
						.addComponent(placeABlockLabel)
						.addComponent(moveABlockLabel))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
						.addComponent(selectPositionLabel)
						.addComponent(blockSelectedCheckBox))
					.addGap(8)
					.addComponent(andLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
						.addComponent(selectLabel)
						.addComponent(selectBlockComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(newPositionLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
					.addComponent(moveBlockButton)
					.addContainerGap())
		);
		
		selectLevelComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectLevelComboBox.getSelectedItem() != null) {
					if (!((selectLevelComboBox.getSelectedItem()).equals("Select level"))) {
						selectedLevel = (int) selectLevelComboBox.getSelectedItem();
						try {
							selectedLevelBlocks = Block223Controller.getBlocksAtLevelOfCurrentDesignableGame(selectedLevel);
							if (selectedLevelBlocks.size() == 0) {
								selectedLevelBlocks = null;
							}
							gridTable.repaint();
						} catch (InvalidInputException e1) {
							JOptionPane.showMessageDialog(contentPane, e1.getMessage());
						}
					}
					else {
						selectedLevelBlocks = null;
						gridTable.repaint();
					}
				}
			}
		});
		
		panel_3.setLayout(gl_panel_3);
		selectGameComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selectGameComboBox.getSelectedItem() != null) {
					if (!((selectGameComboBox.getSelectedItem()).equals("Select game"))) {
						String selectedGame = (String) selectGameComboBox.getSelectedItem();
						try {
							Block223Controller.selectGame(selectedGame);
						} catch (InvalidInputException e1) {
							JOptionPane.showMessageDialog(contentPane, e1.getMessage());
						}
						selectLevelComboBox.removeAllItems();
						selectLevelComboBox.setModel(new DefaultComboBoxModel(new String[] {"Select level"}));
						//Once selected a game, get its levels
						int currentSelectedGameLevels = 0;
						try {
							currentSelectedGameLevels = Block223Controller.getCurrentDesignableGame().getNrLevels();
						} catch (InvalidInputException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for (int i = 0; i < currentSelectedGameLevels; i++) {
							selectLevelComboBox.addItem(i+1);
						}
						// here updates the blocks list
						blockSettingsRefreshData();
						blockDeleteRefreshData();
					}
					else {
						selectLevelComboBox.removeAllItems();
						selectLevelComboBox.setModel(new DefaultComboBoxModel(new String[] {"Select level"}));
						selectBlockComboBox.removeAllItems();
						selectBlockComboBox.setModel(new DefaultComboBoxModel(new String[] {"Select block"}));
						selectBlockToDeleteComboBox.removeAllItems();
						selectBlockToDeleteComboBox.setModel(new DefaultComboBoxModel(new String[] {"Select block"}));
					}
				}
			}
		});
		
		JButton launchTestModeButton = new JButton("Launch test mode");

		JButton publishGameButton = new JButton("Publish this game");

		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(selectGameComboBox, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(selectLevelComboBox, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(blockLivePositionLabel)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(publishGameButton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(launchTestModeButton))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(gridTable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(panel, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)))))
					.addGap(16))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(gridTable, GroupLayout.PREFERRED_SIZE, 351, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
								.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(selectGameComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(selectLevelComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(blockLivePositionLabel)
						.addComponent(launchTestModeButton)
						.addComponent(publishGameButton))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		
		JLabel createBlockLabel = new JLabel("Create a block:");
		createBlockLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JButton createColorButton = new JButton("Choose the color");
		createColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = new JDialog();
				dialog.setTitle("Choose the color");
				dialog.setContentPane(colorChooserCreateBlock);
				dialog.pack();

				dialog.setVisible(true);
			}
		});
		
		JLabel pointsLabel = new JLabel("Points :");
		
		pointsTxt = new JTextField();
		pointsTxt.setColumns(10);
		
		JButton createButton = new JButton("Create");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(59)
					.addComponent(createColorButton)
					.addContainerGap(59, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(78)
					.addComponent(pointsLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pointsTxt, 0, 0, Short.MAX_VALUE)
					.addGap(78))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(83)
					.addComponent(createButton)
					.addContainerGap(83, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(73)
					.addComponent(createBlockLabel)
					.addContainerGap(74, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(createBlockLabel)
					.addGap(18)
					.addComponent(createColorButton)
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(pointsTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(pointsLabel)))
					.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
					.addComponent(createButton)
					.addGap(18))
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);

		// Listener methods 
		
		launchTestModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!((selectGameComboBox.getSelectedItem()).equals("Select game"))) {
					blockSettingsRefreshData();
					if (atLeastOneBlockExists) {
						testModeAuthorized = true;
						BlockSettings.this.dispose();
					}
					else {
						JOptionPane.showMessageDialog(contentPane, "At least one block must be defined for a game to be tested.");	
					}
				}
			}
		});
		
		
		publishGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					Block223Controller.publishGame();
					BlockSettings.this.dispose();
				} catch (InvalidInputException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(contentPane, e1.getMessage());
				}
			}
		});
		
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        Color newColor = colorChooserCreateBlock.getColor();
		        int points = Integer.valueOf(pointsTxt.getText());
		        int red, green, blue;
		        if (newColor != null ) {
		        	red = newColor.getRed();
		        	blue = newColor.getBlue();
		        	green = newColor.getGreen();
		        } else {
		        	red = green = blue = 0;
		        }
		        try {
					Block223Controller.addBlock(red, green, blue, points);
					blockSettingsRefreshData();
					blockDeleteRefreshData();
					atLeastOneBlockExists = true;
				} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(contentPane, e.getMessage());
				}
				
			}
		});
		
		deleteFromGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (selectBlockToDeleteComboBox.getSelectedItem() != null) {
					if (!((selectBlockToDeleteComboBox.getSelectedItem().equals(new String("Select block"))))) {
						int selectedBlockId = Integer.parseInt(selectBlockToDeleteComboBox.getSelectedItem().toString());
						try {
							Block223Controller.deleteBlock(selectedBlockId);
							blockDeleteRefreshData();
							blockSettingsRefreshData();
							if (selectBlockToDeleteComboBox.getItemCount() == 1) {
								atLeastOneBlockExists = false;
							}
							} catch (InvalidInputException e) {
							JOptionPane.showMessageDialog(contentPane, e.getMessage());
						}
						
					}
				}
			}
		});
		
		deleteFromLevelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(selectLevelComboBox.getSelectedIndex()>0) {
					int indexLevel = (int) selectLevelComboBox.getSelectedItem();
					int gridHorizontalPosition = gridTable.getSelectedColumn() / 2;
					int gridVerticalPosition = gridTable.getSelectedRow() / 2;
					try {
						Block223Controller.removeBlock(indexLevel, gridHorizontalPosition + 1, gridVerticalPosition + 1);
					} catch(InvalidInputException e) {
						JOptionPane.showMessageDialog(contentPane, e.getMessage());
					} 
					blockSettingsRefreshData();
				} else {
					JOptionPane.showMessageDialog(contentPane, "Please select Game/Level.");
				}
			}
		});
		
		blockSelectedCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean blockSelected = blockSelectedCheckBox.isSelected();
				if (blockSelected) {
					oldGridHorizontalPosition = gridTable.getSelectedColumn() / 2 + 1;
					oldGridVerticalPosition = gridTable.getSelectedRow() / 2 + 1;
				}
			}
		});
		
		moveBlockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(selectLevelComboBox.getSelectedIndex()>0) {
					int newGridHorizontalPosition;
					int newGridVerticalPosition;
					int indexLevel = (int) selectLevelComboBox.getSelectedItem();
					newGridHorizontalPosition = gridTable.getSelectedColumn() / 2 + 1;
					newGridVerticalPosition = gridTable.getSelectedRow() / 2 + 1;
					try {
						Block223Controller.moveBlock(indexLevel, oldGridHorizontalPosition, oldGridVerticalPosition, newGridHorizontalPosition, newGridVerticalPosition);
					}
					catch (InvalidInputException e) {
						JOptionPane.showMessageDialog(contentPane, e.getMessage());
					}
					blockSelectedCheckBox.setSelected(false);
					blockSettingsRefreshData();
				} else {
					JOptionPane.showMessageDialog(contentPane, "Please select Game/Level.");
				}

			}
		});
	}

	public void updateGamesList() throws InvalidInputException {
		selectGameComboBox.removeAllItems();
		selectGameComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Select game"}));
		List<TOGame> games = Block223Controller.getDesignableGames();
		for(TOGame game : games) {
			selectGameComboBox.addItem(game.getName());
		}
		selectGameComboBox.setSelectedIndex(0);
	}

	public void blockSettingsRefreshData() {
		selectBlockComboBox.removeAllItems();
		selectBlockComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Select block"}));
		try {
			List<TOBlock> blocks = Block223Controller.getBlocksOfCurrentDesignableGame();
			if (blocks.size() > 0) { atLeastOneBlockExists = true; }
			for(TOBlock block: blocks) {
				selectBlockComboBox.addItem(String.valueOf(block.getId()));
			}
		} catch (InvalidInputException e) {
			JOptionPane.showMessageDialog(contentPane, e.getMessage());
		}
		selectBlockComboBox.setSelectedIndex(0);
	}
	public void blockDeleteRefreshData() {
		selectBlockToDeleteComboBox.removeAllItems();
		selectBlockToDeleteComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Select block"}));
		try {
			List<TOBlock> blocks = Block223Controller.getBlocksOfCurrentDesignableGame();
			for(TOBlock block: blocks) {
				selectBlockToDeleteComboBox.addItem(String.valueOf(block.getId()));
			}
		} catch (InvalidInputException e) {
			JOptionPane.showMessageDialog(contentPane, e.getMessage());
		}
		selectBlockToDeleteComboBox.setSelectedIndex(0);
	}
}

package ca.mcgill.ecse223.block.application;

import java.io.File;

import javax.sound.sampled.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import ca.mcgill.ecse223.block.model.*;
import ca.mcgill.ecse223.block.persistence.Block223Persistence;
import ca.mcgill.ecse223.block.view.Block223Page;

public class Block223Application {
	
	private static Block223 block223;
	private static UserRole currentUserRole;
	private static Game currentGame;
	private static PlayedGame currentPlayableGame;
	private static Clip clip;
	
	public static void main(String[] args) {
	// start UI
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                new Block223Page().setVisible(true);
            }
        });
        String filepath = "./lib/bgmusic.wav";
        playMusic(filepath);
	}
	public static void playMusic(String filepath) {
		try {
			File musicPath = new File(filepath);
			if (musicPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start();
				if (filepath.equals("./lib/bgmusic.wav")) clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				JOptionPane.showMessageDialog(null, "Error: Coud not find or play music file.");
			}
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	public static void stopMusic() {
		clip.stop();
	}
	// returns the root block223 object
	public static Block223 getBlock223() {
		if (block223 == null) {
			block223 = Block223Persistence.load();
		}
		return block223;
	}
	
	// forces a load from the file and return the root block223 object
	public static Block223 resetBlock223() {
		if (block223 != null) {
			block223.delete();
		}
		setCurrentGame(null);
		setCurrentPlayableGame(null);
		block223 = Block223Persistence.load();
		return block223;
	}
	
	
	// remember the currently logged in user role
	// change setCurrentUserRole to type UserRole, add return statement, modification wrt the logout() feature
	public static void setCurrentUserRole(UserRole aUserRole) {
		currentUserRole = aUserRole;
	}
	
	// return the currently logged in user role
	public static UserRole getCurrentUserRole() {
		return currentUserRole;
	}	
	
	// remember the current game
	public static void setCurrentGame(Game aGame) {
		currentGame = aGame;
	}
	
	// return the current game
	public static Game getCurrentGame() {
		return currentGame;
	}
	
	public static void setCurrentPlayableGame(PlayedGame aGame) {
		currentPlayableGame = aGame;
	}

	public static PlayedGame getCurrentPlayableGame() {
		return currentPlayableGame;
	}
		
}

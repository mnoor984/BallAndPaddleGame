package ca.mcgill.ecse223.block.controller;

import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Finishings;
import javax.swing.JOptionPane;

import ca.mcgill.ecse223.block.model.*;
import ca.mcgill.ecse223.block.model.PlayedGame.PlayStatus;
import ca.mcgill.ecse223.block.persistence.Block223Persistence;
import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.controller.TOUserMode.Mode;
import ca.mcgill.ecse223.block.view.Block223Page;
import ca.mcgill.ecse223.block.view.Block223PlayModeInterface;


public class Block223Controller {

	// ****************************
	// Modifier methods
	// ****************************

	// feature 1 add a game
	public static void createGame(String name) throws InvalidInputException {
		Block223 block223 = Block223Application.getBlock223();
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) 
		{
			throw new InvalidInputException("Admin privileges are required to create a game.");
		}
		UserRole role = Block223Application.getCurrentUserRole();
		Admin admin = (Admin) role;
		try {
			if (Block223Application.getBlock223().findGame(name) != null)
			{
				throw new InvalidInputException("The name of a game must be unique.");
			}
			else
			{
				Game game = new Game(name,1,admin,1,1,1,10,10,block223);
			}
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
//		try {
//			selectGame(name);
//		} catch (InvalidInputException e) {
//			e.printStackTrace();
//		}
	}

	// feature 2 define game settings
	public static void setGameDetails(int nrLevels, int nrBlocksPerLevel, int minBallSpeedX, int minBallSpeedY,
			Double ballSpeedIncreaseFactor, int maxPaddleLength, int minPaddleLength) throws InvalidInputException 
	{
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) 
		{
			throw new InvalidInputException("Admin privileges are required to define game settings.");
		}

		Game game = Block223Application.getCurrentGame();

		if (game == null) 
		{
			throw new InvalidInputException("A game must be selected to define game settings.");
		}
		if (!(Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin()))) 
		{
			throw new InvalidInputException("Only the admin who created the game can define its game settings.");
		}
		if (nrLevels < 1 || nrLevels > 99)
		{
			throw new InvalidInputException("The number of levels must be between 1 and 99.");
		}
		if ((minBallSpeedX == 0) && (minBallSpeedY == 0)) {
			throw new InvalidInputException("The minimum speed of the ball must be greater than zero.");
		}

		try {
			game.setNrBlocksPerLevel(nrBlocksPerLevel);
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		Ball ball = game.getBall();
		try {
			game.setNrBlocksPerLevel(nrBlocksPerLevel);
			ball.setMinBallSpeedX(minBallSpeedX);
			ball.setMinBallSpeedY(minBallSpeedY);
			ball.setBallSpeedIncreaseFactor(ballSpeedIncreaseFactor);
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		Paddle paddle = game.getPaddle();
		try {
			paddle.setMaxPaddleLength(maxPaddleLength);
			paddle.setMinPaddleLength(minPaddleLength);
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		List<Level> levels = game.getLevels();
		int size = levels.size();
		while (nrLevels > size) {
			game.addLevel();
			size = levels.size();
		}
		while (nrLevels < size) {
			Level level = game.getLevel(size-1);
			level.delete();
			size = levels.size();
		}
	}

	// feature 3 delete a game
	public static void deleteGame(String name) throws InvalidInputException {
		UserRole currentRole = Block223Application.getCurrentUserRole();
		if (!(currentRole instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to delete a game.");
		}
		else {
			Block223 block223 = Block223Application.getBlock223();
			Game game = block223.findGame(name);
			if(game != null) {
				if ((game.getAdmin()!=currentRole)) {
					throw new InvalidInputException("Only the admin who created the game can delete the game.");
				}
				else {
					if(game.getPublished())
					{
						throw new InvalidInputException("A published game cannot be deleted.");
					}
					else
					{
						game.delete();
						Block223Persistence.save(block223);
					}
				}
			}
		}
	}

	// feature 4 update a game
	public static void selectGame(String name) throws InvalidInputException {
		UserRole currentRole = Block223Application.getCurrentUserRole();
		if (!(currentRole instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to select a game.");
		}
		else {
			Block223 block223 = Block223Application.getBlock223();
			Game game = block223.findGame(name);
			if(game == null) {
				throw new InvalidInputException("A game with name " + name + " does not exist.");
			}
			else {
				if ((game.getAdmin()!=currentRole)) {
					throw new InvalidInputException("Only the admin who created the game can select the game.");
				}
				else
				{
					if(game.getPublished())
					{
						throw new InvalidInputException("A published game cannot be changed.");
					}
					else Block223Application.setCurrentGame(game);
				}
			}
		}
	}

	// feature 4
	public static void updateGame(String name, int nrLevels, int nrBlocksPerLevel, int minBallSpeedX, int minBallSpeedY,
		Double ballSpeedIncreaseFactor, int maxPaddleLength, int minPaddleLength) throws InvalidInputException {
		if(name == null || name.equals("")) throw new InvalidInputException("The name of a game must be specified.");
		UserRole currentRole = Block223Application.getCurrentUserRole();
		if (currentRole instanceof Admin) {
			Game game = Block223Application.getCurrentGame();
			if(game != null) {
				if (game.getAdmin().equals(currentRole)) {
					
					if(!game.getName().equals(name)) { //game name is not same as before
						if (Block223Application.getBlock223().findGame(name) != null)	{ //game name copys another existing game's name
							throw new InvalidInputException("The name of a game must be unique.");
						}
						else {
							game.setName(name);
							setGameDetails(nrLevels, nrBlocksPerLevel, minBallSpeedX, minBallSpeedY, ballSpeedIncreaseFactor, maxPaddleLength, minPaddleLength);
						}
					}
					setGameDetails(nrLevels, nrBlocksPerLevel, minBallSpeedX, minBallSpeedY, ballSpeedIncreaseFactor, maxPaddleLength, minPaddleLength);
					
//					else
//					{
//						if (!game.setName(name))	{ //game name does not copy another existing game's name
//							throw new InvalidInputException("The name of a game must be unique.");
//						}
//						else
//						{
//							setGameDetails(nrLevels, nrBlocksPerLevel, minBallSpeedX, minBallSpeedY, ballSpeedIncreaseFactor, maxPaddleLength, minPaddleLength);
//						}
//					}
				}
				else {
					throw new InvalidInputException("Only the admin who created the game can define its game settings.");
				}
			}
			else {
				throw new InvalidInputException("A game must be selected to define game settings.");
			}
		}
		else {
			throw new InvalidInputException("Admin privileges are required to define game settings.");
		}

	}

	// feature 5 add a block to the game
	public static void addBlock(int red, int green, int blue, int points) throws InvalidInputException {
		UserRole currentRole = Block223Application.getCurrentUserRole();

		if(!(currentRole instanceof Admin)) {
			String error = "Admin privileges are required to add a block.";
			throw new InvalidInputException(error);
		}
		Game game = Block223Application.getCurrentGame();
		if(game==null) {
			String error = "A game must be selected to add a block.";
			throw new InvalidInputException(error);
		}
		if(!game.getAdmin().equals(currentRole)) {
			String error = "Only the admin who created the game can add a block.";
			throw new InvalidInputException(error);
		}
		List<Block> blocks = game.getBlocks();
		for(Block block : blocks) {
			if(block.getRed()==red && block.getGreen()==green && block.getBlue()==blue) {
				String error = "A block with the same color already exists for the game.";
				throw new InvalidInputException(error);
			}
		}

		try{
			Block newBlock = new Block(red, green, blue, points, game);
		} 
		catch (RuntimeException e) {
			String error = e.getMessage();
			throw new InvalidInputException(error);
		}
	}

	// feature 6 delete a block from a game
	public static void deleteBlock(int id) throws InvalidInputException {
		UserRole currentRole = Block223Application.getCurrentUserRole();

		if(!(currentRole instanceof Admin)) {
			String error = "Admin privileges are required to delete a block.";
			throw new InvalidInputException(error);
		}
		Game game = Block223Application.getCurrentGame();
		if(game==null) {
			String error = "A game must be selected to delete a block.";
			throw new InvalidInputException(error);
		}
		if(game.getAdmin()!=currentRole) {
			String error = "Only the admin who created the game can delete a block.";
			throw new InvalidInputException(error);
		}
		Block toDelete = game.findBlock(id);
		if(toDelete!=null) {
			toDelete.delete();
		}
	}

	// feature 7
	public static void updateBlock(int id, int red, int green, int blue, int points) throws InvalidInputException {

		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to update a block.");
		}
		Game game = Block223Application.getCurrentGame();
		if (game == null) {
			throw new InvalidInputException("A game must be selected to update a block.");
		}
		if (!(Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin()))) {
			throw new InvalidInputException("Only the admin who created the game can update a block.");
		}

		if (points < Block.MIN_POINTS || points > Block.MAX_POINTS) {
			throw new InvalidInputException("Points must be between 1 and 1000.");
		}

		List<Block> blocks = game.getBlocks();
		for (Block aBlock : blocks) {
			if (aBlock.getRed() == red && aBlock.getGreen() == green && aBlock.getBlue() == blue) {
				throw new InvalidInputException("A block with the same color already exists for the game.");
			}
		} 

		Block block = game.findBlock(id);
		if (block == null) {
			throw new InvalidInputException("The block does not exist.");
		}

		try {
			block.setRed(red);
			block.setGreen(green);
			block.setBlue(blue);
			block.setPoints(points);
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	// feature 8
	public static void positionBlock(int id, int levelIndex, int gridHorizontalPosition, int gridVerticalPosition)
			throws InvalidInputException {

		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to position a block.");
		}
		Game game = Block223Application.getCurrentGame();
		if (game == null) {
			throw new InvalidInputException("A game must be selected to position a block.");
		}
		if (!(Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin()))) {
			throw new InvalidInputException("Only the admin who created the game can position a block.");
		}

		Level level;
		try {
			level = game.getLevel(levelIndex - 1);
		}
		catch(IndexOutOfBoundsException e) {
			throw new InvalidInputException(e.getMessage());
		}

		if (level.numberOfBlockAssignments() >= game.getNrBlocksPerLevel()) {
			throw new InvalidInputException("The number of blocks has reached the maximum number (" + game.getNrBlocksPerLevel() + ") allowed for this game.");
		}

		if (level.findBlockAssignment(gridHorizontalPosition, gridVerticalPosition) != null) {
			throw new InvalidInputException("A block already exists at location " + gridHorizontalPosition + "/" + gridVerticalPosition + ".");	
		}

		Block block = game.findBlock(id);
		if (block == null) {
			throw new InvalidInputException("The block does not exist.");
		}

		try {
			BlockAssignment blockAssignment = new BlockAssignment(gridHorizontalPosition, gridVerticalPosition, level, block, game);
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	// feature 9
	public static void moveBlock(int indexLevel, int oldGridHorizontalPosition, int oldGridVerticalPosition,
			int newGridHorizontalPosition, int newGridVerticalPosition) throws InvalidInputException {
		Level level = null;
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to move a block.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to move a block.");
		}
		if (!(Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin()))) {
			throw new InvalidInputException("Only the admin who created the game can move a block.");
		}
		Game game = Block223Application.getCurrentGame();
		try {
			level = game.getLevel(indexLevel - 1);
		}
		catch(IndexOutOfBoundsException e) {
			throw new InvalidInputException(e.getMessage());
		}	

		if (level.findBlockAssignment(newGridHorizontalPosition, newGridVerticalPosition) != null) { // method findBlockAssignment under "added methods" in level class
			throw new InvalidInputException("A block already exists at location " + newGridHorizontalPosition + "/" + newGridVerticalPosition + ".");
		}
		BlockAssignment assignment = level.findBlockAssignment(oldGridHorizontalPosition, oldGridVerticalPosition); 
		if (assignment == null) {
			throw new InvalidInputException("A block does not exist at location " + oldGridHorizontalPosition + "/" + oldGridVerticalPosition + ".");
		}
		try {
			assignment.setGridHorizontalPosition(newGridHorizontalPosition); 			
		}
		catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		try {
			assignment.setGridVerticalPosition(newGridVerticalPosition); 
		}
		catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	// feature 10
	public static void removeBlock(int indexLevel, int gridHorizontalPosition, int gridVerticalPosition)
			throws InvalidInputException {		
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to remove a block.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to remove a block.");
		}
		if (!(Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin()))) {
			throw new InvalidInputException("Only the admin who created the game can remove a block.");
		}	
		Game game = Block223Application.getCurrentGame();
		Level level = game.getLevel(indexLevel - 1);
		BlockAssignment assignment = level.findBlockAssignment(gridHorizontalPosition, gridVerticalPosition); // METHOD ADDED INSIDE THE LEVEL CLASS (UNDER "ADDED METHODS")
		if (assignment != null) {
			assignment.delete();
		}		

	}

	// feature 11
	public static void saveGame() throws InvalidInputException {
		UserRole currentUserRole = Block223Application.getCurrentUserRole();

		Game currentGame = Block223Application.getCurrentGame();
		if (currentGame == null) {
			throw new InvalidInputException("A game must be selected to save it.");
		}

		if(!(currentUserRole instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to save a game.");
		}
		/*
		 * 
		else {
			List<Game> games = ((Admin) currentUserRole).getGames();
			for (Game game: games) {
				if (!(Block223Application.getCurrentUserRole().equals(game.getAdmin()))) {
					throw new InvalidInputException("Only the admin who created the game can save it.");
				}
			}
		}
		*/
		
		if(!(Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin()))) {
			throw new InvalidInputException("Only the admin who created the game can save it.");
		}
		
		Block223 block223 = Block223Application.getBlock223();

		try {
			Block223Persistence.save(block223);}
		catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	// feature 12
	public static void register(String username, String playerPassword, String adminPassword)
			throws InvalidInputException {
		if (Block223Application.getCurrentUserRole() != null) {
			throw new InvalidInputException("Cannot register a new user while a user is logged in.");
		}
		if (playerPassword == null) {
			throw new InvalidInputException("The player password needs to be specified.");
		}
		// Not null, but contains nothing
		if (playerPassword.equals("")) {
			throw new InvalidInputException("The player password needs to be specified.");
		}
		if (playerPassword.equals(adminPassword)) {
			throw new InvalidInputException("The passwords have to be different.");
		}
		Block223 block223 = Block223Application.getBlock223();
		Player player = null;
		Admin admin = null;
		try {
			player = new Player(playerPassword, block223);
			User user = new User(username, block223, player);
			if (adminPassword != null && !adminPassword.equals("")) {
				admin = new Admin(adminPassword, block223);
				user.addRole(admin);
			}
			Block223Persistence.save(block223);
		} catch (RuntimeException e) {
			if (player != null)
				player.delete();
			if (admin != null) 
				admin.delete();
			if (e.getMessage().equals("Cannot create due to duplicate username")) {
				throw new InvalidInputException("The username has already been taken.");
			}
			throw new InvalidInputException(e.getMessage());
		}

	}

	// feature 12
	public static void login(String username, String password) throws InvalidInputException {
		Block223 block223 = Block223Application.resetBlock223();
		User user = User.getWithUsername(username);
		if (user == null) {
			throw new InvalidInputException("The username and password do not match.");	
		}
		List<UserRole> roles = user.getRoles();

		if (Block223Application.getCurrentUserRole() != null) {
			throw new InvalidInputException("Cannot login a user while a user is already logged in.");
		}	
		if (username == null) {
			throw new InvalidInputException("The username and password do not match.");

		}
		for (UserRole role : roles) {
			String rolePassword = role.getPassword();
			if (rolePassword.equals(password)) {
				Block223Application.setCurrentUserRole(role);
				return;
			}
		}
		throw new InvalidInputException("The username and password do not match.");	
	}

	// feature 12
	public static void logout() {
		Block223Application.setCurrentUserRole(null);

	}

	// play mode

	public static void selectPlayableGame(String name, int id) throws InvalidInputException  {	
		// validation checks
		if (!(Block223Application.getCurrentUserRole() instanceof Player)) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}

		if (Block223Application.getBlock223().findGame(name) == null && Block223Application.getBlock223().findPlayableGame(id) == null) {
			throw new InvalidInputException("The game does not exist.");
		}

		if (Block223Application.getBlock223().findGame(name) == null && 
				!(Block223Application.getCurrentUserRole().equals(Block223Application.getBlock223().findPlayableGame(id).getPlayer()))	) {
			throw new InvalidInputException("Only the player that started a game can continue the game.");
		}
		PlayedGame pgame;
		Block223 block223 = Block223Application.getBlock223();
		Game game = block223.findGame(name);
		if (game != null) {
			UserRole player = Block223Application.getCurrentUserRole();
			String username = User.findUsername((Player) player);
			pgame = new PlayedGame(username, game, block223);
			pgame.setPlayer((Player) player);
		} else {
			pgame = block223.findPlayableGame(id);
			if (pgame == null ) {
				throw new InvalidInputException("The game does not exist.");
			}
		}
		Block223Application.setCurrentPlayableGame(pgame);
	}
	
	public static void updatePaddlePosition(String userInputs) {
		PlayedGame game = Block223Application.getCurrentPlayableGame();
		for (int i = 0; i < userInputs.length(); i++) {
			char c = userInputs.charAt(i);
			
			if (c == 'l') {
				if (game.getCurrentPaddleX() + PlayedGame.PADDLE_MOVE_LEFT >= 0 ) {
					game.setCurrentPaddleX(PlayedGame.PADDLE_MOVE_LEFT + game.getCurrentPaddleX());
				}
			}
			else if (c == 'r') {
				if (game.getCurrentPaddleX() + game.getCurrentPaddleLength() + PlayedGame.PADDLE_MOVE_RIGHT <= 390) {
					game.setCurrentPaddleX(PlayedGame.PADDLE_MOVE_RIGHT + game.getCurrentPaddleX());	
				}
			}
			else if (c == ' ') {
				//ignore the rest of inputs
				return;
			}
			else {
				// error, unknown key pressed
				return;
			}
		}
		
	}
	
	public static void startGame(Block223PlayModeInterface ui) throws InvalidInputException {
		
		// validation checks
		if (Block223Application.getCurrentUserRole() == null ) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}
		if (Block223Application.getCurrentPlayableGame() == null ) {
			throw new InvalidInputException("A game must be selected to play it.");
		}
		if ((Block223Application.getCurrentUserRole() instanceof Admin) && Block223Application.getCurrentPlayableGame().getPlayer() != null) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}
		if ((Block223Application.getCurrentUserRole() instanceof Admin) && !(Block223Application.getCurrentUserRole().equals
			(Block223Application.getCurrentPlayableGame().getGame().getAdmin()))) {
			throw new InvalidInputException("Only the admin of a game can test the game.");
		}
		if ((Block223Application.getCurrentUserRole() instanceof Player) && Block223Application.getCurrentPlayableGame().getPlayer() == null) {
			throw new InvalidInputException("Admin privileges are required to test a game.");
		}	
		
		PlayedGame game = Block223Application.getCurrentPlayableGame();
		game.play();
		ui.takeInputs();

		while (game.getPlayStatus() == PlayStatus.Moving) {
			String userInputs = ui.takeInputs();
			updatePaddlePosition(userInputs);
			game.move();

			if (userInputs.contains(" ")) {
				game.pause();
			}
			// call clearInputs here???, maybe inside refresh() we clear input?
			try {
				Thread.sleep((long) game.getWaitTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (game.getPlayStatus() != PlayStatus.GameOver ) {
				ui.refresh();
			}
			
		}

		if (game.getPlayStatus() == PlayStatus.GameOver ) {
			Block223Application.setCurrentPlayableGame(null); 
			Block223 block223 = Block223Application.getBlock223();
			Block223Persistence.save(block223);
		}
		else if (game.getPlayer() != null) {
			game.setBounce(null);
			Block223 block223 = Block223Application.getBlock223();
			Block223Persistence.save(block223);
		}

	}

	public static void testGame(Block223PlayModeInterface ui) throws InvalidInputException {
		Game game = Block223Application.getCurrentGame();
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) 
		{
			throw new InvalidInputException("Admin privileges are required to test a game.");
		}
		if(game == null)
		{
			throw new InvalidInputException("A game must be selected to test it.");
		}
		if (!((Block223Application.getCurrentUserRole()).equals(game.getAdmin()))) {
			throw new InvalidInputException("Only the admin who created the game can test it.");
		}
		if (Block223Application.getCurrentGame().getBlocks().size() < 1) {
			throw new InvalidInputException("At least one block must be defined for a game to be tested.");
		}
		String username = User.findUsername(Block223Application.getCurrentUserRole());
		Block223 block223 = Block223Application.getBlock223();
		PlayedGame pgame = new PlayedGame(username, game, block223);
		pgame.setPlayer(null);
		Block223Application.setCurrentPlayableGame(pgame);
		startGame(ui);
	}

	public static void publishGame () throws InvalidInputException {
		if (Block223Application.getCurrentGame() == null ) {
			throw new InvalidInputException("A game must be selected to publish it.");
		}
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to publish a game.");
		}
		if (!(Block223Application.getCurrentUserRole() == Block223Application.getCurrentGame().getAdmin())) {
			throw new InvalidInputException("Only the admin who created the game can publish it.");
		}
		if (Block223Application.getCurrentGame().getBlocks().size() < 1) {
			throw new InvalidInputException("At least one block must be defined for a game to be published.");
		}	
		Game game = Block223Application.getCurrentGame();
		game.setPublished(true);
	}

	// ****************************
	// Query methods
	// ****************************

	// feature 3
	public static List<TOGame> getDesignableGames() throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		else {
			Admin gameAdmin;
			Block223 block223 = Block223Application.getBlock223();
			UserRole admin = Block223Application.getCurrentUserRole();
			List<TOGame> result = new ArrayList<TOGame>();
			List<Game> games = block223.getGames();
			for (Game game : games) {
				gameAdmin = game.getAdmin();
				if (gameAdmin.equals(admin) && !game.getPublished()) {
					TOGame to = new TOGame(game.getName(), game.getLevels().size(), game.getNrBlocksPerLevel(),
							game.getBall().getMinBallSpeedX(), game.getBall().getMinBallSpeedY(),
							game.getBall().getBallSpeedIncreaseFactor(), game.getPaddle().getMaxPaddleLength(),
							game.getPaddle().getMinPaddleLength());
					result.add(to);
				}
			}
			return result;
		}
	}

	// feature 4
	public static TOGame getCurrentDesignableGame() throws InvalidInputException {
		UserRole currentRole = Block223Application.getCurrentUserRole();
		if (currentRole instanceof Admin) {
			Game game = Block223Application.getCurrentGame();
			if(game != null) {
				if (game.getAdmin()==currentRole) {
					return new TOGame(game.getName(), game.getLevels().size(), game.getNrBlocksPerLevel(),
							game.getBall().getMinBallSpeedX(), game.getBall().getMinBallSpeedY(),
							game.getBall().getBallSpeedIncreaseFactor(), game.getPaddle().getMaxPaddleLength(),
							game.getPaddle().getMinPaddleLength());
				}
				else {
					throw new InvalidInputException("Only the admin who created the game can access its information.");
				}
			}
			else {
				throw new InvalidInputException("A game must be selected to access its information.");
			}
		}
		else {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
	}

	// feature 6
	public static List<TOBlock> getBlocksOfCurrentDesignableGame() throws InvalidInputException{
		UserRole currentRole = Block223Application.getCurrentUserRole();

		if(!(currentRole instanceof Admin)) {
			String error = "Admin privileges are required to access game information.";
			throw new InvalidInputException(error);
		}
		Game game = Block223Application.getCurrentGame();
		if(game==null) {
			String error = "A game must be selected to access its information.";
			throw new InvalidInputException(error);
		}
		if(!game.getAdmin().equals(currentRole)) {
			String error = "Only the admin who created the game can access its information.";
			throw new InvalidInputException(error);
		}

		ArrayList<TOBlock> result = new ArrayList<TOBlock>();
		List<Block> blocks = game.getBlocks();
		for(Block block : blocks) {
			TOBlock toBlock = new TOBlock(block.getId(), block.getRed(), block.getGreen(), block.getBlue(), block.getPoints());
			result.add(toBlock);
		}

		return result;
	}

	// feature 7
	public static TOBlock getBlockOfCurrentDesignableGame(int id) throws InvalidInputException {

		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		Game game = Block223Application.getCurrentGame();
		if (game == null) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}
		if (!(Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin()))) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}

		Block block = game.findBlock(id);
		if (block == null) {
			throw new InvalidInputException("The block does not exist.");
		}

		TOBlock to = new TOBlock(block.getId(), block.getRed(), block.getGreen(), block.getBlue(), block.getPoints());
		return to;
	}

	// feature 8
	public static List<TOGridCell> getBlocksAtLevelOfCurrentDesignableGame(int levelIndex) throws InvalidInputException {

		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		Game game = Block223Application.getCurrentGame();
		if (game == null) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}
		if (!(Block223Application.getCurrentUserRole().equals(Block223Application.getCurrentGame().getAdmin()))) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}

		ArrayList<TOGridCell> result = new ArrayList<TOGridCell>();

		Level level;
		try {
			level = game.getLevel(levelIndex - 1);
		}
		catch(IndexOutOfBoundsException e) {
			throw new InvalidInputException(e.getMessage());
		}

		List<BlockAssignment> assignments = level.getBlockAssignments();

		for (BlockAssignment assignment : assignments) {
			TOGridCell to = new TOGridCell(assignment.getGridHorizontalPosition(), assignment.getGridVerticalPosition(), assignment.getBlock().getId(), 
					assignment.getBlock().getRed(), assignment.getBlock().getGreen(), assignment.getBlock().getBlue(),
					assignment.getBlock().getPoints());
			result.add(to);
		}

		return result;
	}

	// feature 12
	public static TOUserMode getUserMode() {
		UserRole userRole = Block223Application.getCurrentUserRole();

		if (userRole == null) {
			TOUserMode to = new TOUserMode(Mode.None);
			return to;
		}

		if (userRole instanceof Player) {
			TOUserMode to = new TOUserMode(Mode.Play);
			return to;
		}

		if (userRole instanceof Admin) {
			TOUserMode to = new TOUserMode(Mode.Design);
			return to;
		}
		return null;

	}

	// play mode

	public static List<TOPlayableGame> getPlayableGames() throws InvalidInputException {

		// validation checks
		if (!((Block223Application.getCurrentUserRole()) instanceof Player)) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}

		Block223 block223 = Block223Application.getBlock223();
		UserRole player = Block223Application.getCurrentUserRole();
		List<TOPlayableGame> result = new ArrayList<TOPlayableGame>();
		List<Game> games = block223.getGames();

		for (Game game: games) {
			boolean published = game.isPublished();
			if (published) {
				TOPlayableGame to = new TOPlayableGame(game.getName(),-1,0);
				result.add(to);	
			}
		}
		// cast UserRole player to Player, since already checked and throw exception
		List<PlayedGame> pgames = ((Player) player).getPlayedGames();

		for (PlayedGame game : pgames) {
			TOPlayableGame to = new TOPlayableGame(game.getGame().getName(), game.getId(), game.getCurrentLevel());
			result.add(to);
		}
		return result;
	}

	public static TOCurrentlyPlayedGame getCurrentPlayableGame() throws InvalidInputException {

		// validation checks
		
		if (Block223Application.getCurrentUserRole() == null) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}		
		if (Block223Application.getCurrentPlayableGame() == null) {
			throw new InvalidInputException("A game must be selected to play it.");
		}
		if ((Block223Application.getCurrentUserRole() instanceof Admin) && Block223Application.getCurrentPlayableGame().getPlayer() != null) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}	
		if((Block223Application.getCurrentUserRole() instanceof Admin) && !Block223Application.getCurrentPlayableGame().getGame().getAdmin().equals(Block223Application.getCurrentUserRole())) {
			throw new InvalidInputException("Only the admin of a game can test the game.");
		}
		if((Block223Application.getCurrentUserRole() instanceof Player) && Block223Application.getCurrentPlayableGame().getPlayer() == null) {
			throw new InvalidInputException("Admin privileges are required to test a game.");
		}
				
		PlayedGame pgame = Block223Application.getCurrentPlayableGame();
		boolean paused = pgame.getPlayStatus() == PlayStatus.Ready || pgame.getPlayStatus() == PlayStatus.Paused;

		TOCurrentlyPlayedGame result = new TOCurrentlyPlayedGame(pgame.getGame().getName(), paused, pgame.getScore(), pgame.getLives(), 
				pgame.getCurrentLevel(), pgame.getPlayername(), pgame.getCurrentBallX(), pgame.getCurrentBallY(), 
				pgame.getCurrentPaddleLength(), pgame.getCurrentPaddleX());
		List<PlayedBlockAssignment> blocks = pgame.getBlocks();

		for (PlayedBlockAssignment pblock: blocks) {
			TOCurrentBlock to = new TOCurrentBlock(pblock.getBlock().getRed(), pblock.getBlock().getGreen(), pblock.getBlock().getBlue(), 
					pblock.getBlock().getPoints(), pblock.getX(), pblock.getY(), result); 
		}
		return result;
	}

	public static TOHallOfFame getHallOfFame(int start, int end) throws InvalidInputException {   
		
		if (!((Block223Application.getCurrentUserRole()) instanceof Player)) {
			throw new InvalidInputException("Player privileges are required to access a game's hall of fame.");
		}
		PlayedGame pgame = Block223Application.getCurrentPlayableGame();
		if (pgame == null) {
			throw new InvalidInputException("A game must be selected to view its hall of fame.");
		}
		Game game = pgame.getGame();
		TOHallOfFame result = new TOHallOfFame(game.getName());
		if (start < 1) {
			start = 1;
		}
		if (end > game.numberOfHallOfFameEntries()) {
			end = game.numberOfHallOfFameEntries();
		}
		start = game.numberOfHallOfFameEntries() - start;
		end = game.numberOfHallOfFameEntries() - end ;
		for (int i = start ; i >= end ; i--) {
			TOHallOfFameEntry to = new TOHallOfFameEntry(i + 1,game.getHallOfFameEntry(i).getPlayername(),game.getHallOfFameEntry(i).getScore(),result);
		}
		return result;
	}

	public static TOHallOfFame getHallOfFameGO(int start, int end, String game) throws InvalidInputException {   
		if (!((Block223Application.getCurrentUserRole()) instanceof Player)) {
			throw new InvalidInputException("Player privileges are required to access a game's hall of fame.");
		}
		Game foundgame = Block223Application.getBlock223().findGame(game);
		if (foundgame == null) { return null; }
		TOHallOfFame result = new TOHallOfFame(game);
		if (start < 1) {
			start = 1;
		}
		if (end > foundgame.numberOfHallOfFameEntries()) {
			end = foundgame.numberOfHallOfFameEntries();
		}
		start = foundgame.numberOfHallOfFameEntries() - start;
		end = foundgame.numberOfHallOfFameEntries() - end ;
		for (int i = start ; i >= end ; i--) {
			TOHallOfFameEntry to = new TOHallOfFameEntry(i + 1,foundgame.getHallOfFameEntry(i).getPlayername(),foundgame.getHallOfFameEntry(i).getScore(),result);
		}
		return result;
	}
	
	public static TOHallOfFame getHallOfFameWithMostRecentEntry(int numberOfEntries) throws InvalidInputException {
		
		
	if (!((Block223Application.getCurrentUserRole()) instanceof Player)) {
		throw new InvalidInputException("Player privileges are required to access a game's hall of fame.");
	}
	PlayedGame pgame = Block223Application.getCurrentPlayableGame();
	if (pgame == null) {
		throw new InvalidInputException("A game must be selected to view its hall of fame.");
	}
	
	 Game game = pgame.getGame();
	 TOHallOfFame result = new TOHallOfFame(game.getName());
	 HallOfFameEntry mostRecent = game.getMostRecentEntry();
	 int indexR = game.indexOfHallOfFameEntry(mostRecent);
	 
	 int start = indexR + numberOfEntries/2;
	 if (start > (game.numberOfHallOfFameEntries() - 1)) {
		 start = game.numberOfHallOfFameEntries() - 1;
	 }
	 int end = start - numberOfEntries + 1;
	 if (end < 0) {
		 end = 0;
	 }
	 
	 for (int i = start; i >= end; i--) {
		 TOHallOfFameEntry to = new TOHallOfFameEntry(i + 1,game.getHallOfFameEntry(i).getPlayername(),game.getHallOfFameEntry(i).getScore(), result);
	 }
	 return result;
	}
	
	// Query methods for constants
	
	public static int getBlockSize() {
		return Block.SIZE;
	}
	
	public static int getPaddleWidth() {
		return Paddle.PADDLE_WIDTH;
	}
	
	public static int getBallDiameter() {
		return Ball.BALL_DIAMETER;
	}
	
	public static double getBallSpeedX() {
		return Block223Application.getCurrentPlayableGame().getBallDirectionX();
	}
	
	public static double getBallSpeedY() {
		return Block223Application.getCurrentPlayableGame().getBallDirectionY();
	}
	public static void resetBallSpeed() {
		Block223Application.getCurrentPlayableGame().resetBallDirectionX();
		Block223Application.getCurrentPlayableGame().resetBallDirectionY();
	}
	public static void outOfPlayAreaX390() {
		Block223Application.getCurrentPlayableGame().setCurrentBallX(Game.PLAY_AREA_SIDE - Ball.BALL_DIAMETER);
	}
	public static void outOfPlayAreaX0() {
		Block223Application.getCurrentPlayableGame().setCurrentBallX(Ball.BALL_DIAMETER);
	}
	public static void outOfPlayAreaY390() {
		Block223Application.getCurrentPlayableGame().setCurrentBallY(Game.PLAY_AREA_SIDE - Ball.BALL_DIAMETER);
	}
	public static void outOfPlayAreaY0() {
		Block223Application.getCurrentPlayableGame().setCurrentBallY(Ball.BALL_DIAMETER);
	}
	public static void stopMusic() {
		Block223Application.stopMusic();
	}
	
	public static void playMusic() {
		Block223Application.playMusic("./lib/bgmusic.wav");
	}
}

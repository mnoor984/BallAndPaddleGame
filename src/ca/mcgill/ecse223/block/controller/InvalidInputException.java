package ca.mcgill.ecse223.block.controller;

public class InvalidInputException extends Exception{
	private static final long serialVersionUID = 2718533882352487864L;

	public InvalidInputException(String errorMessage) {
		super(errorMessage);
	}
	
}

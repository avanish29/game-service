package com.backbase.assignment.kalah.game.exception;

/**
 * Exception thrown if pit number is invalid.
 * 
 * @author @vanish
 */
public class IllegalPitNumberException extends RuntimeException {
	private static final long serialVersionUID = -5920955480162734541L;

	public IllegalPitNumberException(String message) {
		super(message);
	}
}

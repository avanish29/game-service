package com.backbase.assignment.kalah.game.exception;

/**
 * Exception thrown if is finished.
 * 
 * @author @vanish
 */
public class GameTerminatedException extends RuntimeException {
	private static final long serialVersionUID = -8281857960790564992L;

	public GameTerminatedException(String message) {
		super(message);
	}
}

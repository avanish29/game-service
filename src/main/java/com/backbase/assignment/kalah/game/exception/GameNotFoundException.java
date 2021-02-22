package com.backbase.assignment.kalah.game.exception;

/**
 * Exception thrown if game not found in database.
 * 
 * @author @vanish
 */
public class GameNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 658691970404864653L;

	public GameNotFoundException(final String message) {
		super(message);
	}
}

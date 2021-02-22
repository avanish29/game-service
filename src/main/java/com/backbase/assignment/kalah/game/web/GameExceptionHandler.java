package com.backbase.assignment.kalah.game.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.backbase.assignment.kalah.game.domain.GameErrorView;
import com.backbase.assignment.kalah.game.exception.GameNotFoundException;
import com.backbase.assignment.kalah.game.exception.GameTerminatedException;
import com.backbase.assignment.kalah.game.exception.IllegalPitNumberException;

/**
 * An advice handler for exceptions.
 * 
 * @author @vanish
 */
@RestControllerAdvice
public class GameExceptionHandler {
	@ExceptionHandler(GameNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public GameErrorView handleNotFound(Exception ex) {
		return new GameErrorView(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
	}

	@ExceptionHandler(IllegalPitNumberException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public GameErrorView handleBadRequest(Exception ex) {
		return new GameErrorView(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
	}

	@ExceptionHandler(GameTerminatedException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public GameErrorView handleConflict(GameTerminatedException ex) {
		return new GameErrorView(HttpStatus.CONFLICT, ex.getMessage(), ex);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public GameErrorView handleGericError(Exception ex) {
		return new GameErrorView(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
	}
}

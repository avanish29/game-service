package com.backbase.assignment.kalah.game.domain;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * A game error view.
 * 
 * @author @vanish
 */
@Data
public class GameErrorView {
	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
	private LocalDateTime timestamp;
	private String message;
	private String debugMessage;

	public GameErrorView() {
		timestamp = LocalDateTime.now();
	}

	public GameErrorView(final HttpStatus status) {
		this();
		this.status = status;
	}

	public GameErrorView(final HttpStatus status, final Throwable ex) {
		this();
		this.status = status;
		this.message = "Unexpected error";
		if (ex != null)
			this.debugMessage = ex.getLocalizedMessage();
	}

	public GameErrorView(final HttpStatus status, final String message, final Throwable ex) {
		this();
		this.status = status;
		this.message = message;
		if (ex != null)
			this.debugMessage = ex.getLocalizedMessage();
	}

	public Integer getStatusCode() {
		return status != null ? status.value() : null;
	}
}

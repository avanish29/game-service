package com.backbase.assignment.kalah.game.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A game board view.
 * 
 * @author @vanish
 */
@Data
@AllArgsConstructor
public class GameBoardView {
	@JsonProperty("id")
	private Long gameId;

	private String uri;
}

package com.backbase.assignment.kalah.game.domain;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A game view.
 * 
 * @author @vanish
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameView {
	private Long id;
	private String uri;
	private Map<Integer, Integer> status;
}

package com.backbase.assignment.kalah.game.util;

/**
 * Utility class store all constants.
 * 
 * @author @vanish
 */
public class GameConstant {
	public static final int NUMBER_OF_PITS = 6;
	public static final int NUMBER_OF_STONES_PER_PIT = 6;
	public static final int NUMBER_OF_PLAYERS = 2;
	public static final int NUMBER_OF_TOTAL_PITS = (NUMBER_OF_STONES_PER_PIT * NUMBER_OF_PLAYERS) + NUMBER_OF_PLAYERS;

	public static final String GAME_API_PATH = "/games";
	public static final String GAME_BY_ID_API_PATH = "/{gameId}";
	public static final String MOVE_PIT_BY_GAME_ID_API_PATH = "/{gameId}/pits/{pitId}";

	private GameConstant() {
	}
}

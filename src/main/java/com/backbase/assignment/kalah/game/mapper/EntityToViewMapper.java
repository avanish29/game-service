package com.backbase.assignment.kalah.game.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backbase.assignment.kalah.game.domain.GameBoardView;
import com.backbase.assignment.kalah.game.domain.GameView;
import com.backbase.assignment.kalah.game.entity.Game;
import com.backbase.assignment.kalah.game.entity.Pit;
import com.backbase.assignment.kalah.game.entity.Player;
import com.backbase.assignment.kalah.game.util.GameConstant;

import lombok.NonNull;

/**
 * Mapper used to convert entity to view.
 * 
 * @author @vanish
 *
 */
@Component
public class EntityToViewMapper {
	/**
	 * Create new instance of {@link GameView} using {@link Game} entity.
	 * 
	 * @param gameEntity - gameEntity must not be {@literal null}
	 * @return - instance of {@link GameView}
	 */
	public GameView toGameView(@NonNull final Game gameEntity) {
		Map<Integer, Integer> status = new LinkedHashMap<>();
		int pitId = 1;
		for (Player player : gameEntity.getPlayers()) {
			for (Pit pit : player.getPits()) {
				status.put(pitId++, pit.getPitValue());
			}
			status.put(pitId++, player.getScore());
		}
		String url = generateGameUrl(gameEntity.getGameId());
		return new GameView(gameEntity.getGameId(), url, status);
	}

	/**
	 * Create new instance of {@link GameBoardView} using {@link Game} entity.
	 * 
	 * @param gameEntity - gameEntity must not be {@literal null}
	 * @return - instance of {@link GameBoardView}
	 */
	public GameBoardView toGameBoard(@NonNull final Game gameEntity) {
		return new GameBoardView(gameEntity.getGameId(), generateGameUrl(gameEntity.getGameId()));
	}

	/**
	 * Create URL to link game by ID.
	 * 
	 * @param gameId - gameId must not be {@literal null}
	 * @return - URL to link game by ID.
	 */
	public String generateGameUrl(@NonNull Long gameId) {
		StringBuilder uriBuilder = new StringBuilder(
				ServletUriComponentsBuilder.fromCurrentServletMapping().build().toUriString());
		uriBuilder.append(GameConstant.GAME_API_PATH).append("/").append(gameId);
		return uriBuilder.toString();
	}
}

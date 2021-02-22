package com.backbase.assignment.kalah.game.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backbase.assignment.kalah.game.domain.GameBoardView;
import com.backbase.assignment.kalah.game.domain.GameView;
import com.backbase.assignment.kalah.game.entity.Game;

@ExtendWith(MockitoExtension.class)
class EntityToViewMapperTest {
	@Spy
	private EntityToViewMapper entityToViewMapper;

	@Test
	void testToGameView() {
		Mockito.doReturn("http://localhost:8080/games/1").when(entityToViewMapper).generateGameUrl(Long.valueOf(1));

		Game gameEntity = new Game();
		gameEntity.setGameId(Long.valueOf(1));

		GameView gameView = entityToViewMapper.toGameView(gameEntity);
		Assertions.assertNotNull(gameView);
		Assertions.assertEquals(gameEntity.getGameId(), gameView.getId());
		Assertions.assertEquals("http://localhost:8080/games/1", gameView.getUri());
	}

	@Test
	void testToGameBoard() {
		Mockito.doReturn("http://localhost:8080/games/1").when(entityToViewMapper).generateGameUrl(Long.valueOf(1));

		Game gameEntity = new Game();
		gameEntity.setGameId(Long.valueOf(1));

		GameBoardView gameBoardView = entityToViewMapper.toGameBoard(gameEntity);
		Assertions.assertNotNull(gameBoardView);
		Assertions.assertEquals(gameEntity.getGameId(), gameBoardView.getGameId());
		Assertions.assertEquals("http://localhost:8080/games/1", gameBoardView.getUri());
	}

	@Test
	void testToGameViewWhenParamIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> entityToViewMapper.toGameView(null));
	}

	@Test
	void testToGameBoardWhenParamIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> entityToViewMapper.toGameBoard(null));
	}
}

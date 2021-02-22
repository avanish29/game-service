package com.backbase.assignment.kalah.game.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backbase.assignment.kalah.game.domain.GameBoardView;
import com.backbase.assignment.kalah.game.domain.GameView;
import com.backbase.assignment.kalah.game.entity.Game;
import com.backbase.assignment.kalah.game.entity.Player;
import com.backbase.assignment.kalah.game.exception.GameNotFoundException;
import com.backbase.assignment.kalah.game.exception.GameTerminatedException;
import com.backbase.assignment.kalah.game.exception.IllegalPitNumberException;
import com.backbase.assignment.kalah.game.mapper.EntityToViewMapper;
import com.backbase.assignment.kalah.game.repository.GameRepository;
import com.backbase.assignment.kalah.game.repository.PlayerRepository;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
	@InjectMocks
	private GameService gameService;

	@Mock
	private GameRepository gameRepository;

	@Mock
	private PlayerRepository playerRepository;

	@Mock
	private EntityToViewMapper mapper;

	@Test
	void testCreateGame() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));

		when(gameRepository.save(ArgumentMatchers.any(Game.class))).thenReturn(created);
		when(playerRepository.saveAll(Mockito.<Player>anyList())).thenReturn(created.getPlayers());
		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));
		when(mapper.toGameBoard(ArgumentMatchers.any(Game.class))).thenCallRealMethod();
		when(mapper.generateGameUrl(Long.valueOf(1))).thenReturn("http://google.com/");

		GameBoardView newGame = gameService.createGame();
		Assertions.assertNotNull(newGame);
		assertEquals(1, newGame.getGameId());
	}

	@Test
	void testFindById() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));
		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));
		mockMapper();

		GameView gameView = gameService.findById(Long.valueOf(1));
		Assertions.assertNotNull(gameView);
		assertEquals(1, gameView.getId());
	}

	@Test
	void testFindByUnkownId() {
		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.empty());
		Assertions.assertThrows(GameNotFoundException.class, () -> gameService.findById(Long.valueOf(1)));
	}

	@Test
	void testMakeMove() {
		mockRepository();
		mockMapper();

		GameView gameView = gameService.makeMove(Long.valueOf(1), 4);
		Assertions.assertNotNull(gameView);
		assertEquals(1, gameView.getId());
	}

	@Test
	void testMakeMoveWithInvalidPit() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));

		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));

		Assertions.assertThrows(IllegalPitNumberException.class, () -> gameService.makeMove(Long.valueOf(1), 0));
	}

	@Test
	void testMakeMoveWhenPitNumberIsGreater() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));

		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));

		Assertions.assertThrows(IllegalPitNumberException.class, () -> gameService.makeMove(Long.valueOf(1), 15));
	}

	@Test
	void testMakeMoveWhenPlayerIndexIsWrong() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));
		created.setNextPlayer(Integer.valueOf(3));

		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));

		Assertions.assertThrows(IllegalPitNumberException.class, () -> gameService.makeMove(Long.valueOf(1), 4));
	}

	@Test
	void testMakeMoveWhenPitIsKalahForPlayerOne() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));

		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));

		Assertions.assertThrows(IllegalPitNumberException.class, () -> gameService.makeMove(Long.valueOf(1), 7));
	}

	@Test
	void testMakeMoveWhenPitIsKalahForPlayerTwo() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));

		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));

		Assertions.assertThrows(IllegalPitNumberException.class, () -> gameService.makeMove(Long.valueOf(1), 14));
	}

	@Test
	void testMakeMoveWhenGameIsFinishedWithPlayerOneWin() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));
		created.setWinner(Integer.valueOf(0));

		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));

		Assertions.assertThrows(GameTerminatedException.class, () -> gameService.makeMove(Long.valueOf(1), 4));
	}

	@Test
	void testMakeMoveWhenGameIsFinishedWithPlayerTwoWin() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));
		created.setWinner(Integer.valueOf(1));

		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));

		Assertions.assertThrows(GameTerminatedException.class, () -> gameService.makeMove(Long.valueOf(1), 4));
	}

	@Test
	void testMakeMoveWhenGameIsFinishedWithTie() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));
		created.setWinner(Integer.valueOf(2));

		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));

		Assertions.assertThrows(GameTerminatedException.class, () -> gameService.makeMove(Long.valueOf(1), 4));
	}

	@Test
	void testMakeMoveWhenInvalidPlayerOnePlay() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));

		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));

		Assertions.assertThrows(IllegalPitNumberException.class, () -> gameService.makeMove(Long.valueOf(1), 10));
	}

	@Test
	void testMakeMoveWhenInvalidPlayerTwoPlay() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));
		created.setNextPlayer(Integer.valueOf(1));

		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));

		Assertions.assertThrows(IllegalPitNumberException.class, () -> gameService.makeMove(Long.valueOf(1), 4));
	}

	@Test
	void testMakeMoveWhenPitHasZeroStones() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));
		created.getPlayers().get(0).getPits().get(3).setPitValue(0);

		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));

		Assertions.assertThrows(IllegalPitNumberException.class, () -> gameService.makeMove(Long.valueOf(1), 4));
	}

	@Test
	void testMakeMoveWithPlayerOneWin() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));
		created.getPlayers().get(0).setScore(50);
		created.getPlayers().get(0).getPits().forEach(pit -> pit.setPitValue(0));
		created.getPlayers().get(0).getPits().get(5).setPitValue(1);

		when(gameRepository.save(ArgumentMatchers.any(Game.class))).thenReturn(created);
		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));
		mockMapper();

		GameView gameView = gameService.makeMove(Long.valueOf(1), 6);
		Assertions.assertNotNull(gameView);
		assertEquals(1, gameView.getId());
	}

	@Test
	void testMakeMoveWithPlayerTwoWin() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));
		created.setNextPlayer(Integer.valueOf(1));
		created.getPlayers().get(1).setScore(50);
		created.getPlayers().get(1).getPits().forEach(pit -> pit.setPitValue(0));
		created.getPlayers().get(1).getPits().get(5).setPitValue(1);

		when(gameRepository.save(ArgumentMatchers.any(Game.class))).thenReturn(created);
		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));
		mockMapper();

		GameView gameView = gameService.makeMove(Long.valueOf(1), 13);
		Assertions.assertNotNull(gameView);
		assertEquals(1, gameView.getId());
	}

	void mockRepository() {
		Game created = new Game();
		created.setGameId(Long.valueOf(1));

		when(gameRepository.save(ArgumentMatchers.any(Game.class))).thenReturn(created);
		when(gameRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(created));
	}

	void mockMapper() {
		when(mapper.toGameView(ArgumentMatchers.any(Game.class))).thenCallRealMethod();
		when(mapper.generateGameUrl(Long.valueOf(1))).thenReturn("http://google.com/");
	}
}

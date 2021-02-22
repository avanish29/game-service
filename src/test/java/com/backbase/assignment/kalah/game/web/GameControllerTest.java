package com.backbase.assignment.kalah.game.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.backbase.assignment.kalah.game.domain.GameBoardView;
import com.backbase.assignment.kalah.game.domain.GameView;
import com.backbase.assignment.kalah.game.exception.GameNotFoundException;
import com.backbase.assignment.kalah.game.exception.GameTerminatedException;
import com.backbase.assignment.kalah.game.exception.IllegalPitNumberException;
import com.backbase.assignment.kalah.game.service.GameService;
import com.backbase.assignment.kalah.game.util.GameConstant;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {
	private static final String URL = "http://<host>:<port>/games/1";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GameService gameService;

	@Test
	void testCreateNewGame() throws Exception {
		GameBoardView newGame = new GameBoardView(Long.valueOf(1), URL);
		BDDMockito.given(gameService.createGame()).willReturn(newGame);

		mockMvc.perform(MockMvcRequestBuilders.post(GameConstant.GAME_API_PATH).header(HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.uri").value(newGame.getUri()));
	}

	@Test
	void testFindById() throws Exception {
		GameView gameView = populateNewGameView();
		BDDMockito.given(gameService.findById(Long.valueOf(1))).willReturn(gameView);

		mockMvc.perform(MockMvcRequestBuilders.get(GameConstant.GAME_API_PATH + "/1").header(HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.uri").value(gameView.getUri()));
	}

	@Test
	void testMakeMove() throws Exception {
		GameView gameView = populateNewGameView();
		BDDMockito.given(gameService.makeMove(Long.valueOf(1), 4)).willReturn(gameView);

		mockMvc.perform(MockMvcRequestBuilders.put(GameConstant.GAME_API_PATH + "/1/pits/4")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.uri").value(gameView.getUri()));
	}

	@Test
	void testMakeMoveWhenIllegalPit() throws Exception {
		BDDMockito.given(gameService.makeMove(Long.valueOf(1), 15))
				.willThrow(new IllegalPitNumberException("Either player or pit index is not valid."));

		mockMvc.perform(MockMvcRequestBuilders.put(GameConstant.GAME_API_PATH + "/1/pits/15")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("Either player or pit index is not valid."));
	}

	@Test
	void testMakeMoveWhenGameNotFund() throws Exception {
		BDDMockito.given(gameService.makeMove(Long.valueOf(1), 4))
				.willThrow(new GameNotFoundException("Game with ID '1' does not exists!"));

		mockMvc.perform(MockMvcRequestBuilders.put(GameConstant.GAME_API_PATH + "/1/pits/4")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value("NOT_FOUND"))
				.andExpect(jsonPath("$.message").value("Game with ID '1' does not exists!"));
	}

	@Test
	void testMakeMoveWhenGameTerminated() throws Exception {
		BDDMockito.given(gameService.makeMove(Long.valueOf(1), 4))
				.willThrow(new GameTerminatedException("Game end with winner 'Player 1 wins!'"));

		mockMvc.perform(MockMvcRequestBuilders.put(GameConstant.GAME_API_PATH + "/1/pits/4")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value("CONFLICT"))
				.andExpect(jsonPath("$.message").value("Game end with winner 'Player 1 wins!'"));
	}

	@Test
	void testMakeMoveWhenExceptionOccurred() throws Exception {
		BDDMockito.given(gameService.makeMove(Long.valueOf(1), 4))
				.willThrow(new IllegalArgumentException("Game instance is marked as nonnull, but value is null.'"));

		mockMvc.perform(MockMvcRequestBuilders.put(GameConstant.GAME_API_PATH + "/1/pits/4")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
				.andExpect(jsonPath("$.message").value("Game instance is marked as nonnull, but value is null.'"));
	}

	GameView populateNewGameView() {
		GameView gameView = new GameView();
		gameView.setId(Long.valueOf(1));
		gameView.setUri(URL);
		gameView.setStatus(IntStream.rangeClosed(1, GameConstant.NUMBER_OF_TOTAL_PITS).boxed()
				.collect(Collectors.toMap(Function.identity(),
						i -> i % (GameConstant.NUMBER_OF_PITS + 1) == 0 ? 0 : GameConstant.NUMBER_OF_STONES_PER_PIT)));
		return gameView;

	}
}
